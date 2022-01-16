package assignmentproblem;

/**
 * Wrapper to test {@link CostMatrix} implementations.
 */
public class CostMatrixArguments {
    
    public final String name;
    public final int[][] matrix;
    public final boolean isValid;

    CostMatrixArguments(int[][] matrix, String name, boolean isValid) {
        this.matrix = matrix;
        this.name = name;
        this.isValid = isValid;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}
