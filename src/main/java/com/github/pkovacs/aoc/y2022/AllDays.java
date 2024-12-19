package com.github.pkovacs.aoc.y2022;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * Verifies the solution for each day against the expected answers for my puzzle input files.
 */
public class AllDays {

    private static final List<Day> DAYS = List.of(
            new Day("Day 01", Day01::main,"72718", "213089"),
            new Day("Day 02", Day02::main,"10624", "14060"),
            new Day("Day 03", Day03::main,"8139", "2668"),
            new Day("Day 04", Day04::main,"515", "883"),
            new Day("Day 05", Day05::main,"TDCHVHJTG", "NGCMPJLHV"),
            new Day("Day 06", Day06::main,"1850", "2823"),
            new Day("Day 07", Day07::main,"1989474", "1111607"),
            new Day("Day 08", Day08::main,"1717", "321975"),
            new Day("Day 09", Day09::main,"6037", "2485"),
            new Day("Day 10", Day10::main,"14560", "EKRHEPUZ"),
            new Day("Day 11", Day11::main,"64032", "12729522272"),
            new Day("Day 12", Day12::main,"504", "500"),
            new Day("Day 13", Day13::main,"5196", "22134"),
            new Day("Day 14", Day14::main,"1001", "27976"),
            new Day("Day 15", Day15::main,"5073496", "13081194638237"),
            new Day("Day 16", Day16::main,"1871", "2416"),
            new Day("Day 17", Day17::main,"3067", "1514369501484"),
            new Day("Day 18", Day18::main,"4370", "2458"),
            new Day("Day 19", Day19::main,"1177", "62744"),
            new Day("Day 20", Day20::main,"7004", "17200008919529"),
            new Day("Day 21", Day21::main,"124765768589550", "3059361893920"),
            new Day("Day 22", Day22::main,"13566", "11451"),
            new Day("Day 23", Day23::main,"4249", "980"),
            new Day("Day 24", Day24::main,"221", "739"),
            new Day("Day 25", Day25::main,"2=001=-2=--0212-22-2", "0")
    );

    public static void main(String[] args) {
        String format = "%-12s%-8s%-8s%8s%n";
        System.out.printf(format, "Day", "Part 1", "Part 2", "Time");

        DAYS.stream().filter(day -> day.mainMethod != null).forEach(day -> {
            long start = System.nanoTime();
            var results = runDay(day);
            long time = (System.nanoTime() - start) / 1_000_000L;

            System.out.printf(format, day.name, evaluate(day, results, 0), evaluate(day, results, 1), time + " ms");
        });
    }

    private static String evaluate(Day day, List<String> results, int index) {
        var expected = index == 0 ? day.expected1 : day.expected2;
        return results.size() == 2 && expected.equals(results.get(index)) ? "\u2714" : "FAILED";
    }

    private static List<String> runDay(Day day) {
        var origOut = System.out;
        try {
            var out = new ByteArrayOutputStream(200);
            System.setOut(new PrintStream(out));
            day.mainMethod.accept(null);
            return out.toString(StandardCharsets.UTF_8).lines().map(l -> l.split(": ")[1]).toList();
        } catch (Exception e) {
            return List.of();
        } finally {
            System.setOut(origOut);
        }
    }

    private record Day(String name, Consumer<String[]> mainMethod, String expected1, String expected2) {}

}
