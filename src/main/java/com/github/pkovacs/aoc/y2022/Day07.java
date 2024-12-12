package com.github.pkovacs.aoc.y2022;

import java.util.Stack;

import com.github.pkovacs.util.Utils;
import com.github.pkovacs.util.data.CounterMap;

public class Day07 {

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());

        var dirs = new CounterMap<String>();
        var paths = new Stack<String>();
        for (var line : lines) {
            if (line.equals("$ cd ..")) {
                paths.pop();
            } else if (line.startsWith("$ cd")) {
                var dirName = line.split(" ")[2];
                paths.add(paths.isEmpty() ? dirName : paths.peek() + dirName + "/");
            } else if (!line.startsWith("$") && !line.startsWith("dir")) {
                long size = Long.parseLong(line.split(" ")[0]);
                paths.forEach(p -> dirs.add(p, size));
            }
        }

        long minToFree = dirs.getValue("/") - 40_000_000L;

        System.out.println("Part 1: " + dirs.valueStream().filter(x -> x <= 100_000).sum());
        System.out.println("Part 2: " + dirs.valueStream().filter(x -> x >= minToFree).min().orElseThrow());
    }

}
