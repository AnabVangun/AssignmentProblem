package assignmentproblem.hungariansolver;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Wrapper for cases to test an Hungarian solver implementation.
 */
public class HungarianSolverTestCase {
    /** Input cost matrix to solve. */
    final int[][] costMatrix;
    /** 
     * Expected cost matrix after optional transposition to make it horizontal and reducing by rows.
     */
    final int[][] horizontalRowReducedCostMatrix;
    /** 
     * Expected cost matrix after optional transposition to make it horizontal and reducing by 
     * columns.
     */
    final int[][] horizontalColReducedCostMatrix;
    /** 
     * Expected cost matrix after optional transposition to make it horizontal and reducing first by
     * rows and then by columns.
     */
    final int[][] horizontalRowThenColReducedCostMatrix;
    /** 
     * Expected cost matrix after optional transposition to make it vertical and reducing by rows.
     */
    final int[][] verticalRowReducedCostMatrix;
    /** 
     * Expected cost matrix after optional transposition to make it vertical and reducing by 
     * columns.
     */
    final int[][] verticalColReducedCostMatrix;
    /** 
     * Expected cost matrix after optional transposition to make it vertical and reducing first by
     * rows and then by columns.
     */
    final int[][] verticalRowThenColReducedCostMatrix;
    /** Expected row assignments. */
    final Integer[] rows;
    /** Expected column assignments. */
    final Integer[] cols;
    /** Name of the test case. */
    final String name;
    /** Value to use as the solver's unassigned value. */
    public static final Integer UNASSIGNED_VALUE = null;
    /**
     * Initialise a test case.
     * @param costMatrix Cost matrix of the problem.
     * @param horizontalRowReducedCostMatrix reduced cost matrix if it was reduced only by rows 
     *     after optionally being flipped to have more columns than rows.
     * @param horizontalColReducedCostMatrix reduced cost matrix if it was reduced only by columns 
     *     after optionally being flipped to have more columns than rows.
     * @param horizontalRowThenColReducedCostMatrix reduced cost matrix if it was reduced after 
     *     optionally being flipped to have more columns than rows.
     * @param verticalRowReducedCostMatrix reduced cost matrix if it was reduced only by rows 
     *     after optionally being flipped to have more rows than columns.
     * @param verticalColReducedCostMatrix reduced cost matrix if it was reduced only by columns 
     *     after optionally being flipped to have more rows than columns.
     * @param verticalRowThenColReducedCostMatrix reduced cost matrix if it was reduced after 
     *     optionally being flipped to have more rows than columns.
     * @param rows row assignments of the optimal solution.
     * @param cols column assignments of the optimal solution.
     * @param name of the test case.
     */
    HungarianSolverTestCase(int[][] costMatrix, 
            int[][] horizontalRowReducedCostMatrix, int[][] horizontalColReducedCostMatrix,
            int[][] horizontalRowThenColReducedCostMatrix, 
            int[][] verticalRowReducedCostMatrix, int[][] verticalColReducedCostMatrix,
            int[][] verticalRowThenColReducedCostMatrix, 
            Integer[] rows, Integer[] cols, String name){
        this.costMatrix = costMatrix;
        this.horizontalRowReducedCostMatrix = horizontalRowReducedCostMatrix;
        this.horizontalColReducedCostMatrix = horizontalColReducedCostMatrix;
        this.horizontalRowThenColReducedCostMatrix = horizontalRowThenColReducedCostMatrix;
        this.verticalRowReducedCostMatrix = verticalRowReducedCostMatrix;
        this.verticalColReducedCostMatrix = verticalColReducedCostMatrix;
        this.verticalRowThenColReducedCostMatrix = verticalRowThenColReducedCostMatrix;
        this.rows = rows;
        this.cols = cols;
        this.name = name;
    }
    /**
     * Initialise a test case.
     * @param costMatrix Cost matrix of the problem.
     * @param rowReducedCostMatrix reduced cost matrix if it was reduced only by rows 
     *     after optionally being flipped to have more columns than rows.
     * @param colReducedCostMatrix reduced cost matrix if it was reduced only by columns 
     *     after optionally being flipped to have more columns than rows.
     * @param rowThenColReducedCostMatrix reduced cost matrix if it was reduced after 
     *     optionally being flipped to have more columns than rows.
     * @param rows row assignments of the optimal solution.
     * @param cols column assignments of the optimal solution.
     * @param name of the test case.
     */
    HungarianSolverTestCase(int[][] costMatrix, 
            int[][] rowReducedCostMatrix, int[][] colReducedCostMatrix,
            int[][] rowThenColReducedCostMatrix, 
            Integer[] rows, Integer[] cols, String name){
        if (costMatrix.length != costMatrix[0].length){
            throw new IllegalArgumentException("The square constructor can only be used for "
                + "square matrices, received " + Arrays.deepToString(costMatrix) + " of lengths " 
                + costMatrix.length + " and " + costMatrix[0].length);
        }
        this.costMatrix = costMatrix;
        this.horizontalRowReducedCostMatrix = rowReducedCostMatrix;
        this.horizontalColReducedCostMatrix = colReducedCostMatrix;
        this.horizontalRowThenColReducedCostMatrix = rowThenColReducedCostMatrix;
        this.verticalRowReducedCostMatrix = rowReducedCostMatrix;
        this.verticalColReducedCostMatrix = colReducedCostMatrix;
        this.verticalRowThenColReducedCostMatrix = rowThenColReducedCostMatrix;
        this.rows = rows;
        this.cols = cols;
        this.name = name;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    /**
     * Produces a stream of test cases for an Hungarian algorithm.
     * The stream contains several standard test cases and a variable-sized worst-case test case for
     * the algorithm.
     * @param worstCaseSize size of the worst-case matrix.
     * @return A stream of test cases with input and expected result.
     */
    public static Stream<HungarianSolverTestCase> getStandardCases(int worstCaseSize){
        int[][] worstCaseMatrix = new int[worstCaseSize][worstCaseSize];
        int[][] rowThenColReducedWorstCaseMatrix = new int[worstCaseSize][worstCaseSize];
        int[][] rowReducedWorstCaseMatrix = new int[worstCaseSize][worstCaseSize];
        int[][] colReducedWorstCaseMatrix = new int[worstCaseSize][worstCaseSize];
        for (int i = 0; i < worstCaseMatrix.length; i++) {
            for (int j = 0; j < worstCaseMatrix[i].length; j++){
                worstCaseMatrix[i][j] = (i+1)*(j+1);
                //Reduced value: remove min value from row and column but after column was reduced
                rowReducedWorstCaseMatrix[i][j] = worstCaseMatrix[i][j] - worstCaseMatrix[i][0];
                colReducedWorstCaseMatrix[i][j] = worstCaseMatrix[i][j] - worstCaseMatrix[0][j];
                rowThenColReducedWorstCaseMatrix[i][j] = rowReducedWorstCaseMatrix[i][j]
                    - worstCaseMatrix[0][j] + worstCaseMatrix[0][0];
            }
        }
        Integer[] worstCaseLinearExpectation = new Integer[worstCaseSize];
        Arrays.setAll(worstCaseLinearExpectation, i -> worstCaseSize-i-1);
        return Stream.of(new HungarianSolverTestCase(
                new int[][]{{2500, 4000, 3500}, {4000, 6000, 3500}, {2000, 4000, 2500}},
                new int[][]{{0, 1500, 1000}, {500, 2500, 0}, {0, 2000, 500}},
                new int[][]{{500, 0, 1000}, {2000, 2000, 1000}, {0, 0, 0}},
                new int[][]{{0, 0, 1000},{500, 1000, 0}, {0, 500, 500}},
                new Integer[]{1,2,0}, new Integer[]{2,0,1}, "simple 3*3 matrix"),
            new HungarianSolverTestCase(
                new int[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}},
                new int[][]{{0,1,2,3},{0,1,2,3},{0,1,2,3}},
                new int[][]{{0,0,0,0},{4,4,4,4},{8,8,8,8}},
                new int[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0}},
                new int[][]{{0,4,8},{0,4,8},{0,4,8},{0,4,8}},
                new int[][]{{0,0,0},{1,1,1},{2,2,2},{3,3,3}},
                new int[][]{{0,0,0},{0,0,0},{0,0,0}, {0,0,0}},
                new Integer[]{0,1,2}, new Integer[]{0,1,2, UNASSIGNED_VALUE},
                "3*4 matrix with equality case"),
            new HungarianSolverTestCase(
                new int[][]{{2000,6000,3500},{1500, 4000, 4500},{2000,4000,2500}},
                new int[][]{{0, 4000, 1500}, {0, 2500, 3000}, {0, 2000, 500}},
                new int[][]{{500, 2000, 1000}, {0, 0, 2000}, {500, 0, 0}},
                new int[][]{{0, 2000, 1000}, {0, 500, 2500}, {0, 0, 0}},
                new Integer[]{0,1,2}, new Integer[]{0,1,2}, "mildly complex 3*3 matrix"),
            new HungarianSolverTestCase(
                new int[][]{{1,2,25,13},{5,7,25,15},{10,13,16,13},{17,21,11,18},{15,15,15,14}},
                new int[][]{{0,4,9,16,14},{0,5,11,19,13},{14,14,5,0,4},{0,2,0,5,1}},
                new int[][]{{0,0,0,6,1},{1,2,3,10,1},{24,20,6,0,1},{12,10,3,7,0}},
                new int[][]{{0,2,9,16,13},{0,3,11,19,12},{14,12,5,0,3},{0,0,0,5,0}},
                new int[][]{{0,1,24,12},{0,2,20,10},{0,3,6,3},{6,10,0,7},{1,1,1,0}},
                new int[][]{{0,0,14,0},{4,5,14,2},{9,11,5,0},{16,19,0,5},{14,13,4,1}},
                new int[][]{{0,0,24,12},{0,1,20,10},{0,2,6,3},{6,9,0,7},{1,0,1,0}},
                new Integer[]{1,0,UNASSIGNED_VALUE,2,3}, new Integer[]{1,0,3,4},
                "first complex 5*4 matrix without equality case"),
            new HungarianSolverTestCase(
                new int[][]{{1,2,25,13},{5,7,25,15},{10,13,16,14},{17,21,11,18},{15,15,15,13}},
                new int[][]{{0,4,9,16,14},{0,5,11,19,13},{14,14,5,0,4},{0,2,1,5,0}},
                new int[][]{{0,0,0,6,2},{1,2,3,10,2},{24,20,6,0,2},{12,10,4,7,0}},
                new int[][]{{0,2,8,16,14},{0,3,10,19,13},{14,12,4,0,4},{0,0,0,5,0}},
                new int[][]{{0,1,24,12},{0,2,20,10},{0,3,6,4},{6,10,0,7},{2,2,2,0}},
                new int[][]{{0,0,14,0},{4,5,14,2},{9,11,5,1},{16,19,0,5},{14,13,4,0}},
                new int[][]{{0,0,24,12},{0,1,20,10},{0,2,6,4},{6,9,0,7},{2,1,2,0}},
                new Integer[]{1,0,3,2,UNASSIGNED_VALUE}, new Integer[]{1,0,3,2},
                "second complex 5*4 matrix without equality case"),
            new HungarianSolverTestCase(
                new int[][]{{1,2,3,1001},{2,4,6,1002},{3,6,9,1003}},
                new int[][]{{0,1,2,1000},{0,2,4,1000},{0,3,6,1000}},
                new int[][]{{0,0,0,0},{1,2,3,1},{2,4,6,2}},
                new int[][]{{0,0,0,0},{0,1,2,0},{0,2,4,0}},
                new int[][]{{0,1,2},{0,2,4},{0,3,6},{0,1,2}},
                new int[][]{{0,0,0},{1,2,3},{2,4,6},{1000,1000,1000}},
                new int[][]{{0,0,0},{0,1,2},{0,2,4},{0,0,0}},
                new Integer[]{2,1,0}, new Integer[]{2,1,0,UNASSIGNED_VALUE},
                "square 3*3 matrix with prohibitive column"),
            new HungarianSolverTestCase(
                worstCaseMatrix, rowReducedWorstCaseMatrix, colReducedWorstCaseMatrix,
                rowThenColReducedWorstCaseMatrix,
                worstCaseLinearExpectation, worstCaseLinearExpectation,
                "worst case " + worstCaseSize + "*" + worstCaseSize + " matrix"
            )
        );
    }   
}
