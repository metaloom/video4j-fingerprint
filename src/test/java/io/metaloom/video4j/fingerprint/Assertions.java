package io.metaloom.video4j.fingerprint;

import io.metaloom.video4j.fingerprint.v1.BinaryFingerprint;
import io.metaloom.video4j.fingerprint.v1.BinaryFingerprintAssert;
import io.metaloom.video4j.fingerprint.v2.QuadFingerprint;
import io.metaloom.video4j.fingerprint.v2.QuadFingerprintAssert;

public class Assertions extends org.assertj.core.api.Assertions {

	public static BinaryFingerprintAssert assertThat(BinaryFingerprint fingerprint) {
		return new BinaryFingerprintAssert(fingerprint);
	}

	public static QuadFingerprintAssert assertThat(QuadFingerprint fingerprint) {
		return new QuadFingerprintAssert(fingerprint);
	}

}
