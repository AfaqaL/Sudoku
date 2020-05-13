package hw3;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTest {

    Sudoku sudoku;
    int[][] gride;
    int[][] gridm;
    int[][] gridh;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        gride = Sudoku.stringsToGrid(
                "1 6 4 0 0 0 0 0 2",
                "2 0 0 4 0 3 9 1 0",
                "0 0 5 0 8 0 4 0 7",
                "0 9 0 0 0 6 5 0 0",
                "5 0 0 1 0 2 0 0 8",
                "0 0 8 9 0 0 0 3 0",
                "8 0 9 0 4 0 2 0 0",
                "0 7 3 5 0 9 0 0 1",
                "4 0 0 0 0 0 6 7 9");

        gridm = Sudoku.stringsToGrid(
                "530070000",
                "600195000",
                "098000060",
                "800060003",
                "400803001",
                "700020006",
                "060000280",
                "000419005",
                "000080079");

        gridh = Sudoku.stringsToGrid(
                "3 7 0 0 0 0 0 8 0",
                "0 0 1 0 9 3 0 0 0",
                "0 4 0 7 8 0 0 0 3",
                "0 9 3 8 0 0 0 1 2",
                "0 0 0 0 4 0 0 0 0",
                "5 2 0 0 0 6 7 9 0",
                "6 0 0 0 2 1 0 4 0",
                "0 0 0 5 3 0 9 0 0",
                "0 3 0 0 0 0 0 5 1");
        sudoku = new Sudoku(gridm);
    }


    @org.junit.jupiter.api.Test
    void textToGrid() {
        String txtPuzzle = "5300700006001950000980000608000600034008" +
                "03001700020006060000280000419005000080079";
        int[][] txtm = Sudoku.textToGrid(txtPuzzle);
        Arrays.deepEquals(gridm,txtm);
    }

    @org.junit.jupiter.api.Test
    void main() {
        Sudoku.main(null);
    }

    @org.junit.jupiter.api.Test
    void solve() {
        int res = sudoku.solve();
        assertEquals(1, res);
        assertEquals(sudoku.getSolutionText(), sudoku.getSolutionText());
        assertEquals(sudoku.getElapsed(), sudoku.getElapsed());
    }


    @org.junit.jupiter.api.Test
    void testToString() {
        assertEquals(sudoku.toString(), sudoku.toString());
    }

    @Test
    void testZeroSudoku(){
        int[][] gridz = Sudoku.stringsToGrid(
                "0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0",
                "0 0 0 0 0 0 0 0 0"
        );
        sudoku = new Sudoku(gridz);
        sudoku.solve();
    }

    @Test
    void testAssertion(){
        Exception exc = assertThrows(RuntimeException.class, () -> Sudoku.textToGrid("123125123123"));
        assertTrue(exc.getMessage().contains("Needed 81 numbers, but got:"));
    }
}