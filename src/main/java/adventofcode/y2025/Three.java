package adventofcode.y2025;

public class Three {
    static int[] parseBank(String bank) {
        return bank.chars().map(c -> Character.digit(c, 10)).toArray();
    }
}