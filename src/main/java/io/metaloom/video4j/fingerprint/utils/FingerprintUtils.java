package io.metaloom.video4j.fingerprint.utils;

import java.util.Arrays;
import java.util.BitSet;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FingerprintUtils {

	public static final Logger log = LoggerFactory.getLogger(FingerprintUtils.class);

	private FingerprintUtils() {
	}

	/**
	 * Transform the {@link Mat} data into a bitset which contains the bw pixel data.
	 * 
	 * @param frame
	 * @return
	 */
	public static BitSet transform(Mat frame) {
		BitSet bitset = new BitSet();
		int bitNo = 0;
		if (log.isTraceEnabled()) {
			log.trace("Converting mat [" + frame.width() + " x " + frame.height() + "] to bitset.");
		}
		for (int x = 0; x < frame.width(); x++) {
			for (int y = 0; y < frame.height(); y++) {
				double[] value = frame.get(x, y);
				boolean bit = value[0] > 128.0f ? true : false;
				bitset.set(bitNo, bit);
				bitNo++;
			}
		}
		return bitset;
	}

	/**
	 * Compute the levenshtein distance between both strings.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static int levenshteinDistance(String x, String y) {
		int[][] dp = new int[x.length() + 1][y.length() + 1];

		for (int i = 0; i <= x.length(); i++) {
			for (int j = 0; j <= y.length(); j++) {
				if (i == 0) {
					dp[i][j] = j;
				} else if (j == 0) {
					dp[i][j] = i;
				} else {
					dp[i][j] = min(dp[i - 1][j - 1]
						+ costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
						dp[i - 1][j] + 1,
						dp[i][j - 1] + 1);
				}
			}
		}

		return dp[x.length()][y.length()];
	}

	private static int costOfSubstitution(char a, char b) {
		return a == b ? 0 : 1;
	}

	private static int min(int... numbers) {
		return Arrays.stream(numbers)
			.min().orElse(Integer.MAX_VALUE);
	}

	public static void debugBin(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			for (int b = 7; b >= 0; --b) {
				sb.append(data[i] >>> b & 1);
			}
		}
		log.trace(sb.toString());
	}

}
