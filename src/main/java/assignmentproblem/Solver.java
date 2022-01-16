package assignmentproblem;

/**
 * Template for a solver for the assignment problem.
 * @param <T> type of input matrix compatible with the solver.
 */
public interface Solver<T extends CostMatrix> {
    /**
     * Solves an assignment problem with a given cost matrix.
     * @param costMatrix Valid cost matrix representing to problem to solve.
     *     Note that {@code costMatrix } MAY be modified in-place by the {@link Solver}.
     * @return the {@link Result} object wrapping the solution to the problem.
     */
    Result solve(T costMatrix);
    
    /**
     * Solves an assignment problem with a given cost matrix.
     * @param costMatrix Cost matrix representing to problem to solve.
     *     Note that {@code costMatrix } MAY be modified in-place by the {@link Solver}.
     * @return the {@link Result} object wrapping the solution to the problem.
     * @throws IllegalArgumentException If the cost matrix is invalid for the given solver.
     */
    Result solve(int[][] costMatrix) throws IllegalArgumentException;
}
