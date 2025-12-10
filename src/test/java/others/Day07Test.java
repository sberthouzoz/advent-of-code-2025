package others;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class Day07Test {
    private Map<Day07.Coord, Long> pathMap;

    @BeforeEach
    void setUp() {
        var example = """
                .......S.......
                ...............
                .......^.......
                ...............
                ......^.^......
                ...............
                .....^.^.^.....
                ...............
                ....^.^...^....
                ...............
                ...^.^...^.^...
                ...............
                ..^...^.....^..
                ...............
                .^.^.^.^.^...^.
                ...............""";
        var input = Day07.readInput(example);
        pathMap = Day07.creatPathMap(input);
    }

    @Test
    void part1() {
        var result = Day07.part1(pathMap);
        assertThat(result).isEqualTo(21);
    }

    @Test
    void part2() {
        var result = Day07.part2(pathMap);
        assertThat(result).isEqualTo(40);
    }
}