package io.metaloom.video4j.fingerprint.v1;

import static io.metaloom.video4j.fingerprint.v1.BinaryFingerprint.FINGERPRINT_VECTOR_SIZE;
import static io.metaloom.video4j.fingerprint.v1.BinaryFingerprint.FINGERPRINT_VERSION;

import java.nio.ByteBuffer;
import java.util.BitSet;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.video4j.fingerprint.FingerprintCodec;
import io.metaloom.video4j.fingerprint.InvalidFormatException;
import io.metaloom.video4j.fingerprint.utils.FingerprintUtils;

/**
 * Data format:
 * 
 * <pre>
 *  [0-2] - version
 *  [3] - 0
 *  [3-5] - fingerprint size
 *  [6] - 0
 *  [7] fingerprint byte data
 *  …
 *  [256+7] END
 * </pre>
 */
public class BinaryFingerprintCodec implements FingerprintCodec<BinaryFingerprint> {

	public static final Logger log = LoggerFactory.getLogger(BinaryFingerprintCodec.class);

	private static final BinaryFingerprintCodec INSTANCE = new BinaryFingerprintCodec();

	public static BinaryFingerprintCodec instance() {
		return INSTANCE;
	}

	@Override
	public BinaryFingerprint decode(byte[] data) {
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

		byte[] bitsetData = new byte[FINGERPRINT_VECTOR_SIZE / 8];
		bb.get(bitsetData);
		BitSet bits = BitSet.valueOf(bitsetData);
		return new BinaryFingerprintImpl(bits);
	}

	@Override
	public BinaryFingerprint encode(Mat mat) {
		BitSet bits = FingerprintUtils.transform(mat);
		return new BinaryFingerprintImpl(bits);
	}

	@Override
	public byte[] encode(BinaryFingerprint fingerprint) {
		int headerSize = (2 + 1 + 2 + 1);
		ByteBuffer bb = ByteBuffer.allocate(32 + headerSize);
		bb.putShort(fingerprint.version());
		bb.put((byte) 0x00);
		bb.putShort(FINGERPRINT_VECTOR_SIZE);
		bb.put((byte) 0xFF);
		bb.put(fingerprint.bits().toByteArray());
		return bb.array();
	}

	/**
	 * Returns a condensed form of the regular vector.
	 * 
	 * @param fingerprint
	 * @return
	 */
	public float[] toQuadVector(BinaryFingerprint fingerprint) {
		float[] vector = new float[FINGERPRINT_VECTOR_SIZE / 4];
		BitSet bits = fingerprint.bits();
		int bit = 0;
		for (int i = 0; i < FINGERPRINT_VECTOR_SIZE/4; i++) {
			float component = 0;
			for (int r = 0; r < 4; r++) {
				component += bits.get(bit) ? 1f : 0f;
				bit++;
			}
			vector[i] = component;
		}
		return vector;
	}

	@Override
	public float[] toVector(BinaryFingerprint fingerprint) {
		float[] result = new float[FINGERPRINT_VECTOR_SIZE];
		for (int i = 0; i < FINGERPRINT_VECTOR_SIZE; i++) {
			result[i] = fingerprint.bits().get(i) ? 1f : 0f;
		}
		return result;
	}

}
