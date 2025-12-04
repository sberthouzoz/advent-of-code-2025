package adventofcode.y2025;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        final var size = 10;
        var result = Four.parse(EXAMPLE.lines());
        assertThat(result.getInput()).as("should have %d rows", size).hasSize(10);
        assertThat(result.getInput().getLast().isEmpty()).isFalse();
        assertThat(result.getInput().getLast().cardinality()).as("should have %d rolls on the last line", 6).isEqualTo(6);
    }

    @Test
    void example() {
        var input = Four.parse(EXAMPLE.lines());
        input.setLineSize(10);

        var result = input.nbAccessibleRoll(4);

        assertThat(result).isEqualTo(13);
    }
}