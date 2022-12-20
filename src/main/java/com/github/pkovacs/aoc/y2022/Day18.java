package com.github.pkovacs.aoc.y2022;

import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;
import com.github.pkovacs.util.alg.Bfs;
import com.github.pkovacs.util.data.Vector;

public class Day18 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines, 1));
        System.out.println("Part 2: " + solve(lines, 2));
    }

    private static long solve(List<String> lines, int part) {
        var cubes = new HashSet<Vector>();
        for (var line : lines) {
            var ints = InputUtils.parseInts(line);
            cubes.add(new Vector(ints[0], ints[1], ints[2]));
        }

        var min = cubes.stream().flatMapToLong(v -> LongStream.of(v.x(), v.y(), v.z())).min().orElseThrow();
        var max = cubes.stream().flatMapToLong(v -> LongStream.of(v.x(), v.y(), v.z())).max().orElseThrow();

        var from = new Vector(min - 1, min - 1, min - 1);
        var to = new Vector(max + 1, max + 1, max + 1);
        var outside = Bfs.run(from,
                v -> neighbors(v).filter(n -> isInBox(v, from, to) && !cubes.contains(n)).toList()).keySet();

        Predicate<Vector> p = part == 2 ? outside::contains : v -> true;

        return cubes.stream().flatMap(Day18::neighbors).filter(n -> !cubes.contains(n) && p.test(n)).count();
    }

    private static Stream<Vector> neighbors(Vector v) {
        return Stream.of(
                new Vector(v.x() - 1, v.y(), v.z()),
                new Vector(v.x() + 1, v.y(), v.z()),
                new Vector(v.x(), v.y() - 1, v.z()),
                new Vector(v.x(), v.y() + 1, v.z()),
                new Vector(v.x(), v.y(), v.z() - 1),
                new Vector(v.x(), v.y(), v.z() + 1)
        );
    }

    private static boolean isInBox(Vector v, Vector a, Vector b) {
        return v.x() >= a.x() && v.y() >= a.y() && v.z() >= a.z() &&
                v.x() <= b.x() && v.y() <= b.y() && v.z() <= b.z();
    }

}
