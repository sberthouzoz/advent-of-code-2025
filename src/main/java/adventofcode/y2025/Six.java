package adventofcode.y2025;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.LongBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Six {
    private static final String OPERATOR_CHARS = "+*";
    private List<Long> results;
    private List<Operator> operators;

    public Six(List<Operator> operators, List<Long> longs) {
        this.operators = operators;
        this.results = Collections.synchronizedList(longs);
    }

    public static Six parse(String input) {
        var lineOper = input.lines().dropWhile(s -> !StringUtils.containsAny(s, OPERATOR_CHARS)).findFirst().orElseThrow();
        var operators = Arrays.stream(StringUtils.split(lineOper)).map(Operator::valueOfChar).toList();
        return new Six(operators, new ArrayList<>());
    }

    public void partOne(String input) {
        var firstline = input.lines().findFirst().orElseThrow();
        Arrays.stream(StringUtils.split(firstline)).forEach(s -> results.add(Long.parseLong(s)));
        input.lines().parallel()
                .filter(s -> !firstline.equals(s))
                .filter(s -> StringUtils.containsNone(s, OPERATOR_CHARS))
                .forEach(line -> {
                    var splitted = StringUtils.split(line);
                    IntStream.range(0, splitted.length).parallel().forEach(idx -> results.set(idx, operators.get(idx).oper(results.get(idx), Long.parseLong(splitted[idx]))));
                });
    }

    public long summingResults() {
        return results.parallelStream().mapToLong(Long::longValue).sum();
    }

    enum Operator {
        PLUS("+", Long::sum), MULT("*", (a, b) -> a * b);
        private static Map<String, Operator> ofChar;

        static {
            ofChar = Arrays.stream(Operator.values()).collect(Collectors.toMap(op -> op.c, Function.identity()));
        }

        private String c;
        private LongBinaryOperator op;

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