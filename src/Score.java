import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * a class to record scores for SnakeGame
 */
public class Score {

    private String n; //name
    private int s; //score
    private static final int numHighScores = 5; //number of high scores that can be listed
    private final static File file = new File("highScores.txt"); //high score file
    private static List<Score> highScores; //current list of high scores (initially brought in from text file)

    public Score(String name, int score) { //constructor
        n = name;
        s = score;
    }

    /**
     * getter for name
     * @return
     */
    public String getName() {
        return n;
    }

    /**
     * setter for name
     * @param newName
     */
    public void setName(String newName) {
        n = newName;
    }

    /**
     * getter for score
     * @return
     */
    public int getScore() {
        return s;
    }

    /**
     * setter for score
     * @param newScore
     */
    public void setScore(int newScore) {
        s = newScore;
    }

    /**
     * toString for Score
     * @return name and score
     */
    public String toString() {
        return n + ": " + s;
    }

    /**
     * compares names and scores to determine if it is the same score
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if(o instanceof Score) {
            Score compare = (Score) o;
            return n.equals(compare.n) && s == compare.s;
        }
        return false;
    }

    /**
     * gets the scores from the high score file
     */
    public static void initializeScores() {
        highScores = new ArrayList<>();
        try {
            Scanner inFile = new Scanner(file);
            while(inFile.hasNextLine()) {
                String line = inFile.nextLine();
                String name = line.substring(0, line.lastIndexOf(' '));
                String score = line.substring(line.lastIndexOf(' ') + 1);
                highScores.add(new Score(name, Integer.parseInt(score)));
            }
            inFile.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("Not Able to Retrieve High Scores (File Not Found).");
        }
    }

    /**
     * @return high score list
     */
    public static List<Score> getHighScores() {
        return highScores;
    }

    /**
     * @return String representation of high scores (for text based game)
     */
    public static String strHighScores() {
        if(highScores.size() == 0)  return "No High Scores";
        if(highScores.size() == 1)  return "High Score: " + highScores.get(0).n + ", " + highScores.get(0).s;

        String result = "High Scores\n";

        for(int i = 0; i < highScores.size(); i++) {
            result += String.valueOf(i + 1) + ". ";
            String formattedName = String.format("%-" + String.valueOf(getLongestLen() + 1) + "s", highScores.get(i).n + ":");
            result += formattedName + " " + highScores.get(i).s;
            if(i != highScores.size() - 1)  result += "\n";
        }

        return result;
    }

    /**
     * @return length of the longest name in the high scores list
     */
    private static int getLongestLen() {
        int max = 0;

        for(Score score : highScores) {
            max = Math.max(max, score.n.length());
        }

        return max;
    }

    /**
     * sees if it is possible to add high score
     * @param newScore
     * @return
     */
    public static boolean canAddHighScore(int newScore) {
        if(highScores.size() < numHighScores)  return true;
        for(int i = 0; i < highScores.size(); i++) {
            if(newScore > highScores.get(i).s)  return true;
        }
        return false;
    }

    /**
     * adds a high score to the high score list if high enough
     * @param newScore
     */
    public static void addScore(Score newScore) {
        if(!canAddHighScore(newScore.s))  return;

        //adds score if no scores (and more than 0 high scores being stored)
        if(highScores.size() == 0 && numHighScores > 0) {
            highScores.add(newScore);
            writeScores();
            return;
        }

        //finds position to add high score
        for(int i = 0; i < highScores.size(); i++) {
            if(newScore.s > highScores.get(i).s) {
                highScores.add(i, newScore);
                break;
            }
        }

        //adds in new score if it is last
        if(!highScores.contains(newScore) && highScores.size() < numHighScores) {
            highScores.add(newScore);
        }

        //removes extra scores if necessary
        while(highScores.size() > numHighScores)    highScores.remove(highScores.size() - 1);

        writeScores();
    }

    /**
     * writes high scores to the high score file
     */
    public static void writeScores() {
        try {
            PrintWriter outFile = new PrintWriter(file);
            for(Score score : highScores) {
                outFile.println(score.n + " " + score.s);
            }
            outFile.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("Not Able to Write to High Scores File");
        }
    }

    /**
     * clears the scores file
     */
    public static void resetScores() {
        try {
            PrintWriter outFile = new PrintWriter(file);
            outFile.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("Not Able to Write to High Scores File");
        }
    }

}
