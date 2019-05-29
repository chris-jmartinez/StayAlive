package ma2015.trabocchetto.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ma2015.trabocchetto.model.Player;
import ma2015.trabocchetto.util.Utils;


/**
 * This class manages the database: allow to extract players (to populate ranking), matches, and playerNames
 * to populate the autoCompleteTextViews with suggestions when a player is typing his nickname.
 * It allows to delete a player, or all the players, and allows also to update, save and retrieve players in the db/from
 * the db, in order to update score points, save stats and similar stuff.
 */
public class SQLiteStayAliveRepository extends SQLiteOpenHelper implements StayAliveRepository {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "stayalive";
    private static final String PLAYER_TABLE_NAME = "player";
    private static final String MATCH_TABLE_NAME = "match";

    private static final String ID_COLUMN_NAME = "id";
    private static final String PLAYERNAME_COLUMN_NAME = "playername";
    private static final String SCORE_COLUMN_NAME = "score";
    private static final String WINS_COLUMN_NAME = "wins";
    private static final String LOSSES_COLUMN_NAME = "losses";
    private static final String SKILLRATIO_COLUMN_NAME = "skillratio";

    private static final String MATCH_DATE_COLUMN_NAME = "date";
    private static final String FIRST_CLASSIFIED_COLUMN_NAME = "first";
    private static final String SECOND_CLASSIFIED_COLUMN_NAME = "second";
    private static final String THIRD_CLASSIFIED_COLUMN_NAME = "third";
    private static final String FOURTH_CLASSIFIED_COLUMN_NAME = "fourth";

    private String queryCreatePlayerTable;
    private String queryCreateMatchTable;



    public SQLiteStayAliveRepository(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    /**
     * Called the first time the app is installed. Creates the tables of the database
     * @param db SQLITEDatabase object, to perform operations.
     */
    @Override
    public void onCreate(SQLiteDatabase db){  //la funzione create prende in ingresso un SQLiteDatabase standard, che uso per creare il db
        Log.d("SQLiteTodoRepository", "onCreate() called");

        queryCreatePlayerTable = "CREATE TABLE " + PLAYER_TABLE_NAME + "(" + ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PLAYERNAME_COLUMN_NAME + " TEXT UNIQUE NOT NULL," + SCORE_COLUMN_NAME + " INTEGER NOT NULL," + WINS_COLUMN_NAME + " INTEGER NOT NULL,"
                + LOSSES_COLUMN_NAME + " INTEGER NOT NULL," + SKILLRATIO_COLUMN_NAME + " REAL NOT NULL)";


        db.execSQL(queryCreatePlayerTable); //Command that creates the table "Player"


        queryCreateMatchTable = "CREATE TABLE " + MATCH_TABLE_NAME + "(" + ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MATCH_DATE_COLUMN_NAME + " TEXT NOT NULL,"
                + FIRST_CLASSIFIED_COLUMN_NAME + " TEXT NOT NULL," + SECOND_CLASSIFIED_COLUMN_NAME + " TEXT NOT NULL,"
                + THIRD_CLASSIFIED_COLUMN_NAME + " TEXT," + FOURTH_CLASSIFIED_COLUMN_NAME + " TEXT)";

        db.execSQL(queryCreateMatchTable);
        //non si ha alcun vantaggio a chiudere il db in questo caso

    }


    /**
     * Upgrades database from older versions to newer ones (with more tables etc)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.d("SQLiteTodoRepository", "onUpgrade() called");

        switch(oldVersion){
            case 0: {
                db.execSQL(queryCreatePlayerTable);
                db.execSQL(queryCreateMatchTable);
                // we want all the updates, so no break statement here...
            }
            case 1:{
                //new tables creation...
            }
        }

        /*  Don't do this in production!:
        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MATCH_TABLE_NAME);
        onCreate(db);
         */

        /*Other tip:
        adb uninstall <yourpackagename>
        totally uninstalls the app. When you install again, you are guaranteed to hit onCreate which
        keeps me from having to keep incrementing the database version as I develop
         */

    }


    /**
     * Retrieves all player names (used to populate autocompleteTextViews with suggestions of already existing players in database)
     * @return List of Strings
     */
    public List<String> findAllPlayerNames(){
        List<String> playerNamesList = new ArrayList<>();

        String selectQuery = "SELECT " + PLAYERNAME_COLUMN_NAME + " FROM " + PLAYER_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase(); //ottengo il db
        Cursor cursor = db.rawQuery(selectQuery, null); //iteratore di tuple del database

        if (cursor.moveToFirst()) {

            do{
                String playerName;
                playerName = cursor.getString(0);
                playerNamesList.add(playerName);
            }while (cursor.moveToNext());
        }

        cursor.close(); //libero le risorse usate dal cursore
        db.close();
        return playerNamesList;
    }



    /**
     * Useful to populate the table in RankingActivity, which displays the players ordered by score
     * @return Cursor, used to populate the table automatically
     */
    public Cursor findAllPlayersCursor(){

        String selectQuery = "SELECT " + PLAYERNAME_COLUMN_NAME + ", " + SCORE_COLUMN_NAME + ", " +
                WINS_COLUMN_NAME + ", " + LOSSES_COLUMN_NAME + ", printf(\"%.1f\", " + SKILLRATIO_COLUMN_NAME + ")" +
                " FROM " + PLAYER_TABLE_NAME + " ORDER BY " + SCORE_COLUMN_NAME + " DESC LIMIT 50";

        SQLiteDatabase db = this.getWritableDatabase(); //ottengo il db
        Cursor cursor = db.rawQuery(selectQuery, null); //cursor iterabile


        if (cursor != null) {
            cursor.moveToFirst();
        }

        db.close();

        return cursor;
    }


    /**
     * Retrieves an already existing player (used in gameSettingsActivity when users are invited to insert their nicknames):
     * in case the player already exists, that player is retrieved and used to play the match
     * @param nameOfPlayerToRetrieve String (searched name in the db)
     * @return Player, if exists, otherwise null (indicating the player doesn't exists and needs to be created from scratch)
     */
    public Player retrievePlayerFromDb(String nameOfPlayerToRetrieve) {
        Player player = new Player();

        String selectQuery = "SELECT * FROM " + PLAYER_TABLE_NAME + " WHERE " + PLAYERNAME_COLUMN_NAME + " = '" + nameOfPlayerToRetrieve + "'";

        SQLiteDatabase db = this.getWritableDatabase(); //ottengo il db


        Cursor cursor;
        try {
            cursor = db.rawQuery(selectQuery, null);
        } catch (Exception e) {
            return null;
        }


        if (cursor.moveToFirst()==true){
            player.setIdFromDb(Integer.parseInt(cursor.getString(0))); //NB: ogni elemento del cursore, ha campi (numerati) tanti quanti le colonne della tabella generata dalla query
            player.setPlayerName(cursor.getString(1));
            player.setScore(Integer.parseInt(cursor.getString(2)));
            player.setWins(Integer.parseInt(cursor.getString(3)));
            player.setLosses(Integer.parseInt(cursor.getString(4)));
            player.setSkillratio(Float.parseFloat(cursor.getString(5)));
            cursor.close();
            db.close();
            return player;
        }
        else{
            cursor.close();
            db.close();
            return null;
        }



    }


    /**
     * Updates the data of a specific player in the database (updates score, wins, losses, skillratio...)
     * @param player Player that we want to update (add earned score...)
     */
    public void update(Player player){
        int idToUpdate = player.getIdFromDb();

        String selectQuery = "SELECT * FROM " + PLAYER_TABLE_NAME + " WHERE id = " + idToUpdate;

        SQLiteDatabase db = this.getWritableDatabase(); //ottengo il db
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){

            int idInDb = Integer.parseInt(cursor.getString(0));
            int scoreInDb = Integer.parseInt(cursor.getString(2));
            int winsInDb = Integer.parseInt(cursor.getString(3));
            int lossesInDb = Integer.parseInt(cursor.getString(4));
            float skillratioInDb = Float.parseFloat(cursor.getString(5));

            ContentValues values = new ContentValues();
            values.put(SCORE_COLUMN_NAME, scoreInDb + player.getScoreToAdd() );

            int totMatches = winsInDb + lossesInDb + 1;

            if (player.getIsWinner()==true){
                values.put(WINS_COLUMN_NAME, winsInDb + 1);
                values.put(SKILLRATIO_COLUMN_NAME, ( (float)(winsInDb + 1) / (float)totMatches )*100 );
            }
            else{
                values.put(LOSSES_COLUMN_NAME, lossesInDb + 1);
                values.put(SKILLRATIO_COLUMN_NAME, ( (float)(winsInDb) / (float)totMatches )*100 );
            }

            db.update(PLAYER_TABLE_NAME, values, "id=" + idToUpdate, null); //NB for me: some people use "_id" as the primary key
            Log.d("TEST", "player with name " + player.getPlayerName() + " has been updated. OldScore:" + scoreInDb + "  NewScore:" + (scoreInDb+player.getScoreToAdd()));
        }

        cursor.close();
        db.close();

    }


    /**
     * Used to create/save a new player. Gives back the just-created Player object, to let the activity store it in dbPlayers array,
     * in order to play the game.
     * @param player Player that needs to be created in database
     * @return Created Player (with database id set as 'IdFromDb' attribute).
     */
    public Player save(Player player){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues(); //crea una tupla vuota, che riempirò e metterò nel db

        values.put(PLAYERNAME_COLUMN_NAME, player.getPlayerName()); //mette i dati dentro la tupla
        values.put(SCORE_COLUMN_NAME, player.getScore());
        values.put(WINS_COLUMN_NAME, player.getWins());
        values.put(LOSSES_COLUMN_NAME, player.getLosses());
        values.put(SKILLRATIO_COLUMN_NAME, player.getSkillratio());

        long id = db.insert(PLAYER_TABLE_NAME, null, values); //inserisco la tupla nel db. L'id viene generato automaticamente e restituito

        player.setIdFromDb(Utils.safeLongToInt(id)); //l'id restituito (presente anche nel db), lo converto in int e lo imposto all'oggetto, che restituirò al chiamante e sarà pronto per essere aggiunto alla user interface mantenendo il binding col db

        db.close();

        return player; //pronto per essere aggiunto alla user interface (o nel mio caso per essere aggiunto all'array di playersDb)
    }



    /**
     * Given an id of the player, deletes him from the database.
     * @param id: id of the player we want to delete
     */
    public void deletePlayer(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PLAYER_TABLE_NAME, ID_COLUMN_NAME + " = ?", new String[]{String.valueOf(id)});
        db.close();
        //? è un parametro posizionale, e String.valueOf(id) converte l'id da int a stringa, e consente di eliminare l'elemento dal db.
    }



    /**
     * Deletes all the Players in the PLAYER_TABLE.
     */
    public void deleteAllPlayers(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ PLAYER_TABLE_NAME);

        db.close();
        //ALTERNATIVE 1: db.execSQL("delete from "+ PLAYER_TABLE_NAME);
        /*ALTERNATIVE 2:db.delete(TABLE_NAME, null, null);
        or, if you want the function to return the count of deleted rows,

        db.delete(TABLE_NAME, "1", null);
        */

        //Use Vacuum after delete command.. db.execSQL("vacuum"); this will clear all allocated spaces
    }



    /**
     * Saves the match in the database
     * @param dateOfTheMatch: date in which the match has been played
     * @param players: array of objects-Player[], used to extract the names of the players and store them in the MATCH_TABLE
     * @param selectedNumPlayers Players that participated to that match
     */
    public void saveMatch(String dateOfTheMatch, Player players[], int selectedNumPlayers){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MATCH_DATE_COLUMN_NAME, dateOfTheMatch);

        for (int i=0; i<selectedNumPlayers; i++){

            switch (i){
                case 0:
                    values.put(FIRST_CLASSIFIED_COLUMN_NAME, players[i].getPlayerName());
                    break;
                case 1:
                    values.put(SECOND_CLASSIFIED_COLUMN_NAME, players[i].getPlayerName());
                    break;
                case 2:
                    values.put(THIRD_CLASSIFIED_COLUMN_NAME, players[i].getPlayerName());
                    break;
                case 3:
                    values.put(FOURTH_CLASSIFIED_COLUMN_NAME, players[i].getPlayerName());
            }

        }

        long id = db.insert(MATCH_TABLE_NAME, null, values);

        db.close();

        //Eventually I can put also (if needed to add details to my history of played games activity):
        //match.setIdFromDb(Utils.safeLongToInt(id));
        //return match;

    }



    /**
     * Used to populate HistoryActivity table with all the played matches.
     * @return Cursor, that's used to automatically populate the tableLayout of historyOfPlayedMatches
     */
    public Cursor findAllMatchesCursor(){

        String selectQuery = "SELECT * FROM " + MATCH_TABLE_NAME + " ORDER BY " + MATCH_DATE_COLUMN_NAME + " DESC LIMIT 999";

        SQLiteDatabase db = this.getWritableDatabase(); //ottengo il db
        Cursor cursor = db.rawQuery(selectQuery, null); //restituisce iteratore/collezione di tuple del database, iterabili

        if (cursor != null) {
            cursor.moveToFirst();
        }

        db.close();

        return cursor;
    }



}
