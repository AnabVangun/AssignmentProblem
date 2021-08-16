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
 * If several solutions exist, the solver SHOULD select and return only one.
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
     * Returns the column index assigned to each row.
     * @return The result of the assignment problem from the row perspective.
     * The i-th element of the output is the index of the column assigned to the
     * i-th row, or -1 if the row has not been assigned.
     */
    public abstract int[] getRowAssignments();
    /**
     * Returns the row index assigned to each column.
     * @return The result of the assignment problem from the column perspective.
     * The i-th element of the output is the index of the row assigned to the
     * i-th column, or -1 if the column has not been assigned.
     */
    public abstract int[] getColumnAssignemnts();
    /**
     * Returns the pairs of row and column indices of the assignments.
     * @return The result of the assignment problem as pairs. Each element of 
     * the output is an assigned pair whose first element is the index of the 
     * row and the second element is the index of the column. Unassigned rows
     * and columns are not included.
     */
    public abstract int[][] getAssignments();
    
    /**
     * Checks the validity of the input cost matrix.
     * @param costMatrix the matrix to solve.
     * @throws IllegalArgumentException if {@code costMatrix } is not 
     * rectangular (e.g. rows do not all have the same length).
     */
    static void checkMatrixValidity(int[][] costMatrix)
            throws IllegalArgumentException{
        if (costMatrix == null){
            throw new IllegalArgumentException("input matrix was null");
        }
        if (costMatrix.length == 0){
            throw new IllegalArgumentException("input matrix was of length 0");
        }
        for (int[] row : costMatrix){
            if (row.length != costMatrix[0].length){
                throw new IllegalArgumentException("input matrix was not rectangular");
            }
        }
    }
    /**
     * No-op constructor to restrict access.
     */
    Solver(){};
}
