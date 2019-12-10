/**
 * Prathit Pannase: ppannase
 */

package Game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.List;

public class TwisterController extends WordNerdController {


    TwisterView twisterView;
    Twister twister;
    WordNerdModel wordNerdModel = new WordNerdModel();
    @Override
    void startController() {

        // Start the round
        twister = new Twister();
        twisterView = new TwisterView();
        twister.twisterRound = twister.setupRound();
        setupBindings();
    }

    @Override
    void setupBindings() {
        // Refresh and set the score
        twisterView.refreshGameRoundView(twister.twisterRound);
        twisterView.topMessageText.setText(twister.getScoreString());
        // Set button actions
        twisterView.playButtons[Twister.NEW_WORD_BUTTON_INDEX].setOnAction(new NewButtonHandler());
        twisterView.playButtons[Twister.TWIST_BUTTON_INDEX].setOnAction(new TwistButtonHandler());
        twisterView.playButtons[Twister.CLEAR_BUTTON_INDEX].setOnAction(new ClearButtonHandler());
        twisterView.playButtons[Twister.SUBMIT_BUTTON_INDEX].setOnAction(new SubmitButtonHandler());

        VBox lowerPanel = new VBox();
        lowerPanel.getChildren().add(twisterView.bottomGrid);
        lowerPanel.getChildren().add(WordNerd.exitButton);
        lowerPanel.setAlignment(Pos.CENTER);

        WordNerd.root.setTop(twisterView.topMessageText);
        WordNerd.root.setCenter(twisterView.topGrid);
        WordNerd.root.setBottom(lowerPanel);


        // Set up clue and answer buttons set on action
        for(int i=0;i< twisterView.clueButtons.length;i++){
            twisterView.clueButtons[i].setOnAction(new ClueButtonHandler(i));
        }
        for(int i=0;i< twisterView.answerButtons.length;i++){
            twisterView.answerButtons[i].setOnAction(new AnswerButtonHandler(i));
        }

        // Remove rows where there are no words in solution list
        for(int i=0;i<twisterView.wordScoreLabels.length;i++){
            twisterView.wordScoreLabels[i].setText(twister.twisterRound.getSubmittedListsByWordLength(i).size()+"/"+
                    twister.twisterRound.getSolutionListsByWordLength(i).size());
            if(twister.twisterRound.getSolutionListsByWordLength(i).size()==0){
                twisterView.bottomGrid.getChildren().remove(twisterView.solutionListViews[i]);
                twisterView.bottomGrid.getChildren().remove(twisterView.wordLengthLabels[i]);
                twisterView.bottomGrid.getChildren().remove(twisterView.wordScoreLabels[i]);
            }
        }
        //Bind a listener to twister's clueWordProperty, so that whenever it changes, the clueLabels should also
        //change in twisterView
        twister.twisterRound.clueWordProperty().addListener((observable, oldValue, newValue) -> {
            for (int i = 0; i < twister.twisterRound.getClueWord().length(); i++) {
                twisterView.clueButtons[i].setText(String.format("%s", newValue.charAt(i)));
            }
        });

        // Setting solutionListViews to the submitted word lists
        for(int i=0;i<twisterView.solutionListViews.length;i++){
            twisterView.solutionListViews[i].setItems(twister.twisterRound.getSubmittedListsByWordLength(i));
        }

        //When timer runs out, set smiley to sadly, isRoundComplete to true
        GameView.wordTimer.timeline.setOnFinished(event -> {
            twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[GameView.SADLY_INDEX]);
            String score = "1"+","+twister.twisterRound.getPuzzleWord()+","+(120-Integer.parseInt(GameView.wordTimer.timerButton.getText()))+","+getScoreDouble();
            wordNerdModel.writeScore(score);
            twister.twisterRound.setIsRoundComplete(true);
        });


        // Disable clueGrid and the playbuttons except for new word when the round is over
        twisterView.clueGrid.disableProperty().bind(twister.twisterRound.isRoundCompleteProperty());
        for(int i=1;i<twisterView.playButtons.length;i++)
            twisterView.playButtons[i].disableProperty().bind(twister.twisterRound.isRoundCompleteProperty());


    }

    private double getScoreDouble(){
        int totalCount = twister.twisterRound.getSolutionWordsList().size();

        int listCount=0;
        for(List list: twister.twisterRound.getSubmittedListsByWordLength())
            listCount+=list.size();

        // If start of game print only total words to find
        if(listCount==0)
            return 0.0;
        return (double) listCount/totalCount;
    }

    class TwistButtonHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {
            // Replacing all the spaces with nothing, calling make a clue function and then appending the spaces back to the string in the end
            String wordToTwist = twister.makeAClue(twister.twisterRound.getClueWord());
            String finalWord=wordToTwist.replaceAll(" ","");
            int spaceCount = wordToTwist.length()-finalWord.length(); // Counts number of spaces
            finalWord=twister.makeAClue(finalWord);
            for(int i=0;i<spaceCount;i++)
                finalWord+=" ";

            twister.twisterRound.setClueWord(finalWord);
        }
    }

    class ClueButtonHandler implements EventHandler<ActionEvent>{

        // We use a constructor so that we get the index of clue button pressed
        private int index;
        private ClueButtonHandler(int index) {
            this.index = index;
        }

        @Override
        public void handle(ActionEvent actionEvent) {

            Button b = (Button)actionEvent.getSource();
            String text = b.getText();

            // If the button is not empty then perform the action
            if(!text.equals(" ")){
                // Set clue word which automatically updates the binded clue button
                String clueWord = twister.twisterRound.getClueWord();
                twister.twisterRound.setClueWord(clueWord.substring(0,index)+" "+clueWord.substring(index+1));

                // Find empty space in answer button and place the letter
                for(Button answerButton : twisterView.answerButtons){
                    if(answerButton.getText().equals("")){
                        answerButton.setText(text);
                        break;
                    }
                }
            }
        }
    }

    class AnswerButtonHandler implements EventHandler<ActionEvent>{

        // We use a constructor so that we get the index of answer button pressed
        private int index;
        private AnswerButtonHandler(int index) {
            this.index = index;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            Button b = (Button)actionEvent.getSource();
            String text = b.getText();

            // If the button is not empty then perform the action
            if(!text.trim().equals("")){
                b.setText("");

                // Shifting letters left for answer buttons
                for(int i =index; i< twisterView.answerButtons.length-1;i++){
                    twisterView.answerButtons[i].setText(twisterView.answerButtons[i+1].getText());
                }
                twisterView.answerButtons[twisterView.answerButtons.length-1].setText("");

                // Find the first available space in clue button and placing the letter
                String clueWord = twister.twisterRound.getClueWord();
                int indexClue = clueWord.indexOf(" ");
                twister.twisterRound.setClueWord(clueWord.substring(0,indexClue)+text+clueWord.substring(indexClue+1));

            }
        }
    }

    class ClearButtonHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {

            // Reads the answer button and updates the clue word which is binded to clue buttons breaks when answer button is empty
            for(int i=0;i<twisterView.answerButtons.length;i++){
                if(twisterView.answerButtons[i].getText().equals(""))
                    break;
                String text =  twisterView.answerButtons[i].getText();
                String clueWord = twister.twisterRound.getClueWord();
                int indexClue = clueWord.indexOf(" ");
                twister.twisterRound.setClueWord(clueWord.substring(0,indexClue)+text+clueWord.substring(indexClue+1));
                twisterView.answerButtons[i].setText(""); // Setting the answer button back to ""

            }

        }
    }

    class SubmitButtonHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {

            String answerText ="";

            // Get the text from answer buttons
            for(Button answerButton: twisterView.answerButtons){
                if(answerButton.getText().equals(""))
                    break;
                answerText+=answerButton.getText();
            }

            // Call nextTry method
            int index = twister.nextTry(answerText);

            twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[index]); // Sets the graphic button

            // If the game is won or lost stop the timer and set round complete to true
            if (index == GameView.SMILEY_INDEX || index == GameView.SADLY_INDEX ) {

                twisterView.topMessageText.setText(twister.getScoreString()); // Set Top Score

                twisterView.wordScoreLabels[answerText.length()-Twister.TWISTER_MIN_WORD_LENGTH]
                        .setText(twister.twisterRound.getSubmittedListsByWordLength(answerText.length()-Twister.TWISTER_MIN_WORD_LENGTH).size()
                                +"/"+twister.twisterRound.getSolutionListsByWordLength(answerText.length()-Twister.TWISTER_MIN_WORD_LENGTH).size());

                String score = "1"+","+twister.twisterRound.getPuzzleWord()+","+(120-Integer.parseInt(GameView.wordTimer.timerButton.getText()))+","+getScoreDouble();
                wordNerdModel.writeScore(score);
                GameView.wordTimer.timeline.stop();
                twister.twisterRound.setIsRoundComplete(true);

            }

            else if(index == GameView.THUMBS_UP_INDEX){
                twisterView.topMessageText.setText(twister.getScoreString());
                // Update Score Label for that word length row
                twisterView.wordScoreLabels[answerText.length()-Twister.TWISTER_MIN_WORD_LENGTH]
                        .setText(twister.twisterRound.getSubmittedListsByWordLength(answerText.length()-Twister.TWISTER_MIN_WORD_LENGTH).size()
                                +"/"+twister.twisterRound.getSolutionListsByWordLength(answerText.length()-Twister.TWISTER_MIN_WORD_LENGTH).size());

                twister.twisterRound.setClueWord(twister.makeAClue(twister.twisterRound.getPuzzleWord())); // Reset clue word

                // Clear Answer Buttons
                for(int i =0; i< twisterView.answerButtons.length;i++)
                    twisterView.answerButtons[i].setText("");
            }
        }
    }

    class NewButtonHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent actionEvent) {

            // Resetting the timer and setting up new round
            GameView.wordTimer.timeline.stop();
            twister.twisterRound = twister.setupRound();
            twisterView = new TwisterView();
            twisterView.refreshGameRoundView(twister.twisterRound);
            GameView.wordTimer.restart(Twister.TWISTER_GAME_TIME);
            setupBindings();
        }
    }
}
