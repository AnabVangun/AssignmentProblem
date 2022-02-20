package assignmentproblem.hungariansolver;

import java.util.function.Consumer;

/**
 * Matrix reducer to preprocess columns of cost matrices for a Hungarian solver.
 *
 * Implementation based on GeeksforGeeks explanations found here:
 * {@link https://www.geeksforgeeks.org/hungarian-algorithm-assignment-problem-set-1-introduction/}.
 * This reducer only performs the second step: it reduces the value of all columns. If the first
 * step is necessary, either a {@link G4gHungarianRowReducer} should be used before, or a
 * {@link G4gHungarianReducer} should be used instead.
 */
final class G4gHungarianColumnReducer implements Consumer<HungarianCostMatrix> {

    @Override
    public void accept(HungarianCostMatrix matrix) {
       for (int j = 0; j < matrix.nCols; j++){
           int colMin = Integer.MAX_VALUE;
           for (int i = 0; i < matrix.nRows; i++){
               if (matrix.costMatrix[i][j] < colMin){
                   colMin = matrix.costMatrix[i][j];
               }
           }
           for (int i = 0; i < matrix.nRows; i++){
               matrix.costMatrix[i][j] -= colMin;
           }
       }
    }
}
