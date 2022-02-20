package assignmentproblem.hungariansolver;

import static assignmentproblem.hungariansolver.G4gHungarianReducerTest.reducerAcceptHelper;

import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import test.tools.TestFramework;

public class G4gHungarianRowReducerTest implements TestFramework<HungarianSolverTestCase>{
    static final int WORST_CASE_SIZE = 5;

    @TestFactory
    public Stream<DynamicNode> accept() {
        return reducerAcceptHelper(this, new G4gHungarianRowReducer(), args -> {
            return new int[][][]{args.horizontalRowReducedCostMatrix, 
                args.verticalRowReducedCostMatrix};
        });
    }
    
    @Override
    public Stream<HungarianSolverTestCase> argumentsSupplier() {
        return HungarianSolverTestCase.getStandardCases(WORST_CASE_SIZE);
    }
}
