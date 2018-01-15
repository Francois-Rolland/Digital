package de.neemann.digital.core;

import junit.framework.TestCase;

public class BitsTest extends TestCase {

    public void testMask() {
        assertEquals(1, Bits.mask(1));
        assertEquals(0x7fffffffffffffffL, Bits.mask(63));
        assertEquals(-1, Bits.mask(64));
        assertEquals(-1, Bits.mask(65));
    }

    public void testDown() {
        assertEquals(1, Bits.down(2, 1));
        assertEquals(0x7fffffffffffffffL, Bits.down(-1, 1));
        assertEquals(0L, Bits.down(0xff, 64));
    }

    public void testUp() throws BitsException {
        assertEquals(0x8000000000000000L, Bits.up(1, 63));
        assertEquals(2L, Bits.up(1, 1));
        assertEquals(4L, Bits.up(1, 2));
        assertEquals(0x8000000000000000L, Bits.up(0xff, 63));
        assertEquals(0L, Bits.up(1, 64));
    }

    public void testSignedFlag() {
        assertEquals(1, Bits.signedFlagMask(1));
        assertEquals(0x8000000000000000L, Bits.signedFlagMask(64));
    }

    public void testIsNegative() {
        assertTrue(Bits.isNegative(7, 3));
        assertFalse(Bits.isNegative(3, 3));
        assertTrue(Bits.isNegative(0x8000000000000000L, 64));
        assertFalse(Bits.isNegative(0x4000000000000000L, 64));
    }

    public void testSignExtend() {
        assertEquals(-1, Bits.signExtend(3, 2));
        assertEquals(3, Bits.signExtend(3, 3));
    }

    public void testBitsLn2() {
        assertEquals(1, Bits.binLn2(0));
        assertEquals(1, Bits.binLn2(1));
        assertEquals(2, Bits.binLn2(2));
        assertEquals(2, Bits.binLn2(3));
        assertEquals(3, Bits.binLn2(4));
        assertEquals(3, Bits.binLn2(5));
        assertEquals(3, Bits.binLn2(6));
        assertEquals(3, Bits.binLn2(7));
        assertEquals(4, Bits.binLn2(8));
        assertEquals(4, Bits.binLn2(15));
        assertEquals(5, Bits.binLn2(16));
        assertEquals(5, Bits.binLn2(31));
        assertEquals(6, Bits.binLn2(32));
    }

    public void testDecode() throws Bits.NumberFormatException {
        assertEquals(0, Bits.decode(null));
        assertEquals(0, Bits.decode(""));
        assertEquals(0, Bits.decode("  "));
        assertEquals(0, Bits.decode("0"));
        assertEquals(0, Bits.decode("-0"));
        assertEquals(0, Bits.decode("000"));
        assertEquals(2, Bits.decode("2"));
        assertEquals(2, Bits.decode(" 2"));
        assertEquals(2, Bits.decode("2 "));
        assertEquals(2, Bits.decode(" 2 "));
        assertEquals(-2, Bits.decode("-2"));
        assertEquals(0, Bits.decode("-000"));
        assertEquals(12, Bits.decode("12"));
        assertEquals(-12, Bits.decode("-12"));
        assertEquals(10, Bits.decode("012"));
        assertEquals(7, Bits.decode("0b111"));
        assertEquals(7, Bits.decode("0B111"));
        assertEquals(255, Bits.decode("0xff"));
        assertEquals(255, Bits.decode("0Xff"));
        assertEquals(0x8000000000000000L, Bits.decode("0x8000000000000000"));
        assertEquals(0xFFFFFFFFFFFFFFFFL, Bits.decode("0xFFFFFFFFFFFFFFFF"));

        assertEquals(0xFFFFFFFFFFFFFFFFL, Bits.decode("FFFFFFFFFFFFFFFF", 0, 16));

        assertEquals(42, Bits.decode("'*'"));
    }

    public void testDecodeInvalid() {
        checkInvalid("-");
        checkInvalid("1h");
        checkInvalid("--1");
        checkInvalid("-01");
        checkInvalid("-0x1");
        checkInvalid("-0b1");
        checkInvalid("-0-1");
        checkInvalid("0x");
        checkInvalid("0b");
        checkInvalid("0ba");
        checkInvalid("0xg");
    }

    private void checkInvalid(String s) {
        try {
            Bits.decode(s);
            fail(s);
        } catch (Bits.NumberFormatException e) {
            assertTrue(true);
        }
    }

}