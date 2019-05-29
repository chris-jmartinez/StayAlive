package ma2015.trabocchetto.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Stores informations about the player, like idFromDb, name, score, won matches, lost matches, skillratio of the player.
 * These informations are updated/retrieved from database when necessary
 */
public class Player implements Comparable<Player> {

    private int idFromDb; //Id of the player in the database
    private String name;
    private int score;
    private int wins;
    private int losses;
    private float skillratio;

    private int penalties; //In penaltyMode, if a player tries to move a forbidden bar, it gets 1 penalty (that removes -5points to the earnedPoints)
    private int scoreToAdd; //Used to store the score that has to be summed with the database score of the player, when the match ends and points are assigned.
    private boolean isWinner; //Indicates if the player is the winner or not
    private int placeInThePodium; //Indicates the place in the podium (the player who won will be the 1st, the player who died first will be the 4th in the podium, for example)
    private int playerNumber;

    /**
     * Used when a player needs to be created and saved in the db
     * @param nameOfPlayerToCreate nickname of the player, that the user has inserted into editText
     */
    public Player(String nameOfPlayerToCreate){
        this.idFromDb=0;
        this.name=nameOfPlayerToCreate;
        this.score=0;
        this.wins=0;
        this.losses=0;
        this.skillratio=0;

        this.scoreToAdd = 0;
        this.isWinner = false;
        this.penalties = 0;
        this.placeInThePodium = 0;

    }


    /**
     * Used when a Player needs to be created by database, and filled with db content, before being
     * sent back to an activity to draw statistics
     */
    public Player(){
        this.idFromDb=0;
        this.name="";
        this.score=0;
        this.wins=0;
        this.losses=0;
        this.skillratio=0;

        this.scoreToAdd = 0;
        this.isWinner = false;
        this.penalties = 0;
        this.placeInThePodium = 0;


    }



    /**
     * Lets me to sort the array of playersDb[] by placeInThePodium (ascending order) (Needs interface comparable
     * and this method overridden)
     */
    public int compareTo(@NonNull Player comparePlayer) {

        int comparePlaceInThePodium = (comparePlayer).getPlaceInThePodium();

        //ascending order
        return this.placeInThePodium - comparePlaceInThePodium;

        //descending order
        //return comparePlaceInThePodium - this.placeInThePodium;

    }





    //GETTERS AND SETTERS METHODS TO STORE AND RETRIEVE attributes.

    public int getIdFromDb(){
        return idFromDb;
    }

    public void setIdFromDb(int id){
        this.idFromDb = id;
    }


    public String getPlayerName(){
        return this.name;
    }

    public void setPlayerName(String name){
        this.name = name;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getWins(){
        return wins;
    }

    public void setWins(int wins){
        this.wins = wins;
    }

    public int getLosses(){
        return losses;
    }

    public void setLosses(int losses){
        this.losses = losses;
    }

    public float getSkillratio(){
        return skillratio;
    }

    public void setSkillratio(float skillratio){
        this.skillratio = skillratio;
    }


    public int getScoreToAdd(){
        return scoreToAdd;
    }

    public void setScoreToAdd (int scoreToAdd){
        this.scoreToAdd = scoreToAdd;
    }

    public boolean getIsWinner(){
        return isWinner;
    }

    public void setIsWinner (boolean isWinner){
        this.isWinner = isWinner;
    }


    public int getPlaceInThePodium (){
        return placeInThePodium;
    }


    public void setPlaceInThePodium(int placeInThePodium){
        this.placeInThePodium = placeInThePodium;
    }


    public int getPenalties(){
        return this.penalties;
    }

    public void addPenalty(){
        this.penalties++;
    }

    public int getPlayerNumber(){
        return this.playerNumber;
    }

    public void setPlayerNumber(int playerNumber){
        this.playerNumber = playerNumber;
    }



}
