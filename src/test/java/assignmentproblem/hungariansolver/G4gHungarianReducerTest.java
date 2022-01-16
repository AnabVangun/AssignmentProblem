package assignmentproblem.hungariansolver;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import test.tools.TestFramework;

public class G4gHungarianReducerTest implements TestFramework<HungarianSolverTestCase>{
    static final int WORST_CASE_SIZE = 5;

    @TestFactory
    public Stream<DynamicNode> accept() {
        G4gHungarianReducer reducer = new G4gHungarianReducer();
        return test("accept", args -> {
            HungarianCostMatrix costMatrix = new HungarianCostMatrix(args.costMatrix);
            if (costMatrix.nCols == costMatrix.nRows){
                reducer.accept(costMatrix);
                Assertions.assertArrayEquals(args.horizontallyReducedCostMatrix,
                    costMatrix.costMatrix);
            } else {
                HungarianCostMatrix transposed = costMatrix.transpose();
                reducer.accept(costMatrix);
                reducer.accept(transposed);
                if (costMatrix.nCols > costMatrix.nRows){
                    Assertions.assertArrayEquals(args.horizontallyReducedCostMatrix,
                            costMatrix.costMatrix);
                    Assertions.assertArrayEquals(args.verticallyReducedCostMatrix,
                            transposed.costMatrix);
                } else {
                    Assertions.assertArrayEquals(args.horizontallyReducedCostMatrix,
                            transposed.costMatrix);
                    Assertions.assertArrayEquals(args.verticallyReducedCostMatrix,
                            costMatrix.costMatrix);
                }
            }
        });
    }

    @Override
    public Stream<HungarianSolverTestCase> argumentsSupplier() {
        return HungarianSolverTestCase.getStandardCases(WORST_CASE_SIZE);
    }
    
}
