package adventofcode.y2025;

import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.assertj.core.api.Assertions.assertThat;

class TenTest {
    static final String EXAMPLE = """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}""";

    @Test
    void part1() {
        var res = Ten.solvePart1(EXAMPLE.lines());

        assertThat(res).isEqualTo(7);
    }

    @Test
    void part1_singleMachine() {
        String input = EXAMPLE.lines().findFirst().orElseThrow();

        var res = Machine.parse(input);

        assertThat(res.lightsGoal()).isEqualTo(Lights.parse("[.##.]"));
        assertThat(res.buttonWirings()).hasSize(6);
        assertThat(res.buttonWirings().getFirst().bs()).isEqualTo(ButtonWiring.parse("(3)").bs());
        assertThat(res.buttonWirings().getFirst().indexes()).containsExactly(3);
        assertThat(res.buttonWirings().getLast().bs()).isEqualTo(ButtonWiring.parse("(0,1)").bs());
        assertThat(res.joltage()).containsExactly(3, 5, 4, 7);

        var part1 = res.solvePart1();
        assertThat(part1).isEqualTo(2);
    }

    @Test
    void parseLights() {
        String input = "[.##.]";

        Lights parsed = Lights.parse(input);
        var expected = new BitSet();
        expected.set(1, 3);

        assertThat(parsed).isEqualTo(new Lights(expected));
    }

    @Test
    void parseButtonWirings() {
        String input = "(2,3)";

        var parsed = ButtonWiring.parse(input);
        var expected = new BitSet();
        expected.set(2, 4);

        assertThat(parsed.indexes()).contains(2, 3);
        assertThat(parsed.bs()).isEqualTo(expected);
    }
}