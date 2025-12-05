package adventofcode.y2025;

import org.apache.commons.lang3.IntegerRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(testee.getFreshIngredients()).hasSize(4).contains(IntegerRange.of(3, 5), IntegerRange.of(10, 14), IntegerRange.of(16, 20), IntegerRange.of(12, 18));
    }
}