package com.github.pkovacs.aoc.y2022;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.Utils;
import com.github.pkovacs.util.data.Range;

public class Day04 {

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());

        int cnt1 = 0, cnt2 = 0;
        for (var line : lines) {
            var ints = Utils.parseInts(line);
            var a = new Range(ints[0], ints[1]);
            var b = new Range(ints[2], ints[3]);
            if (a.containsAll(b) || b.containsAll(a)) {
                cnt1++;
            }
            if (a.overlaps(b)) {
                cnt2++;
            }
        }

        System.out.println("Part 1: " + cnt1);
        System.out.println("Part 2: " + cnt2);
    }

}
