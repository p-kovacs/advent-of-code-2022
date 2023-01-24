package com.github.pkovacs.aoc.y2022;

import java.util.HashSet;
import java.util.List;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.Utils;
import com.google.common.collect.Lists;

public class Day03 {

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines.stream().map(Day03::split).toList()));
        System.out.println("Part 2: " + solve(Lists.partition(lines, 3)));
    }

    private static List<String> split(String str) {
        int n = str.length() / 2;
        return List.of(str.substring(0, n), str.substring(n));
    }

    private static int solve(List<List<String>> lists) {
        return lists.stream().map(Day03::getCommonChar).mapToInt(Day03::getPriority).sum();
    }

    private static char getCommonChar(List<String> lists) {
        var set = new HashSet<>(Lists.charactersOf(lists.get(0)));
        for (int i = 1; i < lists.size(); i++) {
            set.retainAll(new HashSet<>(Lists.charactersOf(lists.get(i))));
        }
        return set.iterator().next(); // assume that we always have a single common character
    }

    private static int getPriority(char ch) {
        return ch >= 'a' && ch <= 'z' ? ch - 'a' + 1 : ch - 'A' + 27;
    }

}
