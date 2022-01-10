package io.metaloom.video4j.fingerprint.v2;

public class QuadFingerprintImpl implements QuadFingerprint {

	private static final QuadFingerprintCodec CODEC = QuadFingerprintCodec.instance();

	private byte[] vectorData;

	public QuadFingerprintImpl(byte[] vectorData) {
		this.vectorData = vectorData;
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
	public byte[] getVectorData() {
		return vectorData;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("\n");
		b.append("Version: " + version() + "\n");
		int r = 0;
		b.append("Vector:\n");
		b.append("---\n");
		float[] vector = vector();
		for (int n = 0; n < vector.length; n++) {
			b.append(String.format("%.2f", vector[n]) + " ");
			if (++r % 32 == 0) {
				b.append("\n");
			}
		}
		b.append("---\n");
		b.append("\n");
		return b.toString();
	}

}
