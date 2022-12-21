package com.github.pkovacs.aoc.y2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.InputUtils;

public class Day19 {

    private static final int GEODE = 3;

    public static void main(String[] args) {
        var lines = InputUtils.readLines(AocUtils.getInputPath());

        System.out.println("Part 1: " + solve1(lines));
        System.out.println("Part 2: " + solve2(lines));
    }

    private static int solve1(List<String> lines) {
        int sum = 0;
        for (int i = 0; i < lines.size(); i++) {
            sum += (i + 1) * new Blueprint(lines.get(i)).calculate(24);
        }
        return sum;
    }

    private static int solve2(List<String> lines) {
        int prod = 1;
        for (int i = 0; i < 3; i++) {
            prod *= new Blueprint(lines.get(i)).calculate(32);
        }
        return prod;
    }

    private static class Blueprint {

        /** The cost of building robots. costs[i][j] is the number of resource j required for building robot i. */
        final int[][] costs;

        /** The max. number of robots of each type to build (based on resource requirements). */
        final int[] maxRobotCount;

        /** The min. remaining time to build robots of each type. */
        final int[] minRobotRemainingTime;

        int bestResult;

        Blueprint(String line) {
            costs = new int[4][4];

            var ints = InputUtils.parseInts(line);
            costs[0][0] = ints[1];
            costs[1][0] = ints[2];
            costs[2][0] = ints[3];
            costs[2][1] = ints[4];
            costs[3][0] = ints[5];
            costs[3][2] = ints[6];

            int maxOreCost = IntStream.range(0, 4).map(i -> costs[i][0]).max().orElseThrow();
            maxRobotCount = new int[] { maxOreCost, costs[2][1], costs[3][2], Integer.MAX_VALUE };
            minRobotRemainingTime = new int[] { 5, 3, 2, 1 };
        }

        int calculate(int maxTime) {
            bestResult = 0;
            dfs(new State(new byte[] { 0, 0, 0, 0 }, new byte[] { 1, 0, 0, 0 }, maxTime));
            return bestResult;
        }

        void dfs(State s) {
            var list = new ArrayList<State>();

            int idleResult = s.res[GEODE] + s.robots[GEODE] * s.time;
            bestResult = Math.max(bestResult, idleResult);

            int maxResult = idleResult + s.time * (s.time - 1) / 2;
            if (maxResult <= bestResult) {
                return;
            }

            for (int i = 3; i >= 0; i--) {
                // Attempt to build a robot of type i as the next one (after waiting 0 or more minutes as necessary)
                var cost = costs[i];
                if (s.robots[i] < maxRobotCount[i]
                        && s.time - 1 >= minRobotRemainingTime[i]
                        && IntStream.range(0, 4).allMatch(j -> cost[j] == 0 || s.robots[j] > 0)) {

                    int timeToWait = Math.max(0,
                            IntStream.range(0, 4)
                                    .filter(j -> cost[j] > 0)
                                    .map(j -> (cost[j] - s.res[j] + s.robots[j] - 1) / s.robots[j])
                                    .max().orElseThrow());

                    int time = s.time - 1 - timeToWait;
                    if (time >= minRobotRemainingTime[i]) {
                        var res = s.res.clone();
                        IntStream.range(0, 4).forEach(j -> res[j] += (timeToWait + 1) * s.robots[j] - cost[j]);
                        var robots = s.robots.clone();
                        robots[i]++;
                        list.add(new State(res, robots, time));
                    }

                    // Additional heuristic: if we can build a geode robot without waiting, then we should do that,
                    // other options can be ignored. And it's also true for the first instance of any other robot type.
                    boolean importantRobot = i == GEODE || s.robots[i] == 0;
                    if (!list.isEmpty() && timeToWait == 0 && importantRobot) {
                        break;
                    }
                }
            }

            list.forEach(this::dfs);
        }

    }

    private static final class State {

        final byte[] res;
        final byte[] robots;
        final int time;

        final int hash;

        State(byte[] res, byte[] robots, int time) {
            this.res = res;
            this.robots = robots;
            this.time = time;
            hash = Objects.hash(Arrays.hashCode(res), Arrays.hashCode(robots), time);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            var other = (State) obj;
            return this.time == other.time
                    && Arrays.equals(res, other.res)
                    && Arrays.equals(robots, other.robots);
        }

        @Override
        public int hashCode() {
            return hash;
        }

    }

}
