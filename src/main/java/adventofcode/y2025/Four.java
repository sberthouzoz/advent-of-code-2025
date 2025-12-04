package adventofcode.y2025;

import org.apache.commons.lang3.util.FluentBitSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Four {
    private final List<FluentBitSet> input = new ArrayList<>();

    public static Four parse(String input) {
        return input.lines().map(s -> {
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

    public List<FluentBitSet> getInput() {
        return input;
    }
}