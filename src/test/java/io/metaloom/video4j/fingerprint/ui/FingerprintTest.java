package io.metaloom.video4j.fingerprint.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

import io.metaloom.utils.hash.HashUtils;
import io.metaloom.video4j.fingerprint.AbstractVideoTest;
import io.metaloom.video4j.Video;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.fingerprint.DefaultVideoFingerprinter;
import io.metaloom.video4j.fingerprint.FingerprintUtils;
import io.metaloom.video4j.impl.MatProvider;

public class FingerprintTest extends AbstractVideoTest {

	public static int blowupSize = 128;

	@Test
	public void runHasher() throws InterruptedException, IOException {
		String hash1, hash2, hash3;
		try (Video video1 = Videos.open(BBB_SMALL)) {
			DefaultVideoFingerprinter hasher1 = new DefaultVideoFingerprinter(video1);
			hash1 = HashUtils.bytesToHex(hasher1.hash());
		}
		try (Video video2 = Videos.open(BBB_MEDIUM)) {
			DefaultVideoFingerprinter hasher2 = new DefaultVideoFingerprinter(video2);
			hash2 = HashUtils.bytesToHex(hasher2.hash());
		}
		try (Video video3 = Videos.open(BBB_LARGE)) {
			DefaultVideoFingerprinter hasher3 = new DefaultVideoFingerprinter(video3);
			hash3 = HashUtils.bytesToHex(hasher3.hash());
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
