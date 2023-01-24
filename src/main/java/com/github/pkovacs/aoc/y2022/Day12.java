package com.github.pkovacs.aoc.y2022;

import java.util.function.Predicate;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;
import com.github.pkovacs.util.alg.Bfs;
import com.github.pkovacs.util.data.Cell;
import com.github.pkovacs.util.data.CharTable;

public class Day12 {

    public static void main(String[] args) {
        var table = new CharTable(InputUtils.readCharMatrix(AocUtils.getInputPath()));

        System.out.println("Part 1: " + solve(table, c -> table.get(c) == 'S'));
        System.out.println("Part 2: " + solve(table, c -> getHeight(table.get(c)) == 0));
    }

    private static long solve(CharTable table, Predicate<Cell> startPredicate) {
        var target = table.cells().filter(c -> table.get(c) == 'E').findFirst().orElseThrow();

        return Bfs.findPathFromAny(table.cells().filter(startPredicate).toList(),
                c -> table.neighbors(c)
                        .filter(n -> getHeight(table.get(n)) <= getHeight(table.get(c)) + 1)
                        .toList(),
                target::equals).orElseThrow().dist();
    }

    private static int getHeight(char ch) {
        return switch (ch) {
            case 'S' -> 0;
            case 'E' -> 'z' - 'a';
            default -> ch - 'a';
        };
    }

}
