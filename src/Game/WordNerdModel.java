/**
 * Prathit Pannase: ppannase
 */
package Game;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.Scanner;

public class WordNerdModel {

    static String [] wordsFromFile;
    // For mac you have to use '/' and windows '\'. Hence used System.getProperty("file.separator") to make it generic
    static final String WORDS_FILE_NAME = "data"+System.getProperty("file.separator")+"wordsfile.txt";
    static final String SCORE_FILE_NAME = "data"+System.getProperty("file.separator")+"scores.csv";
    ObservableList<Score> scoreList  = FXCollections.observableArrayList();;

    static boolean readWordsFile(String wordsFileName){


        // Reading the file
        StringBuilder sb = new StringBuilder();
        try(Scanner sc = new Scanner(new File(wordsFileName))) {
            while (sc.hasNext()) {
                String  word = sc.next();

                for(char c: word.toCharArray())
                    if(!Character.isLetter(c)) //Makes Sure every word consists of Letters
                        throw new InvalidWordSourceException("Check word source format");

                sb.append(word);
                sb.append("\n");
            }
            //Checks if the file is badFile
            if (sb.length() == 0)
                throw new InvalidWordSourceException("Check word source format");
        }

        catch (InvalidWordSourceException e1){
            // Show Alert and return false;
            e1.showAlert();
            return false;
        }
        catch (FileNotFoundException e){
            System.out.println("File Location is wrong");
            return false;
        }
        wordsFromFile = sb.toString().split("\n");

        return true;
    }

    void writeScore(String scoreString){
        // Function to Write Score to File
       try(FileWriter fw = new FileWriter(new File(SCORE_FILE_NAME),true)){

           BufferedWriter bw = new BufferedWriter(fw);
           bw.write(scoreString);
           bw.newLine();
           bw.close();
       }
       catch (IOException e){
           System.out.println("IOException");
       }

    }

    void readScore(){
        // Reads Score
        try(Scanner sc = new Scanner(new File(SCORE_FILE_NAME))){
            while (sc.hasNextLine()){
                String [] scoreString = sc.nextLine().split(",");
                scoreList.add(new Score(Integer.parseInt(scoreString[0]), scoreString[1],Integer.parseInt(scoreString[2]),Float.parseFloat(scoreString[3])));
            }
        }
        catch (FileNotFoundException e){
            throw new InvalidWordSourceException("Check word source format!");
        }
    }

}
