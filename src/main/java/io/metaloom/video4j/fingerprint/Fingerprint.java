package io.metaloom.video4j.fingerprint;

import io.metaloom.utils.hash.HashUtils;

public interface Fingerprint {

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
	 * Return the hex representation of the fingerprint.
	 * 
	 * @return
	 */
	default String hex() {
		return HashUtils.bytesToHex(array());
	}

	/**
	 * Convert the fingerprint into a vector of floats.
	 * 
	 * @return
	 */
	float[] vector();

}
