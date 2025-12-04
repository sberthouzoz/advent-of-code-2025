package adventofcode.y2025;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FourTest {
    private Four testee;

    @BeforeEach
    void setup() {
        var example = """
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
        testee = Four.parse(example.lines());
    }

    @Test
    void parse() {
        final var size = 10;
        assertThat(testee.getInput()).as("should have %d rows", size).hasSize(size);
        assertThat(testee.getInput().getLast().isEmpty()).isFalse();
        assertThat(testee.getInput().getLast().cardinality()).as("should have %d rolls on the last line", 6).isEqualTo(6);
    }

    @Test
    void example() {
        testee.setLineSize(10);

        var result = testee.nbAccessibleRoll();

        assertThat(result).isEqualTo(13);
    }

    // part 2
    @Test
    void removing() {
        var removable = testee.nbAccessibleRoll();
        assertThat(removable).isEqualTo(13);

        testee.removeAccessible();
        removable = testee.nbAccessibleRoll();
        assertThat(removable).isEqualTo(12);
    }

    @Test
    void example_part2() {
        var result = testee.nbRemovable();

        assertThat(result).isEqualTo(43);
    }
}