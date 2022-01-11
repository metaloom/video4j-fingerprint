package io.metaloom.video4j.fingerprint.v2.impl;

import static io.metaloom.video4j.opencv.CVUtils.normalize;

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

	/**
	 * Defines the output size of the final {@link Mat} that gets generated.
	 */
	public static int hashSize = 16;

	/**
	 * Defines how many content should be used to generate the fingerprint (per sector).
	 */
	public static int len = 30 * 3;

	/**
	 * Defines a factor which will impact how much effect will different frames in the stacking process will have.
	 */
	public static double stackFactor = 1.30955d;

	/**
	 * Amount of sectors that should be generated to influence the fingerprint.
	 * 
	 * Increasing the sector count will choose more areas from the video to use the stacking process on.
	 */
	public static int sectorCount = 4;

	/**
	 * Defines the skip factor offset per sector.
	 */
	public double skipFactor = 1f / sectorCount - 0.1f;

	public MultiSectorVideoFingerprinterImpl() {
		super(hashSize, len, stackFactor);
	}

	@Override
	public MultiSectorFingerprint hash(Video video, PreviewHandler handler) {
		if (log.isDebugEnabled()) {
			log.debug("Start hashing of " + video.path());
			log.debug("Using skip factor: " + String.format("%.2f", skipFactor));
		}

		Mat multiSectorStack = null;
		Mat binaryResult = null;
		try {
			for (int i = 1; i <= sectorCount; i++) {
				Mat stack = null;
				try {
					// Determine the effective skip factor for this sector
					double factor = i * skipFactor;
					if (factor >= 1) {
						if (log.isWarnEnabled()) {
							log.warn("The effective skkip factor exceeded 1. Aborting stacking after " + i + " runs.");
						}
						break;
					}
					stack = computeImageStack(video, factor, handler);
					if (stack == null) {
						if (log.isWarnEnabled()) {
							log.warn("Stack for factor " + factor + " yield null. Aborting stacking after " + i + " runs.");
						}
						break;
					}
					if (multiSectorStack == null) {
						multiSectorStack = CVUtils.empty(stack);
					}
					combineStack(multiSectorStack, stack, stackFactor);
					if (handler != null) {
						handler.update(null, null, null, null, null, null, multiSectorStack, null);
					}
				} finally {
					CVUtils.free(stack);
				}
			}

			normalize(multiSectorStack, multiSectorStack, 0, 255);
			binaryResult = toBinary(multiSectorStack, handler);
			if (handler != null) {
				handler.update(null, null, null, null, null, null, multiSectorStack, binaryResult);
			}
			return createFingerprint(multiSectorStack);
		} finally {
			CVUtils.free(multiSectorStack);
			CVUtils.free(binaryResult);
		}
	}

	/**
	 * Stack the provided frame onto the stack.
	 * 
	 * @param result
	 *            Result to which the frame will be added
	 * @param stack
	 *            Frame to be added to the stack
	 * @param factor
	 *            Factor to be used to influence stacking process
	 * @return New stack
	 */
	protected static void combineStack(Mat result, Mat stack, double factor) {

		float threshold = 125f;
		for (int x = 0; x < stack.width(); x++) {
			for (int y = 0; y < stack.height(); y++) {
				// double[] values = new double[3];
				// stack.getData().getPixel(x, y, values);
				// System.out.println(values[0]);
				double stackValue = result.get(x, y)[0];
				if (stackValue > threshold) {
					stackValue = stackValue / 2;
				}
				double frameValue = stack.get(x, y)[0];
				if (frameValue > threshold) {
					frameValue = frameValue / 2;
				}
				double mixedValue = stackValue;
				mixedValue = (stackValue + (frameValue * factor));
				// System.out.println(mixedValue + " = " + stackValue + " + " + (frameValue * factor) + " (" + factor + ")");
				result.put(x, y, mixedValue);
			}
		}
	}

	@Override
	protected MultiSectorFingerprint createFingerprint(Mat mat) {
		return MultiSectorFingerprint.CODEC.encode(mat);
	}

	@Override
	public void setSkipFactor(double factor) {
		this.skipFactor = factor;
	}
}
