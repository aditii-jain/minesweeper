package com.example;

public interface MSModelInterface {
	// returns whether there is a mine here
	boolean isMine(int row, int col); 
	 
	boolean isGameWon();
	
	void toggleFlag(int r, int c);
	
	void setFlag(int x, int y, boolean flagged);
	boolean isFlagged(int r, int c);
	
	void reveal(int r, int c); // reveal
	
	boolean isOver(); // game is over (either won or lost)

	boolean isVisible(int x, int y); // isRevealed
	
	int numMinesAdjacent(int x, int y);
	
	int getNumRows();
	int getNumCols();
	
	int numMinesLeft(); // numMines - numFlags
	int numFlags();
	int numMines();
	
	void resetGame(); // sets up new game with default rows, cols and mines
	void resetGame(int numRows, int numCols, int numMines);




	
}
