package io.metaloom.video4j.fingerprint.v1;

import org.opencv.core.Mat;

import io.metaloom.video4j.fingerprint.AbstractVideoFingerprinter;

public class BinaryVideoFingerprinter extends AbstractVideoFingerprinter<BinaryFingerprint> {

	public static int hashSize = 16;
	public static int len = 30 * 3;
	public static double skipFactor = 0.35f;
	public static double stackFactor = 1.30955d;

	public static final BinaryFingerprintCodec CODEC = BinaryFingerprintCodec.instance();

	public BinaryVideoFingerprinter() {
		super(hashSize, len, skipFactor, stackFactor);
	}

	@Override
	protected BinaryFingerprint createFingerprint(Mat mat) {
		return CODEC.encode(mat);
	}
}
