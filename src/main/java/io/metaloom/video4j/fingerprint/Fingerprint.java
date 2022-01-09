package io.metaloom.video4j.fingerprint;

import io.metaloom.utils.hash.HashUtils;

public interface Fingerprint {

	/**
	 * Version of the fingerprint.
	 * 
	 * @return
	 */
	int version();

	/**
	 * Return the raw binary data of the fingerprint.
	 * 
	 * @return
	 */
	byte[] hash();

	/**
	 * Return the hex representation of the fingerprint.
	 * 
	 * @return
	 */
	default String hex() {
		return HashUtils.bytesToHex(hash());
	}
}
