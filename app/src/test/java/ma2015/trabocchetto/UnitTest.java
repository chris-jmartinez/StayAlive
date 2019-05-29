package ma2015.trabocchetto;


import org.junit.Test;

import ma2015.trabocchetto.activity.MainActivity;

import static org.junit.Assert.*;


/**
 * This class contains some tests to check the correctness of the game logic's implementation.
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UnitTest {

    //RETURNED BY METHOD MOVE IN BOARD
    private static String forbiddenBarRule1Error ="Forbidden Bar! #RULE1 You cannot move it now"; //RULE#1: You cannot move any of the bars that were slid by your opponents in the previous turn
    private static String forbiddenBarRule2Error ="Forbidden Bar! #RULE2(TwoPlayers) You cannot move it now"; //RULE#2: You cannot move the same bar more than twice when there are 2 players
    //RETURNED BY METHOD MOVE IN BOARD
    private static String invalidMovementDirectionError ="Invalid Movement Direction: already in OUTER position OR already in INNER position OR invalid direction (allowed only inwards or outwards)";
    private static String invalidMovementOrientationError ="Invalid Bar Orientation, allowed only vertical (v) or horizontal (h)";
    private static String invalidBarNumberError ="Invalid bar number";

    //RETURNED BY PARSER
    private static String invalidNumOfPlayersError = "Invalid number of players.";
    private static String invalidMovingPlayerError = "Invalid moving player.";
    private static String invalidBarPositionError = "Invalid bar position.(Allowed only positions 0,1,2 (Inner, central, outer).";


    /**
     * Starting from a given game's configuration, tests some moves specified below
     * @throws Exception
     */
    @Test
    public void testSlideExample() throws Exception {

        MainActivity mainActivity= new MainActivity();
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

        String expectedOutput = "2"
                + "1"
                + "0110120" // positions of the horizontal bars
                + "2201102" // positions of the vertical bars
                + "0000100" // beads in the grid
                + "0020000"
                + "0000000" // (a bead fell down)
                + "2000000"
                + "0000000"
                + "0001000"
                + "0000000";

        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }



    /**
     * In this test a player tries to move a forbidden bar (used by the other player in the previous turn)
     * @throws Exception
     */
    @Test
    public void testForbiddenMoves1() throws Exception {

        MainActivity mainActivity= new MainActivity();
        String input="2"
                + "1" //player1 has to move
                + "0120120"
                + "2101102"
                + "0000100"
                + "0020000"
                + "0000001"
                + "2000000"
                + "0000000"
                + "0001000"
                + "0000000"
                +"h3i" //player1 moves horizontal bar3
                +"v2o" //player2 moves vertical bar2
                +"v2i"; //player1 tries to move vertical bar2 (FORBIDDEN! because was moved by player2 in the previous turn)

        String expectedOutput ="error: "+ forbiddenBarRule1Error;

        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }


    /**
     * In this test there are only 2 players left. The first player tries to move the same bar more than two times
     * @throws Exception
     */
    @Test
    public void testForbiddenMoves2Players() throws Exception {

        MainActivity mainActivity= new MainActivity();
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
                +"v2o"
                +"h3o"
                +"v1i"
                +"h3i"; //the third horizontal bar is forbidden (moved more than two times by the same player)

        String expectedOutput ="error: "+ forbiddenBarRule2Error;

        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }


    /**
     * In this test the input string that contains the game configuration has an incorrect number of players (5)
     * @throws Exception
     */
    @Test
    public void testInvalidNumOfPlayers2() throws Exception {

        MainActivity mainActivity= new MainActivity();
        String input="5"
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
                +"h3i";

        String expectedOutput ="error: "+ invalidNumOfPlayersError;

        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }



    /**
     * In this test the input string that contains the game configuration, has an incorrect number of players (0).
     * (And there are also some beads left in the grid)
     * @throws Exception
     */
    @Test
    public void testInvalidNumOfPlayers1() throws Exception {

        MainActivity mainActivity= new MainActivity();
        String input="0"
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
                +"h3i";

        String expectedOutput ="error: "+ invalidNumOfPlayersError;

        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }



    /**
     * In this test there are three players, and player4 tries to move a bar (but he has no beads)
     * @throws Exception
     */
    @Test
    public void testInvalidMovingPlayers() throws Exception {

        MainActivity mainActivity= new MainActivity();
        String input="3"
                + "4"
                + "0120120"
                + "2101102"
                + "0000100"
                + "0020000"
                + "0000001"
                + "2000000"
                + "0000000"
                + "0001000"
                + "0000000"
                +"h3i";

        String expectedOutput ="error: "+ invalidMovingPlayerError;

        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }


    /**
     * In this test player1 tries to move, with an inward direction, a bar that's in inner position.
     * @throws Exception
     */
    @Test
    public void testMovementNotAdmitted1() throws Exception {

        MainActivity mainActivity= new MainActivity();
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
                +"h1i";

        String expectedOutput ="error: "+ invalidMovementDirectionError;


        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }


    /**
     * In this test player1 tries to move, with an outward direction, a bar that is in outer position.
     * @throws Exception
     */
    @Test
    public void testMovementNotAdmitted2() throws Exception {

        MainActivity mainActivity= new MainActivity();
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
                +"h3o";

        String expectedOutput ="error: "+ invalidMovementDirectionError;


        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }



    /**
     * In this test player1 tries to move a bar, but with an incorrect orientation ('F' isn't a vertical or horizontal bar)
     * @throws Exception
     */
    @Test
    public void testInvalidOrientation() throws Exception {

        MainActivity mainActivity= new MainActivity();
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
                +"F3i"; //invalid orientation

        String expectedOutput ="error: "+ invalidMovementOrientationError;


        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }


    /**
     * In this test player1 tries to move a bar that has an invalid number (9, so it doesn't exist)
     * @throws Exception
     */
    @Test
    public void testInvalidBarNumber() throws Exception {

        MainActivity mainActivity= new MainActivity();
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
                +"h9i";

        String expectedOutput ="error: "+ invalidBarNumberError;


        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }



    /**
     * Starting from a given game's configuration, tests some vertical bars movements (and a few beads fall)
     * @throws Exception
     */
    @Test
    public void test1() throws Exception{

        MainActivity mainActivity = new MainActivity();
        String input ="2" +
                "1" +
                "0000000" + //0inner 1central 2outer
                "0201010" +
                "0000002" +
                "0000000" +
                "0201000" +
                "2000001" +
                "0000000" +
                "0200100" +
                "0000000" +
                "v2i" + //Player1 moves vertical bar2 inwards(a bead falls)
                "v5o" + //Player2 moves vertical bar5 outwards (a bead falls)
                "v7o"; //Player1 moves vertical bar7 outwards (no beads fall)

        String expectedOutput ="2" +
                "2" +
                "0000000" +
                "0101111" +
                "0000002" +
                "0000000" +
                "0001000" +
                "2000001" +
                "0000000" +
                "0200000" +
                "0000000";

        String output = mainActivity.moveTest(input);

        assertEquals(output, expectedOutput);
    }



    /**
     * Starting from a given game's configuration, tests the moves specified below
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {

        MainActivity mainActivity= new MainActivity();
        String input="4" +
                "1" +
                "1111111" +
                "1111111" +
                "0102000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000034"+
                "h4i" + // Player 1
                "h3i" + // Player 2
                "h2i" + // Player 3
                "h1i" + // Player 4, removes 1 & 2
                "h2o" + // Player 3
                "h1o" + // Player 4
                "h5o";  // Player 3;


        String expectedOutput ="4" + "4" +
                "1100211" +
                "1111111" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000034";

        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }

    /**
     * Starting from a given game's configuration, tests the moves specified below
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {

        MainActivity mainActivity= new MainActivity();
        String input="4" +
                "1" +
                "1111111" +
                "1111111" +
                "0102000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000034"+
                "h4i" + // Player 1
                "h3i" + // Player 2
                "h2i" + // Player 3
                "h1i" + // Player 4, removes 1 & 2
                "h2o" + // Player 3
                "h1o" + // Player 4
                "h2i";  // Player 3, error


        String expectedOutput ="error: "+ forbiddenBarRule2Error;

        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }

    /**
     * Starting from a given game's configuration, tests the moves specified below
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {

        MainActivity mainActivity= new MainActivity();
        String input="4" +
                "1" +
                "1111111" +
                "1111111" +
                "0304000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000012"+
                "h4i" + // Player 1
                "h3i" + // Player 2
                "h2i" + // Player 3
                "h1i" + // Player 4, removes 3 & 4
                "h4o" + // Player 1
                "h3o" + // Player 2
                "h5o";  // Player 1


        String expectedOutput =
                "4" +
                        "2" +
                        "0011211" +
                        "1111111" +
                        "0000000" +
                        "0000000" +
                        "0000000" +
                        "0000000" +
                        "0000000" +
                        "0000000" +
                        "0000012";

        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }


    /**
     * Starting from a given game's configuration, tests the moves specified below
     * @throws Exception
     */
    @Test
    public void test5() throws Exception {

        MainActivity mainActivity= new MainActivity();
        String input="4" +
                "1" +
                "1111111" +
                "1111111" +
                "0304000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000012"+
                "h4i" + // Player 1
                "h3i" + // Player 2
                "h2i" + // Player 3
                "h1i" + // Player 4, removes 3 & 4
                "h4o" + // Player 1
                "h3o" + // Player 2
                "h4i";  // Player 1, error


        String expectedOutput = "error: "+ forbiddenBarRule2Error;
        String output=mainActivity.moveTest(input);

        assertEquals(output,expectedOutput);
    }


    /**
     * Starting from a given game's configuration, tests the moves specified below.
     * 3Players, player2 moves a bar and two beads fall. Only one player left.
     * @throws Exception
     */
    @Test
    public void test6() throws Exception{

        MainActivity mainActivity = new MainActivity();
        String input ="3" +
                "2" +
                "0100010" + //horizontal bars position: 0inner 1central 2outer
                "0101111" +
                "0000000" +
                "0000000" +
                "0001000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "2003000" +
                "h7o"; //Player2 moves horizontal bar7 outwards(two beads fall, only one player left)


        String expectedOutput ="3" +
                "2" +
                "0100011" +
                "0101111" +
                "0000000" +
                "0000000" +
                "0001000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000";

        String output = mainActivity.moveTest(input);

        assertEquals(output, expectedOutput);
    }


    /**
     * Starting from a given game's configuration, tests the moves specified below.
     * There are 4 players: player3 moves a bar and all the beads fall down.
     * @throws Exception
     */
    @Test
    public void test7() throws Exception{

        MainActivity mainActivity = new MainActivity();
        String input ="4" +
                "3" +
                "0000000" + //horizontal bars positions: 0inner 1central 2outer
                "1111221" + //vertical bars positions
                "2030401" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "h1o"; //Player3 moves horizontal bar1 outwards (4 beads fall and no one player is left)


        String expectedOutput ="4" +
                "3" +
                "1000000" + //horizontal bars positions: 0inner 1central 2outer
                "1111221" + //vertical bars positions
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000" +
                "0000000";

        String output = mainActivity.moveTest(input);

        assertEquals(output, expectedOutput);
    }


    /**
     * Starting from a given game's configuration, tests a mix of vertical/horizontal bar movements.
     * @throws Exception
     */
    @Test
    public void test8() throws Exception{

        MainActivity mainActivity = new MainActivity();
        String input ="4" +
                "3" +
                "1200100" + //horizontal bars positions: 0inner 1central 2outer
                "1111222" + //vertical bars positions
                "0300000" +
                "0000200" +
                "4000000" +
                "0010000" +
                "0000003" +
                "0000120" +
                "0040000" +
                "v6i"+ //Player 3 moves vbar6 inwards
                "h1o"+ //Player 4 moves hbar1 outwards (a player3's bead falls)
                "v5i"+ //Player 1 moves vbar5 inwards (a player1's bead falls)
                "h7o"+ //Player 2 moves hbar7 outwards
                "v1i"+ //Player 3 moves vbar1 inwards
                "h3o"+ //Player 4 moves hbar3 outwards (a player4's bead falls)
                "v5o"; //Player 1 moves vbar5 outwards


        String expectedOutput ="4" +
                "2" +
                "2210101" + //horizontal bars positions: 0inner 1central 2outer
                "0111212" + //vertical bars positions
                "0000000" +
                "0000200" +
                "0000000" +
                "0010000" +
                "0000003" +
                "0000020" +
                "0040000";

        String output = mainActivity.moveTest(input);

        assertEquals(output, expectedOutput);
    }


    /**
     * Several tests that check the functionalities and the correctness of the model.
     * @throws Exception
     */
    @Test
    public void testMartinenghi() throws Exception{

        String arg1_1_1="22101201022010220201010100000002000000000000000000000200100000000v1o";
        String result1_1_1 = new MainActivity().moveTest(arg1_1_1);
        assertTrue(result1_1_1.startsWith("error"));
        String arg1_1_2="22101201022010220201010100000002000000000000000000000200100000000h7o";
        String result1_1_2 = new MainActivity().moveTest(arg1_1_2);
        assertEquals("21101201122010220201010100000002000000000000000000000200100000000", result1_1_2);
        String arg1_2_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1o";
        String result1_2_1 = new MainActivity().moveTest(arg1_2_1);
        assertEquals("22201201122010220000000100000002000000000000000000000200100000000",result1_2_1);
        String arg1_3_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7i";
        String result1_3_1 = new MainActivity().moveTest(arg1_3_1);
        assertEquals("21201201022010220000000100000002000000000000000000000200100000000", result1_3_1);
        String arg1_4_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv1o";
        String result1_4_1 = new MainActivity().moveTest(arg1_4_1);
        assertTrue(result1_4_1.startsWith("error"));
        String arg1_4_2="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3i";
        String result1_4_2 = new MainActivity().moveTest(arg1_4_2);
        assertTrue(result1_4_2.startsWith("error"));
        String arg1_4_3="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7ih7o";
        String result1_4_3 = new MainActivity().moveTest(arg1_4_3);
        assertTrue(result1_4_3.startsWith("error"));
        String arg1_4_4="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv1o";
        String result1_4_4 = new MainActivity().moveTest(arg1_4_4);
        assertTrue(result1_4_4.startsWith("error"));
        String arg1_4_5="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3o";
        String result1_4_5 = new MainActivity().moveTest(arg1_4_5);
        assertEquals("22201201022110220000000100000002000000000000000000000000100000000", result1_4_5);
        String arg1_5_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1i";
        String result1_5_1 = new MainActivity().moveTest(arg1_5_1);
        assertEquals("21101201022110220000000100000002000000000000000000000000100000000",result1_5_1);
        String arg1_6_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7o";
        String result1_6_1 = new MainActivity().moveTest(arg1_6_1);
        assertEquals("22101201122110220000000100000002000000000000000000000000100000000", result1_6_1);
        String arg1_7_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7ov7o";
        String result1_7_1 = new MainActivity().moveTest(arg1_7_1);
        assertTrue(result1_7_1.startsWith("error"));
        String arg1_7_2="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7oh6i";
        String result1_7_2 = new MainActivity().moveTest(arg1_7_2);
        assertEquals("21101200122110220000000100000002000000000000000000000000100000000", result1_7_2);
        String arg1_8_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7oh6iv5o";
        String result1_8_1 = new MainActivity().moveTest(arg1_8_1);
        assertEquals("22101200122111220000000100000002000000000000000000000000100000000",result1_8_1);
        String arg1_9_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7oh6iv5ov6i";
        String result1_9_1 = new MainActivity().moveTest(arg1_9_1);
        assertEquals("21101200122111120000000100000002000000000000000000000000100000000", result1_9_1);
        String arg1_10_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7oh6iv5ov6ih5i";
        String result1_10_1 = new MainActivity().moveTest(arg1_10_1);
        assertTrue(result1_10_1.startsWith("error"));
        String arg1_10_2="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7oh6iv5ov6iv3i";
        String result1_10_2 = new MainActivity().moveTest(arg1_10_2);
        assertEquals("22101200122011120000000100000002000000000000000000000000100000000", result1_10_2);
        String arg1_11_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7oh6iv5ov6iv3iv4o";
        String result1_11_1 = new MainActivity().moveTest(arg1_11_1);
        assertEquals("21101200122021120000000100000002000000000000000000000000100000000",result1_11_1);
        String arg1_12_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7oh6iv5ov6iv3iv4ov1i";
        String result1_12_1 = new MainActivity().moveTest(arg1_12_1);
        assertEquals("22101200112021120000000100000002000000000000000000000000100000000",result1_12_1);
        String arg1_13_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7oh6iv5ov6iv3iv4ov1iv7i";
        String result1_13_1 = new MainActivity().moveTest(arg1_13_1);
        assertEquals("21101200112021110000000100000002000000000000000000000000100000000", result1_13_1);
        String arg1_14_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7oh6iv5ov6iv3iv4ov1iv7iv7i";
        String result1_14_1 = new MainActivity().moveTest(arg1_14_1);
        assertTrue(result1_14_1.startsWith("error"));
        String arg1_14_2="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7oh6iv5ov6iv3iv4ov1iv7ih4i";
        String result1_14_2 = new MainActivity().moveTest(arg1_14_2);
        assertEquals("22101100112021110000000100000002000000000000000000000000100000000", result1_14_2);
        String arg1_15_1="22101201022010220201010100000002000000000000000000000200100000000h7oh1oh7iv3oh1ih7oh6iv5ov6iv3iv4ov1iv7ih4iv2i";
        String result1_15_1 = new MainActivity().moveTest(arg1_15_1);
        assertEquals("22101100111021110000000100000000000000000000000000000000100000000", result1_15_1);
        String arg2_1_1="42202221211112000000010004102200300030002000031000030000000001020h2i";
        String result2_1_1 = new MainActivity().moveTest(arg2_1_1);
        assertTrue(result2_1_1.startsWith("error"));
        String arg2_1_2="42202221211112000000010004102200300030002000031000030000000001020v2o";
        String result2_1_2 = new MainActivity().moveTest(arg2_1_2);
        assertEquals("43202221212112000000010004102200300030002000001000030000000001020", result2_1_2);
        String arg2_2_1="42202221211112000000010004102200300030002000031000030000000001020v2ov2o";
        String result2_2_1 = new MainActivity().moveTest(arg2_2_1);
        assertTrue(result2_2_1.startsWith("error"));
        String arg2_2_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6o";
        String result2_2_2 = new MainActivity().moveTest(arg2_2_2);
        assertEquals("44202222212112000000010004102200300030002000001000000000000001020", result2_2_2);
        String arg2_3_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6oh5o";
        String result2_3_1 = new MainActivity().moveTest(arg2_3_1);
        assertTrue(result2_3_1.startsWith("error"));
        String arg2_3_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5i";
        String result2_3_2 = new MainActivity().moveTest(arg2_3_2);
        assertEquals("41202222212111000000010004102200300030002000001000000000000001020", result2_3_2);
        String arg2_4_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5iv5i";
        String result2_4_1 = new MainActivity().moveTest(arg2_4_1);
        assertTrue(result2_4_1.startsWith("error"));
        String arg2_4_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih6i";
        String result2_4_2 = new MainActivity().moveTest(arg2_4_2);
        assertTrue(result2_4_2.startsWith("error"));
        String arg2_4_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4i";
        String result2_4_3 = new MainActivity().moveTest(arg2_4_3);
        assertEquals("42202122212111000000010004102200300030002000001000000000000001020", result2_4_3);
        String arg2_5_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1i";
        String result2_5_1 = new MainActivity().moveTest(arg2_5_1);
        assertEquals("43102122212111000000010004102200300030002000001000000000000001020",result2_5_1);
        String arg2_6_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7i";
        String result2_6_1 = new MainActivity().moveTest(arg2_6_1);
        assertEquals("44102122112111000000010004102200300030002000001000000000000000000", result2_6_1);
        String arg2_7_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7ih1o";
        String result2_7_1 = new MainActivity().moveTest(arg2_7_1);
        assertTrue(result2_7_1.startsWith("error"));
        String arg2_7_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7ih4o";
        String result2_7_2 = new MainActivity().moveTest(arg2_7_2);
        assertTrue(result2_7_2.startsWith("error"));
        String arg2_7_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3o";
        String result2_7_3 = new MainActivity().moveTest(arg2_7_3);
        assertEquals("41102122112211000000010000102200300030002000000000000000000000000", result2_7_3);
        String arg2_8_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3ov3o";
        String result2_8_1 = new MainActivity().moveTest(arg2_8_1);
        assertTrue(result2_8_1.startsWith("error"));
        String arg2_8_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3i";
        String result2_8_2 = new MainActivity().moveTest(arg2_8_2);
        assertEquals("42101122112211000000010000102200000000002000000000000000000000000", result2_8_2);
        String arg2_9_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1i";
        String result2_9_1 = new MainActivity().moveTest(arg2_9_1);
        assertEquals("41101122102211000000010000102200000000002000000000000000000000000",result2_9_1);
        String arg2_10_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4o";
        String result2_10_1 = new MainActivity().moveTest(arg2_10_1);
        assertEquals("42101122102221000000010000102200000000002000000000000000000000000",result2_10_1);
        String arg2_11_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6i";
        String result2_11_1 = new MainActivity().moveTest(arg2_11_1);
        assertEquals("41101121102221000000010000102200000000002000000000000000000000000",result2_11_1);
        String arg2_12_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7i";
        String result2_12_1 = new MainActivity().moveTest(arg2_12_1);
        assertEquals("42101121002221000000010000102200000000002000000000000000000000000",result2_12_1);
        String arg2_13_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2i";
        String result2_13_1 = new MainActivity().moveTest(arg2_13_1);
        assertEquals("41101121001221000000010000102200000000002000000000000000000000000",result2_13_1);
        String arg2_14_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4o";
        String result2_14_1 = new MainActivity().moveTest(arg2_14_1);
        assertEquals("42101221001221000000010000102200000000000000000000000000000000000", result2_14_1);
        String arg2_15_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4oh5o";
        String result2_15_1 = new MainActivity().moveTest(arg2_15_1);
        assertTrue(result2_15_1.startsWith("error"));
        String arg2_15_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1o";
        String result2_15_2 = new MainActivity().moveTest(arg2_15_2);
        assertEquals("41101221011221000000010000102200000000000000000000000000000000000", result2_15_2);
        String arg2_16_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov1i";
        String result2_16_1 = new MainActivity().moveTest(arg2_16_1);
        assertTrue(result2_16_1.startsWith("error"));
        String arg2_16_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2o";
        String result2_16_2 = new MainActivity().moveTest(arg2_16_2);
        assertEquals("42101221012221000000010000102200000000000000000000000000000000000", result2_16_2);
        String arg2_17_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5o";
        String result2_17_1 = new MainActivity().moveTest(arg2_17_1);
        assertEquals("41101221012222000000010000102200000000000000000000000000000000000", result2_17_1);
        String arg2_18_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov3o";
        String result2_18_1 = new MainActivity().moveTest(arg2_18_1);
        assertTrue(result2_18_1.startsWith("error"));
        String arg2_18_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1o";
        String result2_18_2 = new MainActivity().moveTest(arg2_18_2);
        assertEquals("42101221022222000000010000102200000000000000000000000000000000000", result2_18_2);
        String arg2_19_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1oh5o";
        String result2_19_1 = new MainActivity().moveTest(arg2_19_1);
        assertTrue(result2_19_1.startsWith("error"));
        String arg2_19_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3i";
        String result2_19_2 = new MainActivity().moveTest(arg2_19_2);
        assertEquals("41101221022122000000010000102200000000000000000000000000000000000", result2_19_2);
        String arg2_20_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5i";
        String result2_20_1 = new MainActivity().moveTest(arg2_20_1);
        assertEquals("42101211022122000000010000102200000000000000000000000000000000000", result2_20_1);
        String arg2_21_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5iv6i";
        String result2_21_1 = new MainActivity().moveTest(arg2_21_1);
        assertTrue(result2_21_1.startsWith("error"));
        String arg2_21_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7o";
        String result2_21_2 = new MainActivity().moveTest(arg2_21_2);
        assertEquals("41101211122122000000010000102200000000000000000000000000000000000", result2_21_2);
        String arg2_22_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7ov2o";
        String result2_22_1 = new MainActivity().moveTest(arg2_22_1);
        assertTrue(result2_22_1.startsWith("error"));
        String arg2_22_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1o";
        String result2_22_2 = new MainActivity().moveTest(arg2_22_2);
        assertEquals("42201211122122000000010000102200000000000000000000000000000000000", result2_22_2);
        String arg2_23_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2i";
        String result2_23_1 = new MainActivity().moveTest(arg2_23_1);
        assertEquals("41201211121122000000010000102200000000000000000000000000000000000", result2_23_1);
        String arg2_24_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2iv5o";
        String result2_24_1 = new MainActivity().moveTest(arg2_24_1);
        assertTrue(result2_24_1.startsWith("error"));
        String arg2_24_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2iv4o";
        String result2_24_2 = new MainActivity().moveTest(arg2_24_2);
        assertTrue(result2_24_2.startsWith("error"));
        String arg2_24_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5o";
        String result2_24_3 = new MainActivity().moveTest(arg2_24_3);
        assertEquals("42201221121122000000010000102200000000000000000000000000000000000", result2_24_3);
        String arg2_25_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3o";
        String result2_25_1 = new MainActivity().moveTest(arg2_25_1);
        assertEquals("41202221121122000000010000102200000000000000000000000000000000000",result2_25_1);
        String arg2_26_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2o";
        String result2_26_1 = new MainActivity().moveTest(arg2_26_1);
        assertEquals("42202221122122000000010000102200000000000000000000000000000000000", result2_26_1);
        String arg2_27_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2oh3o";
        String result2_27_1 = new MainActivity().moveTest(arg2_27_1);
        assertTrue(result2_27_1.startsWith("error"));
        String arg2_27_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov2i";
        String result2_27_2 = new MainActivity().moveTest(arg2_27_2);
        assertTrue(result2_27_2.startsWith("error"));
        String arg2_27_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5i";
        String result2_27_3 = new MainActivity().moveTest(arg2_27_3);
        assertEquals("41202221122121000000010000102200000000000000000000000000000000000", result2_27_3);
        String arg2_28_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2o";
        String result2_28_1 = new MainActivity().moveTest(arg2_28_1);
        assertEquals("42212221122121000000010000102000000000000000000000000000000000000",result2_28_1);
        String arg2_29_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1i";
        String result2_29_1 = new MainActivity().moveTest(arg2_29_1);
        assertEquals("41112221122121000000010000102000000000000000000000000000000000000",result2_29_1);
        String arg2_30_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7i";
        String result2_30_1 = new MainActivity().moveTest(arg2_30_1);
        assertEquals("42112221022121000000010000102000000000000000000000000000000000000",result2_30_1);
        String arg2_31_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5i";
        String result2_31_1 = new MainActivity().moveTest(arg2_31_1);
        assertEquals("41112221022120000000010000102000000000000000000000000000000000000", result2_31_1);
        String arg2_32_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih3o";
        String result2_32_1 = new MainActivity().moveTest(arg2_32_1);
        assertTrue(result2_32_1.startsWith("error"));
        String arg2_32_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih4o";
        String result2_32_2 = new MainActivity().moveTest(arg2_32_2);
        assertTrue(result2_32_2.startsWith("error"));
        String arg2_32_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2i";
        String result2_32_3 = new MainActivity().moveTest(arg2_32_3);
        assertEquals("42102221022120000000010000102000000000000000000000000000000000000", result2_32_3);
        String arg2_33_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6i";
        String result2_33_1 = new MainActivity().moveTest(arg2_33_1);
        assertEquals("41102220022120000000010000102000000000000000000000000000000000000",result2_33_1);
        String arg2_34_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1i";
        String result2_34_1 = new MainActivity().moveTest(arg2_34_1);
        assertEquals("42002220022120000000010000102000000000000000000000000000000000000", result2_34_1);
        String arg2_35_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1iv5i";
        String result2_35_1 = new MainActivity().moveTest(arg2_35_1);
        assertTrue(result2_35_1.startsWith("error"));
        String arg2_35_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4i";
        String result2_35_2 = new MainActivity().moveTest(arg2_35_2);
        assertEquals("41002120022120000000010000102000000000000000000000000000000000000", result2_35_2);
        String arg2_36_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih2i";
        String result2_36_1 = new MainActivity().moveTest(arg2_36_1);
        assertTrue(result2_36_1.startsWith("error"));
        String arg2_36_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih4i";
        String result2_36_2 = new MainActivity().moveTest(arg2_36_2);
        assertTrue(result2_36_2.startsWith("error"));
        String arg2_36_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1o";
        String result2_36_3 = new MainActivity().moveTest(arg2_36_3);
        assertEquals("42102120022120000000010000102000000000000000000000000000000000000", result2_36_3);
        String arg2_37_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4o";
        String result2_37_1 = new MainActivity().moveTest(arg2_37_1);
        assertEquals("41102220022120000000010000102000000000000000000000000000000000000", result2_37_1);
        String arg2_38_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov5i";
        String result2_38_1 = new MainActivity().moveTest(arg2_38_1);
        assertTrue(result2_38_1.startsWith("error"));
        String arg2_38_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3o";
        String result2_38_2 = new MainActivity().moveTest(arg2_38_2);
        assertEquals("42102220022220000000010000102000000000000000000000000000000000000", result2_38_2);
        String arg2_39_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2o";
        String result2_39_1 = new MainActivity().moveTest(arg2_39_1);
        assertEquals("41112220022220000000010000102000000000000000000000000000000000000",result2_39_1);
        String arg2_40_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1o";
        String result2_40_1 = new MainActivity().moveTest(arg2_40_1);
        assertEquals("42212220022220000000010000102000000000000000000000000000000000000", result2_40_1);
        String arg2_41_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4o";
        String result2_41_1 = new MainActivity().moveTest(arg2_41_1);
        assertTrue(result2_41_1.startsWith("error"));
        String arg2_41_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4i";
        String result2_41_2 = new MainActivity().moveTest(arg2_41_2);
        assertEquals("41212120022220000000010000102000000000000000000000000000000000000", result2_41_2);
        String arg2_42_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7o";
        String result2_42_1 = new MainActivity().moveTest(arg2_42_1);
        assertEquals("42212120022220010000010000102000000000000000000000000000000000000", result2_42_1);
        String arg2_43_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7ov2o";
        String result2_43_1 = new MainActivity().moveTest(arg2_43_1);
        assertTrue(result2_43_1.startsWith("error"));
        String arg2_43_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh7i";
        String result2_43_2 = new MainActivity().moveTest(arg2_43_2);
        assertTrue(result2_43_2.startsWith("error"));
        String arg2_43_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6o";
        String result2_43_3 = new MainActivity().moveTest(arg2_43_3);
        assertEquals("41212121022220010000010000102000000000000000000000000000000000000", result2_43_3);
        String arg2_44_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7o";
        String result2_44_1 = new MainActivity().moveTest(arg2_44_1);
        assertEquals("42212121122220010000010000102000000000000000000000000000000000000",result2_44_1);
        String arg2_45_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4i";
        String result2_45_1 = new MainActivity().moveTest(arg2_45_1);
        assertEquals("41212021122220010000010000102000000000000000000000000000000000000", result2_45_1);
        String arg2_46_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv1o";
        String result2_46_1 = new MainActivity().moveTest(arg2_46_1);
        assertTrue(result2_46_1.startsWith("error"));
        String arg2_46_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2i";
        String result2_46_2 = new MainActivity().moveTest(arg2_46_2);
        assertEquals("42212021121220010000010000102000000000000000000000000000000000000", result2_46_2);
        String arg2_47_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7i";
        String result2_47_1 = new MainActivity().moveTest(arg2_47_1);
        assertEquals("41212021121220000000010000102000000000000000000000000000000000000",result2_47_1);
        String arg2_48_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2i";
        String result2_48_1 = new MainActivity().moveTest(arg2_48_1);
        assertEquals("42212021120220000000010000102000000000000000000000000000000000000",result2_48_1);
        String arg2_49_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2o";
        String result2_49_1 = new MainActivity().moveTest(arg2_49_1);
        assertEquals("41222021120220000000010000102000000000000000000000000000000000000",result2_49_1);
        String arg2_50_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6o";
        String result2_50_1 = new MainActivity().moveTest(arg2_50_1);
        assertEquals("42222022120220000000010000102000000000000000000000000000000000000", result2_50_1);
        String arg2_51_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov7i";
        String result2_51_1 = new MainActivity().moveTest(arg2_51_1);
        assertTrue(result2_51_1.startsWith("error"));
        String arg2_51_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6oh4i";
        String result2_51_2 = new MainActivity().moveTest(arg2_51_2);
        assertTrue(result2_51_2.startsWith("error"));
        String arg2_51_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3i";
        String result2_51_3 = new MainActivity().moveTest(arg2_51_3);
        assertEquals("41222022120120000000010000102000000000000000000000000000000000000", result2_51_3);
        String arg2_52_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3i";
        String result2_52_1 = new MainActivity().moveTest(arg2_52_1);
        assertEquals("42221022120120000000010000102000000000000000000000000000000000000",result2_52_1);
        String arg2_53_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5o";
        String result2_53_1 = new MainActivity().moveTest(arg2_53_1);
        assertEquals("41221022120121000000010000102000000000000000000000000000000000000", result2_53_1);
        String arg2_54_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5ov5i";
        String result2_54_1 = new MainActivity().moveTest(arg2_54_1);
        assertTrue(result2_54_1.startsWith("error"));
        String arg2_54_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh5o";
        String result2_54_2 = new MainActivity().moveTest(arg2_54_2);
        assertTrue(result2_54_2.startsWith("error"));
        String arg2_54_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5ov2i";
        String result2_54_3 = new MainActivity().moveTest(arg2_54_3);
        assertTrue(result2_54_3.startsWith("error"));
        String arg2_54_4="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2i";
        String result2_54_4 = new MainActivity().moveTest(arg2_54_4);
        assertEquals("42211022120121000000010000102000000000000000000000000000000000000", result2_54_4);
        String arg2_55_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3o";
        String result2_55_1 = new MainActivity().moveTest(arg2_55_1);
        assertEquals("41211022120221000000010000102000000000000000000000000000000000000",result2_55_1);
        String arg2_56_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4i";
        String result2_56_1 = new MainActivity().moveTest(arg2_56_1);
        assertEquals("42211022120211000000010000002000000000000000000000000000000000000", result2_56_1);
        String arg2_57_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv4i";
        String result2_57_1 = new MainActivity().moveTest(arg2_57_1);
        assertTrue(result2_57_1.startsWith("error"));
        String arg2_57_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2o";
        String result2_57_2 = new MainActivity().moveTest(arg2_57_2);
        assertEquals("41211022121211000000010000002000000000000000000000000000000000000", result2_57_2);
        String arg2_58_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5o";
        String result2_58_1 = new MainActivity().moveTest(arg2_58_1);
        assertEquals("42211022121212000000010000002000000000000000000000000000000000000",result2_58_1);
        String arg2_59_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5i";
        String result2_59_1 = new MainActivity().moveTest(arg2_59_1);
        assertEquals("41211012121212000000010000002000000000000000000000000000000000000",result2_59_1);
        String arg2_60_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1i";
        String result2_60_1 = new MainActivity().moveTest(arg2_60_1);
        assertEquals("42211012111212000000010000002000000000000000000000000000000000000", result2_60_1);
        String arg2_61_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1ih4i";
        String result2_61_1 = new MainActivity().moveTest(arg2_61_1);
        assertTrue(result2_61_1.startsWith("error"));
        String arg2_61_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv1o";
        String result2_61_2 = new MainActivity().moveTest(arg2_61_2);
        assertTrue(result2_61_2.startsWith("error"));
        String arg2_61_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4i";
        String result2_61_3 = new MainActivity().moveTest(arg2_61_3);
        assertEquals("41211012111202000000010000002000000000000000000000000000000000000", result2_61_3);
        String arg2_62_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1i";
        String result2_62_1 = new MainActivity().moveTest(arg2_62_1);
        assertEquals("42111012111202000000010000002000000000000000000000000000000000000",result2_62_1);
        String arg2_63_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3i";
        String result2_63_1 = new MainActivity().moveTest(arg2_63_1);
        assertEquals("41110012111202000000010000002000000000000000000000000000000000000",result2_63_1);
        String arg2_64_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7o";
        String result2_64_1 = new MainActivity().moveTest(arg2_64_1);
        assertEquals("42110012111202010000010000002000000000000000000000000000000000000",result2_64_1);
        String arg2_65_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4o";
        String result2_65_1 = new MainActivity().moveTest(arg2_65_1);
        assertEquals("41110112111202010000010000002000000000000000000000000000000000000", result2_65_1);
        String arg2_66_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh6o";
        String result2_66_1 = new MainActivity().moveTest(arg2_66_1);
        assertTrue(result2_66_1.startsWith("error"));
        String arg2_66_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh6o";
        String result2_66_2 = new MainActivity().moveTest(arg2_66_2);
        assertTrue(result2_66_2.startsWith("error"));
        String arg2_66_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1i";
        String result2_66_3 = new MainActivity().moveTest(arg2_66_3);
        assertEquals("42010112111202010000010000002000000000000000000000000000000000000", result2_66_3);
        String arg2_67_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv6i";
        String result2_67_1 = new MainActivity().moveTest(arg2_67_1);
        assertTrue(result2_67_1.startsWith("error"));
        String arg2_67_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2o";
        String result2_67_2 = new MainActivity().moveTest(arg2_67_2);
        assertEquals("41010112112202010000010000002000000000000000000000000000000000000", result2_67_2);
        String arg2_68_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2ov2i";
        String result2_68_1 = new MainActivity().moveTest(arg2_68_1);
        assertTrue(result2_68_1.startsWith("error"));
        String arg2_68_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7o";
        String result2_68_2 = new MainActivity().moveTest(arg2_68_2);
        assertEquals("42010112212202010000010000002000000000000000000000000000000000000", result2_68_2);
        String arg2_69_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5o";
        String result2_69_1 = new MainActivity().moveTest(arg2_69_1);
        assertEquals("41010122212202010000010000002000000000000000000000000000000000000",result2_69_1);
        String arg2_70_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3o";
        String result2_70_1 = new MainActivity().moveTest(arg2_70_1);
        assertEquals("42011122212202010000010000002000000000000000000000000000000000000", result2_70_1);
        String arg2_71_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3oh6o";
        String result2_71_1 = new MainActivity().moveTest(arg2_71_1);
        assertTrue(result2_71_1.startsWith("error"));
        String arg2_71_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4o";
        String result2_71_2 = new MainActivity().moveTest(arg2_71_2);
        assertEquals("41011122212212010000010000002000000000000000000000000000000000000", result2_71_2);
        String arg2_72_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5i";
        String result2_72_1 = new MainActivity().moveTest(arg2_72_1);
        assertEquals("42011122212211010000010000002000000000000000000000000000000000000",result2_72_1);
        String arg2_73_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4i";
        String result2_73_1 = new MainActivity().moveTest(arg2_73_1);
        assertEquals("41011022212211010000010000002000000000000000000000000000000000000", result2_73_1);
        String arg2_74_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5o";
        String result2_74_1 = new MainActivity().moveTest(arg2_74_1);
        assertTrue(result2_74_1.startsWith("error"));
        String arg2_74_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4iv6i";
        String result2_74_2 = new MainActivity().moveTest(arg2_74_2);
        assertTrue(result2_74_2.startsWith("error"));
        String arg2_74_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5i";
        String result2_74_3 = new MainActivity().moveTest(arg2_74_3);
        assertEquals("42011012212211010000010000002000000000000000000000000000000000000", result2_74_3);
        String arg2_75_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5iv6i";
        String result2_75_1 = new MainActivity().moveTest(arg2_75_1);
        assertTrue(result2_75_1.startsWith("error"));
        String arg2_75_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7i";
        String result2_75_2 = new MainActivity().moveTest(arg2_75_2);
        assertEquals("41011012112211010000010000002000000000000000000000000000000000000", result2_75_2);
        String arg2_76_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6i";
        String result2_76_1 = new MainActivity().moveTest(arg2_76_1);
        assertEquals("42011011112211010000010000002000000000000000000000000000000000000",result2_76_1);
        String arg2_77_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1o";
        String result2_77_1 = new MainActivity().moveTest(arg2_77_1);
        assertEquals("41011011122211010000010000002000000000000000000000000000000000000",result2_77_1);
        String arg2_78_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4o";
        String result2_78_1 = new MainActivity().moveTest(arg2_78_1);
        assertEquals("42011011122221010000010000002000000000000000000000000000000000000",result2_78_1);
        String arg2_79_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3i";
        String result2_79_1 = new MainActivity().moveTest(arg2_79_1);
        assertEquals("41010011122221010000010000002000000000000000000000000000000000000", result2_79_1);
        String arg2_80_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1o";
        String result2_80_1 = new MainActivity().moveTest(arg2_80_1);
        assertTrue(result2_80_1.startsWith("error"));
        String arg2_80_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1i";
        String result2_80_2 = new MainActivity().moveTest(arg2_80_2);
        assertEquals("42010011112221010000010000002000000000000000000000000000000000000", result2_80_2);
        String arg2_81_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5i";
        String result2_81_1 = new MainActivity().moveTest(arg2_81_1);
        assertEquals("41010011112220010000010000002000000000000000000000000000000000000",result2_81_1);
        String arg2_82_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4o";
        String result2_82_1 = new MainActivity().moveTest(arg2_82_1);
        assertEquals("42010111112220010000010000002000000000000000000000000000000000000",result2_82_1);
        String arg2_83_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1o";
        String result2_83_1 = new MainActivity().moveTest(arg2_83_1);
        assertEquals("41110111112220010000010000002000000000000000000000000000000000000",result2_83_1);
        String arg2_84_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3o";
        String result2_84_1 = new MainActivity().moveTest(arg2_84_1);
        assertEquals("42111111112220010000010000002000000000000000000000000000000000000",result2_84_1);
        String arg2_85_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5o";
        String result2_85_1 = new MainActivity().moveTest(arg2_85_1);
        assertEquals("41111121112220010000010000002000000000000000000000000000000000000",result2_85_1);
        String arg2_86_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1i";
        String result2_86_1 = new MainActivity().moveTest(arg2_86_1);
        assertEquals("42011121112220010000010000002000000000000000000000000000000000000",result2_86_1);
        String arg2_87_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7i";
        String result2_87_1 = new MainActivity().moveTest(arg2_87_1);
        assertEquals("41011121112220000000010000002000000000000000000000000000000000000",result2_87_1);
        String arg2_88_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5i";
        String result2_88_1 = new MainActivity().moveTest(arg2_88_1);
        assertEquals("42011111112220000000010000002000000000000000000000000000000000000",result2_88_1);
        String arg2_89_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7o";
        String result2_89_1 = new MainActivity().moveTest(arg2_89_1);
        assertEquals("41011111112220010000010000002000000000000000000000000000000000000",result2_89_1);
        String arg2_90_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6o";
        String result2_90_1 = new MainActivity().moveTest(arg2_90_1);
        assertEquals("42011112112220010000010000002000000000000000000000000000000000000",result2_90_1);
        String arg2_91_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1o";
        String result2_91_1 = new MainActivity().moveTest(arg2_91_1);
        assertEquals("41011112122220010000010000002000000000000000000000000000000000000",result2_91_1);
        String arg2_92_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2i";
        String result2_92_1 = new MainActivity().moveTest(arg2_92_1);
        assertEquals("42011112121220010000010000002000000000000000000000000000000000000", result2_92_1);
        String arg2_93_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2iv1o";
        String result2_93_1 = new MainActivity().moveTest(arg2_93_1);
        assertTrue(result2_93_1.startsWith("error"));
        String arg2_93_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4o";
        String result2_93_2 = new MainActivity().moveTest(arg2_93_2);
        assertEquals("41011212121220010000010000002000000000000000000000000000000000000", result2_93_2);
        String arg2_94_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7i";
        String result2_94_1 = new MainActivity().moveTest(arg2_94_1);
        assertEquals("42011212121220000000010000002000000000000000000000000000000000000", result2_94_1);
        String arg2_95_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv6i";
        String result2_95_1 = new MainActivity().moveTest(arg2_95_1);
        assertTrue(result2_95_1.startsWith("error"));
        String arg2_95_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5o";
        String result2_95_2 = new MainActivity().moveTest(arg2_95_2);
        assertEquals("41011212121221000000010000002000000000000000000000000000000000000", result2_95_2);
        String arg2_96_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov3o";
        String result2_96_1 = new MainActivity().moveTest(arg2_96_1);
        assertTrue(result2_96_1.startsWith("error"));
        String arg2_96_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7o";
        String result2_96_2 = new MainActivity().moveTest(arg2_96_2);
        assertEquals("42011212121221010000010000002000000000000000000000000000000000000", result2_96_2);
        String arg2_97_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov1o";
        String result2_97_1 = new MainActivity().moveTest(arg2_97_1);
        assertTrue(result2_97_1.startsWith("error"));
        String arg2_97_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7oh6o";
        String result2_97_2 = new MainActivity().moveTest(arg2_97_2);
        assertTrue(result2_97_2.startsWith("error"));
        String arg2_97_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov1o";
        String result2_97_3 = new MainActivity().moveTest(arg2_97_3);
        assertTrue(result2_97_3.startsWith("error"));
        String arg2_97_4="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5i";
        String result2_97_4 = new MainActivity().moveTest(arg2_97_4);
        assertEquals("41011212121220010000010000002000000000000000000000000000000000000", result2_97_4);
        String arg2_98_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3i";
        String result2_98_1 = new MainActivity().moveTest(arg2_98_1);
        assertEquals("42011212121120010000010000002000000000000000000000000000000000000",result2_98_1);
        String arg2_99_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7o";
        String result2_99_1 = new MainActivity().moveTest(arg2_99_1);
        assertEquals("41011212221120010000010000002000000000000000000000000000000000000",result2_99_1);
        String arg2_100_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6o";
        String result2_100_1 = new MainActivity().moveTest(arg2_100_1);
        assertEquals("42011212221120110000010000002000000000000000000000000000000000000", result2_100_1);
        String arg2_101_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov5i";
        String result2_101_1 = new MainActivity().moveTest(arg2_101_1);
        assertTrue(result2_101_1.startsWith("error"));
        String arg2_101_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7o";
        String result2_101_2 = new MainActivity().moveTest(arg2_101_2);
        assertEquals("41011212221120120000010000002000000000000000000000000000000000000", result2_101_2);
        String arg2_102_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7o";
        String result2_102_1 = new MainActivity().moveTest(arg2_102_1);
        assertTrue(result2_102_1.startsWith("error"));
        String arg2_102_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh6o";
        String result2_102_2 = new MainActivity().moveTest(arg2_102_2);
        assertTrue(result2_102_2.startsWith("error"));
        String arg2_102_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7ov5i";
        String result2_102_3 = new MainActivity().moveTest(arg2_102_3);
        assertTrue(result2_102_3.startsWith("error"));
        String arg2_102_4="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7i";
        String result2_102_4 = new MainActivity().moveTest(arg2_102_4);
        assertEquals("42011212121120120000010000002000000000000000000000000000000000000", result2_102_4);
        String arg2_103_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7ih7o";
        String result2_103_1 = new MainActivity().moveTest(arg2_103_1);
        assertTrue(result2_103_1.startsWith("error"));
        String arg2_103_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7iv5o";
        String result2_103_2 = new MainActivity().moveTest(arg2_103_2);
        assertEquals("41011212121121120000010000002000000000000000000000000000000000000", result2_103_2);
        String arg2_104_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7iv5oh1i";
        String result2_104_1 = new MainActivity().moveTest(arg2_104_1);
        assertTrue(result2_104_1.startsWith("error"));
        String arg2_104_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7iv5oh1i";
        String result2_104_2 = new MainActivity().moveTest(arg2_104_2);
        assertTrue(result2_104_2.startsWith("error"));
        String arg2_104_3="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7iv5ov3i";
        String result2_104_3 = new MainActivity().moveTest(arg2_104_3);
        assertEquals("42011212121021120000010000002000000000000000000000000000000000000", result2_104_3);
        String arg2_105_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7iv5ov3ih7i";
        String result2_105_1 = new MainActivity().moveTest(arg2_105_1);
        assertEquals("41011212021021120000010000002000000000000000000000000000000000000", result2_105_1);
        String arg2_106_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7iv5ov3ih7ih1i";
        String result2_106_1 = new MainActivity().moveTest(arg2_106_1);
        assertTrue(result2_106_1.startsWith("error"));
        String arg2_106_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7iv5ov3ih7iv5o";
        String result2_106_2 = new MainActivity().moveTest(arg2_106_2);
        assertEquals("42011212021022120000010000002000000000000000000000000000000000000", result2_106_2);
        String arg2_107_1="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7iv5ov3ih7iv5oh1i";
        String result2_107_1 = new MainActivity().moveTest(arg2_107_1);
        assertTrue(result2_107_1.startsWith("error"));
        String arg2_107_2="42202221211112000000010004102200300030002000031000030000000001020v2oh6ov5ih4ih1ih7iv3oh3iv1iv4oh6ih7iv2ih4ov1ov2ov5ov1ov3ih5ih7oh1ov2ih5oh3ov2ov5ih2oh1ih7iv5ih2ih6ih1ih4ih1oh4ov3oh2oh1oh4iv7oh6oh7oh4iv2iv7iv2ih2oh6ov3ih3iv5oh2iv3ov4iv2ov5oh5iv1iv4ih1ih3iv7oh4oh1iv2oh7oh5oh3ov4ov5ih4ih5ih7ih6iv1ov4oh3iv1iv5ih4oh1oh3oh5oh1iv7ih5iv7oh6ov1ov2ih4ov7iv5ov7ov5iv3ih7ov6ov7oh7iv5ov3ih7iv5oh2i";
        String result2_107_2 = new MainActivity().moveTest(arg2_107_2);
        assertEquals("42001212021022120000010000000000000000000000000000000000000000000", result2_107_2);
        String arg3_1_1="32212000000121000000300001000000220000000300202010030000100010001h1o";
        String result3_1_1 = new MainActivity().moveTest(arg3_1_1);
        assertTrue(result3_1_1.startsWith("error"));
        String arg3_1_2="32212000000121000000300001000000220000000300202010030000100010001v6o";
        String result3_1_2 = new MainActivity().moveTest(arg3_1_2);
        assertEquals("33212000000121100000300001000000220000000300202010030000100010001", result3_1_2);
        String arg3_2_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6o";
        String result3_2_1 = new MainActivity().moveTest(arg3_2_1);
        assertEquals("31212001000121100000300001000000220000000300202010030000000010001",result3_2_1);
        String arg3_3_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3i";
        String result3_3_1 = new MainActivity().moveTest(arg3_3_1);
        assertEquals("32211001000121100000300001000000020000000300202010030000000010001", result3_3_1);
        String arg3_4_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih7i";
        String result3_4_1 = new MainActivity().moveTest(arg3_4_1);
        assertTrue(result3_4_1.startsWith("error"));
        String arg3_4_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih1o";
        String result3_4_2 = new MainActivity().moveTest(arg3_4_2);
        assertTrue(result3_4_2.startsWith("error"));
        String arg3_4_3="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih7i";
        String result3_4_3 = new MainActivity().moveTest(arg3_4_3);
        assertTrue(result3_4_3.startsWith("error"));
        String arg3_4_4="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4o";
        String result3_4_4 = new MainActivity().moveTest(arg3_4_4);
        assertEquals("33211101000121100000300001000000020000000000202010030000000010001", result3_4_4);
        String arg3_5_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3i";
        String result3_5_1 = new MainActivity().moveTest(arg3_5_1);
        assertEquals("31211101000021100000300001000000020000000000200010030000000000001",result3_5_1);
        String arg3_6_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1i";
        String result3_6_1 = new MainActivity().moveTest(arg3_6_1);
        assertEquals("32111101000021100000300001000000020000000000200010030000000000001",result3_6_1);
        String arg3_7_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6i";
        String result3_7_1 = new MainActivity().moveTest(arg3_7_1);
        assertEquals("33111101000021000000300001000000020000000000200010030000000000001",result3_7_1);
        String arg3_8_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3i";
        String result3_8_1 = new MainActivity().moveTest(arg3_8_1);
        assertEquals("31110101000021000000300001000000020000000000200010030000000000001",result3_8_1);
        String arg3_9_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4o";
        String result3_9_1 = new MainActivity().moveTest(arg3_9_1);
        assertEquals("32110201000021000000300001000000020000000000200010030000000000001", result3_9_1);
        String arg3_10_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4ov3i";
        String result3_10_1 = new MainActivity().moveTest(arg3_10_1);
        assertTrue(result3_10_1.startsWith("error"));
        String arg3_10_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh5i";
        String result3_10_2 = new MainActivity().moveTest(arg3_10_2);
        assertTrue(result3_10_2.startsWith("error"));
        String arg3_10_3="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4ov4o";
        String result3_10_3 = new MainActivity().moveTest(arg3_10_3);
        assertTrue(result3_10_3.startsWith("error"));
        String arg3_10_4="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7i";
        String result3_10_4 = new MainActivity().moveTest(arg3_10_4);
        assertTrue(result3_10_4.startsWith("error"));
        String arg3_10_5="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7o";
        String result3_10_5 = new MainActivity().moveTest(arg3_10_5);
        assertEquals("33110201100021000000300001000000020000000000200010030000000000001", result3_10_5);
        String arg3_11_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh5i";
        String result3_11_1 = new MainActivity().moveTest(arg3_11_1);
        assertTrue(result3_11_1.startsWith("error"));
        String arg3_11_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6i";
        String result3_11_2 = new MainActivity().moveTest(arg3_11_2);
        assertEquals("31110200100021000000300001000000020000000000200010030000000000001", result3_11_2);
        String arg3_12_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1o";
        String result3_12_1 = new MainActivity().moveTest(arg3_12_1);
        assertEquals("32110200110021000000300001000000020000000000200010030000000000001",result3_12_1);
        String arg3_13_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2i";
        String result3_13_1 = new MainActivity().moveTest(arg3_13_1);
        assertEquals("33100200110021000000300000000000020000000000200010030000000000001",result3_13_1);
        String arg3_14_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1i";
        String result3_14_1 = new MainActivity().moveTest(arg3_14_1);
        assertEquals("31000200110021000000300000000000020000000000200010030000000000001", result3_14_1);
        String arg3_15_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih1i";
        String result3_15_1 = new MainActivity().moveTest(arg3_15_1);
        assertTrue(result3_15_1.startsWith("error"));
        String arg3_15_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6i";
        String result3_15_2 = new MainActivity().moveTest(arg3_15_2);
        assertTrue(result3_15_2.startsWith("error"));
        String arg3_15_3="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6o";
        String result3_15_3 = new MainActivity().moveTest(arg3_15_3);
        assertEquals("32000201110021000000300000000000020000000000200010030000000000001", result3_15_3);
        String arg3_16_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2o";
        String result3_16_1 = new MainActivity().moveTest(arg3_16_1);
        assertEquals("33000201111021000000300000000000020000000000200010030000000000001",result3_16_1);
        String arg3_17_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5o";
        String result3_17_1 = new MainActivity().moveTest(arg3_17_1);
        assertEquals("31000211111021000000300000000000020000000000200010030000000000001",result3_17_1);
        String arg3_18_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6o";
        String result3_18_1 = new MainActivity().moveTest(arg3_18_1);
        assertEquals("32000212111021000000300000000000020000000000200010000000000000001", result3_18_1);
        String arg3_19_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh5i";
        String result3_19_1 = new MainActivity().moveTest(arg3_19_1);
        assertTrue(result3_19_1.startsWith("error"));
        String arg3_19_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh5i";
        String result3_19_2 = new MainActivity().moveTest(arg3_19_2);
        assertTrue(result3_19_2.startsWith("error"));
        String arg3_19_3="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6ov4o";
        String result3_19_3 = new MainActivity().moveTest(arg3_19_3);
        assertTrue(result3_19_3.startsWith("error"));
        String arg3_19_4="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4i";
        String result3_19_4 = new MainActivity().moveTest(arg3_19_4);
        assertEquals("33000112111021000000300000000000020000000000200010000000000000001", result3_19_4);
        String arg3_20_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1o";
        String result3_20_1 = new MainActivity().moveTest(arg3_20_1);
        assertEquals("31000112121021000000300000000000020000000000000010000000000000001",result3_20_1);
        String arg3_21_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3o";
        String result3_21_1 = new MainActivity().moveTest(arg3_21_1);
        assertEquals("32001112121021000000300000000000020000000000000010000000000000001",result3_21_1);
        String arg3_22_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6i";
        String result3_22_1 = new MainActivity().moveTest(arg3_22_1);
        assertEquals("33001111121021000000300000000000020000000000000010000000000000001",result3_22_1);
        String arg3_23_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5o";
        String result3_23_1 = new MainActivity().moveTest(arg3_23_1);
        assertEquals("31001111121022000000300000000000020000000000000000000000000000001",result3_23_1);
        String arg3_24_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2o";
        String result3_24_1 = new MainActivity().moveTest(arg3_24_1);
        assertEquals("32001111122022000000300000000000020000000000000000000000000000001",result3_24_1);
        String arg3_25_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6o";
        String result3_25_1 = new MainActivity().moveTest(arg3_25_1);
        assertEquals("33001112122022000000300000000000020000000000000000000000000000001",result3_25_1);
        String arg3_26_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4i";
        String result3_26_1 = new MainActivity().moveTest(arg3_26_1);
        assertEquals("31001112122012000000300000000000020000000000000000000000000000001",result3_26_1);
        String arg3_27_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3i";
        String result3_27_1 = new MainActivity().moveTest(arg3_27_1);
        assertEquals("32000112122012000000300000000000020000000000000000000000000000001", result3_27_1);
        String arg3_28_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3ih2i";
        String result3_28_1 = new MainActivity().moveTest(arg3_28_1);
        assertTrue(result3_28_1.startsWith("error"));
        String arg3_28_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3ih3o";
        String result3_28_2 = new MainActivity().moveTest(arg3_28_2);
        assertTrue(result3_28_2.startsWith("error"));
        String arg3_28_3="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1o";
        String result3_28_3 = new MainActivity().moveTest(arg3_28_3);
        assertTrue(result3_28_3.startsWith("error"));
        String arg3_28_4="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1i";
        String result3_28_4 = new MainActivity().moveTest(arg3_28_4);
        assertEquals("33000112112012000000300000000000020000000000000000000000000000001", result3_28_4);
        String arg3_29_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4i";
        String result3_29_1 = new MainActivity().moveTest(arg3_29_1);
        assertEquals("31000112112002000000300000000000000000000000000000000000000000001",result3_29_1);
        String arg3_30_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5i";
        String result3_30_1 = new MainActivity().moveTest(arg3_30_1);
        assertEquals("33000102112002000000300000000000000000000000000000000000000000001",result3_30_1);
        String arg3_31_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6o";
        String result3_31_1 = new MainActivity().moveTest(arg3_31_1);
        assertEquals("31000102112002100000300000000000000000000000000000000000000000001",result3_31_1);
        String arg3_32_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3o";
        String result3_32_1 = new MainActivity().moveTest(arg3_32_1);
        assertEquals("33001102112002100000300000000000000000000000000000000000000000001",result3_32_1);
        String arg3_33_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7o";
        String result3_33_1 = new MainActivity().moveTest(arg3_33_1);
        assertEquals("31001102112002110000300000000000000000000000000000000000000000001", result3_33_1);
        String arg3_34_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7oh5i";
        String result3_34_1 = new MainActivity().moveTest(arg3_34_1);
        assertTrue(result3_34_1.startsWith("error"));
        String arg3_34_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1i";
        String result3_34_2 = new MainActivity().moveTest(arg3_34_2);
        assertEquals("33001102102002110000300000000000000000000000000000000000000000001", result3_34_2);
        String arg3_35_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv4i";
        String result3_35_1 = new MainActivity().moveTest(arg3_35_1);
        assertTrue(result3_35_1.startsWith("error"));
        String arg3_35_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7i";
        String result3_35_2 = new MainActivity().moveTest(arg3_35_2);
        assertEquals("31001102102002100000300000000000000000000000000000000000000000001", result3_35_2);
        String arg3_36_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6i";
        String result3_36_1 = new MainActivity().moveTest(arg3_36_1);
        assertEquals("33001102102002000000300000000000000000000000000000000000000000001", result3_36_1);
        String arg3_37_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv4i";
        String result3_37_1 = new MainActivity().moveTest(arg3_37_1);
        assertTrue(result3_37_1.startsWith("error"));
        String arg3_37_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv3i";
        String result3_37_2 = new MainActivity().moveTest(arg3_37_2);
        assertTrue(result3_37_2.startsWith("error"));
        String arg3_37_3="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1o";
        String result3_37_3 = new MainActivity().moveTest(arg3_37_3);
        assertEquals("31001102112002000000300000000000000000000000000000000000000000001", result3_37_3);
        String arg3_38_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3o";
        String result3_38_1 = new MainActivity().moveTest(arg3_38_1);
        assertEquals("33002102112002000000300000000000000000000000000000000000000000001",result3_38_1);
        String arg3_39_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3o";
        String result3_39_1 = new MainActivity().moveTest(arg3_39_1);
        assertEquals("31002102112102000000300000000000000000000000000000000000000000001", result3_39_1);
        String arg3_40_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3ov7i";
        String result3_40_1 = new MainActivity().moveTest(arg3_40_1);
        assertTrue(result3_40_1.startsWith("error"));
        String arg3_40_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3ov4i";
        String result3_40_2 = new MainActivity().moveTest(arg3_40_2);
        assertTrue(result3_40_2.startsWith("error"));
        String arg3_40_3="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5o";
        String result3_40_3 = new MainActivity().moveTest(arg3_40_3);
        assertEquals("33002112112102000000300000000000000000000000000000000000000000001", result3_40_3);
        String arg3_41_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov2o";
        String result3_41_1 = new MainActivity().moveTest(arg3_41_1);
        assertTrue(result3_41_1.startsWith("error"));
        String arg3_41_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov2o";
        String result3_41_2 = new MainActivity().moveTest(arg3_41_2);
        assertTrue(result3_41_2.startsWith("error"));
        String arg3_41_3="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5oh1i";
        String result3_41_3 = new MainActivity().moveTest(arg3_41_3);
        assertTrue(result3_41_3.startsWith("error"));
        String arg3_41_4="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1o";
        String result3_41_4 = new MainActivity().moveTest(arg3_41_4);
        assertEquals("31002112122102000000300000000000000000000000000000000000000000001", result3_41_4);
        String arg3_42_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3i";
        String result3_42_1 = new MainActivity().moveTest(arg3_42_1);
        assertEquals("33001112122102000000300000000000000000000000000000000000000000001", result3_42_1);
        String arg3_43_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3iv7i";
        String result3_43_1 = new MainActivity().moveTest(arg3_43_1);
        assertTrue(result3_43_1.startsWith("error"));
        String arg3_43_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4o";
        String result3_43_2 = new MainActivity().moveTest(arg3_43_2);
        assertEquals("31001212122102000000300000000000000000000000000000000000000000001", result3_43_2);
        String arg3_44_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4oh4o";
        String result3_44_1 = new MainActivity().moveTest(arg3_44_1);
        assertTrue(result3_44_1.startsWith("error"));
        String arg3_44_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6o";
        String result3_44_2 = new MainActivity().moveTest(arg3_44_2);
        assertEquals("33001212122102100000300000000000000000000000000000000000000000001", result3_44_2);
        String arg3_45_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6ov1o";
        String result3_45_1 = new MainActivity().moveTest(arg3_45_1);
        assertTrue(result3_45_1.startsWith("error"));
        String arg3_45_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6oh4o";
        String result3_45_2 = new MainActivity().moveTest(arg3_45_2);
        assertTrue(result3_45_2.startsWith("error"));
        String arg3_45_3="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6ov3o";
        String result3_45_3 = new MainActivity().moveTest(arg3_45_3);
        assertEquals("31001212122202100000300000000000000000000000000000000000000000001", result3_45_3);
        String arg3_46_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6ov3oh7i";
        String result3_46_1 = new MainActivity().moveTest(arg3_46_1);
        assertEquals("33001212022202100000300000000000000000000000000000000000000000001", result3_46_1);
        String arg3_47_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6ov3oh7ih4o";
        String result3_47_1 = new MainActivity().moveTest(arg3_47_1);
        assertTrue(result3_47_1.startsWith("error"));
        String arg3_47_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6ov3oh7ih7i";
        String result3_47_2 = new MainActivity().moveTest(arg3_47_2);
        assertTrue(result3_47_2.startsWith("error"));
        String arg3_47_3="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6ov3oh7ih6i";
        String result3_47_3 = new MainActivity().moveTest(arg3_47_3);
        assertEquals("31001211022202100000300000000000000000000000000000000000000000001", result3_47_3);
        String arg3_48_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6ov3oh7ih6iv3o";
        String result3_48_1 = new MainActivity().moveTest(arg3_48_1);
        assertTrue(result3_48_1.startsWith("error"));
        String arg3_48_2="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6ov3oh7ih6iv3i";
        String result3_48_2 = new MainActivity().moveTest(arg3_48_2);
        assertEquals("33001211022102100000300000000000000000000000000000000000000000001", result3_48_2);
        String arg3_49_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6ov3oh7ih6iv3iv2i";
        String result3_49_1 = new MainActivity().moveTest(arg3_49_1);
        assertEquals("31001211021102100000300000000000000000000000000000000000000000001",result3_49_1);
        String arg3_50_1="32212000000121000000300001000000220000000300202010030000100010001v6oh6oh3ih4ov3ih1iv6ih3ih4oh7oh6iv1oh2ih1ih6ov2oh5oh6oh4iv1oh3oh6iv5ov2oh6ov4ih3iv1iv4ih5iv6oh3ov7ov1iv7iv6iv1oh3ov3oh5ov1oh3ih4ov6ov3oh7ih6iv3iv2ih1o";
        String result3_50_1 = new MainActivity().moveTest(arg3_50_1);
        assertEquals("31101211021102100000000000000000000000000000000000000000000000001",result3_50_1);
        String arg4_1_1="33220112200020200220000000100310000000000000020000021000100000020v1o";
        String result4_1_1 = new MainActivity().moveTest(arg4_1_1);
        assertEquals("31220112210020200220000000100310000000000000020000001000100000020",result4_1_1);
        String arg4_2_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2i";
        String result4_2_1 = new MainActivity().moveTest(arg4_2_1);
        assertEquals("32210112210020200220000000100010000000000000020000001000100000020", result4_2_1);
        String arg4_3_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2iv4o";
        String result4_3_1 = new MainActivity().moveTest(arg4_3_1);
        assertTrue(result4_3_1.startsWith("error"));
        String arg4_3_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5o";
        String result4_3_2 = new MainActivity().moveTest(arg4_3_2);
        assertEquals("31210122210020200220000000100010000000000000020000001000100000020", result4_3_2);
        String arg4_4_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5ov7i";
        String result4_4_1 = new MainActivity().moveTest(arg4_4_1);
        assertTrue(result4_4_1.startsWith("error"));
        String arg4_4_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6i";
        String result4_4_2 = new MainActivity().moveTest(arg4_4_2);
        assertEquals("32210121210020200220000000100010000000000000020000001000100000020", result4_4_2);
        String arg4_5_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2i";
        String result4_5_1 = new MainActivity().moveTest(arg4_5_1);
        assertEquals("31200121210020200220000000100010000000000000020000001000100000020", result4_5_1);
        String arg4_6_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2ih1o";
        String result4_6_1 = new MainActivity().moveTest(arg4_6_1);
        assertTrue(result4_6_1.startsWith("error"));
        String arg4_6_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6i";
        String result4_6_2 = new MainActivity().moveTest(arg4_6_2);
        assertEquals("32200121210020100220000000100010000000000000020000001000000000020", result4_6_2);
        String arg4_7_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7o";
        String result4_7_1 = new MainActivity().moveTest(arg4_7_1);
        assertEquals("31200121210020110220000000100010000000000000020000001000000000020",result4_7_1);
        String arg4_8_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4o";
        String result4_8_1 = new MainActivity().moveTest(arg4_8_1);
        assertEquals("32200221210020110220000000100010000000000000020000001000000000020", result4_8_1);
        String arg4_9_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4oh1o";
        String result4_9_1 = new MainActivity().moveTest(arg4_9_1);
        assertTrue(result4_9_1.startsWith("error"));
        String arg4_9_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov4o";
        String result4_9_2 = new MainActivity().moveTest(arg4_9_2);
        assertTrue(result4_9_2.startsWith("error"));
        String arg4_9_3="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3o";
        String result4_9_3 = new MainActivity().moveTest(arg4_9_3);
        assertEquals("31200221210120110220000000100010000000000000020000001000000000020", result4_9_3);
        String arg4_10_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3oh1o";
        String result4_10_1 = new MainActivity().moveTest(arg4_10_1);
        assertTrue(result4_10_1.startsWith("error"));
        String arg4_10_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3oh3i";
        String result4_10_2 = new MainActivity().moveTest(arg4_10_2);
        assertTrue(result4_10_2.startsWith("error"));
        String arg4_10_3="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3oh7o";
        String result4_10_3 = new MainActivity().moveTest(arg4_10_3);
        assertTrue(result4_10_3.startsWith("error"));
        String arg4_10_4="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4i";
        String result4_10_4 = new MainActivity().moveTest(arg4_10_4);
        assertEquals("32200221210110110220000000100010000000000000020000001000000000020", result4_10_4);
        String arg4_11_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6i";
        String result4_11_1 = new MainActivity().moveTest(arg4_11_1);
        assertEquals("31200221210110010220000000100010000000000000020000001000000000020", result4_11_1);
        String arg4_12_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6iv6i";
        String result4_12_1 = new MainActivity().moveTest(arg4_12_1);
        assertTrue(result4_12_1.startsWith("error"));

        //TEST 4.12.2
        String arg4_12_2="3" + //number of initial players
                "3" +          //player that has to move
                "2201122" +    //horizontal bars positions
                "0002020" +    //vertical bars positions
                "0220000" + //beadsGrid
                "0001003" +
                "1000000" +
                "0000000" +
                "0200000" +
                "2100010" +
                "0000020" +
                "v1o" +
                "h2i" +
                "h5o" +
                "h6i" +
                "h2i" +
                "v6i" +
                "v7o" +
                "h4o" +
                "v3o" +
                "v4i" +
                "v6i" +
                "h4i";

        String result4_12_2 = new MainActivity().moveTest(arg4_12_2);

        String expectedOutput = "3" +  //number of initial players
                "2" +                 //player that have to move (final)
                "2001212" + //hBars
                "1011001" + //vBars
                "0220000" +
                "0001000" +
                "1000000" +
                "0000000" +
                "0200000" +
                "0100000" +
                "0000020";

        assertEquals(expectedOutput, result4_12_2);




        String arg4_13_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih7o";
        String result4_13_1 = new MainActivity().moveTest(arg4_13_1);
        assertTrue(result4_13_1.startsWith("error"));
        String arg4_13_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih4o";
        String result4_13_2 = new MainActivity().moveTest(arg4_13_2);
        assertTrue(result4_13_2.startsWith("error"));
        String arg4_13_3="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih7o";
        String result4_13_3 = new MainActivity().moveTest(arg4_13_3);
        assertTrue(result4_13_3.startsWith("error"));
        String arg4_13_4="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1i";
        String result4_13_4 = new MainActivity().moveTest(arg4_13_4);
        assertEquals("31100121210110010200000000100010000000000000020000001000000000020",result4_13_4);
        String arg4_14_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3o";
        String result4_14_1 = new MainActivity().moveTest(arg4_14_1);
        assertEquals("32100121210210010200000000100010000000000000020000001000000000020",result4_14_1);
        String arg4_15_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5o";
        String result4_15_1 = new MainActivity().moveTest(arg4_15_1);
        assertEquals("31100121210211010200000000100010000000000000020000001000000000020",result4_15_1);
        String arg4_16_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov3o";
        String result4_16_1 = new MainActivity().moveTest(arg4_16_1);
        assertTrue(result4_16_1.startsWith("error"));
        String arg4_16_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1i";
        String result4_16_2 = new MainActivity().moveTest(arg4_16_2);
        assertEquals("32100121200211010200000000100010000000000000020000001000000000020",result4_16_2);
        String arg4_17_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7o";
        String result4_17_1 = new MainActivity().moveTest(arg4_17_1);
        assertEquals("31100121200211020200000000100010000000000000020000001000000000020",result4_17_1);
        String arg4_18_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5i";
        String result4_18_1 = new MainActivity().moveTest(arg4_18_1);
        assertEquals("32100121200210020200000000100010000000000000020000001000000000020",result4_18_1);
        String arg4_19_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7i";
        String result4_19_1 = new MainActivity().moveTest(arg4_19_1);
        assertEquals("31100121100210020200000000100010000000000000020000001000000000000",result4_19_1);
        String arg4_20_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1o";
        String result4_20_1 = new MainActivity().moveTest(arg4_20_1);
        assertEquals("32100121110210020200000000100010000000000000020000001000000000000", result4_20_1);
        String arg4_21_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov5i";
        String result4_21_1 = new MainActivity().moveTest(arg4_21_1);
        assertTrue(result4_21_1.startsWith("error"));
        String arg4_21_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov5i";
        String result4_21_2 = new MainActivity().moveTest(arg4_21_2);
        assertTrue(result4_21_2.startsWith("error"));
        String arg4_21_3="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov1o";
        String result4_21_3 = new MainActivity().moveTest(arg4_21_3);
        assertTrue(result4_21_3.startsWith("error"));
        String arg4_21_4="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2i";
        String result4_21_4 = new MainActivity().moveTest(arg4_21_4);
        assertTrue(result4_21_4.startsWith("error"));
        String arg4_21_5="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2o";
        String result4_21_5 = new MainActivity().moveTest(arg4_21_5);
        assertEquals("31100121111210020200000000100010000000000000020000000000000000000",result4_21_5);
        String arg4_22_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3i";
        String result4_22_1 = new MainActivity().moveTest(arg4_22_1);
        assertEquals("32100121111110020200000000100010000000000000020000000000000000000",result4_22_1);
        String arg4_23_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1o";
        String result4_23_1 = new MainActivity().moveTest(arg4_23_1);
        assertEquals("31100121121110020200000000100010000000000000020000000000000000000",result4_23_1);
        String arg4_24_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2i";
        String result4_24_1 = new MainActivity().moveTest(arg4_24_1);
        assertEquals("32100121120110020200000000100010000000000000020000000000000000000",result4_24_1);
        String arg4_25_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4i";
        String result4_25_1 = new MainActivity().moveTest(arg4_25_1);
        assertEquals("31100021120110020200000000100010000000000000020000000000000000000",result4_25_1);
        String arg4_26_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5o";
        String result4_26_1 = new MainActivity().moveTest(arg4_26_1);
        assertEquals("32100021120111020200000000100010000000000000020000000000000000000",result4_26_1);
        String arg4_27_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3o";
        String result4_27_1 = new MainActivity().moveTest(arg4_27_1);
        assertEquals("31100021120211020200000000100010000000000000020000000000000000000",result4_27_1);
        String arg4_28_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6o";
        String result4_28_1 = new MainActivity().moveTest(arg4_28_1);
        assertEquals("32100021120211120200000000100010000000000000020000000000000000000",result4_28_1);
        String arg4_29_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2o";
        String result4_29_1 = new MainActivity().moveTest(arg4_29_1);
        assertEquals("31110021120211120200000000000010000000000000020000000000000000000",result4_29_1);
        String arg4_30_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6o";
        String result4_30_1 = new MainActivity().moveTest(arg4_30_1);
        assertEquals("32110021120211220200000000000010000000000000020000000000000000000", result4_30_1);
        String arg4_31_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov1o";
        String result4_31_1 = new MainActivity().moveTest(arg4_31_1);
        assertTrue(result4_31_1.startsWith("error"));
        String arg4_31_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7i";
        String result4_31_2 = new MainActivity().moveTest(arg4_31_2);
        assertEquals("31110021120211210200000000000010000000000000020000000000000000000",result4_31_2);
        String arg4_32_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2o";
        String result4_32_1 = new MainActivity().moveTest(arg4_32_1);
        assertEquals("32120021120211210200000000000010000000000000020000000000000000000", result4_32_1);
        String arg4_33_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2ov6o";
        String result4_33_1 = new MainActivity().moveTest(arg4_33_1);
        assertTrue(result4_33_1.startsWith("error"));
        String arg4_33_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2ov6o";
        String result4_33_2 = new MainActivity().moveTest(arg4_33_2);
        assertTrue(result4_33_2.startsWith("error"));
        String arg4_33_3="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6i";
        String result4_33_3 = new MainActivity().moveTest(arg4_33_3);
        assertEquals("31120020120211210200000000000010000000000000020000000000000000000",result4_33_3);
        String arg4_34_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1i";
        String result4_34_1 = new MainActivity().moveTest(arg4_34_1);
        assertEquals("32120020110211210200000000000010000000000000020000000000000000000",result4_34_1);
        String arg4_35_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6o";
        String result4_35_1 = new MainActivity().moveTest(arg4_35_1);
        assertEquals("31120021110211210200000000000010000000000000020000000000000000000",result4_35_1);
        String arg4_36_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1i";
        String result4_36_1 = new MainActivity().moveTest(arg4_36_1);
        assertEquals("32120021100211210200000000000010000000000000020000000000000000000",result4_36_1);
        String arg4_37_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7i";
        String result4_37_1 = new MainActivity().moveTest(arg4_37_1);
        assertEquals("31120021100211200200000000000010000000000000020000000000000000000", result4_37_1);
        String arg4_38_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih4i";
        String result4_38_1 = new MainActivity().moveTest(arg4_38_1);
        assertTrue(result4_38_1.startsWith("error"));
        String arg4_38_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6o";
        String result4_38_2 = new MainActivity().moveTest(arg4_38_2);
        assertEquals("32120022100211200200000000000010000000000000020000000000000000000", result4_38_2);
        String arg4_39_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6oh6i";
        String result4_39_1 = new MainActivity().moveTest(arg4_39_1);
        assertTrue(result4_39_1.startsWith("error"));
        String arg4_39_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4i";
        String result4_39_2 = new MainActivity().moveTest(arg4_39_2);
        assertEquals("31120022100201200200000000000010000000000000020000000000000000000", result4_39_2);
        String arg4_40_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4iv1i";
        String result4_40_1 = new MainActivity().moveTest(arg4_40_1);
        assertTrue(result4_40_1.startsWith("error"));
        String arg4_40_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih3i";
        String result4_40_2 = new MainActivity().moveTest(arg4_40_2);
        assertTrue(result4_40_2.startsWith("error"));
        String arg4_40_3="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7o";
        String result4_40_3 = new MainActivity().moveTest(arg4_40_3);
        assertEquals("32120022200201200200000000000010000000000000020000000000000000000", result4_40_3);
        String arg4_41_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh7o";
        String result4_41_1 = new MainActivity().moveTest(arg4_41_1);
        assertTrue(result4_41_1.startsWith("error"));
        String arg4_41_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1i";
        String result4_41_2 = new MainActivity().moveTest(arg4_41_2);
        assertEquals("31020022200201200200000000000010000000000000020000000000000000000", result4_41_2);
        String arg4_42_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv6o";
        String result4_42_1 = new MainActivity().moveTest(arg4_42_1);
        assertTrue(result4_42_1.startsWith("error"));
        String arg4_42_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1ih2o";
        String result4_42_2 = new MainActivity().moveTest(arg4_42_2);
        assertTrue(result4_42_2.startsWith("error"));
        String arg4_42_3="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2o";
        String result4_42_3 = new MainActivity().moveTest(arg4_42_3);
        assertEquals("32020022201201200000000000000010000000000000020000000000000000000", result4_42_3);
        String arg4_43_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2ov2o";
        String result4_43_1 = new MainActivity().moveTest(arg4_43_1);
        assertTrue(result4_43_1.startsWith("error"));
        String arg4_43_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2ov2o";
        String result4_43_2 = new MainActivity().moveTest(arg4_43_2);
        assertTrue(result4_43_2.startsWith("error"));
        String arg4_43_3="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2ov5i";
        String result4_43_3 = new MainActivity().moveTest(arg4_43_3);
        assertEquals("31020022201200200000000000000010000000000000020000000000000000000", result4_43_3);
        String arg4_44_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2ov5iv6o";
        String result4_44_1 = new MainActivity().moveTest(arg4_44_1);
        assertTrue(result4_44_1.startsWith("error"));
        String arg4_44_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2ov5iv4i";
        String result4_44_2 = new MainActivity().moveTest(arg4_44_2);
        assertTrue(result4_44_2.startsWith("error"));
        String arg4_44_3="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2ov5iv3i";
        String result4_44_3 = new MainActivity().moveTest(arg4_44_3);
        assertEquals("32020022201100200000000000000010000000000000020000000000000000000", result4_44_3);
        String arg4_45_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2ov5iv3ih2o";
        String result4_45_1 = new MainActivity().moveTest(arg4_45_1);
        assertTrue(result4_45_1.startsWith("error"));
        String arg4_45_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2ov5iv3iv2i";
        String result4_45_2 = new MainActivity().moveTest(arg4_45_2);
        assertEquals("31020022200100200000000000000010000000000000020000000000000000000", result4_45_2);
        String arg4_46_1="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2ov5iv3iv2ih4i";
        String result4_46_1 = new MainActivity().moveTest(arg4_46_1);
        assertTrue(result4_46_1.startsWith("error"));
        String arg4_46_2="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2ov5iv3iv2iv4i";
        String result4_46_2 = new MainActivity().moveTest(arg4_46_2);
        assertTrue(result4_46_2.startsWith("error"));
        String arg4_46_3="33220112200020200220000000100310000000000000020000021000100000020v1oh2ih5oh6ih2iv6iv7oh4ov3ov4iv6ih4ih1iv3ov5ov1iv7ov5ih7iv1ov2ov3iv1ov2ih4iv5ov3ov6oh2ov6ov7ih2oh6iv1ih6ov1iv7ih6ov4ih7oh1iv2ov5iv3iv2ih3o";
        String result4_46_3 = new MainActivity().moveTest(arg4_46_3);
        assertEquals("31021022200100200000000000000000000000000000020000000000000000000", result4_46_3);
        String arg5_1_1="21120110011220200010000000000000000000100000000000000000000002002v3o";
        String result5_1_1 = new MainActivity().moveTest(arg5_1_1);
        assertTrue(result5_1_1.startsWith("error"));
        String arg5_1_2="21120110011220200010000000000000000000100000000000000000000002002v5o";
        String result5_1_2 = new MainActivity().moveTest(arg5_1_2);
        assertEquals("22120110011221200010000000000000000000100000000000000000000002002", result5_1_2);
        String arg5_2_1="21120110011220200010000000000000000000100000000000000000000002002v5oh3i";
        String result5_2_1 = new MainActivity().moveTest(arg5_2_1);
        assertTrue(result5_2_1.startsWith("error"));
        String arg5_2_2="21120110011220200010000000000000000000100000000000000000000002002v5ov2o";
        String result5_2_2 = new MainActivity().moveTest(arg5_2_2);
        assertEquals("21120110012221200010000000000000000000100000000000000000000002002",result5_2_2);
        String arg5_3_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2i";
        String result5_3_1 = new MainActivity().moveTest(arg5_3_1);
        assertEquals("22110110012221200010000000000000000000100000000000000000000002002",result5_3_1);
        String arg5_4_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2i";
        String result5_4_1 = new MainActivity().moveTest(arg5_4_1);
        assertEquals("21110110011221200010000000000000000000100000000000000000000002002",result5_4_1);
        String arg5_5_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7o";
        String result5_5_1 = new MainActivity().moveTest(arg5_5_1);
        assertEquals("22110110011221210010000000000000000000100000000000000000000002000",result5_5_1);
        String arg5_6_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1i";
        String result5_6_1 = new MainActivity().moveTest(arg5_6_1);
        assertEquals("21110110001221210010000000000000000000100000000000000000000002000",result5_6_1);
        String arg5_7_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7i";
        String result5_7_1 = new MainActivity().moveTest(arg5_7_1);
        assertEquals("22110110001221200010000000000000000000100000000000000000000002000", result5_7_1);
        String arg5_8_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih3i";
        String result5_8_1 = new MainActivity().moveTest(arg5_8_1);
        assertTrue(result5_8_1.startsWith("error"));
        String arg5_8_2="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1o";
        String result5_8_2 = new MainActivity().moveTest(arg5_8_2);
        assertEquals("21210110001221200010000000000000000000100000000000000000000002000",result5_8_2);
        String arg5_9_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5o";
        String result5_9_1 = new MainActivity().moveTest(arg5_9_1);
        assertEquals("22210110001222200010000000000000000000100000000000000000000002000",result5_9_1);
        String arg5_10_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5oh4o";
        String result5_10_1 = new MainActivity().moveTest(arg5_10_1);
        assertEquals("21210210001222200010000000000000000000100000000000000000000002000",result5_10_1);
        String arg5_11_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5oh4oh5i";
        String result5_11_1 = new MainActivity().moveTest(arg5_11_1);
        assertEquals("22210200001222200010000000000000000000100000000000000000000002000", result5_11_1);
        String arg5_12_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5oh4oh5ih3i";
        String result5_12_1 = new MainActivity().moveTest(arg5_12_1);
        assertTrue(result5_12_1.startsWith("error"));
        String arg5_12_2="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5oh4oh5ih3o";
        String result5_12_2 = new MainActivity().moveTest(arg5_12_2);
        assertEquals("21211200001222200010000000000000000000100000000000000000000002000", result5_12_2);
        String arg5_13_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5oh4oh5ih3oh4o";
        String result5_13_1 = new MainActivity().moveTest(arg5_13_1);
        assertTrue(result5_13_1.startsWith("error"));
        String arg5_13_2="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5oh4oh5ih3oh7o";
        String result5_13_2 = new MainActivity().moveTest(arg5_13_2);
        assertEquals("22211200101222200010000000000000000000100000000000000000000002000", result5_13_2);
        String arg5_14_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5oh4oh5ih3oh7oh6i";
        String result5_14_1 = new MainActivity().moveTest(arg5_14_1);
        assertTrue(result5_14_1.startsWith("error"));
        String arg5_14_2="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5oh4oh5ih3oh7oh5o";
        String result5_14_2 = new MainActivity().moveTest(arg5_14_2);
        assertEquals("21211210101222200010000000000000000000100000000000000000000002000", result5_14_2);
        String arg5_15_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5oh4oh5ih3oh7oh5oh1o";
        String result5_15_1 = new MainActivity().moveTest(arg5_15_1);
        assertTrue(result5_15_1.startsWith("error"));
        String arg5_15_2="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5oh4oh5ih3oh7oh5oh6o";
        String result5_15_2 = new MainActivity().moveTest(arg5_15_2);
        assertEquals("22211211101222200010000000000000000000100000000000000000000002000",result5_15_2);
        String arg5_16_1="21120110011220200010000000000000000000100000000000000000000002002v5ov2oh2iv2iv7ov1iv7ih1ov5oh4oh5ih3oh7oh5oh6ov4i";
        String result5_16_1 = new MainActivity().moveTest(arg5_16_1);
        assertEquals("22211211101212200010000000000000000000100000000000000000000000000",result5_16_1);
        String arg6_1_1="22221221102212110000011000020000000002020000000000000100000000000v1o";
        String result6_1_1 = new MainActivity().moveTest(arg6_1_1);
        assertEquals("21221221112212110000011000020000000002020000000000000100000000000",result6_1_1);
        String arg6_2_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2i";
        String result6_2_1 = new MainActivity().moveTest(arg6_2_1);
        assertEquals("22221221111212110000011000020000000002020000000000000100000000000", result6_2_1);
        String arg6_3_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih2o";
        String result6_3_1 = new MainActivity().moveTest(arg6_3_1);
        assertTrue(result6_3_1.startsWith("error"));
        String arg6_3_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6i";
        String result6_3_2 = new MainActivity().moveTest(arg6_3_2);
        assertEquals("21221220111212110000011000020000000002020000000000000100000000000",result6_3_2);
        String arg6_4_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7i";
        String result6_4_1 = new MainActivity().moveTest(arg6_4_1);
        assertEquals("22221220011212110000011000020000000002020000000000000100000000000",result6_4_1);
        String arg6_5_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7i";
        String result6_5_1 = new MainActivity().moveTest(arg6_5_1);
        assertEquals("21221220011212100000011000020000000002020000000000000100000000000",result6_5_1);
        String arg6_6_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3i";
        String result6_6_1 = new MainActivity().moveTest(arg6_6_1);
        assertEquals("22220220011212100000011000020000000002020000000000000100000000000", result6_6_1);
        String arg6_7_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3ih3o";
        String result6_7_1 = new MainActivity().moveTest(arg6_7_1);
        assertTrue(result6_7_1.startsWith("error"));
        String arg6_7_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7o";
        String result6_7_2 = new MainActivity().moveTest(arg6_7_2);
        assertEquals("21220220011212110000011000020000000002020000000000000100000000000", result6_7_2);
        String arg6_8_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7oh1o";
        String result6_8_1 = new MainActivity().moveTest(arg6_8_1);
        assertTrue(result6_8_1.startsWith("error"));
        String arg6_8_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov7o";
        String result6_8_2 = new MainActivity().moveTest(arg6_8_2);
        assertTrue(result6_8_2.startsWith("error"));
        String arg6_8_3="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2o";
        String result6_8_3 = new MainActivity().moveTest(arg6_8_3);
        assertEquals("22220220012212110000011000020000000002020000000000000100000000000",result6_8_3);
        String arg6_9_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4i";
        String result6_9_1 = new MainActivity().moveTest(arg6_9_1);
        assertEquals("21220120012212110000011000020000000000020000000000000100000000000",result6_9_1);
        String arg6_10_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6i";
        String result6_10_1 = new MainActivity().moveTest(arg6_10_1);
        assertEquals("22220120012212010000011000020000000000020000000000000100000000000",result6_10_1);
        String arg6_11_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4o";
        String result6_11_1 = new MainActivity().moveTest(arg6_11_1);
        assertEquals("21220120012222010000011000020000000000020000000000000100000000000",result6_11_1);
        String arg6_12_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6o";
        String result6_12_1 = new MainActivity().moveTest(arg6_12_1);
        assertEquals("22220120012222110000011000020000000000020000000000000100000000000", result6_12_1);
        String arg6_13_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3i";
        String result6_13_1 = new MainActivity().moveTest(arg6_13_1);
        assertTrue(result6_13_1.startsWith("error"));
        String arg6_13_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3o";
        String result6_13_2 = new MainActivity().moveTest(arg6_13_2);
        assertEquals("21221120012222110000011000020000000000020000000000000100000000000",result6_13_2);
        String arg6_14_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4o";
        String result6_14_1 = new MainActivity().moveTest(arg6_14_1);
        assertEquals("22221220012222110000011000020000000000020000000000000100000000000",result6_14_1);
        String arg6_15_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3o";
        String result6_15_1 = new MainActivity().moveTest(arg6_15_1);
        assertEquals("21222220012222110000011000020000000000020000000000000100000000000",result6_15_1);
        String arg6_16_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6o";
        String result6_16_1 = new MainActivity().moveTest(arg6_16_1);
        assertEquals("22222221012222110000011000020000000000020000000000000100000000000",result6_16_1);
        String arg6_17_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4i";
        String result6_17_1 = new MainActivity().moveTest(arg6_17_1);
        assertEquals("21222221012212110000011000020000000000020000000000000100000000000",result6_17_1);
        String arg6_18_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1i";
        String result6_18_1 = new MainActivity().moveTest(arg6_18_1);
        assertEquals("22122221012212110000010000020000000000020000000000000100000000000", result6_18_1);
        String arg6_19_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv5o";
        String result6_19_1 = new MainActivity().moveTest(arg6_19_1);
        assertTrue(result6_19_1.startsWith("error"));
        String arg6_19_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3o";
        String result6_19_2 = new MainActivity().moveTest(arg6_19_2);
        assertTrue(result6_19_2.startsWith("error"));
        String arg6_19_3="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3i";
        String result6_19_3 = new MainActivity().moveTest(arg6_19_3);
        assertEquals("21122221012112110000010000020000000000020000000000000000000000000", result6_19_3);
        String arg6_20_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3ih4o";
        String result6_20_1 = new MainActivity().moveTest(arg6_20_1);
        assertTrue(result6_20_1.startsWith("error"));
        String arg6_20_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3ih3o";
        String result6_20_2 = new MainActivity().moveTest(arg6_20_2);
        assertTrue(result6_20_2.startsWith("error"));
        String arg6_20_3="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3ih4o";
        String result6_20_3 = new MainActivity().moveTest(arg6_20_3);
        assertTrue(result6_20_3.startsWith("error"));
        String arg6_20_4="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv2o";
        String result6_20_4 = new MainActivity().moveTest(arg6_20_4);
        assertTrue(result6_20_4.startsWith("error"));
        String arg6_20_5="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3ih5o";
        String result6_20_5 = new MainActivity().moveTest(arg6_20_5);
        assertTrue(result6_20_5.startsWith("error"));
        String arg6_20_6="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6o";
        String result6_20_6 = new MainActivity().moveTest(arg6_20_6);
        assertEquals("22122221012112210000010000020000000000020000000000000000000000000", result6_20_6);
        String arg6_21_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh5o";
        String result6_21_1 = new MainActivity().moveTest(arg6_21_1);
        assertTrue(result6_21_1.startsWith("error"));
        String arg6_21_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6ov6i";
        String result6_21_2 = new MainActivity().moveTest(arg6_21_2);
        assertTrue(result6_21_2.startsWith("error"));
        String arg6_21_3="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7o";
        String result6_21_3 = new MainActivity().moveTest(arg6_21_3);
        assertEquals("21122221112112210000010000020000000000020000000000000000000000000", result6_21_3);
        String arg6_22_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7oh3o";
        String result6_22_1 = new MainActivity().moveTest(arg6_22_1);
        assertTrue(result6_22_1.startsWith("error"));
        String arg6_22_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6i";
        String result6_22_2 = new MainActivity().moveTest(arg6_22_2);
        assertEquals("22122221112112110000010000020000000000020000000000000000000000000", result6_22_2);
        String arg6_23_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih4o";
        String result6_23_1 = new MainActivity().moveTest(arg6_23_1);
        assertTrue(result6_23_1.startsWith("error"));
        String arg6_23_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5i";
        String result6_23_2 = new MainActivity().moveTest(arg6_23_2);
        assertEquals("21122211112112110000010000020000000000020000000000000000000000000",result6_23_2);
        String arg6_24_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4i";
        String result6_24_1 = new MainActivity().moveTest(arg6_24_1);
        assertEquals("22122211112102110000010000020000000000020000000000000000000000000",result6_24_1);
        String arg6_25_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3o";
        String result6_25_1 = new MainActivity().moveTest(arg6_25_1);
        assertEquals("21122211112202110000010000020000000000020000000000000000000000000",result6_25_1);
        String arg6_26_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6i";
        String result6_26_1 = new MainActivity().moveTest(arg6_26_1);
        assertEquals("22122211112202010000010000020000000000020000000000000000000000000", result6_26_1);
        String arg6_27_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv4i";
        String result6_27_1 = new MainActivity().moveTest(arg6_27_1);
        assertTrue(result6_27_1.startsWith("error"));
        String arg6_27_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv5o";
        String result6_27_2 = new MainActivity().moveTest(arg6_27_2);
        assertTrue(result6_27_2.startsWith("error"));
        String arg6_27_3="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7o";
        String result6_27_3 = new MainActivity().moveTest(arg6_27_3);
        assertEquals("21122211112202020000010000020000000000020000000000000000000000000",result6_27_3);
        String arg6_28_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5o";
        String result6_28_1 = new MainActivity().moveTest(arg6_28_1);
        assertEquals("22122221112202020000010000020000000000020000000000000000000000000",result6_28_1);
        String arg6_29_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3i";
        String result6_29_1 = new MainActivity().moveTest(arg6_29_1);
        assertEquals("21121221112202020000010000020000000000020000000000000000000000000",result6_29_1);
        String arg6_30_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2i";
        String result6_30_1 = new MainActivity().moveTest(arg6_30_1);
        assertEquals("22121221111202020000010000020000000000020000000000000000000000000",result6_30_1);
        String arg6_31_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7i";
        String result6_31_1 = new MainActivity().moveTest(arg6_31_1);
        assertEquals("21121221111202010000010000020000000000020000000000000000000000000", result6_31_1);
        String arg6_32_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv3o";
        String result6_32_1 = new MainActivity().moveTest(arg6_32_1);
        assertTrue(result6_32_1.startsWith("error"));
        String arg6_32_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7ih5o";
        String result6_32_2 = new MainActivity().moveTest(arg6_32_2);
        assertTrue(result6_32_2.startsWith("error"));
        String arg6_32_3="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7ih4o";
        String result6_32_3 = new MainActivity().moveTest(arg6_32_3);
        assertTrue(result6_32_3.startsWith("error"));
        String arg6_32_4="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1o";
        String result6_32_4 = new MainActivity().moveTest(arg6_32_4);
        assertEquals("22121221121202010000010000020000000000020000000000000000000000000",result6_32_4);
        String arg6_33_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4i";
        String result6_33_1 = new MainActivity().moveTest(arg6_33_1);
        assertEquals("21121121121202010000010000020000000000020000000000000000000000000",result6_33_1);
        String arg6_34_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3o";
        String result6_34_1 = new MainActivity().moveTest(arg6_34_1);
        assertEquals("22122121121202010000010000020000000000020000000000000000000000000",result6_34_1);
        String arg6_35_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1i";
        String result6_35_1 = new MainActivity().moveTest(arg6_35_1);
        assertEquals("21122121111202010000010000020000000000020000000000000000000000000",result6_35_1);
        String arg6_36_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6i";
        String result6_36_1 = new MainActivity().moveTest(arg6_36_1);
        assertEquals("22122120111202010000010000020000000000020000000000000000000000000", result6_36_1);
        String arg6_37_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6ih5o";
        String result6_37_1 = new MainActivity().moveTest(arg6_37_1);
        assertTrue(result6_37_1.startsWith("error"));
        String arg6_37_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6o";
        String result6_37_2 = new MainActivity().moveTest(arg6_37_2);
        assertEquals("21122120111202110000010000020000000000020000000000000000000000000", result6_37_2);
        String arg6_38_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6ov4i";
        String result6_38_1 = new MainActivity().moveTest(arg6_38_1);
        assertTrue(result6_38_1.startsWith("error"));
        String arg6_38_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh6i";
        String result6_38_2 = new MainActivity().moveTest(arg6_38_2);
        assertTrue(result6_38_2.startsWith("error"));
        String arg6_38_3="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1i";
        String result6_38_3 = new MainActivity().moveTest(arg6_38_3);
        assertEquals("22022120111202110000010000020000000000020000000000000000000000000", result6_38_3);
        String arg6_39_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih1o";
        String result6_39_1 = new MainActivity().moveTest(arg6_39_1);
        assertTrue(result6_39_1.startsWith("error"));
        String arg6_39_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2i";
        String result6_39_2 = new MainActivity().moveTest(arg6_39_2);
        assertEquals("21012120111202110000010000000000000000020000000000000000000000000",result6_39_2);
        String arg6_40_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4i";
        String result6_40_1 = new MainActivity().moveTest(arg6_40_1);
        assertEquals("22012020111202110000010000000000000000020000000000000000000000000", result6_40_1);
        String arg6_41_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4ih5o";
        String result6_41_1 = new MainActivity().moveTest(arg6_41_1);
        assertTrue(result6_41_1.startsWith("error"));
        String arg6_41_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4ih3o";
        String result6_41_2 = new MainActivity().moveTest(arg6_41_2);
        assertTrue(result6_41_2.startsWith("error"));
        String arg6_41_3="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6i";
        String result6_41_3 = new MainActivity().moveTest(arg6_41_3);
        assertEquals("21012020111202010000010000000000000000020000000000000000000000000", result6_41_3);
        String arg6_42_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3o";
        String result6_42_1 = new MainActivity().moveTest(arg6_42_1);
        assertTrue(result6_42_1.startsWith("error"));
        String arg6_42_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3i";
        String result6_42_2 = new MainActivity().moveTest(arg6_42_2);
        assertEquals("22011020111202010000010000000000000000020000000000000000000000000", result6_42_2);
        String arg6_43_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih5o";
        String result6_43_1 = new MainActivity().moveTest(arg6_43_1);
        assertTrue(result6_43_1.startsWith("error"));
        String arg6_43_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7o";
        String result6_43_2 = new MainActivity().moveTest(arg6_43_2);
        assertEquals("21011020211202010000010000000000000000020000000000000000000000000",result6_43_2);
        String arg6_44_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5i";
        String result6_44_1 = new MainActivity().moveTest(arg6_44_1);
        assertEquals("22011010211202010000010000000000000000020000000000000000000000000", result6_44_1);
        String arg6_45_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5iv4i";
        String result6_45_1 = new MainActivity().moveTest(arg6_45_1);
        assertTrue(result6_45_1.startsWith("error"));
        String arg6_45_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3o";
        String result6_45_2 = new MainActivity().moveTest(arg6_45_2);
        assertEquals("21012010211202010000010000000000000000020000000000000000000000000",result6_45_2);
        String arg6_46_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4o";
        String result6_46_1 = new MainActivity().moveTest(arg6_46_1);
        assertEquals("22012110211202010000010000000000000000020000000000000000000000000",result6_46_1);
        String arg6_47_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7o";
        String result6_47_1 = new MainActivity().moveTest(arg6_47_1);
        assertEquals("21012110211202020000010000000000000000020000000000000000000000000", result6_47_1);
        String arg6_48_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7ov5o";
        String result6_48_1 = new MainActivity().moveTest(arg6_48_1);
        assertTrue(result6_48_1.startsWith("error"));
        String arg6_48_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7oh5i";
        String result6_48_2 = new MainActivity().moveTest(arg6_48_2);
        assertEquals("22012100211202020000010000000000000000020000000000000000000000000", result6_48_2);
        String arg6_49_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7oh5iv6i";
        String result6_49_1 = new MainActivity().moveTest(arg6_49_1);
        assertTrue(result6_49_1.startsWith("error"));
        String arg6_49_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7oh5iv6o";
        String result6_49_2 = new MainActivity().moveTest(arg6_49_2);
        assertEquals("21012100211202120000010000000000000000020000000000000000000000000", result6_49_2);
        String arg6_50_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7oh5iv6ov4i";
        String result6_50_1 = new MainActivity().moveTest(arg6_50_1);
        assertTrue(result6_50_1.startsWith("error"));
        String arg6_50_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7oh5iv6oh1o";
        String result6_50_2 = new MainActivity().moveTest(arg6_50_2);
        assertEquals("22112100211202120000010000000000000000020000000000000000000000000", result6_50_2);
        String arg6_51_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7oh5iv6oh1ov3o";
        String result6_51_1 = new MainActivity().moveTest(arg6_51_1);
        assertTrue(result6_51_1.startsWith("error"));
        String arg6_51_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7oh5iv6oh1oh5o";
        String result6_51_2 = new MainActivity().moveTest(arg6_51_2);
        assertEquals("21112110211202120000010000000000000000020000000000000000000000000", result6_51_2);
        String arg6_52_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7oh5iv6oh1oh5oh6i";
        String result6_52_1 = new MainActivity().moveTest(arg6_52_1);
        assertTrue(result6_52_1.startsWith("error"));
        String arg6_52_2="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7oh5iv6oh1oh5ov2o";
        String result6_52_2 = new MainActivity().moveTest(arg6_52_2);
        assertEquals("22112110212202120000010000000000000000020000000000000000000000000",result6_52_2);
        String arg6_53_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7oh5iv6oh1oh5ov2oh5i";
        String result6_53_1 = new MainActivity().moveTest(arg6_53_1);
        assertEquals("21112100212202120000010000000000000000020000000000000000000000000",result6_53_1);
        String arg6_54_1="22221221102212110000011000020000000002020000000000000100000000000v1ov2ih6ih7iv7ih3iv7ov2oh4iv6iv4ov6oh3oh4oh3oh6ov4ih1iv3iv6oh7ov6ih5iv4iv3ov6iv7oh5oh3iv2iv7iv1oh4ih3ov1ih6iv6oh1ih2ih4iv6ih3ih7oh5ih3oh4ov7oh5iv6oh1oh5ov2oh5iv3i";
        String result6_54_1 = new MainActivity().moveTest(arg6_54_1);
        assertEquals("21112100212102120000010000000000000000000000000000000000000000000",result6_54_1);
        String arg7_1_1="32202102211100221000100000000300300000000000020200100000000001002h6i";
        String result7_1_1 = new MainActivity().moveTest(arg7_1_1);
        assertEquals("33202101211100221000100000000300300000000000020200100000000001002", result7_1_1);
        String arg7_2_1="32202102211100221000100000000300300000000000020200100000000001002h6ih6o";
        String result7_2_1 = new MainActivity().moveTest(arg7_2_1);
        assertTrue(result7_2_1.startsWith("error"));
        String arg7_2_2="32202102211100221000100000000300300000000000020200100000000001002h6iv5i";
        String result7_2_2 = new MainActivity().moveTest(arg7_2_2);
        assertTrue(result7_2_2.startsWith("error"));
        String arg7_2_3="32202102211100221000100000000300300000000000020200100000000001002h6ih7i";
        String result7_2_3 = new MainActivity().moveTest(arg7_2_3);
        assertEquals("31202101111100221000100000000300300000000000020200100000000000002", result7_2_3);
        String arg7_3_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7ih7o";
        String result7_3_1 = new MainActivity().moveTest(arg7_3_1);
        assertTrue(result7_3_1.startsWith("error"));
        String arg7_3_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7ih1o";
        String result7_3_2 = new MainActivity().moveTest(arg7_3_2);
        assertTrue(result7_3_2.startsWith("error"));
        String arg7_3_3="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv4i";
        String result7_3_3 = new MainActivity().moveTest(arg7_3_3);
        assertTrue(result7_3_3.startsWith("error"));
        String arg7_3_4="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5o";
        String result7_3_4 = new MainActivity().moveTest(arg7_3_4);
        assertEquals("32202101111101221000100000000300300000000000020200100000000000002", result7_3_4);
        String arg7_4_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov5o";
        String result7_4_1 = new MainActivity().moveTest(arg7_4_1);
        assertTrue(result7_4_1.startsWith("error"));
        String arg7_4_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1i";
        String result7_4_2 = new MainActivity().moveTest(arg7_4_2);
        assertEquals("33202101101101221000100000000300300000000000020200100000000000002", result7_4_2);
        String arg7_5_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih2i";
        String result7_5_1 = new MainActivity().moveTest(arg7_5_1);
        assertTrue(result7_5_1.startsWith("error"));
        String arg7_5_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6i";
        String result7_5_2 = new MainActivity().moveTest(arg7_5_2);
        assertEquals("31202100101101221000100000000300300000000000020200100000000000002",result7_5_2);
        String arg7_6_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4o";
        String result7_6_1 = new MainActivity().moveTest(arg7_6_1);
        assertEquals("32202200101101221000100000000300300000000000020200100000000000002",result7_6_1);
        String arg7_7_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4o";
        String result7_7_1 = new MainActivity().moveTest(arg7_7_1);
        assertEquals("33202200101111221000100000000300300000000000020000100000000000002",result7_7_1);
        String arg7_8_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3i";
        String result7_8_1 = new MainActivity().moveTest(arg7_8_1);
        assertEquals("31201200101111221000100000000300000000000000020000100000000000002", result7_8_1);
        String arg7_9_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih4o";
        String result7_9_1 = new MainActivity().moveTest(arg7_9_1);
        assertTrue(result7_9_1.startsWith("error"));
        String arg7_9_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3iv6o";
        String result7_9_2 = new MainActivity().moveTest(arg7_9_2);
        assertTrue(result7_9_2.startsWith("error"));
        String arg7_9_3="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3iv4i";
        String result7_9_3 = new MainActivity().moveTest(arg7_9_3);
        assertTrue(result7_9_3.startsWith("error"));
        String arg7_9_4="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3iv4i";
        String result7_9_4 = new MainActivity().moveTest(arg7_9_4);
        assertTrue(result7_9_4.startsWith("error"));
        String arg7_9_5="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2o";
        String result7_9_5 = new MainActivity().moveTest(arg7_9_5);
        assertEquals("32211200101111221000100000000300000000000000020000100000000000002",result7_9_5);
        String arg7_10_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7i";
        String result7_10_1 = new MainActivity().moveTest(arg7_10_1);
        assertEquals("31211200101111211000100000000000000000000000020000000000000000002",result7_10_1);
        String arg7_11_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5i";
        String result7_11_1 = new MainActivity().moveTest(arg7_11_1);
        assertEquals("32211200101110211000100000000000000000000000020000000000000000002", result7_11_1);
        String arg7_12_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5ih5i";
        String result7_12_1 = new MainActivity().moveTest(arg7_12_1);
        assertTrue(result7_12_1.startsWith("error"));
        String arg7_12_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2i";
        String result7_12_2 = new MainActivity().moveTest(arg7_12_2);
        assertEquals("31211200100110211000100000000000000000000000020000000000000000002", result7_12_2);
        String arg7_13_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv1i";
        String result7_13_1 = new MainActivity().moveTest(arg7_13_1);
        assertTrue(result7_13_1.startsWith("error"));
        String arg7_13_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7i";
        String result7_13_2 = new MainActivity().moveTest(arg7_13_2);
        assertEquals("32211200100110201000100000000000000000000000020000000000000000002",result7_13_2);
        String arg7_14_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5o";
        String result7_14_1 = new MainActivity().moveTest(arg7_14_1);
        assertEquals("31211200100111201000100000000000000000000000020000000000000000002", result7_14_1);
        String arg7_15_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6o";
        String result7_15_1 = new MainActivity().moveTest(arg7_15_1);
        assertTrue(result7_15_1.startsWith("error"));
        String arg7_15_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov1i";
        String result7_15_2 = new MainActivity().moveTest(arg7_15_2);
        assertTrue(result7_15_2.startsWith("error"));
        String arg7_15_3="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5oh1o";
        String result7_15_3 = new MainActivity().moveTest(arg7_15_3);
        assertTrue(result7_15_3.startsWith("error"));
        String arg7_15_4="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6o";
        String result7_15_4 = new MainActivity().moveTest(arg7_15_4);
        assertTrue(result7_15_4.startsWith("error"));
        String arg7_15_5="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6o";
        String result7_15_5 = new MainActivity().moveTest(arg7_15_5);
        assertTrue(result7_15_5.startsWith("error"));
        String arg7_15_6="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6i";
        String result7_15_6 = new MainActivity().moveTest(arg7_15_6);
        assertEquals("32211200100111101000100000000000000000000000020000000000000000002",result7_15_6);
        String arg7_16_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7o";
        String result7_16_1 = new MainActivity().moveTest(arg7_16_1);
        assertEquals("31211200100111111000100000000000000000000000020000000000000000002",result7_16_1);
        String arg7_17_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7i";
        String result7_17_1 = new MainActivity().moveTest(arg7_17_1);
        assertEquals("32211200000111111000100000000000000000000000020000000000000000000", result7_17_1);
        String arg7_18_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih5i";
        String result7_18_1 = new MainActivity().moveTest(arg7_18_1);
        assertTrue(result7_18_1.startsWith("error"));
        String arg7_18_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6o";
        String result7_18_2 = new MainActivity().moveTest(arg7_18_2);
        assertEquals("31211201000111111000100000000000000000000000020000000000000000000",result7_18_2);
        String arg7_19_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4i";
        String result7_19_1 = new MainActivity().moveTest(arg7_19_1);
        assertEquals("32211101000111111000100000000000000000000000020000000000000000000",result7_19_1);
        String arg7_20_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6o";
        String result7_20_1 = new MainActivity().moveTest(arg7_20_1);
        assertEquals("31211101000111211000100000000000000000000000020000000000000000000",result7_20_1);
        String arg7_21_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6i";
        String result7_21_1 = new MainActivity().moveTest(arg7_21_1);
        assertEquals("32211100000111211000100000000000000000000000020000000000000000000", result7_21_1);
        String arg7_22_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6iv1i";
        String result7_22_1 = new MainActivity().moveTest(arg7_22_1);
        assertTrue(result7_22_1.startsWith("error"));
        String arg7_22_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih7i";
        String result7_22_2 = new MainActivity().moveTest(arg7_22_2);
        assertTrue(result7_22_2.startsWith("error"));
        String arg7_22_3="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3o";
        String result7_22_3 = new MainActivity().moveTest(arg7_22_3);
        assertEquals("31212100000111211000100000000000000000000000020000000000000000000", result7_22_3);
        String arg7_23_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh6i";
        String result7_23_1 = new MainActivity().moveTest(arg7_23_1);
        assertTrue(result7_23_1.startsWith("error"));
        String arg7_23_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2i";
        String result7_23_2 = new MainActivity().moveTest(arg7_23_2);
        assertEquals("32202100000111211000100000000000000000000000020000000000000000000",result7_23_2);
        String arg7_24_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4o";
        String result7_24_1 = new MainActivity().moveTest(arg7_24_1);
        assertEquals("31202100000121211000100000000000000000000000020000000000000000000",result7_24_1);
        String arg7_25_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5i";
        String result7_25_1 = new MainActivity().moveTest(arg7_25_1);
        assertEquals("32202100000120211000100000000000000000000000020000000000000000000",result7_25_1);
        String arg7_26_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2o";
        String result7_26_1 = new MainActivity().moveTest(arg7_26_1);
        assertEquals("31202100001120211000100000000000000000000000020000000000000000000",result7_26_1);
        String arg7_27_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6o";
        String result7_27_1 = new MainActivity().moveTest(arg7_27_1);
        assertEquals("32202101001120211000100000000000000000000000020000000000000000000",result7_27_1);
        String arg7_28_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4i";
        String result7_28_1 = new MainActivity().moveTest(arg7_28_1);
        assertEquals("31202101001110211000100000000000000000000000020000000000000000000",result7_28_1);
        String arg7_29_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2i";
        String result7_29_1 = new MainActivity().moveTest(arg7_29_1);
        assertEquals("32202101000110211000100000000000000000000000020000000000000000000",result7_29_1);
        String arg7_30_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6i";
        String result7_30_1 = new MainActivity().moveTest(arg7_30_1);
        assertEquals("31202100000110211000100000000000000000000000020000000000000000000", result7_30_1);
        String arg7_31_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6ih1o";
        String result7_31_1 = new MainActivity().moveTest(arg7_31_1);
        assertTrue(result7_31_1.startsWith("error"));
        String arg7_31_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7o";
        String result7_31_2 = new MainActivity().moveTest(arg7_31_2);
        assertEquals("32202100000110221000100000000000000000000000020000000000000000000", result7_31_2);
        String arg7_32_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov7o";
        String result7_32_1 = new MainActivity().moveTest(arg7_32_1);
        assertTrue(result7_32_1.startsWith("error"));
        String arg7_32_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2o";
        String result7_32_2 = new MainActivity().moveTest(arg7_32_2);
        assertEquals("31202100001110221000100000000000000000000000020000000000000000000", result7_32_2);
        String arg7_33_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh2i";
        String result7_33_1 = new MainActivity().moveTest(arg7_33_1);
        assertTrue(result7_33_1.startsWith("error"));
        String arg7_33_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4o";
        String result7_33_2 = new MainActivity().moveTest(arg7_33_2);
        assertEquals("32202200001110221000100000000000000000000000020000000000000000000", result7_33_2);
        String arg7_34_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4ov1i";
        String result7_34_1 = new MainActivity().moveTest(arg7_34_1);
        assertTrue(result7_34_1.startsWith("error"));
        String arg7_34_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7o";
        String result7_34_2 = new MainActivity().moveTest(arg7_34_2);
        assertEquals("31202200101110221000100000000000000000000000020000000000000000000", result7_34_2);
        String arg7_35_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7oh3o";
        String result7_35_1 = new MainActivity().moveTest(arg7_35_1);
        assertTrue(result7_35_1.startsWith("error"));
        String arg7_35_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7oh5i";
        String result7_35_2 = new MainActivity().moveTest(arg7_35_2);
        assertTrue(result7_35_2.startsWith("error"));
        String arg7_35_3="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7ov1o";
        String result7_35_3 = new MainActivity().moveTest(arg7_35_3);
        assertEquals("32202200111110221000100000000000000000000000020000000000000000000", result7_35_3);
        String arg7_36_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7ov1ov7o";
        String result7_36_1 = new MainActivity().moveTest(arg7_36_1);
        assertTrue(result7_36_1.startsWith("error"));
        String arg7_36_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7ov1ov6o";
        String result7_36_2 = new MainActivity().moveTest(arg7_36_2);
        assertTrue(result7_36_2.startsWith("error"));
        String arg7_36_3="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7ov1ov3i";
        String result7_36_3 = new MainActivity().moveTest(arg7_36_3);
        assertEquals("31202200111010221000100000000000000000000000020000000000000000000",result7_36_3);
        String arg7_37_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7ov1ov3ih3i";
        String result7_37_1 = new MainActivity().moveTest(arg7_37_1);
        assertEquals("32201200111010221000100000000000000000000000020000000000000000000", result7_37_1);
        String arg7_38_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7ov1ov3ih3ih1o";
        String result7_38_1 = new MainActivity().moveTest(arg7_38_1);
        assertTrue(result7_38_1.startsWith("error"));
        String arg7_38_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7ov1ov3ih3ih5o";
        String result7_38_2 = new MainActivity().moveTest(arg7_38_2);
        assertEquals("31201210111010221000100000000000000000000000020000000000000000000", result7_38_2);
        String arg7_39_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7ov1ov3ih3ih5oh5o";
        String result7_39_1 = new MainActivity().moveTest(arg7_39_1);
        assertTrue(result7_39_1.startsWith("error"));
        String arg7_39_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7ov1ov3ih3ih5ov1i";
        String result7_39_2 = new MainActivity().moveTest(arg7_39_2);
        assertEquals("32201210101010221000100000000000000000000000020000000000000000000", result7_39_2);
        String arg7_40_1="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7ov1ov3ih3ih5ov1ih1o";
        String result7_40_1 = new MainActivity().moveTest(arg7_40_1);
        assertTrue(result7_40_1.startsWith("error"));
        String arg7_40_2="32202102211100221000100000000300300000000000020200100000000001002h6ih7iv5ov1ih6ih4ov4oh3ih2ov7iv5iv2iv7iv5ov6iv7oh7ih6oh4iv6oh6ih3oh2iv4ov5iv2oh6ov4iv2ih6iv7ov2oh4oh7ov1ov3ih3ih5ov1iv2o";
        String result7_40_2 = new MainActivity().moveTest(arg7_40_2);
        assertEquals("32201210102010221000100000000000000000000000000000000000000000000", result7_40_2);
        String arg8_1_1="22000101120112002000020002100010000001000200000000000002000000000v6i";
        String result8_1_1 = new MainActivity().moveTest(arg8_1_1);
        assertTrue(result8_1_1.startsWith("error"));
        String arg8_1_2="22000101120112002000020002100010000001000200000000000002000000000v5o";
        String result8_1_2 = new MainActivity().moveTest(arg8_1_2);
        assertTrue(result8_1_2.startsWith("error"));
        String arg8_1_3="22000101120112002000020002100010000001000200000000000002000000000v5i";
        String result8_1_3 = new MainActivity().moveTest(arg8_1_3);
        assertEquals("21000101120111002000020002100010000001000000000000000002000000000", result8_1_3);
        String arg8_2_1="22000101120112002000020002100010000001000200000000000002000000000v5iv7i";
        String result8_2_1 = new MainActivity().moveTest(arg8_2_1);
        assertTrue(result8_2_1.startsWith("error"));
        String arg8_2_2="22000101120112002000020002100010000001000200000000000002000000000v5ih2o";
        String result8_2_2 = new MainActivity().moveTest(arg8_2_2);
        assertEquals("22010101120111002000020002000010000001000000000000000002000000000",result8_2_2);
        String arg8_3_1="22000101120112002000020002100010000001000200000000000002000000000v5ih2oh3o";
        String result8_3_1 = new MainActivity().moveTest(arg8_3_1);
        assertEquals("21011101120111002000020002000000000001000000000000000002000000000",result8_3_1);
        String arg8_4_1="22000101120112002000020002100010000001000200000000000002000000000v5ih2oh3oh7o";
        String result8_4_1 = new MainActivity().moveTest(arg8_4_1);
        assertEquals("22011101220111002000020002000000000001000000000000000002000000000",result8_4_1);
        String arg8_5_1="22000101120112002000020002100010000001000200000000000002000000000v5ih2oh3oh7oh3i";
        String result8_5_1 = new MainActivity().moveTest(arg8_5_1);
        assertEquals("21010101220111002000020002000000000001000000000000000002000000000",result8_5_1);
        String arg8_6_1="22000101120112002000020002100010000001000200000000000002000000000v5ih2oh3oh7oh3ih2o";
        String result8_6_1 = new MainActivity().moveTest(arg8_6_1);
        assertEquals("22020101220111002000020002000000000001000000000000000002000000000", result8_6_1);
        String arg8_7_1="22000101120112002000020002100010000001000200000000000002000000000v5ih2oh3oh7oh3ih2oh1i";
        String result8_7_1 = new MainActivity().moveTest(arg8_7_1);
        assertTrue(result8_7_1.startsWith("error"));
        String arg8_7_2="22000101120112002000020002100010000001000200000000000002000000000v5ih2oh3oh7oh3ih2ov2i";
        String result8_7_2 = new MainActivity().moveTest(arg8_7_2);
        assertTrue(result8_7_2.startsWith("error"));
        String arg8_7_3="22000101120112002000020002100010000001000200000000000002000000000v5ih2oh3oh7oh3ih2ov6o";
        String result8_7_3 = new MainActivity().moveTest(arg8_7_3);
        assertEquals("21020101220111102000020002000000000001000000000000000002000000000",result8_7_3);
        String arg8_8_1="22000101120112002000020002100010000001000200000000000002000000000v5ih2oh3oh7oh3ih2ov6ov1i";
        String result8_8_1 = new MainActivity().moveTest(arg8_8_1);
        assertEquals("21020101210111102000020002000000000000000000000000000002000000000",result8_8_1);
        String arg9_1_1="21210022222012210000000000000000010000100000000000000200200000000h4o";
        String result9_1_1 = new MainActivity().moveTest(arg9_1_1);
        assertEquals("22210122222012210000000000000000010000100000000000000200200000000", result9_1_1);
        String arg9_2_1="21210022222012210000000000000000010000100000000000000200200000000h4oh3i";
        String result9_2_1 = new MainActivity().moveTest(arg9_2_1);
        assertTrue(result9_2_1.startsWith("error"));
        String arg9_2_2="21210022222012210000000000000000010000100000000000000200200000000h4ov5o";
        String result9_2_2 = new MainActivity().moveTest(arg9_2_2);
        assertTrue(result9_2_2.startsWith("error"));
        String arg9_2_3="21210022222012210000000000000000010000100000000000000200200000000h4oh5i";
        String result9_2_3 = new MainActivity().moveTest(arg9_2_3);
        assertEquals("21210112222012210000000000000000010000100000000000000200200000000",result9_2_3);
        String arg9_3_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7i";
        String result9_3_1 = new MainActivity().moveTest(arg9_3_1);
        assertEquals("22210112222012200000000000000000010000100000000000000200200000000", result9_3_1);
        String arg9_4_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7ih1o";
        String result9_4_1 = new MainActivity().moveTest(arg9_4_1);
        assertTrue(result9_4_1.startsWith("error"));
        String arg9_4_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4i";
        String result9_4_2 = new MainActivity().moveTest(arg9_4_2);
        assertEquals("21210112222002200000000000000000000000100000000000000200200000000",result9_4_2);
        String arg9_5_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2i";
        String result9_5_1 = new MainActivity().moveTest(arg9_5_1);
        assertEquals("22200112222002200000000000000000000000100000000000000200200000000", result9_5_1);
        String arg9_6_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2ih6o";
        String result9_6_1 = new MainActivity().moveTest(arg9_6_1);
        assertTrue(result9_6_1.startsWith("error"));
        String arg9_6_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv5o";
        String result9_6_2 = new MainActivity().moveTest(arg9_6_2);
        assertTrue(result9_6_2.startsWith("error"));
        String arg9_6_3="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2ih1o";
        String result9_6_3 = new MainActivity().moveTest(arg9_6_3);
        assertTrue(result9_6_3.startsWith("error"));
        String arg9_6_4="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv6o";
        String result9_6_4 = new MainActivity().moveTest(arg9_6_4);
        assertTrue(result9_6_4.startsWith("error"));
        String arg9_6_5="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv5o";
        String result9_6_5 = new MainActivity().moveTest(arg9_6_5);
        assertTrue(result9_6_5.startsWith("error"));
        String arg9_6_6="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7o";
        String result9_6_6 = new MainActivity().moveTest(arg9_6_6);
        assertEquals("21200112222002210000000000000000000000100000000000000200200000000",result9_6_6);
        String arg9_7_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5i";
        String result9_7_1 = new MainActivity().moveTest(arg9_7_1);
        assertEquals("22200112222001210000000000000000000000100000000000000200200000000", result9_7_1);
        String arg9_8_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5iv4i";
        String result9_8_1 = new MainActivity().moveTest(arg9_8_1);
        assertTrue(result9_8_1.startsWith("error"));
        String arg9_8_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih2i";
        String result9_8_2 = new MainActivity().moveTest(arg9_8_2);
        assertTrue(result9_8_2.startsWith("error"));
        String arg9_8_3="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih3i";
        String result9_8_3 = new MainActivity().moveTest(arg9_8_3);
        assertTrue(result9_8_3.startsWith("error"));
        String arg9_8_4="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5iv4i";
        String result9_8_4 = new MainActivity().moveTest(arg9_8_4);
        assertTrue(result9_8_4.startsWith("error"));
        String arg9_8_5="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1i";
        String result9_8_5 = new MainActivity().moveTest(arg9_8_5);
        assertEquals("21100112222001210000000000000000000000100000000000000200200000000",result9_8_5);
        String arg9_9_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4i";
        String result9_9_1 = new MainActivity().moveTest(arg9_9_1);
        assertEquals("22100012222001210000000000000000000000100000000000000200200000000", result9_9_1);
        String arg9_10_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih7o";
        String result9_10_1 = new MainActivity().moveTest(arg9_10_1);
        assertTrue(result9_10_1.startsWith("error"));
        String arg9_10_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2o";
        String result9_10_2 = new MainActivity().moveTest(arg9_10_2);
        assertEquals("21110012222001210000000000000000000000100000000000000200200000000", result9_10_2);
        String arg9_11_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2ov2o";
        String result9_11_1 = new MainActivity().moveTest(arg9_11_1);
        assertTrue(result9_11_1.startsWith("error"));
        String arg9_11_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4o";
        String result9_11_2 = new MainActivity().moveTest(arg9_11_2);
        assertEquals("22110112222001210000000000000000000000100000000000000200200000000",result9_11_2);
        String arg9_12_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2i";
        String result9_12_1 = new MainActivity().moveTest(arg9_12_1);
        assertEquals("21100112222001210000000000000000000000100000000000000200200000000",result9_12_1);
        String arg9_13_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3o";
        String result9_13_1 = new MainActivity().moveTest(arg9_13_1);
        assertEquals("22100112222101210000000000000000000000100000000000000000200000000", result9_13_1);
        String arg9_14_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3ov2o";
        String result9_14_1 = new MainActivity().moveTest(arg9_14_1);
        assertTrue(result9_14_1.startsWith("error"));
        String arg9_14_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh2i";
        String result9_14_2 = new MainActivity().moveTest(arg9_14_2);
        assertTrue(result9_14_2.startsWith("error"));
        String arg9_14_3="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3ov3i";
        String result9_14_3 = new MainActivity().moveTest(arg9_14_3);
        assertTrue(result9_14_3.startsWith("error"));
        String arg9_14_4="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3o";
        String result9_14_4 = new MainActivity().moveTest(arg9_14_4);
        assertEquals("21101112222101210000000000000000000000100000000000000000200000000",result9_14_4);
        String arg9_15_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5o";
        String result9_15_1 = new MainActivity().moveTest(arg9_15_1);
        assertEquals("22101112222102210000000000000000000000100000000000000000200000000", result9_15_1);
        String arg9_16_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh2i";
        String result9_16_1 = new MainActivity().moveTest(arg9_16_1);
        assertTrue(result9_16_1.startsWith("error"));
        String arg9_16_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4i";
        String result9_16_2 = new MainActivity().moveTest(arg9_16_2);
        assertEquals("21101012222102210000000000000000000000100000000000000000200000000", result9_16_2);
        String arg9_17_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4iv1o";
        String result9_17_1 = new MainActivity().moveTest(arg9_17_1);
        assertTrue(result9_17_1.startsWith("error"));
        String arg9_17_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4iv6o";
        String result9_17_2 = new MainActivity().moveTest(arg9_17_2);
        assertTrue(result9_17_2.startsWith("error"));
        String arg9_17_3="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih2i";
        String result9_17_3 = new MainActivity().moveTest(arg9_17_3);
        assertTrue(result9_17_3.startsWith("error"));
        String arg9_17_4="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih7o";
        String result9_17_4 = new MainActivity().moveTest(arg9_17_4);
        assertTrue(result9_17_4.startsWith("error"));
        String arg9_17_5="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3i";
        String result9_17_5 = new MainActivity().moveTest(arg9_17_5);
        assertEquals("22100012222102210000000000000000000000100000000000000000200000000", result9_17_5);
        String arg9_18_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3ih2i";
        String result9_18_1 = new MainActivity().moveTest(arg9_18_1);
        assertTrue(result9_18_1.startsWith("error"));
        String arg9_18_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv6o";
        String result9_18_2 = new MainActivity().moveTest(arg9_18_2);
        assertTrue(result9_18_2.startsWith("error"));
        String arg9_18_3="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2i";
        String result9_18_3 = new MainActivity().moveTest(arg9_18_3);
        assertEquals("21100012221102210000000000000000000000100000000000000000200000000", result9_18_3);
        String arg9_19_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih3i";
        String result9_19_1 = new MainActivity().moveTest(arg9_19_1);
        assertTrue(result9_19_1.startsWith("error"));
        String arg9_19_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5i";
        String result9_19_2 = new MainActivity().moveTest(arg9_19_2);
        assertEquals("22100002221102210000000000000000000000100000000000000000200000000", result9_19_2);
        String arg9_20_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5ih6o";
        String result9_20_1 = new MainActivity().moveTest(arg9_20_1);
        assertTrue(result9_20_1.startsWith("error"));
        String arg9_20_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7o";
        String result9_20_2 = new MainActivity().moveTest(arg9_20_2);
        assertEquals("21100002221102220000000000000000000000100000000000000000200000000",result9_20_2);
        String arg9_21_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5o";
        String result9_21_1 = new MainActivity().moveTest(arg9_21_1);
        assertEquals("22100012221102220000000000000000000000100000000000000000200000000", result9_21_1);
        String arg9_22_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5ov5o";
        String result9_22_1 = new MainActivity().moveTest(arg9_22_1);
        assertTrue(result9_22_1.startsWith("error"));
        String arg9_22_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3o";
        String result9_22_2 = new MainActivity().moveTest(arg9_22_2);
        assertEquals("21101012221102220000000000000000000000100000000000000000200000000", result9_22_2);
        String arg9_23_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov4i";
        String result9_23_1 = new MainActivity().moveTest(arg9_23_1);
        assertTrue(result9_23_1.startsWith("error"));
        String arg9_23_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov1o";
        String result9_23_2 = new MainActivity().moveTest(arg9_23_2);
        assertTrue(result9_23_2.startsWith("error"));
        String arg9_23_3="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov4i";
        String result9_23_3 = new MainActivity().moveTest(arg9_23_3);
        assertTrue(result9_23_3.startsWith("error"));
        String arg9_23_4="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6i";
        String result9_23_4 = new MainActivity().moveTest(arg9_23_4);
        assertEquals("22101012221102120000000000000000000000100000000000000000200000000",result9_23_4);
        String arg9_24_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4o";
        String result9_24_1 = new MainActivity().moveTest(arg9_24_1);
        assertEquals("21101012221112120000000000000000000000100000000000000000200000000",result9_24_1);
        String arg9_25_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7i";
        String result9_25_1 = new MainActivity().moveTest(arg9_25_1);
        assertEquals("22101012121112120000000000000000000000100000000000000000200000000",result9_25_1);
        String arg9_26_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3o";
        String result9_26_1 = new MainActivity().moveTest(arg9_26_1);
        assertEquals("21101012121212120000000000000000000000100000000000000000200000000",result9_26_1);
        String arg9_27_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4o";
        String result9_27_1 = new MainActivity().moveTest(arg9_27_1);
        assertEquals("22101012121222120000000000000000000000100000000000000000200000000", result9_27_1);
        String arg9_28_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4ov1o";
        String result9_28_1 = new MainActivity().moveTest(arg9_28_1);
        assertTrue(result9_28_1.startsWith("error"));
        String arg9_28_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4ov4o";
        String result9_28_2 = new MainActivity().moveTest(arg9_28_2);
        assertTrue(result9_28_2.startsWith("error"));
        String arg9_28_3="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4oh7o";
        String result9_28_3 = new MainActivity().moveTest(arg9_28_3);
        assertEquals("21101012221222120000000000000000000000100000000000000000200000000", result9_28_3);
        String arg9_29_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4oh7ov4o";
        String result9_29_1 = new MainActivity().moveTest(arg9_29_1);
        assertTrue(result9_29_1.startsWith("error"));
        String arg9_29_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4oh7oh6o";
        String result9_29_2 = new MainActivity().moveTest(arg9_29_2);
        assertTrue(result9_29_2.startsWith("error"));
        String arg9_29_3="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4oh7ov4o";
        String result9_29_3 = new MainActivity().moveTest(arg9_29_3);
        assertTrue(result9_29_3.startsWith("error"));
        String arg9_29_4="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4oh7oh5i";
        String result9_29_4 = new MainActivity().moveTest(arg9_29_4);
        assertEquals("22101002221222120000000000000000000000100000000000000000200000000",result9_29_4);
        String arg9_30_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4oh7oh5iv6i";
        String result9_30_1 = new MainActivity().moveTest(arg9_30_1);
        assertEquals("21101002221222020000000000000000000000100000000000000000200000000", result9_30_1);
        String arg9_31_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4oh7oh5iv6ih4i";
        String result9_31_1 = new MainActivity().moveTest(arg9_31_1);
        assertTrue(result9_31_1.startsWith("error"));
        String arg9_31_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4oh7oh5iv6iv4o";
        String result9_31_2 = new MainActivity().moveTest(arg9_31_2);
        assertTrue(result9_31_2.startsWith("error"));
        String arg9_31_3="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4oh7oh5iv6ih3i";
        String result9_31_3 = new MainActivity().moveTest(arg9_31_3);
        assertEquals("22100002221222020000000000000000000000100000000000000000200000000", result9_31_3);
        String arg9_32_1="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4oh7oh5iv6ih3iv4o";
        String result9_32_1 = new MainActivity().moveTest(arg9_32_1);
        assertTrue(result9_32_1.startsWith("error"));
        String arg9_32_2="21210022222012210000000000000000010000100000000000000200200000000h4oh5iv7iv4ih2iv7ov5ih1ih4ih2oh4oh2iv3oh3ov5oh4ih3iv2ih5iv7oh5oh3ov6iv4oh7iv3ov4oh7oh5iv6ih3ih6i";
        String result9_32_2 = new MainActivity().moveTest(arg9_32_2);
        assertEquals("22100001221222020000000000000000000000100000000000000000000000000", result9_32_2);
        String arg10_1_1="33220122020110202010000002030000000001301000000000000030000030000h2o";
        String result10_1_1 = new MainActivity().moveTest(arg10_1_1);
        assertTrue(result10_1_1.startsWith("error"));
        String arg10_1_2="33220122020110202010000002030000000001301000000000000030000030000v3o";
        String result10_1_2 = new MainActivity().moveTest(arg10_1_2);
        assertEquals("31220122020210202010000000030000000001301000000000000030000030000",result10_1_2);
        String arg10_2_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6i";
        String result10_2_1 = new MainActivity().moveTest(arg10_2_1);
        assertEquals("32220122020210102010000000030000000001301000000000000030000030000", result10_2_1);
        String arg10_3_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv5i";
        String result10_3_1 = new MainActivity().moveTest(arg10_3_1);
        assertTrue(result10_3_1.startsWith("error"));
        String arg10_3_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4o";
        String result10_3_2 = new MainActivity().moveTest(arg10_3_2);
        assertEquals("33220122020220102010000000030000000001301000000000000030000030000",result10_3_2);
        String arg10_4_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3o";
        String result10_4_1 = new MainActivity().moveTest(arg10_4_1);
        assertEquals("31221122020220102010000000030000000001301000000000000030000030000",result10_4_1);
        String arg10_5_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7o";
        String result10_5_1 = new MainActivity().moveTest(arg10_5_1);
        assertEquals("32221122120220102010000000030000000001301000000000000030000030000", result10_5_1);
        String arg10_6_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh3i";
        String result10_6_1 = new MainActivity().moveTest(arg10_6_1);
        assertTrue(result10_6_1.startsWith("error"));
        String arg10_6_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6i";
        String result10_6_2 = new MainActivity().moveTest(arg10_6_2);
        assertEquals("33221121120220102010000000030000000001301000000000000000000030000",result10_6_2);
        String arg10_7_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5o";
        String result10_7_1 = new MainActivity().moveTest(arg10_7_1);
        assertEquals("31221121120221102010000000030000000001301000000000000000000030000", result10_7_1);
        String arg10_8_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov2i";
        String result10_8_1 = new MainActivity().moveTest(arg10_8_1);
        assertTrue(result10_8_1.startsWith("error"));
        String arg10_8_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6o";
        String result10_8_2 = new MainActivity().moveTest(arg10_8_2);
        assertEquals("32221121120221202010000000030000000001301000000000000000000030000", result10_8_2);
        String arg10_9_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6ov5o";
        String result10_9_1 = new MainActivity().moveTest(arg10_9_1);
        assertTrue(result10_9_1.startsWith("error"));
        String arg10_9_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6ov5i";
        String result10_9_2 = new MainActivity().moveTest(arg10_9_2);
        assertTrue(result10_9_2.startsWith("error"));
        String arg10_9_3="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7o";
        String result10_9_3 = new MainActivity().moveTest(arg10_9_3);
        assertEquals("33221121220221202010000000030000000001301000000000000000000030000",result10_9_3);
        String arg10_10_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6i";
        String result10_10_1 = new MainActivity().moveTest(arg10_10_1);
        assertEquals("31221120220221202010000000030000000001301000000000000000000030000",result10_10_1);
        String arg10_11_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6i";
        String result10_11_1 = new MainActivity().moveTest(arg10_11_1);
        assertEquals("32221120220221102010000000030000000001301000000000000000000030000",result10_11_1);
        String arg10_12_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4i";
        String result10_12_1 = new MainActivity().moveTest(arg10_12_1);
        assertEquals("33221120220211102010000000030000000001301000000000000000000030000", result10_12_1);
        String arg10_13_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4iv4i";
        String result10_13_1 = new MainActivity().moveTest(arg10_13_1);
        assertTrue(result10_13_1.startsWith("error"));
        String arg10_13_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4iv2i";
        String result10_13_2 = new MainActivity().moveTest(arg10_13_2);
        assertTrue(result10_13_2.startsWith("error"));
        String arg10_13_3="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4iv7i";
        String result10_13_3 = new MainActivity().moveTest(arg10_13_3);
        assertTrue(result10_13_3.startsWith("error"));
        String arg10_13_4="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2i";
        String result10_13_4 = new MainActivity().moveTest(arg10_13_4);
        assertEquals("31211120220211102010000000000000000001301000000000000000000030000",result10_13_4);
        String arg10_14_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5i";
        String result10_14_1 = new MainActivity().moveTest(arg10_14_1);
        assertEquals("32211120220210102010000000000000000001301000000000000000000030000", result10_14_1);
        String arg10_15_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv1o";
        String result10_15_1 = new MainActivity().moveTest(arg10_15_1);
        assertTrue(result10_15_1.startsWith("error"));
        String arg10_15_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4o";
        String result10_15_2 = new MainActivity().moveTest(arg10_15_2);
        assertEquals("33211120220220102010000000000000000001301000000000000000000030000",result10_15_2);
        String arg10_16_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2o";
        String result10_16_1 = new MainActivity().moveTest(arg10_16_1);
        assertEquals("31221120220220102010000000000000000001301000000000000000000030000",result10_16_1);
        String arg10_17_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3o";
        String result10_17_1 = new MainActivity().moveTest(arg10_17_1);
        assertEquals("32222120220220102010000000000000000001301000000000000000000030000", result10_17_1);
        String arg10_18_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3ov3o";
        String result10_18_1 = new MainActivity().moveTest(arg10_18_1);
        assertTrue(result10_18_1.startsWith("error"));
        String arg10_18_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1i";
        String result10_18_2 = new MainActivity().moveTest(arg10_18_2);
        assertEquals("33122120220220100010000000000000000001301000000000000000000030000", result10_18_2);
        String arg10_19_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1ih6i";
        String result10_19_1 = new MainActivity().moveTest(arg10_19_1);
        assertTrue(result10_19_1.startsWith("error"));
        String arg10_19_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv5i";
        String result10_19_2 = new MainActivity().moveTest(arg10_19_2);
        assertTrue(result10_19_2.startsWith("error"));



        //TEST 10.19.3
        String arg10_19_3="3" +  //number of initial players
                "3" +            //player that has to move
                "2201220" +      //horizontal bars positions
                "2011020" +      //vertical bars positions
                "2010000" +  //beadsGrid
                "0020300" +
                "0000000" +
                "1301000" +
                "0000000" +
                "0003000" +
                "0030000" +
                "v3o" +
                "v6i" +
                "v4o" +
                "h3o" +
                "h7o" +
                "h6i" +
                "v5o" +
                "v6o" +
                "h7o" +
                "h6i" +
                "v6i" +
                "v4i" +
                "h2i" +
                "v5i" +
                "v4o" +
                "h2o" +
                "h3o" +
                "h1i" +
                "v2o";

        String result10_19_3 = new MainActivity().moveTest(arg10_19_3);

        expectedOutput = "3" + //number of initial players
                "1" +  //player that has to move (final)
                "1221202" +
                "2122010" +
                "0010000" +
                "0000000" +
                "0000000" +
                "1301000" +
                "0000000" +
                "0000000" +
                "0030000";

        assertEquals(expectedOutput, result10_19_3);



        String arg10_20_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1i";
        String result10_20_1 = new MainActivity().moveTest(arg10_20_1);
        assertEquals("33122120211220100010000000000000000000301000000000000000000030000",result10_20_1);
        String arg10_21_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1o";
        String result10_21_1 = new MainActivity().moveTest(arg10_21_1);
        assertEquals("31222120211220100010000000000000000000301000000000000000000030000",result10_21_1);
        String arg10_22_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2i";
        String result10_22_1 = new MainActivity().moveTest(arg10_22_1);
        assertEquals("33222120210220100010000000000000000000301000000000000000000030000",result10_22_1);
        String arg10_23_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7i";
        String result10_23_1 = new MainActivity().moveTest(arg10_23_1);
        assertEquals("31222120110220100010000000000000000000301000000000000000000030000",result10_23_1);
        String arg10_24_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4o";
        String result10_24_1 = new MainActivity().moveTest(arg10_24_1);
        assertEquals("33222220110220100010000000000000000000000000000000000000000030000",result10_24_1);
        String arg10_25_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh5o";
        String result10_25_1 = new MainActivity().moveTest(arg10_25_1);
        assertTrue(result10_25_1.startsWith("error"));
        String arg10_25_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7i";
        String result10_25_2 = new MainActivity().moveTest(arg10_25_2);
        assertEquals("31222220010220100010000000000000000000000000000000000000000030000",result10_25_2);
        String arg10_26_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6o";
        String result10_26_1 = new MainActivity().moveTest(arg10_26_1);
        assertEquals("33222220010220200010000000000000000000000000000000000000000030000",result10_26_1);
        String arg10_27_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5i";
        String result10_27_1 = new MainActivity().moveTest(arg10_27_1);
        assertEquals("31222210010220200010000000000000000000000000000000000000000030000",result10_27_1);
        String arg10_28_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5iv7i";
        String result10_28_1 = new MainActivity().moveTest(arg10_28_1);
        assertTrue(result10_28_1.startsWith("error"));
        String arg10_28_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7o";
        String result10_28_2 = new MainActivity().moveTest(arg10_28_2);
        assertEquals("33222210110220200010000000000000000000000000000000000000000030000",result10_28_2);
        String arg10_29_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3i";
        String result10_29_1 = new MainActivity().moveTest(arg10_29_1);
        assertEquals("31222210110120200010000000000000000000000000000000000000000030000",result10_29_1);
        String arg10_30_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3ih7o";
        String result10_30_1 = new MainActivity().moveTest(arg10_30_1);
        assertEquals("33222210210120200010000000000000000000000000000000000000000030000",result10_30_1);
        String arg10_31_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3ih7oh6o";
        String result10_31_1 = new MainActivity().moveTest(arg10_31_1);
        assertEquals("31222211210120200010000000000000000000000000000000000000000030000",result10_31_1);
        String arg10_32_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3ih7oh6ov6o";
        String result10_32_1 = new MainActivity().moveTest(arg10_32_1);
        assertTrue(result10_32_1.startsWith("error"));
        String arg10_32_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3ih7oh6oh6i";
        String result10_32_2 = new MainActivity().moveTest(arg10_32_2);
        assertTrue(result10_32_2.startsWith("error"));
        String arg10_32_3="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3ih7oh6oh6o";
        String result10_32_3 = new MainActivity().moveTest(arg10_32_3);
        assertTrue(result10_32_3.startsWith("error"));
        String arg10_32_4="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3ih7oh6oh6o";
        String result10_32_4 = new MainActivity().moveTest(arg10_32_4);
        assertTrue(result10_32_4.startsWith("error"));
        String arg10_32_5="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3ih7oh6oh2i";
        String result10_32_5 = new MainActivity().moveTest(arg10_32_5);
        assertEquals("33212211210120200010000000000000000000000000000000000000000030000",result10_32_5);
        String arg10_33_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3ih7oh6oh2ih2o";
        String result10_33_1 = new MainActivity().moveTest(arg10_33_1);
        assertTrue(result10_33_1.startsWith("error"));
        String arg10_33_2="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3ih7oh6oh2iv6o";
        String result10_33_2 = new MainActivity().moveTest(arg10_33_2);
        assertTrue(result10_33_2.startsWith("error"));
        String arg10_33_3="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3ih7oh6oh2ih6o";
        String result10_33_3 = new MainActivity().moveTest(arg10_33_3);
        assertEquals("31212212210120200010000000000000000000000000000000000000000030000",result10_33_3);
        String arg10_34_1="33220122020110202010000002030000000001301000000000000030000030000v3ov6iv4oh3oh7oh6iv5ov6oh7oh6iv6iv4ih2iv5iv4oh2oh3oh1iv2ov1ih1ov2ih7ih4oh7iv6oh5ih7ov3ih7oh6oh2ih6ov3i";
        String result10_34_1 = new MainActivity().moveTest(arg10_34_1);
        assertEquals("31212212210020200010000000000000000000000000000000000000000000000",result10_34_1);

    }


}