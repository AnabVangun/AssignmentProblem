package AssignmentProblem;

import java.util.function.Function;
/**
 * Template for a solver for the assignment problem.
 * Implementations of this class SHOULD solve the given problem at 
 * initialisation time, rather than each time one of the methods to get results
 * is called.
 * The input of the assignment problem MAY be a rectangular cost matrix rather 
 * than a square one. In the former case, the extra rows or columns will not be
 * assigned. The input MAY be modified in-place during the search for an optimal
 * solution.
 * If several solutions exist, the solver will select and return only one.
 * @author AnabVangun
 */
public abstract class Solver {
    /**
     * Types of solvers available to solve an assignment problem.
     */
    public static enum SolverType {

        /**
         * Implementation of the Hungarian algorithm.
         */
        HUNGARIAN(args -> HungarianSolver.initialise(args));

        private final Function<int[][], Solver> generator;

        private SolverType(Function<int[][], Solver> generator) {
            this.generator = generator;
        }
    };
    /**
     * Default value used to fill added rows and columns used to make cost 
     * matrix square.
     */
    public static final int DEFAULT_VALUE = Integer.MAX_VALUE;
    /**
     * Solver used by default when calling {@link #solve(int[][]) }.
     */
    public static final SolverType DEFAULT_SOLVER = SolverType.HUNGARIAN;
    /**
     * Solves an assignment problem with a given cost matrix.
     * @param costMatrix matrix where cell[i][j] represents the cost of 
     * assigning row i to column j. {@code costMatrix } MAY be rectangular 
     * rather than square, in which case the extra rows or columns will not be 
     * assigned. {@code costMatrix } MAY be modified in-place by the 
     * {@link Solver}.
     * @return The {@link Solver} object containing the solution to the problem.
     */
    public static Solver solve(int[][] costMatrix){
        return solve(costMatrix, DEFAULT_SOLVER);
    }
    /**
     * Solves an assignment problem with a given cost matrix.
     * @param costMatrix matrix where cell[i][j] represents the cost of 
     * assigning row i to column j. {@code costMatrix } MAY be rectangular 
     * rather than square, in which case the extra rows or columns will not be 
     * assigned. {@code costMatrix } MAY be modified in-place by the 
     * {@link Solver}.
     * @param solver type of solver used to solve the assignment problem.
     * @return The {@link Solver} object containing the solution to the problem.
     */
    public static Solver solve(int[][] costMatrix, SolverType solver){
        return solver.generator.apply(costMatrix);
    }
    /**
     * @return The result of the assignment problem from the row perspective.
     * The i-th element of the output is the index of the column assigned to the
     * i-th row, or -1 if the row has not been assigned.
     */
    public abstract int[] getRowAssignments();
    /**
     * @return The result of the assignment problem from the column perspective.
     * The i-th element of the output is the index of the row assigned to the
     * i-th column, or -1 if the column has not been assigned.
     */
    public abstract int[] getColumnAssignemnts();
    /**
     * @return The result of the assignment problem as pairs. Each element of 
     * the output is an assigned pair whose first element is the index of the 
     * row and the second element is the index of the column. Unassigned rows
     * and columns are not included.
     */
    public abstract int[][] getAssignments();
    /**
     * Generates the smallest possible square matrix containing the input array.
     * If the matrix contains more rows (resp. columns) than columns (resp. 
     * rows), columns (resp. rows) will be added to make the matrix square.
     * @param costMatrix matrix to make square.
     * @param defaultValue value for the added cells.
     * @return A square matrix in which each cell with indices found in 
     * {@code costMatrix } has the same value as in it, and the others all have
     * the {@code defaultValue }.
     * @throws  IllegalArgumentException if {@code costMatrix } is not 
     * rectangular (e.g. rows do not all have the same length).
     */
    static int[][] squarifyMatrix(int[][] costMatrix, int defaultValue) 
            throws IllegalArgumentException{
        if (costMatrix == null){
            throw new IllegalArgumentException("squarifyMatrix input was null");
        }
        int n = costMatrix.length;
        if (n == 0){
            throw new IllegalArgumentException("squarifyMatrix input was of length 0");
        }
        int m = costMatrix[0].length;
        for (int[] row : costMatrix){
            if (row.length != m){
                throw new IllegalArgumentException("squarifyMatrix input was not rectangular");
            }
        }
        if (n == m){
            return costMatrix;
        }
        int[][] result = n < m ? new int[m][m] : new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(costMatrix[i], 0, result[i], 0, m);
        }
        if (n < m){
            for (int i = n; i < m; i++){
                for (int j = 0; j < m; j++){
                    result[i][j] = defaultValue;
                }
            }
        } else {
            for (int i = 0; i < n; i++){
                for (int j = m; j < n; j++){
                    result[i][j] = defaultValue;
                }
            }
        }
        return result;
    }
    /**
     * No-op constructor to restrict access.
     */
    Solver(){};
}
