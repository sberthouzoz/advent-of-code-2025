package adventofcode.y2025;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SixTest {
    private static final String EXAMPLE = """
            123 328  51 64\s
             45 64  387 23\s
              6 98  215 314
            *   +   *   + \s""";
    private Six testee;

    @BeforeEach
    void setUp() {
        testee = Six.parse(EXAMPLE);
    }

    @Test
    void partOne() {
        testee.partOne(EXAMPLE);

        var res = testee.summingResults();

        assertThat(res).isEqualTo(4277556);
    }

    @Test
    void partTwo() {
        testee.partTwo(EXAMPLE);
        var res = testee.summingResults();
        assertThat(res).isPositive().isEqualTo(3263827);
    }
}