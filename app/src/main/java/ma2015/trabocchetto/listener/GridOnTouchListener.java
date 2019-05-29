package ma2015.trabocchetto.listener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ma2015.trabocchetto.model.Board;
import ma2015.trabocchetto.R;
import ma2015.trabocchetto.activity.GameActivity;
import ma2015.trabocchetto.application.StayAliveApplication;
import ma2015.trabocchetto.util.Utils;

/**
 * This allow players to put their beads on the beadsGrid. It's a listener attached to the barsGridGui cells, and listens
 * for touches performed by the players when they have to put their beads. Depending on the position of the touch,
 * the related beadsGridGui cell element is retrieved and the correct image of the bead is placed in that position.
 * Then the icon and the notifications box is updated to tell which is the next player that has to put a bead, and this
 * mechanism continue until all the players have put all their beads (this is detected with arrays that count how many
 * beads the players have already put.
 */
public class GridOnTouchListener implements View.OnTouchListener {


    private GameActivity gameActivity;
    private Board board;
    private static int placedBeads[];
    private static final int FIRST_VALUE = 0, SECOND_VALUE = 1, PLAYER_ONE = 0, PLAYER_TWO = 1, PLAYER_THREE = 2, PLAYER_FOUR= 3, MAX_BEADS_PER_PLAYER = 5, PLACING_BEAD_FX = 4;

    private int initialX, initialY; //Could be useful to use


    public GridOnTouchListener(GameActivity gameActivity, Board board){
        this.gameActivity = gameActivity;
        this.board = board;
        GridOnTouchListener.placedBeads = new int[board.getPLAYERS()];
        for (int i = 0; i<board.getPLAYERS(); i++){
            placedBeads[i]=0;
        }
    }




    @Override
    public boolean onTouch(final View view, final MotionEvent event) {
        int drawableResIdFromAttr;

        //the tag of the touched view (barsGridGui cell), will be useful to identify the related "beadsGridGui cell" imageView, where I want to put the image of the bead.
        Object viewTagObject = view.getTag();
        String viewTagString = (String)viewTagObject;
        char viewTagChars[] = viewTagString.toCharArray();
        //From the tag I extract the row and the col.
        int row = Character.getNumericValue(viewTagChars[FIRST_VALUE]);
        int col = Character.getNumericValue(viewTagChars[SECOND_VALUE]);


        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE: {

                return true;
            }


            case MotionEvent.ACTION_UP: {

                Log.d("FLOW_CONTROL", "ACTION_UP: Touched-cell: extracted values: row=" + viewTagChars[FIRST_VALUE] + "col=" + viewTagChars[SECOND_VALUE]);

                //If the position, in the beadsGrid, is free (value=0, so no other players put a bead) AND the position is filled at least by one of the bars....:
                if ( board.getBeadsGrid().getBeadsGrid()[row-1][col-1] == 0  &&  ( board.getvBoard()[row-1][col-1]==1 || board.gethBoard()[row-1][col-1]==1)){

                    //...then place the bead in the beads grid
                    try {
                        int playerPuttingBead = board.getPlayerPuttingBead();
                        board.getBeadsGrid().setBead(playerPuttingBead, row-1, col-1);

                        //searching the related imageView (where I will put the bead) that is contained in the same frame layout of the touched view.
                        int resId = gameActivity.getResources().getIdentifier("b_grid_" + (row) + (col), "id", gameActivity.getPackageName());
                        ImageView imageViewBeadsGridGui = (ImageView) gameActivity.findViewById(resId);

                        if (StayAliveApplication.soundEffectsAreOn) {
                            gameActivity.getSp().play(gameActivity.getSoundIds()[PLACING_BEAD_FX], (float)0.6, (float)0.6, 1, 0, (float) 1);
                        }

                        //depending on the player who is putting the bead, I set the correct image:
                        switch (board.getPlayerPuttingBead()){
                            case (1): {
                                drawableResIdFromAttr= Utils.getResIdFromAttribute(gameActivity, R.attr.beadP1);
                                imageViewBeadsGridGui.setImageResource(drawableResIdFromAttr);
                                placedBeads[PLAYER_ONE]++;


                                break;
                            }
                            case (2): {
                                drawableResIdFromAttr= Utils.getResIdFromAttribute(gameActivity, R.attr.beadP2);
                                imageViewBeadsGridGui.setImageResource(drawableResIdFromAttr);
                                placedBeads[PLAYER_TWO]++;


                                break;
                            }
                            case (3): {
                                drawableResIdFromAttr= Utils.getResIdFromAttribute(gameActivity, R.attr.beadP3);
                                imageViewBeadsGridGui.setImageResource(drawableResIdFromAttr);
                                placedBeads[PLAYER_THREE]++;


                                break;
                            }
                            case (4): {
                                drawableResIdFromAttr= Utils.getResIdFromAttribute(gameActivity, R.attr.beadP4);
                                imageViewBeadsGridGui.setImageResource(drawableResIdFromAttr);
                                placedBeads[PLAYER_FOUR]++;
                                break;
                            }

                        }

                        //Updates the next player that has to put a bead
                        board.setNextPlayerPuttingBead();
                        updateNotificationBoxAndIcon();

                        if ( placedBeads[board.getPLAYERS()-1] == MAX_BEADS_PER_PLAYER){ //When the last player put all his beads, we're ready.
                            gameActivity.startTheMatch();
                        }


                    } catch (Exception e) {
                        System.out.println("error:" + e.getMessage());
                    }

                }else{
                    //If a player put a bead in a black hole or a place where other players put a bead...do nothing.
                }


                return true;
            }

            case MotionEvent.ACTION_DOWN: {

                initialX = (int) event.getRawX();
                initialY = (int) event.getRawY();

                return true;
            }
        }
        return false;
    }


    /**
     * Updates the TextView and ImageView with the next player (and respective icon) that has to put a bead.
     */
    public void updateNotificationBoxAndIcon(){
        ImageView imageViewPlayerIcon = (ImageView) gameActivity.findViewById(R.id.icon_moving_player);
        TextView notificationBox = (TextView) gameActivity.findViewById(R.id.notifications_box);

        notificationBox.setText(String.format(gameActivity.getResources().getString(R.string.game_hey_place_a_bead), board.getPlayerPuttingBead(), gameActivity.getPlayersDb()[board.getPlayerPuttingBead() - 1].getPlayerName()));
        switch(board.getPlayerPuttingBead()){
            case (1):
                imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(gameActivity, R.attr.beadP1));
                break;
            case (2):
                imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(gameActivity, R.attr.beadP2));
                break;
            case (3):
                imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(gameActivity, R.attr.beadP3));
                break;
            case (4):
                imageViewPlayerIcon.setImageResource(Utils.getResIdFromAttribute(gameActivity, R.attr.beadP4));
                break;
        }

    }





}
