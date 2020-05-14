import java.lang.reflect.Array;
import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");

	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) throws RuntimeException{
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {

		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);

		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}


	private int[][] grid;
	ArrayList<spot> ls;
	private static final Integer[] possibleVals = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};


	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		grid = new int[ints.length][ints[0].length];
		for (int i = 0; i < ints.length; i++) {
			System.arraycopy(ints[i], 0, grid[i],0, ints[i].length);
		}
		ls = new ArrayList<>(50);
		getDenseSpots();
	}

	private void getDenseSpots() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if(grid[i][j] == 0){
					ls.add(new spot(i, j));
				}
			}
		}
		Collections.sort(ls);
	}

	public class spot implements Comparable<spot> {
		private int row, col;
		private int density;
		private HashSet<Integer> hs;

		public spot(int row, int col){
			this.row = row;
			this.col = col;
			hs = new HashSet<>(/*Arrays.asList(possibleVals)*/);
			setDensity();
		}

		public void set(int val){
			grid[row][col] = val;
		}

	//	@Override
	//	public String toString(){ return String.valueOf(density); }

	//	public int getDensity(){
	//		return density;
	//	}
	//	public final HashSet<Integer> possVals(){
	//		return hs;
	//	}

		public void clear(){
			grid[row][col] = 0;
		}

		public void setDensity(){
			hs.addAll(Arrays.asList(possibleVals));
			for (int i = 0; i < SIZE; i++) {
				hs.remove(grid[row][i]);
				hs.remove(grid[i][col]);
			}

			int x = (row/3) * 3;
			int y = (col/3) * 3;
			for (int i = 0; i < PART; i++) {
				for (int j = 0; j < PART; j++) {
					hs.remove(grid[x + i][y + j]);
				}
			}
			density = hs.size();
		}

		@Override
		public int compareTo(spot o) {
			return this.density - o.density;
		}
	}

	/**
	 * Containers for solving the puzzle.
	 */

	private class SolStruct{
		public int val;
		public String firstSol;
		public long solTime;
		public SolStruct(){
			val = 0;
			firstSol = "";
			solTime = 0;
		}
	}

	private SolStruct numSols;

	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		numSols = new SolStruct();

		long start = System.currentTimeMillis();
		solveHelper(0);
		long end = System.currentTimeMillis();

		numSols.solTime = end - start;
		return numSols.val;
	}

	private void solveHelper(int filled) {
		if(filled >= ls.size()){
			if(++numSols.val == 1) {
				numSols.firstSol = toString();
			}
			return;
		}

		spot curr = ls.get(filled);
		curr.setDensity();
		for(int i : curr.hs){
			curr.set(i);

			solveHelper(filled + 1);

			curr.clear();

			if(numSols.val >= 100) return;
		}
	}

	public String getSolutionText() {
		return numSols.firstSol;
	}
	
	public long getElapsed() {
		return numSols.solTime;
	}

	@Override
	public String toString() {
		String gridStr = "";
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				gridStr += grid[i][j] + " ";
			}
			gridStr += "\n";
		}
		return gridStr;
	}
}
