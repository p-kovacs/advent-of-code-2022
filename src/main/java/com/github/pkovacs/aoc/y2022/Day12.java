package com.github.pkovacs.aoc.y2022;

import java.util.Set;

import com.github.pkovacs.util.Utils;
import com.github.pkovacs.util.alg.Bfs;
import com.github.pkovacs.util.data.CharTable;

public class Day12 {

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());
        var table = new CharTable(lines);

        System.out.println("Part 1: " + solve(table, Set.of('S')));
        System.out.println("Part 2: " + solve(table, Set.of('S', 'a')));
    }

    private static long solve(CharTable table, Set<Character> startChars) {
        var sources = table.cells().filter(c -> startChars.contains(table.get(c))).toList();
        var target = table.find('E');

        return Bfs.findPathFromAny(sources,
                c -> table.neighbors(c).filter(n -> getHeight(table.get(n)) <= getHeight(table.get(c)) + 1).toList(),
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
