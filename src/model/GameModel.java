package model;

import java.util.ArrayList;

public interface GameModel {
//	void undoMove();

	GameBoard getBoard();
//	int[][] getBoard();

//	void setBoard(int[][] board);

//	int getScore();

//	void setScore(int score);

//	int getBestScore();

//	void setBestScore(int bestScore);

//	Stack getMoves();
//
//	void setMoves(Stack moves);

	boolean CheckEndOfGame();

//	void setDifficulty(String arg1);

	void setBoard(GameBoard newBoard);

	boolean movement(int dx, int dy);

	boolean isBoardEquals(int[][] A, int[][] B);

	ArrayList<Integer[]> getPossibleMoves();
	
	//void setSolverServerProperties(ServerProperties arg1);

	boolean CanAskServer();
	
	
	
	void DoBestMoves(int MovesNnumber);

	

}
