package com.github.pkovacs.aoc.y2022;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.UnaryOperator;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;

public class Day11 {

    public static void main(String[] args) {
        var blocks = InputUtils.collectLineBlocks(InputUtils.readString(AocUtils.getInputPath()));

        System.out.println("Part 1: " + solve(blocks, 1));
        System.out.println("Part 2: " + solve(blocks, 2));
    }

    private static long solve(List<List<String>> blocks, int part) {
        var monkeys = blocks.stream().map(Day11::readMonkey).toList();

        // To ensure that the item values don't get too large in part 2, we replace them with their remainder
        // after dividing by the product of all monkeys' "div" values (in fact, it's their LCM, because they are
        // distinct prime numbers). This transformation doesn't change the result of any subsequent "divisible"
        // checks, because only additions and multiplications are applied to the values in part 2.
        long commonMultiple = monkeys.stream().mapToLong(m -> m.div).reduce(1, (a, b) -> a * b);
        UnaryOperator<Long> scaleDown = part == 1 ? (i -> i / 3) : (i -> i % commonMultiple);

        int roundCount = part == 1 ? 20 : 10_000;
        for (int round = 0; round < roundCount; round++) {
            for (var m : monkeys) {
                while (!m.items.isEmpty()) {
                    long i = m.items().remove();
                    long j = scaleDown.apply(m.op.apply(i));
                    monkeys.get(j % m.div == 0 ? m.out1 : m.out2).items.add(j);
                    m.cnt.incrementAndGet();
                }
            }
        }

        var list = monkeys.stream().map(m -> m.cnt.get()).sorted(Comparator.reverseOrder()).toList();
        return list.get(0) * list.get(1);
    }

    private static Monkey readMonkey(List<String> block) {
        var items = new ArrayDeque<Long>();
        Arrays.stream(InputUtils.parseLongs(block.get(1))).forEach(items::add);
        var op = readOp(block.get(2).split(" = ")[1]);
        long div = Long.parseLong(block.get(3).split(" by ")[1]);
        int out1 = Integer.parseInt(block.get(4).split(" monkey ")[1]);
        int out2 = Integer.parseInt(block.get(5).split(" monkey ")[1]);
        return new Monkey(items, op, div, out1, out2, new AtomicLong());
    }

    private static UnaryOperator<Long> readOp(String opStr) {
        if (opStr.equals("old * old")) {
            return i -> i * i;
        } else if (opStr.contains(" * ")) {
            var arg = Long.parseLong(opStr.split(" \\* ")[1]);
            return i -> i * arg;
        } else {
            var arg = Long.parseLong(opStr.split(" \\+ ")[1]);
            return i -> i + arg;
        }
    }

    record Monkey(Queue<Long> items, UnaryOperator<Long> op, long div, int out1, int out2, AtomicLong cnt) {}

}
