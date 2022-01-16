package assignmentproblem.hungariansolver;

import assignmentproblem.Result;
import assignmentproblem.Solver;
import java.util.function.Consumer;

/**
 * Implementation of the {@link Solver} interface based on the Hungarian algorithm. The
 * Hungarian algorithm is described here : 
 * {@link https://en.wikipedia.org/wiki/Hungarian_algorithm}.
 */
public class HungarianSolver implements Solver<HungarianCostMatrix>{
    final Consumer<HungarianCostMatrix> reducer;
    final HungarianCoreSolver solver;
    
    HungarianSolver(Consumer<HungarianCostMatrix> reducer, HungarianCoreSolver solver){
        this.reducer = reducer;
        this.solver = solver;
    }
    /**
     * Initialise a HungarianSolver ready to solve assignment problems.
     * @param unassigned Default value for unassigned rows and columns.
     */
    public HungarianSolver(Integer unassigned){
        this(new G4gHungarianReducer(), new Brc2HungarianSolver(unassigned));
    }

    @Override
    public Result solve(HungarianCostMatrix costMatrix) {
        if (costMatrix == null){
            //TODO log this properly
            throw new NullPointerException("tried to solve a null matrix");
        }
        HungarianCostMatrix toSolve;
        boolean transpose = false;
        switch(solver.getHandledMatrixType()){
            case HORIZONTAL:
                //if matrix is vertical, flip it
                if (costMatrix.nRows > costMatrix.nCols){
                    toSolve = costMatrix.transpose();
                    transpose = true;
                } else {
                    toSolve = costMatrix;
                }
                break;
            case VERTICAL:
                //if matrix is horizontal, flip it
                if (costMatrix.nCols > costMatrix.nRows){
                    toSolve = costMatrix.transpose();
                    transpose = true;
                } else {
                    toSolve = costMatrix;
                }
                break;
            case SQUARE:
                if (costMatrix.nCols != costMatrix.nRows){
                    //TODO log this properly
                    throw new IllegalStateException("Tried to solve a non-square matrix with a"
                        + "square-only solver");
                }
                //break
            case ALL:
                toSolve = costMatrix;
                break;
            default:
                throw new UnsupportedOperationException("Not implemented yet");
        }
        reducer.accept(toSolve);
        HungarianResult result = solver.apply(toSolve);
        return transpose ? result.transpose() : result;
    }
    @Override
    public Result solve(int[][] costMatrix) {
        return this.solve(new HungarianCostMatrix(costMatrix));
    }
    
}
