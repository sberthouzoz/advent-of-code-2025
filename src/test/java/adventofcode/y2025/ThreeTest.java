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
                Arguments.of(new int[]{8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9}, new Three.IndexOfMax(0, 14)),
                Arguments.of(new int[]{2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 3, 4, 2, 7, 8}, new Three.IndexOfMax(13, 14)),
                Arguments.of(new int[]{8, 1, 8, 1, 8, 1, 9, 1, 1, 1, 1, 2, 1, 1, 1}, new Three.IndexOfMax(6, 11))
        );
    }

    @ParameterizedTest
    @MethodSource
    void getIndexOfMax(int[] bank, Three.IndexOfMax expectedIdx) {

        var result = Three.getIndexOfMax(bank);

        assertThat(result).isEqualTo(expectedIdx);
    }

    @Test
    void maxOfBank() {

        var maxOfBank = Three.maxOfBank(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1}, new Three.IndexOfMax(0, 1));

        assertThat(maxOfBank).isEqualTo(98);
    }

    @Test
    void example() {
        var result = input.lines().map(Three::parseBank)
                .map(bank -> new BankAndIndexes(bank, Three.getIndexOfMax(bank)))
                .mapToInt(bi -> Three.maxOfBank(bi.bank(), bi.indexes()))
                .sum();
        assertThat(result).isEqualTo(357);
    }

    record BankAndIndexes(int[] bank, Three.IndexOfMax indexes) {
    }

}