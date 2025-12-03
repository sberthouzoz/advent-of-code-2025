package adventofcode.y2025;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

public class One {
    private int starting;
    private int current;
    private int nbAtZero;
    private int nbAtZeroPartTwo;

    public One(int starting) {
        this.starting = starting;
        current = this.starting;
    }

    public static void main(String[] args) throws IOException {
        var dial = new One(50);
        readInstructions(Files.readAllLines(Path.of(args[0])), dial);
        System.out.println("dial = " + dial);
    }

    public static void readInstructions(Collection<String> instructions, One dial) {
        instructions.stream()
                .map(Rotation::new)
                .forEach(dial::rotate);
    }

    public void rotate(Rotation r) {
        System.out.println("before rotate = " + this);
        switch (r.direction()) {
            case RIGHT -> updateCurrent(r.nClick());
            case LEFT -> updateCurrent(-r.nClick);
        }
        System.out.printf("after rotate = %s%n%n", this);
    }

    private void updateCurrent(int i) {
        starting = current;
        current += i;
        if (current >= 100 || current <= 0) {
            var temp = current;
            if (current <= 0) {
                temp = -current;
                if (starting != 0) nbAtZeroPartTwo++;
            }
            nbAtZeroPartTwo += temp / 100;
        }
        current %= 100;
        if (current < 0) current = 100 + current;
        if (current == 0) {
            nbAtZero++;
        }
    }

    public int getCurrent() {
        return current;
    }

    public int getNbAtZero() {
        return nbAtZero;
    }

    public int getNbAtZeroPartTwo() {
        return nbAtZeroPartTwo;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    enum Direction {
        LEFT("L"), RIGHT("R");
        private final String letter;

        Direction(String letter) {
            this.letter = letter;
        }

        public static Direction fromLetter(char c) {
            return switch (c) {
                case 'L' -> LEFT;
                case 'R' -> RIGHT;
                default ->
                        throw new IllegalArgumentException("Invalid value received: %s, only %s are allowed".formatted(c, Arrays.stream(Direction.values()).map(Direction::getLetter).toList()));
            };
        }

        public String getLetter() {
            return letter;
        }
    }

    public record Rotation(Direction direction, int nClick) {
        Rotation(String parse) {
            this(
                    Direction.fromLetter(parse.charAt(0)),
                    Integer.parseInt(parse, 1, parse.length(), 10));
        }
    }
}