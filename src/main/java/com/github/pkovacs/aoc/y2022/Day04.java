package com.github.pkovacs.aoc.y2022;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;

public class Day04 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        int cnt1 = 0, cnt2 = 0;
        for (var line : lines) {
            var parts = line.split("[,-]");
            var a = Integer.parseInt(parts[0]);
            var b = Integer.parseInt(parts[1]);
            var c = Integer.parseInt(parts[2]);
            var d = Integer.parseInt(parts[3]);
            if ((c >= a && d <= b) || (c <= a && d >= b)) {
                cnt1++;
            }
            if (Math.max(a, c) <= Math.min(b, d)) {
                cnt2++;
            }
        }

        System.out.println("Part 1: " + cnt1);
        System.out.println("Part 2: " + cnt2);
    }

}
