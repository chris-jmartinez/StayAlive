package ma2015.trabocchetto.model;

/**
 * Could be useful in future to store more details of the match and let users see them later, in history of played games, clicking in some textViews
 * to see further details
 */
public class Match {

    private int idFromDb;
    private String dateOfTheMatch;
    private String firstClassified;
    private String secondClassified;
    private String thirdClassified;
    private String fourthClassified;


    public Match(){
        this.idFromDb = 0;
        this.dateOfTheMatch = "";
        this.firstClassified = "";
        this.secondClassified = "";
        this.thirdClassified = "";
        this.fourthClassified = "";
    }

}