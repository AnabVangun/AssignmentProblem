package assignmentproblem.hungariansolver;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Wrapper for cases to test an Hungarian solver implementation.
 */
public class HungarianSolverTestCase {
    final int[][] costMatrix;
    final int[][] horizontallyReducedCostMatrix;
    final int[][] verticallyReducedCostMatrix;
    final Integer[] rows;
    final Integer[] cols;
    final String name;
    public static final Integer UNASSIGNED_VALUE = null;
    /**
     * Initialise a test case.
     * @param costMatrix Cost matrix of the problem.
     * @param horizontallyReducedCostMatrix reduced cost matrix if it was reduced after optionally
     *     being flipped to have more columns than rows.
     * @param verticallyReducedCostMatrix reduced cost matrix if it was reduced after optionally
     *     being flipped to have more rows than columns.
     * @param rows row assignments of the optimal solution.
     * @param cols column assignments of the optimal solution.
     * @param name of the test case.
     */
    HungarianSolverTestCase(int[][] costMatrix, int[][] horizontallyReducedCostMatrix, 
            int[][] verticallyReducedCostMatrix, Integer[] rows, Integer[] cols, String name){
        this.costMatrix = costMatrix;
        this.horizontallyReducedCostMatrix = horizontallyReducedCostMatrix;
        this.verticallyReducedCostMatrix = verticallyReducedCostMatrix;
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
        int[][] reducedWorstCaseMatrix = new int[worstCaseSize][worstCaseSize];
        for (int i = 0; i < worstCaseMatrix.length; i++) {
            for (int j = 0; j < worstCaseMatrix[i].length; j++){
                worstCaseMatrix[i][j] = (i+1)*(j+1);
                //Reduced value: remove min value from row and column but after column was reduced
                reducedWorstCaseMatrix[i][j] = worstCaseMatrix[i][j] - worstCaseMatrix[0][j] 
                    - worstCaseMatrix[i][0] + worstCaseMatrix[0][0];
            }
        }
        Integer[] worstCaseLinearExpectation = new Integer[worstCaseSize];
        Arrays.setAll(worstCaseLinearExpectation, i -> worstCaseSize-i-1);
        return Stream.of(new HungarianSolverTestCase(
                new int[][]{{2500, 4000, 3500}, {4000, 6000, 3500}, {2000, 4000, 2500}},
                new int[][]{{0, 0, 1000},{500, 1000, 0}, {0, 500, 500}},
                new int[][]{{0, 0, 1000},{500, 1000, 0}, {0, 500, 500}},
                new Integer[]{1,2,0}, new Integer[]{2,0,1}, "simple 3*3 matrix"),
            new HungarianSolverTestCase(
                new int[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}},
                new int[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0}},
                new int[][]{{0,0,0},{0,0,0},{0,0,0}, {0,0,0}},
                new Integer[]{0,1,2}, new Integer[]{0,1,2, UNASSIGNED_VALUE},
                "3*4 matrix with equality case"),
            new HungarianSolverTestCase(
                new int[][]{{2000,6000,3500},{1500, 4000, 4500},{2000,4000,2500}},
                new int[][]{{0, 2000, 1000}, {0, 500, 2500}, {0, 0, 0}},
                new int[][]{{0, 2000, 1000}, {0, 500, 2500}, {0, 0, 0}},
                new Integer[]{0,1,2}, new Integer[]{0,1,2}, "mildly complex 3*3 matrix"),
            new HungarianSolverTestCase(
                new int[][]{{1,2,25,13},{5,7,25,15},{10,13,16,13},{17,21,11,18},{15,15,15,14}},
                new int[][]{{0,2,9,16,13},{0,3,11,19,12},{14,12,5,0,3},{0,0,0,5,0}},
                new int[][]{{0,0,24,12},{0,1,20,10},{0,2,6,3},{6,9,0,7},{1,0,1,0}},
                new Integer[]{1,0,3,2,UNASSIGNED_VALUE}, new Integer[]{1,0,3,2},
                "first complex 5*4 matrix without equality case"),
            new HungarianSolverTestCase(
                new int[][]{{1,2,25,13},{5,7,25,15},{10,13,16,14},{17,21,11,18},{15,15,15,13}},
                new int[][]{{0,2,8,16,14},{0,3,10,19,13},{14,12,4,0,4},{0,0,0,5,0}},
                new int[][]{{0,0,24,12},{0,1,20,10},{0,2,6,4},{6,9,0,7},{2,1,2,0}},
                new Integer[]{1,0,3,2,UNASSIGNED_VALUE}, new Integer[]{1,0,3,2},
                "second complex 5*4 matrix without equality case"),
            new HungarianSolverTestCase(
                worstCaseMatrix, reducedWorstCaseMatrix, reducedWorstCaseMatrix,
                worstCaseLinearExpectation, worstCaseLinearExpectation,
                "worst case " + worstCaseSize + "*" + worstCaseSize + " matrix"
            )
        );
    }   
}
