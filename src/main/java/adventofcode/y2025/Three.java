package adventofcode.y2025;

import org.apache.commons.lang3.IntegerRange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.lang.Math.max;

public class Three {
    public static void main(String[] args) throws IOException {
        try (var lines = Files.lines(Path.of(args[0]))) {
            var totalOutputJoltage = lines.map(Three::parseBank)
                    .map(bank -> new BankAndIndexes(bank, Three.getIndexOfMax(bank)))
                    .mapToInt(bi -> Three.maxOfBank(bi.bank(), bi.indexes()))
                    .sum();

            System.out.println("totalOutputJoltage = " + totalOutputJoltage);
        }
    }

    static int[] parseBank(String bank) {
        return bank.chars().map(c -> Character.digit(c, 10)).toArray();
    }

    static int maxOfBank(int[] bank, IndexOfMax indexes) {
        return bank[indexes.idx1()] * 10 + bank[indexes.idx2()];
    }

    static double maxOfBank(int[] bank, IndexOfMax12 indexes) {
        return bank[indexes.idx1()] * 1e11
                + bank[indexes.idx2()] * 1e10
                + bank[indexes.idx3()] * 1e9
                + bank[indexes.idx4()] * 1e8
                + bank[indexes.idx5()] * 1e7
                + bank[indexes.idx6()] * 1e6
                + bank[indexes.idx7()] * 1e5
                + bank[indexes.idx8()] * 1e4
                + bank[indexes.idx9()] * 1e3
                + bank[indexes.idx10()] * 1e2
                + bank[indexes.idx11()] * 1e1
                + bank[indexes.idx12()];
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

    static IndexOfMax12 getIndexOfMax12(int[] bank) {
        var bankWithIdx = IntStream.range(0, bank.length)
                .mapToObj(i -> new IndexOfMax(i, bank[i]))
                .sorted(Comparator.comparingInt(IndexOfMax::idx2).reversed())
                .toList();
        int[] nextPossibleIndex = new int[1];
        return new IndexOfMax12(
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(0, bank.length - 12).contains(iv.idx1())).findFirst()),
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(max(1, nextPossibleIndex[0]), bank.length - 11).contains(iv.idx1())).findFirst()),
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(max(2, nextPossibleIndex[0]), bank.length - 10).contains(iv.idx1())).findFirst()),
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(max(3, nextPossibleIndex[0]), bank.length - 9).contains(iv.idx1())).findFirst()),
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(max(4, nextPossibleIndex[0]), bank.length - 8).contains(iv.idx1())).findFirst()),
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(max(5, nextPossibleIndex[0]), bank.length - 7).contains(iv.idx1())).findFirst()),
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(max(6, nextPossibleIndex[0]), bank.length - 6).contains(iv.idx1())).findFirst()),
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(max(7, nextPossibleIndex[0]), bank.length - 5).contains(iv.idx1())).findFirst()),
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(max(8, nextPossibleIndex[0]), bank.length - 4).contains(iv.idx1())).findFirst()),
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(max(9, nextPossibleIndex[0]), bank.length - 3).contains(iv.idx1())).findFirst()),
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(max(10, nextPossibleIndex[0]), bank.length - 2).contains(iv.idx1())).findFirst()),
                extractIdx(nextPossibleIndex, bankWithIdx.stream()
                        .filter(iv -> IntegerRange.of(max(11, nextPossibleIndex[0]), bank.length - 1).contains(iv.idx1())).findFirst())
        );

    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static int extractIdx(int[] nextPossibleIndex, Optional<IndexOfMax> index) {
        return index.map(iom -> {
                    if (iom.idx1() >= nextPossibleIndex[0]) nextPossibleIndex[0] = iom.idx1() + 1;
                    return iom;
                })
                .map(IndexOfMax::idx1)
                .orElseThrow(() -> new IllegalArgumentException("Nothing found after " + index));
    }

    private static Predicate<IndexOfMax> nextMax(final IndexOfMax currentMax) {
        return iom -> iom.idx1() > currentMax.idx1();
    }

    record IndexOfMax(int idx1, int idx2) {
    }

    record BankAndIndexes(int[] bank, Three.IndexOfMax indexes) {
    }

    // part 2
    record IndexOfMax12(int idx1, int idx2, int idx3, int idx4, int idx5, int idx6, int idx7, int idx8, int idx9,
                        int idx10, int idx11, int idx12) {
    }

    record BankAndIndexes12(int[] bank, Three.IndexOfMax12 indexes) {
    }
}