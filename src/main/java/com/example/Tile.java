package com.example;

public class Tile {
	private boolean isMine = false;
	private boolean isFlagged = false;
	private boolean isVisible = false;
	
	public boolean isMine() {
		return isMine;
	}

	public boolean isFlagged() {
		return isFlagged;
	}

	public void setFlag(boolean flagged) {
		isFlagged = flagged;
		
	}

	public void setReveal(boolean b) {
		isVisible = b;
		
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setMine(boolean b) {
		isMine = b;
		
	}

	

}
