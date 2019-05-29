package ma2015.trabocchetto.activity;


import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ma2015.trabocchetto.R;
import ma2015.trabocchetto.application.StayAliveApplication;
import ma2015.trabocchetto.repository.SQLiteStayAliveRepository;
import ma2015.trabocchetto.repository.StayAliveRepository;
import ma2015.trabocchetto.util.Utils;

import android.view.View;
import android.widget.TableRow.LayoutParams;

/**
 * This activity shows the played matches using a table-based Layout. It automatically fills the table using a cursor,
 * requested by calling a SQLiteRepository method
 */
public class HistoryActivity extends AppCompatActivity {

    TableLayout tableLayoutHistoryMatches;
    StayAliveRepository stayAliveRepository;

    /**
     * Fills the table with database informations. I preferred to not give the possibility to cancel the played matches.
     * The user can only delete his player, but played matches remain.
     * @param savedInstanceState serialized state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_history);

        stayAliveRepository = new SQLiteStayAliveRepository(this);
        tableLayoutHistoryMatches = (TableLayout) findViewById(R.id.tableLayoutHistoryMatches);
        buildTable(); //Fills the table with data retrieved from the database
    }


    /**
     * Builds the table. rows and cols are extracted from the cursor received doing the query, and with this information I know how much rows
     * I have to fill with extracted data.
     */
    private void buildTable(){

        final int PADDING_CELL = 12;

        Cursor cursor = stayAliveRepository.findAllMatchesCursor();

        int rows = cursor.getCount();
        int cols = cursor.getColumnCount();

        cursor.moveToFirst();

        for (int i = 0; i < rows; i++){

            TableRow row = new TableRow(this);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            for (int j=0; j<cols; j++){

                //I fill the row "i" with data from database, and then I programmatically add it to the tableLayout.
                TextView textView = new TextView(this);

                textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                //textView.setBackgroundResource(R.drawable.cell_shape);  //per dargli la forma di cella
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                textView.setPadding(PADDING_CELL, PADDING_CELL, PADDING_CELL, PADDING_CELL);

                textView.setText(cursor.getString(j)); //Information extracted (number of the match, date, 1st classified...etc)

                row.addView(textView);

            }

            cursor.moveToNext();

            tableLayoutHistoryMatches.addView(row);
        }

        cursor.close();
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
