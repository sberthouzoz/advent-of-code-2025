package adventofcode.y2025;

import org.jspecify.annotations.NonNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Nine {
    static Rectangle fromOppositeCorners(Point point1, Point point2) {
        var rect = new Rectangle(new Point(Integer.min(point1.x, point2.x), Integer.min(point1.y, point2.y)));
        rect.add(new Point(Integer.max(point1.x, point2.x) + 1, Integer.max(point1.y, point2.y) + 1));
        return rect;
    }

    static long getArea(Rectangle rect) {
        return (long) rect.width * rect.height;
    }

    static long partOne(Stream<String> lines) {
        var sep = ',';
        var radix_ten = 10;
        List<PairWithDistance> pairs = new ArrayList<>();
        var points = lines.map(line -> new Point(Integer.parseInt(line, 0, line.indexOf(sep), radix_ten),
                        Integer.parseInt(line, line.indexOf(sep) + 1, line.length(), radix_ten)))
                .toList();
        for (int i = 0; i < points.size(); i++) {
            var point = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                var other = points.get(j);
                pairs.add(new PairWithDistance(point, other, point.distance(other)));
            }
        }
        Collections.sort(pairs);
        var largestRect = fromOppositeCorners(pairs.getLast().first(), pairs.getLast().second());
        return getArea(largestRect);
    }
}

record PairWithDistance(Point first, Point second, double distance) implements Comparable<PairWithDistance> {
    private static final Comparator<PairWithDistance> DISTANCE_COMPARATOR = Comparator.comparingDouble(PairWithDistance::distance);

    @Override
    public int compareTo(@NonNull PairWithDistance o) {
        return DISTANCE_COMPARATOR.compare(this, o);
    }
}