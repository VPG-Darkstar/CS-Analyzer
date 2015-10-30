/**
 * Game class contains information about CS in past games.
 */

public class Game implements java.io.Serializable{
    //Variables for game info
    private String lane;        //The lane of the game
    private int cs5;            //CS at 5
    private int cs10;           //CS at 10
    private int cs13;           //CS at 13
    private char grade;         //CSing grade
    
    /**
     * No-arg constructor creates a default game
     */
    
    public Game(){
        lane = "";
        cs5 = 0;
        cs10 = 0;
        cs13 = 0;
        grade = 'Z';
    }
    
    /**
     * Constructor creates a game with the appropriate stats.     
     * @param l The lane.
     * @param c5 The cs at 5.
     * @param c10 The cs at 10.
     * @param c13 The cs at 13.
     * @param g The grade.
     */
    
    public Game(String l, int c5, int c10, int c13, char g){
        lane = l;
        cs5 = c5;
        cs10 = c10;
        cs13 = c13;
        grade = g;
    }
    
    /**
     * Accessor method for the lane.
     * @return The lane.
     */
    
    public String getLane(){
        return lane;
    }
    
    /**
     * Accessor method for the cs at 5.
     * @return The cs at 5.
     */
    
    public int getCS5(){
        return cs5;
    }
    
    /**
     * Accessor method for the cs at 10.
     * @return The cs at 10
     */
    
    public int getCS10(){
        return cs10;
    }
    
    /**
     * Accessor method for the cs at 13.
     * @return The cs at 13.
     */
    
    public int getCS13(){
        return cs13;
    }
    
    /**
     * Accessor method for the grade.
     * @return The grade.
     */
    
    public char getGrade(){
        return grade;
    }
    
    /**
     * toString method for the data.
     */
    
    @Override
    public String toString(){
        return lane + "\t" + cs5 + "\t" + cs10 + "\t" + cs13 + "\t"+ grade + "\n";
    }
}
