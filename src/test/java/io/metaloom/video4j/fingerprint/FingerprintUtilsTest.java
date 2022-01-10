package io.metaloom.video4j.fingerprint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.Test;

import io.metaloom.video4j.fingerprint.utils.FingerprintUtils;

public class FingerprintUtilsTest {

	@Test
	public void testToShort2Bit() {
		BitSet bits = new BitSet(20);
		bits.set(0, true);
		bits.set(1, false);

		bits.set(2, true);
		bits.set(3, true);

		short s1 = FingerprintUtils.toShort2Bit(bits, 2);
		assertEquals(Short.parseShort("11", 2), s1);

		short s2 = FingerprintUtils.toShort2Bit(bits, 0);
		assertEquals(Short.parseShort("10", 2), s2);
	}

	@Test
	public void testToShort4Bit() {
		BitSet bits = new BitSet();

		// 1101 = 13
		bits.set(0, true);
		bits.set(1, true);
		bits.set(2, false);
		bits.set(3, true);

		assertEquals(13, FingerprintUtils.toShort4Bit(bits, 0));

//		// 0011 = 3
//		bits.set(4, false);
//		bits.set(5, false);
//		bits.set(6, true);
//		bits.set(7, true);
//
//		assertEquals(3, FingerprintUtils.toShort4Bit(bits, 4));

	}

	@Test
	public void testTransformToShort4Bit() {
		BitSet bits = new BitSet();

		// 1101 = 11
		bits.set(0, true);
		bits.set(1, true);
		bits.set(2, false);
		bits.set(3, true);

		// 0011 = 12
		bits.set(4, false);
		bits.set(5, false);
		bits.set(6, true);
		bits.set(7, true);

		short[] numbers = FingerprintUtils.transformToShort4Bit(bits, 2);
		assertEquals(11, numbers[0]);
		assertEquals(12, numbers[1]);
	}

	@Test
	public void testTransformToBitSet4Bit() {
		short[] numbers = new short[] { 12, 11, 0, 3 };

		BitSet bits = FingerprintUtils.transformToBitSet4Bit(numbers);

		// 12 = 1100
		assertTrue(bits.get(0));
		assertTrue(bits.get(1));
		assertFalse(bits.get(2));
		assertFalse(bits.get(3));

		// 11 = 1011
		assertTrue(bits.get(4));
		assertFalse(bits.get(5));
		assertTrue(bits.get(6));
		assertTrue(bits.get(7));

		// 0 = 0000
		assertFalse(bits.get(8));
		assertFalse(bits.get(9));
		assertFalse(bits.get(10));
		assertFalse(bits.get(11));

	}

	@Test
	public void testTransformToBitSet2Bit() {
		short[] numbers = new short[] { 1, 0, 2, 3 };

		BitSet bits = FingerprintUtils.transformToBitSet2Bit(numbers);

		// 1 = 01
		assertFalse(bits.get(0));
		assertTrue(bits.get(1));

		// 0 = 00
		assertFalse(bits.get(2));
		assertFalse(bits.get(3));

		// 2 = 10
		assertTrue(bits.get(4));
		assertFalse(bits.get(5));

		// 3 = 11
		assertTrue(bits.get(6));
		assertTrue(bits.get(7));
	}

	@Test
	public void testTransformToShort2Bit() {
		BitSet bits = new BitSet();
		// 1 = 01
		bits.set(0, false);
		bits.set(1, true);

		// 3 = 11
		bits.set(2, true);
		bits.set(3, true);

		// 0 = 00
		bits.set(4, false);
		bits.set(5, false);

		// 2 = 10
		bits.set(6, true);
		bits.set(7, false);

		short[] shorts = FingerprintUtils.transformToShort2Bit(bits);

		assertEquals(1, shorts[0]);
		assertEquals(3, shorts[1]);
		assertEquals(0, shorts[2]);
		assertEquals(2, shorts[3]);

	}
}
