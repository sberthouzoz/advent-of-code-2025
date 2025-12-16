package adventofcode.y2025;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ElevenTest {
    static final String EXAMPLE_PART_ONE = """
            aaa: you hhh
            you: bbb ccc
            bbb: ddd eee
            ccc: ddd eee fff
            ddd: ggg
            eee: out
            fff: out
            ggg: out
            hhh: ccc fff iii
            iii: out""";
    static final String EXAMPLE_PART_TWO = """
            svr: aaa bbb
            aaa: fft
            fft: ccc
            bbb: tty
            tty: ccc
            ccc: ddd eee
            ddd: hub
            hub: fff
            eee: dac
            dac: fff
            fff: ggg hhh
            ggg: out
            hhh: out""";

    @Test
    void part1() {
        var res = Eleven.parse(EXAMPLE_PART_ONE);

        var part1 = Eleven.countPath(new Device("you"), new Device("out"), res, new HashMap<>());

        assertThat(part1).isEqualTo(5);
    }

    @Test
    void part2() {
        var res = Eleven.parse(EXAMPLE_PART_TWO);

        var part2 = Eleven.part2(res);

        assertThat(part2).isEqualTo(2);
    }

    @Test
    void parse() {
        var res = Eleven.parse(EXAMPLE_PART_ONE);

        assertThat(res).isNotNull().hasSize(11);
        assertThat(res.getFirst()).isEqualTo(new Device("aaa"));
        assertThat(res.getFirst().name()).isEqualTo("aaa");
        assertThat(res.getFirst().outputsTo()).hasSize(2);
        assertThat(res).containsAll(res.getFirst().outputsTo());
    }

    @Test
    void parseInitial() {
        var res = Eleven.parseInitial(EXAMPLE_PART_ONE.lines());

        assertThat(res).isNotNull().hasSize(10);
    }

    @Test
    void updateOutputs() {
    }

    @Nested
    class DeviceTests {
        @Test
        void parse() {
            var input = EXAMPLE_PART_ONE.lines().findFirst().orElseThrow();
            var res = Device.parse(input);
            assertThat(res.name()).isEqualTo("aaa");
        }

        @Test
        void equalsAndHashCode_isBasedOnNameOnly() {
            var device1 = new Device("aaa");
            var device2 = new Device("bbb");
            var device3 = new Device("ccc");
            var device4 = new Device("aaa", List.of(device2, device3));

            assertThat(device1).isEqualTo(device4);
            assertThat(device1.hashCode()).isEqualTo(device4.hashCode());
        }

    }
}