/*
adapted from https://www.happycoders.eu/algorithms/advent-of-code-2025/,
https://github.com/SvenWoltmann/advent-of-code-2025/blob/main/src/main/java/eu/happycoders/adventofcode2025/day10/GaussianElimination.java
instanceof int changed to doubleVar % 1 == 0
 */
package others.day10;

import java.util.ArrayList;
import java.util.List;

public class GaussianElimination {
    private final int[][] matrix;
    private final int numColumnsExceptSum;
    private final int[] maxFactors;

    public GaussianElimination(int[][] matrix) {
        this.matrix = matrix;
        this.numColumnsExceptSum = matrix[0].length - 1;
        this.maxFactors = calculateMaxFactors();
    }

    private static boolean isInstanceOfInt(double d) {
        return d == Math.rint(d);
    }

    private static int findRowToSwitch(int[][] matrix, int row, int col) {
        for (int row2 = row + 1; row2 < matrix.length; row2++) {
            if (matrix[row2][col] != 0) {
                return row2;
            }
        }
        return -1;
    }

    private static void switchRows(int[][] matrix, int row, int rowToSwitchWith) {
        int[] help = matrix[row];
        matrix[row] = matrix[rowToSwitchWith];
        matrix[rowToSwitchWith] = help;
    }

    private static void findSingleSolution(int[] currentRow, int[] factors, SolutionsContext solutionsContext,
                                           List<Integer> nonZeroColsWithoutFactor, int numberWeNeedToReach) {
        int index = nonZeroColsWithoutFactor.getFirst();
        double factor = divide(numberWeNeedToReach, currentRow[index]);
        if (factor % 1 == 0 && ((int) factor) >= 0) {
            factors[index] = (int) factor;
        } else {
            solutionsContext.markCurrentSolutionAsInvalid();
        }
    }

    private static void addPossibleCombinations(List<int[]> possibleCombinations,
                                                int[] factors,
                                                SolutionsContext solutionsContext,
                                                int... indices) {
        if (possibleCombinations.isEmpty()) {
            solutionsContext.markCurrentSolutionAsInvalid();
            return;
        }

        int[] firstCombination = possibleCombinations.getFirst();
        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            factors[index] = firstCombination[i];
        }

        // More --> Add new solutions
        for (int ci = 1; ci < possibleCombinations.size(); ci++) {
            int[] combination = possibleCombinations.get(ci);
            int[] newFactors = factors.clone();
            for (int i = 0; i < indices.length; i++) {
                int index = indices[i];
                newFactors[index] = combination[i];
            }
            solutionsContext.addSolution(newFactors);
        }
    }

    private static double divide(int dividend, int divisor) {
        double fraction = (double) dividend / divisor;

        // -0.0 isn't an `instanceof int` --> convert it to 0.0
        if (fraction == -0.0) {
            fraction = 0.0;
        }

        return fraction;
    }

    private int[] calculateMaxFactors() {
        int[] result = new int[numColumnsExceptSum];
        for (int col = 0; col < numColumnsExceptSum; col++) {
            result[col] = calculateMaxFactor(col);
        }
        return result;
    }

    private int calculateMaxFactor(int col) {
        int maxFactor = Integer.MAX_VALUE;
        for (int[] row : matrix) {
            if (row[col] != 0) {
                double factorAtIndex = divide(row[numColumnsExceptSum], row[col]);
                if (isInstanceOfInt(factorAtIndex)) {
                    int f = (int) factorAtIndex;
                    if (f < maxFactor) {
                        maxFactor = f;
                    }
                } else {
                    throw new IllegalStateException("Non-integer factor: " + factorAtIndex);
                }
            }
        }
        return maxFactor;
    }

    public List<int[]> solve() {
        reduceRows(matrix);

        int[] initialFactors = createInitialFactors();

        List<int[]> solutions = new ArrayList<>();
        solutions.add(initialFactors);

        for (int row = matrix.length - 1; row >= 0; row--) {
            int[] currentRow = matrix[row];
            if (isAllZero(currentRow)) {
                if (currentRow[numColumnsExceptSum] != 0) {
                    throw new IllegalStateException("All zero except sum column");
                } else {
                    continue;
                }
            }

            int numSolutions = solutions.size();
            List<Integer> invalidSolutions = new ArrayList<>();

            for (int solutionIndex = 0; solutionIndex < numSolutions; solutionIndex++) {
                int[] factors = solutions.get(solutionIndex);

                findSolutionsForRowAndFactors(currentRow, factors,
                        new SolutionsContext(solutions, invalidSolutions, solutionIndex));
            }

            for (int s : invalidSolutions.reversed()) {
                solutions.remove(s);
            }
        }
        return solutions;
    }

    private void reduceRows(int[][] matrix) {
        int col = 0;
        for (int row = 0; row < matrix.length && col < numColumnsExceptSum; row++) {
            while (matrix[row][col] == 0 && col < numColumnsExceptSum) {
                int rowToSwitchWith = findRowToSwitch(matrix, row, col);
                if (rowToSwitchWith != -1) {
                    switchRows(matrix, row, rowToSwitchWith);
                } else {
                    col++;
                }
            }

            adjustRowsBelow(matrix, row, col);
            col++;
        }
    }

    private void adjustRowsBelow(int[][] matrix, int row, int col) {
        for (int row2 = row + 1; row2 < matrix.length; row2++) {
            if (matrix[row2][col] != 0) {
                int factor = computeRowFactor(matrix, row, row2, col);
                for (int col2 = col; col2 <= numColumnsExceptSum; col2++) {
                    matrix[row2][col2] = matrix[row2][col2] - matrix[row][col2] * factor;
                }
            }
        }
    }

    private int computeRowFactor(int[][] matrix, int row, int row2, int col) {
        int[] originalRow2 = matrix[row2].clone();
        while (true) {
            double factor = divide(matrix[row2][col], matrix[row][col]);
            if (isInstanceOfInt(factor)) {
                return (int) factor;
            } else {
                for (int col2 = col; col2 <= numColumnsExceptSum; col2++) {
                    matrix[row2][col2] += originalRow2[col2];
                }
            }
        }
    }

    private int[] createInitialFactors() {
        int[] initialFactors = new int[numColumnsExceptSum];
        for (int i = 0; i < numColumnsExceptSum; i++) {
            initialFactors[i] = -1; // -1 = not calculated
        }
        return initialFactors;
    }

    private boolean isAllZero(int[] row) {
        for (int i = 0; i < numColumnsExceptSum; i++) {
            if (row[i] != 0) {
                return false;
            }
        }
        return true;
    }

    private void findSolutionsForRowAndFactors(int[] currentRow, int[] factors, SolutionsContext solutionsContext) {
        List<Integer> nonZeroColsWithoutFactor = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < numColumnsExceptSum; i++) {
            if (factors[i] != -1) {
                sum += currentRow[i] * factors[i];
            } else if (currentRow[i] != 0) {
                nonZeroColsWithoutFactor.add(i);
            }
        }

        int numberWeNeedToReach = currentRow[numColumnsExceptSum] - sum;

        if (nonZeroColsWithoutFactor.size() == 3) {
            findSolutionTriplets(currentRow, factors, solutionsContext, nonZeroColsWithoutFactor, numberWeNeedToReach);
        } else if (nonZeroColsWithoutFactor.size() == 2) {
            findSolutionTuples(currentRow, factors, solutionsContext, nonZeroColsWithoutFactor, numberWeNeedToReach);
        } else if (nonZeroColsWithoutFactor.size() == 1) {
            findSingleSolution(currentRow, factors, solutionsContext, nonZeroColsWithoutFactor, numberWeNeedToReach);
        } else {
            throw new IllegalStateException(
                    "Unhandled number of non-zero columns without a factor: " + nonZeroColsWithoutFactor.size());
        }
    }

    private void findSolutionTriplets(int[] currentRow, int[] factors, SolutionsContext solutionsContext,
                                      List<Integer> nonZeroColsWithoutFactor, int numberWeNeedToReach) {
        int index1 = nonZeroColsWithoutFactor.get(0);
        int index2 = nonZeroColsWithoutFactor.get(1);
        int index3 = nonZeroColsWithoutFactor.get(2);

        int max1 = maxFactors[index1];
        int max2 = maxFactors[index2];
        int max3 = maxFactors[index3];

        List<int[]> possibleCombinations = new ArrayList<>();

        for (int factor1 = 0; factor1 <= max1; factor1++) {
            for (int factor2 = 0; factor2 <= max2; factor2++) {
                double factor3 = divide(numberWeNeedToReach - factor1 * currentRow[index1] - factor2 * currentRow[index2],
                        currentRow[index3]);
                if (factor3 >= 0 && isInstanceOfInt(factor3) && ((int) factor3) <= max3) {

                    possibleCombinations.add(new int[]{factor1, factor2, (int) factor3});
                }
            }
        }

        addPossibleCombinations(possibleCombinations, factors, solutionsContext, index1, index2, index3);
    }

    private void findSolutionTuples(int[] currentRow, int[] factors, SolutionsContext solutionsContext,
                                    List<Integer> nonZeroColsWithoutFactor, int numberWeNeedToReach) {
        int index1 = nonZeroColsWithoutFactor.get(0);
        int index2 = nonZeroColsWithoutFactor.get(1);

        int max1 = maxFactors[index1];
        int max2 = maxFactors[index2];

        List<int[]> possibleCombinations = new ArrayList<>();

        for (int factor1 = 0; factor1 <= max1; factor1++) {
            double factor2 = divide(numberWeNeedToReach - factor1 * currentRow[index1], currentRow[index2]);
            if (factor2 >= 0 && isInstanceOfInt(factor2) && ((int) factor2) <= max2) {
                possibleCombinations.add(new int[]{factor1, (int) factor2});
            }
        }

        addPossibleCombinations(possibleCombinations, factors, solutionsContext, index1, index2);
    }

    private record SolutionsContext(List<int[]> solutions, List<Integer> invalidSolutions, int solutionIndex) {
        void addSolution(int[] newFactors) {
            solutions.add(newFactors);
        }

        void markCurrentSolutionAsInvalid() {
            invalidSolutions.add(solutionIndex);
        }
    }
}