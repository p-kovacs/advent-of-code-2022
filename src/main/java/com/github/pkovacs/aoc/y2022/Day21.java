package com.github.pkovacs.aoc.y2022;

import java.util.Map;
import java.util.stream.Collectors;

import com.github.pkovacs.util.Utils;

public class Day21 {

    public static void main(String[] args) {
        var lines = Utils.readLines(AocUtils.getInputPath());

        var monkeys = lines.stream().collect(Collectors.toMap(s -> s.substring(0, 4), s -> s.substring(6)));

        System.out.println("Part 1: " + eval1(monkeys, "root"));
        System.out.println("Part 2: " + eval2(monkeys, "root", 0));
    }

    private static long eval1(Map<String, String> monkeys, String m) {
        if (!monkeys.get(m).contains(" ")) {
            return Long.parseLong(monkeys.get(m));
        } else {
            var p = monkeys.get(m).split(" ");
            return switch (p[1].charAt(0)) {
                case '+' -> eval1(monkeys, p[0]) + eval1(monkeys, p[2]);
                case '-' -> eval1(monkeys, p[0]) - eval1(monkeys, p[2]);
                case '*' -> eval1(monkeys, p[0]) * eval1(monkeys, p[2]);
                case '/' -> eval1(monkeys, p[0]) / eval1(monkeys, p[2]);
                default -> throw new IllegalArgumentException();
            };
        }
    }

    private static long eval2(Map<String, String> monkeys, String m, long goal) {
        if (!monkeys.get(m).contains(" ")) {
            return goal; // it must be "humn"
        } else {
            var p = monkeys.get(m).split(" ");
            var humLeft = hasHuman(monkeys, p[0]);
            var hum = humLeft ? p[0] : p[2];
            var refValue = eval1(monkeys, humLeft ? p[2] : p[0]);

            if (m.equals("root")) {
                return eval2(monkeys, hum, refValue);
            } else {
                return switch (p[1].charAt(0)) {
                    case '+' -> eval2(monkeys, hum, goal - refValue);
                    case '-' -> eval2(monkeys, hum, humLeft ? goal + refValue : refValue - goal);
                    case '*' -> eval2(monkeys, hum, goal / refValue);
                    case '/' -> eval2(monkeys, hum, humLeft ? goal * refValue : refValue / goal);
                    default -> throw new IllegalArgumentException();
                };
            }
        }
    }

    private static boolean hasHuman(Map<String, String> monkeys, String m) {
        if (!monkeys.get(m).contains(" ")) {
            return m.equals("humn");
        } else {
            var p = monkeys.get(m).split(" ");
            return hasHuman(monkeys, p[0]) || hasHuman(monkeys, p[2]);
        }
    }

}
