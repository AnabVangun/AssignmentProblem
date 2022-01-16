package assignmentproblem.hungariansolver;

import assignmentproblem.CostMatrix;
        
/**
 * Wrapper for matrix representation of assignment costs for Hungarian solvers.
 */
public class HungarianCostMatrix extends CostMatrix {
    final int[][] costMatrix;
    final int nRows;
    final int nCols;
    
    /**
     * Initialise a Hungarian cost matrix.
     * 
     * @param costMatrix The input matrix must be a valid input for a 
     *     {@link assignmentproblem.CostMatrix}.
     * @param deepCopy true if the input matrix must be deep-copied, false otherwise.
     */
    private HungarianCostMatrix(int[][] costMatrix, boolean deepCopy){
        super(costMatrix);
        if (deepCopy){
            this.costMatrix = new int[costMatrix.length][costMatrix[0].length];
            for (int i = 0; i < costMatrix.length; i++){
                System.arraycopy(costMatrix[i], 0, this.costMatrix[i], 0, costMatrix[0].length);
            }
        } else {
            this.costMatrix = costMatrix;
        }
        nRows = this.costMatrix.length;
        nCols = this.costMatrix[0].length;
    }
    /**
     * Initialise a Hungarian cost matrix.
     * 
     * @param costMatrix The input matrix must be a valid input for a 
     *     {@link assignmentproblem.CostMatrix}.
     */
    public HungarianCostMatrix(int[][] costMatrix){
        this(costMatrix, true);
    }
    
    /**
     * Create a transposed copy of the cost matrix. The two matrices are independent of one another.
     * Cell [i][j] of the first corresponds to cell [j][i] of the second.
     * @return A new cost matrix where the rows and columns have been switched.
     */
    HungarianCostMatrix transpose(){
        int[][] transposed = new int[nCols][nRows];
        for (int i = 0; i < nRows; i++){
            for (int j = 0; j < nCols; j++){
                transposed[j][i] = costMatrix[i][j];
            }
        }
        return new HungarianCostMatrix(transposed, false);
    }
}
