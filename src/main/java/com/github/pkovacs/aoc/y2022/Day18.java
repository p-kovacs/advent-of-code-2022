package com.github.pkovacs.aoc.y2022;

import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

import com.github.pkovacs.util.Utils;
import com.github.pkovacs.util.alg.Bfs;
import com.github.pkovacs.util.data.Box;
import com.github.pkovacs.util.data.Vector;

public class Day18 {

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines, 1));
        System.out.println("Part 2: " + solve(lines, 2));
    }

    private static long solve(List<String> lines, int part) {
        var cubes = new HashSet<Vector>();
        for (var line : lines) {
            var ints = Utils.parseInts(line);
            cubes.add(new Vector(ints[0], ints[1], ints[2]));
        }

        var boundingBox = Box.bound(cubes);
        var box = new Box(boundingBox.min().add(-1, -1, -1), boundingBox.max().add(1, 1, 1));

        var outside = Bfs.run(box.min(),
                v -> v.neighbors().filter(n -> box.contains(n) && !cubes.contains(n)).toList()).keySet();

        Predicate<Vector> p = part == 2 ? outside::contains : v -> true;

        return cubes.stream().flatMap(Vector::neighbors).filter(n -> !cubes.contains(n) && p.test(n)).count();
    }

}
