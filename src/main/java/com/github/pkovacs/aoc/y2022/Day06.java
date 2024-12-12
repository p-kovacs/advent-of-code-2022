package com.github.pkovacs.aoc.y2022;

import java.util.stream.IntStream;

import com.github.pkovacs.util.Utils;

public class Day06 {

    public static void main(String[] args) {
        var line = Utils.readFirstLine(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(line, 4));
        System.out.println("Part 2: " + solve(line, 14));
    }

    private static int solve(String input, int count) {
        return IntStream.rangeClosed(count, input.length())
                .filter(i -> Utils.charsOf(input).skip(i - count).limit(count).distinct().count() == count)
                .findFirst().orElseThrow();
    }

}
