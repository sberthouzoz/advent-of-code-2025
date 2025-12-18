package adventofcode.y2025;

import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.function.LongPredicate;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Two {
    public static void main(String[] args) {
        var input = """
                492410748-492568208,246-390,49-90,16-33,142410-276301,54304-107961,12792-24543,3434259704-3434457648,\
                848156-886303,152-223,1303-1870,8400386-8519049,89742532-89811632,535853-567216,6608885-6724046,\
                1985013826-1985207678,585591-731454,1-13,12067202-12233567,6533-10235,6259999-6321337,908315-972306,\
                831-1296,406-824,769293-785465,3862-5652,26439-45395,95-136,747698990-747770821,984992-1022864,34-47,\
                360832-469125,277865-333851,2281-3344,2841977-2953689,29330524-29523460""";
        System.out.println("invalidSum = " + invalidSum(input));
    }

    static long invalidSum(String input) {
        var start = Instant.now();
        var sum = getRanges(input).parallel()
                .flatMapToLong(Two::createRangeStream)
                .filter(invalidFilterPartTwo())
                //.peek(System.out::println)
                .sum();
        var end = Instant.now();
        System.out.println("took: " + Duration.between(start, end));
        return sum;
    }

    static Stream<String> getRanges(String input) {
        return Arrays.stream(input.split(","));
    }

    static LongStream createRangeStream(String range) {
        var radix_ten = 10;
        var indexOfMinus = range.indexOf('-');
        var begin = Long.parseLong(range, 0, indexOfMinus, radix_ten);
        var end = Long.parseLong(range, indexOfMinus + 1, range.length(), radix_ten);
        return LongStream.rangeClosed(begin, end);
    }

    static LongPredicate invalidFilter() {
        return idToTest -> {
            var asString = String.valueOf(idToTest);
            if (asString.length() % 2 != 0) return false;
            return asString.endsWith(asString.substring(0, asString.length() / 2));
        };
    }

    static LongPredicate invalidFilterPartTwo() {
        return idPartTwo -> {
            if (idPartTwo <= 10) return false;
            var asString = String.valueOf(idPartTwo);
            var len = asString.length();
            if (StringUtils.containsOnly(asString, asString.charAt(0))) return true;
            return IntStream.rangeClosed(2, asString.length() / 2).anyMatch(idx -> asString.equals(asString.substring(0, idx).repeat(len / idx)));
        };
    }

}