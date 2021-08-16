# AssignmentProblem

A Java implementation of a solver for the assignment problem : https://en.wikipedia.org/wiki/Assignment_problem.
The only available solver at the time is based on the Hungarian algorithm as described in https://brc2.com/the-algorithm-workshop/.
An improvement of the preprocessing described in https://www.geeksforgeeks.org/hungarian-algorithm-assignment-problem-set-1-introduction/ is included.

This solver can handle rectangular, non-square matrices: extra rows or columns will not be assigned.

## Example
### Input 
|     |  0 |  1 |  2 |  3 |
|-----|----|----|----|----|
|**0**|  1 |  2 | 25 | 13 |
|**1**|  5 |  7 | 25 | 15 |
|**2**| 10 | 13 | 16 | 14 |
|**3**| 17 | 21 | 11 | 18 |
|**4**| 15 | 15 | 15 | 14 |

### Output
|     |  0 |  1 |  2 |  3 |
|-----|----|----|----|----|
|**0**|    |  X |    |    |
|**1**|  X |    |    |    |
|**2**|    |    |    |  X |
|**3**|    |    |  X |    |
|**4**|    |    |    |    |

Coût total : 2 + 5 + 14 + 11 = 32
## Usage
```
int[][] costMatrix = new int[][]{
	{1,2,25,13},
	{5,7,25,15},
	{10,13,16,14},
	{17,21,11,18},
	{15,15,15,14}}
};  

Solver solver = Solver.solve(costMatrix);
int[][] assignments = solver.getAssignments();
```
The results can be returned in three ways: as row assignments, column assignments, and coordinates, using the getter methods defined in the Solver interface.