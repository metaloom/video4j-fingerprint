package io.metaloom.video4j.fingerprint.v2;

import org.opencv.core.Mat;

import io.metaloom.video4j.fingerprint.AbstractVideoFingerprinter;

public class QuadVideoFingerprinter extends AbstractVideoFingerprinter<QuadFingerprint> {

	public static int hashSize = 16;
	public static int len = 30 * 3;
	public static double skipFactor = 0.35f;
	public static double stackFactor = 1.30955d;

	public static final QuadFingerprintCodec CODEC = QuadFingerprintCodec.instance();

	public QuadVideoFingerprinter() {
		super(hashSize, len, skipFactor, stackFactor);
	}

	@Override
	protected QuadFingerprint createFingerprint(Mat mat) {
		return CODEC.encode(mat);
	}

}
