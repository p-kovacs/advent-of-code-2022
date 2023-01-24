package com.github.pkovacs.aoc.y2022;

import java.util.List;

import com.github.pkovacs.aoc.AocUtils;
import com.github.pkovacs.util.Utils;
import com.github.pkovacs.util.data.CharTable;
import com.github.pkovacs.util.data.Direction;
import com.github.pkovacs.util.data.Cell;

public class Day22 {

    public static void main(String[] args) {
        var blocks = Utils.collectLineBlocks(Utils.readString(AocUtils.getInputPath()));

        var table = readTable(blocks.get(0));
        var cmd = readCommands(blocks.get(1).get(0));

        System.out.println("Part 1: " + solve(table, cmd, 1));
        System.out.println("Part 2: " + solve(table, cmd, 2));
    }

    private static CharTable readTable(List<String> matrix) {
        var table = new CharTable(matrix.size() + 2, matrix.get(0).length() + 2, ' ');
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).length(); j++) {
                table.set(i + 1, j + 1, matrix.get(i).charAt(j));
            }
        }
        return table;
    }

    private static String[] readCommands(String str) {
        return str.replace("L", ":L:").replace("R", ":R:").split(":");
    }

    private static int solve(CharTable table, String[] cmd, int part) {
        Cell cell = table.row(1).filter(c -> table.get(c) == '.').findFirst().orElseThrow();
        var dir = Direction.EAST;
        for (var step : cmd) {
            switch (step) {
                case "L" -> dir = dir.rotateLeft();
                case "R" -> dir = dir.rotateRight();
                default -> {
                    int k = Integer.parseInt(step);
                    for (int i = 0; i < k; i++) {
                        var c = cell.neighbor(dir);
                        var d = dir;

                        if (table.get(c) == ' ' && part == 1) {
                            // Wrap in part 1
                            c = switch (dir) {
                                case NORTH -> new Cell(table.rowCount() - 1, c.col());
                                case SOUTH -> new Cell(0, c.col());
                                case WEST -> new Cell(c.row(), table.colCount() - 1);
                                case EAST -> new Cell(c.row(), 0);
                            };
                            while (table.get(c) == ' ') {
                                c = c.neighbor(dir);
                            }
                        } else if (table.get(c) == ' ' && part == 2) {
                            // Wrap in part 2
                            // Note: it seems that every input has the same layout (which is different from the layout
                            // of the example). So it seems to be acceptable to hardcode the transformations of the
                            // indices and the direction for this layout.
                            int a = (c.row() + 49) / 50;
                            int b = (c.col() + 49) / 50;
                            if (a == 0 && b == 2) {
                                c = new Cell(c.col() + 100, 1);
                                d = Direction.EAST;
                            } else if (a == 0 && b == 3) {
                                c = new Cell(200, c.col() - 100);
                                d = Direction.NORTH;
                            } else if (a == 1 && b == 4) {
                                c = new Cell(151 - c.row(), 100);
                                d = Direction.WEST;
                            } else if (a == 2 && b == 3 && d == Direction.SOUTH) {
                                c = new Cell(c.col() - 50, 100);
                                d = Direction.WEST;
                            } else if (a == 2 && b == 3 && d == Direction.EAST) {
                                c = new Cell(50, c.row() + 50);
                                d = Direction.NORTH;
                            } else if (a == 3 && b == 3) {
                                c = new Cell(151 - c.row(), 150);
                                d = Direction.WEST;
                            } else if (a == 4 && b == 2 && d == Direction.SOUTH) {
                                c = new Cell(c.col() + 100, 50);
                                d = Direction.WEST;
                            } else if (a == 4 && b == 2 && d == Direction.EAST) {
                                c = new Cell(150, c.row() - 100);
                                d = Direction.NORTH;
                            } else if (a == 5 && b == 1) {
                                c = new Cell(1, c.col() + 100);
                                d = Direction.SOUTH;
                            } else if (a == 4 && b == 0) {
                                c = new Cell(1, c.row() - 100);
                                d = Direction.SOUTH;
                            } else if (a == 3 && b == 0) {
                                c = new Cell(151 - c.row(), 51);
                                d = Direction.EAST;
                            } else if (a == 2 && b == 1 && d == Direction.NORTH) {
                                c = new Cell(c.col() + 50, 51);
                                d = Direction.EAST;
                            } else if (a == 2 && b == 1 && d == Direction.WEST) {
                                c = new Cell(101, c.row() - 50);
                                d = Direction.SOUTH;
                            } else if (a == 1 && b == 1) {
                                c = new Cell(151 - c.row(), 1);
                                d = Direction.EAST;
                            } else {
                                throw new IllegalStateException(); // should not occur :)
                            }
                        }

                        if (table.get(c) == '#') {
                            break;
                        }
                        cell = c;
                        dir = d;
                    }
                }
            }
        }

        return 1000 * cell.row() + 4 * cell.col() + (dir.ordinal() + 3) % 4;
    }

}
