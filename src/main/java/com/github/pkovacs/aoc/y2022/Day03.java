package com.github.pkovacs.aoc.y2022;

import java.util.List;
import java.util.stream.Stream;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.Utils;

public class Day03 {

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines.stream().map(Day03::split)));
        System.out.println("Part 2: " + solve(Utils.chunked(lines, 3)));
    }

    private static List<String> split(String str) {
        int n = str.length() / 2;
        return List.of(str.substring(0, n), str.substring(n));
    }

    private static int solve(Stream<List<String>> lists) {
        return lists.map(Day03::getCommonChar).mapToInt(Day03::getPriority).sum();
    }

    private static char getCommonChar(List<String> lists) {
        var set = Utils.intersectionOf(lists.stream().map(s -> Utils.charsOf(s).toList()).toList());
        return set.iterator().next(); // assume that we always have a single common character
    }

    private static int getPriority(char ch) {
        return Utils.isInRange(ch, 'a', 'z') ? ch - 'a' + 1 : ch - 'A' + 27;
    }

}
