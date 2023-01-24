package com.github.pkovacs.aoc.y2022;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.Utils;
import com.github.pkovacs.util.data.Cell;
import com.github.pkovacs.util.data.CharTable;
import com.github.pkovacs.util.data.Direction;

public class Day08 {

    public static void main(String[] args) {
        var map = Utils.readCharMatrix(AocUtils.getInputPath());
        var table = new CharTable(map);

        System.out.println("Part 1: " + table.cells().filter(cell -> isVisible(table, cell)).count());
        System.out.println("Part 2: " + table.cells().mapToLong(cell -> getScore(table, cell)).max().orElseThrow());
    }

    public static long getScore(CharTable table, Cell cell) {
        var current = table.get(cell);
        long prod = 1;
        for (var dir : Direction.values()) {
            var trees = getTrees(table, cell, dir);
            prod *= Math.min(trees.stream().takeWhile(c -> c < current).count() + 1, trees.size());
        }
        return prod;
    }

    private static boolean isVisible(CharTable table, Cell cell) {
        var current = table.get(cell);
        return Arrays.stream(Direction.values())
                .anyMatch(dir -> getTrees(table, cell, dir).stream().allMatch(c -> c < current));
    }

    private static List<Character> getTrees(CharTable table, Cell cell, Direction dir) {
        return Stream.iterate(cell.neighbor(dir), table::containsCell, t -> t.neighbor(dir)).map(table::get).toList();
    }

}
