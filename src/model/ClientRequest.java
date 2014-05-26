package model;

import java.io.Serializable;

public class ClientRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GameBoard Board;
	private int depth, method;
	private GameModel model;
	
	public ClientRequest(GameBoard Board, int depth, int method, GameModel model)
	{
		this.Board=Board;
		this.depth=depth;
		this.method=method;
		this.model=model;
	}

	public GameBoard getBoard() {
		return Board;
	}

	public void setBoard(GameBoard board) {
		Board = board;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public GameModel getModel() {
		return model;
	}

	public void setModel(GameModel model) {
		this.model = model;
	}
	
	public void PrintBoard(){
		Board.Print();
	}

}
