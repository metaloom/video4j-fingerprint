package io.metaloom.video4j.fingerprint.v1;

import static io.metaloom.video4j.fingerprint.Assertions.assertThat;

import org.junit.Test;
import org.opencv.core.Mat;

import io.metaloom.video4j.fingerprint.AbstractFingerprintTest;
import io.metaloom.video4j.fingerprint.InvalidFormatException;

public class BinaryFingerprintTest extends AbstractFingerprintTest<BinaryFingerprint> {

	public static final int EXPECTED_FINGERPRINT_HEX_SIZE = 76;

	public static final int EXPECTED_VECTOR_SIZE = 16 * 16;

	public BinaryFingerprintTest() {
		super(EXPECTED_FINGERPRINT_HEX_SIZE, EXPECTED_VECTOR_SIZE);
	}

	@Override
	protected BinaryFingerprint create(String hex) {
		return BinaryFingerprint.of(hex);
	}

	@Override
	protected BinaryFingerprint create(Mat mat) {
		return BinaryFingerprint.of(mat);
	}

	@Override
	protected void fullAssert(BinaryFingerprint fp, Mat mat) {
		assertThat(fp).matches(mat).matches(fp.hex()).matches(fp.array());
	}
	
	@Test(expected = InvalidFormatException.class)
	public void testIncompatibiltyWithV2() {
		BinaryFingerprint.of("0002000100ff0100000000000000000000000000000000000000000000000000000000000000");
	}

}
