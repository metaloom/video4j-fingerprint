package io.metaloom.video4j.fingerprint.impl;

import java.nio.ByteBuffer;
import java.util.BitSet;

import org.opencv.core.Mat;

import io.metaloom.video4j.fingerprint.Fingerprint;
import io.metaloom.video4j.fingerprint.FingerprintCodec;
import io.metaloom.video4j.fingerprint.utils.FingerprintUtils;

/**
 * Data format:
 * 
 * <pre>
 *  [0-4] - version
 *  [4-8] - 0
 *  [8-12] - fingerprint size
 *  [12-16] - 0
 *  [16] fingerprint byte data
 *  …
 *  [256+3] END
 * </pre>
 */
public class DefaultFingerprintCodec implements FingerprintCodec {

	private static final DefaultFingerprintCodec INSTANCE = new DefaultFingerprintCodec();

	public static DefaultFingerprintCodec instance() {
		return INSTANCE;
	}

	@Override
	public Fingerprint decode(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		short version = bb.getShort();
		if (version != Fingerprint.FINGERPRINT_VERSION_V1) {
			throw new RuntimeException("The provided data contains fingerprint version " + version + " and is incompatible with "
				+ Fingerprint.FINGERPRINT_VERSION_V1 + " of this library version");
		}
		// Skip next 1 byte
		bb.get();

		short fingerprintSize = bb.getShort();
		assert (Fingerprint.DEFAULT_FINGERPRINT_SIZE == fingerprintSize);

		// Skip next 1 byte
		bb.get();

		byte[] bitsetData = new byte[Fingerprint.DEFAULT_FINGERPRINT_SIZE / 8];
		bb.get(bitsetData);
		BitSet bits = BitSet.valueOf(bitsetData);
		return new DefaultFingerprintImpl(bits);
	}

	public Fingerprint encode(Mat mat) {
		BitSet bits = FingerprintUtils.transform(mat);
		return new DefaultFingerprintImpl(bits);
	}

	@Override
	public byte[] encode(Fingerprint fingerprint) {
		ByteBuffer bb = ByteBuffer.allocate(32 + (2+1+2+1));
		bb.putShort(fingerprint.version());
		bb.put((byte) 0x00);
		bb.putShort(Fingerprint.DEFAULT_FINGERPRINT_SIZE);
		bb.put((byte) 0xFF);
		bb.put(fingerprint.bits().toByteArray());
		return bb.array();
	}

}
