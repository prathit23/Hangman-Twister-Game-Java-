/**
 * Prathit Pannase: ppannase
 */
package Game;

import java.util.Random;

public class Hangman extends Game{
	static final int MIN_WORD_LENGTH = 5; //minimum length of puzzle word
	static final int MAX_WORD_LENGTH = 10; //maximum length of puzzle word
	static final int HANGMAN_TRIALS = 10;  // max number of trials in a game
	static final int HANGMAN_GAME_TIME = 30; // max time in seconds for one round of game
	
	HangmanRound hangmanRound;
	
	/** setupRound() is a replacement of findPuzzleWord() in HW1. 
	 * It returns a new HangmanRound instance with puzzleWord initialized randomly drawn from wordsFromFile.
	* The puzzleWord must be a word between HANGMAN_MIN_WORD_LENGTH and HANGMAN_MAX_WORD_LEGTH. 
	* Other properties of Hangmanround are also initialized here. 
	*/
	@Override
	HangmanRound setupRound() {
		//write your code here
		hangmanRound = new HangmanRound();

		String[] words =  WordNerdModel.wordsFromFile;
		int index ;
		Random random = new Random();
		// Below do loop will run till we get a word of length between 5 & 10
		do{
			index = random.nextInt(words.length);
		}while (words[index].length()<MIN_WORD_LENGTH || words[index].length()>MAX_WORD_LENGTH);

		hangmanRound.setPuzzleWord(words[index]);
//		System.out.println("Puzzle Word: "+words[index]);
		hangmanRound.setClueWord(makeAClue(hangmanRound.getPuzzleWord()));
		hangmanRound.setIsRoundComplete(false);

		return hangmanRound;
	}
	
	
	/** Returns a clue that has at least half the number of letters in puzzleWord replaced with dashes.
	* The replacement should stop as soon as number of dashes equals or exceeds 50% of total word length. 
	* Note that repeating letters will need to be replaced together.
	* For example, in 'apple', if replacing p, then both 'p's need to be replaced to make it a--le */
	@Override
	String makeAClue(String puzzleWord) {
		//write your code here
		char [] chars = puzzleWord.toCharArray();

		// Runs the while loop till at least half of the letters are replaced by dashes
		while (countDashes(puzzleWord)<puzzleWord.length()/2){ // It should be less than length/2, if we keep as equal to then more than 50% of characters will get replaced by -
			Random random = new Random();
			int replaceIndex = random.nextInt(chars.length); // Gets a random integer (index) to replace character

			String replaceChar = ""+chars[replaceIndex];
			puzzleWord= puzzleWord.replaceAll(replaceChar,"-"); // Will replace all characters of replaceChar in the puzzleWord
		}
		return puzzleWord;

	}

	/** countDashes() returns the number of dashes in a clue String */ 
	int countDashes(String word) {
		//write your code here
		int count =0;

		// Counting dashes
		for (char character:word.toCharArray())
			if (character=='-'||character=='_')
				count++;

		return count;
	}
	
	/** getScoreString() returns a formatted String with calculated score to be displayed after
	 * each trial in Hangman. See the handout and the video clips for specific format of the string. */
	@Override
	String getScoreString() {
		//write your code here
		float Score;
		if(hangmanRound.getMissCount()==0) // To avoid divide by 0 error when no misses are present
			Score =  hangmanRound.getHitCount();

		else
			Score = (float) hangmanRound.getHitCount()/hangmanRound.getMissCount();

		return "Hit: "+hangmanRound.getHitCount()+" Miss: "+hangmanRound.getMissCount()+" Score: "+Score;

	}

	/** nextTry() takes next guess and updates hitCount, missCount, and clueWord in hangmanRound. 
	* Returns INDEX for one of the images defined in GameView (SMILEY_INDEX, THUMBS_UP_INDEX...etc. 
	* The key change from HW1 is that because the keyboardButtons will be disabled after the player clicks on them, 
	* there is no need to track the previous guesses made in userInputs*/
	@Override
	int nextTry(String guess) {  
		//write your code here

		String clueWord = hangmanRound.getClueWord();
		String puzzleWord = hangmanRound.getPuzzleWord();

		 if(!puzzleWord.contains(guess)) {
			hangmanRound.setMissCount(hangmanRound.getMissCount()+1);
			 // We take > 10 since if the user enters a letter that's part of the clue or already entered then trial no.10 should continue
			 if((hangmanRound.getHitCount()+hangmanRound.getMissCount())==HANGMAN_TRIALS)
			 {
				 hangmanRound.setIsRoundComplete(true);
				 return GameView.SADLY_INDEX;
			 }
			return GameView.THUMBS_DOWN_INDEX;
		}

		else if(puzzleWord.contains(guess)) {

			for (int i =0;i<puzzleWord.length();i++) // Replace the dash with the correctly guessed character
				if(puzzleWord.charAt(i)==guess.charAt(0))
					clueWord = clueWord.substring(0,i)+guess+clueWord.substring(i+1);

			//Set updated clue word and hit count
			hangmanRound.setClueWord(clueWord);
			hangmanRound.setHitCount(hangmanRound.getHitCount()+1);

			 //If all words guessed return smiley face index
			 if(countDashes(hangmanRound.getClueWord())==0){
				 hangmanRound.setIsRoundComplete(true);
				 return GameView.SMILEY_INDEX;
			 }
			 //If all words are not guessed but number of tries reached maxium return sadly face index
			 if((hangmanRound.getHitCount()+hangmanRound.getMissCount())==HANGMAN_TRIALS)
			 {
				 hangmanRound.setIsRoundComplete(true);
				 return GameView.SADLY_INDEX;
			 }
			return GameView.THUMBS_UP_INDEX;
		}
		return -1; // -1 for cases unexpected cases
	}
}
