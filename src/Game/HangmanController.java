/**
 * Prathit Pannase: ppannase
 */
package Game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

// Acts a controller for playing hangman	
public class HangmanController extends WordNerdController {

	HangmanView hangmanView; 
	Hangman hangman;


	/**startController() creates new Hangman, HangmanView, invokes setupRound() to create a new hangmanRound,
	 * refreshes the view for next round, and invokes setupBindings to bind the new
	 * HangmanRound properties with GUI components.
	 */
	@Override
	void startController() {
		hangmanView = new HangmanView();
		hangman = new Hangman();
		hangman.hangmanRound = hangman.setupRound();
		hangmanView.refreshGameRoundView(hangman.hangmanRound);
		setupBindings();

		VBox lowerPanel = new VBox();
		lowerPanel.getChildren().add(hangmanView.bottomGrid);
		lowerPanel.getChildren().add(WordNerd.exitButton);
		lowerPanel.setAlignment(Pos.CENTER);

		WordNerd.root.setTop(hangmanView.topMessageText);
		WordNerd.root.setCenter(hangmanView.topGrid);
		WordNerd.root.setBottom(lowerPanel);

		//bind newWordButton to start a new round, refresh the view,
		//restart the timer, and setupBindings for new HangmanRound
		hangmanView.newWordButton.setOnAction(event -> { 
			hangman.hangmanRound = hangman.setupRound();
			hangmanView.refreshGameRoundView(hangman.hangmanRound);
			GameView.wordTimer.restart(Hangman.HANGMAN_GAME_TIME);
			setupBindings();
		});
		
		//attach handlers to all keyboardButtons
		for (int i = 0; i < hangmanView.keyboardButtons.length; i++) {
			hangmanView.keyboardButtons[i].setOnAction(new KeyboardButtonHandler() );
		}
	}


	/** setupRoundBindings binds GUI components with hangmanRound properties and the timer
	 * to change clue labels, change smiley button, disable clueGrid and keyboardGrid 
	 */
	@Override
	void setupBindings() {

		//Bind a listener to hangmanRound's clueWordProperty 
		//so that whenever it changes, the clueLabels should also 
		//change in hangmanView 
		hangman.hangmanRound.clueWordProperty().addListener((observable, oldValue, newValue) -> {
			for (int i = 0; i < hangman.hangmanRound.getClueWord().length(); i++) {
				hangmanView.clueLabels[i].setText(String.format("%s", newValue.charAt(i)));
			}
		});

		//When timer runs out, set smiley to sadly, isRoundComplete to true
		GameView.wordTimer.timeline.setOnFinished(event -> { 
			hangmanView.smileyButton.setGraphic(hangmanView.smileyImageViews[GameView.SADLY_INDEX]);
			hangman.hangmanRound.setIsRoundComplete(true);
			String score = "0"+","+hangman.hangmanRound.getPuzzleWord()+","+(30-Integer.parseInt(GameView.wordTimer.timerButton.getText()))+","+getScoreDouble();
			wordNerdModel.writeScore(score);
		});

		//Bind keyboardGrid's and clueGrid's disable property to hangmanRound's isRoundCompleteProperty.
		//This means that when round is complete, the two should be disabled.
		hangmanView.keyboardGrid.disableProperty().bind(hangman.hangmanRound.isRoundCompleteProperty());
		hangmanView.clueGrid.disableProperty().bind(hangman.hangmanRound.isRoundCompleteProperty());

	}
	private double getScoreDouble(){
		// Method used to give double value of score
		double Score;
		if(hangman.hangmanRound.getMissCount()==0) // To avoid divide by 0 error when no misses are present
			Score =  hangman.hangmanRound.getHitCount();
		else
			Score = (double) hangman.hangmanRound.getHitCount()/hangman.hangmanRound.getMissCount();
		return Score;
	}

	/** KeyBoardHandler disables the button pressed and passes the button-text to nextTry()
	 *  Invokes getScoreString() to update scoreLabel */
	class KeyboardButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Button b = (Button)event.getSource();
			b.setDisable(true);
			int index = hangman.nextTry(b.getText());
			hangmanView.smileyButton.setGraphic(hangmanView.smileyImageViews[index]);
			if (index == GameView.SMILEY_INDEX || index == GameView.SADLY_INDEX ) {

				//Writing the score to file
				String score = "0"+","+hangman.hangmanRound.getPuzzleWord()+","+(30-Integer.parseInt(GameView.wordTimer.timerButton.getText()))+","+getScoreDouble();
				wordNerdModel.writeScore(score);
				GameView.wordTimer.timeline.stop();
				hangman.hangmanRound.setIsRoundComplete(true);
			}
			String scoreString = hangman.getScoreString();
			hangmanView.scoreLabel.setText(scoreString);
		}
	}
}
