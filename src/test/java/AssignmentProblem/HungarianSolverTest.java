package AssignmentProblem;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import test.tools.TestArguments;
import test.tools.TestFrameWork;

/**
 *
 * @author AnabVangun
 */
public class HungarianSolverTest implements TestFrameWork<HungarianSolver, HungarianArgument> {

    @TestFactory
    public Stream<DynamicTest> testInitialiseValidInput() {
        //Check that initialise does not crash on valid input.
        //Correctness of the result is checked in tests linked to the methods getting the results.
        return test("initialise (valid input)", v -> v.convert());
    }
    
    @TestFactory
    public Stream<DynamicTest> testInitialiseInvalidInput(){
        Stream<HungarianArgument> cases = Stream.of(
                new HungarianArgument(null, null, null, null, "null cost matrix"),
                new HungarianArgument(new int[0][0], null, null, null, "size 0 cost matrix"),
                new HungarianArgument(new int[][]{{0}, {0,1}, {0,1,2},{0,1},{0}}, null, null, null, "non-rectangular cost matrix"));
        return test(cases, 
                "initialise (invalid input)", 
                v -> Assertions.assertThrows(IllegalArgumentException.class, 
                        () -> v.convert()));
    }

    @TestFactory
    public Stream<DynamicTest> testGetRowAssignments() {
        return test("getRowAssignments", v -> {
           Solver solver = v.convert();
           assertArrayEquals(v.expectedRowAssignment, solver.getRowAssignments());
        });
    }

    @TestFactory
    public Stream<DynamicTest> testGetColumnAssignemnts() {
        return test("getColumnAssignments", v -> {
            Solver solver = v.convert();
            assertArrayEquals(v.expectedColAssignment, solver.getColumnAssignemnts());
        });
    }

    @TestFactory
    public Stream<DynamicTest> testGetAssignments() {
        Comparator<int[]> comparator = (first, second) ->
            Integer.compare(first[0], second[0]) == 0 ? Integer.compare(first[1], second[1]) : Integer.compare(first[0], second[0]);
        return test("getAssignments", v-> {
            /*
            There is no contract on the ordering of the result values.
            */
            int[][] assignments = v.convert().getAssignments();
            Arrays.sort(assignments, comparator);
            Arrays.sort(v.expectedMatrixResult, comparator);
            assertArrayEquals(v.expectedMatrixResult, assignments);
        });
    }

    @TestFactory
    public Stream<DynamicTest> testReduceInitialMatrix() {
        Stream<HungarianArgument> cases = Stream.of(
                new HungarianArgument(new int[][]{{25, 40, 35}, {40, 60, 35}, {20, 40, 25}}, 
                        new int[][]{{0, 0, 10}, {5, 10, 0}, {0, 5, 5}}, 
                        null, null, "square 3*3 matrix"),
                new HungarianArgument(new int[][]{{150, 400, 450},{200, 600, 350}, {200, 400, 250}},
                        new int[][]{{0, 50, 250}, {0, 200, 100}, {0, 0, 0}},
                        null, null, "second square 3*3 matrix"),
                new HungarianArgument(new int[][]{{70, 40, 20, 55},{65, 60, 45, 90},{30, 45, 50, 75},{25,0,55,40}},
                        new int[][]{{50, 20, 0, 0},{20, 15, 0, 10},{0, 15, 20, 10},{25, 0, 55, 5}},
                        null, null, "square 4*4 with initial zeroes matrix"),
                new HungarianArgument(new int[][]{{1,2,25,13},{5,7,25,15},{10,13,16,13},{17,21,11,18},{15,15,15,14}},
                        new int[][]{{0,2,9,16,13},{0,3,11,19,12},{14,12,5,0,3},{0,0,0,5,0}}, 
                        null, null, "5*4 matrix without initial zeroes")
        );
        return test(cases, 
                "reduceInitialMatrix", 
                v -> {
                    HungarianSolver solver = v.convertWithConstructor();
                    solver.reduceInitialMatrix();
                    assertArrayEquals(v.expectedMatrixResult, solver.getState());
                });
    }

    @Override
    public Stream<HungarianArgument> argumentsSupplier() {
        int worstCaseSize = 200;
        int[][] worstCaseMatrix = new int[worstCaseSize][worstCaseSize];
        for (int i = 0; i < worstCaseMatrix.length; i++) {
            for (int j = 0; j < worstCaseMatrix[i].length; j++){
                worstCaseMatrix[i][j] = (i+1)*(j+1);
            }
        }
        int[] worstCaseLinearExpectation = new int[worstCaseSize];
        Arrays.setAll(worstCaseLinearExpectation, i -> worstCaseSize-i-1);
        int[][] worstCaseExpectedAssignments = new int[worstCaseSize][2];
        for (int i = 0; i < worstCaseSize; i++){
            worstCaseExpectedAssignments[i][0] = i;
            worstCaseExpectedAssignments[i][1] = worstCaseSize-i-1;
        }
        return Stream.of(new HungarianArgument(new int[][]{{2500, 4000, 3500}, {4000, 6000, 3500}, {2000, 4000, 2500}},
                new int[][]{{0,1},{1,2},{2,0}}, new int[]{1,2,0}, new int[]{2,0,1}, "simple 3*3 matrix"),
                new HungarianArgument(new int[][]{{2000,6000,3500},{1500, 4000, 4500},{2000,4000,2500}},
                new int[][]{{0,0},{1,1},{2,2}}, new int[]{0,1,2}, new int[]{0,1,2}, "mildly complex 3*3 matrix"),
                new HungarianArgument(new int[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}},
                new int[][]{{0,0},{1,1},{2,2}}, new int[]{0,1,2}, new int[]{0,1,2,-1}, "complex 4*3 matrix with equality case"),
                new HungarianArgument(new int[][]{{1,2,25,13},{5,7,25,15},{10,13,16,13},{17,21,11,18},{15,15,15,14}},
                new int[][]{{0,1},{1,0},{2,3},{3,2}}, new int[]{1,0,3,2,-1}, new int[]{1,0,3,2}, "first complex 5*4 matrix without equality case"),
                new HungarianArgument(new int[][]{{1,2,25,13},{5,7,25,15},{10,13,16,14},{17,21,11,18},{15,15,15,13}},
                new int[][]{{0,1},{1,0},{2,3},{3,2}}, new int[]{1,0,3,2,-1}, new int[]{1,0,3,2}, "second complex 5*4 matrix without equality case"),
                new HungarianArgument(worstCaseMatrix, worstCaseExpectedAssignments,
                worstCaseLinearExpectation, worstCaseLinearExpectation, "worst case " + worstCaseSize + "*" + worstCaseSize + " matrix")
        );
    }
    
}

class HungarianArgument implements TestArguments<HungarianSolver>{
    final int[][] costMatrix;
    final int[][] expectedMatrixResult;
    final int[] expectedRowAssignment;
    final int[] expectedColAssignment;
    private final String name;
    HungarianArgument(int[][] costMatrix, int[][] expectedMatrixResult, 
            int[] expectedRowAssignment, int[] expectedColAssignment,
            String name){
        this.costMatrix = costMatrix;
        this.expectedMatrixResult = expectedMatrixResult;
        this.expectedRowAssignment = expectedRowAssignment;
        this.expectedColAssignment = expectedColAssignment;
        this.name = name;
    }
    @Override
    public HungarianSolver convert() {
        return HungarianSolver.initialise(costMatrix);
    }
    public HungarianSolver convertWithConstructor(){
        return new HungarianSolver(costMatrix);
    }
    
    @Override
    public String toString(){
        return this.name;
    }
}