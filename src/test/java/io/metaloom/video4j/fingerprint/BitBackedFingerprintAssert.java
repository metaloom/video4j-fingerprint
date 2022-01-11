package io.metaloom.video4j.fingerprint;

import static io.metaloom.video4j.fingerprint.v1.BinaryFingerprint.FINGERPRINT_VECTOR_SIZE;
import static io.metaloom.video4j.fingerprint.v1.BinaryFingerprint.FINGERPRINT_VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

import org.assertj.core.api.AbstractAssert;
import org.opencv.core.Mat;

import io.metaloom.utils.hash.HashUtils;
import io.metaloom.video4j.fingerprint.v1.BinaryFingerprint;
import io.metaloom.video4j.fingerprint.v2.MultiSectorFingerprint;

public class BitBackedFingerprintAssert extends AbstractAssert<BitBackedFingerprintAssert, BitBackedFingerprint> {

	public static final int EXPECTED_VECTOR_SIZE = 256;

	public BitBackedFingerprintAssert(BitBackedFingerprint actual) {
		super(actual, BitBackedFingerprintAssert.class);
	}

	public BitBackedFingerprintAssert matches(Mat mat) {
		assertEquals("The version of the fingerprint did not match.", actual.version(), expectedVersionFor(actual));
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

	private short expectedVersionFor(BitBackedFingerprint actual) {
		if (actual instanceof BinaryFingerprint) {
			return 1;
		}
		if (actual instanceof MultiSectorFingerprint) {
			return 2;
		}
		fail("Unknown type of fingerprint " + actual.getClass().getSimpleName());
		return -1;
	}

	public BitBackedFingerprintAssert matches(String hex) {
		matches(HashUtils.hexToBytes(hex));
		assertEquals("The hex fingerprint should always have the same length", 76, hex.length());
		assertEquals("The hex strings were different", hex, actual.hex());
		return this;
	}

	public BitBackedFingerprintAssert matches(byte[] data) {
		ByteBuffer bb = ByteBuffer.wrap(data);
		assertEquals("The area should contain the fingerprint version.", expectedVersionFor(actual), bb.getShort());
		assertEquals("The area should always be 0.", 0x00, bb.get());
		assertEquals("The area should contain the fingerprint size.", EXPECTED_VECTOR_SIZE, bb.getShort());
		assertEquals("The area should always be 0.", -1, bb.get());

		byte[] bitData = Arrays.copyOfRange(data, 2 + 1 + 2 + 1, data.length);
		BitSet set = BitSet.valueOf(bitData);
		for (int bit = 0; bit < EXPECTED_VECTOR_SIZE; bit++) {
			assertEquals("The bit at {" + bit + "} did not match", actual.bits().get(bit), set.get(bit));
		}
		return this;
	}
}
