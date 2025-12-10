/*
Adapted from https://github.com/zebalu/advent-of-code-2025/blob/master/Day07.java
 */
package others;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Day07 {
    public static void main(String[] args) {
        var start = Instant.now();
        Map<Coord, Long> beamWays = creatPathMap(readInput(args[0]));
        var end = Instant.now();
        System.out.println("parsing Duration = " + Duration.between(start, end));

        start = Instant.now();
        System.out.println("part 1: " + part1(beamWays));
        end = Instant.now();
        System.out.println("part 1 Duration = " + Duration.between(start, end));

        start = Instant.now();
        System.out.println("part 2: " + part2(beamWays));
        end = Instant.now();
        System.out.println("part 2 Duration = " + Duration.between(start, end));
    }

    static long part1(Map<Coord, Long> beamWays) {
        return beamWays.values().stream().filter(v -> v == -1).count();
    }

    static long part2(Map<Coord, Long> beamWays) {
        int lastRow = beamWays.keySet().stream().mapToInt(Coord::y).max().orElseThrow();
        return beamWays.entrySet().stream().filter(e -> e.getKey().y == lastRow).mapToLong(Map.Entry::getValue).sum();
    }

    static Map<Coord, Long> creatPathMap(List<String> input) {
        int startPos = input.getFirst().indexOf("S");
        Map<Coord, Long> beamWays = new HashMap<>();
        SequencedSet<Integer> beams = new LinkedHashSet<>(List.of(startPos));
        beamWays.put(new Coord(startPos, 0), NumberUtils.LONG_ONE);
        for (int row = 1; row < input.size(); row++) {
            String level = input.get(row);
            SequencedSet<Integer> newBeams = new LinkedHashSet<>();
            for (int beam : beams) {
                Coord beamPos = new Coord(beam, row);
                Coord beamPrev = new Coord(beam, row - 1);
                var prev = beamWays.get(beamPrev);
                if (level.charAt(beam) == '.') {
                    newBeams.add(beam);
                    update(beamWays, beamPos, prev);
                } else if (level.charAt(beam) == '^') {
                    int left = beam - 1;
                    int right = beam + 1;
                    beamWays.put(beamPos, NumberUtils.LONG_MINUS_ONE);
                    if (left >= 0) {
                        newBeams.add(left);
                        update(beamWays, new Coord(left, row), prev);
                    }
                    if (right < level.length()) {
                        newBeams.add(right);
                        update(beamWays, new Coord(right, row), prev);
                    }
                }
            }
            beams = newBeams;
        }
        return beamWays;
    }

    static void update(Map<Coord, Long> beamWays, Coord pos, long value) {
        beamWays.merge(pos, value, Long::sum);
    }

    static List<String> readInput(String path) {
        if (path.contains("^")) {
            return path.lines().toList();
        }
        var asPath = Path.of(path);
        try {
            return Files.readAllLines(asPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    record Coord(int x, int y) {
    }
}