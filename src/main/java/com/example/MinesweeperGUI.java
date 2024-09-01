package com.example;







import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.animation.AnimationTimer;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.control.TextInputDialog;

public class MinesweeperGUI extends Application {
	MyActionHandler bHand = new MyActionHandler();
	boolean firstMove = true;
	private long lastTime = 0;
	int numRows = 10;
	int numCols = 10;
	MinesweeperModel model = new MinesweeperModel(10, 10, 10);
	final Image tileImg = new Image(getClass().getResource("/minesweeper_images/blank.gif").toExternalForm());
	final Image smileyImg = new Image(getClass().getResource("/minesweeper_images/face_smile.gif").toExternalForm());
	final Image winSmileyImg = new Image(getClass().getResource("/minesweeper_images/face_win.gif").toExternalForm());
	final Image flagged = new Image(getClass().getResource("/minesweeper_images/bomb_flagged.gif").toExternalForm());
	final Image bombImg = new Image(getClass().getResource("/minesweeper_images/bomb_revealed.gif").toExternalForm());
	final Image bombDeathImg = new Image(getClass().getResource("/minesweeper_images/bomb_death.gif").toExternalForm());
	final Image bombWrongImg = new Image(getClass().getResource("/minesweeper_images/bomb_wrong.gif").toExternalForm());
	final Image deadSmileyImg = new Image(getClass().getResource("/minesweeper_images/face_dead.gif").toExternalForm());
	final ImageView smileyView = new ImageView(smileyImg);
	int clickedRow;
	int clickedCol;
	ImageView[][] tileViews = new ImageView[numRows][numCols];
	Pane[][] panes = new Pane[numRows][numCols];
	ImageView tileView = new ImageView(tileImg);
	// A MenuBar with Menus that have these MenuItems
	MenuBar menuBar = new MenuBar();
	
	// Game Menu
	Menu gameMenu = new Menu("Game");
	// New Beginner Game
	MenuItem newBeginner = new MenuItem("New Beginner Game");
	// New Intermediate Game
	MenuItem newIntermediate = new MenuItem("New Intermediate Game");
	// New Expert Game
	MenuItem newExpert = new MenuItem("New Expert Game");
	// New Custom Game
	MenuItem newCustom = new MenuItem("New Custom Game");
	// Exit
	MenuItem exit = new MenuItem("Exit");
	
	// Options Menu
	Menu optionsMenu = new Menu("Options");
	// Set Number of Mines
	MenuItem setMines = new MenuItem("Set Number of Mines");
	// Set Grid Size
	MenuItem setGrid = new MenuItem("Set Grid Size");
	
	// Help Menu
	Menu helpMenu = new Menu("Help");
	// How To Play
	MenuItem instructions = new MenuItem("How To Play");
	// Set Grid Size
	MenuItem about = new MenuItem("About");
	
	// grid
	GridPane grid = new GridPane();
	
	// root node
	VBox root = new VBox();
	BorderPane top = new BorderPane();

    // Labels
    Label mines = new Label("Mines Remaining\n\t       10");
   
    Label time = new Label("Time Elapsed\n0s");
    AnimationTimer timer;
	
    public static void main(String[] args) {
		launch(args);
	}
    void updateView() {  
    	if (model.isGameWon()) {
    		smileyView.setImage(winSmileyImg);
    	} else if (model.isOver()) {
    		smileyView.setImage(deadSmileyImg);
    	}
        // loop through each row/col of the View (double loop)
    	for (int row = 0; row < model.getNumRows(); row++) {
            for (int col = 0; col < model.getNumCols(); col++) {
            	
            	if (model.isGameWon() && model.isFlagged(row, col)) {
            		tileViews[row][col].setImage(bombImg);      
            	} else if (model.isOver()) {
            		if (model.isFlagged(row, col)) {
            			if (!model.isMine(row, col)) {
            				tileViews[row][col].setImage(bombWrongImg); 
            			} else {
            				tileViews[row][col].setImage(bombImg);  
            			}
            		} else if (model.isMine(row, col)) {
            			if (row == clickedRow && col == clickedCol) {
            				tileViews[clickedRow][clickedCol].setImage(bombDeathImg);
            			} else {
            				tileViews[row][col].setImage(bombImg);  
            			}
            		}
            	}
            	// Ask the model questions and update the View. For example:  
            	else if (!model.isVisible(row, col)) {
                    if (model.isFlagged(row, col)) {
                    // Set the image at (row, col) to FLAG image in the View
                    // You can change an ImageView's image using setImage()  
                    	tileViews[row][col].setImage(flagged);                    	
                    } else tileViews[row][col].setImage(tileImg); // set image to blank tile  
                } else if (model.isVisible(row, col)) {  
                	tileViews[row][col].setImage(new Image(getClass().getResource("/minesweeper_images/num_" + model.numMinesAdjacent(row, col) + ".gif").toExternalForm()));
                }
            }
    	}
            
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		 time.setTextAlignment(TextAlignment.CENTER);
		 timer = new AnimationTimer() {
	            @Override
	            public void handle(long now) {
	                if (lastTime == 0) {
	                    lastTime = now;
	                }
	                long elapsed = now - lastTime;
	                time.setText("Time Elapsed\n" + elapsed / 1_000_000_000 + "s");
	            }
	     };
	     
	     
	    timer.start();

		gameMenu.getItems().addAll(newBeginner, newIntermediate, newExpert, newCustom, exit);
		optionsMenu.getItems().addAll(setMines, setGrid);
		helpMenu.getItems().addAll(instructions, about);
		menuBar.getMenus().addAll(gameMenu, optionsMenu, helpMenu);
		
		newBeginner.setOnAction(bHand);
	    newIntermediate.setOnAction(bHand);
	    newExpert.setOnAction(bHand);
	    newCustom.setOnAction(bHand);
	    exit.setOnAction(bHand);
	    setMines.setOnAction(bHand);
	    setGrid.setOnAction(bHand);
	    instructions.setOnAction(bHand);
	    about.setOnAction(bHand);
	   
	
		
		for (int row = 0; row < model.getNumRows(); row++) {
			RowConstraints r = new RowConstraints();
			r.setPercentHeight(100);
			grid.getRowConstraints().add(r);
            for (int col = 0; col <  model.getNumCols(); col++) {
            	if (row == 0) {
            		ColumnConstraints c = new ColumnConstraints();
        			c.setPercentWidth(100);
        			grid.getColumnConstraints().add(c);
            	}
     
            	tileView = new ImageView(tileImg);
            	tileViews[row][col] = tileView;
            	
            	Pane p = new Pane(tileView);
            	panes[row][col] = p;
            	grid.add(p, col, row);
            	tileView.fitWidthProperty().bind(p.widthProperty());
        		tileView.fitHeightProperty().bind(p.heightProperty());
        		
        		// add mouse listener to the ImageView
        		
    			p.setOnMouseClicked(event -> {
        			mines.setText("Mines Remaining\n\t       " + model.numMinesLeft());
        			
                    clickedRow = GridPane.getRowIndex(p);
                    clickedCol = GridPane.getColumnIndex(p);
                    if (event.getButton() == MouseButton.PRIMARY) {
                    	if (!model.isVisible(clickedRow, clickedCol) && 
                        		!model.isFlagged(clickedRow, clickedCol)) {
                    		if (firstMove && model.isMine(clickedRow, clickedCol)) {
        						model.moveMine(clickedRow, clickedCol);
        					} 
                        	model.reveal(clickedRow, clickedCol);
                        	firstMove = false;
                        }
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                    	if (!model.isVisible(clickedRow, clickedCol)) {
                        	model.toggleFlag(clickedRow, clickedCol);
                        }
                    	mines.setText("Mines Remaining\n\t       " + model.numMinesLeft());
                    }
                    if  (model.isOver()) {
        				System.out.println("You Lose");
        			} else if (model.isGameWon()){
        				System.out.println("You Win");
        			}
                    
                    updateView();
                    if (model.isOver() || model.isGameWon()) {
                    	for (int j = 0; j < numRows; j++) {
                            for (int k = 0; k < numCols; k++) {
                            	panes[j][k].setOnMouseClicked(null);
                            }
                    	}
                    	
                    	timer.stop();
                    }
                    
                });
        		
        		
        		
        		//tileView.setPreserveRatio(true);
                
            }
        }
		
		BorderPane.setMargin(smileyView, new Insets(10));		
		BorderPane.setMargin(mines, new Insets(10));
		BorderPane.setMargin(time, new Insets(10));
		top.setTop(menuBar);
		mines.setStyle("-fx-background-color: lightblue; -fx-border-color: darkblue; -fx-padding: 5px; -fx-background-radius: 10px; -fx-border-radius: 10px;");
		top.setLeft(mines);
		mines.setMinWidth(150);
		mines.setMinHeight(50);
		mines.setAlignment(Pos.CENTER);
		time.setAlignment(Pos.CENTER);
		time.setMinWidth(150);
		time.setMinHeight(50);
		time.setStyle("-fx-background-color: lightblue; -fx-border-color: darkblue; -fx-padding: 5px; -fx-background-radius: 10px; -fx-border-radius: 10px;");
		top.setRight(time);
		top.setCenter(smileyView);
		//grid.setGridLinesVisible(true);
		VBox.setVgrow(grid, Priority.ALWAYS);
		
		
		
		
		root.getChildren().addAll(top, grid);
		
	
		Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        primaryStage.setMinHeight(numRows * tileImg.getHeight() + 100);
        primaryStage.setMinWidth(numCols * tileImg.getWidth() + 100);
	}

	
	void updateModel() {
		smileyView.setImage(smileyImg);
		panes = new Pane[model.getNumRows()][model.getNumCols()];
		root.getChildren().remove(grid);
		grid = new GridPane();
		root.getScene().getWindow().setHeight(numRows * tileImg.getHeight() + 100);
		root.getScene().getWindow().setWidth(numCols * tileImg.getWidth() + 100);
		tileViews = new ImageView[model.getNumRows()][model.getNumCols()];
		for (int row = 0; row < model.getNumRows(); row++) {
			RowConstraints r = new RowConstraints();
			r.setPercentHeight(100);
			grid.getRowConstraints().add(r);
            for (int col = 0; col < model.getNumCols(); col++) {
            	if (row == 0) {
            		ColumnConstraints c = new ColumnConstraints();
        			c.setPercentWidth(100);
        			grid.getColumnConstraints().add(c);
            	}
     
            	tileView = new ImageView(tileImg);
            	tileViews[row][col] = tileView;
            	
            	Pane p = new Pane(tileView);
            	panes[row][col] = p;
            	grid.add(p, col, row);
            	tileView.fitWidthProperty().bind(p.widthProperty());
        		tileView.fitHeightProperty().bind(p.heightProperty());
        		
        		// add mouse listener to the ImageView
        		
    			p.setOnMouseClicked(event -> {
        			mines.setText("Mines Remaining\n\t       " + model.numMinesLeft());
        			
                    clickedRow = GridPane.getRowIndex(p);
                    clickedCol = GridPane.getColumnIndex(p);
                    if (event.getButton() == MouseButton.PRIMARY) {
                    	if (!model.isVisible(clickedRow, clickedCol) && 
                        		!model.isFlagged(clickedRow, clickedCol)) {
                    		if (firstMove && model.isMine(clickedRow, clickedCol)) {
        						model.moveMine(clickedRow, clickedCol);
        					} 
                        	model.reveal(clickedRow, clickedCol);
                        	firstMove = false;
                        }
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                    	if (!model.isVisible(clickedRow, clickedCol)) {
                        	model.toggleFlag(clickedRow, clickedCol);
                        }
                    	mines.setText("Mines Remaining\n\t      " + model.numMinesLeft());
                    }
                    if  (model.isOver()) {
        				System.out.println("You Lose");
        			} else if (model.isGameWon()){
        				System.out.println("You Win");
        			}
                    
                    updateView();
                    if (model.isOver() || model.isGameWon()) {
                    	for (int j = 0; j < model.getNumRows(); j++) {
                            for (int k = 0; k < model.getNumCols(); k++) {
                            	panes[j][k].setOnMouseClicked(null);
                            }
                    	}
                    	
                    	timer.stop();
                    }
                    
                });
            }
		}
		
		root.getChildren().add(grid);
		VBox.setVgrow(grid, Priority.ALWAYS);
		
	}
	
	private class MyActionHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			
			if (event.getSource() == newBeginner) {
				model.resetGame(8, 8, 10);
				updateModel();
				mines.setText("Mines Remaining\n\t       " + model.numMinesLeft());
			} else if (event.getSource() == newIntermediate) {
				model.resetGame(16, 16, 40);
				updateModel();
				mines.setText("Mines Remaining\n\t       " + model.numMinesLeft());
			} else if (event.getSource() == newExpert) {
				model.resetGame(16, 31, 99);
				updateModel();
				mines.setText("Mines Remaining\n\t       " + model.numMinesLeft());
			} else if (event.getSource() == newCustom) {
				
				TextInputDialog input = new TextInputDialog();  
				input.setHeaderText("How many mines would you like?");  
				input.showAndWait();  
				String answer = input.getEditor().getText();
				if (!answer.equals("")) {
					int numMinesRequested = Integer.parseInt(answer);
					input = new TextInputDialog();  
					input.setHeaderText("How many rows would you like?");  
					input.showAndWait();  
					answer = input.getEditor().getText(); 
					int rows = Integer.parseInt(answer);
					
					if (!answer.equals("")) {
						input = new TextInputDialog();  
						input.setHeaderText("How many columns would you like?");  
						input.showAndWait();  
						answer = input.getEditor().getText(); 
						int cols = Integer.parseInt(answer);
						
						if (!answer.equals("")) {
							model.resetGame(rows, cols, numMinesRequested);
							updateModel();
							mines.setText("Mines Remaining\n\t       " + model.numMinesLeft());
						}
						
					}
				}
				
				
				
				
				
				
			} else if (event.getSource() == exit) {
				Stage stage = (Stage) exit.getParentPopup().getOwnerWindow();
			    stage.close();
				
			} else if (event.getSource() == setMines) {
				TextInputDialog input = new TextInputDialog();  
				input.setHeaderText("How many mines would you like?");  
				input.showAndWait();  
				String answer = input.getEditor().getText(); 
				int numMinesRequested = Integer.parseInt(answer);
				
				if (numMinesRequested < 1 || numMinesRequested > model.getNumRows() * model.getNumCols() - 1) {
				    String message = "Sorry.  The number of mines must be between 1 and " + (model.getNumRows() * model.getNumCols() - 1);  
				    Alert a = new Alert(AlertType.ERROR, message, ButtonType.OK);  
				    a.showAndWait();  
				} else {
					model.resetGame(model.getNumRows(), model.getNumCols(), numMinesRequested);
					mines.setText("Mines Remaining\n\t       " + model.numMinesLeft());
				}
			} else if (event.getSource() == setGrid) {
				TextInputDialog input = new TextInputDialog();  
				input.setHeaderText("How many rows would you like?");  
				input.showAndWait();  
				String answer = input.getEditor().getText(); 
				int rows = Integer.parseInt(answer);
				
				input = new TextInputDialog();  
				input.setHeaderText("How many columns would you like?");  
				input.showAndWait();  
				answer = input.getEditor().getText(); 
				int cols = Integer.parseInt(answer);
				
				
				model.resetGame(rows, cols, model.numMines());
				updateModel();
				mines.setText("Mines Remaining\n\t       " + model.numMinesLeft());
			} else if (event.getSource() == instructions) {
				WebView instructionsPage = new WebView();
				WebEngine engine = instructionsPage.getEngine();
			
				// Load the HTML file from the resources directory
				String url = getClass().getResource("/html/instructions.html").toExternalForm();
				engine.load(url);
			
				Stage instructionsStage = new Stage();
				Scene scene = new Scene(instructionsPage);
				instructionsStage.setScene(scene);
				instructionsStage.show();
			} else if (event.getSource() == about) {
				WebView aboutPage = new WebView();
				WebEngine engine = aboutPage.getEngine();
			
				// Load the HTML file from the resources directory
				String url = getClass().getResource("/html/about.html").toExternalForm();
				engine.load(url);
			
				Stage aboutStage = new Stage();
				Scene scene = new Scene(aboutPage);
				aboutStage.setScene(scene);
				aboutStage.show();
			}
		
    	
		}
	}

}
