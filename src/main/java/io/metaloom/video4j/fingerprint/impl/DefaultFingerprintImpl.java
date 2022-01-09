package io.metaloom.video4j.fingerprint.impl;

import io.metaloom.video4j.fingerprint.Fingerprint;

public class DefaultFingerprintImpl implements Fingerprint {

	public static int FINGERPRINT_VERSION = 1;

	private byte[] hash;

	public DefaultFingerprintImpl(byte[] hash) {
		this.hash = hash;
	}

	@Override
	public byte[] hash() {
		return hash;
	}

	@Override
	public int version() {
		return FINGERPRINT_VERSION;
	}

}
