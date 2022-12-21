package com.github.pkovacs.aoc.y2022;

import java.util.List;
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
            dfs(new byte[] { 0, 0, 0, 0 }, new byte[] { 1, 0, 0, 0 }, maxTime);
            return bestResult;
        }

        void dfs(byte[] res, byte[] robots, int time) {
            // Update best result
            int idleResult = res[GEODE] + robots[GEODE] * time;
            bestResult = Math.max(bestResult, idleResult);

            // Check upper bound for this branch
            int maxResult = idleResult + time * (time - 1) / 2;
            if (maxResult <= bestResult) {
                return;
            }

            for (int i = 3; i >= 0; i--) {
                // Attempt to build a robot of type i as the next one (after waiting 0 or more minutes as necessary)
                var cost = costs[i];
                if (robots[i] == maxRobotCount[i]
                        || IntStream.range(0, 4).anyMatch(j -> cost[j] > 0 && robots[j] == 0)) {
                    continue;
                }

                int timeToWait = Math.max(0,
                        IntStream.range(0, 4)
                                .filter(j -> cost[j] > 0)
                                .map(j -> (cost[j] - res[j] + robots[j] - 1) / robots[j])
                                .max().orElseThrow());

                int newTime = time - 1 - timeToWait;
                if (newTime >= minRobotRemainingTime[i]) {
                    var newRes = res.clone();
                    IntStream.range(0, 4).forEach(j -> newRes[j] += (timeToWait + 1) * robots[j] - cost[j]);
                    var newRobots = robots.clone();
                    newRobots[i]++;

                    dfs(newRes, newRobots, newTime);

                    // Additional heuristic: if we can build a geode robot without waiting, then we should do that,
                    // other options can be ignored. And it's also true for the first instance of any other robot type.
                    boolean importantRobot = i == GEODE || robots[i] == 0;
                    if (timeToWait == 0 && importantRobot) {
                        break;
                    }
                }
            }
        }

    }

}
