import loggingdemo.SudokuSolver;
import loggingdemo.SudokuSolverCorrect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SudokuSolverTest {

    boolean checkEqual(char[][] b1, char[][] b2){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (b1[i][j] != b2[i][j]) {
                    return false;
                }
            }

        }
        return true;
    }

    @Test
    void testSudokuSolver1() {
        char[][] board = {{'5','.','.','.','7','.','.','.','2'},
                {'6','.','.','1','9','5','.','.','.'},
                {'.','9','8','.','.','.','.','6','.'},
                {'8','.','.','.','6','.','.','.','3'},
                {'4','.','.','8','.','3','.','.','1'},
                {'7','.','.','.','2','.','.','.','6'},
                {'.','6','.','.','.','.','2','8','.'},
                {'.','.','.','4','1','9','.','.','5'},
                {'.','.','.','.','8','.','.','7','9'}};

        char[][] expected = {{'5','3','4','6','7','8','9','1','2'},
                {'6','7','2','1','9','5','3','4','8'},
                {'1','9','8','3','4','2','5','6','7'},
                {'8','5','9','7','6','1','4','2','3'},
                {'4','2','6','8','5','3','7','9','1'},
                {'7','1','3','9','2','4','8','5','6'},
                {'9','6','1','5','3','7','2','8','4'},
                {'2','8','7','4','1','9','6','3','5'},
                {'3','4','5','2','8','6','1','7','9'}};
        SudokuSolver solver = new SudokuSolver(board);
        solver.solve();
        // this assert should pass once you fix the bugs.
        assertTrue(checkEqual(board, expected));

    }


}
