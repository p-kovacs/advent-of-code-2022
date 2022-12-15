package com.github.pkovacs.aoc.y2022;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;
import com.github.pkovacs.util.data.Point;
import com.google.common.collect.Range;

public class Day15 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        var sensors = readSensors(lines);

        System.out.println("Part 1: " + solvePart1Fast(sensors));
        System.out.println("Part 2: " + solvePart2Fast(sensors));
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

    private static long solvePart1Fast(List<Sensor> sensors) {
        int min = sensors.stream().mapToInt(s -> s.sp().x() - s.d).min().orElseThrow();
        int max = sensors.stream().mapToInt(s -> s.sp().x() + s.d).max().orElseThrow();

        int y = 2000000;

        // Count the points that are within a signal's checking circle
        long count = 0;
        var ranges = collectXRanges(sensors, y);
        for (int x = min; x <= max; ) {
            var range = findRange(ranges, x);
            if (range.isPresent()) {
                int step = range.get().upperEndpoint() + 1 - x;
                count += step;
                x += step;
            } else {
                x++;
            }
        }

        // Subtract the number of distinct beacons in this row
        count -= sensors.stream().filter(s -> s.bp.y() == y).mapToInt(s -> s.bp.x()).distinct().count();

        return count;
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
            var ranges = collectXRanges(sensors, y);
            for (int x = 0; x <= max; ) {
                var range = findRange(ranges, x);
                if (range.isPresent()) {
                    x = range.get().upperEndpoint() + 1;
                } else {
                    return (long) x * max + y;
                }
            }
        }

        return 0; // not reached
    }

    private static List<Range<Integer>> collectXRanges(List<Sensor> sensors, int y) {
        var ranges = new ArrayList<Range<Integer>>();
        for (var s : sensors) {
            int dx = s.d - Math.abs(y - s.sp.y());
            if (dx >= 0) {
                ranges.add(Range.closed(s.sp.x() - dx, s.sp.x() + dx));
            }
        }
        return ranges;
    }

    private static Optional<Range<Integer>> findRange(List<Range<Integer>> ranges, int v) {
        return ranges.stream().filter(r -> r.contains(v)).findFirst();
    }

    /**
     * A fast solution for part 2.
     * <p>
     * Let's consider the lines that bound the sensors' checking circles from the outside. Note that the point
     * we search for must be contained by at least two of these lines (or it must be one of the corners of the
     * search area and contained in at least one of the lines).
     * <p>
     * The lines can be described by equations like {@code x + y = A} and {@code x - y = B}. This method collects
     * the constants A and B for the sensors and calculates the intersection of each pair of perpendicular lines
     * (each two of the lines are either parallel or perpendicular). These points are then checked for feasibility
     * against all sensors. In order to check the four corners as well, the two diagonals of the search area are
     * also added as equations {x + y = 4000000} and {x - y = 0}.
     * <p>
     * For two lines {@code x + y = A} and {@code x - y = B}, the intersection point is calculated this way:
     * {@code x = (A + B) / 2}, {@code y = A - x} (provided that {@code A + B} is even).
     */
    private static long solvePart2Fast(List<Sensor> sensors) {
        int max = 4000000;

        var sums = new HashSet<Integer>();
        var diffs = new HashSet<Integer>();
        for (var s : sensors) {
            sums.add(s.sp.x() + s.sp.y() - s.d - 1);
            sums.add(s.sp.x() + s.sp.y() + s.d + 1);
            diffs.add(s.sp.x() - s.sp.y() - s.d - 1);
            diffs.add(s.sp.x() - s.sp.y() + s.d + 1);
        }
        sums.add(max);
        diffs.add(0);

        for (int a : sums) {
            for (int b : diffs) {
                int x = (a + b) / 2;
                int y = a - x;
                var p = new Point(x, y);
                if ((a + b) % 2 == 0 && x >= 0 && x <= max && y >= 0 && y <= max
                        && sensors.stream().noneMatch(s -> s.excludes(p))) {
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
