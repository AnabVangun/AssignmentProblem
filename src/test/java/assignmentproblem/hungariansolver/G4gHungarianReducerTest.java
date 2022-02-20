package assignmentproblem.hungariansolver;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import test.tools.TestFramework;

public class G4gHungarianReducerTest implements TestFramework<HungarianSolverTestCase>{
    static final int WORST_CASE_SIZE = 5;

    @TestFactory
    public Stream<DynamicNode> accept() {
        return reducerAcceptHelper(this, new G4gHungarianReducer(), args -> {
            return new int[][][]{args.horizontalRowThenColReducedCostMatrix, 
                args.verticalRowThenColReducedCostMatrix};
        });
    }
    /**
     * Produces all the tests necessary to verify that a reducer performs adequately.
     * @param tester Object used to perform the test.
     * @param reducer Actual reducer to test.
     * @param expectedProducer Function that returns the expected result of the reducer for a given
     *     {@link HungarianSolverTestCase} argument. If the cost matrix is square, the output SHOULD
     *     be an array containing only the expected reduced cost matrix. If the cost matrix is 
     *     rectangular, the output SHOULD be an array containing first the expected horizontal 
     *     reduced cost matrix, and then its vertical counterpart.
     * @return a stream of tests checking comparing the actual reduced cost matrix (and its 
     *     transposed version if it is rectangular) with the expected one.
     */
    public static Stream<DynamicNode> reducerAcceptHelper(
            TestFramework<HungarianSolverTestCase> tester,
            Consumer<HungarianCostMatrix> reducer,
            Function<HungarianSolverTestCase, int[][][]> expectedProducer){
        return tester.test("accept", args -> {
           HungarianCostMatrix costMatrix = new HungarianCostMatrix(args.costMatrix);
           int[][][] expected = expectedProducer.apply(args);
           if (costMatrix.nCols == costMatrix.nRows){
               reducer.accept(costMatrix);
               Assertions.assertArrayEquals(expected[0], costMatrix.costMatrix);
           } else {
               HungarianCostMatrix transposed = costMatrix.transpose();
               reducer.accept(costMatrix);
               reducer.accept(transposed);
               if (costMatrix.nCols > costMatrix.nRows){
                   Assertions.assertArrayEquals(expected[0], costMatrix.costMatrix, "horizontal");
                   Assertions.assertArrayEquals(expected[1], transposed.costMatrix, "vertical");
               } else {
                   Assertions.assertArrayEquals(expected[0], transposed.costMatrix, "horizontal");
                   Assertions.assertArrayEquals(expected[1], costMatrix.costMatrix, "vertical");
                   
               }
           }
        });
    }

    @Override
    public Stream<HungarianSolverTestCase> argumentsSupplier() {
        return HungarianSolverTestCase.getStandardCases(WORST_CASE_SIZE);
    }
    
}
