package assignmentproblem.hungariansolver;

import assignmentproblem.Result;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Package-private implementation of the {@link assignmentproblem.Result} class used to report 
 * results in an unmodifiable way.
 */
class HungarianResult extends Result{
    private final List<Integer> rows;
    private final List<Integer> cols;
    /**
     * Initialise a result.
     * @param rows Row assignments to report through {@link #getRowAssignments()}.
     * @param cols Column assignments to report through {@link #getColumnAssignments()}.
     * @param unassigned Default value of unassigned rows and columns.
     */
    HungarianResult(Integer[] rows, Integer[] cols, Integer unassigned){
        super(unassigned);
        this.rows = Collections.unmodifiableList(Arrays.asList(rows));
        this.cols = Collections.unmodifiableList(Arrays.asList(cols));
    }
    /**
     * Private constructor used to transpose results.
     * @param rows Unmodifiable list of row assignments.
     * @param cols Unmodifiable list of column assignments.
     * @param unassigned Default value of unassigned rows and columns.
     */
    private HungarianResult(List<Integer> rows, List<Integer> cols, Integer unassigned){
        super(unassigned);
        this.rows = rows;
        this.cols = cols;
    }
    @Override
    public List<Integer> getRowAssignments() {
        return this.rows;
    }
    @Override
    public List<Integer> getColumnAssignments() {
        return this.cols;
    }
    /**
     * Transpose a result.
     * @return A result object whose rows are exchanged with its columns.
     */
    HungarianResult transpose(){
        return new HungarianResult(cols, rows, unassigned);
    }
}