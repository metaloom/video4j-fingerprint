package io.metaloom.video4j.fingerprint.v2;

import io.metaloom.video4j.Video;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.fingerprint.v1.BinaryFingerprint;
import io.metaloom.video4j.fingerprint.v1.BinaryVideoFingerprinter;

public class UsageDemo {

	public static void main(String[] args) throws InterruptedException {
		Video4j.init();

		// Create a fingerprinter for the video
		BinaryVideoFingerprinter gen = new BinaryVideoFingerprinter();

		// Open the video using the Video4j API
		try (Video video = Videos.open("src/test/resources/Big_Buck_Bunny_720_10s_30MB.mp4")) {

			// Run the actual hashing process
			BinaryFingerprint fingerprint = gen.hash(video);
			String hex = fingerprint.hex();
			// hex = 0001000100ff060006000f002e001d0084000600e40076d172c07c84ffcefffffefff8fffdff
		}
	}
}
