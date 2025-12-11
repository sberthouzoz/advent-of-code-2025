package adventofcode.y2025;

import java.awt.*;

public class Nine {
    static Rectangle fromOppositeCorners(Point point1, Point point2) {
        var rect = new Rectangle(new Point(Integer.min(point1.x, point2.x), Integer.min(point1.y, point2.y)));
        rect.add(new Point(Integer.max(point1.x, point2.x) + 1, Integer.max(point1.y, point2.y) + 1));
        return rect;
    }

    static long getArea(Rectangle rect) {
        return (long) rect.width * rect.height;
    }
}