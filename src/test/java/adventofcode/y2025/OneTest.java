package adventofcode.y2025;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OneTest {

    @Test
    void rotateLeft() {
        var dial = new One(5);

        dial.rotate(new One.Rotation(One.Direction.LEFT, 5));

        assertThat(dial.getCurrent()).isEqualTo(0);
        assertThat(dial.getNbAtZeroPartTwo()).isEqualTo(1);
    }

    @Test
    void rotateRight() {
        var dial = new One(95);

        dial.rotate(new One.Rotation(One.Direction.RIGHT,5));

        assertThat(dial.getCurrent()).isEqualTo(0);
        assertThat(dial.getNbAtZeroPartTwo()).isEqualTo(1);
    }

    @Test
    void rotateLeft_moreThanOneRound() {
        var dial = new One(5);

        dial.rotate(new One.Rotation(One.Direction.LEFT,206));

        assertThat(dial.getCurrent()).isEqualTo(99);
        assertThat(dial.getNbAtZeroPartTwo()).isEqualTo(3);
    }

    @Test
    void rotateRight_moreThanOneRound() {
        var dial = new One(95);

        dial.rotate(new One.Rotation(One.Direction.RIGHT,506));

        assertThat(dial.getCurrent()).isEqualTo(1);
        assertThat(dial.getNbAtZeroPartTwo()).isEqualTo(6);
    }

    @Test
    void rotateRight_noUpdateWhenStartAtZero() {
        var dial = new One(0);

        dial.rotate(new One.Rotation(One.Direction.RIGHT, 2));

        assertThat(dial.getCurrent()).isEqualTo(2);
        assertThat(dial.getNbAtZeroPartTwo()).isZero();
    }

    @Test
    void rotateLeft_noUpdateWhenStartAtZero() {
        var dial = new One(0);

        dial.rotate(new One.Rotation(One.Direction.LEFT, 2));

        assertThat(dial.getCurrent()).isEqualTo(98);
        assertThat(dial.getNbAtZeroPartTwo()).isZero();
    }

    @Test
    void readInstructions() {
        var dial = new One(50);
        var instructions = """
                L68
                L30
                R48
                L5
                R60
                L55
                L1
                L99
                R14
                L82""";
        One.readInstructions(instructions.lines().toList(), dial);
        assertThat(dial.getCurrent()).isEqualTo(32);
        assertThat(dial.getNbAtZero()).isEqualTo(3);
        assertThat(dial.getNbAtZeroPartTwo()).isEqualTo(6);
    }
}