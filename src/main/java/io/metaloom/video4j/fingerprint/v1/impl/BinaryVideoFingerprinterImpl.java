package io.metaloom.video4j.fingerprint.v1.impl;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.video4j.Video;
import io.metaloom.video4j.fingerprint.AbstractVideoFingerprinter;
import io.metaloom.video4j.fingerprint.PreviewHandler;
import io.metaloom.video4j.fingerprint.v1.BinaryFingerprint;
import io.metaloom.video4j.fingerprint.v1.BinaryFingerprintCodec;
import io.metaloom.video4j.fingerprint.v1.BinaryVideoFingerprinter;
import io.metaloom.video4j.opencv.CVUtils;

public class BinaryVideoFingerprinterImpl extends AbstractVideoFingerprinter<BinaryFingerprint> implements BinaryVideoFingerprinter {

	private static Logger log = LoggerFactory.getLogger(BinaryVideoFingerprinterImpl.class.getName());

	public static int hashSize = 16;
	public static int len = 30 * 3;
	public static double stackFactor = 1.30955d;

	private double skipFactor = 0.35f;

	public static final BinaryFingerprintCodec CODEC = BinaryFingerprintCodec.instance();

	public BinaryVideoFingerprinterImpl() {
		super(hashSize, len, stackFactor);
	}

	@Override
	protected BinaryFingerprint createFingerprint(Mat mat) {
		return CODEC.encode(mat);
	}

	@Override
	public BinaryFingerprint hash(Video video) {
		return hash(video, null);
	}

	@Override
	public BinaryFingerprint hash(Video video, PreviewHandler handler) {
		if (log.isDebugEnabled()) {
			log.debug("Start hashing of " + video.path());
		}
		Mat stack = null;
		try {
			stack = computeImageStack(video, skipFactor, handler);
			BinaryFingerprint fp = createFingerprint(stack);
			return fp;
		} finally {
			CVUtils.free(stack);
		}
	}

	@Override
	public void setSkipFactor(double skipFactor) {
		this.skipFactor = skipFactor;
	}

}
