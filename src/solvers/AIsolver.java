package solvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Game2048.Board2048;
import model.GameBoard;
import model.GameModel;

/**
 * The AIsolver class that uses Artificial Intelligence to estimate the next
 * move.
 * 
 * @author Vasilis Vryniotis <bbriniotis at datumbox.com>
 */
public class AIsolver {
	private static GameModel model = null;

	/**
	 * Player vs Computer enum class
	 */
	public enum Player {
		/**
		 * Computer
		 */
		COMPUTER,

		/**
		 * User
		 */
		USER
	}

	public static void setModel(GameModel newModel) {
		model = newModel;
	}

	private static BestMovesCache cacheLoader = new BestMovesCache();

	private static HashMap<String, String> bestMovesCache = new HashMap<String, String>();

	public static void loadMap(String FileName) {
		HashMap<String, String> tmpCache = cacheLoader.FromFile(FileName);
		if (tmpCache != null)
			bestMovesCache = tmpCache;
	}

	public static void saveMap(String FileName) {
		cacheLoader.ToFile(bestMovesCache, FileName);
	}

	/**
	 * Method that finds the best next move.
	 * 
	 * @param theBoard
	 * @param depth
	 * @int method - 0-minimax, 1-alphabeta,2-expectimax
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public static Integer[] findBestMove(GameBoard theBoard, int depth,
			int method, GameModel nmodel) throws CloneNotSupportedException {
		model = nmodel;
		if (listofMoves == null)
			listofMoves = model.getPossibleMoves();
		Map<String, Object> result = new HashMap<String, Object>();
		Integer[] arr = new Integer[2];
		/*if (bestMovesCache.get("" + theBoard.getBoardHash()) != null) {
			String[] tmp = bestMovesCache.get(
					new String("" + theBoard.getBoardHash())).split(",");
			arr = new Integer[] { Integer.parseInt(tmp[0]),
					Integer.parseInt(tmp[1]) };
			System.out.println(arr[0] + "," + arr[1]);
		} else */if (model == null) {
			arr = null;
		} else {
			switch (method) {
			case 0:
				result = minimax(theBoard, depth, Player.USER);
				break;
			case 1:
				result = alphabeta(theBoard, depth, Integer.MIN_VALUE,
						Integer.MAX_VALUE, Player.USER);
				break;
			case 2:
				result = expectimax(theBoard, depth, Player.USER);
				break;
			}
			arr = (Integer[]) result.get("Direction");
			if (arr != null)
				bestMovesCache.put(new String("" + theBoard.getBoardHash()),
						new String("" + arr[0] + "," + arr[1]));
			saveMap("mapCache");
		}
		return arr;
	}

	private static GameBoard copyBoard(GameBoard board) {
		if (board instanceof Board2048)
			return new Board2048((Board2048) board);
		else
			return null;
	}

	/**
	 * Finds the best move by using the Minimax algorithm.
	 * 
	 * @param theBoard
	 * @param depth
	 * @param player
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private static Map<String, Object> minimax(GameBoard theBoard, int depth,
			Player player) throws CloneNotSupportedException {
		Map<String, Object> result = new HashMap<>();
		Integer[] bestDirection = new Integer[2];
		int bestScore;
		if (depth == 0 || model.CheckEndOfGame()) {
			bestScore = heuristicScore(theBoard.getScore(),
					theBoard.getNumberOfEmptyCells(),
					calculateClusteringScore(theBoard.getBoard()));
		} else {
			if (player == Player.USER) {
				bestScore = Integer.MIN_VALUE;
				for (Integer[] direction : listofMoves) {
					GameBoard newBoard = copyBoard(theBoard);
					model.setBoard(newBoard);
					boolean res = model.movement(direction[0], direction[1]);

					if (res) {
						continue;
					}

					Map<String, Object> currentResult = minimax(
							model.getBoard(), depth - 1, Player.COMPUTER);
					int currentScore = ((Number) currentResult.get("Score"))
							.intValue();
					if (result.get("Direction") == null)
						result.put("Direction", direction);
					if (currentScore > bestScore) { // maximize score
						bestScore = currentScore;
						bestDirection = direction;
					}
					// g2048.undoMove();
				}
				if (bestDirection != null)
					result.put("Direction", bestDirection);
			} else {
				bestScore = Integer.MAX_VALUE;

				List<Integer> moves = theBoard.getEmptyCellIds();
				if (moves.isEmpty()) {
					bestScore = 0;
				}
				int[] possibleValues = { 2, 4 };

				int i, j;
				for (Integer cellId : moves) {
					i = cellId / theBoard.getN();
					j = cellId % theBoard.getN();

					for (int value : possibleValues) {
						GameBoard newBoard = copyBoard(theBoard);
						newBoard.setEmptyCell(i, j, value);

						Map<String, Object> currentResult = minimax(newBoard,
								depth - 1, Player.USER);
						int currentScore = ((Number) currentResult.get("Score"))
								.intValue();
						if (currentScore < bestScore) { // minimize best score
							bestScore = currentScore;
						}
					}
				}
			}
		}

		result.put("Score", bestScore);
		return result;
	}

	/**
	 * @return an ArrayList with all possible moves
	 */
	private static ArrayList<Integer[]> getAllPossibleMoves() {
		ArrayList<Integer[]> possibleMoves = null;
		if (model != null)
			possibleMoves = model.getPossibleMoves();
		return possibleMoves;
	}

	private static ArrayList<Integer[]> listofMoves = getAllPossibleMoves();

	/**
	 * Finds the best move bay using the Alpha-Beta pruning algorithm.
	 * 
	 * @param theBoard
	 * @param depth
	 * @param alpha
	 * @param beta
	 * @param player
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private static Map<String, Object> alphabeta(GameBoard theBoard, int depth,
			int alpha, int beta, Player player)
			throws CloneNotSupportedException {
		Map<String, Object> result = new HashMap<>();
		int bestScore;

		if (model.CheckEndOfGame()) {
			if (theBoard.hasWon()) {
				bestScore = Integer.MAX_VALUE; // highest possible score
			} else {
				bestScore = Math.min(theBoard.getScore(), 1); // lowest possible
																// score
			}
		} else if (depth == 0) {
			bestScore = heuristicScore(theBoard.getScore(),
					theBoard.getNumberOfEmptyCells(),
					calculateClusteringScore(theBoard.getBoard()));
		} else {
			if (player == Player.USER) {
				Integer[] bestDirection = null;
				for (Integer[] direction : listofMoves) {
					GameBoard newBoard = copyBoard(theBoard);
					model.setBoard(newBoard);
					boolean moveres = model
							.movement(direction[0], direction[1]);
					// The move didn't affect the board, thus don't continue
					// with the following code and continue to the next
					// available move
					if (moveres) {
						continue;
					}
					Map<String, Object> currentResult = alphabeta(
							model.getBoard(), depth - 1, alpha, beta,
							Player.COMPUTER);
					int currentScore = ((Number) currentResult.get("Score"))
							.intValue();
					if (result.get("Direction") == null)
						bestDirection = direction;
					result.put("Direction", direction);

					if (currentScore > alpha) { // maximize score
						alpha = currentScore;
						bestDirection = direction;
					}

					if (beta <= alpha) {
						break; // beta cutoff
					}
				}
				if (bestDirection != null)
					result.put("Direction", bestDirection);
				bestScore = alpha;
			} else {
				List<Integer> moves = theBoard.getEmptyCellIds();
				int[] possibleValues = { 2, 4 };
				int i, j;
				abloop: for (Integer cellId : moves) {
					i = cellId / theBoard.getN();
					j = cellId % theBoard.getN();

					for (int value : possibleValues) {
						GameBoard newBoard = copyBoard(theBoard);
						newBoard.setEmptyCell(i, j, value);
						Map<String, Object> currentResult = alphabeta(newBoard,
								depth - 1, alpha, beta, Player.USER);
						int currentScore = ((Number) currentResult.get("Score"))
								.intValue();
						if (currentScore < beta) { // minimize best score
							beta = currentScore;
						}

						if (beta <= alpha) {
							break abloop; // alpha cutoff
						}
					}
				}
				bestScore = beta;
				if (moves.isEmpty()) {
					bestScore = 0;
				}
			}
		}

		result.put("Score", bestScore);
		return result;
	}

	/**
	 * Estimates a heuristic score by taking into account the real score, the
	 * number of empty cells and the clustering score of the board.
	 * 
	 * @param actualScore
	 * @param numberOfEmptyCells
	 * @param clusteringScore
	 * @return
	 */
	private static int heuristicScore(int actualScore, int numberOfEmptyCells,
			int clusteringScore) {
		int score = (int) (actualScore + Math.log(actualScore)
				* numberOfEmptyCells - clusteringScore);
		return Math.max(score, Math.min(actualScore, 1));
	}

	/**
	 * Calculates a heuristic variance-like score that measures how clustered
	 * the board is.
	 * 
	 * @param boardArray
	 * @return
	 */
	private static int calculateClusteringScore(int[][] boardArray) {
		int clusteringScore = 0;

		int[] neighbors = { -1, 0, 1 };

		for (int i = 0; i < boardArray.length; ++i) {
			for (int j = 0; j < boardArray.length; ++j) {
				if (boardArray[i][j] == 0) {
					continue; // ignore empty cells
				}

				// clusteringScore-=boardArray[i][j];

				// for every pixel find the distance from each neightbors
				int numOfNeighbors = 0;
				int sum = 0;
				for (int k : neighbors) {
					int x = i + k;
					if (x < 0 || x >= boardArray.length) {
						continue;
					}
					for (int l : neighbors) {
						int y = j + l;
						if (y < 0 || y >= boardArray.length) {
							continue;
						}

						if (boardArray[x][y] > 0) {
							++numOfNeighbors;
							sum += Math
									.abs(boardArray[i][j] - boardArray[x][y]);
						}

					}
				}

				clusteringScore += sum / numOfNeighbors;
			}
		}

		return clusteringScore;
	}

	/**
	 * Implementation of expectimax algorithm to calculate the best next move
	 * 
	 * @param theBoard
	 * @param depth
	 * @param player
	 * @return
	 */
	private static Map<String, Object> expectimax(GameBoard theBoard,
			int depth, Player player) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (depth == 0) {
			if (!model.CheckEndOfGame()) {
				result.put("Score", new Double(gradient(theBoard)));
				return result;
			} else {
				return null; // Penalizes deadend.
			}
		}

		if (player == Player.USER) {
			Double best_score = new Double(-1);
			Integer[] best_direction = null;
			for (Integer[] direction : listofMoves) {
				GameBoard newBoard = copyBoard(theBoard);
				model.setBoard(newBoard);
				model.movement(direction[0], direction[1]);
				boolean move = model.isBoardEquals(theBoard.getBoard(), model
						.getBoard().getBoard());
				if (move)
					continue;
				result = expectimax(model.getBoard(), depth - 1,
						Player.COMPUTER);
				Double score = (Double) result.get("Score");

				if (result.get("Direction") == null)
					best_direction = direction;

				if (score >= best_score) {
					best_score = score;
					best_direction = direction;
				}
			}
			result.put("Score", best_score);
			result.put("Direction", best_direction);
		} else {
			// This time the player is Computer
			Double total_score = new Double(0);
			Double total_weight = new Double(0);
			List<Integer> moves = theBoard.getEmptyCellIds();
			int[] possibleValues = { 2, 4 };
			int i, j;
			for (Integer cellId : moves) {
				i = cellId / theBoard.getN();
				j = cellId % theBoard.getN();

				for (int value : possibleValues) {
					GameBoard newBoard = copyBoard(theBoard);
					newBoard.setEmptyCell(i, j, value);
					result = expectimax(newBoard, depth - 1, Player.USER);
					Double score = (Double) result.get("Score");
					if (value == 2) {
						total_score += score * 0.9;
						total_weight += 0.9;
					} else {
						total_score += score * 0.1;
						total_weight += 0.1;
					}
				}
			}
			result.put("Score", new Double(total_score / total_weight));
		}
		return result;
	}

	/**
	 * gradient calculate an additional heuristic value based on the board,
	 * returns the current state (whether the board is arranged as "gradiant" or
	 * not)
	 * 
	 * @param board
	 * @return
	 */
	private static int gradient(GameBoard board) {
		int gradientX = 0;
		int gradientY = 0;

		for (int i = 0; i < board.getN(); i++) {
			for (int j = 0; j < board.getN(); j++) {
				if (board.getBoard(j, i) != 0) {
					gradientX += board.getBoard(j, i)
							* (2 * j - board.getN() - 1);
					gradientY += board.getBoard(j, i)
							* (2 * i - board.getN() - 1);
				}
			}
		}

		// Absolute value ensures that both directions are taken into
		// account
		return Math.abs(gradientX) + Math.abs(gradientY);
	}
}
