package com.github.pkovacs.aoc.y2022;

import com.github.pkovacs.util.Utils;
import com.github.pkovacs.util.alg.Bfs;
import com.github.pkovacs.util.data.Cell;
import com.github.pkovacs.util.data.CharTable;

public class Day24 {

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());

        var table = new CharTable(lines);
        var start = table.row(0).filter(c -> table.get(c) == '.').findFirst().orElseThrow();
        var goal = table.row(table.rowCount() - 1).filter(c -> table.get(c) == '.').findFirst().orElseThrow();

        var t1 = dist(table, start, goal, 0);
        var t2 = dist(table, goal, start, t1);
        var t3 = dist(table, start, goal, t1 + t2);

        System.out.println("Part 1: " + t1);
        System.out.println("Part 2: " + (t1 + t2 + t3));
    }

    private static int dist(CharTable table, Cell start, Cell goal, int delta) {
        return Bfs.dist(new State(start, delta),
                s -> table.neighborsAndSelf(s.cell)
                        .filter(c -> table.get(c) != '#')
                        .filter(c -> table.get(c.row(), wrap(c.col() + s.time + 1, table.colCount())) != '<')
                        .filter(c -> table.get(c.row(), wrap(c.col() - s.time - 1, table.colCount())) != '>')
                        .filter(c -> table.get(wrap(c.row() + s.time + 1, table.rowCount()), c.col()) != '^')
                        .filter(c -> table.get(wrap(c.row() - s.time - 1, table.rowCount()), c.col()) != 'v')
                        .map(c -> new State(c, s.time + 1))
                        .toList(),
                s -> s.cell.equals(goal));
    }

    private static int wrap(int i, int size) {
        return Utils.wrapToRange(i, 1, size - 2);
    }

    private record State(Cell cell, int time) {}

}
