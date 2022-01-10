package io.metaloom.video4j.fingerprint.v1;

import static io.metaloom.video4j.fingerprint.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.opencv.core.Mat;

import io.metaloom.video4j.fingerprint.AbstractFingerprintTest;

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

	@Test
	public void testWithFourPixelsForCompaction() throws IOException, InterruptedException {
		Mat mat = fivePixelMat();
		BinaryFingerprint fp = create(mat);
		log.debug(fp.toString());
		System.out.println(fp.hex().length());
		System.out.println(fp.hex());
		float[] vector = BinaryFingerprintCodec.instance().toQuadVector(fp);

		// The vector values should be compacted
		assertEquals(EXPECTED_VECTOR_SIZE / 4, vector.length);
		assertEquals(4f, vector[0], 0);
		assertEquals(1f, vector[1], 0);
		assertEquals(0f, vector[2], 0);
		fullAssert(fp, mat);
	}
}
