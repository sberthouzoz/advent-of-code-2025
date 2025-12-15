package adventofcode.y2025;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Ten {
    public static int solvePart1(Stream<String> lines) {
        return lines.map(Machine::parse).mapToInt(Machine::solvePart1).sum();
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
}