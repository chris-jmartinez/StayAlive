package ma2015.trabocchetto.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import ma2015.trabocchetto.application.StayAliveApplication;
import ma2015.trabocchetto.model.BeadsGrid;
import ma2015.trabocchetto.model.Board;
import ma2015.trabocchetto.model.Player;
import ma2015.trabocchetto.R;
import ma2015.trabocchetto.listener.GridOnTouchListener;
import ma2015.trabocchetto.listener.HorizontalOnTouchListener;
import ma2015.trabocchetto.listener.VerticalOnTouchListener;
import ma2015.trabocchetto.repository.SQLiteStayAliveRepository;
import ma2015.trabocchetto.repository.StayAliveRepository;
import ma2015.trabocchetto.util.Utils;


/**
 * Manages all the aspects of a Match: let players prepare all their beads on the beadsGrid, initializing
 * Gui elements, eventually the soundPool, and touch listeners to let them put their beads. After that, those
 * listeners are deactivated.
 * Then the real match starts and barsListeners are set. Every time a bar is moved, the involved line is refreshed
 * (so, imageViews of that line are refreshed).
 * When the match ends (and bar listeners are deactivated as well), tells who won and programmatically
 * displays some buttons to let players see the match results, go back to main menu or play another match.
 */
public class GameActivity extends AppCompatActivity{

    private final static int HDIM = 7;
    private final static int VDIM = 7;
    private final static int DIM_SIDE_BEADS_GRID = 7;
    private final static int BEGIN_FX = 0, BAR_MOVEMENT_FX = 1, FALLING_BEAD_FX=2, WINNER_FX=3;
    private final static double SCALING_FACTOR = 10.5;

    private Player[] playersDb;
    private int selectedNumPlayers;

    private Board board;
    private StayAliveRepository stayAliveRepository;
    private String dateOfTheMatch;

    private SoundPool sp; //I WIll use SoundPool to play sound effects.
    private int[] soundIds; //Array that contains the identificator of the SoundPool loaded sounds

    private TextView notificationsBox; //Tells which player has to move
    private static Toast toastBeadsFallen;
    public static Toast toastForbiddenBar;

    private ImageView vBarsGui[] = new ImageView[VDIM]; //Stores imageViews of the vertical bars
    private ImageView hBarsGui[] = new ImageView[HDIM];
    private ImageView barsGridGui[][] = new ImageView[HDIM][VDIM];
    private ImageView beadsGridGui[][] = new ImageView[HDIM][VDIM];



    /**
     * Inflates the layout, sets the proper theme, extracts the data stored in the received intent,
     * initializes all the gui elements (storing them in arrays and matrices) and eventually the soundpool
     * (if soundEffects are activated). Then sets some touch listeners to let players put their beads and
     * proceed with the game preparation.
     * @param savedInstanceState it represents the serialized state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_game);

        this.stayAliveRepository = new SQLiteStayAliveRepository(this);

        //Extracting  number of selected players and players names, from intent.
        Intent intentByGameSettings;
        intentByGameSettings = getIntent();

        this.selectedNumPlayers = intentByGameSettings.getIntExtra(GameSettingsActivity.SELECTED_NPLAYERS, 0);

        this.playersDb = new Player[selectedNumPlayers];
        String insertedPlayersNames[];
        insertedPlayersNames = intentByGameSettings.getStringArrayExtra(GameSettingsActivity.ARRAY_INSERTED_PLAYERS_NAMES);

        /*The line below, with "Serializable" as interface implemented by class "Player", allows me to send an receive custom objects via Intents (Keep in Mind for future!)
          this.playersDb = (Player[])intentByGameSettings.getSerializableExtra(GameSettingsActivity.ARRAY_PLAYERS);   */

        createOrRetrievePlayersDb(insertedPlayersNames); //Players are retrieved from db (or created) and assigned to playersDb

        int playersNumbersGenerator = 0;
        for (int i=0; i<selectedNumPlayers; i++){
            playersNumbersGenerator++;
            playersDb[i].setPlayerNumber(playersNumbersGenerator);
            Log.d("TEST","PLAYER number: " + (playersDb[i].getPlayerNumber()) + "  Name: " + playersDb[i].getPlayerName());
        }


        //Catching the date in order to store (at the end, only if the players didn't quit the match) the date (that I will use in History of Played games)
        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");   //Other format:: SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        this.dateOfTheMatch = format.format(currentDate);

        //I will use this to tell which of the players has to do a move
        this.notificationsBox = (TextView)findViewById(R.id.notifications_box);
        GameActivity.toastBeadsFallen = new Toast(this);
        GameActivity.toastForbiddenBar = new Toast(this);



        if (StayAliveApplication.soundEffectsAreOn) {
            initializeSoundPool();
        }

        //Catches the imageViews of the bars and the bars/beads grid, and stores them in arrays and matrices.
        loadGuiElements();

        this.board = new Board(selectedNumPlayers, playersDb);

        //If true, the penalty mode is activated (the second argument of getBooleanExtra is the default value)
        if (intentByGameSettings.getBooleanExtra(GameSettingsActivity.PENALTY_FLAG, false)){
            board.setIsPenaltyActivated(true);
        }else{
            board.setIsPenaltyActivated(false);
        }

        drawCompleteGui();

        setBarsGridTouchListeners(); //In order to let users put their beads, touching the cells.


    }



    /**
     * Creates or retrieves an existing player from db, depending on the name the user has inserted. If a retrieval tentative
     * returns null, it means the player needs to be created.
     * @param insertedPlayersNames contains the nicknames that the users typed in the autocompleteTextViews.
     */
    private void createOrRetrievePlayersDb(String[] insertedPlayersNames){

        //Players creation / retrieval from database
        for (int i = 1; i <= selectedNumPlayers; i++) {

            String nameOfPlayerToFind = insertedPlayersNames[i - 1];

            if (stayAliveRepository.retrievePlayerFromDb(nameOfPlayerToFind) == null) {
                Log.d("FLOW_CONTROL", "Player doesn't exist. I'm creating a new one with the name provided:" + nameOfPlayerToFind);
                Player playerToCreate = new Player(nameOfPlayerToFind);
                Player newPlayerCreatedInDb = stayAliveRepository.save(playerToCreate);
                this.playersDb[i - 1] = newPlayerCreatedInDb;
            } else if (stayAliveRepository.retrievePlayerFromDb(nameOfPlayerToFind).getPlayerName().equals(nameOfPlayerToFind)) {
                Log.d("FLOW_CONTROL", "Existing player: " + nameOfPlayerToFind + ". I'm retrieving him.");
                this.playersDb[i - 1] = stayAliveRepository.retrievePlayerFromDb(nameOfPlayerToFind);
            } else {
                Log.d("TEST", "WARN: Something went wrong trying to retrieve the player OR save a new one. None of these two things was successful");
            }

        }
        //Players creation / retrieval done
    }



    /**
     * If soundEffects are activated (from preferences menu), the soundPool is loaded with this method.
     * I load the effects in the soundIds array, and then I will play them with the command sp.play(...)
     * that requires in input the id of the loaded sound and some other parameters like the volume (left and right).
     */
    public void initializeSoundPool(){
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sp = new SoundPool.Builder()
                .setMaxStreams(7)
                .setAudioAttributes(attrs)
                .build();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        this.soundIds = new int[10];
        soundIds[0] = sp.load(this, R.raw.begin, 1); //The last attribute  has no particular meaning, they suggest to set 1 "to future compatibility"
        soundIds[1] = sp.load(this, R.raw.bar_movement, 1);
        int effectResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.fallingBead);
        soundIds[2] = sp.load(this, effectResIdFromAttr, 1);
        soundIds[3] = sp.load(this, R.raw.winner, 1);
        effectResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.placingBead);
        soundIds[4] = sp.load(this, effectResIdFromAttr, 1);



    }


    /**
     * When all the players beads are put, this method is called, in order to start the match. It disables the barsGridTouchListeners
     * because now the players have to move only the bars. Sets the barsTouchListeners. And the game starts.
     */
    public void startTheMatch(){
        if (StayAliveApplication.soundEffectsAreOn) {
            sp.play(soundIds[BEGIN_FX], (float)0.6, (float)0.6, 1, 0, (float) 1);
        }
        disableBarsGridTouchListeners();
        setBarsTouchListeners();
        updateNotificationBoxAndIconMovingPlayer();
        Toast toast = Toast.makeText(getApplicationContext(), R.string.game_begin, Toast.LENGTH_SHORT);
        toast.show();


    }




    /**
     * In this method: first, I set the board dimensions depending on the display width of the current device,
     * and then I load the imageView elements in arrays (vBarsGui, hBarsGui, barsGridGui, beadsGridGui) in order
     * to use them later to set the proper images corresponding on the bar positions, beads, barsGrid, and so on.
     */
    public void loadGuiElements(){

        //Initially I set the board dimensions depending of the display width of the current device
        //I will set height and width of the imageViews depending of the screen's width:
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        Log.d("INFO_DISPLAY_DEVICE", "displayWIdth:" + displayWidth);

        int barsGridSideCellSize = (int)(displayWidth / SCALING_FACTOR); //Scaling factor roughly depends on how many cells I have on the board
        int beadsGridSideCellSize = (int)(barsGridSideCellSize * 0.8); //I want the beads not to fit completely the barsGrid cell, otherwise player won't see color of the bar in which is positioned.
        int barShortSide = (int)(barsGridSideCellSize);
        int barLongSide = (int)(barsGridSideCellSize*2);
        int infoDirectionSideCellSize = (int) barLongSide;

        Log.d("TEST", "barsGridSideCellSize:" + barsGridSideCellSize + "  beadsGridSideCellSize:" + beadsGridSideCellSize + "  barShortSide:" + barShortSide + "  barLongSide:" + barLongSide);

        //Sets the dimension of the player's icon
        ImageView imageViewPlayerIcon = (ImageView)findViewById(R.id.icon_moving_player);
        imageViewPlayerIcon.getLayoutParams().height = Utils.getDisplayWidth(this)/10;
        imageViewPlayerIcon.getLayoutParams().width = Utils.getDisplayWidth(this)/10;
        imageViewPlayerIcon.requestLayout();

        //Sets dimension of the InfoDirection Box (indicates, with an arrow, the direction in which the user wants to move the bar)
        ImageView imageViewInfoDir = (ImageView)findViewById(R.id.info_direction);
        imageViewInfoDir.getLayoutParams().height = infoDirectionSideCellSize;
        imageViewInfoDir.getLayoutParams().width = infoDirectionSideCellSize;
        imageViewInfoDir.requestLayout();

        //INITIALIZATION OF GUI ELEMENTS VERTICAL BARS, HORIZONTAL BARS, GRID:
        //   we catch them from the xml file and we store them in arrays and a matrix, in order to use them later
        for (int i=0; i<DIM_SIDE_BEADS_GRID; i++){

            int resId = getResources().getIdentifier("bar_v" + (i+1), "id", getPackageName());
            ImageView imageView = (ImageView) findViewById(resId);
            imageView.getLayoutParams().height = barLongSide; //Setting height and width of the imageView, related to screen width.
            imageView.getLayoutParams().width = barShortSide;
            imageView.requestLayout();
            vBarsGui[i] = imageView;


            resId = getResources().getIdentifier("bar_h" + (i+1), "id", getPackageName());
            imageView = (ImageView) findViewById(resId);
            imageView.getLayoutParams().height = barShortSide;
            imageView.getLayoutParams().width = barLongSide;
            imageView.requestLayout();
            hBarsGui[i] = imageView;

            /*Research of the j imageViews/columns of the 7*7bars/beads grid, corresponding to the row "i" of the bars/beads grid.
            //For more readability (but less efficiency) I can put a separate   "for (int i = 0; i<VDIM; i++)" (outside this FOR cycle) with
            //an internal   "for (int j = 0; j<HDIM; j++)" in which I put the same following code (resId = getResources().get....imageView.getLayoutParams...etcetc):
            */
            for(int j=0; j<HDIM; j++) {
                resId = getResources().getIdentifier("grid_" + (i + 1) + (j + 1), "id", getPackageName());
                imageView = (ImageView) findViewById(resId);
                imageView.getLayoutParams().height = barsGridSideCellSize; //I set to the imageViews the width and the height depending on the display-device size
                imageView.getLayoutParams().width = barsGridSideCellSize;
                imageView.requestLayout();
                barsGridGui[i][j] = imageView; //..and then I store the imageView in the matrix barsGridGui containing all the imageViews related to the barsGridGui
            }

            //Same as above, but for beads_grid imageViews. (The images of the beads are set in different imageViews, but are inside
            // the same frameLayout as the barsGrid imageViews, so they overlap.
            for(int j=0; j<HDIM; j++) {
                resId = getResources().getIdentifier("b_grid_" + (i + 1) + (j + 1), "id", getPackageName());
                imageView = (ImageView) findViewById(resId);
                imageView.getLayoutParams().height = beadsGridSideCellSize;
                imageView.getLayoutParams().width = beadsGridSideCellSize;
                imageView.requestLayout();
                beadsGridGui[i][j] = imageView;
            }

        }

    }




    /**
     * Drawing of the complete imageViews gui: sets the proper images for bars and barsGridGui (in random positions, given by the board).
     * This method is called only one time, at the beginning, when the board needs to be completely drawed for the first time. Later I refresh only
     * the movement-involved lines.
     */
    public void drawCompleteGui(){
        int drawableResIdFromAttr;

        notificationsBox.setText(String.format(getResources().getString(R.string.game_hey_place_a_bead), board.getPlayerPuttingBead(), getPlayersDb()[board.getPlayerPuttingBead() - 1].getPlayerName()));

        ImageView imageView = (ImageView)findViewById(R.id.icon_moving_player);
        drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.beadP1);
        imageView.setImageResource(drawableResIdFromAttr);


        //Drawing of the vertical,horizontal bars in its respective position, & the beads/barsGrid line per line.
        for (int i = 0; i<DIM_SIDE_BEADS_GRID; i++){
            //Drawing of the i-VERTICAL bar
            if (board.getvBarPosition()[i]==0 ){
                drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.vBarInner);
                vBarsGui[i].setImageResource(drawableResIdFromAttr);
            }
            else if (board.getvBarPosition()[i]==1 ){
                drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.vBarCentral);
                vBarsGui[i].setImageResource(drawableResIdFromAttr);
            }
            else if (board.getvBarPosition()[i]==2 ){
                drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.vBarOuter);
                vBarsGui[i].setImageResource(drawableResIdFromAttr);
            }

            //Drawing of the i-HORIZONTAL bar
            if (board.gethBarPosition()[i]==0 ){
                drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.hBarInner);
                hBarsGui[i].setImageResource(drawableResIdFromAttr);
            }
            else if (board.gethBarPosition()[i]==1 ){
                drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.hBarCentral);
                hBarsGui[i].setImageResource(drawableResIdFromAttr);
            }
            else if (board.gethBarPosition()[i]==2 ){
                drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.hBarOuter);
                hBarsGui[i].setImageResource(drawableResIdFromAttr);
            }

            /*Drawing of the j columns of the 7*7bars/beads grid, corresponding to the row "i" of the bars/beads grid.
            //For more readability (but less efficiency) I can put a separate   "for (int i = 0; i<VDIM; i++)" (outside this FOR cycle) with
            //an internal   "for (int j = 0; j<HDIM; j++)" in which I put the same following code (the if and else if statements below):
            */
            for (int j = 0; j<HDIM; j++) {
                if (board.getvBoard()[i][j] == 0 && board.gethBoard()[i][j] == 0) {
                    drawableResIdFromAttr = Utils.getResIdFromAttribute(this, R.attr.blackHole);
                    barsGridGui[i][j].setImageResource(drawableResIdFromAttr);

                } else if (board.getvBoard()[i][j] == 1) {
                    drawableResIdFromAttr = Utils.getResIdFromAttribute(this, R.attr.vFilled);
                    barsGridGui[i][j].setImageResource(drawableResIdFromAttr);

                } else if (board.getvBoard()[i][j] == 0 && board.gethBoard()[i][j] == 1) {
                    drawableResIdFromAttr = Utils.getResIdFromAttribute(this, R.attr.hFilled);
                    barsGridGui[i][j].setImageResource(drawableResIdFromAttr);

                }
            }

        }
    }


    /**
     * This method refreshes the involved line gui, after a player's movement. The imageViews involved in the movement are updated with the new proper image
     * @param orientationBarToRefresh horizontal 'h' or vertical 'v'
     * @param numBarToRefresh number of the bar (from 1 to 7)
     */
    public void refreshInvolvedLineGui(char orientationBarToRefresh, int numBarToRefresh){
        int drawableResIdFromAttr;

        if (orientationBarToRefresh == 'h') {
            for (int j = 0; j < HDIM; j++) {
                if (board.getvBoard()[numBarToRefresh-1][j] == 0 && board.gethBoard()[numBarToRefresh-1][j] == 0) {
                    drawableResIdFromAttr = Utils.getResIdFromAttribute(this, R.attr.blackHole);
                    barsGridGui[numBarToRefresh-1][j].setImageResource(drawableResIdFromAttr);
                    //We must refresh/throw away the bead image too: if there's a black hole, the player is fallen: so I find the bead view and set his image drawable to null. For every blackhole there's always no bead to display.
                    int resId = getResources().getIdentifier("b_grid_" + (numBarToRefresh) + (j+1), "id", getPackageName());
                    ImageView imageViewFallingBead = (ImageView) findViewById(resId);
                    imageViewFallingBead.setImageDrawable(null);

                } else if (board.getvBoard()[numBarToRefresh-1][j] == 1) {
                    drawableResIdFromAttr = Utils.getResIdFromAttribute(this, R.attr.vFilled);
                    barsGridGui[numBarToRefresh-1][j].setImageResource(drawableResIdFromAttr);

                } else if (board.getvBoard()[numBarToRefresh-1][j] == 0 && board.gethBoard()[numBarToRefresh-1][j] == 1) {
                    drawableResIdFromAttr = Utils.getResIdFromAttribute(this, R.attr.hFilled);
                    barsGridGui[numBarToRefresh-1][j].setImageResource(drawableResIdFromAttr);

                }
            }
        }
        else if (orientationBarToRefresh == 'v'){
            for (int i = 0; i< VDIM; i++){
                if (board.getvBoard()[i][numBarToRefresh-1] == 0 && board.gethBoard()[i][numBarToRefresh-1] == 0) {
                    drawableResIdFromAttr = Utils.getResIdFromAttribute(this, R.attr.blackHole);
                    barsGridGui[i][numBarToRefresh-1].setImageResource(drawableResIdFromAttr);
                    //We must refresh/throw away the bead image too: if there's a black hole, the player is fallen: so I find the bead view and set his image drawable to null.
                    int resId = getResources().getIdentifier("b_grid_" + (i+1) + (numBarToRefresh), "id", getPackageName());
                    ImageView imageViewFallingBead = (ImageView) findViewById(resId);
                    imageViewFallingBead.setImageDrawable(null);

                } else if (board.getvBoard()[i][numBarToRefresh-1] == 1) {
                    drawableResIdFromAttr = Utils.getResIdFromAttribute(this, R.attr.vFilled);
                    barsGridGui[i][numBarToRefresh-1].setImageResource(drawableResIdFromAttr);

                } else if (board.getvBoard()[i][numBarToRefresh-1] == 0 && board.gethBoard()[i][numBarToRefresh-1] == 1) {
                    drawableResIdFromAttr = Utils.getResIdFromAttribute(this, R.attr.hFilled);
                    barsGridGui[i][numBarToRefresh-1].setImageResource(drawableResIdFromAttr);

                }
            }
        }

        if (board.getIfSomeBeadsHaveFallen()){
            if (StayAliveApplication.soundEffectsAreOn) {
                sp.play(soundIds[FALLING_BEAD_FX], (float)0.6, (float)0.6, 1, 0, (float) 1);
            }

            //I place this condition because when there's a winner I don't wanna display "Ouch someone is fallen", but: "Winner!"
            if (board.getWinner()== -1 && !Board.oracleIsActivated){
                    toastBeadsFallen.cancel(); //To avoid toast accumulation
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.game_some_beads_fallen, Toast.LENGTH_SHORT);
                    toastBeadsFallen = toast;
                    toastBeadsFallen.show();
            }

        }



    }






    /**
     * Sets listeners to allow players to put their beads in the barsGridGui
     */
    public void setBarsGridTouchListeners(){

        for (int i=0; i<VDIM; i++){
            for (int j=0; j<HDIM; j++){
                barsGridGui[i][j].setOnTouchListener( new GridOnTouchListener(GameActivity.this, board));  //IL LISTENER LO METTO NELLE CELLE DI BARS_GRID_GUI PERCHE' PIU' GRANDI
            }
        }

    }


    /**
     * Disables the listeners in the cells of the 7*7barsGrid (where players where putting their beads). When they've finished
     * to put their beads, I disable these listeners.
     */
    private void disableBarsGridTouchListeners(){
        for (int i=0; i<VDIM; i++){
            for (int j=0; j<HDIM; j++){
                barsGridGui[i][j].setOnTouchListener( null );  //RIMUOVO IL LISTENER
            }
        }
    }


    /**
     * Sets listeners to the bars imageView elements
     */
    private void setBarsTouchListeners(){
        for (int i=0; i<DIM_SIDE_BEADS_GRID; i++){
            vBarsGui[i].setOnTouchListener(new VerticalOnTouchListener(GameActivity.this, board));
            hBarsGui[i].setOnTouchListener(new HorizontalOnTouchListener(GameActivity.this, board));
        }
    }


    /**
     * Disable bars listeners
     */
    private void disableBarsTouchListeners(){
        for (int i = 0; i< DIM_SIDE_BEADS_GRID; i++){
            vBarsGui[i].setOnTouchListener(null);
            hBarsGui[i].setOnTouchListener(null);
        }
    }


    /**
     * Method called when a player has made a move: it refreshes the involved line (refreshes the imageViews with che new proper ones).
     * @param orientationBarToRefresh horizontal or vertical
     * @param numBarToRefresh numberOfBar to refresh
     */
    public void prepareForNextMove(char orientationBarToRefresh, int numBarToRefresh){
        if (StayAliveApplication.soundEffectsAreOn) {
            sp.play(soundIds[BAR_MOVEMENT_FX], (float)0.6, (float)0.6, 1, 0, (float) 1);
        }

        refreshInvolvedLineGui(orientationBarToRefresh, numBarToRefresh);

        updateNotificationBoxAndIconMovingPlayer(); //The notification box is a textView that sais which player has to move.

        //LOG OF FORBIDDEN BARS (RULE#1) AND LAST PLAYED BARS (RULE#2 useful only when 2 players are left)
        for (int i=0; i<board.getForbiddenBars().length ; i++) {
            Log.i("FLOW_CONTROL", "ForbiddenBars Status: blocked bar--> numBar:" + board.getForbiddenBars()[i][0] + " orientation:" + board.getForbiddenBars()[i][1] + " playerWhoMovedIt:" + board.getForbiddenBars()[i][2]);
        }
        for (int i=0; i<board.getLastPlayedBar().length ; i++){
            Log.i("FLOW_CONTROL", "LastPlayedBars Status: player" + (i + 1) + " numBar:" + board.getLastPlayedBar()[i][0] + " numTimesPlayed:" + board.getLastPlayedBar()[i][1] + " orientation:" + board.getLastPlayedBar()[i][2]);
        }

        oracleToastIfPlayersDiedRandomly();

        //If there's a winner, deactivate bars touch listeners, updates notification box with the winner, updates the players'
        // data in the db, saves the match, updates bar buttons to let users do another match, see results of the match,
        // or go to main menu.
        if ( board.getWinner() != -1){

            Toast toast = Toast.makeText(getApplicationContext(), R.string.game_winner, Toast.LENGTH_SHORT);
            toast.show();

            if (StayAliveApplication.soundEffectsAreOn){
                sp.play(soundIds[WINNER_FX], (float)0.6, (float)0.6, 1, 0, (float)1);
            }

            disableBarsTouchListeners();

            //PLAYERS DB ALREADY SORTED, ASCENDING BY PODIUM POSITION, IN MODEL CLASS.
            //Arrays.sort(playersDb); //I've already sorted by podium position in the class Board, at the end of the match.

            //In the board class I have used Arrays.sort(playersDb) (interface comparable / method compareTo), so the array playersDb
            //is now already ordered (ASCENDING) by podium position, so the position 0 in the array playersDb it's the position
            //of the winner.

            final int WINNER = 0; //position of the winner in the ordered array playersDb (ordered Ascending by podium position)
            notificationsBox.setText(String.format(getResources().getString(R.string.game_hey_winner), board.getWinner(), playersDb[WINNER].getPlayerName()));
            ImageView imageViewPlayerIcon = (ImageView)findViewById(R.id.icon_moving_player);
            switch(board.getWinner()){
                case (1):
                    imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(this, R.attr.beadP1));
                    break;
                case (2):
                    imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(this, R.attr.beadP2));
                    break;
                case (3):
                    imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(this, R.attr.beadP3));
                    break;
                case (4):
                    imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(this, R.attr.beadP4));
                    break;
            }


            for (int i=0; i<selectedNumPlayers; i++){
                stayAliveRepository.update(playersDb[i]); //updates each player's database data.
            }

            stayAliveRepository.saveMatch(dateOfTheMatch, playersDb, selectedNumPlayers);

            updateBarButtons();

        }
    }


    private void oracleToastIfPlayersDiedRandomly(){
        if  (Board.oracleIsActivated){

            Collections.sort(BeadsGrid.killedPlayersByMov);

            for (int i=0; i<BeadsGrid.killedPlayersByMov.size(); i++){
                Log.d("TEST: ", "Killed Player n" + BeadsGrid.killedPlayersByMov.get(i).getPlayerNumber() + " Name: " + BeadsGrid.killedPlayersByMov.get(i).getPlayerName() + " Podium: " + BeadsGrid.killedPlayersByMov.get(i).getPlaceInThePodium());
            }

            String message = getResources().getString(R.string.game_oracle_reveals);
            for (int i=BeadsGrid.killedPlayersByMov.size()-1; i>=0; i--){
                message += "*" + BeadsGrid.killedPlayersByMov.get(i).getPlayerName() + " " + getResources().getString(R.string.game_oracle_sentence) + BeadsGrid.killedPlayersByMov.get(i).getPlaceInThePodium() +"\n";
            }
            final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            TextView toastTextView = (TextView) toast.getView().findViewById(android.R.id.message);
            if (toastTextView != null) {
                int drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.oracle);
                toastTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawableResIdFromAttr, 0, 0, 0);
                toastTextView.setCompoundDrawablePadding(this.getResources().getDimensionPixelSize(R.dimen.padding_oracle));
            }
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, +220);
            toast.show();

            new CountDownTimer(3500, 1000) //Constructor: TotalMilliSeconds until onFinish is called ,  Interval milliseconds when onTick is called)
            {
                public void onTick(long millisUntilFinished) {toast.show();}
                public void onFinish() {toast.show();}

            }.start();


        }
    }


    /**
     * Self-Explanatory: updates notification box (telling which player has to move) and its respective icon
     */
    private void updateNotificationBoxAndIconMovingPlayer(){
        notificationsBox.setText(String.format(getResources().getString(R.string.game_hey_move), board.getMovingPlayer(), getPlayersDb()[board.getMovingPlayer() - 1].getPlayerName(), board.getBeadsGrid().getBeadsLeft()[board.getMovingPlayer() - 1]));

        ImageView imageViewPlayerIcon = (ImageView)findViewById(R.id.icon_moving_player);
        switch(board.getMovingPlayer()){
            case (1):
                imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(this, R.attr.beadP1));
                break;
            case (2):
                imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(this, R.attr.beadP2));
                break;
            case (3):
                imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(this, R.attr.beadP3));
                break;
            case (4):
                imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(this, R.attr.beadP4));
                break;
        }
    }



    /**
     * When the match reaches the end, and there's a winner. Buttons are changed a bit and displayed in
     * a different manner. I set different text and listeners, programmatically. These buttons allows
     * user to see Results, Start a new match, or go to the MainMenu
     */
    private void updateBarButtons(){
        LinearLayout barButtonsLayout = (LinearLayout)findViewById(R.id.barButtonsContainer);

        //setting to "Off" the Align Parent Bottom of the barButtons (only for stylish reasons...)
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams)barButtonsLayout.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        barButtonsLayout.setLayoutParams(params);

        //When clicked I want to show the results of the match
        Button buttonRulesAndResults = (Button)findViewById(R.id.buttonRulesAndResults);
        buttonRulesAndResults.setText(R.string.game_results);
        buttonRulesAndResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMatchResults();
            }
        });


        //To let users make another match
        Button buttonNewGame = (Button)findViewById(R.id.buttonNewGame);
        buttonNewGame.setVisibility(View.VISIBLE);

        //To let users go to main menu
        Button buttonQuitAndMainMenu = (Button) findViewById(R.id.buttonQuitAndMainMenu);
        buttonQuitAndMainMenu.setText(R.string.game_main_menu);
        buttonQuitAndMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.release(); //Sondpool released
                StayAliveApplication.pauseMusicOutsideApp = false;

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                finish(); //libera memoria?
            }
        });


    }


    /**
     * Method associated to the button "Rules". It explains the rules of the game.
     * @param view button
     */
    public void rules(View view){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(R.string.game_rules);
        builder1.setMessage(R.string.game_rules_explanation);
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }


    /**
     * Method that I programmatically set to the button "Results": It shows the match results:
     * Players ordered in the podium (from the survivor to the first-died player) with earnedScore.
     */
    private void showMatchResults(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(R.string.game_match_results);

        //playersDb = board.orderPlayersDbByPodiumPosition(playersDb); //Players are already ordered by podium position, I've done that right after detecting the winner.

        //Shows Playername, score EARNED, and indicates if penalty mode was Active. The players are ordered wrt dying time (From the survivor to the first-died player, that will be the last)
        String messageMatchResults = "";
        for (int i=0; i<selectedNumPlayers; i++){
            messageMatchResults += ".:" + playersDb[i].getPlaceInThePodium() + ":." + playersDb[i].getPlayerName() + " " + getResources().getString(R.string.game_results_earned) + playersDb[i].getScoreToAdd()
                    + getResources().getString(R.string.game_results_penalties) +playersDb[i].getPenalties() + ")\n\n";
        }

        if (board.isPenaltyActivated()){
            messageMatchResults += getResources().getString(R.string.game_results_penalty_on);
        }else{
            messageMatchResults += getResources().getString(R.string.game_results_penalty_off);
        }

        builder1.setMessage(messageMatchResults);
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }



    /**
     * When button "NewGame" is pressed this method is called. Starts again GameSettingsActivity, to let players make another match.
     * @param view pressed button
     */
    public void newGame(View view){
        StayAliveApplication.pauseMusicOutsideApp = false; //I'm navigating through my app so I don't want music to be paused

        Intent intent = new Intent(this, GameSettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }




    /**
     * Inflates the action bar menu
     * @param menu contains the items of the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pref_music_quick_menu, menu);

        if (StayAliveApplication.musicIsOn){
            menu.getItem(0).setTitle(R.string.menu_music_on);
        }
        else{
            menu.getItem(0).setTitle(R.string.menu_music_off);
        }
        return true;
    }

    /**
     * When user clicks on the action bar item (for example: Music(ON)) this method is called.
     * @param item the item touched
     */
    public void onMenuPrefMusicClick(MenuItem item){
        if (StayAliveApplication.musicIsOn){
            StayAliveApplication.player.pause();
            StayAliveApplication.musicIsOn = false;
            item.setTitle(R.string.menu_music_off);

        }
        else{
            StayAliveApplication.player.start();
            StayAliveApplication.musicIsOn = true;
            item.setTitle(R.string.menu_music_on);
        }
    }



    /**
     * quitMatch is called when user presses backAndroidButton OR the specific layout button "Quit the match".
     * It prompts user if he wants to quit the match and return to main menu.
     * @param view pressed button (when user presses "QuitTheMatch")
     */
    public void quitMatch(View view){
        //Builds dialogue window
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.game_really)
                .setMessage(R.string.game_really_want)
                .setPositiveButton(R.string.game_quit_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp.release(); //Soundpool releases memory.

                        Toast toast = Toast.makeText(getApplicationContext(), R.string.game_quitting, Toast.LENGTH_SHORT);
                        toast.show();
                        StayAliveApplication.pauseMusicOutsideApp = false;

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        finish();
                    }
                })
                .setNegativeButton(R.string.game_resume_game, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();

        alertDialog.show();

    }




    /**
     * Back (android) button is pressed. It goes back to the main.
     */
    public void onBackPressed() {
        //I don't call super.onBackPressed otherwise it returns immediately to main menu withouth prompting user if he wants to leave the match
        View view = new View(this);
        quitMatch(view); //quitMatch is called when user presses backAndroidButton OR the specific layout button "Quit the match"
    }




    /**
     * Called when user enters the activity. If he has pressed the home(android)button, music was paused, so I resume and continue playing it.
     */
    @Override
    protected void onResume(){
        super.onResume();
        if (StayAliveApplication.musicIsOn && !StayAliveApplication.player.isPlaying() && !StayAliveApplication.firstTimeInApp){
            StayAliveApplication.player.start();
        }
        StayAliveApplication.pauseMusicOutsideApp = true;
    }


    /**
     * Called when user leaves the activity OR has pressed homebutton (in the second case music is paused, if the MediaPlayer was playing)
     */
    @Override
    protected void onPause(){
        super.onPause();
        if (StayAliveApplication.player.isPlaying() && StayAliveApplication.pauseMusicOutsideApp){
            StayAliveApplication.player.pause();
        }
    }



    /**
     * Overridden in order to clean unused drawables
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();

        //Helps cleaning all the drawables after playing the game
        unbindDrawables(findViewById(R.id.relative_layout_main_container)); //unbinds all the views recursively (starting from the layout main container)
        System.gc(); //Garbage collector call and cleaning
        Log.d("CLEANING", "unbindDrawables and System.gc CALLED");
    }



    /**
     * This method explores the view tree recursiverly (starting from the layout container) and 1:removes
     * callbacks on all the background drawables; 2: removes childs of every viewGroup
     * @param view the main container layout (and recursively the viewGroups) in which I'm performing the cleaning.
     */
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }


    /**
     * Getter. Returns the int array with the sounds of soundpool.
     * @return array int[] with song elements
     */
    public int[] getSoundIds(){
        return soundIds;
    }


    /**
     * Getter. Returns Soundpool object.
     * @return SoundPool Obj
     */
    public SoundPool getSp(){
        return sp;
    }


    /**
     * Getter
     * @return playersDb
     */
    public Player[] getPlayersDb(){
        return playersDb;
    }



}
