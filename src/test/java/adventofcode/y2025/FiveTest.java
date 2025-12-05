package adventofcode.y2025;

import org.apache.commons.lang3.LongRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class FiveTest {
    private Five testee;

    @BeforeEach
    void setUp() {
        var exampleInput = """
                3-5
                10-14
                16-20
                12-18
                
                1
                5
                8
                11
                17
                32""";
        testee = Five.parse(exampleInput.lines());
    }

    @Test
    void parse() {
        assertThat(testee).isNotNull();
        assertThat(testee.getAvailableIngredients()).hasSize(6).containsExactlyInAnyOrder(1, 5, 8, 11, 17, 32);
        assertThat(testee.getFreshIngredients()).hasSize(4).contains(LongRange.of(3, 5), LongRange.of(10, 14), LongRange.of(16, 20), LongRange.of(12, 18));
    }

    public static Stream<Arguments> isFresh() {
        return Stream.of(
                of(1, false),
                of(5, true),
                of(8, false),
                of(11, true),
                of(17, true),
                of(32, false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void isFresh(int ingredientId, boolean expected) {
        assertThat(testee.isFresh(ingredientId)).isEqualTo(expected);
    }

    @Test
    void example_part1() {
        assertThat(testee.nbFresh()).isEqualTo(3);
    }
}