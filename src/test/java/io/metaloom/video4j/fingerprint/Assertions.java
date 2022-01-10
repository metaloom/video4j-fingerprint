package io.metaloom.video4j.fingerprint;

import io.metaloom.video4j.fingerprint.v1.BinaryFingerprint;
import io.metaloom.video4j.fingerprint.v1.BinaryFingerprintAssert;

public class Assertions extends org.assertj.core.api.Assertions {

	public static BinaryFingerprintAssert assertThat(BinaryFingerprint fingerprint) {
		return new BinaryFingerprintAssert(fingerprint);
	}

}
