package io.metaloom.video4j.fingerprint.v1;

import java.util.BitSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryFingerprintImpl implements BinaryFingerprint {

	public static final Logger log = LoggerFactory.getLogger(BinaryFingerprintImpl.class);

	public static final BinaryFingerprintCodec CODEC = BinaryFingerprintCodec.instance();

	private BitSet bits;

	public BinaryFingerprintImpl(BitSet bits) {
		this.bits = bits;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("\n");
		b.append("Version: " + version() + "\n");
		int r = 0;
		b.append("Bits:\n");
		b.append("---\n");
		for (int n = 0; n < FINGERPRINT_VECTOR_SIZE; n++) {
			b.append(bits.get(n) ? "1" : "0");
			if (++r % 32 == 0) {
				b.append("\n");
			}
		}
		b.append("---\n");
		b.append("\n");

		float[] floats = vector();
		r = 0;
		b.append("Vector:\n");
		b.append("----\n");
		for (int i = 0; i < floats.length; i++) {
			float f = floats[i];
			b.append(String.format("%.0f", f) + " ");
			if (++r % 16 == 0) {
				b.append("\n");
			}
		}
		b.append("----\n");
		return b.toString();
	}

	@Override
	public byte[] array() {
		return CODEC.encode(this);
	}

	@Override
	public float[] vector() {
		return CODEC.toVector(this);
	}

	@Override
	public BitSet bits() {
		return bits;
	}

}
