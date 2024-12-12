package com.github.pkovacs.aoc.y2022;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import com.github.pkovacs.util.Utils;

public class Day05 {

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines, 1));
        System.out.println("Part 2: " + solve(lines, 2));
    }

    private static String solve(List<String> lines, int part) {
        int n = lines.indexOf("") - 1;
        int stackCount = (lines.get(n - 1).length() + 1) / 4;

        var stacks = new ArrayList<Stack<Character>>();
        for (int i = 0; i < stackCount; i++) {
            stacks.add(new Stack<>());
        }

        for (int k = n - 1; k >= 0; k--) {
            var line = lines.get(k);
            for (int i = 0; i < stackCount; i++) {
                char ch = line.length() > 1 + i * 4 ? line.charAt(1 + i * 4) : ' ';
                if (ch != ' ') {
                    stacks.get(i).add(ch);
                }
            }
        }

        for (int k = n + 2; k < lines.size(); k++) {
            var d = Utils.parseInts(lines.get(k));
            var from = stacks.get(d[1] - 1);
            var to = stacks.get(d[2] - 1);
            if (part == 2) {
                var tmp = new Stack<Character>();
                for (int i = 0; i < d[0]; i++) {
                    tmp.add(from.pop());
                }
                from = tmp;
            }
            for (int i = 0; i < d[0]; i++) {
                to.add(from.pop());
            }
        }

        return stacks.stream().map(Stack::peek).map(String::valueOf).collect(Collectors.joining());
    }

}
