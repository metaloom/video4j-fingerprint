package io.metaloom.video4j.fingerprint;

import static io.metaloom.video4j.opencv.CVUtils.empty;
import static io.metaloom.video4j.opencv.CVUtils.isBlackFrame;
import static io.metaloom.video4j.opencv.CVUtils.mean;
import static io.metaloom.video4j.opencv.CVUtils.normalize;
import static io.metaloom.video4j.opencv.CVUtils.resize;
import static io.metaloom.video4j.opencv.CVUtils.toGreyScale;

import java.util.BitSet;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import io.metaloom.video4j.Video;
import io.metaloom.video4j.impl.MatProvider;
import io.metaloom.video4j.opencv.CVUtils;

public class VideoFingerprinter {

	private static Logger log = Logger.getLogger(VideoFingerprinter.class.getName());

	private int resXY;

	private double stackFactor;

	private Video video;

	private int len;

	private double skipFactor;

	private int speedUp = 8;

	public static void setLevel(Level targetLevel) {
		Logger root = Logger.getLogger("");
		root.setLevel(targetLevel);
		for (Handler handler : root.getHandlers()) {
			handler.setLevel(targetLevel);
		}
		System.out.println("level set: " + targetLevel.getName());
	}

	/**
	 * Create a new hasher.
	 * 
	 * @param resXY
	 * @param len
	 *            Amount of frames to use for the hash
	 * @param skipFactor
	 *            Percent of video duration which should be skipped
	 * @param stackFactor
	 * @param video
	 *            Video that should be hashed
	 */
	public VideoFingerprinter(int resXY, int len, double skipFactor, double stackFactor, Video video) {
		this.resXY = resXY;
		this.video = video;
		this.len = len;
		this.skipFactor = skipFactor;
		this.stackFactor = stackFactor;
	}

	public byte[] hash() throws InterruptedException {
		return hash(video, null);
	}

	public byte[] hash(FPPreviewHandler handler) throws InterruptedException {
		return hash(video, handler);
	}

	public byte[] hash(Video video, FPPreviewHandler handler) throws InterruptedException {
		log.finest("Starting Test");
		if (!video.isOpen()) {
			throw new RuntimeException("Video has not yet been opened.");
		}

		int frameNo = 0;
		int useableFrameNo = 0;

		video.seekToFrameRatio(skipFactor);
		// System.out.println("Start reading");
		Mat frame = MatProvider.mat();
		Mat stack = null;
		Mat finalStep = null;
		while (true) {
			if (!video.frame(frame)) {
				break;
			}

			frameNo++;
			if (frameNo % speedUp == 0) {
				continue;
			}
			useableFrameNo++;
			if (useableFrameNo >= len) {
				break;
			}
			if (stack == null) {
				stack = empty(frame);
			}
			Mat step1 = empty(frame);

			// 1. Resize
			resize(frame, step1, 512, 512);

			// 2. To greyscale
			toGreyScale(step1, step1);
			normalize(step1, step1, 0, 205);

			// 3. Skip black frames
			if (isBlackFrame(step1)) {
				continue;
			}

			// 3. Increase contrast
			// increaseContrast(step1, step1, contrastAlpha, 0);

			// 4. Resize
			Imgproc.blur(step1, step1, new Size(15.1f, 15.1f));
			resize(step1, step1, resXY, resXY);

			// 5. Normalize
			Mat step2 = empty(frame);
			normalize(step1, step2, 0, 255);
			CVUtils.free(step1);

			// 6. Stack the image
			double factor = (1.0d / (double) len * stackFactor);
			Mat oldStack = stack;
			stack = stackImage(stack, step2, factor);

			Mat step3 = stack.clone();
			// Core.normalize(reduce, reduce, 125.0, 255.0, Core.NORM_MINMAX);

			resize(step3, step3, resXY, resXY);
			// increaseContract(step3, step3, 1, 23);
			// Imgproc.threshold(reduce2, reduce2, 0, 255f, Imgproc.THRESH_BINARY);

			Mat step4 = empty(step3);
			normalize(step3, step4, 0, 255);

			finalStep = toBinary(step4, handler);

			if (handler != null) {
				handler.update(frame, step1, step2, step3, step4, finalStep);
			}
			CVUtils.free(oldStack);
			CVUtils.free(step2);
			CVUtils.free(step3);
			CVUtils.free(step4);
		}
		CVUtils.free(frame);
		if (finalStep == null) {
			return null;
		}
		byte[] hash = computeHash(finalStep);
		CVUtils.free(finalStep);
		CVUtils.free(stack);
		return hash;
	}

	private Mat toBinary(Mat src, FPPreviewHandler handler) {
		Mat result = src.clone();
		double meanV = mean(src);
		Imgproc.threshold(src, result, meanV, 255f, Imgproc.THRESH_BINARY);
		return result;
	}

	public void thresh(Mat frame) {
		for (int x = 0; x < frame.width(); x++) {
			for (int y = 0; y < frame.height(); y++) {
				double[] value = frame.get(x, y);
				boolean bit = value[0] > 128.0f ? true : false;
				if (bit) {
					frame.put(x, y, 0);
				} else {
					frame.put(x, y, 255);
				}
			}
		}
	}

	private byte[] computeHash(Mat frame) {
		BitSet bitset = new BitSet();
		int bitNo = 0;
		for (int x = 0; x < frame.width(); x++) {
			for (int y = 0; y < frame.height(); y++) {
				double[] value = frame.get(x, y);
				boolean bit = value[0] > 128.0f ? true : false;
				bitset.set(bitNo, bit);
				// System.out.println(bitNo + " = " + bit);
				bitNo++;
			}
		}
		bitset.set(256, true);
		// System.out.println("Bits: " + bitNo + " " + bitset.length());
		return bitset.toByteArray();
	}

	private static Mat stackImage(Mat stack, Mat frame, double factor) {
		if (stack == null) {
			return empty(frame);
		}
		Mat result = empty(frame);
		for (int x = 0; x < frame.width(); x++) {
			for (int y = 0; y < frame.height(); y++) {
				// double[] values = new double[3];
				// stack.getData().getPixel(x, y, values);
				// System.out.println(values[0]);
				double stackValue = stack.get(x, y)[0];
				double frameValue = frame.get(x, y)[0];
				double mixedValue = stackValue;
				mixedValue = (stackValue + (frameValue * factor));
				// System.out.println(mixedValue + " = " + stackValue + " + " + (frameValue *
				// factor) + " (" + factor + ")");
				result.put(x, y, mixedValue);
			}
		}
		return result;
	}

	// public void setContrastAlpha(double contrastAlpha) {
	// this.contrastAlpha = contrastAlpha;
	// }

	public void setStackFactor(double factor) {
		this.stackFactor = factor;
	}

	public void setSkipFactor(double skipFactor) {
		this.skipFactor = skipFactor;
	}

}
