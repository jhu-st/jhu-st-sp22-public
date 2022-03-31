import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 *
 * Given an unsolved Sudoku board, this program should be able to find a
 * valid solution by correctly filling the empty cells on the board.
 *
 * This class seems to be a buggy Sudoku solver. We realized this by running the test
 * case provided in SudokuSolverTest.java (which currently results in a failure).
 * You need to find the faults and then debug the methods below by utilizing
 * "debugging by observation":
 *
 * first read this source file completely all the way to the end to develop an understanding of how
 * things work and what each method is supposed to accomplish. In particular, read all
 * the javadoc comments carefully.
 *
 *  1) Use log4j 2 to create and write to a logfile in the current folder named SudokuSolver.log that
 *    contains the following 'debug' logs:
 *    a) the content of the initial board
 *    b) the result of getEmptyXY(board) call in solve()
 *    c) in each recursive iteration of trySol, log the content of the board as well as the
 *       result of "getRemainNums(board, cur);" call.
 *    To make the logs more readable make use of some sort of separators, e.g., use "-----------" to
 *    separate the log messages. Also, each log message should have a timestamp (date and time) as well as
 *    what level of logging it is (should be debug for all the log messages in this case). Make sure
 *    the logger you instantiate has a correct "logging level" to pass along the debug logs.
 *  2) Inspect the content of SudokuSolver.log carefully. This should provide pointers and hints
 *     where (and in what way) things might be going wrong. Try to fix as many issues as you find leveraging the
 *     content of the logfile.
 *  3) Next, if needed, run an interactive debugger and try to find all the remaining issues. Develop hypotheses (remember
 *     a hypothesis is a tentative explanation on failure's cause) on possible faults before running the debugger. Then verify each
 *     hypothesis by setting appropriate breakpoints/watchpoints at reasonable locations in the code and
 *     stepping through the code line by line.
 *  4) Finally, fix all the issues you have found and save your work into a new class named
 *     SudokuSolverFixed.java. Leave detailed comments at the exact locations of the code where
 *     you found faults and explain in details what exactly each fault was and how you fixed it.
 *     Make sure you remove/disable the log messages in SudokuSolverFixed.java.
 *
 *  5) Deliverables: 1) SudokuSolver.java which contains log4j 2 logging code, 2) a short report in the form of
 *     comments at the top of SudokuSolver.java that explains how you used the log messages and/or a debugger
 *     of your choice and what hypotheses you developed, what breakpoints/watchpoints you used etc. Write
 *     at least a few paragraphs and make detailed explanations. Try to use a debugger in a meaningful way
 *     to find the faults. Even if you spot a bug before running the debugger, still set appropriate
 *     breakpoints/watchpints and run the debugger to verify your hypothesis. If you do so, it should
 *     feel natural and straightforward to write this short report. 3) SudokuSolverFixed.java containing all fixes
 *     to all the faults found as well as detailed and clear comments for each fault at the exact corresponding
 *     location in the code.
 *
 * Once you find a bug, fix it by addressing the root cause. Once you fix all the faults, your
 * program should pass the sample test case given in SolveSudokuTest.java
 *
 * */
public class SudokuSolver {

    private final static int size = 9;

    class Cell {
        public int x, y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    private boolean complete;
    char[][] board;

    public SudokuSolver(char[][] board) {
        this.board = board;
        complete = false; // is solving the puzzle completed?
    }

    /**
     * the high-level idea to solve a given sudoku puzzle is as follows:
     * 1) first, locate all empty cells on the board.
     * 2) for each empty cell, find all "possible" correct values.
     * 3) in a depth-first manner, pick a "possible" correct value and continue the search with other cells:
     *   3-a) if the algorithm doesn't find any solution using the chosen value for the current cell, then
     *        continue the search by trying other possibilities
     *   3-b) if the algorithm finds a solution using the chosen value, then return
     *
     * */
    public void solve() {
        Vector<Cell> empties = getEmptyXY(board);
        trySol(empties, board);
    }

    /** This recursive method conducts a depth-first search to find a solution to the given sudoko "board".
     *  Given all empty cells that need to be filled on the board:
     *  if there are no empty cells, then return
     *  otherwise, pick an empty cell:
     * 	find all the possible valid numbers for that empty cell
     *  pick one of the possible valid numbers for the cell and continue the search
     *  based on that value:
     *     if the search fails, try the other possibilities.
     *     if the search succeeds, return.
     *  @param empties all empty cells on the board
     *  @param board the board
     *
     * */
    public void trySol(Vector<Cell> empties, char[][] board) {
        if (empties.size() == 0) {
            complete = true;
            return;
        }

        Cell cur = empties.firstElement();
        empties.remove(0);

        Vector<Character> nums = getRemainNums(board, cur);
        for (char n : nums) {
            board[cur.x][cur.y] = n;
            trySol(empties, board);
            board[cur.x][cur.y] = '.';
        }
    }

    /**
     * This method finds and returns all empty cells on the board.
     * @param board the board
     * */
    public Vector<Cell> getEmptyXY(char[][] board) {
        Vector<Cell> emptiesPoints = new Vector<SudokuSolver.Cell>();

        for (int i = size - 1; i > 0; i--) {
            for (int j = size - 1; j > 0; j--) {
                if (board[i][j] == '.') {
                    emptiesPoints.add(new Cell(i, j));
                }
            }
        }
        return emptiesPoints;
    }

    /**
     * This method finds and returns possible valid numbers for cell 'cur'.
     * For this, it checks the row, the column and the 3 x 3 sub-grid.
     * For example, let's assume cur's row index number is 1 and cur's column index number is 2.
     * This means 'cur' is located in the top-left 3 x 3 sub-grid. Also, let's
     * assume currently numbers 2 and 6 appear in the row index number 1 and numbers 4 and 7 appear in
     * column index number 2. Also, we have 2, 8, and 9 already appearing in the top-left 3 x 3 sub-grid.
     * This means that a correct value for 'cur' can only be from {1, 3, 5} set.
     *
     * @param board the board
     * @param cur the current empty cell
     * */
    public Vector<Character> getRemainNums(char[][] board, Cell cur) {

        Character[] ns = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Set<Character> nums = new HashSet<Character>();
        for (char c : ns) {
            nums.add(c);
        }

        for (int i = 0; i < size; i++) {
            if (nums.contains(board[i][cur.y])) nums.remove(board[i][cur.y]);
        }
        for (int j = 0; j < size; j++) {
            if (nums.contains(board[cur.x][j])) nums.remove(board[cur.x][j]);
        }
        // sub-grid index
        Integer sx = cur.x / 3;
        Integer sy = cur.y / 3;
        for (int i = sx * 3; i < sx * 4; i++) {
            for (int j = sy * 3; j < sy * 4; j++) {
                if (nums.contains(board[i][j])) {
                    nums.remove(board[i][j]);
                }
            }
        }
        return new Vector<Character>(nums);
    }

}
