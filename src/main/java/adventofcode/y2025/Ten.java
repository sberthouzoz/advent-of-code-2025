package adventofcode.y2025;

import others.day10.GaussianElimination;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Ten {
    public static void main(String[] args) throws IOException {
        var path = Path.of(args[0]);
        var start = Instant.now();
        var partOne = solvePart1(Files.lines(path));
        var end = Instant.now();
        System.out.println("Duration = " + Duration.between(start, end));
        System.out.println("partOne = " + partOne);
        start = Instant.now();
        var partTwo = solvePart2(Files.lines(path));
        end = Instant.now();
        System.out.println("[Part two] Duration = " + Duration.between(start, end));
        System.out.println("partTwo = " + partTwo);
    }

    public static int solvePart1(Stream<String> lines) {
        return lines.map(Machine::parse).mapToInt(Machine::solvePart1).sum();
    }

    public static int solvePart2(Stream<String> lines) {
        return lines.map(Machine::parse).mapToInt(Machine::solvePart2).sum();
    }
}

record Lights(BitSet bs) {
    Lights() {
        this(new BitSet());
    }

    static Lights parse(String s) {
        var res = new Lights();
        IntStream.range(0, s.length()).forEach(i -> {
            var ch = s.charAt(i);
            if (ch == '#') {
                res.bs.set(i - 1);
            }
        });
        return res;
    }

    Lights toggle(ButtonWiring buttonWiring) {
        // prevent modification of bitset. Since xor() modify the current bitset, we use a clone
        var clone = (BitSet) bs.clone();
        clone.xor(buttonWiring.bs());
        return new Lights(clone);
    }
}

record ButtonWiring(BitSet bs, int[] indexes) {
    static ButtonWiring parse(String s) {
        var bs = new BitSet();
        String[] split = s.substring(1, s.length() - 1).split(",");
        int[] indexes = new int[split.length];

        for (int i = 0; i < split.length; i++) {
            indexes[i] = Integer.parseInt(split[i]);
            bs.set(indexes[i]);
        }
        return new ButtonWiring(bs, indexes);
    }
}

record ComputationState(Lights lights, int buttonIndex, int depth) {
}

record Machine(Lights lightsGoal, List<ButtonWiring> buttonWirings, int[] joltage) {
    static Machine parse(String line) {
        var split = line.split(" ");
        Lights lightsGoal = Lights.parse(split[0]);

        List<ButtonWiring> buttonWirings = new ArrayList<>();
        for (int i = 1; i < split.length - 1; i++) {
            buttonWirings.add(ButtonWiring.parse(split[i]));
        }

        int[] joltage = parsInts(split[split.length - 1]);
        return new Machine(lightsGoal, buttonWirings, joltage);
    }

    private static int[] parsInts(String s) {
        var split = s.substring(1, s.length() - 1).split(",");
        return Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
    }

    private int numButtonWirings() {
        return buttonWirings.size();
    }

    int solvePart1() {
        Queue<ComputationState> queue = new ArrayDeque<>();
        queue.add(new ComputationState(new Lights(), 0, 0));

        while (!queue.isEmpty()) {
            ComputationState state = queue.poll();

            for (int i = state.buttonIndex(); i < numButtonWirings(); i++) {
                Lights lights = state.lights().toggle(buttonWirings.get(i));
                if (lights.equals(lightsGoal)) {
                    return state.depth() + 1;
                } else {
                    queue.add(new ComputationState(lights, i + 1, state.depth() + 1));
                }
            }
        }
        throw new IllegalArgumentException("No solution found!");
    }

    int solvePart2() {
        int[][] matrix = createMatrix();

        var solutions = new GaussianElimination(matrix).solve();

        if (solutions.isEmpty()) {
            throw new IllegalArgumentException("No solution found!");
        }

        return solutions.stream().mapToInt(arr -> Arrays.stream(arr).sum()).min().orElseThrow();
    }

    private int[][] createMatrix() {
        int numRows = joltage.length;
        int numCols = buttonWirings.size();

        int[][] matrix = new int[numRows][numCols + 1];
        for (int col = 0; col < numCols; col++) {
            ButtonWiring buttonWiring = buttonWirings.get(col);
            for (int lightIndex : buttonWiring.indexes()) {
                matrix[lightIndex][col] = 1;
            }
        }
        for (int row = 0; row < numRows; row++) {
            matrix[row][numCols] = joltage[row];
        }
        return matrix;
    }
}