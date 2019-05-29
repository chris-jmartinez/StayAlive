package ma2015.trabocchetto.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import ma2015.trabocchetto.model.Player;

/**
 * Interface implemented by my SQLiteRepository
 */
public interface StayAliveRepository {
    /*Se avessi creato gli oggetti SQLiteStayAliveRepository come:  SQLiteStayAliveRepository stayAliveRepository = new SQLiteStayAliveRepo,
     anzichè StayAliveRepository = new SQLiteStayAliveRepository;  allora avrei potuto usare i suoi metodi senza ogni volta doverli
     dichiarare sempre tutti qua (anche se avesse implementato l'interfaccia StayAliveRepository). Dichiarando invece come Type il fatto
     che sia uno StayAliveRepository, possono essere usati solo i metodi dichiarati qui, mentre quelli aggiuntivi dichiarati in
     SQLiteStayAliveRepository appaiono "invisibili" alle altre classi.
    */
    public void onCreate(SQLiteDatabase db);
    public List<String> findAllPlayerNames();
    public Cursor findAllPlayersCursor();
    public Player retrievePlayerFromDb(String nameOfPlayerToRetrieve);
    public Player save(Player player);
    public void update(Player player);
    public void deletePlayer(int id);
    public void deleteAllPlayers();
    public void saveMatch(String dateOfTheMatch, Player players[], int selectedNumPlayers);
    public Cursor findAllMatchesCursor();
}
