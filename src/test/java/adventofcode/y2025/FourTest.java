package adventofcode.y2025;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FourTest {
    private final String EXAMPLE = """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.""";

    @Test
    void parse() {
        var result = Four.parse(EXAMPLE);
        Assertions.assertThat(result.getInput()).hasDimensions(10, 10);
    }
}