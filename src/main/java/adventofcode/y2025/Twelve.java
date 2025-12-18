package adventofcode.y2025;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Twelve {
    final List<Shape> shapes = new ArrayList<>();
    final List<Region> regions = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        var path = Path.of(args[0]);
        var input = Files.readString(path);

        var obj = parse(input);
        var start = Instant.now();
        var part1 = obj.solvePart1();
        var end = Instant.now();
        System.out.println("Duration = " + Duration.between(start, end));
        System.out.println("part1 = " + part1);
    }

    public static Twelve parse(String input) {
        var parsed = new Twelve();
        var lines = input.lines().filter(StringUtils::isNotBlank).toList();
        var sb = new StringBuilder();
        for (var line : lines) {
            if (line.contains("x")) {
                if (!sb.isEmpty()) {
                    parsed.shapes.add(Shape.parse(sb.toString()));
                    sb.setLength(0);
                }
                parsed.regions.add(Region.parse(line));
                continue;
            }
            if (line.matches("^\\d+:")) {
                if (!sb.isEmpty()) parsed.shapes.add(Shape.parse(sb.toString()));
                sb.setLength(0);
            } else {
                sb.append("\n").append(line);
            }
        }
        return parsed;
    }

    long solvePart1() {
        return regions.stream()
                .filter(hasEnoughSpace())
                .filter(fitsEnoughPresentRegardlessOfShape())
                .count();
    }

    private Predicate<? super Region> fitsEnoughPresentRegardlessOfShape() {
        return r -> {
            var numberOfPresentsThatWouldFitRegardlessOfTheirShape = (r.width / 3) * (r.height / 3);
            var nbPresentsToFit = Arrays.stream(r.requiredShapes).sum();
            return nbPresentsToFit <= numberOfPresentsThatWouldFitRegardlessOfTheirShape;
        };
    }

    private Predicate<? super Region> hasEnoughSpace() {
        return r -> {
            var size = r.height * r.width;
            var totalPixel = IntStream.range(0, r.requiredShapes.length).map(i -> shapes.get(i).getSizeInPx() * r.requiredShapes[i]).sum();
            return size >= totalPixel;
        };
    }

    record Shape(boolean[][] shape) {
        private static final char FILLED = '#';

        public static Shape parse(String input) {
            var lines = input.lines().toList();
            var arr = new boolean[lines.getFirst().length()][lines.size()];
            for (int i = 0; i < arr.length; i++) {
                var line = lines.get(i);
                for (int j = 0; j < line.length(); j++) {
                    arr[i][j] = line.charAt(j) == FILLED;
                }
            }
            return new Shape(arr);
        }

        public int getSizeInPx() {
            return (int) Arrays.stream(shape).flatMap(arr -> IntStream.range(0, arr.length).mapToObj(i -> arr[i]))
                    .filter(Boolean.TRUE::equals).count();
        }
        Shape rotateLeft() {
            return rotateRight().rotateRight().rotateRight();
        }

        Shape rotateRight() {
            var clone = new Shape(deepCopy(shape));
            int dim = clone.shape.length;

            for (int i = 0; i <= (dim - 1) / 2; i++) {
                for (int j = i; j < dim - i - 1; j++) {
                    var p1 = clone.shape[i][j];
                    var p2 = clone.shape[j][dim - i - 1];
                    var p3 = clone.shape[dim - i - 1][dim - j - 1];
                    var p4 = clone.shape[dim - j - 1][i];

                    clone.shape[j][dim - i - 1] = p1;
                    clone.shape[dim - i - 1][dim - j - 1] = p2;
                    clone.shape[dim - j - 1][i] = p3;
                    clone.shape[i][j] = p4;
                }
            }
            return clone;
        }

        private boolean[][] deepCopy(boolean[][] arr) {
            boolean[][] copy = new boolean[arr.length][];
            for (int i = 0; i < arr.length; i++) {
                copy[i] = arr[i].clone();
            }
            return copy;
        }

        @Override
        public @NonNull String toString() {
            var sb = new StringBuilder(shape.length * shape.length + shape.length);
            for (boolean[] bools : shape) {
                for (int j = 0; j < shape.length; j++) {
                    sb.append(bools[j] ? FILLED : '.');
                }
                sb.append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
            return new StringJoiner(", ", Shape.class.getSimpleName() + "[", "]")
                    .add("shape=" + Arrays.deepToString(shape))
                    .add("displayString=" + sb)
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Shape(boolean[][] shape2))) return false;
            return Objects.deepEquals(shape, shape2);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(shape);
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

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Region region)) return false;
            return width == region.width && height == region.height && Objects.deepEquals(requiredShapes, region.requiredShapes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(width, height, Arrays.hashCode(requiredShapes));
        }
    }
}