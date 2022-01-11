package io.metaloom.video4j.fingerprint;

import static io.metaloom.video4j.fingerprint.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.BitSet;

import org.junit.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.video4j.fingerprint.v1.BinaryFingerprint;
import io.metaloom.video4j.opencv.CVUtils;

public abstract class AbstractFingerprintTest<T extends Fingerprint> extends AbstractMediaTest {

	public static Logger log = LoggerFactory.getLogger(AbstractFingerprintTest.class);

	public static final String HASH_TRUNCATED = "038008e00ef0bf";

	private int expectedHexLen;

	private int expectedVectorSize;

	public AbstractFingerprintTest(int expectedHexLen, int expectedVectorSize) {
		this.expectedHexLen = expectedHexLen;
		this.expectedVectorSize = expectedVectorSize;
	}

	@Test
	public void testWithFourPixels() throws IOException, InterruptedException {
		Mat mat = fivePixelMat();
		T fp = create(mat);
		fullAssert(fp, mat);
	}

	protected abstract void fullAssert(T fp, Mat mat);

	@Test
	public void testEmpty() {
		Mat mat = emptyMat(16, 16);
		BinaryFingerprint fp = BinaryFingerprint.of(mat);
		assertThat(fp).matches(mat).matches(fp.hex()).matches(fp.array());
	}

	@Test
	public void testWithLastPixel() throws IOException {
		Mat mat = lastPixelMat();
		BinaryFingerprint fp = BinaryFingerprint.of(mat);
		assertThat(fp).matches(mat).matches(fp.hex()).matches(fp.array());
	}

	@Test
	public void testWithFirstPixel() throws IOException, InterruptedException {
		Mat mat = firstPixelMat();
		BinaryFingerprint fp = BinaryFingerprint.of(mat);
		assertEquals(expectedHexLen, fp.hex().length());
		debug(fp.array());
		assertThat(fp).matches(mat).matches(fp.hex()).matches(fp.array());
	}

	@Test
	public void testVector() throws IOException {
		Mat mat = firstPixelMat();
		T fp = create(mat);
		log.debug(fp.toString());
		float[] floats = fp.vector();
		assertEquals("The fingerprint did not contain the expected amount of vectors.", expectedVectorSize, floats.length);
	}

	@Test(expected = InvalidFormatException.class)
	public void testConversionErrorHandling() throws IOException {
		create(HASH_TRUNCATED);
	}

	@Test(expected = NullPointerException.class)
	public void testNull() throws IOException {
		create((String) null);
	}

	@Test(expected = InvalidFormatException.class)
	public void testEmptyHex() throws IOException {
		create("");
	}

	@Test(expected = InvalidFormatException.class)
	public void testNonHex() throws IOException {
		create("Röäeoüpü");
	}

	@Test(expected = InvalidFormatException.class)
	public void testPartialHex() throws IOException {
		create("F");
	}

	@Test(expected = InvalidFormatException.class)
	public void testOldFingerprint() {
		String hex = "fefff4fdf4f9bcf980f800f840fa407e003e581efe1ffe1fff330e000000000001";
		create(hex);
	}

	@Test
	public void testRandomMat() {
		for (int n = 0; n < 10; n++) {
			Mat mat = randomMat();
			T fp = create(mat);
			System.out.println(fp.hex());
			fullAssert(fp, mat);
		}
	}

	@Test
	public void testWithFourPixelsForCompaction() throws IOException, InterruptedException {
		Mat mat = fivePixelMat();
		T fp = create(mat);
		log.debug(fp.toString());
		System.out.println(fp.hex().length());
		System.out.println(fp.hex());
		float[] vector = fp.quadVector();

		// The vector values should be compacted
		assertEquals(expectedVectorSize / 4, vector.length);
		assertEquals(4f, vector[0], 0);
		assertEquals(1f, vector[1], 0);
		assertEquals(0f, vector[2], 0);
		fullAssert(fp, mat);
	}

	protected Mat lastPixelMat() {
		Mat mat = emptyMat(16, 16);
		mat.put(15, 15, 255f);
		return mat;
	}

	protected Mat firstPixelMat() {
		Mat mat = emptyMat(16, 16);
		mat.put(0, 0, 255f);
		return mat;
	}

	protected Mat fivePixelMat() {
		// 1111 1000...
		// 15 8
		Mat mat = emptyMat(16, 16);
		mat.put(0, 0, 255f);
		mat.put(0, 1, 255f);
		mat.put(0, 2, 255f);
		mat.put(0, 3, 255f);
		mat.put(0, 4, 255f);
		return mat;
	}

	protected Mat emptyMat(int w, int h) {
		Mat mat = new Mat(w, h, CvType.CV_8UC1);
		CVUtils.clear(mat, 0f);
		return mat;
	}

	protected Mat randomMat() {
		Mat mat = new Mat(16, 16, CvType.CV_8UC1);
		for (int x = 0; x < mat.width(); x++) {
			for (int y = 0; y < mat.height(); y++) {
				boolean b = Math.random() > 0.5f;
				mat.put(x, y, b ? 255f : 0f);
			}
		}
		return mat;
	}

	public void debug(byte[] data) {
		BitSet bits = BitSet.valueOf(data);
		StringBuffer b = new StringBuffer();
		int r = 0;
		for (int n = 0; n < 900; n++) {
			b.append(bits.get(n) ? "1" : "0");
			if (++r % 8 == 0) {
				b.append(" ");
			}
			if (r % 32 == 0) {
				b.append("\n");
			}
		}
		log.debug("Data:\n" + b.toString());
	}

	protected abstract T create(String hex);

	protected abstract T create(Mat mat);

}
