package assignmentproblem.hungariansolver;

import assignmentproblem.Result;
import java.util.AbstractMap;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import test.tools.TestFramework;

public class Brc2HungarianSolverTest implements TestFramework<HungarianSolverTestCase>{
    //TODO improvement axis: check that input unassigned value is properly used.
    static final int WORST_CASE_SIZE = 25;
    @Test
    public void constructor_nullInput(){
        new Brc2HungarianSolver(null);//only check that this does not raise an exception.
    }
    @Test
    public void constructor_negativeInput(){
        new Brc2HungarianSolver(-6);//only check that this does not raise an exception.
    }
    @TestFactory
    public Stream<DynamicNode> apply() {
        //return separate test nodes for row and column result check.
        return testContainer("apply", args -> {
            Result result = 
                new Brc2HungarianSolver(HungarianSolverTestCase.UNASSIGNED_VALUE)
                .apply(new HungarianCostMatrix(args.horizontalRowThenColReducedCostMatrix));
            /*
            The matrix must have more columns than rows: if it was transposed, transpose the 
            expected results as well.
            */
            Integer[] expectedRows;
            Integer[] expectedCols;
            if (args.rows.length > args.cols.length){
                expectedRows = args.cols;
                expectedCols = args.rows;
            } else {
                expectedRows = args.rows;
                expectedCols = args.cols;
            }
            return Stream.of(
                new AbstractMap.SimpleEntry<>(" rows", () -> {
                    Assertions.assertEquals(expectedRows.length, result.getRowAssignments().size(),
                            "Assignments have different sizes");
                    for (int i = 0; i < expectedRows.length; i++){
                        Assertions.assertEquals(expectedRows[i], result.getRowAssignments().get(i),
                            "Failed at index " + i);
                    }
                }),
                new AbstractMap.SimpleEntry<>(" columns", () -> {
                    Assertions.assertEquals(expectedCols.length, 
                        result.getColumnAssignments().size(),
                        "Assignments have different sizes");
                    for (int i = 0; i < expectedCols.length; i++){
                        Assertions.assertEquals(expectedCols[i], 
                            result.getColumnAssignments().get(i),
                            "Failed at index " + i);
                   }
               })
           );
        });
    }

    @Override
    public Stream<HungarianSolverTestCase> argumentsSupplier() {
        return HungarianSolverTestCase.getStandardCases(WORST_CASE_SIZE);
    }
    
}
