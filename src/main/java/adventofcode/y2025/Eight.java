package adventofcode.y2025;

import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

public class Eight {
    private final List<Point3D> input;
    private final List<Point3DPairWithDistance> pairsWithDistance = new ArrayList<>();
    private final CircuitSet circuits;

    public static void main(String[] args) throws IOException {
        var filePath = Path.of(args[0]);
        try (var stream = Files.lines(filePath)) {
            var start = Instant.now();
            var partOneResult = partOne(stream, 1000);
            var end = Instant.now();
            System.out.println("Duration = " + Duration.between(start, end));
            System.out.println("partOneResult = " + partOneResult);
        }
    }

    public Eight(Stream<String> input) {
        this.input = input.parallel().map(line -> line.split(","))
                .map(arr -> new Point3D(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2])))
                .toList();
        circuits = new CircuitSet(this.input);
        for (int i = 0; i < this.input.size(); i++) {
            var point = this.input.get(i);

            for (int j = i + 1; j < this.input.size(); j++) {
                var point2 = this.input.get(j);
                pairsWithDistance.add(new Point3DPairWithDistance(point, point2, point.distanceTo(point2)));
            }
        }
        Collections.sort(pairsWithDistance);
    }

    public static long partOne(Stream<String> input, int iterations) {
        var junctions = new Eight(input);

        for (int i = 0; i < iterations; i++) {
            var pair = junctions.pairsWithDistance.get(i);
            junctions.circuits.add(pair);
        }

        var largest = junctions.circuits.getLargest(3);
        return largest.stream().mapToInt(Set::size).reduce(1, (a, b) -> a * b);
    }


}

record Point3D(int x, int y, int z) implements Comparable<Point3D> {
    public static final Point3D ORIGIN = new Point3D(0, 0, 0);
    private static final Comparator<Point3D> COMPARATOR = Comparator.comparingDouble(p3d -> p3d.distanceTo(ORIGIN));

    Point3D(Point3D fromPoint) {
        this(fromPoint.x(), fromPoint.y(), fromPoint.z());
    }
    @Override
    public int compareTo(@NonNull Point3D o) {
        return COMPARATOR.compare(this, o);
    }

    public double distanceTo(Point3D other) {
        if (this.equals(other)) {
            return 0;
        }
        return Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2) + Math.pow(other.z - this.z, 2));
    }


}

record Point3DPairWithDistance(Point3D first, Point3D second,
                               double distance) implements Comparable<Point3DPairWithDistance> {
    @Override
    public int compareTo(@NonNull Point3DPairWithDistance o) {
        return Double.compare(this.distance, o.distance);
    }
}

class CircuitSet {
    private final List<Set<Point3D>> circuits = new ArrayList<>();

    CircuitSet(List<Point3D> points) {
        for (var point : points) {
            var circuit = new HashSet<Point3D>();
            circuit.add(point);
            circuits.add(circuit);
        }
    }

    void add(Point3DPairWithDistance pair) {
        int firstIndex = -1;
        int secondIndex = -1;

        for (int i = 0; i < circuits.size(); i++) {
            Set<Point3D> circuit = circuits.get(i);
            if (circuit.contains(pair.first())) {
                firstIndex = i;
            }
            if (circuit.contains(pair.second())) {
                secondIndex = i;
            }
        }

        // Connect circuits if both are in different ones
        if (firstIndex != secondIndex) {
            circuits.get(firstIndex).addAll(circuits.get(secondIndex));
            circuits.remove(secondIndex);
        }
    }

    public List<Set<Point3D>> getLargest(int limit) {
        return circuits.stream().sorted(comparing(Set::size, reverseOrder())).limit(limit).toList();
    }
}