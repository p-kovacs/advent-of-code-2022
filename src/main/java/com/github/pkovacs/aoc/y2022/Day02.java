package com.github.pkovacs.aoc.y2022;

import java.util.List;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;

public class Day02 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines, 1));
        System.out.println("Part 2: " + solve(lines, 2));
    }

    public static int solve(List<String> lines, int part) {
        return lines.stream().mapToInt(line -> {
            int a = line.charAt(0) - 'A';
            int b = line.charAt(2) - 'X';
            return part == 1 ? getScore(a, b) : getScore(a, getSecondShape(a, b));
        }).sum();
    }

    public static int getScore(int s1, int s2) {
        return playRound(s1, s2) * 3 + s2 + 1;
    }

    /**
     * Returns 0 if the first shape wins, 2 if the second shape wins, and 1 for a draw.
     * The parameters must be 0, 1, 2 for Rock, Paper, and Scissors, respectively.
     */
    public static int playRound(int s1, int s2) {
        return (s2 - s1 + 4) % 3;
        // or: s1 == s2 ? 1 : ((s1 + 1) % 3 == s2 ? 2 : 0)
    }

    public static int getSecondShape(int s1, int result) {
        return (s1 + result + 2) % 3;
        // or: IntStream.range(0, 3).filter(i -> playRound(s1, i) == result).findFirst().orElseThrow();
    }

}
