package io.metaloom.video4j.fingerprint.v2;

import static io.metaloom.video4j.fingerprint.Assertions.assertThat;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.opencv.core.Mat;

import io.metaloom.video4j.fingerprint.AbstractFingerprintTest;

public class QuadFingerprintTest extends AbstractFingerprintTest<QuadFingerprint> {

	public static final int EXPECTED_FINGERPRINT_HEX_SIZE = 76;

	public static final int EXPECTED_VECTOR_SIZE = 16 * 16 / 4;

	public QuadFingerprintTest() {
		super(EXPECTED_FINGERPRINT_HEX_SIZE, EXPECTED_VECTOR_SIZE);
	}

	@Override
	protected QuadFingerprint create(String hex) {
		return QuadFingerprint.of(hex);
	}

	@Override
	protected QuadFingerprint create(Mat mat) {
		return QuadFingerprint.of(mat);
	}

	@Override
	protected void fullAssert(QuadFingerprint fp, Mat mat) {
		assertThat(fp).matches(mat).matches(fp.hex()).matches(fp.array());
	}

	@Test
	public void testWithFourPixelsForCompaction() throws IOException, InterruptedException {
		Mat mat = fivePixelMat();
		QuadFingerprint fp = create(mat);
		assertEquals(EXPECTED_FINGERPRINT_HEX_SIZE, fp.hex().length());
		log.debug(fp.toString());
		System.out.println(fp.hex().length());
		System.out.println(fp.hex());
		float[] vector = fp.vector();

		// The vector values should be compacted 
		assertEquals(EXPECTED_VECTOR_SIZE, vector.length);
		assertEquals(3f, vector[0], 0);
		assertEquals(1f, vector[1], 0);
		assertEquals(0f, vector[2], 0);
		fullAssert(fp, mat);
	}
}
