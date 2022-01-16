package assignmentproblem;

import java.util.List;

/**
 * Immutable wrapper for the solution of an assignment problem.
 */
public abstract class Result {
    /** Default value for unassigned rows or columns.*/
    public final Integer unassigned;
    
    public Result(Integer unassigned){
        this.unassigned = unassigned;
    }
    /**
     * Returns the column index assigned to each row.
     * @return The result of the assignment problem from the row perspective.
     * The i-th element of the output is the index of the column assigned to the
     * i-th row, or {@link #unassigned} if the row has not been assigned.
     */
    public abstract List<Integer> getRowAssignments();
    /**
     * Returns the row index assigned to each column.
     * @return The result of the assignment problem from the column perspective.
     * The i-th element of the output is the index of the row assigned to the
     * i-th column, or {@link #Unassigned} if the column has not been assigned.
     */
    public abstract List<Integer> getColumnAssignments();
}