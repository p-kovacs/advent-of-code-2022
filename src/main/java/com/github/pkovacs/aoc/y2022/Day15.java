package com.github.pkovacs.aoc.y2022;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;
import com.github.pkovacs.util.data.Point;
import com.google.common.collect.Range;

public class Day15 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        var sensors = readSensors(lines);

        System.out.println("Part 1: " + solvePart1(sensors));
        System.out.println("Part 2: " + solvePart2(sensors));
    }

    private static long solvePart1(List<Sensor> sensors) {
        int min = sensors.stream().mapToInt(s -> s.sp().x() - s.d).min().orElseThrow();
        int max = sensors.stream().mapToInt(s -> s.sp().x() + s.d).max().orElseThrow();

        int y = 2000000;
        return IntStream.range(min, max)
                .mapToObj(x -> new Point(x, y))
                .filter(p -> sensors.stream().anyMatch(it -> it.excludes(p) && !it.bp.equals(p)))
                .count();
    }

    /**
     * A simple solution for part 2.
     * <p>
     * For each y, we search for an x value so that the point (x, y) is feasible. In order to make this check fast
     * enough, if (x, y) is within a signal's checking circle, then x is set to the smallest value outside the circle.
     */
    private static long solvePart2(List<Sensor> sensors) {
        int max = 4000000;

        for (int y = 0; y <= max; y++) {
            var ranges = new ArrayList<Range<Integer>>();
            for (var s : sensors) {
                int dx = s.d - Math.abs(y - s.sp.y());
                if (dx >= 0) {
                    ranges.add(Range.closed(s.sp.x() - dx, s.sp.x() + dx));
                }
            }

            for (int x = 0; x <= max; ) {
                int xx = x;
                var range = ranges.stream().filter(r -> r.contains(xx)).findFirst();
                if (range.isPresent()) {
                    x = range.get().upperEndpoint() + 1;
                } else {
                    return (long) x * max + y;
                }
            }
        }

        return 0; // not reached
    }

    private static List<Sensor> readSensors(List<String> lines) {
        var sensors = new ArrayList<Sensor>();
        for (var line : lines) {
            var ints = InputUtils.parseInts(line);
            var sp = new Point(ints[0], ints[1]);
            var bp = new Point(ints[2], ints[3]);
            sensors.add(new Sensor(sp, bp, sp.dist(bp)));
        }
        return sensors;
    }

    private record Sensor(Point sp, Point bp, int d) {
        boolean excludes(Point p) {
            return sp.dist(p) <= d;
        }
    }

}
