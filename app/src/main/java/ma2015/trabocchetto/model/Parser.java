package ma2015.trabocchetto.model;

import java.util.ArrayList;


/**
 * This class represents a Parser. It receives an input String (a complete configuration of the game)
 * and converts/stores in different attributes all the elements composing this game's configuration.
 */
public class Parser {

    private static String invalidNumOfPlayersError = "Invalid number of players."; //Error message
    private static String invalidMovingPlayerError = "Invalid moving player.";
    private static String invalidBarPositionError = "Invalid bar position.(Allowed only positions 0,1,2 (Inner, central, outer).";

    private String inputString; //String representing the given game configuration
    private char[] string; //Array of chars representing the given game configuration

    private int players; //number of the initial players
    private int movingPlayer;
    private int beadsGrid[][]=new int[7][7]; //matrix containing all the players' beads
    int hBarPosition[]=new int[7]; //Array containing all the initial horizontal bars position (0:inner, 1:central, 2:outer)
    int vBarPosition[]=new int[7];
    private ArrayList<Move> moves=new ArrayList<Move>(); //ArrayList containing all the moves extracted from the given game config (inputString)


    /**
     * Parser Constructor: it receives in input a string that represents a complete configuration of the game,
     * and converts/stores in different class attributes all the elements composing this game's configuration.
     * @param s : string containing a game configuration
     * @throws Exception : in case of invalid arguments composing the input String, the constructor throws an exception to the caller
     */
    public Parser(String s) throws Exception{
        this.inputString=s;
        string=inputString.toCharArray(); //conversion from String to an Array of Chars

        int k=0; //counter that I'll use to scan the String given in input

        //Extracting gameConfiguration data from 'string':
        players=string[k]-48; //conversion from char to int. Steps(supposing 4Players): players = string[k]-48 = (int)"4"-48 = 52-48 = 4
        k++;
        if(players<2 || players>4){
            IllegalArgumentException e=new IllegalArgumentException(invalidNumOfPlayersError);
            System.out.println("error:"+ e.getMessage());
            throw e;
        }

        movingPlayer=string[k]-48;
        k++;
        if(movingPlayer<1 || movingPlayer>players){
            IllegalArgumentException e=new IllegalArgumentException(invalidMovingPlayerError);
            System.out.println("error:"+ e.getMessage());
            throw e;
        }

        for(int i=0;i<7;i++){
            if(string[k]-48<0 || string[k]-48 >2){
                IllegalArgumentException e=new IllegalArgumentException(invalidBarPositionError);
                System.out.println("error:"+ e.getMessage());
                throw e;
            }else{
                hBarPosition[i]=string[k]-48;
            }

            k++;
        }

        for(int i=0;i<7;i++){
            if(string[k]-48<0 || string[k]-48 >2){
                IllegalArgumentException e=new IllegalArgumentException(invalidBarPositionError);
                System.out.println("error:"+ e.getMessage());
                throw e;
            }else {
                vBarPosition[i] = string[k] - 48;
                k++;
            }
        }

        for(int i=0;i<7;i++){
            for(int j=0;j<7;j++){
                beadsGrid[i][j]= string[k]-48;
                k++;
            }
        }

        char orientation;
        int bar;
        char direction;

        while(k<string.length){
            orientation=string[k++];
            bar=string[k++]-48;
            direction=string[k++];
            moves.add(new Move(orientation,bar,direction));
        }
    }






    /**
     * Getter method, that returns the given game's configuration.
     * @return String, representing the game's configuration
     */
    public String getInputString() {
        return inputString;
    }

    /**
     * Getter method, that returns the given game's configuration converted in an array of chars.
     * @return array of chars, containing the complete game's configuration given in input.
     */
    public char[] getString() {
        return string;
    }

    /**
     * Getter method
     * @return int, gameConfiguration's number of the players
     */
    public int getPlayers() {
        return players;
    }

    /**
     * Getter method
     * @return int, gameConfiguration's movingPlayer (that has to move in the incoming turn)
     */
    public int getMovingPlayer() {
        return movingPlayer;
    }

    public int[][] getBeadsGrid() {
        return beadsGrid;
    }

    /**
     * Getter method
     * @return array of int, gameConfiguration's horizontalBarsPosition (for each bar position: 0:inner, 1:central, 2:outer)
     */
    public int[] gethBarPosition() {
        return hBarPosition;
    }

    /**
     * Getter method
     * @return array of int, gameConfiguration's verticalBarsPosition (for each bar position: 0:inner, 1:central, 2:outer)
     */
    public int[] getvBarPosition() {
        return vBarPosition;
    }

    /**
     * Getter method
     * @return ArrayList of type Move. GameConfiguration's moves to make.
     */
    public ArrayList<Move> getMoves() {
        return moves;
    }
}
