package com.github.pkovacs.aoc.y2022;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import com.github.pkovacs.util.Utils;

public class Day17 {

    private static final int CHAMBER_LAYER = 0b100000001;
    private static final int CHAMBER_FLOOR = 0b111111111;

    private static final int[][] ROCKS = new int[][] {
            { 0b000111100 },
            { 0b000010000, 0b000111000, 0b000010000 },
            { 0b000001000, 0b000001000, 0b000111000 },
            { 0b000100000, 0b000100000, 0b000100000, 0b000100000 },
            { 0b000110000, 0b000110000 },
    };

    public static void main(String[] args) {
        var input = Utils.readFirstLine(AocUtils.getInputPath()).trim();

        System.out.println("Part 1: " + solve(input, 2022));
        System.out.println("Part 2: " + solve(input, 1000000000000L));
    }

    private static long solve(String input, long maxRockCount) {
        var chamber = new ArrayList<Integer>();
        chamber.add(CHAMBER_FLOOR);

        int height = 0;
        int index = 0;

        boolean cycleFound = false;
        var heights = new HashMap<Integer, Integer>();
        var rockCounts = new HashMap<Integer, Long>();
        var towerTops = new HashMap<Integer, List<Integer>>();
        long skippedHeight = 0;

        long rockCount = 0;
        while (rockCount < maxRockCount) {
            int rockIndex = (int) (rockCount % ROCKS.length);

            // We occasionally check if a cycle is found or not. If the same input index is seen the 3rd time when
            // a rock of shape 0 begins to fall, then we compare the top layers of the tower to the previous state.
            // If the layers generated between the 2nd and 3rd occurrence are the same as the layers between the
            // 1st and 2nd occurrence, then we found a cycle.
            if (!cycleFound && rockIndex == 0) {
                if (heights.containsKey(index)) {
                    int prevHeight = heights.get(index);
                    var towerTop = chamber.subList(prevHeight + 1, height + 1);
                    if (towerTop.equals(towerTops.get(index))) {
                        cycleFound = true;
                        long cycleRockCount = rockCount - rockCounts.get(index);
                        long cycles = (maxRockCount - rockCount) / cycleRockCount;
                        rockCount += cycles * cycleRockCount;
                        skippedHeight = cycles * (height - prevHeight);
                        continue;
                    }
                    towerTops.put(index, new ArrayList<>(towerTop));
                }
                heights.put(index, height);
                rockCounts.put(index, rockCount);
            }

            int[] rock = ROCKS[rockIndex];
            int rockTop = height + 3 + rock.length;
            addLayers(chamber, rockTop);

            for (; !hasCollision(chamber, rock, rockTop); rockTop--) {
                boolean left = input.charAt(index) == '<';
                index = (index + 1) % input.length();

                int[] pushed = pushRock(rock, left);
                rock = hasCollision(chamber, pushed, rockTop) ? rock : pushed;
            }

            addRock(chamber, rock, rockTop + 1);

            while (chamber.get(height + 1) != CHAMBER_LAYER) {
                height++;
            }

            rockCount++;
        }

        return height + skippedHeight;
    }

    private static void addLayers(List<Integer> chamber, int rockTop) {
        while (chamber.size() <= rockTop) {
            chamber.add(CHAMBER_LAYER);
        }
    }

    private static int[] pushRock(int[] rock, boolean left) {
        int[] pushed = rock.clone();
        IntStream.range(0, pushed.length).forEach(i -> pushed[i] = left ? pushed[i] << 1 : pushed[i] >> 1);
        return pushed;
    }

    private static boolean hasCollision(List<Integer> chamber, int[] rock, int rockTop) {
        return IntStream.range(0, rock.length).anyMatch(i -> (rock[i] & chamber.get(rockTop - i)) != 0);
    }

    private static void addRock(List<Integer> chamber, int[] rock, int rockTop) {
        IntStream.range(0, rock.length).forEach(i -> chamber.set(rockTop - i, chamber.get(rockTop - i) | rock[i]));
    }

}
