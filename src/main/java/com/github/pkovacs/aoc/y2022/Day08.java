package com.github.pkovacs.aoc.y2022;

import java.util.Arrays;
import java.util.stream.Stream;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.Utils;
import com.github.pkovacs.util.data.Cell;
import com.github.pkovacs.util.data.CharTable;
import com.github.pkovacs.util.data.Direction;

public class Day08 {

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());
        var table = new CharTable(lines);

        System.out.println("Part 1: " + table.cells().filter(cell -> isVisible(table, cell)).count());
        System.out.println("Part 2: " + table.cells().mapToLong(cell -> getScore(table, cell)).max().orElseThrow());
    }

    public static long getScore(CharTable table, Cell cell) {
        var current = table.get(cell);
        long prod = 1;
        for (var dir : Direction.values()) {
            var trees = trees(table, cell, dir).toList();
            prod *= Math.min(trees.stream().takeWhile(ch -> ch < current).count() + 1, trees.size());
        }
        return prod;
    }

    private static boolean isVisible(CharTable table, Cell cell) {
        var current = table.get(cell);
        return Arrays.stream(Direction.values()).anyMatch(dir -> trees(table, cell, dir).allMatch(ch -> ch < current));
    }

    private static Stream<Character> trees(CharTable table, Cell cell, Direction dir) {
        return table.ray(cell, dir).map(table::get);
    }

}
