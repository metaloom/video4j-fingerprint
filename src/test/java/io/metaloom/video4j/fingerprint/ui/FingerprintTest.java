package io.metaloom.video4j.fingerprint.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

import io.metaloom.video4j.Video;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.fingerprint.AbstractVideoTest;
import io.metaloom.video4j.fingerprint.impl.DefaultVideoFingerprinter;
import io.metaloom.video4j.fingerprint.utils.FingerprintUtils;
import io.metaloom.video4j.impl.MatProvider;

public class FingerprintTest extends AbstractVideoTest {

	public static int blowupSize = 128;

	@Test
	public void runHasher() throws InterruptedException, IOException {
		DefaultVideoFingerprinter hasher = new DefaultVideoFingerprinter();
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

		// Assert that only a few bits differ
		assertEquals(3, FingerprintUtils.levenshteinDistance(hash1, hash2));
		assertEquals(3, FingerprintUtils.levenshteinDistance(hash1, hash3));
		assertEquals(0, FingerprintUtils.levenshteinDistance(hash2, hash3));

		// Verify that no leaks occur
		MatProvider.printLeaks();
		assertFalse("There should not be any leaked mats", MatProvider.hasLeaks());

	}
}
