package adventofcode.y2025;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Eleven {
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