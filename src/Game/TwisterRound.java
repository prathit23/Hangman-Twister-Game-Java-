/**
 * Prathit Pannase: ppannase
 */
package Game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collections;
import java.util.List;

public class TwisterRound extends GameRound {

    // Methods with getters and setters for ObservableList
    private ObservableList<String> solutionWordsList;
    private ObservableList<ObservableList<String>> submittedListsByWordLength = FXCollections.observableArrayList();
    private ObservableList<ObservableList<String>> solutionListsByWordLength = FXCollections.observableArrayList();

    public TwisterRound() {
        // Initializing all index of the list of list ObservableList
        for(int i=0;i<=Twister.TWISTER_MAX_WORD_LENGTH-Twister.TWISTER_MIN_WORD_LENGTH;i++)
        {
            solutionListsByWordLength.add(FXCollections.observableArrayList());
            submittedListsByWordLength.add(FXCollections.observableArrayList());
        }
    }

    public void setSolutionWordsList(List<String> solutionWordsList) {
        this.solutionWordsList = FXCollections.observableArrayList(solutionWordsList) ;
    }

    public List<String> getSolutionWordsList() {
        return solutionWordsList;
    }

    public ObservableList<String> solutionWordsListProperty(){
        return solutionWordsList;
    }

    public void setSubmittedListsByWordLength(String word) {
        // Since the index for 3 letter word is 0 we subtract word's length with the minimum word length and then sort the list
        this.submittedListsByWordLength.get(word.length()-Twister.TWISTER_MIN_WORD_LENGTH).add(word);
        Collections.sort(submittedListsByWordLength.get(word.length()-Twister.TWISTER_MIN_WORD_LENGTH));
    }

    public ObservableList<String> getSubmittedListsByWordLength(int letterCount) {
        return submittedListsByWordLength.get(letterCount);
    }

    public ObservableList<ObservableList<String>> submittedListsByWordLengthProperty(){
        return submittedListsByWordLength;
    }

    public void setSolutionListsByWordLength(String word) {

        // Since the index for 3 letter word is 0 we subtract word's length with the minimum word length and then sort the list
        this.solutionListsByWordLength.get(word.length()-Twister.TWISTER_MIN_WORD_LENGTH).add(word);
        Collections.sort(solutionListsByWordLength.get(word.length()-Twister.TWISTER_MIN_WORD_LENGTH));
    }

    public ObservableList<String> getSolutionListsByWordLength(int letterCount) {
        return solutionListsByWordLength.get(letterCount);
    }

    public ObservableList<ObservableList<String>> solutionListsByWordLengthProperty(){
        return solutionListsByWordLength;
    }

    // Following 2 functions are mentioned in description but not in UML
    public ObservableList<ObservableList<String>> getSolutionListsByWordLength(){
        return  this.solutionListsByWordLength;
    }

    public ObservableList<ObservableList<String>> getSubmittedListsByWordLength(){
        return  this.submittedListsByWordLength;
    }
}
