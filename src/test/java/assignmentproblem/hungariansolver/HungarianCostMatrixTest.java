package assignmentproblem.hungariansolver;

import assignmentproblem.CostMatrixTest;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
/**
 *
 * @author tobin
 */
public class HungarianCostMatrixTest extends CostMatrixTest {

    @Override
    @TestFactory
    public Stream<DynamicNode> constructor(){
        return test("HungarianCostMatrix", args -> {
            if (args.isValid){
                new HungarianCostMatrix(args.matrix);//simply expect not to fail
            } else {
                Assertions.assertThrows(IllegalArgumentException.class, 
                    () -> new HungarianCostMatrix(args.matrix));
            }
        });
    }
    
    @TestFactory
    Stream<DynamicNode> constructor_correctCopy(){
        return test(this.argumentsSupplier().filter(args -> args.isValid),
            "HungarianCostMatrix (correctly copy input)", args -> {
                //Deep copy args.matrix to make sure that constructor makes a valid copy of it
                int[][] deepcopy = new int[args.matrix.length][args.matrix[0].length];
                for (int i = 0; i < args.matrix.length; i++){
                    for (int j = 0 ; j < args.matrix[i].length; j++){
                        deepcopy[i][j] = args.matrix[i][j];
                    }
                }
                HungarianCostMatrix matrix = new HungarianCostMatrix(args.matrix);
                //check that all cells have valid value
                Assertions.assertArrayEquals(deepcopy, matrix.costMatrix);
        });
    }
    
    @TestFactory
    Stream<DynamicNode> constructor_deepCopy(){
        return test(this.argumentsSupplier().filter(args -> args.isValid),
            "HungarianCostMatrix (correctly copy input)", args -> {
                HungarianCostMatrix matrix = new HungarianCostMatrix(args.matrix);
                for (int i = 0; i < args.matrix.length ; i++){
                    for (int j = 0; j < args.matrix[i].length ; j++){
                        matrix.costMatrix[i][j] += 1;
                    }
                }
                //arrays are expected not to be equal
                try {
                    Assertions.assertArrayEquals(args.matrix, matrix.costMatrix);
                } catch (AssertionError e){
                    return;
                }
                Assertions.fail("Initial cost matrix and modified HungarianCostMatrix are "
                    + "identical.");
        });
    }
    
    @TestFactory
    Stream<DynamicNode> transpose(){
        return test(this.argumentsSupplier().filter(args -> args.isValid),
            "transpose", args -> {
                HungarianCostMatrix matrix = new HungarianCostMatrix(args.matrix).transpose();
                for (int i = 0; i < args.matrix.length; i++){
                    for (int j = 0; j < args.matrix[i].length; j++){
                        Assertions.assertEquals(args.matrix[i][j], matrix.costMatrix[j][i],
                            "Cell [" + j + "][" + i + "] = " + matrix.costMatrix[j][i] 
                            + "of the transposed matrix does not match cell [" + i + "][" + j 
                            + "] = " + args.matrix[i][j] + " of the first matrix.");
                    }
                }
            });
    }
}
