package io.metaloom.video4j.fingerprint.v2;

import org.opencv.core.Mat;

import io.metaloom.video4j.fingerprint.Fingerprint;

/**
 * Alternative experimental fingerprint format which reduces vector size and fingerprint size.
 */
public interface QuadFingerprint extends Fingerprint {

	public static final short FINGERPRINT_VERSION = 2;

	public static final QuadFingerprintCodec CODEC = QuadFingerprintCodec.instance();

	/**
	 * Size of the actual pixels which the fingerprint will process
	 */
	public static final int FINGERPRINT_INPUT_MAT_SIZE = 16 * 16;

	/**
	 * The amount of components / floats which the vector of the fingerprint stores.
	 */
	public static final short FINGERPRINT_VECTOR_SIZE = FINGERPRINT_INPUT_MAT_SIZE / 4;

	default short version() {
		return FINGERPRINT_VERSION;
	}

	static QuadFingerprint of(Mat mat) {
		return CODEC.encode(mat);
	}

	static QuadFingerprint of(byte[] data) {
		return CODEC.decode(data);
	}

	static QuadFingerprint of(String hex) {
		return CODEC.decode(hex);
	}

	/**
	 * Return the raw vector data.
	 * 
	 * @return
	 */
	short[] getVectorData();

}
