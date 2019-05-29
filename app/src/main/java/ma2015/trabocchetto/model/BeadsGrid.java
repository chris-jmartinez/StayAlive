package ma2015.trabocchetto.model;

import java.util.ArrayList;

/**
 * This class represents the logic related to the players' beads, players and the grid where the players put their beads.
 * The methods can remove a bead from the grid, count beads (in the grid) of a given player, check if a player is able to play (status checking).
 */
public class BeadsGrid {

    private static int HDIM=7; //Horizontal dimension of the grid (it's also the number of horizontal bars)
    private static int VDIM=7;
    private static int INITIAL_BEADS_PER_PLAYER = 5;

    private int beadsGrid [][]=new int[HDIM][VDIM]; //Stores players' beads, represented as numbers from 1 to 4

    private int beadsLeft[]; //Keeps trace of the number of remaining beads for each player, in order to not check the entire matrix every time

    private boolean playerStatus[];//Keeps trace of the status of each player: each position represents a player (if he has no beads the position is set to false).

    private Player playersDb[];

    public static ArrayList<Player> killedPlayersByMov;

    private int selectedPlayers;

    /**
     * BeadsGrid Constructor (FOR REAL GAME): it initializes beadsLeft array, counting for each player the beads in the Grid;
     * it stores the received parameter beadsGrid in the attributes of the object;
     * it initializes playerStatus array, that contains, for each array's position (player), the boolean status of the player (true = he can play)
     * @param players : the number of the initial players.
     */
    public BeadsGrid(int players, Player playersDb[]){

        this.selectedPlayers = players;
        beadsLeft=new int[players];
        for(int i=0;i<players;i++){
            beadsLeft[i]= INITIAL_BEADS_PER_PLAYER;
        }

        //I use this redundant variable for understability-of-the-code reasons (this grid will be filled with beads by the players)
        int cleanBeadsGridToFill[][]={
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0} };

        this.beadsGrid = cleanBeadsGridToFill;

        playerStatus=new boolean[players];
        for(int i=0;i<players;i++){
            playerStatus[i]=true;
        }

        this.playersDb = playersDb;
        BeadsGrid.killedPlayersByMov = new ArrayList<>();


    }



    /**
     * BeadsGrid Constructor (FOR TEST): it initializes beadsLeft array, counting for each player the beads in the Grid;
     * it stores the received parameter beadsGrid in the attributes of the object;
     * it initializes playerStatus array, that contains, for each array's position (player), the boolean status of the player (true = he can play)
     * @param players : the number of the initial players.
     * @param beadsGrid : the matrix containing the beads of all the players.
     */
    public BeadsGrid(int players,int beadsGrid [][]){

        this.selectedPlayers = players;
        beadsLeft=new int[players];
        for(int i=0;i<players;i++){
            beadsLeft[i]=countBeads(beadsGrid,i+1);
        }
        this.beadsGrid=beadsGrid;

        playerStatus=new boolean[players];
        for(int i=0;i<players;i++){
            playerStatus[i]=true;
        }

        this.playersDb = null;
        BeadsGrid.killedPlayersByMov = new ArrayList<>();

    }


    /**
     * Given the matrix of the beads and a player, this method counts the beads that belong to the given
     * specific player
     * @param beadsGrid : matrix containing all the players' beads.
     * @param player : number of the player you want to know the beads number in the beadsGrid.
     * @return integer containing the number of the beads belonging to the given player.
     */
    private int countBeads(int beadsGrid[][], int player){
        int n=0;
        for(int i=0;i<VDIM;i++){
            for(int j=0;j<HDIM;j++){
                if(beadsGrid[i][j]==player){
                    n++;
                }
            }
        }
        return n;
    }


    /**
     * Given a horizontal position (hPos) and a vertical position (vPos) of a bead, this method removes
     * it from the Grid containing all the beads (beadsGrid) and updates the remaining player's beads
     * affected by that removal. If the beads of a player reach the value 0, the player loses and his status
     * is put to "false" (not able to play) in the playerStatus array.
     * @param hPos : Horizontal position of the bead to remove
     * @param vPos : Vertical position of the bead to remove
     */
    public void removeBead(int vPos,int hPos){
        //ArrayList<Player> killedPlayersByMov = new ArrayList<Player>();

        int playerNumber=beadsGrid[vPos][hPos];
        beadsGrid[vPos][hPos]=0;
        beadsLeft[playerNumber-1]--;
        if(beadsLeft[playerNumber-1]==0){
            playerStatus[playerNumber-1]=false;
            if (!Board.TESTModeActivated){
                killedPlayersByMov.add(playersDb[playerNumber - 1]);
                //playersDb[playerNumber-1].setPlaceInThePodium(placeInThePodium);
                //placeInThePodium--;
            }
        }

    }

    public void setBead(int playerNumber, int vPos, int hPos){
        beadsGrid[vPos][hPos]=playerNumber;
    }


    /**
     * Given a playerNumber as parameter, this method checks in the playerStatus array if a player
     * is able to play (in that case, the method returns true).
     * @param playerNumber : number of the player you want to know the status
     * @return boolean indicating if the player is able to play (true) or not.
     */
    public boolean isPlaying(int playerNumber) {
        if (playerNumber <= playerStatus.length) {
            return playerStatus[playerNumber - 1];
        }else{
            return false;
        }
    }



    /**
     * Getter method: it returns the Grid containing all the beads of the players.
     * @return matrix beadsGrid, containing all players' beads
     */
    public int[][] getBeadsGrid() {
        return beadsGrid;
    }



    /**
     * Getter method: it returns the array containing the number of remaining beads for each player
     * (each player is represented as a position in the array
     * @return array beadsLeft
     */
    public int[] getBeadsLeft() {
        return beadsLeft;
    }

    public boolean allPlayersAreDead(){
        for (int i=0; i<selectedPlayers; i++){
            if (playerStatus[i] == true){
                return false;
            }
        }
        return true;
    }
}
