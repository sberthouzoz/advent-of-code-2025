package adventofcode.y2025;

import java.util.Arrays;

public class Twelve {

    record Shape(boolean[][] shape) {
        Shape rotateLeft() {
            var clone = new Shape(shape.clone());
            return clone;
        }

        Shape rotateRight() {
            var clone = new Shape(shape.clone());
            return clone;
        }
    }

    record Region(int width, int height, int[] requiredShapes, boolean[][] area) {
        Region(int width, int height, int[] requiredShapes) {
            this(width, height, requiredShapes, new boolean[width][height]);
        }

        public static Region parse(String input) {
            final var xIndex = input.indexOf('x');
            final var radix_ten = 10;
            final var colonIndex = input.indexOf(':');
            var width = Integer.parseInt(input, 0, xIndex, radix_ten);
            var height = Integer.parseInt(input, xIndex + 1, colonIndex, radix_ten);
            var requiredShapes = Arrays.stream(input.substring(colonIndex + 2).split(" ")).mapToInt(Integer::parseInt).toArray();
            return new Region(width, height, requiredShapes);
        }
    }
}