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
	// private int[][] board;
	// private int score = 0, bestscore = 0;
	private int N = 4;
	private Board2048 board;
	private int bestscore = 0;
	public Stack<Board2048> undo;
	private String difficulty = "Normal";
	private boolean gameover = false;

	// /**
	// * The default constructor of the model will create a 4X4 board
	// */
	// public Game2048Model() {
	// undo = new Stack<Board2048>();
	// board = new Board2048(new int[N][N], 0, N);
	// }

	// /**
	// * The constructor will receive the parameter N
	// *
	// * @param N
	// * used to set the board size I.e Given N=10 will create board
	// * size 10x10
	// */
	// public Game2048Model(int N) {
	// this.N = N;
	// undo = new Stack<Board2048>();
	// board = new Board2048(new int[N][N], 0, N);
	// }

	// /**
	// * The constructor receives an existing board
	// *
	// * @param board
	// * used to create a new model insance based on existing board
	// */
	// public Game2048Model(Board2048 board) {
	// undo = new Stack<Board2048>();
	// this.N = board.getN();
	// this.board = board;
	// }
	//
	// /**
	// * Copy constructor creates new isntance identical to the given model.
	// *
	// * @param newmodel
	// * the model that you'd like to copy.
	// */
	// public Game2048Model(Game2048Model newmodel) {
	// this.board = newmodel.board;
	// this.bestscore = newmodel.bestscore;
	// this.undo = newmodel.undo;
	// this.gameover = newmodel.gameover;
	// }

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

	// /**
	// * Movement function will move the board according to X,Y received, and
	// also
	// * spawn a new tile
	// *
	// * @param dx
	// * what to do on X
	// * @param dy
	// * what to do on Y
	// * @return boolean that represent whether the board was changed (true) or
	// * the move didn't change it (false)
	// */
	// public boolean movement(int dx, int dy) {
	// if (!gameover) {
	// saveData();
	// saveData();
	// shiftBoard(dx, dy);
	// if (!isBoardEquals(undo.pop().getBoard(), board.getBoard())) {
	// if (difficulty.equals("Hard"))
	// Spawn();
	// Spawn();
	// setBestScore(bestscore);
	// GameOver();
	// setChanged();
	// notifyObservers();
	// return true;
	// } else
	// undo.pop();
	// }
	// return false;
	// }

	// /**
	// * Moves the board up
	// */
	// @Override
	// public void moveUp() {
	// movement(0, 1);
	// }
	//
	// /**
	// * Moves the board down
	// */
	// @Override
	// public void moveDown() {
	// movement(0, -1);
	// }
	//
	// /**
	// * Moves the board left
	// */
	// @Override
	// public void moveLeft() {
	// movement(1, 0);
	// }
	//
	// /**
	// * Moves the board right
	// */
	// @Override
	// public void moveRight() {
	// movement(-1, 0);
	// }

	// /**
	// * checks if the undo stack is empty, if it's not - pop the board and
	// notify
	// * the observer for changes
	// */
	// public void undoMove() {
	// if (!undo.isEmpty()) {
	// Board2048 ob = new Board2048(new int[N][N], 0, N);
	// ob = undo.pop();
	// this.board.setBoard(ob.getBoard());
	// this.board.setScore(ob.getScore());
	// gameover = false;
	// setChanged();
	// notifyObservers();
	// }
	// }

	// /**
	// * push the current board into the stack for using undo moves in the
	// future
	// */
	// private void saveData() {
	// undo.push(new Board2048(board.getBoard(), board.getScore(), board
	// .getN()));
	// }

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

	// /**
	// * The function will add a random value to an empty cell 90% chance for
	// * adding the value 2 10% chance for adding the value 4
	// */
	// private void Spawn() {
	// ArrayList<Integer[]> zeroCells = new ArrayList<Integer[]>();
	// for (int a = 0; a < N; a++)
	// for (int b = 0; b < N; b++)
	// if (board.getBoard(a, b) == 0) {
	// zeroCells.add(new Integer[] { a, b });
	// }
	// if (zeroCells.size() >= 1) {
	// Integer[] putnewnumber = zeroCells.get(new Random()
	// .nextInt(zeroCells.size()));
	// if (new Random().nextFloat() * (1 - 0) > 0.1) {
	// board.setBoard(putnewnumber[0], putnewnumber[1], 2);
	// } else
	// board.setBoard(putnewnumber[0], putnewnumber[1], 4);
	// }
	// }

	// /**
	// * Function to get the Board data member from the model
	// *
	// * @return existing board
	// */
	// public Board2048 getBoards() {
	// return board;
	// }

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

	/**
	 * Checks if the game is over Game over is defined as - No more moves
	 */
	private void GameOver() {
		boolean res = false;
		int curscore = board.getScore();
		// Create 4 boards
		int[][] boardDown, boardUp, boardLeft, boardRight;
		// Initialize 4 boards
		boardDown = new int[4][4];
		boardUp = new int[4][4];
		boardLeft = new int[4][4];
		boardRight = new int[4][4];
		// Copy current board
		for (int a = 0; a < 4; a++)
			for (int b = 0; b < 4; b++) {
				boardDown[a][b] = board.getBoard(b, a);
				boardUp[a][b] = board.getBoard(b, a);
				boardLeft[a][b] = board.getBoard(b, a);
				boardRight[a][b] = board.getBoard(b, a);
			}
		// Move current board once to each direction
		for (int a = 0; a < 4; a++) {
			boardUp = moveBoard(
					new Board2048(boardUp, board.getScore(), board.getN()), a,
					0, 0, 1).getBoard();
			boardDown = moveBoard(
					new Board2048(boardDown, board.getScore(), board.getN()),
					a, 3, 0, -1).getBoard();
			boardLeft = moveBoard(
					new Board2048(boardLeft, board.getScore(), board.getN()),
					0, a, 1, 0).getBoard();
			boardRight = moveBoard(
					new Board2048(boardRight, board.getScore(), board.getN()),
					3, a, -1, 0).getBoard();
		}
		// Check if they are all the same boards then game is over
		if (isBoardEquals(board.getBoard(), boardUp)
				&& isBoardEquals(board.getBoard(), boardDown)
				&& isBoardEquals(board.getBoard(), boardLeft)
				&& isBoardEquals(board.getBoard(), boardRight))
			res = true;
		board.setScore(curscore);
		gameover = res;
	}

	// /**
	// * Returns the board matrix
	// */
	// @Override
	// public int[][] getBoard() {
	// return board.getBoard();
	// }

	@Override
	public Board2048 getBoard() {
		return this.board;
	}

	// /**
	// * Returns the board score
	// */
	// @Override
	// public int getScore() {
	// return board.getScore();
	// }

	// @Override
	// public void moveUp_Right() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void moveUp_Left() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void moveDown_Right() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void moveDown_Left() {
	// // TODO Auto-generated method stub
	//
	// }

	// /**
	// * Changes the currently used board
	// *
	// * @param board
	// * - the board that the model is going to work on
	// */
	// public void setBoard(Board2048 board) {
	// this.board = board;
	// }

	// /**
	// * Sets the current board array
	// *
	// * @param board
	// * - int[][] to represent the new board
	// */
	// @Override
	// public void setBoard(int[][] board) {
	// // this.board = board;
	// this.board.setBoard(board);
	// }

	// /**
	// * Sets the board score
	// *
	// * @param score
	// * - the new score
	// */
	// @Override
	// public void setScore(int score) {
	// // this.score = score;
	// this.board.setScore(score);
	// }
	//
	// /**
	// * Gets the best score known by the model
	// *
	// * @return int
	// */
	// @Override
	// public int getBestScore() {
	// return bestscore;
	// }
	//
	// /**
	// * Based on given newscore as parameter checks if the score is higher than
	// * the current best score, if so updates it
	// *
	// * @param newscore
	// * - the new score to update
	// */
	// @Override
	// public void setBestScore(int newscore) {
	// if (this.board.getScore() > this.bestscore)
	// this.bestscore = this.board.getScore();
	// }

	// /**
	// * Returns a stack contains the moves done by the player
	// */
	// @Override
	// public Stack<Board2048> getMoves() {
	// return undo;
	// }
	//
	// /**
	// * Replaces the current stack holds the moves done by the player
	// *
	// * @param moves
	// * - new stack with the old moves
	// */
	// @Override
	// public void setMoves(Stack moves) {
	// undo = moves;
	// }

	/**
	 * Returns true if the game is over, else returns false
	 */
	@Override
	public boolean CheckEndOfGame() {
		// GameOver();
		return gameover;
	}

	// /**
	// * Initialize the current board: Sets the board to be a new game, clears
	// the
	// * old moves done by the player, clears the board, set score to 0 and adds
	// 2
	// * cells in random location
	// */
	// @Override
	// public void InitBoard() {
	// gameover = false;
	// undo.removeAllElements();
	// board.setBoard(new int[N][N]);
	// int x = new Random().nextInt(4), y = new Random().nextInt(4);
	// int mx = x, my = y;
	// board.setBoard(x, y, 2);
	// int add = 2;
	// if (new Random().nextFloat() * (1 - 0) <= 0.1)
	// add = 4;
	// while (mx == x)
	// mx = new Random().nextInt(4);
	// while (my == y)
	// my = new Random().nextInt(4);
	// board.setBoard(mx, my, add);
	// board.setScore(0);
	// setChanged();
	// notifyObservers();
	// }

	/**
	 * Saves the current model to XML file.
	 * 
	 * @param path
	 *            - the location of targeted file
	 */
	// @Override
	// public boolean Save(String path) {
	// return Game2048ModelXMLManager.ToFile(new Game2048Model(this), path);
	// }
	//
	// /**
	// * Loads a model based on a given path
	// *
	// * @param path
	// * - represents the path of the file we need to load
	// * @return true if the operation succeed
	// */
	// @Override
	// public boolean Load(String path) {
	// Game2048Model LoadedModel = (Game2048Model) Game2048ModelXMLManager
	// .FromFile(path);
	// if (LoadedModel == null)
	// return false;
	// this.setBoard(LoadedModel.getBoard());
	// this.setScore(LoadedModel.getScore());
	// this.setBestScore(LoadedModel.getBestScore());
	// this.setMoves(LoadedModel.getMoves());
	// this.gameover = LoadedModel.CheckEndOfGame();
	// return true;
	// }

	// /**
	// * Change the difficulty of the game
	// *
	// * @param arg1
	// * - "Normal" - will pop only 1 tile after a valid move "Hard" -
	// * will pop 2 tiles after a valid move
	// */
	// @Override
	// public void setDifficulty(String arg1) {
	// difficulty = arg1;
	// }

	/**
	 * Print the board
	 */
	public void printCurrentBoard() {
		this.board.Print();
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