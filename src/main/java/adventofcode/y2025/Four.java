package adventofcode.y2025;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.util.FluentBitSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Four {
    static final int FEWER_THAN_ADJACENT_ROLL = 4;
    private final List<FluentBitSet> input = new ArrayList<>();
    private final List<Position> toRemove = new ArrayList<>();
    private static final FluentBitSet EMPTY_BS = new FluentBitSet(0);

    private int lineSize;

    public static void main(String[] args) throws IOException {
        System.out.println("-------Part 1-------------");
        var inputPath = Path.of(args[0]);
        var result = Four.parse(Files.lines(inputPath));
        var start = Instant.now();
        int nbAccessibleRoll = result.nbAccessibleRoll(Integer.parseInt(args[1]));
        var end = Instant.now();
        System.out.println("duration = " + Duration.between(start, end));
        System.out.println("result = " + result);
        System.out.println("nbAccessibleRoll = " + nbAccessibleRoll);
        System.out.println("------- Part 2--------");
    }

    public static Four parse(Stream<String> lines) {
        return lines.map(s -> {
            var bs = new FluentBitSet(s.length());
            IntStream.range(0, s.length()).forEachOrdered(i -> {
                switch (s.charAt(i)) {
                    case '.' -> bs.clear(i);
                    case '@' -> bs.set(i);
                }
            });
            return bs;
        }).collect(Four::new, (f, bs) -> f.getInput().add(bs), (a, b) -> a.getInput().addAll(b.getInput()));
    }

    int nbAccessibleRoll(int fewerThanAdjacentRoll) {
        for (int row = 0; row < getInput().size(); row++) {
            for (int col = 0; col < getLineSize(); col++) {
                if (input.get(row).get(col)) {
                    if (nbAdjacentRoll(row, col) < fewerThanAdjacentRoll) {
                        toRemove.add(new Position(row, col));
                    }
                }
            }
        }
        return toRemove.size();
    }

    int nbAdjacentRoll(int rowIndex, int colIndex) {
        var self = getInput().get(rowIndex).get(colIndex);
        if (!self) throw new IllegalArgumentException("No roll at position %d:%d".formatted(rowIndex, colIndex));
        var bs = new BitSet(8);
        var lineUp = (rowIndex != 0) ? getInput().get(rowIndex - 1) : EMPTY_BS;
        boolean hasPrevious = colIndex != 0;
        if (!lineUp.isEmpty()) {
            bs.set(0, hasPrevious && lineUp.get(colIndex - 1));
            bs.set(1, lineUp.get(colIndex));
            bs.set(2, lineUp.get(colIndex + 1));
        }
        var line = getInput().get(rowIndex);
        bs.set(3, hasPrevious && line.get(colIndex - 1));
        bs.set(4, line.get(colIndex + 1));
        var lineDown = (rowIndex != getInput().size() - 1) ? getInput().get(rowIndex + 1) : EMPTY_BS;
        if (!lineDown.isEmpty()) {
            bs.set(5, hasPrevious && lineDown.get(colIndex - 1));
            bs.set(6, lineDown.get(colIndex));
            bs.set(7, lineDown.get(colIndex + 1));
        }
        return bs.cardinality();
    }

    public void removeAccessible() {
        toRemove.stream().parallel().forEach(pos -> input.get(pos.row()).clear(pos.col()));
        toRemove.clear();
    }

    public int nbRemovable() {
        var sum = 0;
        for (var nbAccessibleRoll = nbAccessibleRoll(FEWER_THAN_ADJACENT_ROLL); nbAccessibleRoll > 0; nbAccessibleRoll = nbAccessibleRoll(FEWER_THAN_ADJACENT_ROLL)) {
            removeAccessible();
            sum += nbAccessibleRoll;
        }
        return sum;
    }

    public List<FluentBitSet> getInput() {
        return input;
    }

    public int getLineSize() {
        if (lineSize == 0) {
            lineSize = input.stream().mapToInt(FluentBitSet::length).max().orElse(0);
        }
        return lineSize;
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("lineSize", lineSize)
                .toString();
    }

    record Position(int row, int col) {
    }
}