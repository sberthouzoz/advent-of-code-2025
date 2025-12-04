package adventofcode.y2025;

public class Four {
    private final boolean[][] input;

    public Four(int nbRows, int nbCols) {
        input = new boolean[nbRows][nbCols];
    }

    public static Four parse(String input) {
        final int[] len = {0};
        var nbLines = input.lines().parallel().peek(s -> {
            if (len[0] == 0) len[0] = s.length();
        }).count();
        return new Four((int) nbLines, len[0]);
    }

    public boolean[][] getInput() {
        return input;
    }
}