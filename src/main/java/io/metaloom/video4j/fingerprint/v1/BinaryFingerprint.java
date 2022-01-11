package io.metaloom.video4j.fingerprint.v1;

import java.util.BitSet;

import org.opencv.core.Mat;

import io.metaloom.video4j.fingerprint.Fingerprint;

/**
 * Fingerprint which relies on using a bit-wise mapping of its fp data to the vector output.
 */
public interface BinaryFingerprint extends Fingerprint {

	public static final short FINGERPRINT_VERSION = 1;

	public static final short FINGERPRINT_VECTOR_SIZE = 16 * 16;
	
	public static final BinaryFingerprintCodec CODEC= BinaryFingerprintCodec.instance();

	default short version() {
		return FINGERPRINT_VERSION;
	}

	/**
	 * Return the set bits for the fingerprint.
	 * 
	 * @return
	 */
	BitSet bits();

	static BinaryFingerprint of(Mat mat) {
		return CODEC.encode(mat);
	}

	static BinaryFingerprint of(byte[] data) {
		return CODEC.decode(data);
	}

	static BinaryFingerprint of(String hex) {
		return CODEC.decode(hex);
	}

}
