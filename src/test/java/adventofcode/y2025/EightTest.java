package adventofcode.y2025;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class EightTest {
    private final String example = """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689""";

    @Test
    void partOne() {
        var result = Eight.partOne(example.lines(), 10);

        assertThat(result).isEqualTo(40);
    }

    @Test
    void partTwo() {
        var result = Eight.partTwo(example.lines());

        assertThat(result).isEqualTo(216 * 117);
    }

    @Nested
    class Point3DTest {
        private final Point3D point1 = new Point3D(984, 92, 344);
        private final Point3D point2 = new Point3D(425, 690, 689);

        @Test
        void compareTo() {
            assertThat(point1).isGreaterThan(Point3D.ORIGIN);
            assertThat(point2).isGreaterThan(Point3D.ORIGIN);
            assertThat(Point3D.ORIGIN).isLessThan(point1);
            assertThat(Point3D.ORIGIN).isLessThan(point2);
            assertThat(point1).isEqualByComparingTo(new Point3D(point1));
            assertThat(point1).isLessThan(point2);
        }

        @Test
        void distance() {
            assertThat(Point3D.ORIGIN.distanceTo(Point3D.ORIGIN)).isZero();
            var distance = point1.distanceTo(point2);
            // expected result from https://www.calculatorsoup.com/calculators/geometry-solids/distance-two-points.php
            assertThat(distance).isPositive().isCloseTo(888.318637, within(1E-6));
        }
    }
}