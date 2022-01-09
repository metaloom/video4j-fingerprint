package io.metaloom.video4j.fingerprint.impl;

import java.util.BitSet;

import io.metaloom.video4j.fingerprint.AbstractFingerprint;

/**
 * Default fingerprint implementation.
 */
public class DefaultFingerprintImpl extends AbstractFingerprint {

	public DefaultFingerprintImpl(BitSet bits) {
		super(bits);
	}

	@Override
	public short version() {
		return FINGERPRINT_VERSION_V1;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Version: " + version() + "\n");
		int r = 0;
		for (int n = 0; n < DEFAULT_FINGERPRINT_SIZE; n++) {
			b.append(bits.get(n) ? "1" : "0");
			if (++r % 32 == 0) {
				b.append("\n");
			}
		}
		return b.toString();
	}

	@Override
	public byte[] array() {
		return DefaultFingerprintCodec.instance().encode(this);
	}

}
