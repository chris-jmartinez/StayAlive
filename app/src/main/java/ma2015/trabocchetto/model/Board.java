package ma2015.trabocchetto.model;


import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * This class represents the principal components of the game. It models the game board, bars,
 * filled and holed slots, forbiddenBars.
 * It allows the interaction with the players through 'move' method.
 */
public class Board {

    private static String forbiddenBarRule1Error ="Forbidden Bar! #RULE1 You cannot move it now"; //RULE#1: You cannot move any of the bars that were slid by your opponents in the previous turn
    private static String forbiddenBarRule2Error ="Forbidden Bar! #RULE2(TwoPlayers) You cannot move it now"; //RULE#2: You cannot move the same bar more than twice when there are 2 players

    private static String invalidMovementDirectionError ="Invalid Movement Direction: already in OUTER position OR already in INNER position OR invalid direction (allowed only inwards or outwards)";
    private static String invalidMovementOrientationError ="Invalid Bar Orientation, allowed only vertical (v) or horizontal (h)";
    private static String invalidBarNumberError ="Invalid bar number";

    private static int INNER=0;
    private static int CENTRAL=1;
    private static int OUTER=2;
    private static int HDIM=7;
    private static int VDIM=7;

    //hBars: matrix representing the physical structure of the horizontal bars (1:filled slot,  0:holed slot)
    private static int hBars[][]={
            {1,0,1,0,1,0,1,0,1},
            {1,0,0,1,0,0,1,0,1},
            {1,0,0,0,1,0,0,0,1},
            {1,0,1,0,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,1},
            {1,1,0,0,0,1,0,1,1},
            {1,0,0,1,0,1,0,1,1}
    };
    private static int vBars[][]={
            {1,0,0,0,0,1,0,1,1},
            {1,0,0,0,1,1,0,0,1},
            {1,0,1,0,0,1,0,1,1},
            {1,0,0,1,1,0,0,0,1},
            {1,1,0,0,0,1,0,1,1},
            {1,1,0,0,0,0,0,1,1},
            {1,0,0,1,0,0,1,0,1}
    };
    private final int PLAYERS; //number of the initial players (constant)
    private Player playersDb[];
    private int movingPlayer; //player that has to move
    private int playerPuttingBead;

    private int currentNumberOfPlayers;
    private int hBoard [][]=new int[HDIM][VDIM]; //7*7 Matrix representing visible filled/holed slots of horizontal bars. It'll be redrawn during the match, as players move horizontal bars
    private int vBoard [][]=new int[HDIM][VDIM];
    private int hBarPosition[]=new int[VDIM]; //Array that keeps trace of the positions (0: inner, 1:central, 2:outer) of horizontal bars. (Each slot contains an hBar position)
    private int vBarPosition[]=new int[HDIM];


    //Matrix. Rows: Forbidden bars.
    //3 Columns: Col1: number of the forbidden bar,  Col2: orientation of the bar(0 horizontal, 1 vertical),  Col3: player who moved that bar
    private int forbiddenBars[][];


    //Matrix. Useful when there are only 2 players left.
    //Rows: Players
    //Cols: FirstColumn: Number of the bar used,   SecondColumn: (counter) number of times that the bar is played continuously by that player,     ThirdCol: orientation of the used bar (1: vertical, 0: horizontal)
    private int lastPlayedBar[][]={
            {-1,0, -1},
            {-1,0, -1},
            {-1,0, -1}, //The matrix is used when there are only two players left. I leave, for an easier implementation, 4 rows instead of 2;
            {-1,0, -1}};//  So only two rows will be effectively used (the survivor players in case of 3/4PlayersMatch,  or the player 1 and 2 in case of 2PlayersMatch)

    private int winner=-1; //It'll become the number of the player who wins.
    private BeadsGrid beadsGrid; //Matrix containing all the positions of the players' beads.
    private boolean someBeadsHaveFallen;
    private boolean isPenaltyActivated = false;
    private int podiumPlaces;

    protected static boolean TESTModeActivated = false;
    public static boolean oracleIsActivated;


    /**
     * Board Constructor (FOR REAL GAME): it initializes the forbiddenBars, the beadsGrid, the array of players, hBoard and vBoard
     * that will contain the visible filledSlots/holedSlots of the horizontal and vertical Bars.
     * @param players : number of the initial players
     */
    public Board(int players, Player playersDb[]){

        this.PLAYERS=players;
        this.playersDb = playersDb;



        this.currentNumberOfPlayers=players;
        this.podiumPlaces = players;

        setPlayerPuttingBead(1);
        setMovingPlayer(1);

        this.beadsGrid = new BeadsGrid(PLAYERS, playersDb);

        //initialization of the queue for the management of forbiddenBars
        forbiddenBars=new int[PLAYERS-1][3];
        for(int i=0;i<PLAYERS-1;i++){
            forbiddenBars[i][0]=-1;  //-1 default value at start. No bars are forbidden at the start of the game
            forbiddenBars[i][1]=-1;
            forbiddenBars[i][2]=-1;
        }


        initializeBars(); //RANDOM INITIALIZATION OF THE BARS (EACH BAR IN A RANDOM POSITION)


        //initialization of the hBoard matrix(that will contain the visible filledSlots/holedSlots of the horizontal Bars)
        for(int i=0;i<hBarPosition.length;i++){

            if(hBarPosition[i]==INNER){
                for(int j=0;j<vBarPosition.length;j++){
                    hBoard[i][j]=hBars[i][j]; //hBoard:7*7Matrix of visible filled/holed slots of horizontal bars.  hBars: matrix, physical structure of horizontal bars (bar length:9)
                }
            }else if(hBarPosition[i]==CENTRAL){
                for(int j=0;j<vBarPosition.length;j++){
                    hBoard[i][j]=hBars[i][j+1];
                }
            }else if(hBarPosition[i]==OUTER){
                for(int j=0;j<vBarPosition.length;j++){
                    hBoard[i][j]=hBars[i][j+2];
                }
            }

            this.hBarPosition[i]=hBarPosition[i];
        }

        //initialization of the vBoard matrix(that will contain the visible filledSlots/holedSlots of the vertical Bars)
        for(int i=0;i<vBarPosition.length;i++){

            if(vBarPosition[i]==INNER){
                for(int j=0;j<hBarPosition.length;j++){
                    vBoard[j][i]=vBars[i][j];
                }
            }else if(vBarPosition[i]==CENTRAL){
                for(int j=0;j<hBarPosition.length;j++){
                    vBoard[j][i]=vBars[i][j+1];
                }
            }else if(vBarPosition[i]==OUTER){
                for(int j=0;j<hBarPosition.length;j++){
                    vBoard[j][i]=vBars[i][j+2];
                }
            }
            this.vBarPosition[i]=vBarPosition[i];
        }

        this.someBeadsHaveFallen = false;
    }





    /**
     * Board Constructor (FOR TEST): it initializes the forbiddenBars, the beadsGrid, the array of players, hBoard and vBoard
     * that will contain the visible filledSlots/holedSlots of the horizontal and vertical Bars.
     * @param hBarPosition : array with the positions of the horizontal bars (each slot contains: 0:inner 1:central 2:outer)
     * @param vBarPosition : array with the positions of the vertical bars (each slot contains: 0:inner 1:central 2:outer)
     * @param players : number of the initial players
     * @param beadsGrid : the matrix containing all the players' beads
     */
    public Board(int hBarPosition[],int vBarPosition[],int players,int beadsGrid [][]){

        Board.TESTModeActivated = true;

        this.PLAYERS=players;

        this.playersDb = new Player[players];
        for (int i=0; i<players; i++){
            playersDb[i] = new Player();
        }


        this.currentNumberOfPlayers=players;
        this.podiumPlaces = players;



        //initialization of the queue for the management of forbiddenBars
        forbiddenBars=new int[PLAYERS-1][3];
        for(int i=0;i<PLAYERS-1;i++){
            forbiddenBars[i][0]=-1;  //-1 default value at start. No bars are forbidden at the start of the game
            forbiddenBars[i][1]=-1;
            forbiddenBars[i][2]=-1;
        }


        this.beadsGrid=new BeadsGrid(PLAYERS,beadsGrid);


        //initialization of the hBoard matrix(that will contain the visible filledSlots/holedSlots of the horizontal Bars)
        for(int i=0;i<hBarPosition.length;i++){

            if(hBarPosition[i]==INNER){
                for(int j=0;j<vBarPosition.length;j++){
                    hBoard[i][j]=hBars[i][j]; //hBoard:7*7Matrix of visible filled/holed slots of horizontal bars.  hBars: matrix, physical structure of horizontal bars (bar length:9)
                }
            }else if(hBarPosition[i]==CENTRAL){
                for(int j=0;j<vBarPosition.length;j++){
                    hBoard[i][j]=hBars[i][j+1];
                }
            }else if(hBarPosition[i]==OUTER){
                for(int j=0;j<vBarPosition.length;j++){
                    hBoard[i][j]=hBars[i][j+2];
                }
            }

            this.hBarPosition[i]=hBarPosition[i];
        }

        //initialization of the vBoard matrix(that will contain the visible filledSlots/holedSlots of the vertical Bars)
        for(int i=0;i<vBarPosition.length;i++){

            if(vBarPosition[i]==INNER){
                for(int j=0;j<hBarPosition.length;j++){
                    vBoard[j][i]=vBars[i][j];
                }
            }else if(vBarPosition[i]==CENTRAL){
                for(int j=0;j<hBarPosition.length;j++){
                    vBoard[j][i]=vBars[i][j+1];
                }
            }else if(vBarPosition[i]==OUTER){
                for(int j=0;j<hBarPosition.length;j++){
                    vBoard[j][i]=vBars[i][j+2];
                }
            }
            this.vBarPosition[i]=vBarPosition[i];
        }
    }


    /**
     * This method allows the player to move a bar, if possible. Then it updates forbiddenBars, lastPlayedBars
     * and checks for the beads that need to be removed.
     * @param orientation : char, orientation ('h' or 'v') of the bar to move
     * @param bar : number of the bar to move
     * @param direction : direction of the bar to move (inward 'i' or outward 'o')
     * @throws Exception : if a passed argument is illegal, or a move is invalid for some reason, the method throws an exception
     */
    public void move(char orientation, int bar, char direction) throws Exception {

        someBeadsHaveFallen = false;
        int orientationInt = -1;
        if (orientation == 'v') {
            orientationInt = 1;
        }
        else if (orientation == 'h') {
            orientationInt = 0;
        }


        if (bar<1 || bar >7){
            IllegalArgumentException e=new IllegalArgumentException(invalidBarNumberError);
            throw e;
        }


        if(!isBarAvailable(bar,orientation)){
            IllegalArgumentException e=new IllegalArgumentException(forbiddenBarRule1Error);

            if (isPenaltyActivated){
                playersDb[movingPlayer-1].addPenalty();
            }

            throw e;
        }

        //Checks the rule#2: when two players are left, a player cannot move the same bar more than twice.
        if (currentNumberOfPlayers == 2) {
            if(lastPlayedBar[movingPlayer-1][0]==bar  && lastPlayedBar[movingPlayer-1][1]==2 && lastPlayedBar[movingPlayer-1][2]==orientationInt){
                IllegalArgumentException e=new IllegalArgumentException(forbiddenBarRule2Error);

                if (isPenaltyActivated){
                    playersDb[movingPlayer-1].addPenalty();
                }

                throw e;
            }
        }


        //The following lines allows to move a bar (if possible), depending on the given bar orientation ('h' or 'v'), bar number. direction.
        //If it's not possible to move the bar, it is thrown an exception. The hBoard and vBoard are re-drawn depending on the movement.
        if(orientation=='h'){
            if(hBarPosition[bar-1]==INNER && direction=='o'){

                for(int i=0;i<HDIM;i++) {
                    hBoard[bar-1][i] = hBars[bar-1][i+1]; //Re-drawing the hBoard(7*7Matrix of visible filled/holed slots of horizontal bars.)
                }                                                    //     hBars: matrix, physical structure of horizontal bars (bar length:9)
                hBarPosition[bar-1]=CENTRAL;

            }else if(hBarPosition[bar-1]==INNER && direction=='i'){

                IllegalArgumentException e=new IllegalArgumentException(invalidMovementDirectionError);//already in inner position
                throw e;

            }else if(hBarPosition[bar-1]==CENTRAL && direction=='o'){

                for(int i=0;i<HDIM;i++){
                    hBoard[bar-1][i]=hBars[bar-1][i+2];
                }
                hBarPosition[bar-1]=OUTER;

            }else if(hBarPosition[bar-1]==CENTRAL && direction=='i'){

                for(int i=0;i<HDIM;i++){
                    hBoard[bar-1][i]=hBars[bar-1][i];
                }
                hBarPosition[bar-1]=INNER;

            }else if(hBarPosition[bar-1]==OUTER && direction=='o'){

                IllegalArgumentException e=new IllegalArgumentException(invalidMovementDirectionError);//already in outer position
                throw e;

            }else if(hBarPosition[bar-1]==OUTER && direction=='i'){

                for(int i=0;i<HDIM;i++){
                    hBoard[bar-1][i]=hBars[bar-1][i+1];
                }
                hBarPosition[bar-1]=CENTRAL;

            }else{

                IllegalArgumentException e=new IllegalArgumentException(invalidMovementDirectionError);//only allowed inwards or outwards
                throw e;

            }

        }else if(orientation=='v'){ //vertical bar orientation

            if(vBarPosition[bar-1]==INNER && direction=='o'){

                for(int i=0;i<HDIM;i++){
                    vBoard[i][bar-1]=vBars[bar-1][i+1];
                }
                vBarPosition[bar-1]=CENTRAL;

            }else if(vBarPosition[bar-1]==INNER && direction=='i'){

                IllegalArgumentException e=new IllegalArgumentException(invalidMovementDirectionError); //already in inner position
                throw e;

            }else if(vBarPosition[bar-1]==CENTRAL && direction=='o'){

                for(int i=0;i<HDIM;i++){
                    vBoard[i][bar-1]=vBars[bar-1][i+2];
                }
                vBarPosition[bar-1]=OUTER;

            }else if(vBarPosition[bar-1]==CENTRAL && direction=='i'){

                for(int i=0;i<HDIM;i++){
                    vBoard[i][bar-1]=vBars[bar-1][i];
                }
                vBarPosition[bar-1]=INNER;

            }else if(vBarPosition[bar-1]==OUTER && direction=='o'){

                IllegalArgumentException e=new IllegalArgumentException(invalidMovementDirectionError); //already in outer position
                throw e;

            }else if(vBarPosition[bar-1]==OUTER && direction=='i'){

                for(int i=0;i<HDIM;i++){
                    vBoard[i][bar-1]=vBars[bar-1][i+1];
                }
                vBarPosition[bar-1]=CENTRAL;

            }else{

                IllegalArgumentException e=new IllegalArgumentException(invalidMovementDirectionError); //only allowed inwards or outwards
                throw e;

            }
        }else{
            IllegalArgumentException e=new IllegalArgumentException(invalidMovementOrientationError);
            throw e;
        }




        //Updates (and eventually resizes) the Forbidden Bars.
        //   OR: the second condition is evaluated only if the first condition is false
        if(forbiddenBars[0][2]==-1 || beadsGrid.isPlaying(forbiddenBars[0][2]) ){
            updateForbiddenBars(bar,orientation);
        }else{
            updateAndReduceForbiddenBars(bar,orientation);
        }



        //MATRIX lastPlayedBar:::   Rows:Players    First Col: numBar      Second Col: NTimes used      Third Col: Orientation (1:vertical, 0:horizontal)
        //Update of the two last consecutively used bars (useful when there're two players left).
        if(lastPlayedBar[movingPlayer-1][0]==bar && lastPlayedBar[movingPlayer-1][2]==orientationInt){
            lastPlayedBar[movingPlayer-1][1]++;
        }else {
            lastPlayedBar[movingPlayer - 1][0] = bar;
            lastPlayedBar[movingPlayer - 1][1]=1; //set to "1" the number of times that the player has moved that bar
            lastPlayedBar[movingPlayer - 1][2]= orientationInt;
        }

        BeadsGrid.killedPlayersByMov.clear();

        checkBeadsToBeRemoved();

        oracleIsActivated = false;
        assignPodiumPlaces();


        updateCurrentPlayersNumber();

        if (currentNumberOfPlayers >= 2) {
            shiftMovingPlayer(); //only if there are at least 2 players left
        }else{
            chooseWinner();
        }

    }


    /**
     * In this method I assign podium places as players die (when they have no beads left). If there
     * are more than one player killed with a bar movement, if he is dead and was the moving player, then he beats
     * the other players (and his podium position will be higher than the others), otherwise (and for remaining
     * players or, in general, when it's not predictable the sequence of death) the players are shuffled and I
     * randomly assign them the podiumPositions.
     */
    private void assignPodiumPlaces(){
        if (BeadsGrid.killedPlayersByMov!= null && BeadsGrid.killedPlayersByMov.size() == 1){
            BeadsGrid.killedPlayersByMov.get(0).setPlaceInThePodium(podiumPlaces--);
        }else if (BeadsGrid.killedPlayersByMov!=null && BeadsGrid.killedPlayersByMov.size() > 1){
            int deadKiller = -1;
            Collections.shuffle(BeadsGrid.killedPlayersByMov);
            for (int i=0; i<BeadsGrid.killedPlayersByMov.size(); i++){

                if ( BeadsGrid.killedPlayersByMov.get(i).getPlayerNumber() != movingPlayer ){
                    BeadsGrid.killedPlayersByMov.get(i).setPlaceInThePodium(podiumPlaces--);
                }else if ( BeadsGrid.killedPlayersByMov.get(i).getPlayerNumber() == movingPlayer ){
                    deadKiller = i;
                }
            }

            if (deadKiller != -1) { //if the movingPlayer is one a player who died...then:
                BeadsGrid.killedPlayersByMov.get(deadKiller).setPlaceInThePodium(podiumPlaces--); //he gets the best position (because this is assigned last, after the others! see for-cycle)
            }

            //Code to decide if I will show the toast of the Oracle or is not necessary (theese are the cases where the death sequence was not predictable)
            if ( (BeadsGrid.killedPlayersByMov.size() >= 3) || (BeadsGrid.killedPlayersByMov.size() == 2 && deadKiller == -1) ) {
                oracleIsActivated = true;
            }

        }
    }




    /**
     * This method counts the players still alive, and updates the current number of players
     */
    private void updateCurrentPlayersNumber(){
        int n=0;
        for(int i=0;i<PLAYERS;i++){
            if(beadsGrid.isPlaying(i+1)==true){ //if the player i+1 is still alive, increments the counter n.
                n++;
            }
        }
        currentNumberOfPlayers=n;
    }


    /**
     * This method selects the next player who has to do a move. It updates the 'movingPlayer'.
     */
    private void shiftMovingPlayer(){
        int temp=movingPlayer; //player who moved.
        boolean flag=true;
        while(flag){

            if(temp==PLAYERS){ //if temp reaches 4 I need to re-start the search of the next moving player from player1.
                for(int i=0;i<PLAYERS;i++){
                    if(beadsGrid.isPlaying(i+1)){ //temp is set = to the first player whose status is true (alive), starting from player1.
                        temp=i+1;
                        break;
                    }
                }
            }else{
                temp++;
            }

            if(beadsGrid.isPlaying(temp)){ //if the player is alive, it becomes the movingPlayer (who has to do the next move).
                movingPlayer=temp;
                flag=false;
            }


        }

    }




    /**
     * This method updates the forbiddenBars shifting old values by one position, and putting
     * the new value (forbiddenBar) at the end of the queue.
     * @param bar : number of the bar that needs to be forbidden
     * @param orientation : orientation of the bar ('h' or 'v')
     */
    private void updateForbiddenBars(int bar,char orientation){
        //forbiddenBars: Col1: number of the forbidden bar,  Col2: orientation of the bar(0 horizontal, 1 vertical),  Col3: player who moved that bar

        //I shift old values by one position
        for(int i=0;i<currentNumberOfPlayers-2;i++){
            forbiddenBars[i][0]=forbiddenBars[i+1][0];
            forbiddenBars[i][1]=forbiddenBars[i+1][1];
            forbiddenBars[i][2]=forbiddenBars[i+1][2];
        }

        //I put the new value at the end
        forbiddenBars[currentNumberOfPlayers-2][0]=bar;
        forbiddenBars[currentNumberOfPlayers-2][2]=movingPlayer;
        if(orientation=='h') {
            forbiddenBars[currentNumberOfPlayers-2][1] = 0;
        }else{
            forbiddenBars[currentNumberOfPlayers-2][1] = 1;
        }
    }





    /**
     * This method updates the forbiddenBars shifting old values by one position, and putting
     * the new value (forbiddenBar) at the end of the queue. Then it resizes the ForbiddenBars array.
     * @param bar : number of the bar that needs to be forbidden
     * @param orientation : orientation of the bar ('h' or 'v')
     */
    private void updateAndReduceForbiddenBars(int bar,char orientation){
        int temp[][]=new int[currentNumberOfPlayers][3];

        //I shift old values by one position
        for(int i=0;i<currentNumberOfPlayers-2;i++){
            temp[i][0]=forbiddenBars[i+1][0];
            temp[i][1]=forbiddenBars[i+1][1];
            temp[i][2]=forbiddenBars[i+1][2];
        }

        //I put the new value at the end
        temp[currentNumberOfPlayers-2][0]=bar;
        temp[currentNumberOfPlayers-2][2]=movingPlayer;
        if(orientation=='h') {
            temp[currentNumberOfPlayers-2][1] = 0;
        }else{
            temp[currentNumberOfPlayers-2][1] = 1;
        }

        forbiddenBars=temp; //resizes the forbiddenBars
    }





    /**
     * This method scans all the 7*7 Grid in order to checks if there are beads that need
     * to be removed. If in a particular slot there's a bead and the hBoard (matrix of the visible filled/holed hBars slots)
     * and the vBoard contains a holedSlot, that bead needs to be removed (falls down).
     */
    private void checkBeadsToBeRemoved(){

        for(int i=0;i<VDIM;i++){
            for(int j=0;j<HDIM;j++){
                if(hBoard[i][j]==0 && vBoard[i][j]==0 && beadsGrid.getBeadsGrid()[i][j]!=0){
                    beadsGrid.removeBead(i, j);
                    someBeadsHaveFallen = true;
                }
            }
        }

    }



    /**
     * Given a bar number and its orientation ('h'horizontal or 'v'vertical) this method checks
     * if the bar is forbidden or not. If the bar is forbidden, it returns false.
     * @param bar : number of the bar you want to check if it is forbidden or not
     * @param orientation : char representing the orientation of the bar ('h' or 'v')
     * @return boolean, true if the bar is available to use.
     */
    private boolean isBarAvailable(int bar,char orientation){
        int o;
        if (orientation=='h'){
            o=0; //horizontal bars are represented with '0' in the int[]forbiddenBars array, that I'm going to check
        }else{
            o=1;
        }
        for(int i=0;i<currentNumberOfPlayers-1;i++){
            if(forbiddenBars[i][0]==bar && forbiddenBars[i][1]==o){
                return false;
            }
        }

        return true;
    }


    /**
     * This method initializes all horizontal and vertical bars in random positions
     * (0:Inner, 1:Central, 2:Outer)
     */
    private void initializeBars(){
        Random random = new Random();
        for(int i=0;i<VDIM;i++){
            int n = 3;
            int k = random.nextInt(n);//Values between 0 and 2
            hBarPosition[i]=k;
        }
        for(int i=0;i<HDIM;i++){
            int n = 3;
            int k = random.nextInt(n);//Values between 0 and 2
            vBarPosition[i]=k;
        }

    }



    /**
     * This method chooses the winner.
     * If the last move make all the beads fall down (noMorePlayers), the player who moved the last bar wins.
     * Instead, if all the others players lose, the remaining player wins.
     */
    private void chooseWinner(){
        //probably this is not necessary anymore after I've made the assignPodiumPlaces method (it's only necessary the setIsWinner)
        // But the ELSE is necessary! in case players die one by one.
        if(currentNumberOfPlayers==0){ //If the last move make all the beads fall down (noMorePlayers), the player who moved the last bar wins.
            winner=movingPlayer;
            playersDb[movingPlayer-1].setPlaceInThePodium(1);
            playersDb[movingPlayer-1].setIsWinner(true);
            if (!TESTModeActivated){
                pointsAssignment();
            }


        }else{ //If all the others players lose, the remaining player wins.
            for(int i=1;i<=PLAYERS;i++){
                if(beadsGrid.isPlaying(i)){
                    winner=i;

                    playersDb[i-1].setPlaceInThePodium(1);
                    playersDb[i-1].setIsWinner(true);
                    if (!TESTModeActivated){
                        pointsAssignment();
                    }

                    break;
                }
            }
        }
    }

    /**
     * Assigns the points to the players: First player gets 100 points, Last player 0 points, and middle players
     * gets MaxPoints/2 (scaling) (so if there are 4 players, 1st: 100, 2nd: 50, 3rd: 25, 4th:0)  and if there
     * are 3 players (1st: 100, 2nd: 50, 3rd: 0)  and if there are 2 players (1st: 100, 2nd: 0). These points
     * can be modified by penalties (each subtracts -5points) if penalty mode is activated. During the match there's
     * a button "rules" that explains this rules.
     */
    public void pointsAssignment(){
        final int FIRST_CLASSIFIED = 1, MAX_POINTS = 100, LAST_CLASSIFIED_POINTS = 0, PENALTY_ENTITY_POINTS = 5;
        int middleClassifiedPoints = MAX_POINTS/2;

        Arrays.sort(playersDb); //ATTENTION!: STARTING FROM THIS POINT, THE ARRAY playersDb IS ORDERED BY PODIUM POSITION

        for (int i = 0; i<PLAYERS; i++){
            System.out.print("TEST::: Sorted Array playersDb by PodiumPlace, player Name: " + playersDb[i].getPlayerName() + " Place in Podium:" + playersDb[i].getPlaceInThePodium() + "\n");
        }


        for (int i = 0; i<PLAYERS; i++){

            if (playersDb[i].getPlaceInThePodium() == FIRST_CLASSIFIED){
                playersDb[i].setScoreToAdd(MAX_POINTS - (PENALTY_ENTITY_POINTS * playersDb[i].getPenalties()) );
            }
            else if (playersDb[i].getPlaceInThePodium() == PLAYERS){
                playersDb[i].setScoreToAdd(LAST_CLASSIFIED_POINTS - (PENALTY_ENTITY_POINTS * playersDb[i].getPenalties()) );
            }
            else{
                playersDb[i].setScoreToAdd( middleClassifiedPoints - (PENALTY_ENTITY_POINTS * playersDb[i].getPenalties()));
                middleClassifiedPoints = (middleClassifiedPoints/2);
            }


            System.out.print("Player" + (i+1) + "Classif points" + playersDb[i].getScoreToAdd());


        }
    }







    /**
     * Orders the playersDb array considering the podium position
     * @param playersDb players objects
     * @return array of players objects ordered by ascending podium position
     */
    /*
    public Player[] orderPlayersDbByPodiumPosition(Player [] playersDb) {
        Player[] playersDbOrdered = new Player[PLAYERS];

        for(int i = 0; i < playersDb.length; i++) {

            switch ( playersDb[i].getPlaceInThePodium() ){

                case 1:
                    playersDbOrdered[0] = playersDb[i];
                    break;
                case 2:
                    playersDbOrdered[1] = playersDb[i];
                    break;
                case 3:
                    playersDbOrdered[2] = playersDb[i];
                    break;
                case 4:
                    playersDbOrdered[3] = playersDb[i];
            }

        }

        return playersDbOrdered;
    }
*/



    /**
     * Getter method.
     * @return matrix of integers, the hBoard containing all the visible filled/holed slots of horizontal bars.
     */
    public int[][] gethBoard() {
        return hBoard;
    }


    /**
     * Getter method.
     * @return matrix of integers, the vBoard containing all the visible filled/holed slots of vertical bars.
     */
    public int[][] getvBoard() {
        return vBoard;
    }


    /**
     * Getter method.
     * @return array containing all the positions of the horizontal bars
     */
    public int[] gethBarPosition() {
        return hBarPosition;
    }


    /**
     * Getter method.
     * @return array containing all the positions of the vertical bars
     */
    public int[] getvBarPosition() {
        return vBarPosition;
    }


    /**
     * Getter method
     * @return int, number of the initial PLAYERS
     */
    public int getPLAYERS() {
        return PLAYERS;
    }



    /**
     * Getter method
     * @return matrix of integers, containing the forbiddenBars
     */
    public int[][] getForbiddenBars() {
        return forbiddenBars;
    }

    /**
     * Getter method
     * @return matrix of integers, containing the lastPlayedBars (useful for rule#2)
     */
    public int[][] getLastPlayedBar(){
        return lastPlayedBar;
    }


    /**
     * Getter method
     * @return the BeadsGrid matrix (integers), containing all the beads of the players
     */
    public BeadsGrid getBeadsGrid() {
        return beadsGrid;
    }



    public void setBeadsGrid(BeadsGrid beadsGrid) {
        this.beadsGrid = beadsGrid;
    }



    /**
     * Setter method. It sets the given player as the movingPlayer, that has to do the next bar movement
     * @param movingPlayer : int, number of player that need to be set as movingPlayer
     */
    public void setMovingPlayer(int movingPlayer) {
        this.movingPlayer = movingPlayer;
    }


    /**
     *Getter method.
     * @return int, movingPlayer (that has to move).
     */
    public int getMovingPlayer() {
        return movingPlayer;
    }


    public void setPlayerPuttingBead(int puttingBeadPlayer){
        this.playerPuttingBead = puttingBeadPlayer;
    }

    public int getPlayerPuttingBead(){
        return playerPuttingBead;
    }


    public void setNextPlayerPuttingBead(){
        if ( (playerPuttingBead + 1) <= PLAYERS ){
            playerPuttingBead += 1;
        }
        else {
            playerPuttingBead = 1;
        }
    }


    /**
     * Getter method
     * @return int, current number of the players (still alive)
     */
    public int getCurrentNumberOfPlayers() {
        return currentNumberOfPlayers;
    }


    /**
     * Getter method
     * @return int, the number of the player who wins.
     */
    public int getWinner() {
        return winner;
    }


    public boolean getIfSomeBeadsHaveFallen() { return someBeadsHaveFallen; }


    public void setIsPenaltyActivated(boolean isPenaltyActivated){
        this.isPenaltyActivated = isPenaltyActivated;
    }

    public boolean isPenaltyActivated(){
        return isPenaltyActivated;
    }


}
