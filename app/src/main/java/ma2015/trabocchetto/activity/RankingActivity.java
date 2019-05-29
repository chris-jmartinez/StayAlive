package ma2015.trabocchetto.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ma2015.trabocchetto.application.StayAliveApplication;
import ma2015.trabocchetto.model.Player;
import ma2015.trabocchetto.R;
import ma2015.trabocchetto.repository.StayAliveRepository;
import ma2015.trabocchetto.repository.SQLiteStayAliveRepository;
import ma2015.trabocchetto.util.Utils;

import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

import java.util.List;

/**
 * This activity shows the Ranking of the players (with a limit on the query, I put 50) using a table-based Layout.
 * It automatically fills the table using a cursor, requested by calling a SQLiteRepository method.
 * It gives also the opportunity to search a player and delete him  /  or can be also deleted all the players.
 * (The matches won't be deleted). The skill column has an alertDialog with explanation on how the skill is calculated.
 */
public class RankingActivity extends AppCompatActivity{

    TableLayout tableLayoutHighScores;
    AutoCompleteTextView playerToDeleteAutocTextView;
    Button deletePlayerButton;

    List<String> allPlayerNamesFromDb;
    StayAliveRepository stayAliveRepository;

    /**
     * Creates layout, catches the useful views, sets filters to editText to avoid user putting non-controlled data in it,
     * fills autoCompleteTextViews with data of already existing players (in order to suggest to the user the already existing players),
     * fills the table with data (using cursor)
     * @param savedInstanceState serialized state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_ranking);

        this.stayAliveRepository = new SQLiteStayAliveRepository(this);


        this.deletePlayerButton = (Button) findViewById(R.id.deletePlayerButton);
        this.tableLayoutHighScores = (TableLayout) findViewById(R.id.tableLayoutHighScores);


        this.playerToDeleteAutocTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_player_to_delete);
        playerToDeleteAutocTextView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)}); //(Nicknames are long 10 chars max)
        playerToDeleteAutocTextView.setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890")); //To avoid users writing not allowed chars
        playerToDeleteAutocTextView.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        //InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //inputManager.hideSoftInputFromWindow(playerToDeleteAutocTextView.getWindowToken(), 0);

        //query to request all PlayerNames, to fill the autocompleteTextView with data and show suggestions to the user when he's typing.
        this.allPlayerNamesFromDb = stayAliveRepository.findAllPlayerNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allPlayerNamesFromDb);
        playerToDeleteAutocTextView.setAdapter(adapter);

        buildTable();
    }


    /**
     * Builds the table with information about playerName, position in the ranking (related to Score),
     * Score, wins, losses, skillratio (clickable if user wants explanation on that)
     */
    private void buildTable(){

        final int SKILL_COLUMN = 4;
        final int PADDING_CELL = 12;

        Cursor cursor = stayAliveRepository.findAllPlayersCursor();

        int rows = cursor.getCount();
        int cols = cursor.getColumnCount();
        int rankingPosition = 1; //I will use this to keep the position in the ranking (sqlite doesn't support native variable syntax)

        cursor.moveToFirst();


        for (int i = 0; i < rows; i++){

            TableRow row = new TableRow(this);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            //I will use this to keep the position in the ranking (sqlite doesn't support native variable syntax)
            TextView textViewRanking = new TextView(this);
            textViewRanking.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textViewRanking.setGravity(Gravity.CENTER);
            textViewRanking.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            textViewRanking.setPadding(PADDING_CELL, PADDING_CELL, PADDING_CELL, PADDING_CELL);
            textViewRanking.setText(String.valueOf(rankingPosition++));
            row.addView(textViewRanking);

            for (int j=0; j<cols; j++){

                //I'll use this to show data extracted from db
                TextView textView = new TextView(this);
                textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                //textView.setBackgroundResource(R.drawable.cell_shape);  //per dargli la forma di cella
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                textView.setPadding(PADDING_CELL, PADDING_CELL, PADDING_CELL, PADDING_CELL);

                textView.setText(cursor.getString(j));

                //I want to put a dialog explaining what's the skill variable.
                if (j==SKILL_COLUMN){
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            skillExplanation(v);
                        }
                    });
                }

                row.addView(textView);

            }

            cursor.moveToNext();

            tableLayoutHighScores.addView(row);
        }

        cursor.close();

    }


    /**
     * Called when the button has pressed the button "delete player".
     * If the user has typed an existing player name, shows an alert dialog asking if he really wants to delete that player
     * @param view the button
     */
    public void deleteTypedPlayer(View view){

        if ( !playerToDeleteAutocTextView.getText().toString().equals("") ){
            final Player playerToDelete;
            playerToDelete = stayAliveRepository.retrievePlayerFromDb(playerToDeleteAutocTextView.getText().toString());

            if (playerToDelete != null){
                final AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.ranking_confirm_player_delete)
                        .setMessage(String.format(getResources().getString(R.string.ranking_really_wanna_delete_this_player), playerToDelete.getPlayerName()))
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast toast = Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.ranking_deleted_player), playerToDelete.getPlayerName()), Toast.LENGTH_SHORT);
                                toast.show();
                                stayAliveRepository.deletePlayer(playerToDelete.getIdFromDb());

                                //Intent launch (but before I set pauseMusic = false because i don't want the music to be paused navigating through my activities.)
                                StayAliveApplication.pauseMusicOutsideApp = false;
                                Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
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
            else{ //If the typed player doesn't exist, I notify that. (And If the player pushed the button without typing, I do noghing. See above).
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle(R.string.ranking_player_doesnt_exist_title);
                builder1.setMessage(R.string.ranking_player_doesnt_exist);
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
        }



    }


    /**
     * Shows alert asking if the user really wants to delete all the player (respective button "delete all the players" has been pushed
     * @param view button
     */
    public void deleteAllPlayers(View view){
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.ranking_confirm_delete_all_players_title)
                .setMessage(R.string.ranking_really_wanna_delete_all_players)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stayAliveRepository.deleteAllPlayers(); //db query

                        Toast toast = Toast.makeText(getApplicationContext(), R.string.ranking_all_players_deleted_toast, Toast.LENGTH_LONG);
                        toast.show();

                        StayAliveApplication.pauseMusicOutsideApp = false;
                        Intent intent = new Intent(getApplicationContext(), RankingActivity.class);
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
     * Explanation of the skill attribute and how is calculated for each player (dialogue)
     * @param view (pushed the TextView skill, or textViews representing the skill of the players)
     */
    public void skillExplanation(View view){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(R.string.ranking_skill_explanation_title);
        builder1.setMessage(R.string.ranking_skill_explanation_message);
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
     * method associated to the button (BackToMain). Returns to the MainActivity.
     * @param view the pressed button
     */
    public void backToMain(View view){
        StayAliveApplication.pauseMusicOutsideApp = false;

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out); //Animation
        finish();
    }



    /**
     * Back (android) button is pressed. It goes back to the main.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StayAliveApplication.pauseMusicOutsideApp = false;

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();

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






    /*USEFUL but I don't use that because inside my tablelayourt I have the column_titles(intestazione) too
    So using tableLayoutHighScores.removeAllViews() I would delete those too.

    private class MyAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            tableLayoutHighScores.removeAllViews();

            progressDialog = new ProgressDialog(RankingActivity.this);
            progressDialog.setTitle("Please Wait..");
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //String playerToCancel = playerToDelete.getText().toString();


            // inserting data

            //stayAliveRepository.delete(playerToCancel);
            buildTable();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            buildTable();
            progressDialog.dismiss();
        }
    }
     */



}
