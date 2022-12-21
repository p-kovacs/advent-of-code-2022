package com.github.pkovacs.aoc.y2022;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class AocSolutionsTest {

    private final PrintStream origOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private static Stream<Arguments> test() {
        return Stream.of(
                new Arguments("Day01", Day01::main, "72718", "213089"),
                new Arguments("Day02", Day02::main, "10624", "14060"),
                new Arguments("Day03", Day03::main, "8139", "2668"),
                new Arguments("Day04", Day04::main, "515", "883"),
                new Arguments("Day05", Day05::main, "TDCHVHJTG", "NGCMPJLHV"),
                new Arguments("Day06", Day06::main, "1850", "2823"),
                new Arguments("Day07", Day07::main, "1989474", "1111607"),
                new Arguments("Day08", Day08::main, "1717", "321975"),
                new Arguments("Day09", Day09::main, "6037", "2485"),
                new Arguments("Day10", Day10::main, "14560", "EKRHEPUZ"),
                new Arguments("Day11", Day11::main, "64032", "12729522272"),
                new Arguments("Day12", Day12::main, "504", "500"),
                new Arguments("Day13", Day13::main, "5196", "22134"),
                new Arguments("Day14", Day14::main, "1001", "27976"),
                new Arguments("Day15", Day15::main, "5073496", "13081194638237"),
                new Arguments("Day16", Day16::main, "1871", "2416"),
                new Arguments("Day17", Day17::main, "3067", "1514369501484"),
                new Arguments("Day18", Day18::main, "4370", "2458"),
                new Arguments("Day19", Day19::main, "1177", "62744"),
                new Arguments("Day20", Day20::main, "0", "0"),
                new Arguments("Day21", Day21::main, "0", "0"),
                new Arguments("Day22", Day22::main, "0", "0"),
                new Arguments("Day23", Day23::main, "0", "0"),
                new Arguments("Day24", Day24::main, "0", "0"),
                new Arguments("Day25", Day25::main, "0", "0")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void test(Arguments args) {
        args.mainMethod().accept(null);
        assertSolution1(args.expected1());
        assertSolution2(args.expected2());
    }

    @BeforeEach
    public void changeSystemOut() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void restoreSystemOut() {
        System.setOut(origOut);
    }

    void assertSolution1(String expected) {
        assertSolution(0, expected);
    }

    void assertSolution2(String expected) {
        assertSolution(1, expected);
    }

    private void assertSolution(int index, String expected) {
        var output = outputStream.toString(StandardCharsets.UTF_8);
        var parts = output.split(System.lineSeparator())[index].split(": ");
        var value = parts.length < 2 ? "" : parts[1].trim();
        Assertions.assertEquals(expected, value);
    }

    private static record Arguments(String name, Consumer<String[]> mainMethod, String expected1, String expected2) {
        @Override
        public String toString() {
            return name;
        }
    }

}
