package io.metaloom.video4j.fingerprint.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

import io.metaloom.video4j.Video;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.fingerprint.AbstractMediaTest;
import io.metaloom.video4j.fingerprint.utils.FingerprintUtils;
import io.metaloom.video4j.fingerprint.v1.impl.BinaryVideoFingerprinterImpl;
import io.metaloom.video4j.impl.MatProvider;

public class BinaryVideoFingerprinterTest extends AbstractMediaTest {

	public static int blowupSize = 128;

	@Test
	public void runHasher() throws InterruptedException, IOException {
		BinaryVideoFingerprinter hasher = new BinaryVideoFingerprinterImpl();
		String hash1, hash2, hash3;
		try (Video video1 = Videos.open(BBB_SMALL)) {
			hash1 = hasher.hash(video1).hex();
		}
		try (Video video2 = Videos.open(BBB_MEDIUM)) {
			hash2 = hasher.hash(video2).hex();
		}
		try (Video video3 = Videos.open(BBB_LARGE)) {
			hash3 = hasher.hash(video3).hex();
		}

		System.out.println(hash1);
		System.out.println(hash2);
		System.out.println(hash3);
		// Assert that only a few bits differ
		assertEquals(3, FingerprintUtils.levenshteinDistance(hash1, hash2));
		assertEquals(3, FingerprintUtils.levenshteinDistance(hash1, hash3));
		assertEquals(0, FingerprintUtils.levenshteinDistance(hash2, hash3));

		// Verify that no leaks occur
		MatProvider.printLeaks();
		assertFalse("There should not be any leaked mats", MatProvider.hasLeaks());

	}
}
