package adventofcode.y2025;

import adventofcode.y2025.Twelve.Region;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TwelveTest {
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