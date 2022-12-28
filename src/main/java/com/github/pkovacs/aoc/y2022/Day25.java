package com.github.pkovacs.aoc.y2022;

import java.util.Map;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;

public class Day25 {

    private static final Map<Character, Integer> fromSnafuDigit = Map.of('0', 0, '1', 1, '2', 2, '-', -1, '=', -2);
    private static final Map<Integer, Character> toSnafuDigit = Map.of(0, '0', 1, '1', 2, '2', 3, '=', 4, '-');

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        long sum = 0;
        for (var line : lines) {
            sum += fromSnafu(line);
        }

        System.out.println("Part 1: " + toSnafu(sum));
        System.out.println("Part 2: " + 0);
    }

    private static long fromSnafu(String s) {
        long x = 0;
        for (char ch : s.toCharArray()) {
            x = x * 5 + fromSnafuDigit.get(ch);
        }
        return x;
    }

    private static String toSnafu(long v) {
        var sb = new StringBuilder();
        for (long x = v; x > 0; ) {
            int d = (int) (x % 5);
            sb.append(toSnafuDigit.get(d));
            x = x / 5 + (d >= 3 ? 1 : 0);
        }
        return sb.reverse().toString();
    }

}
