package ma2015.trabocchetto.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import ma2015.trabocchetto.application.StayAliveApplication;
import ma2015.trabocchetto.model.Board;
import ma2015.trabocchetto.model.Move;
import ma2015.trabocchetto.model.Parser;
import ma2015.trabocchetto.R;
import ma2015.trabocchetto.util.Utils;


/**
 * This class defines the launcher activity. It creates the main menu in which the user can navigate, and initializes the music (only one time)
 */
public class MainActivity extends AppCompatActivity {


    MediaPlayer player;

    /**
     * When this activity starts, the system calls the onCreate() method.
     * @param savedInstanceState : it represents the serialized state of the app (useful when an activity is silently killed: I can recover the activity state)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);

        if (StayAliveApplication.musicIsOn && StayAliveApplication.firstTimeInApp) {
            Log.d("CONTROL_FLOW", "MAIN, music creation,  IsPlaying?: " + StayAliveApplication.musicIsOn);
            player = new MediaPlayer();
            player = MediaPlayer.create(this, R.raw.cloudfields);
            player.setLooping(true);
            player.setVolume(40, 40);
            player.start();
            StayAliveApplication.player = player;
            StayAliveApplication.firstTimeInApp = false;
        }


    }


    /**
     * Pressing the button 'START NEW GAME', it launches the activity 'GameSettingsActivity' passing an explicit intent
     * @param view: the pressed button
     */
    public void startNewGame(View view){
        //This attribute 'pauseMusicOutsideApp' allows me to pause the music only when the HomeButton or RecentButton is pressed. (See also overridden methods onPause() and onResume() to understand
        StayAliveApplication.pauseMusicOutsideApp = false; //When I'm navigating through my activities and my app, I set it to false
        Intent intent = new Intent(this, GameSettingsActivity.class);
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.fade_in, //Fade-Animation passing through activities
                android.R.anim.fade_out);

    }

    /**
     * On Button "ranking" pressed, launches ranking activity
     * @param view: the pressed button
     */
    public void ranking(View view){
        StayAliveApplication.pauseMusicOutsideApp = false;
        Intent intent = new Intent(this, RankingActivity.class);
        super.startActivity(intent);
        this.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    /**
     * On Button "history" pressed, launches history activity
     * @param view: the pressed button
     */
    public void history(View view){
        StayAliveApplication.pauseMusicOutsideApp = false;
        Intent intent = new Intent(this, HistoryActivity.class);
        super.startActivity(intent);
        this.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    /**
     * On Button "preferences" pressed, launches preferences activity
     * @param view: the pressed button
     */
    public void prefs(View view){
        StayAliveApplication.pauseMusicOutsideApp = false;

        Intent intent = new Intent(this, PreferencesActivity.class);
        super.startActivity(intent);
        this.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish(); //So, finish() will eventually result in onDestroy() being called (but not always).
    }


    /**
     * Asks the user if he really wants to quit the app
     */
    @Override
    public void onBackPressed(){
        //builds dialog window
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.main_exit_game_title)
                .setMessage(R.string.main_exit_game_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.main_exiting_toast, Toast.LENGTH_LONG);
                        toast.show();
                        //I don't stop/release the player because user could re-open the app from the task-manager/recentApps, causing problems
                        //I leave android to manage that: when the user do not re-open the app after a certain amount of time, the task will be killed automatically by android
                        //StayAliveApplication.player.stop();
                        //StayAliveApplication.player.release();
                        StayAliveApplication.player.pause();

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP oppure Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,
                                android.R.anim.fade_out);
                        finish();

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();

        alertDialog.show();
    }


    /**
     * If the user pressed home button and then came back to the app by touching the icon or selecting the app
     * from recent apps (task manager) this method allow to continue playing the music (is music setting was on).
     * Note that the first time the user launches the application (and onResume is called), the player.start()
     * isn't called because firstTimeInApp is true.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (StayAliveApplication.musicIsOn && !StayAliveApplication.player.isPlaying() && !StayAliveApplication.firstTimeInApp){
            StayAliveApplication.player.start();
        }
        StayAliveApplication.pauseMusicOutsideApp = true; //I put this on true because when the user touches homeButton the music will be paused (when @onPause() is called)
    }


    /**
     * This allows to pause the mediaPlayer if the user has pressed homeButton or recentButton (and only when the player is playing, so the music preference is on).
     * NB: while navigating through the app the music isn't paused because I set pauseMusicOutsideApp to false everytime I launch an intent (so everytime I'm navigating through the app)
     */
    @Override
    protected void onPause(){
        super.onPause();
        if (StayAliveApplication.player.isPlaying() && StayAliveApplication.pauseMusicOutsideApp){
            StayAliveApplication.player.pause();
        }
    }








    /**
     * This method takes as input a string (containing a complete configuration of the game:
     * the state + the moves(s)). Then, it returns either a 65-character String (resulting state,
     * in the same format as the input) or a string of the form "error:<explanation>" indicating
     * that something went wrong.
     * @param input : String of 68+ characters representing a complete gameConfiguration (state+the move(s)).
     * @return a String, either a 65-character string (resulting state) or a string indicating an error
     */
    public String moveTest(String input){

        Parser parser = null;
        try {
            parser = new Parser(input);
        } catch (Exception e) {
            return "error: "+e.getMessage();
        }

        //I'll use the board object to make the moves
        Board board= new Board(parser.gethBarPosition(),parser.getvBarPosition(),parser.getPlayers(), parser.getBeadsGrid());
        board.setMovingPlayer(parser.getMovingPlayer());

        ArrayList<Move> moves=parser.getMoves();
        for (int i=0;i<moves.size();i++){
            try {
                Move move = moves.get(i);
                board.move(move.getOrientation(), move.getBar(), move.getDirection());
            } catch (Exception e) {
                System.out.println("error:"+ e.getMessage());
                return "error: "+e.getMessage();
            }
        }

        //If nothing went wrong, I can write the 65-character-resultingState output String
        String output="";

        output=String.valueOf(board.getPLAYERS());
        output+=String.valueOf(board.getMovingPlayer());

        for(int i=0;i<7;i++){
            output+=String.valueOf(board.gethBarPosition()[i]);
        }
        for(int i=0;i<7;i++){
            output+=String.valueOf(board.getvBarPosition()[i]);
        }


        for(int i=0;i<7;i++){
            for(int j=0;j<7;j++){
                output+=String.valueOf(board.getBeadsGrid().getBeadsGrid()[i][j]);
            }
        }

        return output;
    }



    /*Not important:
    //This can be put in onCreate: the method moveTest will be called. Can use this to try method moveTest from the onCreate method.
        String input="2"
                + "1"
                + "0120120"
                + "2101102"
                + "0000100"
                + "0020000"
                + "0000001"
                + "2000000"
                + "0000000"
                + "0001000"
                + "0000000"
                +"h3i"
                +"v2o";

        String output=moveTest(input);
        System.out.println(output);
        */






}
