package com.github.pkovacs.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {

    @Test
    void testRangeMethods() {
        assertEquals(0, Utils.constrainIndex(0, 5));
        assertEquals(2, Utils.constrainIndex(2, 5));
        assertEquals(4, Utils.constrainIndex(4, 5));
        assertEquals(4, Utils.constrainIndex(5, 5));
        assertEquals(4, Utils.constrainIndex(42, 5));
        assertEquals(0, Utils.constrainIndex(-1, 5));
        assertEquals(0, Utils.constrainIndex(-42, 5));

        assertEquals(0, Utils.wrapIndex(0, 5));
        assertEquals(2, Utils.wrapIndex(2, 5));
        assertEquals(4, Utils.wrapIndex(4, 5));
        assertEquals(0, Utils.wrapIndex(5, 5));
        assertEquals(2, Utils.wrapIndex(42, 5));
        assertEquals(4, Utils.wrapIndex(-1, 5));
        assertEquals(3, Utils.wrapIndex(-42, 5));

        assertEquals(3, Utils.constrainToRange(0, 3, 7));
        assertEquals(5, Utils.constrainToRange(5, 3, 7));
        assertEquals(7, Utils.constrainToRange(10, 3, 7));
        assertEquals(3, Utils.constrainToRange(-2, 3, 7));
        assertEquals(7, Utils.constrainToRange(42, 3, 7));

        assertEquals(3L, Utils.constrainToRange(0L, 3L, 7L));
        assertEquals(5L, Utils.constrainToRange(5L, 3L, 7L));
        assertEquals(7L, Utils.constrainToRange(10L, 3L, 7L));
        assertEquals(3L, Utils.constrainToRange(-2L, 3L, 7L));
        assertEquals(7L, Utils.constrainToRange(42L, 3L, 7L));

        assertEquals(5, Utils.wrapToRange(0, 3, 7));
        assertEquals(4, Utils.wrapToRange(4, 3, 7));
        assertEquals(3, Utils.wrapToRange(8, 3, 7));
        assertEquals(7, Utils.wrapToRange(12, 3, 7));
        assertEquals(4, Utils.wrapToRange(34, 3, 7));
        assertEquals(6, Utils.wrapToRange(56, 3, 7));
        assertEquals(7, Utils.wrapToRange(-3, 3, 7));

        assertEquals(5L, Utils.wrapToRange(0L, 3L, 7L));
        assertEquals(4L, Utils.wrapToRange(4L, 3L, 7L));
        assertEquals(3L, Utils.wrapToRange(8L, 3L, 7L));
        assertEquals(7L, Utils.wrapToRange(12L, 3L, 7L));
        assertEquals(4L, Utils.wrapToRange(34L, 3L, 7L));
        assertEquals(6L, Utils.wrapToRange(56L, 3L, 7L));
        assertEquals(7L, Utils.wrapToRange(-3L, 3L, 7L));

        assertTrue(Utils.isInRange('k', 'a', 'z'));
        assertTrue(Utils.isInRange(5, 3, 8));
        assertTrue(Utils.isInRange(3.0, Math.E, Math.PI));
    }

    @Test
    void testInts() {
        int[] x = { 3, 2, 1, 5, 4 };

        assertEquals(List.of(), Utils.listOf(new int[0]));
        assertEquals(Set.of(), Utils.setOf(new int[0]));
        assertEquals(List.of(3, 2, 1, 5, 4), Utils.listOf(x));
        assertEquals(Set.of(3, 2, 1, 5, 4), Utils.setOf(x));
        assertEquals(3, Utils.streamOf(x).filter(c -> c % 2 == 1).count());

        assertEquals(1, Utils.min(3, 1, 5));
        assertEquals(1, Utils.min(x));
        assertEquals(1, Utils.minInt(Utils.listOf(x)));
        assertEquals(5, Utils.max(3, 1, 5));
        assertEquals(5, Utils.max(x));
        assertEquals(5, Utils.maxInt(Utils.listOf(x)));

        assertThrows(NoSuchElementException.class, () -> Utils.min(new int[0]));
        assertThrows(NoSuchElementException.class, () -> Utils.max(new int[0]));
        assertThrows(NoSuchElementException.class, () -> Utils.minInt(List.of()));
        assertThrows(NoSuchElementException.class, () -> Utils.maxInt(List.of()));
    }

    @Test
    void testLongs() {
        long[] x = { 3, 2, 1, 5, 4 };

        assertEquals(List.of(), Utils.listOf(new int[0]));
        assertEquals(Set.of(), Utils.setOf(new int[0]));
        assertEquals(List.of(3L, 2L, 1L, 5L, 4L), Utils.listOf(x));
        assertEquals(Set.of(3L, 2L, 1L, 5L, 4L), Utils.setOf(x));
        assertEquals(3, Utils.streamOf(x).filter(c -> c % 2 == 1).count());

        assertEquals(1L, Utils.min(3L, 1L, 5L));
        assertEquals(1L, Utils.min(x));
        assertEquals(1L, Utils.minInt(Utils.listOf(x)));
        assertEquals(5L, Utils.max(3L, 1L, 5L));
        assertEquals(5L, Utils.max(x));
        assertEquals(5L, Utils.maxInt(Utils.listOf(x)));

        assertThrows(NoSuchElementException.class, () -> Utils.min(new long[0]));
        assertThrows(NoSuchElementException.class, () -> Utils.max(new long[0]));
        assertThrows(NoSuchElementException.class, () -> Utils.minLong(List.of()));
        assertThrows(NoSuchElementException.class, () -> Utils.maxLong(List.of()));
    }

    @Test
    void testChars() {
        char[] x = { 'h', 'e', 'l', 'l', 'o' };

        assertEquals(List.of('h', 'e', 'l', 'l', 'o'), Utils.charsOf("hello").toList());
        assertEquals(2, Utils.charsOf("hello").filter(c -> c == 'l').count());

        assertEquals(List.of('h', 'e', 'l', 'l', 'o'), Utils.listOf(x));
        assertEquals(Set.of('h', 'e', 'l', 'o'), Utils.setOf(x));
        assertEquals(3, Utils.streamOf("hello".toCharArray()).filter(c -> c != 'l').count());

        assertEquals('a', Utils.min('c', 'a', 'f', 'b'));
        assertEquals('e', Utils.min(x));
        assertEquals('f', Utils.max('c', 'a', 'f', 'b'));
        assertEquals('o', Utils.max(x));

        assertThrows(NoSuchElementException.class, () -> Utils.min(new char[0]));
        assertThrows(NoSuchElementException.class, () -> Utils.max(new char[0]));
    }

    @Test
    void testSetOperations() {
        var c1 = Utils.setOf("hello".toCharArray());
        var c2 = Utils.setOf("echo".toCharArray());
        var c3 = Utils.listOf("love".toCharArray());
        var c4 = Utils.listOf("old".toCharArray());

        assertEquals(Set.of('h', 'e', 'l', 'o', 'c'), Utils.unionOf(c1, c2));
        assertEquals(Set.of('h', 'e', 'l', 'o', 'v'), Utils.unionOf(c1, c3));
        assertEquals(Set.of('h', 'e', 'l', 'o', 'd'), Utils.unionOf(c1, c4));
        assertEquals(Set.of('e', 'c', 'h', 'o', 'l', 'v'), Utils.unionOf(c2.stream(), c3.stream()));
        assertEquals(Set.of('e', 'c', 'h', 'o', 'l', 'd'), Utils.unionOf(c2.stream(), c4.stream()));
        assertEquals(Set.of('l', 'o', 'v', 'e', 'd'), Utils.unionOf(c3.stream(), c4.stream()));
        assertEquals(Set.of('h', 'e', 'l', 'o', 'c', 'v'), Utils.unionOf(List.of(c1, c2, c3)));
        assertEquals(Set.of('h', 'e', 'l', 'o', 'v', 'd'), Utils.unionOf(List.of(c1, c3, c4)));
        assertEquals(Set.of('h', 'e', 'l', 'o', 'c', 'v', 'd'), Utils.unionOf(List.of(c1, c2, c3, c4)));

        assertEquals(Set.of('h', 'e', 'o'), Utils.intersectionOf(c1, c2));
        assertEquals(Set.of('e', 'l', 'o'), Utils.intersectionOf(c1, c3));
        assertEquals(Set.of('l', 'o'), Utils.intersectionOf(c1, c4));
        assertEquals(Set.of('e', 'o'), Utils.intersectionOf(c2.stream(), c3.stream()));
        assertEquals(Set.of('o'), Utils.intersectionOf(c2.stream(), c4.stream()));
        assertEquals(Set.of('l', 'o'), Utils.intersectionOf(c3.stream(), c4.stream()));
        assertEquals(Set.of('e', 'o'), Utils.intersectionOf(List.of(c1, c2, c3)));
        assertEquals(Set.of('l', 'o'), Utils.intersectionOf(List.of(c1, c3, c4)));
        assertEquals(Set.of('o'), Utils.intersectionOf(List.of(c1, c2, c3, c4)));
    }

    @Test
    void testGenericParametersOfSetOperations() {
        var a = List.of(List.of(1, 2), List.of(1, 2, 3));
        var b = Set.of(new ArrayList<>(List.of(1)), new ArrayList<>(List.of(1, 2)));
        var c = Set.of(new LinkedList<>(List.of(1)), new LinkedList<>(List.of(1, 2, 3)));

        var union = Set.of(List.of(1), List.of(1, 2), List.of(1, 2, 3));
        assertEquals(union, Utils.unionOf(a, b));
        assertEquals(union, Utils.unionOf(b.stream(), a.stream()));
        assertEquals(union, Utils.unionOf(a, c));
        assertEquals(union, Utils.unionOf(c.stream(), a.stream()));
        assertEquals(union, Utils.unionOf(b, c));
        assertEquals(union, Utils.unionOf(c.stream(), b.stream()));
        assertEquals(union, Utils.unionOf(List.of(a, b, c)));
        assertEquals(union, Utils.unionOf(List.of(c, b, a)));

        assertEquals(Set.of(List.of(1, 2)), Utils.intersectionOf(a, b));
        assertEquals(Set.of(List.of(1, 2)), Utils.intersectionOf(b.stream(), a.stream()));
        assertEquals(Set.of(List.of(1, 2, 3)), Utils.intersectionOf(a, c));
        assertEquals(Set.of(List.of(1, 2, 3)), Utils.intersectionOf(c.stream(), a.stream()));
        assertEquals(Set.of(List.of(1)), Utils.intersectionOf(b, c));
        assertEquals(Set.of(List.of(1)), Utils.intersectionOf(c.stream(), b.stream()));
        assertEquals(Set.of(), Utils.intersectionOf(List.of(a, b, c)));
        assertEquals(Set.of(), Utils.intersectionOf(List.of(c, b, a)));
    }

    @Test
    public void testListSlicing() {
        assertEquals(List.of(List.of(1, 2, 3), List.of(4, 5, 6)),
                Utils.chunked(List.of(1, 2, 3, 4, 5, 6), 3).toList());
        assertEquals(List.of(List.of(1, 2, 3), List.of(4, 5)),
                Utils.chunked(List.of(1, 2, 3, 4, 5), 3).toList());
        assertEquals(List.of(List.of(1, 2), List.of(3, 4), List.of(5)),
                Utils.chunked(List.of(1, 2, 3, 4, 5), 2).toList());
        assertEquals(List.of(List.of(1), List.of(2), List.of(3), List.of(4)),
                Utils.chunked(List.of(1, 2, 3, 4), 1).toList());
        assertEquals(List.of(List.of(1, 2, 3), List.of(4)),
                Utils.chunked(List.of(1, 2, 3, 4), 3).toList());
        assertEquals(List.of(List.of(1, 2, 3, 4)),
                Utils.chunked(List.of(1, 2, 3, 4), 4).toList());
        assertEquals(List.of(List.of(1, 2, 3, 4)),
                Utils.chunked(List.of(1, 2, 3, 4), 5).toList());

        assertThrows(IllegalArgumentException.class, () -> Utils.chunked(List.of(1, 2, 3), 0));
        assertThrows(IllegalArgumentException.class, () -> Utils.chunked(List.of(1, 2, 3), -1));

        assertEquals(List.of(List.of(1, 2, 3), List.of(2, 3, 4), List.of(3, 4, 5)),
                Utils.windowed(List.of(1, 2, 3, 4, 5), 3).toList());
        assertEquals(List.of(List.of(1, 2), List.of(2, 3), List.of(3, 4), List.of(4, 5)),
                Utils.windowed(List.of(1, 2, 3, 4, 5), 2).toList());
        assertEquals(List.of(List.of(1), List.of(2), List.of(3), List.of(4)),
                Utils.windowed(List.of(1, 2, 3, 4), 1).toList());
        assertEquals(List.of(List.of(1, 2, 3), List.of(2, 3, 4)),
                Utils.windowed(List.of(1, 2, 3, 4), 3).toList());
        assertEquals(List.of(List.of(1, 2, 3, 4)),
                Utils.windowed(List.of(1, 2, 3, 4), 4).toList());
        assertEquals(List.of(),
                Utils.windowed(List.of(1, 2, 3, 4), 5).toList());

        assertThrows(IllegalArgumentException.class, () -> Utils.windowed(List.of(1, 2, 3), 0));
        assertThrows(IllegalArgumentException.class, () -> Utils.windowed(List.of(1, 2, 3), -1));
    }

    @Test
    public void testInverseMap() {
        var map = Map.of('a', 1, 'b', 2, 'c', 3);

        assertEquals(Map.of(1, 'a', 2, 'b', 3, 'c'), Utils.inverse(map));
        assertEquals(map, Utils.inverse(Utils.inverse(map)));
        assertEquals(Map.of(), Utils.inverse(Map.of()));
    }

}
