package io.metaloom.video4j.fingerprint;

import io.metaloom.video4j.Video;

public class DefaultVideoFingerprinter extends VideoFingerprinter {

	public static int hashSize = 16;
	public static int len = 30 * 3;
	public static double skipFactor = 0.35f;
	public static double stackFactor = 1.30955d;

	public DefaultVideoFingerprinter(Video video) {
		super(hashSize, len, skipFactor, stackFactor, video);
	}
}
