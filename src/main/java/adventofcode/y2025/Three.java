package adventofcode.y2025;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Three {
    static int[] parseBank(String bank) {
        return bank.chars().map(c -> Character.digit(c, 10)).toArray();
    }

    static int maxOfBank(int[] bank, IndexOfMax indexes) {
        return bank[indexes.idx1()] * 10 + bank[indexes.idx2()];
    }

    static IndexOfMax getIndexOfMax(int[] bank) {
        var bankWithIdx = IntStream.range(0, bank.length)
                .mapToObj(i -> new IndexOfMax(i, bank[i]))
                .sorted(Comparator.comparingInt(IndexOfMax::idx2).reversed())
                .toList();
        if (bankWithIdx.getFirst().idx1() == bank.length - 1) { // the max number is at the end, can't be used as first
            return new IndexOfMax(bankWithIdx.get(1).idx1(), bankWithIdx.getFirst().idx1());
        } else {
            return new IndexOfMax(
                    bankWithIdx.getFirst().idx1(),
                    bankWithIdx.stream().filter(nextMax(bankWithIdx.getFirst()))
                            .findFirst()
                            .map(IndexOfMax::idx1)
                            .orElseThrow(() -> new IllegalArgumentException("Nothing found after " + bankWithIdx.getFirst())));
        }
    }

    private static Predicate<IndexOfMax> nextMax(final IndexOfMax currentMax) {
        return iom -> iom.idx1() > currentMax.idx1();
    }

    record IndexOfMax(int idx1, int idx2) {
    }
}