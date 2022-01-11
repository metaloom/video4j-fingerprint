package io.metaloom.video4j.fingerprint.v2.impl;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.video4j.Video;
import io.metaloom.video4j.fingerprint.AbstractVideoFingerprinter;
import io.metaloom.video4j.fingerprint.PreviewHandler;
import io.metaloom.video4j.fingerprint.v1.BinaryFingerprint;
import io.metaloom.video4j.fingerprint.v2.MultiSectorVideoFingerprinter;

public class MultiSectorVideoFingerprinterImpl extends AbstractVideoFingerprinter<BinaryFingerprint> implements MultiSectorVideoFingerprinter {

	private static Logger log = LoggerFactory.getLogger(MultiSectorVideoFingerprinterImpl.class.getName());

	public static int hashSize = 16;
	public static int len = 30 * 3;
	public static double stackFactor = 1.30955d;

	public MultiSectorVideoFingerprinterImpl() {
		super(hashSize, len, stackFactor);
	}

	@Override
	public BinaryFingerprint hash(Video video) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BinaryFingerprint hash(Video video, PreviewHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BinaryFingerprint createFingerprint(Mat mat) {
		// TODO Auto-generated method stub
		return null;
	}
}
