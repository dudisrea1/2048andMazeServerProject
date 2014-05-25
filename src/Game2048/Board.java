package Game2048;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import model.GameBoard;


public class Board implements GameBoard,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[][] board;
	private int score, N, winSize = 2048, emptycells = -1;
	private boolean won;

	/**
	 * Constructor to create a new board
	 * 
	 * @param board
	 *            - matrix that represents the board
	 * @param score
	 *            - represents the board score
	 * @param N
	 *            - the size of the board
	 */
	public Board(int[][] board, int score, int N) {
		this.N = N;
		this.score = score;
		this.board = new int[N][N];
		setBoard(board);
	}

	/**
	 * Copy constructor
	 * 
	 * @param newboard
	 *            - the board to copy
	 */
	public Board(Board newboard) {
		this.N = newboard.getN();
		this.score = newboard.getScore();
		this.board = new int[N][N];
		setBoard(newboard.getBoard());
	}

	/**
	 * get the board matrix
	 * 
	 * @return the board matrix
	 */
	public int[][] getBoard() {
		return board;
	}

	/**
	 * get the value of the matrix at board[y][x]
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getBoard(int x, int y) {
		return board[y][x];
	}

	/**
	 * sets the value of board[y][x] to be val
	 * 
	 * @param x
	 * @param y
	 * @param val
	 */
	public void setBoard(int x, int y, int val) {
		board[y][x] = val;
	}

	/**
	 * replaces the board with the current board sets a win flag if it's
	 * true(found a tile with the win size) or false;
	 * 
	 * @param board
	 */
	public void setBoard(int[][] board) {
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				this.board[i][j] = board[i][j];
				won = (this.board[i][j] == winSize);
			}
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * changes the score according to a given parameter
	 * 
	 * @param score
	 *            the new score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return the size of the board
	 */
	public int getN() {
		return N;
	}

	/**
	 * changes the board size
	 * 
	 * @param n
	 */
	public void setN(int n) {
		N = n;
	}

	/**
	 * check if the user has won the game
	 * 
	 * @return true if he did, false if he didn't
	 */
	public boolean hasWon() {
		return won;
	}

	/**
	 * check if the user has won the game, giving the criteria for winning as
	 * param
	 * 
	 * @param destination
	 *            - what's the tile that the user need to achieve in order to
	 *            win the game
	 * @return true if user won, false if he didn't
	 */
	public boolean hasWon(int destination) {
		winSize = destination;
		setBoard(this.board);
		return won;
	}

	/**
	 * calculate number of empty cells, store the score in data member name
	 * emptycells
	 */
	public void calcNumberOfEmptyCells() {
		int count = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				if (this.board[i][j] == 0)
					++count;
			}
		emptycells = count;
	}

	/**
	 * Check if emptycells never initialized, if that's the case calculating the
	 * number of empty cells and then return the emptcells data member
	 * 
	 * @return amount of empty cells in board matrix
	 */
	public int getNumberOfEmptyCells() {
		if (emptycells == -1)
			calcNumberOfEmptyCells();
		return emptycells;
	}

	/**
	 * Returns a list of integers that holds the amount of empty cell Ids - used
	 * to calculate the next move
	 * 
	 * @return List<Integer>
	 */
	public List<Integer> getEmptyCellIds() {
		emptycells = 0;
		List<Integer> emptyCellsList = new ArrayList<Integer>();
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				if (this.board[i][j] == 0) {
					++emptycells;
					emptyCellsList.add(i * N + j);
				}
			}
		return emptyCellsList;
	}

	/**
	 * Changes an empty cell value with a given value parameter
	 * changes board[i][j] value
	 * @param i 
	 * @param j
	 * @param value
	 */
	public void setEmptyCell(int i, int j, int value) {
		if (board[i][j] == 0) {
			board[i][j] = value;
			emptycells = -1;
		}
	}

	/**
	 * Converts the board object to int from String hashCode func
	 * @return
	 */
	public int getBoardHash() {
		String str = "";
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				str += board[i][j];
			}
		return str.hashCode();
	}
}
