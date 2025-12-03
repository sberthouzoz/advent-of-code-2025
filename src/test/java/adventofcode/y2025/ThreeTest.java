package adventofcode.y2025;

import org.junit.jupiter.api.Test;

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

    @Test
    void getIndexOfMax() {
        var bank = new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1};

        var result = Three.getIndexOfMax(bank);

        assertThat(result).isEqualTo(new Three.IndexOfMax(0, 1));
    }

}