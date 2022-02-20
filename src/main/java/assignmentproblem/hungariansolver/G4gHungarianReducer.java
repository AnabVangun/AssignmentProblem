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
    private final G4gHungarianRowReducer rowReducer = new G4gHungarianRowReducer();
    private final G4gHungarianColumnReducer columnReducer = new G4gHungarianColumnReducer();
    private final Consumer<HungarianCostMatrix> reducer = rowReducer.andThen(columnReducer);
    @Override
    public void accept(HungarianCostMatrix matrix) {
        reducer.accept(matrix);
    }
}
