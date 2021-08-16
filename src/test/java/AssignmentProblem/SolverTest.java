package AssignmentProblem;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;
import test.tools.TestArguments;
import test.tools.TestFrameWork;

/**
 * @author AnabVangun
 */
public class SolverTest implements TestFrameWork<int[][], SolverArguments> {

    @Override
    public Stream<SolverArguments> argumentsSupplier() {
        return Stream.of(
                new SolverArguments(new int[][]{
            {70, 40, 20, 55},
            {65, 60, 45, 90},
            {30, 45, 50, 75},
            {25, 30, 55, 40}
        }, new int[][]{
            {70, 40, 20, 55},
            {65, 60, 45, 90},
            {30, 45, 50, 75},
            {25, 30, 55, 40}
        }, "square matrix"),
                new SolverArguments(new int[][]{
            {70, 40, 20, 55},
            {65, 60, 45, 90},
            {30, 45, 50, 75}
        }, new int[][]{
            {70, 40, 20, 55},
            {65, 60, 45, 90},
            {30, 45, 50, 75},
            {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}
        }, "horizontal matrix"),
                new SolverArguments(new int[][]{
            {70, 40, 20},
            {65, 60, 45},
            {30, 45, 50},
            {25, 30, 55}
        }, new int[][]{
            {70, 40, 20, -12},
            {65, 60, 45, -12},
            {30, 45, 50, -12},
            {25, 30, 55, -12}
        }, "vertical matrix"));
    }

    @TestFactory
    public Stream<DynamicTest> testSquarifyMatrix() {
        return test("squarifyMatrix", v -> Assertions.assertArrayEquals(v.expectedResult,
                        Solver.squarifyMatrix(v.convert(), v.defaultValue))
        );
    }
    
    @TestFactory
    public Stream<DynamicNode> testSolve() {
        //Check only that calling solve does not crash on any solver.
        return testContainer("solve", v
                -> Arrays.stream(Solver.SolverType.values()).map(type
                        -> new AbstractMap.SimpleEntry<String, Executable>(" with " + type.toString() + " solver", 
                                () -> Solver.solve(v.convert(), type)))
        );
    }
}

class SolverArguments implements TestArguments<int[][]> {

    final int[][] costMatrix;
    final int[][] expectedResult;
    final int defaultValue;
    final String name;

    public SolverArguments(int[][] costMatrix, int[][] expectedResult, String name) {
        this.costMatrix = costMatrix;
        this.expectedResult = expectedResult;
        int lastRowIdx = expectedResult.length - 1;
        this.defaultValue = expectedResult[lastRowIdx][expectedResult[lastRowIdx].length-1];
        this.name = name;
    }

    @Override
    public int[][] convert() {
        return this.costMatrix;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
}
