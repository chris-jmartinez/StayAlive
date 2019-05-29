package ma2015.trabocchetto.application;

import android.app.Application;
import android.media.MediaPlayer;


/**
 * This class extends Application and contains App level variables to retain selected spinner value,
 * preferences like flags to determine if music/soundEffects are activated, the mediaPlayer for the music,
 * that will play throughout all the activities, and the pauseMusicOutsideApp, that's used to pause the music
 * when the user pushes the android homeButton or the android taskManager/Recent apps button. See onPause and
 * onResume overridden methods in mainActivity to understand how it works this variable.
 * These variables survive until the process app is killed.
 */
public class StayAliveApplication extends Application{

    //
    public static int currentSpinnerPosition;
    public static boolean musicIsOn = true;
    public static boolean soundEffectsAreOn = true;

    public static boolean firstTimeInApp = true;
    public static boolean pauseMusicOutsideApp = true;
    public static MediaPlayer player;



}
