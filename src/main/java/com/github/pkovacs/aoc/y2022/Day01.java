package com.github.pkovacs.aoc.y2022;

import java.util.List;

import com.github.pkovacs.util.Utils;

public class Day01 {

    public static void main(String[] args) {
        var blocks = Utils.readLineBlocks(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(blocks, 1));
        System.out.println("Part 2: " + solve(blocks, 3));
    }

    private static long solve(List<List<String>> blocks, int count) {
        return blocks.stream()
                .mapToLong(block -> block.stream().mapToLong(Long::parseLong).sum())
                .sorted()
                .skip(blocks.size() - count)
                .sum();
    }

}
