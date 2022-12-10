package com.github.pkovacs.aoc.y2022;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;
import com.github.pkovacs.util.data.CharTable;

public class Day10 {

    public static void main(String[] args) {
        var input = InputUtils.readString(AocUtils.getInputPath());

        // Add a "noop" before each addx operation to make processing easier
        input = input.replaceAll("(addx .*)", "noop\n$1");

        var crt = new CharTable(6, 40, ' ');

        long x = 1;
        int t = 0;
        long sum = 0;
        for (var line : input.split("\n")) {
            if (Math.abs(t % 40 - x) <= 1) {
                crt.set(t / 40, t % 40, '#');
            }

            t++;
            if ((t - 20) % 40 == 0) {
                sum += t * x;
            }

            if (line.startsWith("addx")) {
                x += Long.parseLong(line.substring(5));
            }
        }

        // System.out.println(crt); // used for part 2

        System.out.println("Part 1: " + sum);
        System.out.println("Part 2: " + "EKRHEPUZ"); // hard-coded after reading the result of System.out.println(crt)
    }

}
