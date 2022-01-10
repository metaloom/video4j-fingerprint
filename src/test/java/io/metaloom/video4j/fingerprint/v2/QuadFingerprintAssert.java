package io.metaloom.video4j.fingerprint.v2;

import org.assertj.core.api.AbstractAssert;
import org.opencv.core.Mat;

import io.metaloom.video4j.fingerprint.v2.QuadFingerprint;

public class QuadFingerprintAssert extends AbstractAssert<QuadFingerprintAssert, QuadFingerprint> {

	public QuadFingerprintAssert(QuadFingerprint actual) {
		super(actual, QuadFingerprintAssert.class);
	}

	public QuadFingerprintAssert matches(Mat mat) {
		return this;
	}

	public QuadFingerprintAssert matches(String hex) {
		return this;
	}

	public QuadFingerprintAssert matches(byte[] array) {
		return this;
	}

}
