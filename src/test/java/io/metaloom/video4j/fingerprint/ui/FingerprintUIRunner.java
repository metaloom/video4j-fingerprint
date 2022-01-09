package io.metaloom.video4j.fingerprint.ui;

import java.io.IOException;

import io.metaloom.video4j.Video;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.fingerprint.AbstractVideoTest;
import io.metaloom.video4j.fingerprint.impl.DefaultVideoFingerprinter;

/**
 * Starts a small UI which shows the hasher process in action
 */
public class FingerprintUIRunner extends AbstractVideoTest {

	public static int blowupSize = 128;

	public static void main(String[] args) throws IOException, InterruptedException {
		Video4j.init();
		String moviePath = BBB_SMALL;
		DefaultVideoFingerprinter hasher = new DefaultVideoFingerprinter();
		FingerprintDebugUI debugUi = new FingerprintDebugUI(blowupSize, hasher);

		Video video = Videos.open(moviePath);
		debugUi.add(video);
		debugUi.show();
		System.out.println("Press enter to terminate");
		System.in.read();
		video.close();
	}
}
