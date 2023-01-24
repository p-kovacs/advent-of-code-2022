package com.github.pkovacs.aoc.y2022;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.Utils;
import com.github.pkovacs.util.data.Point;

public class Day14 {

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines, 1));
        System.out.println("Part 2: " + solve(lines, 2));
    }

    private static int solve(List<String> lines, int part) {
        Map<Point, Character> map = readMap(lines);

        Point start = new Point(500, 0);
        int maxY = map.keySet().stream().mapToInt(Point::y).max().orElseThrow();

        int sandCount = 0;
        while (true) {
            sandCount++;
            Point s = start;
            while (true) {
                var next = Stream.of(new Point(s.x(), s.y() + 1),
                                new Point(s.x() - 1, s.y() + 1),
                                new Point(s.x() + 1, s.y() + 1))
                        .filter(p -> p.y() <= maxY + 1)
                        .filter(p -> !map.containsKey(p))
                        .findFirst();

                if (next.isPresent()) {
                    s = next.get();
                } else {
                    break;
                }
            }
            map.put(s, 'o');

            if (part == 1 && s.y() == maxY + 1) {
                sandCount--;
                break;
            } else if (part == 2 && s.equals(start)) {
                break;
            }
        }

        return sandCount;
    }

    private static Map<Point, Character> readMap(List<String> lines) {
        var map = new HashMap<Point, Character>();

        for (var line : lines) {
            var ints = Utils.parseInts(line);
            Point prev = new Point(ints[0], ints[1]);
            for (int i = 2; i < ints.length; i += 2) {
                var p = new Point(ints[i], ints[i + 1]);
                if (prev.x() == p.x()) {
                    for (int y = Math.min(prev.y(), p.y()); y <= Math.max(prev.y(), p.y()); y++) {
                        map.put(new Point(p.x(), y), '#');
                    }
                } else {
                    for (int x = Math.min(prev.x(), p.x()); x <= Math.max(prev.x(), p.x()); x++) {
                        map.put(new Point(x, p.y()), '#');
                    }
                }
                prev = p;
            }
        }

        return map;
    }

}
