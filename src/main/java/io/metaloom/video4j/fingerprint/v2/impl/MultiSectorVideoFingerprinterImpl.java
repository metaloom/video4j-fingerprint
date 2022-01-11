package io.metaloom.video4j.fingerprint.v2.impl;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.video4j.Video;
import io.metaloom.video4j.fingerprint.AbstractVideoFingerprinter;
import io.metaloom.video4j.fingerprint.PreviewHandler;
import io.metaloom.video4j.fingerprint.v2.MultiSectorFingerprint;
import io.metaloom.video4j.fingerprint.v2.MultiSectorVideoFingerprinter;
import io.metaloom.video4j.opencv.CVUtils;

public class MultiSectorVideoFingerprinterImpl extends AbstractVideoFingerprinter<MultiSectorFingerprint> implements MultiSectorVideoFingerprinter {

	private static Logger log = LoggerFactory.getLogger(MultiSectorVideoFingerprinterImpl.class.getName());

	public static int hashSize = 16;
	public static int len = 30 * 3;
	public static double stackFactor = 1.30955d;

	public MultiSectorVideoFingerprinterImpl() {
		super(hashSize, len, stackFactor);
	}


	@Override
	public MultiSectorFingerprint hash(Video video, PreviewHandler handler) {
		if (log.isDebugEnabled()) {
			log.debug("Start hashing of " + video.path());
		}
		Mat stack = null;
		try {
			stack = computeImageStack(video, 0.35f, handler);
			MultiSectorFingerprint fp = createFingerprint(stack);
			return fp;
		} finally {
			CVUtils.free(stack);
		}
	}

	@Override
	protected MultiSectorFingerprint createFingerprint(Mat mat) {
		return MultiSectorFingerprint.CODEC.encode(mat);
	}
}
