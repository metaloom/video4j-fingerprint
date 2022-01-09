package io.metaloom.video4j.fingerprint;

import java.util.BitSet;

public abstract class AbstractFingerprint implements Fingerprint {

	protected BitSet bits;

	public AbstractFingerprint(BitSet bits) {
		this.bits = bits;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < DEFAULT_FINGERPRINT_SIZE; i++) {
			b.append(bits.get(i) ? "1" : "0");
		}
		return b.toString();
	}

	@Override
	public BitSet bits() {
		return bits;
	}

}
