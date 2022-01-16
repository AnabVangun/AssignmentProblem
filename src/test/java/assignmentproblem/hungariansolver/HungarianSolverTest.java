package assignmentproblem.hungariansolver;

import assignmentproblem.Result;
import java.util.AbstractMap;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import test.tools.TestFramework;

/**
 *
 * @author tobin
 */
public class HungarianSolverTest implements TestFramework<HungarianSolverTestCase>{

    @Override
    public Stream<HungarianSolverTestCase> argumentsSupplier() {
        return HungarianSolverTestCase.getStandardCases(50);
    }
    
    @TestFactory
    Stream<DynamicNode> solveIntArray(){
        return checkSolved(args -> new HungarianSolver(HungarianSolverTestCase.UNASSIGNED_VALUE)
            .solve(args.costMatrix),
            "solve on int[][] input");
    }
    
    @TestFactory
    Stream<DynamicNode> solveCostMatrix(){
        return checkSolved(args -> new HungarianSolver(HungarianSolverTestCase.UNASSIGNED_VALUE)
            .solve(new HungarianCostMatrix(args.costMatrix)),
            "solve on HungarianCostMatrix input");
    }
    
    Stream<DynamicNode> checkSolved(Function<HungarianSolverTestCase, Result> solver, String name){
        //return separate test nodes for row and column result check.
        return testContainer(name, args -> {
            Result result = solver.apply(args);
            return Stream.of(
                new AbstractMap.SimpleEntry<>(" rows", () -> {
                    Assertions.assertEquals(args.rows.length, result.getRowAssignments().size(),
                            "Assignments have different sizes");
                    for (int i = 0; i < args.rows.length; i++){
                        Assertions.assertEquals(args.rows[i], result.getRowAssignments().get(i),
                            "Failed at index " + i);
                    }
                }),
                new AbstractMap.SimpleEntry<>(" columns", () -> {
                    Assertions.assertEquals(args.cols.length, 
                        result.getColumnAssignments().size(),
                        "Assignments have different sizes");
                    for (int i = 0; i < args.cols.length; i++){
                        Assertions.assertEquals(args.cols[i], 
                            result.getColumnAssignments().get(i),
                            "Failed at index " + i);
                   }
               })
           );
        });
    }
    
}
