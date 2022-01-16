package assignmentproblem;

/**
 * Wrapper for matrix representation of assignment costs.
 * Cell [i][j] of the matrix represents the cost of assigning row i to column j. A cost matrix is 
 * valid if and only if the following three requirements are met. 
 * <ol>
 *  <li>The matrix MUST NOT be null.</li>
 *  <li>The matrix MUST be either square or rectangular (i.e. all rows must have the same 
 *      size) ; the extra rows or columns will not be assigned by the {@link Solver}.</li>
 *  <li>The matrix MUST have at least one row and at least one column.</li>
 *  <li>All values MUST be greater than or equal to zero.</li>
 * </ol>
 * 
 * <p>A {@code CostMatrix} instance MAY be modified in-place by a {@code Solver}.
 */
public abstract class CostMatrix {
    /**
     * Initialises a valid cost matrix.
     * Checks if all the requirements for a valid <code>CostMatrix</code> are met; if not, fail with
     * an exception. This constructor SHOULD be overridden by any implementing class.
     * 
     * @param costMatrix matrix to convert into a <code>CostMatrix</code> object.
     * @throws IllegalArgumentException if any of the requirements for a valid cost matrix are not 
     *      met.
     */
    public CostMatrix(int[][] costMatrix){
        String errorMessage = null;
        if (costMatrix == null){
            errorMessage = "null cost matrix";
        } else if (costMatrix.length == 0){
            errorMessage = "cost matrix with 0 rows";
        }
        else if (costMatrix[0] != null && costMatrix[0].length == 0){
            errorMessage = "cost matrix with 0 cols in its first row";
        } else {
            for (int i = 0; i < costMatrix.length; i++){
                if (costMatrix[i] == null){
                    errorMessage = "cost matrix where row " + i + " is null";
                    break;
                } else if (costMatrix[i].length != costMatrix[0].length){
                    errorMessage = "non-rectangular cost matrix, expected length " 
                            + costMatrix[0].length + " but row " + i + " has length "
                            + costMatrix[i].length;
                    break;
                }
                for (int j = 0; j < costMatrix[i].length; j++){
                    if (costMatrix[i][j] < 0){
                        errorMessage = "cost matrix where cell [" + i + "][" + j 
                            + "] has negative value: " + costMatrix[i][j];
                        break;
                    }
                }
                if (errorMessage != null){
                    break;
                }
            }
        }
        if (errorMessage != null){
            //TODO log this properly
            throw new IllegalArgumentException("Tried to initialise a " + errorMessage);
        }
    }
}
