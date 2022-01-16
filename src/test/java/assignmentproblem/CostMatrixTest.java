package assignmentproblem;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import test.tools.TestFramework;

public class CostMatrixTest implements TestFramework<CostMatrixArguments>{
    
    @Override
    public Stream<CostMatrixArguments> argumentsSupplier(){
        return Stream.of(
            new CostMatrixArguments(new int[][]{{1,2},{3,4}}, 
                "valid square matrix with values strictly higher than 0", true),
            new CostMatrixArguments(new int[][]{{0,1,2},{0,3,4}},
                "valid square matrix with at least one 0", true),
            new CostMatrixArguments(new int[][]{{532126}},
                "valid square matrix of size 1", true),
            new CostMatrixArguments(new int[][]{{6,15,5126},{12873,0,127}},
                "valid horizontally rectangular matrix", true),
            new CostMatrixArguments(new int[][]{{2,5},{112,732},{2,5323},{1,1},{842,0}}, 
                "valid vertically rectangular matrix", true),
            new CostMatrixArguments(null, 
                "invalid null matrix", false),
            new CostMatrixArguments(new int[][]{{1,2},{3,4,5},{6,7}}, 
                "invalid non-rectangular matrix", false),
            new CostMatrixArguments(new int[][]{},
                "invalid matrix with 0 row", false),
            new CostMatrixArguments(new int[6][0], 
                "invalid matrix with 0 column", false),
            new CostMatrixArguments(new int[][]{{1,2,3},{4,5,-6},{7,8,9}}, 
                "invalid square matrix with a negative value", false),
            new CostMatrixArguments(new int[][]{{1,2,3,-4},{5,-6,7,8}},
                "invalid rectangular matrix with multiple negative values", false),
            new CostMatrixArguments(new int[][]{{-1,-2,-3},{-4,-5},{-6}},
                "invalid diagonal matrix with only negative values", false)
        );
    }
    
    @TestFactory
    public Stream<DynamicNode> constructor(){
        return test("CostMatrix", args -> {
            if (args.isValid){
                new SimpleCostMatrix(args.matrix);//simply expect not to fail
            } else {
                Assertions.assertThrows(IllegalArgumentException.class, 
                    () -> new SimpleCostMatrix(args.matrix));
            }
        });
    }
}


/** Dummy implementation of cost matrix used to check the implementation of the constructor. */
class SimpleCostMatrix extends CostMatrix{
    public SimpleCostMatrix(int[][] costMatrix) {
        super(costMatrix);
    }
}