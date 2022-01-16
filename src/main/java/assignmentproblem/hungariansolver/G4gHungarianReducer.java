package assignmentproblem.hungariansolver;

import java.util.function.Consumer;

/**
 * Matrix reducer to preprocess cost matrices for a Hungarian solver.
 * 
 * Implementation based on GeeksforGeeks explanations found here:
 * {@link https://www.geeksforgeeks.org/hungarian-algorithm-assignment-problem-set-1-introduction/}.
 * This reducer works in two steps: first, it reduces the value of all rows, then it reduces the 
 * value of all columns.
 */
final class G4gHungarianReducer implements Consumer<HungarianCostMatrix> {

    @Override
    public void accept(HungarianCostMatrix matrix) {
        //Compute the min value of each column while reducing the rows
        //First pass: reduce rows
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
       //Second pass: reduce cols
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
