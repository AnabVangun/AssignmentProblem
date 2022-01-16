package assignmentproblem.hungariansolver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
/**
 * Hungarian solver for reduced cost matrices.
 * 
 * Implementation based on Munkre's algorithm as described by the Bevilacqua Research Corporation
 * here: {@link https://brc2.com/the-algorithm-workshop/}.
 * The described algorithm works for horizontally rectangular matrices but this implementation is 
 * modified to rotate the matrix itself if necessary. The behaviour of the algorithm is undefined
 * if the input matrix has not been reduced before hand.
 */
final class Brc2HungarianSolver extends HungarianCoreSolver {
    private final Integer unassigned;
    private boolean[] coveredRows;
    private boolean[] coveredCols;
    private Integer[] starredRows;
    private Integer[] starredCols;
    private Integer[] primedRows;
    private Integer[] primedCols;
    private int numberCoveredCols;
    private HungarianCostMatrix costMatrix;
    
    /**
     * Initialise a solver.
     * @param unassigned value used to mark unassigned rows and columns in the results.
     */
    public Brc2HungarianSolver(Integer unassigned) {
        this.unassigned = unassigned;
    }
    private Integer[] initialiseIntArray(int size, Integer value){
        Integer[] result = new Integer[size];
        Arrays.fill(result, value);
        return result;
    }
    /**
     * Initialise the state of the solver for a given cost matrix.
     * Handle transposing the matrix if need be and initialise all internal variables.
     * @param costMatrix Cost matrix representing the problem to solve.
     */
    private void initialiseState(HungarianCostMatrix costMatrix){
        //Step 0 is the optional matrix transposition so that it has more columns than rows. It is
        //assumed to have been done in preprocessing.
        //step 1 is the matrix reduction. It is assumed to have been done in preprocessing.
        this.costMatrix = costMatrix;
        coveredRows = new boolean[this.costMatrix.nRows];
        starredRows = initialiseIntArray(this.costMatrix.nRows, unassigned);
        primedRows = initialiseIntArray(this.costMatrix.nRows, unassigned);
        coveredCols = new boolean[this.costMatrix.nCols];
        starredCols = initialiseIntArray(this.costMatrix.nCols, unassigned);
        primedCols = initialiseIntArray(this.costMatrix.nCols, unassigned);
        numberCoveredCols = 0;
        //Step 2 : initial zero starring
        //Perform the first occurrence of step 3 along the way: cover the columns.
        for (int i = 0; i < this.costMatrix.nRows; i++) {
            for (int j = 0; j < this.costMatrix.nCols; j++) {
                if (this.costMatrix.costMatrix[i][j] == 0 && starredCols[j] == unassigned) {
                    coveredCols[j] = true;
                    numberCoveredCols++;
                    starredRows[i] = j;
                    starredCols[j] = i;
                    break;
                }
            }
        }
    }
    /**
     * Perform step 4 of the algorithm.
     * Find a noncovered zero and prime it.  If there is no starred zero in the row containing this 
     * primed zero, Go to Step 5.  Otherwise, cover this row and uncover the column containing the 
     * starred zero. Continue in this manner until there are no uncovered zeros left. Save the 
     * smallest uncovered value and Go to Step 6.
     * @return either an array containing the coordinates of the selected primed zero to go to step
     * 5, or null if there is no uncovered zero left to go to step 6.
     */
    private int[] primeZeroes(){
        Queue<Integer> uncoveredColumnQueue = new LinkedList<>();
        int[] result;
        for (int i = 0; i < costMatrix.nRows; i++) {
            if (coveredRows[i]) {
                continue;
            }
            for (int j = 0; j < costMatrix.nCols; j++) {
                if (coveredCols[j] || costMatrix.costMatrix[i][j] > 0) {
                    continue;
                }
                //Found a non-covered zero
                result = handleNoncoveredZero(uncoveredColumnQueue, i, j);
                if (result != null){
                    return result;
                } else {
                    //row is not covered, go to next row
                    break;
                }
            }
        }
        while (!uncoveredColumnQueue.isEmpty()){
            int j = uncoveredColumnQueue.remove();
            for (int i = 0; i < costMatrix.nRows; i++){
                if(coveredRows[i] || costMatrix.costMatrix[i][j] > 0) {
                    continue;
                }
                //Found a non-covered zero
                result = handleNoncoveredZero(uncoveredColumnQueue, i, j);
                if (result != null){
                    return result;
                }
            }
        }
        return null;
    }
    /**
     * Handle a noncovered zero found in step 4.
     * @param uncoveredColumnQueue queue to track the uncovered columns to explore.
     * @param i row of the noncovered zero.
     * @param j column of the noncovered zero.
     * @return the position of the noncovered zero if it should be used in step 5, null otherwise.
     */
    private int[] handleNoncoveredZero(Queue<Integer> uncoveredColumnQueue, int i, int j){
        primedRows[i] = j;
        primedCols[j] = i;
        if (starredRows[i] == unassigned) {
            return new int[]{i,j};
        } else {
            coveredRows[i] = true;
            coveredCols[starredRows[i]] = false;
            numberCoveredCols -= 1;
            //ignore the rest of the row but handle the uncovered column
            uncoveredColumnQueue.add(starredRows[i]);
            return null;
        }
    }
    
    /**
     * Perform step 5 of the algorithm.
     * Construct a series of alternating primed and starred zeros as follows.  Let Z0 represent the 
     * uncovered primed zero found in Step 4. Let Z1 denote the starred zero in the column of Z0 (if
     * any). Let Z2 denote the primed zero in the row of Z1 (there will always be one).  Continue 
     * until the series terminates at a primed zero that has no starred zero in its column. Unstar 
     * each starred zero of the series, star each primed zero of the series, erase all primes and 
     * uncover every line in the matrix. Return to Step 3.
     * @param position (Row, column) position of the uncovered primed zero found in step 4.
     */
    private void invertPrimedAndStarred(int[] position){
        int currentRow = position[0];
        int currentCol = position[1];
        int tmp;
        starredRows[currentRow] = currentCol;
        while (starredCols[currentCol] != unassigned){
            //Move star to its new row in the column of the primed zero
            tmp = starredCols[currentCol];
            starredCols[currentCol] = currentRow;
            currentRow = tmp;
            //Move star to its new column in the column of the previously starred zero
            tmp = primedRows[currentRow];
            starredRows[currentRow] = tmp;
            currentCol = tmp;
        }
        //set starredCols of last changed zero and reset primes and lines covering
        starredCols[currentCol] = currentRow;
        for (int i = 0; i < coveredRows.length; i++){
            coveredRows[i] = false;
            primedRows[i] = unassigned;
        }
        //in next step, all columns containing a starred zero will be marked
        //--> do it right away
        for (int j = 0; j < costMatrix.nCols; j++){
            if(!coveredCols[j] && starredCols[j] != unassigned){
                numberCoveredCols++;
                coveredCols[j] = true;
            }
            //if a column contained a prime zero, it will still contain one
            //after the inversion, so the case where a column needs to be 
            //uncovered does not arise
            primedCols[j] = unassigned;
        }
    }
    
    /**
     * Perform step 6.
     * Add the value found in Step 4 to every element of each covered row, and subtract it from 
     * every element of each uncovered column. Return to Step 4 without altering any stars, primes, 
     * or covered lines.
     */
    private void alterMatrix(){
        //NB: value was not kept in step 4, we have to find it again.
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < costMatrix.nRows && min > 1; i++) {
            if (coveredRows[i]) {
                continue;
            }
            for (int j = 0; j < costMatrix.nCols; j++) {
                if (!coveredCols[j] && costMatrix.costMatrix[i][j] < min) {
                    min = costMatrix.costMatrix[i][j];
                    if (min == 1){
                        break;
                    }
                }
            }
        }
        //modify the matrix
        for (int i = 0; i < costMatrix.nRows; i++) {
            for (int j = 0; j < costMatrix.nCols; j++) {
                if (!coveredRows[i]) {
                    /* If the row is uncovered and the column is covered, 
                then it's a no-op: add and subtract the same value.
                     */
                    if (!coveredCols[j]) {
                        costMatrix.costMatrix[i][j] -= min;
                    }
                } else if (coveredCols[j]) {
                    costMatrix.costMatrix[i][j] += min;
                }
            }
        }
    }
    
    @Override
    public HungarianResult apply(HungarianCostMatrix inputMatrix) {
        initialiseState(inputMatrix);
        while (numberCoveredCols < costMatrix.nRows) {
            //This loop represents step 3
            //First, perform step 4
            int[] position = primeZeroes();
            while (position == null){
                //perform step 6 if a position could not be found
                alterMatrix();
                //Then, go back to step 4
                position = primeZeroes();
            }
            //When a satisfactory uncovered zero has been found, perform step 5
            invertPrimedAndStarred(position);
        }
        //format result
        HungarianResult result = new HungarianResult(starredRows, starredCols, unassigned);
        //protect result from accidental modification.
        starredRows = null;
        starredCols = null;
        return result;
    }

    @Override
    HandledMatrix getHandledMatrixType() {
        return HandledMatrix.HORIZONTAL;
    }
    
}

