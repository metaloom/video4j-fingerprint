package io.metaloom.video4j.fingerprint.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

import org.assertj.core.api.AbstractAssert;
import org.opencv.core.Mat;

import io.metaloom.utils.hash.HashUtils;
import io.metaloom.video4j.fingerprint.Fingerprint;

public class FingerprintAssert extends AbstractAssert<FingerprintAssert, Fingerprint> {

	protected FingerprintAssert(Fingerprint actual) {
		super(actual, FingerprintAssert.class);
	}

	public FingerprintAssert matches(Mat mat) {
		assertEquals("The version of the fingerprint did not match.", actual.version(), Fingerprint.FINGERPRINT_VERSION_V1);
		byte[] data = actual.array();
		int offset = 0;
		for (int x = 0; x < mat.width(); x++) {
			for (int y = 0; y < mat.height(); y++) {
				double pixel = mat.get(x, y)[0];
				if (pixel > 128f) {
					assertTrue("The pixel at {" + x + ":" + y + "} (" + offset + ") should be set to true", actual.bits().get(offset));
				} else {
					assertFalse("The pixel at {" + x + ":" + y + "} (" + offset + ") should be set to false", actual.bits().get(offset));
				}
				offset++;
			}
		}
		matches(data);
		return this;
	}

	public FingerprintAssert matches(String hex) {
		matches(HashUtils.hexToBytes(hex));
		assertEquals("The hex fingerprint should always have the same length", 76, hex.length());
		assertEquals("The hex strings were different", hex, actual.hex());
		return this;
	}

	public FingerprintAssert matches(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		assertEquals("The area should contain the fingerprint version.", Fingerprint.FINGERPRINT_VERSION_V1, bb.getShort());
		assertEquals("The area should always be 0.", 0x00, bb.get());
		assertEquals("The area should contain the fingerprint size.", Fingerprint.DEFAULT_FINGERPRINT_SIZE, bb.getShort());
		assertEquals("The area should always be 0.", -1, bb.get());

		byte[] bitData = Arrays.copyOfRange(data, 2 + 1 + 2 + 1, data.length);
		BitSet set = BitSet.valueOf(bitData);
		for (int bit = 0; bit < Fingerprint.DEFAULT_FINGERPRINT_SIZE; bit++) {
			assertEquals("The bit at {" + bit + "} did not match", actual.bits().get(bit), set.get(bit));
		}
		return this;
	}
}
