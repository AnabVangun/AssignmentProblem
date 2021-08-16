package AssignmentProblem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementation of the HungarianAlgorithm.
 * 
 * This implementation is based mostly on 
 * https://brc2.com/the-algorithm-workshop/ with an improvement of the cost 
 * matrix preprocessing described on 
 * https://www.geeksforgeeks.org/hungarian-algorithm-assignment-problem-set-1-introduction/
 *
 * @author AnabVangun
 */
class HungarianSolver extends Solver {

    final private int[][] costMatrix;
    final private int[][] assignments;
    final private int[] rowAssignments;
    final private int[] colAssignments;
    final private int nInitialRows;
    final private int nInitialCols;
    final private boolean[] coveredRows;
    final private boolean[] coveredCols;
    final private int[] starredRows;
    final private int[] starredCols;
    final private int[] primedRows;
    final private int[] primedCols;
    private int numberCoveredCols;

    /**
     * Instantiates a new solver on the given cost matrix. The input matrix is
     * squared but the problem has not yet been solved. The proper way to get
     * the solution of the assignment problem with a {@link HungarianSolver} is
     * to call {@link #initialise(int[][])}.
     *
     * @param costMatrix
     */
    HungarianSolver(int[][] costMatrix) {
        this.costMatrix = Solver.squarifyMatrix(costMatrix, Solver.DEFAULT_VALUE);
        nInitialRows = costMatrix.length;
        nInitialCols = costMatrix[0].length;
        assignments = new int[nInitialRows < nInitialCols ? nInitialRows : nInitialCols][2];
        rowAssignments = new int[nInitialRows];
        colAssignments = new int[nInitialCols];
        coveredRows = new boolean[this.costMatrix.length];
        coveredCols = new boolean[this.costMatrix.length];
        starredRows = new int[this.costMatrix.length];
        starredCols = new int[this.costMatrix.length];
        primedRows = new int[this.costMatrix.length];
        primedCols = new int[this.costMatrix.length];
        Arrays.fill(starredRows, -1);
        Arrays.fill(starredCols, -1);
        Arrays.fill(primedRows, -1);
        Arrays.fill(primedCols, -1);
        Arrays.fill(rowAssignments, -1);
        Arrays.fill(colAssignments, -1);
        for (int[] assignment : assignments) {
            Arrays.fill(assignment, -1);
        }
    }

    protected static HungarianSolver initialise(int[][] costMatrix) {
        HungarianSolver result = new HungarianSolver(costMatrix);
        result.reduceInitialMatrix();
        result.solveReducedMatrix();
        return result;
    }

    @Override
    public int[] getRowAssignments() {
        return this.rowAssignments;
    }

    @Override
    public int[] getColumnAssignemnts() {
        return this.colAssignments;
    }

    @Override
    public int[][] getAssignments() {
        return this.assignments;
    }

    /**
     * Reduces the values of the square matrix to make zeroes appear. This
     * corresponds to the first step of the Hungarian Algorithm.
     */
    void reduceInitialMatrix() {
        //first part: reduce all rows
        for (int[] row : costMatrix) {
            int min = row[0];
            for (int val : row) {
                if (val < min) {
                    min = val;
                }
            }
            for (int j = 0; j < row.length; j++) {
                row[j] -= min;
            }
        }
        //second part: reduce all columns
        for (int j = 0; j < costMatrix[0].length; j++) {
            int min = costMatrix[0][j];
            for (int[] row : costMatrix) {
                if (row[j] < min) {
                    min = row[j];
                }
            }
            for (int[] row : costMatrix) {
                row[j] -= min;
            }
        }
    }

    /**
     * Performs the main loop of the Hungarian algorithm.
     */
    private void solveReducedMatrix() {
        //Steps 0 and 1 have been preprocessed
        //Step 2 : initial zero starring
        for (int i = 0; i < coveredRows.length; i++) {
            for (int j = 0; j < coveredCols.length; j++) {
                if (costMatrix[i][j] == 0 && starredCols[j] == -1) {
                    coveredCols[j] = true;
                    numberCoveredCols++;
                    starredRows[i] = j;
                    starredCols[j] = i;
                    break;
                }
            }
        }
        while (numberCoveredCols < costMatrix.length) {
            int[] position = primeZero();
            while (position == null){
                //Perform step 6
                //Get minimal unmarked value
                int min = Integer.MAX_VALUE;
                for (int i = 0; i < costMatrix.length; i++) {
                    if (coveredRows[i]) {
                        continue;
                    }
                    for (int j = 0; j < costMatrix.length; j++) {
                        if (coveredCols[j]) {
                            continue;
                        }
                        if (costMatrix[i][j] < min) {
                            min = costMatrix[i][j];
                            if (min == 1){
                                break;
                            }
                        }
                        if (min == 1){
                            break;
                        }
                    }
                }
                //modify the matrix
                for (int i = 0; i < costMatrix.length; i++) {
                    for (int j = 0; j < costMatrix.length; j++) {
                        if (!coveredRows[i]) {
                            /* If the row is uncovered and the column is covered, 
                        then it's a no-op: add and subtract the same value.
                             */
                            if (!coveredCols[j]) {
                                costMatrix[i][j] -= min;
                            }
                        } else if (coveredCols[j]) {
                            costMatrix[i][j] += min;
                        }
                    }
                }
                //go back to step 4
                position = primeZero();
            }
            //perform step 5
            invertPrimedAndStarred(position);
        }
        //format the result
        int assignmentIndex = 0;
        for (int i = 0; i < nInitialRows; i++){
            if (starredRows[i] < nInitialCols){
                rowAssignments[i] = starredRows[i];
                assignments[assignmentIndex][0] = i;
                assignments[assignmentIndex][1] = starredRows[i];
                assignmentIndex++;
            }
            else{
                rowAssignments[i] = -1;
            }
        }
        for (int j = 0; j < nInitialCols; j++){
            colAssignments[j] = starredCols[j] < nInitialRows ? starredCols[j] : -1;
        }
    }

    /**
     * Primes uncovered zeroes in the cost matrix.
     * Performs the fourth step of the Hungarian Algorithm.
     * @return the (rowIndex,colIndex) coordinates of the primed zero to star 
     * that has been found, or null if no such zero has been found.
     */
    private int[] primeZero() {
        Queue<Integer> uncoveredColumnQueue = new LinkedList<>();
        for (int i = 0; i < coveredRows.length; i++) {
            if (coveredRows[i]) {
                continue;
            }
            for (int j = 0; j < coveredCols.length; j++) {
                if (coveredCols[j] || costMatrix[i][j] > 0) {
                    continue;
                }
                //Found a non-covered zero
                primedRows[i] = j;
                primedCols[j] = i;
                if (starredRows[i] == -1) {
                    return new int[]{i,j};
                } else {
                    coveredRows[i] = true;
                    coveredCols[starredRows[i]] = false;
                    numberCoveredCols -= 1;
                    //ignore the rest of the row but handle the uncovered column
                    uncoveredColumnQueue.add(starredRows[i]);
                    break;
                }
            }
        }
        while (!uncoveredColumnQueue.isEmpty()){
            //TODO improve: create a function with the handling of non-covered zeroes
            int j = uncoveredColumnQueue.remove();
            for (int i = 0; i < coveredRows.length; i++){
                if(coveredRows[i] || costMatrix[i][j] > 0) {
                    continue;
                }
                primedRows[i] = j;
                primedCols[j] = i;
                if (starredRows[i] == -1){
                    return new int[]{i,j};
                } else {
                    coveredRows[i] = true;
                    coveredCols[starredRows[i]] = false;
                    numberCoveredCols -= 1;
                    uncoveredColumnQueue.add(starredRows[i]);
                }
            }
        }
        return null;
    }
    
    /**
     * Stars selected primed zeroes to increase the line coverage of the matrix.
     * Performs the fifth step of the Hungarian Algorithm.
     * @param position array of size 2 containing the row and column indices of 
     * the first primed zero in the alternating series to modify.
     */
    private void invertPrimedAndStarred(int[] position){
        int currentRow = position[0];
        int currentCol = position[1];
        int tmp;
        starredRows[currentRow] = currentCol;
        while (starredCols[currentCol] != -1){
            //Move star to its new row in the column of the primed zero
            tmp = starredCols[currentCol];
            starredCols[currentCol] = currentRow;
            currentRow = tmp;
            //Move star to its new column in the column of the previously 
            //starred zero
            tmp = primedRows[currentRow];
            starredRows[currentRow] = tmp;
            currentCol = tmp;
        }
        //set starredCols of last changed zero and reset primes and lines covering
        starredCols[currentCol] = currentRow;
        for (int i = 0; i < coveredRows.length; i++){
            coveredRows[i] = false;
            primedRows[i] = -1;
        }
        //in next step, all columns containing a starred zero will be marked
        //--> do it right away
        for (int j = 0; j < primedCols.length; j++){
            if(!coveredCols[j] && starredCols[j] != -1){
                numberCoveredCols++;
                coveredCols[j] = true;
            }
            //if a column contained a prime zero, it will still contain one
            //after the inversion, so the case where a column needs to be 
            //uncovered does not arise
            primedCols[j] = -1;
        }
    }

    /**
     * @return The internal state of the cost matrix.
     */
    int[][] getState() {
        return this.costMatrix;
    }
}
