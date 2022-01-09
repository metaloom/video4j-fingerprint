package io.metaloom.video4j.fingerprint;

import static io.metaloom.video4j.fingerprint.test.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.opencv.core.Mat;

public class FingerprintCodecTest extends AbstractVideoTest {

	@Test
	public void testEmpty() {
		Mat mat = emptyMat(16, 16);
		Fingerprint fp = Fingerprint.of(mat);
		assertThat(fp).matches(mat).matches(fp.hex()).matches(fp.array());
	}

	@Test
	public void testWithLastPixel() throws IOException {
		Mat mat = lastPixelMat();
		Fingerprint fp = Fingerprint.of(mat);
		assertThat(fp).matches(mat).matches(fp.hex()).matches(fp.array());
	}

	@Test
	public void testWithFirstPixel() throws IOException, InterruptedException {
		Mat mat = firstPixelMat();
		Fingerprint fp = Fingerprint.of(mat);
		debug(fp.array());
		assertThat(fp).matches(mat).matches(fp.hex()).matches(fp.array());
	}

	@Test(expected = InvalidFormatException.class)
	public void testOldFingerprint() {
		String hex = "fefff4fdf4f9bcf980f800f840fa407e003e581efe1ffe1fff330e000000000001";
		Fingerprint.of(hex);
	}

	@Test
	public void testRandomMat() {
		for (int n = 0; n < 10; n++) {
			Mat mat = randomMat();
			Fingerprint fp = Fingerprint.of(mat);
			System.out.println(fp.hex());
			assertThat(fp).matches(mat).matches(fp.hex()).matches(fp.array());
		}
	}

}
