package adventofcode.y2025;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.function.LongBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Six {
    private static final String OPERATOR_CHARS = "+*";
    private final List<Long> results;
    private final List<Operator> operators;

    public static void main(String[] args) throws IOException {
        var file = Path.of(args[0]);
        var obj = Six.parse(file);
        var start = Instant.now();
        obj.partOne(file);
        var end = Instant.now();
        System.out.println("Duration = " + Duration.between(start, end));
        System.out.println("obj.summingResults() = " + obj.summingResults());
    }

    public Six(List<Operator> operators, List<Long> longs) {
        this.operators = operators;
        this.results = Collections.synchronizedList(longs);
    }

    public static Six parse(String input) {
        var lineOper = input.lines().dropWhile(s -> !StringUtils.containsAny(s, OPERATOR_CHARS)).findFirst().orElseThrow();
        var operators = Arrays.stream(StringUtils.split(lineOper)).map(Operator::valueOfChar).toList();
        return new Six(operators, new ArrayList<>());
    }

    public static Six parse(Path input) throws IOException {
        try (var lines = Files.lines(input)) {
            var lineOper = lines.dropWhile(s -> !StringUtils.containsAny(s, OPERATOR_CHARS)).findFirst().orElseThrow();
            var operators = Arrays.stream(StringUtils.split(lineOper)).map(Operator::valueOfChar).toList();
            return new Six(operators, new ArrayList<>());
        }
    }

    public void partOne(String input) {
        var firstLine = input.lines().findFirst().orElseThrow();
        partOne(firstLine, input.lines());
    }

    private void partOne(String firstLine, Stream<String> lines) {
        Arrays.stream(StringUtils.split(firstLine)).forEach(s -> results.add(Long.parseLong(s)));
        lines.parallel()
                .filter(s -> !firstLine.equals(s))
                .filter(s -> StringUtils.containsNone(s, OPERATOR_CHARS))
                .forEach(line -> {
                    var splitted = StringUtils.split(line);
                    IntStream.range(0, splitted.length).parallel().forEach(idx -> results.set(idx, operators.get(idx).oper(results.get(idx), Long.parseLong(splitted[idx]))));
                });
    }

    public void partOne(Path input) throws IOException {
        try (var lines = Files.lines(input)) {
            var firstLine = lines.findFirst().orElseThrow();
            partOne(firstLine, Files.lines(input));
        }
    }

    public long summingResults() {
        return results.parallelStream().mapToLong(Long::longValue).sum();
    }

    public enum Operator {
        PLUS("+", Long::sum), MULT("*", (a, b) -> a * b);
        private static final Map<String, Operator> ofChar;

        static {
            ofChar = Arrays.stream(Operator.values()).collect(Collectors.toMap(op -> op.c, Function.identity()));
        }

        private final String c;
        private final LongBinaryOperator op;

        Operator(String character, LongBinaryOperator op) {
            c = character;
            this.op = op;
        }

        static Operator valueOfChar(String c) {
            return ofChar.get(c);
        }

        public long oper(long a, long b) {
            return op.applyAsLong(a, b);
        }
    }
}