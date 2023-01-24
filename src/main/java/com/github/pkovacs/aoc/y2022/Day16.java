package com.github.pkovacs.aoc.y2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;
import com.github.pkovacs.util.alg.Bfs;

public class Day16 {

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve(lines, 1));
        System.out.println("Part 2: " + solve(lines, 2));
    }

    private static int solve(List<String> lines, int part) {
        var names = new ArrayList<String>();
        for (var line : lines) {
            names.add(line.split(" ")[1]);
        }
        var graph = new ArrayList<List<Integer>>();
        var flow = new ArrayList<Integer>();
        for (var line : lines) {
            var parts = InputUtils.parse(line, "Valve %s has flow rate=%d; tunnels? leads? to valves? %s");
            var list = Arrays.stream(parts.get(2).get().split(",")).map(String::trim).map(names::indexOf).toList();
            graph.add(list);
            flow.add(parts.get(1).toInt());
        }
        int nodeCount = names.size();
        var valvesWithFlow = IntStream.range(0, nodeCount).filter(i -> flow.get(i) > 0).toArray();

        // Compute distances between the valves
        var dist = new int[nodeCount][nodeCount];
        IntStream.range(0, nodeCount).forEach(i ->
                Bfs.run(i, graph::get).values().forEach(p -> dist[i][p.endNode()] = (int) p.dist()));

        // Set the "advance" function for state traversal
        Function<State, List<State>> advance = s -> {
            var next = new ArrayList<State>();
            for (int v : valvesWithFlow) {
                int newTime = s.time - dist[s.valve][v] - 1;
                if (newTime > 0 && !s.opened.get(v)) {
                    int newTotal = s.total + flow.get(v) * newTime;
                    var newOpened = (BitSet) s.opened.clone();
                    newOpened.set(v);
                    next.add(new State(v, newTime, newTotal, newOpened));
                }
            }
            return next;
        };

        int maxTime = part == 1 ? 30 : 26;
        var start = new State(names.indexOf("AA"), maxTime, 0, new BitSet());

        if (part == 1) {
            return dfs(start, advance).stream().mapToInt(State::total).max().orElseThrow();
        } else {
            // Collect reachable states for the first round (you)
            var states = dfs(start, advance);

            // Collect the states to be continued and reset the position and the timer
            var toContinue = new ArrayList<State>();
            states.stream()
                    .map(s -> new State(start.valve, maxTime, s.total, s.opened))
                    .sorted(Comparator.comparing(s -> -s.total))
                    .filter(s -> toContinue.stream().noneMatch(prev -> prev.isBetterThan(s)))
                    .forEach(toContinue::add);

            // Find the best result in the second round (elephant)
            return toContinue.stream()
                    .flatMap(s -> dfs(s, advance).stream())
                    .mapToInt(State::total)
                    .max().orElseThrow();
        }
    }

    private static List<State> dfs(State start, Function<State, List<State>> advance) {
        var states = new ArrayList<State>();
        dfs(start, advance, states);
        return states;
    }

    private static void dfs(State s, Function<State, List<State>> advance, List<State> states) {
        states.add(s);
        advance.apply(s).forEach(next -> dfs(next, advance, states));
    }

    private record State(int valve, int time, int total, BitSet opened) {
        public boolean isBetterThan(State other) {
            return other.total <= total && opened.stream().allMatch(other.opened::get);
        }
    }

}
