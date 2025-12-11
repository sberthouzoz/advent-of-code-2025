package adventofcode.y2025;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;

class NineTest {
    Point point = new Point(2, 5);
    Point point2 = new Point(11, 1);
    String example = """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3""";

    @Test
    void partOne() {
        var result = Nine.partOne(example.lines());
        assertThat(result).isEqualTo(50);
    }

    @Test
    void partTwo() {
        var result = Nine.partTwo(example.lines());
        assertThat(result).isEqualTo(24);
    }

    @Test
    void playground() {
        var dist = point.distance(point2);

        var p3 = new Point(7, 1);
        var p4 = new Point(11, 7);
        var dist2 = p3.distance(p4);

        assertThat(dist).isGreaterThan(dist2);

    }

    @Test
    void fromOppositeCorners() {
        var rect1To2 = Nine.fromOppositeCorners(point, point2);
        assertThat(rect1To2.getWidth()).isEqualTo(10);
        assertThat(rect1To2.getHeight()).isEqualTo(5);
    }

    @Test
    void getArea() {
        var rect1To2 = Nine.fromOppositeCorners(point, point2);
        assertThat(Nine.getArea(rect1To2)).isEqualTo(50);
    }

}