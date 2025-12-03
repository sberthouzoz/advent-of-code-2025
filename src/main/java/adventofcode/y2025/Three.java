package adventofcode.y2025;

import java.util.Comparator;
import java.util.stream.IntStream;

public class Three {
    static int[] parseBank(String bank) {
        return bank.chars().map(c -> Character.digit(c, 10)).toArray();
    }

    static IndexOfMax getIndexOfMax(int[] bank) {
        var maxIdx = IntStream.range(0, bank.length)
                .mapToObj(i -> new IndexOfMax(i, bank[i]))
                .sorted(Comparator.comparingInt(IndexOfMax::idx2).reversed())
                .limit(2)
                .mapToInt(IndexOfMax::idx1)
                .toArray();
        return new IndexOfMax(maxIdx[0], maxIdx[1]);
    }

    record IndexOfMax(int idx1, int idx2) {
    }
}