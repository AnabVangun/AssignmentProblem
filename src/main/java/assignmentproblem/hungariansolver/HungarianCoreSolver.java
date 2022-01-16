/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentproblem.hungariansolver;

import java.util.function.Function;

/**
 * Base abstract class for core solvers.
 *<p>Some core solvers may have specific constraints on their cost matrices: they may need them to 
 * be horizontal, vertical, or square. This class defines a contract to state those constraints.
 */
abstract class HungarianCoreSolver implements Function<HungarianCostMatrix, HungarianResult>{
    static enum HandledMatrix{
        HORIZONTAL,
        VERTICAL,
        SQUARE,
        ALL
    }
    /**
     * State the constraints imposed by the core solver on the cost matrix.
     * @return The type of cost matrix that the solver can handle: {@link HandledMatrix#HORIZONTAL}
     * (resp. {@link HandledMatrix#VERTICAL}) if it requires the matrix to have more columns than 
     * rows (resp. rows than columns), {@link HandledMatrix#SQUARE} if it requires the matrix to 
     * have exactly as many rows as columns, and {@link HandledMatrix#ALL} if there is no additional
     * requirement.
     */
    abstract HandledMatrix getHandledMatrixType();
}
