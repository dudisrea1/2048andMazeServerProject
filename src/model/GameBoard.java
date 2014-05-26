package model;

import java.util.List;

public interface GameBoard {
	public int[][] getBoard();
	public int getBoard(int x, int y);
	public void setBoard(int x, int y, int val);
	public void setBoard(int[][] board);
	public int getScore();
	public void setScore(int score);
	public int getN();
	public void setN(int n);
	public boolean hasWon();
	public boolean hasWon(int destination);
	public void calcNumberOfEmptyCells();
	public int getNumberOfEmptyCells();
	public List<Integer> getEmptyCellIds();
	public void setEmptyCell(int i, int j, int value);
	public int getBoardHash();
	public void Print();
	
}
