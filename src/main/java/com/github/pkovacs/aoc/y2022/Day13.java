package com.github.pkovacs.aoc.y2022;

import java.util.ArrayList;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;

public class Day13 {

    public static void main(String[] args) {
        var lines = new ArrayList<>(InputUtils.readLines(AocUtils.getInputPath()));
        lines.removeIf(String::isBlank);

        int part1 = 0;
        for (int i = 0; i < lines.size() - 1; i += 2) {
            if (compare(lines.get(i), lines.get(i + 1)) < 0) {
                part1 += i / 2 + 1;
            }
        }

        lines.add("[[2]]");
        lines.add("[[6]]");
        lines.sort(Day13::compare);
        int part2 = (lines.indexOf("[[2]]") + 1) * (lines.indexOf("[[6]]") + 1);

        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }

    private static int compare(String s1, String s2) {
        var x = transformNumbers(s1);
        var y = transformNumbers(s2);
        for (int i = 0, j = 0; i < x.length() && j < y.length(); ) {
            var a = x.charAt(i);
            var b = y.charAt(j);
            if (a == b) {
                i++;
                j++;
            } else if (a == ']') {
                return -1;
            } else if (b == ']') {
                return 1;
            } else if (a == '[') {
                y = wrapNumber(y, j);
            } else if (b == '[') {
                x = wrapNumber(x, i);
            } else {
                return Character.compare(a, b);
            }
        }
        return 0;
    }

    /** Adds a leading 0 to single-digit integers in the given packet string to make char-by-char comparison easier. */
    private static String transformNumbers(String s) {
        return s.replaceAll("\\b(\\d)\\b", "0$1");
    }

    /** Wraps the integer starting at the given index into a list in the given packet string. */
    private static String wrapNumber(String s, int index) {
        return s.substring(0, index) + s.substring(index).replaceFirst("(\\d+)", "[$1]");
    }

}
