package io.metaloom.video4j.fingerprint;

import org.opencv.core.Mat;

@FunctionalInterface
public interface FPPreviewHandler {

	void update(Mat source, Mat step1, Mat step2, Mat step3, Mat step4, Mat result);

}
