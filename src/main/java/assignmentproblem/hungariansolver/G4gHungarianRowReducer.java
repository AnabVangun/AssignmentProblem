package assignmentproblem.hungariansolver;

import java.util.function.Consumer;

/**
 * Matrix reducer to preprocess rows of cost matrices for a Hungarian solver.
 * 
 * Implementation based on GeeksforGeeks explanations found here:
 * {@link https://www.geeksforgeeks.org/hungarian-algorithm-assignment-problem-set-1-introduction/}.
 * This reducer only performs the first step: it reduces the value of all rows.
 */
final class G4gHungarianRowReducer implements Consumer<HungarianCostMatrix> {
    @Override
    public void accept(HungarianCostMatrix matrix) {
        for (int i = 0; i < matrix.nRows; i++){
            int rowMin = Integer.MAX_VALUE;
            for (int j = 0; j < matrix.nCols; j++){
                if (matrix.costMatrix[i][j] < rowMin){
                    rowMin = matrix.costMatrix[i][j];
                }
            }
            for (int j = 0; j < matrix.nCols ; j++){
                matrix.costMatrix[i][j] -= rowMin;
            }
        }
    }
}