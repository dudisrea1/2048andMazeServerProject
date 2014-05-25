package Game2048;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Stack;

import model.GameBoard;
import model.GameModel;

public class Game2048Model extends Observable implements GameModel,
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6529685098267757690L;
	private int N = 4;
	private Board2048 board;
	public Stack<Board2048> undo;
	private boolean gameover = false;

	/**
	 * Function receives x,y (direction) to move the existing board in model
	 * Once finished shifting the board to the direction, checks if the board
	 * equal to the old board returns true if nothing change
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean movement(int x, int y) {
		Board2048 oldBoard = new Board2048(this.board);
		if (!gameover) {
			shiftBoard(x, y);
		}
		boolean res = gameover
				|| isBoardEquals(oldBoard.getBoard(), this.board.getBoard());
		return res;
	}

	/**
	 * Shifts the board based on given x,y 1,0 = Left -1,0 = Right 0,-1 = Down
	 * 0,1 = Up No handling for other values. Make sure to use the proper values
	 * as described above!
	 * 
	 * @param dx
	 *            Controls the X axis
	 * @param dy
	 *            Controls the Y axis
	 */
	private void shiftBoard(int dx, int dy) {
		if (dx + dy == 1)
			for (int x = 0; x < N; x++)
				board = (updateBoard(x * dy, x * dx, dx, dy));
		else
			for (int x = 0; x < N; x++)
				board = (updateBoard(3 * dy + N - 1 - dy * x, 3 * dx + N - 1
						- dx * x, dx, dy));
	}

	/**
	 * Update the current board and moves it according the parameters * 1,0 =
	 * Left -1,0 = Right 0,-1 = Down 0,1 = Up
	 * 
	 * @param x
	 *            - which column to move
	 * @param y
	 *            - which line to move
	 * @param dx
	 *            - which direction on X axis
	 * @param dy
	 *            - which direction on y axis
	 * @return a new board after movement
	 */
	public Board2048 updateBoard(int x, int y, int dx, int dy) {
		Board2048 newboard = new Board2048(new int[N][N], board.getScore(), N);
		newboard = moveBoard(board, x, y, dx, dy);
		return newboard;
	}

	/**
	 * moveBoard method used to move the board to specific direction based on dx
	 * and dy possibilities for dx and dy: * 1,0 = Left -1,0 = Right 0,-1 = Down
	 * 0,1 = Up
	 * 
	 * @param board
	 *            - The board you want to move
	 * @param x
	 *            - which column to move
	 * @param y
	 *            - which line to move
	 * @param dx
	 *            - which direction on X axis
	 * @param dy
	 *            - which direction on y axis
	 * @return a new board after movement
	 */
	private Board2048 moveBoard(Board2048 board, int x, int y, int dx, int dy) {
		int origx = x, origy = y;
		// Change the board values
		while ((0 <= x && x <= (N - 1)) && (0 <= y && y <= (N - 1))) {
			board.setBoard(elimiZero(board.getBoard(), origy, origx, dy, dx));
			if (y + dy >= 0 && y + dy <= (N - 1) && x + dx >= 0
					&& x + dx <= (N - 1)) {
				if (board.getBoard(x, y) == board.getBoard(x + dx, y + dy)) {
					board.setBoard(x, y, board.getBoard(x, y) * 2);
					board.setBoard(x + dx, y + dy, 0);
					board.setScore(board.getScore() + board.getBoard(x, y));
					board.setBoard(elimiZero(board.getBoard(), origy, origx,
							dy, dx));
				}
			}
			x += dx;
			y += dy;
		}
		return board;
	}

	/**
	 * Sqeueeze the values, returns a new board after removing 0 values after
	 * shifting a board
	 * 
	 * @param newboard
	 *            - which int[][] to work on
	 * @param y
	 *            - which line to work on
	 * @param x
	 *            - which column to work on
	 * @param dy
	 *            - which direction in the y axis
	 * @param dx
	 *            - which direction in the x axis
	 * @return - a new int[][] after the changes
	 */
	private int[][] elimiZero(int[][] newboard, int y, int x, int dy, int dx) {
		int a = y, b = x;
		while (a >= 0 && b >= 0 && a <= 3 && b <= 3) {
			if (newboard[a][b] == 0) {
				int k = a + dy, l = b + dx;
				while (k >= 0 && l >= 0 && k <= 3 && l <= 3
						&& newboard[a][b] == 0) {
					newboard[a][b] = newboard[k][l];
					newboard[k][l] = 0;
					k += dy;
					l += dx;
				}
			}
			a += dy;
			b += dx;
		}
		return newboard;
	}

	/**
	 * Compares 2 int[][]
	 * 
	 * @param A
	 *            - int array a
	 * @param B
	 *            - int array b
	 * @return - true if array a has same values as array b
	 */
	public boolean isBoardEquals(int[][] A, int[][] B) {
		for (int a = 0; a < N; a++)
			for (int b = 0; b < N; b++)
				if (A[a][b] != B[a][b])
					return false;
		return true;
	}

	@Override
	public Board2048 getBoard() {
		return this.board;
	}

	/**
	 * Returns true if the game is over, else returns false
	 */
	@Override
	public boolean CheckEndOfGame() {
		return gameover;
	}
	
	/**
	 * Returns the best move as string
	 * 
	 * @param bestMove
	 * @return
	 */
	public String ArrayToString(Integer[] bestMove) {
		if (bestMove[0] == 0 && bestMove[1] == 1)
			return "Click Up";
		if (bestMove[0] == 0 && bestMove[1] == -1)
			return "Click Down";
		if (bestMove[0] == 1 && bestMove[1] == 0)
			return "Click Left";
		if (bestMove[0] == -1 && bestMove[1] == 0)
			return "Click Right";
		return "No such move";
	}

	@Override
	public void setBoard(GameBoard newBoard) {
		if (newBoard instanceof Board2048)
			this.board = new Board2048((Board2048) newBoard);
	}

	@Override
	public ArrayList<Integer[]> getPossibleMoves() {
		ArrayList<Integer[]> possibleMoves = new ArrayList<Integer[]>();
		possibleMoves.add(new Integer[] { 0, -1 });
		possibleMoves.add(new Integer[] { 0, 1 });
		possibleMoves.add(new Integer[] { 1, 0 });
		possibleMoves.add(new Integer[] { -1, 0 });
		return possibleMoves;
	}
}