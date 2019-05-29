package ma2015.trabocchetto.util;


import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import ma2015.trabocchetto.R;

/**
 * Some Utils used throughout the application (get Display width, set theme, get resource id from attributes, and so on)
 */
public class Utils {


    private static int sTheme;

    public final static int THEME_GRASS_FRUITS = 0;
    public final static int THEME_SPACE_ALIENS = 1;



    public static int safeLongToInt(long l){

        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE ){
            throw new IllegalArgumentException(l + "id from database cannot be cast to Int without changing/losing its original value.");
        } //throws exception if cannot cast the long into int (used to store id of database elements).

        return (int) l;
    }

    public static int getDisplayWidth(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels; //returns device display width
    }

    /**
     * returns the resource id corresponding to the given attribute. Useful for catching the correct
     * image, for example, depending on the selected theme.
     * @param activity activity that requires the method
     * @param attr attribute to resolve
     * @return resourceId corresponding to the attribute
     */
    public static int getResIdFromAttribute(final Activity activity, final int attr)
    {
        if(attr==0)
            return 0;
        final TypedValue typedvalueattr=new TypedValue();
        activity.getTheme().resolveAttribute(attr,typedvalueattr,true);
        return typedvalueattr.resourceId;
    }

    /**
     * Changes the theme of the activity passed in input, depending on the passed int that represents the selected theme.
     * (Used in preferences activity)
     * @param activity activity that requires theme change
     * @param theme identificator of the theme
     */
    public static void changeToTheme(Activity activity, int theme){
        sTheme = theme;
        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));

        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);

    }


    /**
     * Changes the theme of the activity (used in onCreate of the activities)
     * @param activity
     */
    public static void onActivityCreateSetTheme(Activity activity){
        switch (sTheme){

            case THEME_GRASS_FRUITS:
                activity.setTheme(R.style.Theme_GrassFruits);
                break;
            case THEME_SPACE_ALIENS:
                activity.setTheme(R.style.Theme_SpaceAliens);
                break;
        }
    }


    /**
     * getter
     * @return current theme
     */
    public static int getTheme(){
        return sTheme;
    }





    /*Useful to round decimals
    public double roundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
    }
    */


    /* Useful CODE TO HIDE STATUS BAR, we must put it in all activities onCreate method
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        */



    //Code for storing in an array some images
        /*TypedArray tArray = getResources().obtainTypedArray(R.array.array_images_name);
        int count = tArray.length();
        int[] intArrayImagesName = new int[count];
        for (int i=0; i < intArrayImagesName.length; i++){
            intArrayImagesName[i] = tArray.getResourceId(i, 0);
        }
        */

    /*Tip: Nel caso ci si trovi in un fragment, scrivere invece: (ImageView)getView().findViewById(R.id.immagine);
    public void changeImage(View view){
        ImageView imgView = (ImageView)findViewById(R.id.immagine);
        imgView.setImageResource(R.drawable.blue_bar_central);
    }
    */



}
