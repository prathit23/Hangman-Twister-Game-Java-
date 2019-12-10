/**
 * Prathit Pannase: ppannase
 */

package Game;

import java.util.*;
import java.util.stream.Collectors;

public class Twister extends Game
{
    final static int SOLUTION_LIST_COUNT =5;
    final static int TWISTER_MAX_WORD_LENGTH = 7;
    final static int TWISTER_MIN_WORD_LENGTH =3;
    final static int NEW_WORD_BUTTON_INDEX = 0;
    final static int TWIST_BUTTON_INDEX =1;
    final static int CLEAR_BUTTON_INDEX = 2;
    final static int SUBMIT_BUTTON_INDEX = 3;
    final static int CLUE_BUTTON_SIZE = 75;
    final static int TWISTER_GAME_TIME = 120;
    final static int MIN_SOLUTION_WORDCOUNT = 10;
    TwisterRound twisterRound;

    // Function used to find solution of the puzzleword
    private Set<String> findSolutions(String puzzleWord, String[] words){

        char[] puzzleWordChar = puzzleWord.toCharArray();
        Map<Character, Integer> puzzleWordMap = new HashMap<>();
        Set<String> solutionSet = new HashSet<>();

        // Add every letter to a map
        for (char character:puzzleWordChar)
            puzzleWordMap.merge(character,1,(x,y)->x+y);


        for(String word: words){

            // If word length is within the given bounds then proceed
            if(word.length()>=TWISTER_MIN_WORD_LENGTH && word.length()<= TWISTER_MAX_WORD_LENGTH){
                Map<Character, Integer> tempMap = // Understood logic from GeekForGeek
                        puzzleWordMap.entrySet()
                                .stream()
                                .collect(Collectors.toMap(Map.Entry::getKey,
                                        Map.Entry::getValue));

                boolean subWord = true;
                for(char character:word.toCharArray()){

                    // If letter not present in puzzleword, do not add to set
                    if(!tempMap.containsKey(character)){
                        subWord = false;
                        break;
                    }
                    else{
                        // If letter present more number of times than in puzzleword, do not add to set
                        if(tempMap.get(character)<=0){
                            subWord = false;
                            break;
                        }
                        tempMap.replace(character,tempMap.get(character)-1);
                    }
                }
                // If the word is a solution add to the set
                if(subWord)
                    solutionSet.add(word);
            }
        }
        return solutionSet;
    }

    TwisterRound setupRound(){
        String[] words =  WordNerdModel.wordsFromFile;
        String puzzleWord="";
        Set<String>  solutionSet;
        twisterRound = new TwisterRound();

        int index ;
        Random random = new Random();
        // Below do loop will run till we get a word of length between 5 & 10 and the solutionSet will be >= MIN_SOLUTION_WORDCOUNT
        do{
            do{
                index = random.nextInt(words.length);
            }while (words[index].length()<TWISTER_MIN_WORD_LENGTH || words[index].length()>TWISTER_MAX_WORD_LENGTH);

             puzzleWord = words[index];
            solutionSet = findSolutions(puzzleWord,words);

        }while (solutionSet.size()<MIN_SOLUTION_WORDCOUNT);

        List<String> solutionList = new ArrayList<>();

        //Converting Set to List
        for(String word: solutionSet)
            solutionList.add(word);

        // Set solution word list
        twisterRound.setSolutionWordsList(solutionList);

        // Add word to SolutionListsByWordLength
        for(String word: solutionList)
            twisterRound.setSolutionListsByWordLength(word);

        // Set up round
        twisterRound.setPuzzleWord(puzzleWord);
        twisterRound.setClueWord(makeAClue(twisterRound.getPuzzleWord()));
        twisterRound.setIsRoundComplete(false);

        //System.out.println(twisterRound.getSolutionWordsList());

        return twisterRound;
    }

    String makeAClue(String puzzleWord){


        List<Character> charList = new ArrayList<>();

        for( char character: puzzleWord.toCharArray())
            charList.add(character);

        // Collections.shuffle will shuffle the char array passed
        Collections.shuffle(charList);

        String clueWord = "";
        // Convert charArray back to String clueWord
        for(char character: charList)
            clueWord +=character;

        return clueWord;

    }

    int nextTry(String guess){

        // If length is less than minimum word length then return
        if(guess.length()<TWISTER_MIN_WORD_LENGTH)
            return GameView.THUMBS_DOWN_INDEX;


        List<String> solutionList = twisterRound.getSolutionWordsList();
        // If word is not in solution list
        if(!solutionList.contains(guess))
            return GameView.THUMBS_DOWN_INDEX;

        List<String> solutionListWordLength = twisterRound.getSolutionListsByWordLength(guess.length()-TWISTER_MIN_WORD_LENGTH);
        List<String> submittedListWordLength = twisterRound.getSubmittedListsByWordLength(guess.length()-TWISTER_MIN_WORD_LENGTH);

        // If word already submitted
        if(submittedListWordLength.contains(guess))
            return GameView.REPEAT_INDEX;

        // If word submitted is correct
        if(solutionListWordLength.contains(guess)){
            twisterRound.setSubmittedListsByWordLength(guess);

            int totaCount = twisterRound.getSolutionWordsList().size();

            int listCount=0;
            for(List list: twisterRound.getSubmittedListsByWordLength())
                listCount+=list.size();

            // If all words are covered return smiley index
            if(listCount==totaCount)
                return GameView.SMILEY_INDEX;

            return GameView.THUMBS_UP_INDEX;
        }
        // If no cases are hit return thumbs down
        return GameView.THUMBS_DOWN_INDEX;
    }

    String getScoreString(){
        int totalCount = twisterRound.getSolutionWordsList().size();

        int listCount=0;
        for(List list: twisterRound.getSubmittedListsByWordLength())
            listCount+=list.size();

        // If start of game print only total words to find
        if(listCount==0)
            return "Twist to find "+totalCount+" words!";
        return "Twist to find "+(totalCount-listCount)+" of "+totalCount+" words";
    }
}
