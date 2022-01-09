package io.metaloom.video4j.fingerprint;

import java.util.BitSet;

import org.opencv.core.Mat;

import io.metaloom.utils.hash.HashUtils;
import io.metaloom.video4j.fingerprint.impl.DefaultFingerprintCodec;

public interface Fingerprint {

	public static final short DEFAULT_FINGERPRINT_SIZE = 16 * 16;

	public static final short FINGERPRINT_VERSION_V1 = 1;

	/**
	 * Version of the fingerprint.
	 * 
	 * @return
	 */
	short version();

	/**
	 * Return the fingerprint data.
	 * 
	 * @return
	 */
	byte[] array();

	/**
	 * Return the set bits for the fingerprint.
	 * 
	 * @return
	 */
	BitSet bits();

	/**
	 * Return the hex representation of the fingerprint.
	 * 
	 * @return
	 */
	default String hex() {
		return HashUtils.bytesToHex(array());
	}

	static Fingerprint of(Mat mat) {
		return DefaultFingerprintCodec.instance().encode(mat);
	}

	static Fingerprint of(byte[] data) {
		return DefaultFingerprintCodec.instance().decode(data);
	}

}
