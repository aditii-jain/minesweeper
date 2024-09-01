package com.example;

import java.util.Random;
public class MinesweeperModel implements MSModelInterface{
	Tile[][] gameData;
	int numMines;
	boolean gameOver = false;
	
	
	public MinesweeperModel(int rows, int cols, int mines) {
		gameData = new Tile[rows][cols];
		numMines = mines;
		
		for (int r = 0; r < gameData.length; r++) {
			for (int c = 0; c < gameData[r].length; c++) {
				gameData[r][c] = new Tile();
			}
		}
		
		Random r = new Random();
		
		int mineCount = 0;
		
		while (mineCount < numMines) {
			int row = r.nextInt(rows);
			int col = r.nextInt(cols);
			while (gameData[row][col].isMine()) {
				row = r.nextInt(rows);
				col = r.nextInt(cols);
			}
			gameData[row][col].setMine(true);
			mineCount++;
			
		}
	}
	
	public Tile getTile(int r, int c) {
		return gameData[r][c];
		
	}
	@Override
	public boolean isMine(int row, int col) {
		return gameData[row][col].isMine();
	}

	@Override
	public boolean isGameWon() {
		for (int r = 0; r < gameData.length; r++) {
			for (int c = 0; c < gameData[r].length; c++) {
				if (!gameData[r][c].isMine() 
						&& !gameData[r][c].isVisible()) {
					return false;
					
				}
			}
		}
		return true;
	}

	@Override
	public void toggleFlag(int r, int c) {
		if (gameData[r][c].isFlagged()) {
			setFlag(r, c, false);	
		} else {
			setFlag(r, c, true);	
		}
		
	}

	@Override
	public void setFlag(int r, int c, boolean flagged) {
		gameData[r][c].setFlag(flagged);
		
	}

	@Override
	public boolean isFlagged(int r, int c) {
		return gameData[r][c].isFlagged();
	}

	@Override
	public void reveal(int r, int c) {	
//		If the cell hasn't yet been revealed, reveal it.
		if (!gameData[r][c].isVisible()) gameData[r][c].setReveal(true);
//		If the bottom layer of the cell is a mine, game over.
		if (gameData[r][c].isMine()) {
			gameOver = true;
		}
//		Otherwise if the bottom layer of the cell is blank (no adjacent mines),
//		then recursively reveal each its 8 neighbors 
// 		if they are within bounds and haven't been revealed.
		if (numMinesAdjacent(r, c) == 0) {
			if (r > 0 && c > 0 && !gameData[r - 1][c - 1].isVisible()) reveal(r - 1, c - 1);
			if (r > 0 && !gameData[r - 1][c].isVisible()) reveal(r - 1, c);
			if (r > 0 && c < gameData[r].length - 1 && !gameData[r - 1][c + 1].isVisible()) reveal(r - 1, c + 1);
			if (c > 0 && !gameData[r][c - 1].isVisible()) reveal(r, c - 1);
			if (c < gameData[r].length - 1 && !gameData[r][c + 1].isVisible()) reveal(r, c + 1);
			if (r < gameData.length - 1 && c > 0 && !gameData[r + 1][c - 1].isVisible()) reveal(r + 1, c - 1);
			if (r < gameData.length - 1 && !gameData[r + 1][c].isVisible()) reveal(r + 1, c);
			if (r < gameData.length - 1 && c < gameData[r].length - 1 && !gameData[r + 1][c + 1].isVisible()) reveal(r + 1, c + 1);
		}
		
		
		
	}

	@Override
	public boolean isOver() {
//		for (int r = 0; r < gameData.length; r++) {
//			for (int c = 0; c < gameData[r].length; c++) {
//				if (gameData[r][c].isMine() 
//						&& gameData[r][c].isVisible()) {
//					return true;
//					
//				}
//			}
//		}
//		return false;
		return gameOver;
	}

	@Override
	public boolean isVisible(int r, int c) {
		return gameData[r][c].isVisible();
	}

	@Override
	public int numMinesAdjacent(int r, int c) {
		int numMines = 0;
		
		if (r >= 0 && r < gameData.length && c >= 0 && c < gameData[r].length) {
			if (r > 0 && c > 0 && gameData[r - 1][c - 1].isMine()) numMines++;
			if (r > 0 && gameData[r - 1][c].isMine()) numMines++;
			if (r > 0 && c < gameData[r].length - 1 && gameData[r - 1][c + 1].isMine()) numMines++;
			if (c > 0 && gameData[r][c - 1].isMine()) numMines++;
			if (c < gameData[r].length - 1 && gameData[r][c + 1].isMine()) numMines++;
			if (r < gameData.length - 1 && c > 0 && gameData[r + 1][c - 1].isMine()) numMines++;
			if (r < gameData.length - 1 && gameData[r + 1][c].isMine()) numMines++;
			if (r < gameData.length - 1 && c < gameData[r].length - 1 && gameData[r + 1][c + 1].isMine()) numMines++;
		}
		return  numMines;
		
	}

	@Override
	public int getNumRows() {
		return gameData.length;
	}

	@Override
	public int getNumCols() {
		return gameData[0].length;
	}

	@Override
	public int numMinesLeft() {
		return numMines() - numFlags(); 
	}

	@Override
	public int numFlags() {
		int flags = 0;
		for (int r = 0; r < gameData.length; r++) {
			for (int c = 0; c < gameData[r].length; c++) {
				if (gameData[r][c].isFlagged()) {
					flags++;
					
				}
			}
		}
		return flags;
	}

	@Override
	public int numMines() {
		return numMines;
//		int mines = 0;
//		for (int r = 0; r < gameData.length; r++) {
//			for (int c = 0; c < gameData[r].length; c++) {
//				if (gameData[r][c].isMine()) {
//					mines++;
//					
//				}
//			}
//		}
//		return mines;
	}

	@Override
	public void resetGame() {
		Random r = new Random();
		
		int mines = 0;
		gameData = new Tile[10][10];
		while (mines < 8) {
			int row = r.nextInt(10);
			int col = r.nextInt(10);
			while (gameData[row][col].isMine()) {
				row = r.nextInt(10);
				col = r.nextInt(10);
			}
			gameData[row][col].setMine(true);
			mines++;
		}
		
		
	}

	@Override
	public void resetGame(int numRows, int numCols, int numMines) {
		gameOver = false;
		this.numMines = numMines;
		Random r = new Random();
		
		int mines = 0;
		gameData = new Tile[numRows][numCols];
		for (int j = 0; j < gameData.length; j++) {
			for (int k = 0; k < gameData[j].length; k++) {
				gameData[j][k] = new Tile();
			}
		}
		
		while (mines < numMines) {
			int row = r.nextInt(numRows);
			int col = r.nextInt(numCols);
			while (gameData[row][col].isMine()) {
				row = r.nextInt(numRows);
				col = r.nextInt(numCols);
			}
			gameData[row][col].setMine(true);
			mines++;
			
		}
		
	}

	public void moveMine(int row, int col) {
		Random r = new Random();
		int newRow = r.nextInt(getNumRows());
		int newCol = r.nextInt(getNumCols());
		while (isMine(newRow, newCol)) {
			newRow = r.nextInt(getNumRows());
			newCol = r.nextInt(getNumCols());
			
		}
		gameData[row][col].setMine(false);
		gameData[newRow][newCol].setMine(true);
		
	}

}
