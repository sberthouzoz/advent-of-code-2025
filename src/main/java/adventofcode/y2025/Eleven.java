package adventofcode.y2025;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Eleven {
    public static void main(String[] args) throws IOException {
        var path = Path.of(args[0]);
        var start = new Device("you");
        var end = new Device("out");

        var devices = parse(Files.readString(path));
        var startInstant = Instant.now();
        var part1 = countPath(start, end, devices, new HashMap<>());
        var endInstant = Instant.now();
        System.out.println("Duration: " + Duration.between(startInstant, endInstant));
        System.out.println("part1 = " + part1);
    }

    static int part2(List<Device> devices) {
        Device fft = new Device("fft");
        Device dac = new Device("dac");
        var fftToDac = countPath(fft, dac, devices, new HashMap<>());
        Device svr = new Device("svr");
        Device out = new Device("out");
        if (fftToDac > 0) {
            var svrToFft = countPath(svr, fft, devices, new HashMap<>());
            var dacToOut = countPath(dac, out, devices, new HashMap<>());
            return svrToFft * fftToDac * dacToOut;
        } else {
            var svrToDac = countPath(svr, dac, devices, new HashMap<>());
            var dacToFft = countPath(dac, fft, devices, new HashMap<>());
            var fftToOut = countPath(fft, out, devices, new HashMap<>());
            return svrToDac * dacToFft * fftToOut;
        }
    }

    static int countPath(Device start, Device end, List<Device> devices, Map<Device, Integer> counts) {
        if (counts.containsKey(start)) {
            return counts.get(start);
        }
        if (start.equals(end)) {
            return 1;
        }
        var sum = devices.get(devices.indexOf(start)).outputsTo()
                .stream()
                .mapToInt(dev -> countPath(dev, end, devices, counts)).sum();
        counts.put(start, sum);
        return sum;
    }

    static List<Device> parse(String input) {
        var initDevices = parseInitial(input.lines());
        initDevices.add(new Device("out"));
        updateOutputs(initDevices, input.lines());
        return initDevices;
    }

    static List<Device> parseInitial(Stream<String> lines) {
        return lines.map(Device::parse).collect(Collectors.toCollection(ArrayList::new));
    }

    static void updateOutputs(List<Device> devices, Stream<String> lines) {
        lines.forEach(line -> {
            var device = devices.get(devices.indexOf(Device.parse(line)));
            device.outputsTo().addAll(
                    Arrays.stream(line.substring(line.indexOf(':') + 2).split(" "))
                            .map(name -> devices.get(devices.indexOf(new Device(name))))
                            .toList());
        });
    }
}

record Device(String name, List<Device> outputsTo) {
    Device(String name) {
        this(name, new ArrayList<>());
    }

    static Device parse(String line) {
        return new Device(line.substring(0, line.indexOf(':')));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Device device)) return false;
        return Objects.equals(name, device.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}