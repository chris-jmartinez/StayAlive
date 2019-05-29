package ma2015.trabocchetto.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Spinner;
import android.widget.TextView;


import ma2015.trabocchetto.R;
import ma2015.trabocchetto.application.StayAliveApplication;
import ma2015.trabocchetto.util.Utils;

/**
 * Allows user to change preferences: like the theme, effects during the game, music
 */
public class PreferencesActivity extends AppCompatActivity {

    private TextView textViewToggleMusic;
    private Switch toggleMusic;

    private TextView textViewToggleEffects;
    private Switch toggleEffects;


    /**
     * When onCreate is called, the layout is inflated and then the spinner and the toggles are set
     * according to the settings (that are mantained at application level with static attributes, through the app lifecycle)
     * @param savedInstanceState it represents the serialized state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_preferences);

        setupSpinnerItemSelection();
        setupSwitchMusic();
        setupSwitchEffects();
    }


    /**
     * According with the selected theme, it updates the spinner. When another theme is selected, changes the currentTheme,
     * saves the preference and changes the music corresponding to the selected theme.
     */
    private void setupSpinnerItemSelection() {
        Spinner spinnerThemes = (Spinner) findViewById(R.id.spinnerThemes);
        spinnerThemes.setSelection(StayAliveApplication.currentSpinnerPosition); //Sets the spinner with the current selected theme
        StayAliveApplication.currentSpinnerPosition = spinnerThemes.getSelectedItemPosition();

        spinnerThemes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //When an item is selected, this listener checks if the currentSpinnerPosition (indicator of the current theme) has changed.
            //If the currentTheme is changed, sets it and starts the corresponding music.
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (StayAliveApplication.currentSpinnerPosition != position) {
                    Utils.changeToTheme(PreferencesActivity.this, position);

                    Log.d("CONTROL_FLOW", "Theme selected:" + Utils.getTheme());

                    StayAliveApplication.player.stop();
                    StayAliveApplication.player.release();
                    MediaPlayer playerNew;
                    //int musicResIdFromAttr= Utils.getResIdFromAttribute(PreferencesActivity.this, R.attr.ambientMusic);
                    //playerNew = MediaPlayer.create(PreferencesActivity.this, musicResIdFromAttr);  //Using the attribute there were some problems in changing music
                    if (Utils.getTheme()==Utils.THEME_GRASS_FRUITS){
                        playerNew = MediaPlayer.create(PreferencesActivity.this, R.raw.cloudfields);
                    }
                    else{
                        playerNew = MediaPlayer.create(PreferencesActivity.this, R.raw.noctuary);
                    }

                    playerNew.setLooping(true);
                    playerNew.setVolume(40, 40);
                    StayAliveApplication.player = playerNew; //Stores the player at appLevel, so I can pause music anytime, when needed.
                    if (StayAliveApplication.musicIsOn){
                        StayAliveApplication.player.start();
                    }
                }
                StayAliveApplication.currentSpinnerPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     * Sets the current position of the music toggle, and sets a listener to detect when the user
     * moves the toggle to pause or resume music
     */
    public void setupSwitchMusic(){
        toggleMusic = (Switch) findViewById(R.id.toggle_music);
        toggleMusic.setChecked(StayAliveApplication.musicIsOn);

        textViewToggleMusic = (TextView) findViewById(R.id.textViewToggleMusic);
        if (StayAliveApplication.musicIsOn){
            textViewToggleMusic.setText(R.string.preferences_text_toggle_music_on);
        }
        else{
            textViewToggleMusic.setText(R.string.preferences_text_toggle_music_off);
        }

        //Pauses or resumes the music accordingly to the user choice
        toggleMusic.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    StayAliveApplication.musicIsOn = true;
                    toggleMusic.setChecked(true);
                    textViewToggleMusic.setText(R.string.preferences_text_toggle_music_on);
                    StayAliveApplication.player.start();


                } else {
                    StayAliveApplication.musicIsOn = false;
                    toggleMusic.setChecked(false);
                    textViewToggleMusic.setText(R.string.preferences_text_toggle_music_off);
                    StayAliveApplication.player.pause();


                }
            }
        });


    }



    /**
     * Sets the current position of the soundEffects (played inside the game) toggle, and sets a
     * listener to detect when the user moves the toggle in order to put on/off the effects.
     */
    public void setupSwitchEffects(){
        toggleEffects = (Switch) findViewById(R.id.toggle_effects);
        toggleEffects.setChecked(StayAliveApplication.soundEffectsAreOn);

        textViewToggleEffects = (TextView) findViewById(R.id.textViewToggleEffects);
        if (StayAliveApplication.soundEffectsAreOn){
            textViewToggleEffects.setText(R.string.preferences_text_toggle_effects_on);
        }
        else{
            textViewToggleEffects.setText(R.string.preferences_text_toggle_effects_off);
        }

        toggleEffects.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override //Listener called when the user moves the toggle
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    StayAliveApplication.soundEffectsAreOn = true;
                    toggleEffects.setChecked(true);
                    textViewToggleEffects.setText(R.string.preferences_text_toggle_effects_on);
                } else {
                    StayAliveApplication.soundEffectsAreOn = false;
                    toggleEffects.setChecked(false);
                    textViewToggleEffects.setText(R.string.preferences_text_toggle_effects_off);
                }
            }
        });
    }


    /**
     * method associated to the button (BackToMain). Returns to the MainActivity.
     * @param view the pressed button
     */
    public void backToMain(View view){
        StayAliveApplication.pauseMusicOutsideApp = false; //I'm navigating through my app, so music hasn't to be paused when onPause will be called
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
    protected void onResume(){
        super.onResume();
        if (StayAliveApplication.musicIsOn && !StayAliveApplication.player.isPlaying()){
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


}
