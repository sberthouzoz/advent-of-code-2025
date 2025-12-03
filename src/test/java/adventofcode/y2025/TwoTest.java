package adventofcode.y2025;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Supplier;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

class TwoTest {
    private static final String sampleInput = """
            11-22,95-115,998-1012,1188511880-1188511890,222220-222224,\
            1698522-1698528,446443-446449,38593856-38593862,565653-565659,\
            824824821-824824827,2121212118-2121212124\
            """;

    @Test
    void getRanges() {
        // given
        // when
        var result = Two.getRanges(sampleInput);
        // then
        assertThat(result).hasSize(11);
    }

    @Test
    void createRangeStream() {
        // given
        var input = "11-22";
        // when
        Supplier<LongStream> result = () -> Two.createRangeStream(input);
        // then
        assertThat(result.get()).hasSize(12);
        assertThat(result.get()).first().isEqualTo(11L);
        assertThat(result.get()).last().isEqualTo(22L);
    }

    @ParameterizedTest
    @ValueSource(longs = {1010, 1188511885, 446446, 38593859})
    void invalidFilter(long invalid) {
        // given
        var valid = 1188511785L;
        // when
        var testee = Two.invalidFilter();
        // then
        assertThat(testee).accepts(invalid);
        assertThat(testee).rejects(valid, 1);
    }

    @ParameterizedTest
    @ValueSource(longs = {11, 22, 99, 111, 999, 1010, 1188511885, 222222, 446446, 38593859, 565656, 824824824, 2121212121})
    void invalidFilterPartTwo(long invalid) {
        // given
        var valid = 565657L;
        // when
        var testee = Two.invalidFilterPartTwo();
        // then
        assertThat(testee).accepts(invalid);
        assertThat(testee).rejects(valid, 824824822, 1);
    }

    @Test
    void invalidSum() {
        // given
        // when
        var result = Two.invalidSum(sampleInput);
        // then
        assertThat(result).isEqualTo(4174379265L);
    }
}