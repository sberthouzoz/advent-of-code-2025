package adventofcode.y2025;

import adventofcode.y2025.Twelve.Region;
import adventofcode.y2025.Twelve.Shape;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TwelveTest {
    static final String EXAMPLE = """
            0:
            ###
            ##.
            ##.
            
            1:
            ###
            ##.
            .##
            
            2:
            .##
            ###
            ##.
            
            3:
            ##.
            ###
            ##.
            
            4:
            ###
            #..
            ###
            
            5:
            ###
            .#.
            ###
            
            4x4: 0 0 0 0 2 0
            12x5: 1 0 1 0 2 2
            12x5: 1 0 1 0 3 2""";

    @Test
    void parse() {
        var res = Twelve.parse(EXAMPLE);

        assertThat(res).isNotNull();
        assertThat(res.shapes).hasSize(6);
        assertThat(res.regions).hasSize(3);
    }

    @Test
    void part1() {
        var res = Twelve.parse(EXAMPLE);

        var part1 = res.solvePart1();
        assertThat(part1).isEqualTo(0);
    }

    @Test
    void parseShape() {
        var input = """
                ###
                #..
                ###""";

        var parsed = Shape.parse(input);

        assertThat(parsed).isNotNull();
        assertThat(parsed.shape()).hasDimensions(3, 3);
        assertThat(parsed.shape()[0]).containsExactly(true, true, true);
        assertThat(parsed.shape()[1]).containsExactly(true, false, false);
        assertThat(parsed.shape()[2]).containsExactly(true, true, true);

        assertThat(parsed.getSizeInPx()).isEqualTo(input.chars().filter(c -> c == '#').count());
        assertThat(parsed.toString()).contains(input);

        var rotated = parsed.rotateRight();
        assertThat(rotated).isNotNull();
        assertThat(rotated).isNotEqualTo(parsed);
        assertThat(rotated.getSizeInPx()).isEqualTo(parsed.getSizeInPx());
        assertThat(rotated.shape()[0]).containsExactly(true, true, true);
        assertThat(rotated.shape()[1]).containsExactly(true, false, true);
        assertThat(rotated.shape()[2]).containsExactly(true, false, true);

        assertThat(parsed.rotateLeft().rotateRight()).isEqualTo(parsed);
    }

    @Test
    void parseRegion() {
        var input = "12x5: 1 0 1 0 2 2";

        var parsed = Region.parse(input);

        assertThat(parsed).isNotNull();
        assertThat(parsed.width()).isEqualTo(12);
        assertThat(parsed.height()).isEqualTo(5);
        assertThat(parsed.area().length).isEqualTo(12);
        assertThat(parsed.area()[0].length).isEqualTo(5);
        assertThat(parsed.requiredShapes().length).isEqualTo(6);
        assertThat(parsed.requiredShapes()[0]).isEqualTo(1);
        assertThat(parsed.requiredShapes()[5]).isEqualTo(2);
    }

}