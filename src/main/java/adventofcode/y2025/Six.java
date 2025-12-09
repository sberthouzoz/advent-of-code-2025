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
    private final List<OperatorPos> operators;

    public static void main(String[] args) throws IOException {
        var file = Path.of(args[0]);
        var obj = Six.parse(file);
        var start = Instant.now();
        obj.partOne(file);
        var end = Instant.now();
        System.out.println("Duration = " + Duration.between(start, end));
        System.out.println("obj.summingResults() = " + obj.summingResults());
    }

    public Six(List<OperatorPos> operators, List<Long> longs) {
        this.operators = operators;
        this.results = Collections.synchronizedList(longs);
    }

    public static Six parse(String input) {
        var lineOper = input.lines().dropWhile(s -> !StringUtils.containsAny(s, OPERATOR_CHARS)).findFirst().orElseThrow();
        var operators = parseOperatorsLine(lineOper);
        return new Six(operators, new ArrayList<>());
    }

    private static List<OperatorPos> parseOperatorsLine(String lineOper) {
        return IntStream.range(0, lineOper.length())
                .filter(idx -> StringUtils.containsAny(lineOper.substring(idx, idx + 1), OPERATOR_CHARS))
                .mapToObj(idx -> new OperatorPos(Operator.valueOfChar(lineOper.charAt(idx)), idx))
                .toList();
    }

    public static Six parse(Path input) throws IOException {
        try (var lines = Files.lines(input)) {
            var lineOper = lines.dropWhile(s -> !StringUtils.containsAny(s, OPERATOR_CHARS)).findFirst().orElseThrow();
            var operators = parseOperatorsLine(lineOper);
            return new Six(operators, new ArrayList<>());
        }
    }

    public void partOne(String input) {
        var firstLine = input.lines().findFirst().orElseThrow();
        partOne(firstLine, input.lines());
    }

    public void partOne(Path input) throws IOException {
        try (var lines = Files.lines(input)) {
            var firstLine = lines.findFirst().orElseThrow();
            partOne(firstLine, Files.lines(input));
        }
    }

    private void partOne(String firstLine, Stream<String> lines) {
        Arrays.stream(StringUtils.split(firstLine)).forEach(s -> results.add(Long.parseLong(s)));
        lines.parallel()
                .filter(s -> !firstLine.equals(s))
                .filter(s -> StringUtils.containsNone(s, OPERATOR_CHARS))
                .forEach(line -> {
                    var splitted = StringUtils.split(line);
                    IntStream.range(0, splitted.length).forEach(idx -> results.set(idx, operators.get(idx).operator().oper(results.get(idx), Long.parseLong(splitted[idx]))));
                });
    }

    static int getDigitAt(int n, int posPowerOfTen) {
        var pow = powerOfTenAsInt(posPowerOfTen);
        return n / pow % 10;
    }

    static int setDigitAt(int n, int posPowerOfTen) {
        var pow = powerOfTenAsInt(posPowerOfTen);
        return n * pow;
    }

    private static int powerOfTenAsInt(int posPowerOfTen) {
        return (int) Math.pow(10, posPowerOfTen);
    }

    public void partTwo(String input) {
        var lines = input.lines().filter(line -> StringUtils.containsNone(OPERATOR_CHARS)).toList();
        for (var opPos : operators) {
            results.add(opPos.operator().noOp);
        }
        for (var line : lines) {
            for (int i = 0; i < operators.size(); i++) {
                var opPos = operators.get(i);
                var digitPos = (i == operators.size() - 1) ? line.length() - 1 : operators.get(i + 1).position() - opPos.position();
                var len = opPos.position() - digitPos + 1;

            }
        }
    }

    public long summingResults() {
        return results.parallelStream().mapToLong(Long::longValue).sum();
    }

    public enum Operator {
        PLUS('+', Long::sum, 0), MULT('*', (a, b) -> a * b, 1);
        private static final Map<Character, Operator> ofChar;

        static {
            ofChar = Arrays.stream(Operator.values()).collect(Collectors.toMap(op -> op.c, Function.identity()));
        }

        private final char c;
        private final LongBinaryOperator op;
        private final long noOp;

        Operator(char character, LongBinaryOperator op, long noOp) {
            c = character;
            this.op = op;
            this.noOp = noOp;
        }

        static Operator valueOfChar(char c) {
            return ofChar.get(c);
        }

        public long oper(long a, long b) {
            return op.applyAsLong(a, b);
        }
    }

    public record OperatorPos(Operator operator, int position) {
    }
}