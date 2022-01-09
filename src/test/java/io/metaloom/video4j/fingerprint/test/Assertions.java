package io.metaloom.video4j.fingerprint.test;

import io.metaloom.video4j.fingerprint.Fingerprint;

public class Assertions extends org.assertj.core.api.Assertions {

	public static FingerprintAssert assertThat(Fingerprint fingerprint) {
		return new FingerprintAssert(fingerprint);
	}

}
