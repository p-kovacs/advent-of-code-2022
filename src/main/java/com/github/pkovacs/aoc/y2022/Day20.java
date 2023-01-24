package com.github.pkovacs.aoc.y2022;

import java.util.Arrays;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.Utils;

public class Day20 {

    public static void main(String[] args) {
        var ints = Utils.readInts(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(ints, 1, 1));
        System.out.println("Part 2: " + solve(ints, 10, 811589153L));
    }

    private static long solve(int[] ints, int rounds, long multiplier) {
        int n = ints.length;

        var items = Arrays.stream(ints).mapToObj(i -> new Item(i * multiplier)).toList();
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            items.get(i).next = items.get(j);
            items.get(j).prev = items.get(i);
        }

        int m = n - 1;
        for (int r = 0; r < rounds; r++) {
            for (var it : items) {
                long k = (it.v % m + m) % m;
                it.next.prev = it.prev;
                it.prev.next = it.next;
                var p = it.prev;
                for (int i = 0; i < k; i++) {
                    p = p.next;
                }
                it.next = p.next;
                it.prev = p;
                it.next.prev = it;
                it.prev.next = it;
            }
        }

        var p = items.stream().filter(it -> it.v == 0).findFirst().orElseThrow();
        long sum = 0;
        for (int i = 0; i <= 3000; i++) {
            if (i % 1000 == 0) {
                sum += p.v;
            }
            p = p.next;
        }

        return sum;
    }

    private static class Item {

        final long v;
        Item prev;
        Item next;

        public Item(long i) {
            v = i;
        }

    }

}
