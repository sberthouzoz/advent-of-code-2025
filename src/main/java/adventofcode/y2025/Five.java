package adventofcode.y2025;

import org.apache.commons.lang3.LongRange;
import org.apache.commons.lang3.math.NumberUtils;
import org.roaringbitmap.longlong.Roaring64NavigableMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public class Five {
    private List<LongRange> freshIngredients;
    private final long[] availableIngredients;

    public static void main(String[] args) throws IOException {
        var inputFile = Path.of(args[0]);
        var db = Five.parse(Files.lines(inputFile));
        var start = Instant.now();
        var result = db.nbFresh();
        var end = Instant.now();
        System.out.println("Duration = " + Duration.between(start, end));
        System.out.println("result = " + result);
        System.out.println("------Part Two------");
        start = Instant.now();
        result = db.nbPossiblyFresh();
        end = Instant.now();
        System.out.println("[part 2] Duration = " + Duration.between(start, end));
        System.out.println("[part 2] result = " + result);
    }

    public static Five parse(Stream<String> database) {
        var availableIngredients = Collections.synchronizedList(new ArrayList<Long>());
        var freshIngredients = Collections.synchronizedList(new ArrayList<LongRange>());

        database.parallel().forEach(line -> {
            if (line.contains("-")) {
                freshIngredients.add(
                        LongRange.of(
                                Long.parseLong(line, 0, line.indexOf("-"), 10),
                                Long.parseLong(line, line.indexOf("-") + 1, line.length(), 10)
                        )
                );
            }
            if (NumberUtils.isParsable(line)) {
                availableIngredients.add(Long.parseLong(line));
            }
        });
        freshIngredients.sort(Comparator.comparingLong(LongRange::getMinimum).thenComparingLong(LongRange::getMaximum));
        return new Five(freshIngredients, availableIngredients.stream().mapToLong(Long::longValue).toArray());
    }

    public Five(List<LongRange> freshIngredients, long[] availableIngredients) {
        this.freshIngredients = freshIngredients;
        this.availableIngredients = availableIngredients;
    }

    private static boolean isOverlapOrAdjacent(LongRange rangeBefore, LongRange rangeAfter) {
        if (rangeBefore.isAfterRange(rangeAfter))
            throw new IllegalArgumentException("rangeBefore (%s) must be before rangeAfter: %s".formatted(rangeBefore, rangeAfter));
        return rangeBefore.isOverlappedBy(rangeAfter) || rangeBefore.getMaximum() + 1 == rangeAfter.getMinimum();
    }

    public long nbPossiblyFresh() {
        var bs = new Roaring64NavigableMap(true);
        var max = freshIngredients.stream().parallel().mapToLong(LongRange::getMaximum).max().orElseThrow();
        var min = freshIngredients.stream().parallel().mapToLong(LongRange::getMinimum).min().orElseThrow();
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        reduceFreshIngredients();
        return freshIngredients.stream().mapToLong(r -> r.getMaximum() - r.getMinimum() + 1).sum();
    }

    private void reduceFreshIngredients() {
        var reduced = new ArrayList<LongRange>();
        var i = 0;
        while (i < freshIngredients.size()) {
            var nextNonOverlapOrAdjacent = nextNonOverlapOrAdjacent(i);
            if (i + 1 != nextNonOverlapOrAdjacent) {
                reduced.add(LongRange.of(freshIngredients.get(i).getMinimum(), freshIngredients.get(nextNonOverlapOrAdjacent).getMaximum()));
                i = nextNonOverlapOrAdjacent + 1;
            } else {
                reduced.add(freshIngredients.get(i));
                i++;
            }
        }
        freshIngredients = reduced;
    }

    public long nbFresh() {
        return Arrays.stream(availableIngredients).parallel().filter(this::isFresh).count();
    }

    public boolean isFresh(long ingredientId) {
        return freshIngredients.stream().anyMatch(range -> range.contains(ingredientId));
    }

    public List<LongRange> getFreshIngredients() {
        return Collections.unmodifiableList(freshIngredients);
    }

    public long[] getAvailableIngredients() {
        return Arrays.copyOf(availableIngredients, availableIngredients.length);
    }

    private int nextNonOverlapOrAdjacent(int currentIndex) {
        var startRange = freshIngredients.get(currentIndex);
        for (int i = currentIndex; i < freshIngredients.size(); i++) {
            if (!isOverlapOrAdjacent(startRange, freshIngredients.get(i))) {
                return i;
            }
        }
        return currentIndex;
    }
}