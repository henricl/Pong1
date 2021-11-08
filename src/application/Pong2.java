package application;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Pong2 extends Application {
	
	//Variables
	private static final int BackgroundWidth = 800;
	private static final int BackgroundHeight = 600;
	private static final int RacketHeight =70;
	private static final int RacketWidth = 5;
	private static final double BallRadius = 10;
	private int ballYSpeed = 1;
	private int ballXSpeed = 1;
	private double playerOneYPos = BackgroundHeight / 2;
	private double playerTwoYPos = BackgroundHeight / 2;
	private double ballXPos = BackgroundWidth / 2;
	private double ballYPos = BackgroundHeight / 2;
	private int scoreP1 = 0;
	private int scoreP2 = 0;
	private boolean gameStarted;
	private int playerOneXPos = 0;
	private double playerTwoXPos = BackgroundWidth - RacketWidth;
		
	public void start(Stage stage) throws Exception {
		stage.setTitle("P O N G");
		//The size of the background
		Canvas canvas = new Canvas(BackgroundWidth, BackgroundHeight);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		//JavaFX Timeline = free form animation defined by KeyFrames and their duration 
		Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
		//number of cycles in animation INDEFINITE = repeat indefinitely
		tl.setCycleCount(Timeline.INDEFINITE);
		
		//mouse control (move and click)
		canvas.setOnMouseMoved(e ->  playerOneYPos  = e.getY());
		canvas.setOnMouseClicked(e ->  gameStarted = true);
		stage.setScene(new Scene(new StackPane(canvas)));
		stage.show();
		tl.play();
	}

	private void run(GraphicsContext gc) {
		//set graphics
		//set background color
		gc.setFill(Color.GREEN);
		gc.fillRect(0, 0, BackgroundWidth, BackgroundHeight);
		
		//set text
		gc.setFill(Color.WHITE);
		gc.setFont(Font.font(25));
		
		if(gameStarted) {
			//set ball movement
			ballXPos+=ballXSpeed;
			ballYPos+=ballYSpeed;
			
			//simple computer opponent who is following the ball
			if(ballXPos < BackgroundWidth - BackgroundWidth  / 4) {
				playerTwoYPos = ballYPos - RacketHeight / 2;
			}  else {
				playerTwoYPos =  ballYPos > playerTwoYPos + RacketHeight / 2 ?playerTwoYPos += 1: playerTwoYPos - 1;
			}
			//draw the ball
			gc.fillOval(ballXPos, ballYPos, BallRadius, BallRadius);
			
		} else {
			//set the start text
			gc.setStroke(Color.WHITE);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.strokeText("Start Game", BackgroundWidth / 2, BackgroundHeight / 2);
			
			//reset the ball start position 
			ballXPos = BackgroundWidth / 2;
			ballYPos = BackgroundHeight / 2;
			
			//reset the ball speed and the direction
			ballXSpeed = new Random().nextInt(2) == 0 ? 1: -1;
			ballYSpeed = new Random().nextInt(2) == 0 ? 1: -1;
		}
		
		//makes sure the ball stays in the canvas
		if(ballYPos > BackgroundHeight || ballYPos < 0) ballYSpeed *=-1;
		
		//if user miss the ball, computer gets a point
		if(ballXPos < playerOneXPos - RacketWidth) {
			scoreP2++;
			gameStarted = false;
		}
		
		//if the computer misses the ball, user gets the point
		if(ballXPos > playerTwoXPos + RacketWidth) {  
			scoreP1++;
			gameStarted = false;
		}
	
		//increase the speed after the ball hits the racket
		if( ((ballXPos + BallRadius > playerTwoXPos) && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + RacketHeight) || 
			((ballXPos < playerOneXPos + RacketWidth) && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + RacketHeight)) {
			ballYSpeed += 1 * Math.signum(ballYSpeed);
			ballXSpeed += 1 * Math.signum(ballXSpeed);
			ballXSpeed *= -1;
			ballYSpeed *= -1;
			//gc.setFill(Color.BLUE;) //color(Math.random(), Math.random(), Math.random()));
		}
		
		//draw score
		gc.fillText(scoreP1 + "\t\t\t\t\t\t\t\t" + scoreP2, BackgroundWidth / 2, 100);
		//draw player 1 & 2
		gc.fillRect(playerTwoXPos, playerTwoYPos, RacketWidth, RacketHeight);
		gc.fillRect(playerOneXPos, playerOneYPos, RacketWidth, RacketHeight);
	}
	
		// start the application
		public static void main(String[] args) {
		launch(args);
		}
}