package adventofcode.y2025;

import org.apache.commons.lang3.IntegerRange;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Five {
    private final List<IntegerRange> freshIngredients;
    private final int[] availableIngredients;

    public Five(List<IntegerRange> freshIngredients, int[] availableIngredients) {
        this.freshIngredients = freshIngredients;
        this.availableIngredients = availableIngredients;
    }

    public static Five parse(Stream<String> database) {
        var availableIngredients = new ArrayList<Integer>();
        var freshIngredients = new ArrayList<IntegerRange>();

        database.parallel().forEach(line -> {
            if (line.contains("-")) {
                freshIngredients.add(
                        IntegerRange.of(
                                Integer.parseInt(line, 0, line.indexOf("-"), 10),
                                Integer.parseInt(line, line.indexOf("-") + 1, line.length(), 10)
                        )
                );
            }
            if (NumberUtils.isParsable(line)) {
                availableIngredients.add(Integer.parseInt(line));
            }
        });
        return new Five(freshIngredients, availableIngredients.stream().mapToInt(Integer::intValue).toArray());
    }

    public List<IntegerRange> getFreshIngredients() {
        return Collections.unmodifiableList(freshIngredients);
    }

    public int[] getAvailableIngredients() {
        return Arrays.copyOf(availableIngredients, availableIngredients.length);
    }
}