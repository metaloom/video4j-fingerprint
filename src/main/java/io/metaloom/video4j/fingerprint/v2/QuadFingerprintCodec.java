package io.metaloom.video4j.fingerprint.v2;

import static io.metaloom.video4j.fingerprint.v2.QuadFingerprint.FINGERPRINT_VECTOR_SIZE;
import static io.metaloom.video4j.fingerprint.v2.QuadFingerprint.FINGERPRINT_VERSION;

import java.nio.ByteBuffer;
import java.util.BitSet;

import org.opencv.core.Mat;

import io.metaloom.video4j.fingerprint.FingerprintCodec;
import io.metaloom.video4j.fingerprint.InvalidFormatException;
import io.metaloom.video4j.fingerprint.utils.FingerprintUtils;

public class QuadFingerprintCodec implements FingerprintCodec<QuadFingerprint> {

	private static final QuadFingerprintCodec INSTANCE = new QuadFingerprintCodec();

	public static QuadFingerprintCodec instance() {
		return INSTANCE;
	}

	@Override
	public byte[] encode(QuadFingerprint fingerprint) {
		int headerSize = (2 + 1 + 2 + 1);
		ByteBuffer bb = ByteBuffer.allocate(FINGERPRINT_VECTOR_SIZE + headerSize);
		bb.putShort(fingerprint.version());
		bb.put((byte) 0x00);
		bb.putShort(FINGERPRINT_VECTOR_SIZE);
		bb.put((byte) 0xFF);

		// Add the vector data
		BitSet vectorData = FingerprintUtils.transformToBitSet2Bit(fingerprint.getVectorData());
		bb.put(vectorData.toByteArray());
		return bb.array();
	}

	@Override
	public QuadFingerprint decode(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		short version = bb.getShort();
		if (version != FINGERPRINT_VERSION) {
			throw new InvalidFormatException("The provided data contains fingerprint version " + version + " and is incompatible with "
				+ FINGERPRINT_VERSION + " of this library version");
		}
		// Skip next 1 byte
		bb.get();

		short fingerprintSize = bb.getShort();
		assert (FINGERPRINT_VECTOR_SIZE == fingerprintSize);

		// Skip next 1 byte
		bb.get();

		// Load the bitset which holds the vector data
		byte[] bitsetData = new byte[FINGERPRINT_VECTOR_SIZE / 8];
		bb.get(bitsetData);
		short[] vectorData = FingerprintUtils.transformToShort4Bit(BitSet.valueOf(bitsetData), FINGERPRINT_VECTOR_SIZE);
		return new QuadFingerprintImpl(vectorData);
	}

	@Override
	public QuadFingerprint encode(Mat mat) {
		BitSet bits = FingerprintUtils.transform(mat);
		short[] shorts = FingerprintUtils.transformToShort4Bit(bits,8);
		return new QuadFingerprintImpl(shorts);
	}

	@Override
	public float[] toVector(QuadFingerprint fp) {
		short[] data = fp.getVectorData();

		// Convert short to float
		float[] vector = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			vector[i] = data[i];
		}
		return vector;
	}

}
