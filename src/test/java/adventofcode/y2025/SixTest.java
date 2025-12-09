package adventofcode.y2025;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SixTest {
    private static final String EXAMPLE = """
            123 328  51 64\s
             45 64  387 23\s
              6 98  215 314
            *   +   *   + \s""";
    private static final String OPERATOR_CHARS = "+*";
    private Six testee;

    @BeforeEach
    void setUp() {
        testee = Six.parse(EXAMPLE);
    }

    @Test
    void temp() {

        assertThat(ArrayUtils.indexOf(OPERATOR_CHARS.toCharArray(), "+  *  +".charAt(3))).isNotEqualTo(ArrayUtils.INDEX_NOT_FOUND);
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
        System.out.println("res = " + res);
        assertThat(res).isPositive();
    }

    @Test
    void getDigitAt() {
        assertThat(Six.getDigitAt(652, 0)).isEqualTo(2);
        assertThat(Six.getDigitAt(652, 1)).isEqualTo(5);
        assertThat(Six.getDigitAt(652, 2)).isEqualTo(6);
        assertThat(Six.getDigitAt(652, 3)).isEqualTo(0);
    }

    @Test
    void setDigitAt() {
        assertThat(Six.setDigitAt(1, 2)).isEqualTo(100);
        assertThat(Six.setDigitAt(2, 1)).isEqualTo(20);
        assertThat(Six.setDigitAt(3, 0)).isEqualTo(3);
    }
}