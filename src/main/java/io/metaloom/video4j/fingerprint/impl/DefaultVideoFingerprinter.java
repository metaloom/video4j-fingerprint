package io.metaloom.video4j.fingerprint.impl;

import org.opencv.core.Mat;

import io.metaloom.video4j.fingerprint.AbstractVideoFingerprinter;
import io.metaloom.video4j.fingerprint.Fingerprint;

public class DefaultVideoFingerprinter extends AbstractVideoFingerprinter {

	public static int hashSize = 16;
	public static int len = 30 * 3;
	public static double skipFactor = 0.35f;
	public static double stackFactor = 1.30955d;

	public static final DefaultFingerprintCodec CODEC = DefaultFingerprintCodec.instance();

	public DefaultVideoFingerprinter() {
		super(hashSize, len, skipFactor, stackFactor);
	}

	@Override
	protected Fingerprint createFingerprint(Mat mat) {
		return CODEC.encode(mat);
	}
}
