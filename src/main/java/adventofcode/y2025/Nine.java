package adventofcode.y2025;

import org.jspecify.annotations.NonNull;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SequencedSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Nine {
    public static void main(String[] args) throws IOException {
        var filePath = Path.of(args[0]);
        try (Stream<String> stream = Files.lines(filePath)) {
            var start = Instant.now();
            var partOneResult = partOne(stream);
            var end = Instant.now();
            System.out.println("[Part One] Duration = " + Duration.between(start, end));
            System.out.println("partOneResult = " + partOneResult);
        }
    }
    static Rectangle fromOppositeCorners(Point point1, Point point2) {
        return new Rectangle(
                Integer.min(point1.x, point2.x),
                Integer.min(point1.y, point2.y),
                Math.abs(point2.x - point1.x) + 1,
                Math.abs(point2.y - point1.y) + 1);
    }

    static long getArea(Rectangle rect) {
        return (long) rect.width * rect.height;
    }

    static long partOne(Stream<String> lines) {
        var points = getRedTiles(lines);
        SequencedSet<PairWithRectangleArea> pairs = createPairs(points);
        return pairs.stream().max(PairWithRectangleArea.AREA_COMPARATOR).orElseThrow().area();
    }

    private static @NonNull SequencedSet<PairWithRectangleArea> createPairs(List<Point> points) {
        SequencedSet<PairWithRectangleArea> pairs = new LinkedHashSet<>();
        for (int i = 0; i < points.size(); i++) {
            var point = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                var other = points.get(j);
                pairs.add(new PairWithRectangleArea(point, other));
            }
        }
        return pairs;
    }

    private static @NonNull List<Point> getRedTiles(Stream<String> lines) {
        int radix_ten = 10;
        char sep = ',';
        return lines.map(line -> new Point(Integer.parseInt(line, 0, line.indexOf(sep), radix_ten),
                        Integer.parseInt(line, line.indexOf(sep) + 1, line.length(), radix_ten)))
                .toList();
    }

    static long partTwo(Stream<String> lines) {
        var redTiles = getRedTiles(lines);
        var filterArea = createFilteringArea(redTiles);
        var pairs = createPairs(redTiles);
        Predicate<PairWithRectangleArea> insideUsedArea = (PairWithRectangleArea pair) -> {
            var rect = fromOppositeCorners(pair.first(), pair.second());
            var containsUpLeft = containsOrOnBorder(filterArea, rect.x, rect.y);
            var containsUpRight = containsOrOnBorder(filterArea, rect.getMaxX() - 1, rect.y);
            var containsBottomLeft = containsOrOnBorder(filterArea, rect.x, rect.getMaxY() - 1);
            var containsBottomRight = containsOrOnBorder(filterArea, rect.getMaxX() - 1, rect.getMaxY() - 1);
            return containsUpLeft && containsUpRight && containsBottomLeft && containsBottomRight;
        };
        boolean allMatch = redTiles.stream().allMatch(p -> containsOrOnBorder(filterArea, p.x, p.y));
        System.out.println("allMatch = " + allMatch);
        return pairs.stream().filter(insideUsedArea)
                .max(PairWithRectangleArea.AREA_COMPARATOR).orElseThrow()
                .area();
    }

    private static @NonNull Polygon createFilteringArea(List<Point> redTiles) {
        return new Polygon(redTiles.stream().mapToInt(p -> p.x).toArray(), redTiles.stream().mapToInt(p -> p.y).toArray(), redTiles.size());
    }

    // Check if a point is inside or on the polygon border
    public static boolean containsOrOnBorder(Polygon polygon, double x, double y) {
        // Check first if inside
        if (polygon.contains(x, y)) {
            return true;
        }

        // Check if on any border segment
        for (int i = 0; i < polygon.npoints; i++) {
            int next = (i + 1) % polygon.npoints; // wrap around
            Point p1 = new Point(polygon.xpoints[i], polygon.ypoints[i]);
            Point p2 = new Point(polygon.xpoints[next], polygon.ypoints[next]);
            if (Line2D.ptSegDist(p1.getX(), p1.getY(), p2.getX(), p2.getY(), x, y) == 0) {
                return true;
            }
        }

        return false;
    }

}

record PairWithRectangleArea(Point first, Point second, long area) {
    public static final Comparator<PairWithRectangleArea> AREA_COMPARATOR = Comparator.comparingDouble(PairWithRectangleArea::area);

    PairWithRectangleArea(Point first, Point second) {
        this(first, second, Nine.getArea(Nine.fromOppositeCorners(first, second)));
    }
}