package adventofcode.y2025;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ThreeTest {
    private static final String input = """
            987654321111111
            811111111111119
            234234234234278
            818181911112111""";

    @Test
    void parseBank() {
        var bank = input.lines().findFirst().orElseThrow();

        var result = Three.parseBank(bank);

        assertThat(result).isEqualTo(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1});
    }

    public static Stream<Arguments> getIndexOfMax() {
        return Stream.of(
                Arguments.of(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1}, new Three.IndexOfMax(0, 1)),
                Arguments.of(new int[]{8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9}, new Three.IndexOfMax(0, 14))
        );
    }

    @ParameterizedTest
    @MethodSource
    void getIndexOfMax(int[] bank, Three.IndexOfMax expectedIdx) {

        var result = Three.getIndexOfMax(bank);

        assertThat(result).isEqualTo(expectedIdx);
    }

}