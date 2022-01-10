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

	public static short[] transformToShort4Bit(BitSet bits, int len) {
		short[] result = new short[len];
		int nBit = 0;
		for (int i = 0; i < result.length; i++) {
			byte number = 0;
			number = (byte) (number >> 2);
			// for (int r = 0; r < 4; r++) {
			// number = bits.get(nBit++) ? 1:0;
			// }

			System.out.println("ER: " + toShort4Bit(bits, 9));
			result[i] = (short) number;
		}
		return result;
	}

	/**
	 * Convert the short array into a bitset with reduces precision (2bit)
	 * 
	 * @param numbers
	 * @return
	 */
	public static BitSet transformToBitSet2Bit(short[] numbers) {
		BitSet data = new BitSet();
		int nBit = 0;
		for (int i = 0; i < numbers.length; i++) {
			short number = numbers[i];
			switch (number) {
			case 0:
				data.set(nBit++, false);
				data.set(nBit++, false);
				break;
			case 1:
				data.set(nBit++, false);
				data.set(nBit++, true);
				break;
			case 2:
				data.set(nBit++, true);
				data.set(nBit++, false);
				break;
			case 3:
				data.set(nBit++, true);
				data.set(nBit++, true);
				break;
			default:
				throw new RuntimeException("Invalid number encountered. Can't map values >4 Got: " + number);
			}
		}
		return data;
	}

	/**
	 * Print the given amount of bytes in binary form.
	 * 
	 * @param bits
	 * @param nBytes
	 */
	private static void print(BitSet bits, int nBytes) {
		StringBuffer b = new StringBuffer();
		int nBit = 0;
		for (int i = 0; i < nBytes * 8; i++) {
			b.append(bits.get(i) ? "1" : "0");
			if (++nBit % 8 == 0) {
				b.append(" ");
			}
		}
		b.append("\n");
		System.out.println(b.toString());
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

	/**
	 * Extracts a short from the next two bits of the set.
	 * 
	 * @param bits
	 * @param offset
	 * @return
	 */
	public static short toShort2Bit(BitSet bits, int offset) {
		int radix = 2;
		int i = offset;
		int len = 2;
		int result = 0;
		while (i < offset + len) {
			int digit = bits.get(i) ? 1 : 0;
			result *= radix;
			result -= digit;
			i++;
		}
		return (short) -result;
	}

	/**
	 * Extract a short from the next four bits of the set.
	 * 
	 * @param bits
	 * @param offset
	 * @return
	 */
	public static byte toShort4Bit(BitSet bits, int offset) {
		print(bits, 1);
		byte b = (byte) (bits.toByteArray()[0] << 4);
		BitSet b2 = BitSet.valueOf(new byte[] { b });
		print(b2, 1);
		return b;
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
