/**
 * Prathit Pannase: ppannase
 */
package Game;

import javafx.beans.property.*;

public class Score {

    // Java Beans Class to keep Score
    private IntegerProperty gameId = new SimpleIntegerProperty();
    private IntegerProperty timeStamp = new SimpleIntegerProperty();
    private StringProperty puzzleWord = new SimpleStringProperty();
    private FloatProperty score = new SimpleFloatProperty();

    public Score() {
        // Setting values to default
        this.gameId.set(0);
        this.timeStamp.set(0);
        this.puzzleWord.set("");
        this.score.set(0.0f);
    }

    public Score(int gameId, String puzzleWord,  int timeStamp,float score) {

        // Setting constructor values
        this.gameId.set(gameId);
        this.timeStamp.set(timeStamp);
        this.puzzleWord.set(puzzleWord);
        this.score.set(score);
    }

    // Getter and Setters for all variables
    public int getGameId() {
        return gameId.get();
    }

    public IntegerProperty gameIdProperty() {
        return gameId;
    }

    public final void setGameId(int gameId) {
        this.gameId.set(gameId);
    }

    public final int getTimeStamp() {
        return timeStamp.get();
    }

    public final IntegerProperty timeStampProperty() {
        return timeStamp;
    }

    public final void setTimeStamp(int timeStamp) {
        this.timeStamp.set(timeStamp);
    }

    public final String getPuzzleWord() {
        return puzzleWord.get();
    }

    public final StringProperty puzzleWordProperty() {
        return puzzleWord;
    }

    public final void setPuzzleWord(String puzzleWord) {
        this.puzzleWord.set(puzzleWord);
    }

    public final float getScore() {
        return score.get();
    }

    public final FloatProperty scoreProperty() {
        return score;
    }

    public final void setScore(float score) {
        this.score.set(score);
    }
}
