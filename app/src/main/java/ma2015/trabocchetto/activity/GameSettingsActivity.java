package ma2015.trabocchetto.activity;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import ma2015.trabocchetto.application.StayAliveApplication;
import ma2015.trabocchetto.model.Player;
import ma2015.trabocchetto.R;
import ma2015.trabocchetto.repository.SQLiteStayAliveRepository;
import ma2015.trabocchetto.repository.StayAliveRepository;
import ma2015.trabocchetto.util.Utils;
import android.widget.TableRow.LayoutParams;

import java.util.List;

/**
 * This activity allows user to define the parameters and characteristics of the Match (number of players,
 * name of the players (new or already existing players, suggested with autocompletion), penalty mode On/Off, etc)
 * OnCreates inflates the layout, which has a radioButton that allows the user to select number of players.
 * Depending on the selection, the layout is programmatically constructed to generate the necessary editText
 * (with autocompletion features for already existing players) to insert nicknames. Near each nickname there's
 * the player icon. Penalty mode is activated if user checks the respective checkbox.
 * When he clicks "StartTheGame" the players are retrieved from db, or created, and then an intent is passed to GameActivity.
 * NB: If one of the editText (autocompleteTextView) is not filled, a toast reminds to fill it. If two players choose the same name,
 * a toast reminds them to choose different names.
 */
public class GameSettingsActivity extends AppCompatActivity {

    public final static String ARRAY_PLAYERS = ".ma2015.trabocchetto.ARRAY_PLAYERS";
    public final static String SELECTED_NPLAYERS = ".ma2015.trabocchetto.SELECTED_NPLAYERS";
    public final static String PENALTY_FLAG = ".ma2015.trabocchetto.PENALTY_FLAG";
    public final static String ARRAY_INSERTED_PLAYERS_NAMES = ".ma2015.trabocchetto.ARRAY_INSERTED_PLAYERS_NAMES";
    public final static int PLAYER_ONE = 0, PLAYER_TWO = 1, PLAYER_THREE = 2, PLAYER_FOUR = 3, SCALING_FACTOR=10;

    private EditText[] editTextsPlayers;
    private AutoCompleteTextView[] autoCompleteTextViewsPlayers;
    private CheckBox checkBoxPenaltyActivated;

    private int selectedNumPlayers;
    private Player players[];

    private StayAliveRepository stayAliveRepository;


    /**
     * Inflates layout with radioButton (and backToMainButton).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_game_settings);
        //this.deleteDatabase("stayalive"); //Useful to delete database programmatically, for tests/development purposes
        this.stayAliveRepository = new SQLiteStayAliveRepository(this);
    }


    /**
     * When radioButton is clicked, depending on the number Of players is selected, respective autocompleteTextViews
     * are created, allowing user to enter nicknames of already existing players or new ones.
     * @param view radioButton.
     */
    public void onRadioButtonClicked(View view){

        //is the button now checked?
        boolean radioIsChecked = ((RadioButton) view).isChecked();

        //Check which radio button was clicked
        switch(view.getId()){

            case R.id.four_players:
                if (radioIsChecked){
                    this.selectedNumPlayers = 4;
                }
                break;
            case R.id.three_players:
                if (radioIsChecked){
                    this.selectedNumPlayers = 3;
                }
                break;
            case R.id.two_players:
                if (radioIsChecked){
                    this.selectedNumPlayers = 2;
                }
                break;
        }

        LinearLayout gameSettingsLayout = (LinearLayout) findViewById(R.id.gameSettingsLayout);
        gameSettingsLayout.removeAllViews();

        playersNicknamesInsertion(selectedNumPlayers, gameSettingsLayout);
    }


    /**
     * This method manages the programmatically constructed layout for player insertion. Generated TextViews,
     * EditTexts, imageViews (icons), buttons, etc, in order to let user insert nicknames and choose options.
     * @param selectedNumPlayers the selected number of players extracted from radio button
     * @param gameSettingsLayout root layout containing all the views
     */
    public void playersNicknamesInsertion(final int selectedNumPlayers, LinearLayout gameSettingsLayout){

        //TextView telling the user how to insert nicknames
        TextView textViewInsertNames = new TextView(this);
        textViewInsertNames.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textViewInsertNames.setText(R.string.gamesettings_text_insert_nicknames);
        textViewInsertNames.setGravity(Gravity.CENTER_HORIZONTAL);
        textViewInsertNames.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        gameSettingsLayout.addView(textViewInsertNames);

        //editTextsPlayers = new EditText[selectedNumPlayers];
        this.autoCompleteTextViewsPlayers = new AutoCompleteTextView[selectedNumPlayers];

        List<String> allPlayerNamesFromDb = stayAliveRepository.findAllPlayerNames(); //Useful for the autocompleteTextViews
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allPlayerNamesFromDb);

        for (int i = 1; i <= selectedNumPlayers; i++){
            //For each player there's an edit text to insert the name (which autocompletes with suggestions if user is typing an existing player),
            //and an icon that represents that player.

            //Creating Horizontal LinearLayout to keep editText and iconPlayer for the player "i-1"
            LinearLayout hLayoutPlayerInsertion = new LinearLayout(this);
            hLayoutPlayerInsertion.setOrientation(LinearLayout.HORIZONTAL);
            hLayoutPlayerInsertion.setGravity(Gravity.CENTER_HORIZONTAL);

            //Creating autoCompleteTextViews to let the user insert their nicknames (already existing players will be suggested via autocompletion)
            autoCompleteTextViewsPlayers[i-1] = new AutoCompleteTextView(this);
            autoCompleteTextViewsPlayers[i-1].setHint(String.format(getResources().getString(R.string.gamesettings_autocomplete_tv_hint_player), i));
            autoCompleteTextViewsPlayers[i-1].setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            autoCompleteTextViewsPlayers[i-1].setKeyListener(DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"));
            autoCompleteTextViewsPlayers[i-1].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            autoCompleteTextViewsPlayers[i-1].setAdapter(adapter);
            autoCompleteTextViewsPlayers[i-1].setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            autoCompleteTextViewsPlayers[i-1].setImeOptions(EditorInfo.IME_ACTION_DONE);
            hLayoutPlayerInsertion.addView(autoCompleteTextViewsPlayers[i-1]);

            //Creating respective player bead icon, to let user know which player he is. (dimension based on display size)
            ImageView imageViewPlayerIcon = new ImageView(this);
            imageViewPlayerIcon.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            imageViewPlayerIcon.getLayoutParams().height = Utils.getDisplayWidth(this)/SCALING_FACTOR;
            imageViewPlayerIcon.getLayoutParams().width = Utils.getDisplayWidth(this)/SCALING_FACTOR;
            //Setting the themed image icon for the player imageView:
            int drawableResIdFromAttr;
            switch(i-1){
                case PLAYER_ONE:
                    drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.beadP1);
                    imageViewPlayerIcon.setImageResource(drawableResIdFromAttr);
                    break;
                case PLAYER_TWO:
                    drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.beadP2);
                    imageViewPlayerIcon.setImageResource(drawableResIdFromAttr);
                    break;
                case PLAYER_THREE:
                    drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.beadP3);
                    imageViewPlayerIcon.setImageResource(drawableResIdFromAttr);
                    break;
                case PLAYER_FOUR:
                    drawableResIdFromAttr= Utils.getResIdFromAttribute(this, R.attr.beadP4);
                    imageViewPlayerIcon.setImageResource(drawableResIdFromAttr);
            }
            hLayoutPlayerInsertion.addView(imageViewPlayerIcon);

            //Inserting HorizontalLayout (containing EditText and Icon for the player "i") to the Vertical LinearLayout container that contains all the editTexts&Icons.
            gameSettingsLayout.addView(hLayoutPlayerInsertion);
        }

        //Creating checkbox for penalty mode.
        this.checkBoxPenaltyActivated = new CheckBox(this, null, android.R.attr.starStyle);
        checkBoxPenaltyActivated.setText(R.string.gamesettings_penalty_mode);
        checkBoxPenaltyActivated.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        if (Utils.getTheme()==0) { //Only for style purposes
            checkBoxPenaltyActivated.setTextColor(ContextCompat.getColor(this, R.color.textColorCheckBoxGF));
        }else{
            checkBoxPenaltyActivated.setTextColor(ContextCompat.getColor(this, R.color.textColorCheckBoxSA));
        }
        LinearLayout.LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 5, 0, 0);
        checkBoxPenaltyActivated.setLayoutParams(params);
        gameSettingsLayout.addView(checkBoxPenaltyActivated);


        //StartTheGame Button, clicked after inserting all the nicknames
        Button buttonStartGame = new Button(this, null, R.attr.button);
        buttonStartGame.setText(R.string.button_start_game);
        buttonStartGame.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        buttonStartGame.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        buttonStartGame.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        params.setMargins(0, 10, 0, 0);
        buttonStartGame.setLayoutParams(params);

        gameSettingsLayout.addView(buttonStartGame);

        //Back button, useful for users wanting to go back to the previous menu and select another number of players
        Button buttonBack = new Button(this, null, R.attr.button);
        buttonBack.setText(R.string.button_back);
        params.setMargins(0, 50, 0, 0);
        buttonBack.setLayoutParams(params);
        buttonBack.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        gameSettingsLayout.addView(buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StayAliveApplication.pauseMusicOutsideApp = false;
                Intent intent = new Intent(getApplicationContext(), GameSettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            }
        });


        //setting listener for the startGameButton. When clicked, it checks if the players already exist or not (retrieving them from database) and then launches intent.
        buttonStartGame.setOnClickListener(startingGame());

    }


    /**
     * When user pressed "StartTheGame" button, this listener reacts. The EditTexts (autocompleteTextViews) are checked, in order to control
     * if the user has filled all the fields, and if there weren't more fields with the same nickname. If it's all ok,
     * players are retrieved from db (or created if new), and an intent is launched.
     */
    public View.OnClickListener startingGame(){

        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                players = new Player[selectedNumPlayers]; //Will be filled with existing players and/or new ones.


                //Checks if two players or more players have the same name, or if one editText is blank. If so, toast a message and return (Do nothing else).
                for (int i = 0; i < selectedNumPlayers; i++) {
                    if (autoCompleteTextViewsPlayers[i].getText().toString().equals("")) { //  || editTextsPlayers[i].getText().toString().contains(" ")
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.gamesettings_fill_fields, Toast.LENGTH_LONG);
                        toast.show();
                        return;
                    }

                    for (int j = i + 1; j < selectedNumPlayers; j++) {
                        if (autoCompleteTextViewsPlayers[i].getText().toString().equals(autoCompleteTextViewsPlayers[j].getText().toString())) {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.gamesettings_choose_different_names, Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                    }
                }


                /*If players correctly filled the autoCompleteTextViews, collect the name of the players (maybe some of them
                already exist, and some of them need to be created from scratch*/
                String insertedPlayersNames[] = new String[selectedNumPlayers];

                for (int i = 0; i < selectedNumPlayers; i++){
                    insertedPlayersNames[i] = autoCompleteTextViewsPlayers[i].getText().toString();
                }

                //Players are now created: we can launch the intent with the following extras.
                Intent intent = new Intent(GameSettingsActivity.this, GameActivity.class);

                intent.putExtra(SELECTED_NPLAYERS, selectedNumPlayers);
                intent.putExtra(ARRAY_INSERTED_PLAYERS_NAMES, insertedPlayersNames);

                if (checkBoxPenaltyActivated.isChecked()){
                    intent.putExtra(PENALTY_FLAG, true);
                }else{
                    intent.putExtra(PENALTY_FLAG, false);
                }

                StayAliveApplication.pauseMusicOutsideApp = false; //I don't wanna pause the music while i'm navigating through my activities

                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                finish();

            }
        };
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
                android.R.anim.fade_out);
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
    public void onResume(){
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
    public void onPause(){
        super.onPause();
        if (StayAliveApplication.player.isPlaying() && StayAliveApplication.pauseMusicOutsideApp){
            StayAliveApplication.player.pause();
        }
    }


}
