package ma2015.trabocchetto.listener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ma2015.trabocchetto.model.Board;
import ma2015.trabocchetto.R;
import ma2015.trabocchetto.activity.GameActivity;
import ma2015.trabocchetto.util.Utils;


/**
 * This is used to listen for bar movements: the player touches a bar and drag it to the desired direction:
 * if he drags the bar as much as necessary to exceed the movTolerance, and then an arrow is displayed and indicates
 * that the movement will be performed, in the indicated direction. The image of the bar will be updated and
 * a gameactivity method is called (prepareForNextMove) in order to update the images of the involved line and change
 * the moving player, for the next move.
 * If the user touches the bar, drags it, but is undecided, he can "drag back" the bar to its original position
 * and the arrow disappears, indicating that no movement will be done.
 */
public class VerticalOnTouchListener implements View.OnTouchListener{


    private GameActivity gameActivity;
    private Board board;

    private static final int INNER=0;
    private static final int CENTRAL=1;
    private static final int OUTER=2;

    private int initialY;
    private static final int movTolerance = 15; //below this tolerance, the movement is not performed (maybe the player touches the bar involuntary)

    public VerticalOnTouchListener(GameActivity gameActivity, Board board) {
        this.board = board;
        this.gameActivity = gameActivity;
    }


    @Override
    public boolean onTouch(final View view, final MotionEvent event) {

        int drawableResIdFromAttr;
        ImageView imageViewBar = (ImageView) view;
        ImageView imageViewDirection = (ImageView) gameActivity.findViewById(R.id.info_direction);

        Object viewTagObject = view.getTag();
        int numBar = Integer.parseInt((String) viewTagObject);


        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE: {
                //Whilst the player is moving, an "arrow image" keeps indicating, dinamically, the movement direction of the bar that the player is choosing.
                //If the player is undecided he can bring the bar to its original position and the arrow disappears, indicating that no movement will be done.
                if (((int) event.getRawY() - initialY) >= movTolerance && (int) event.getRawY() > initialY) {
                    drawableResIdFromAttr= Utils.getResIdFromAttribute(gameActivity, R.attr.arrowDown);
                    imageViewDirection.setImageResource(drawableResIdFromAttr);
                }
                else if((initialY - (int) event.getRawY()) >= movTolerance && (int) event.getRawY() < initialY){
                    drawableResIdFromAttr= Utils.getResIdFromAttribute(gameActivity, R.attr.arrowUp);
                    imageViewDirection.setImageResource(drawableResIdFromAttr);
                }
                else{
                    imageViewDirection.setImageDrawable(null);
                }

                return true;
            }


            case MotionEvent.ACTION_UP: {

                imageViewDirection.setImageDrawable(null);


                if (board.getvBarPosition()[numBar - 1] == OUTER) { //based on the position of the touched bar, I set the correct image if the movement is successful
                    Log.i("FLOW_CONTROL", "Starting in OUTER position...");

                    if (((int) event.getRawY() - initialY) >= movTolerance && (int) event.getRawY() > initialY) { //REMEMBER THAT THE Y AXIS IS ORIENTED DOWN IN ANDROID
                        //I'm trying to move the bar down (central position)
                        try {
                            board.move('v', numBar, 'i');
                            Log.i("FLOW_CONTROL", "Moving from OUTER to CENTRAL");
                            drawableResIdFromAttr= Utils.getResIdFromAttribute(gameActivity, R.attr.vBarCentral);
                            imageViewBar.setImageResource(drawableResIdFromAttr);
                            gameActivity.prepareForNextMove('v', numBar);
                        } catch (Exception e) {
                            GameActivity.toastForbiddenBar.cancel(); //This is done in order to avoid "chain of toasts" accumulating, when a player repeatedly tries to move a forbidden bar
                            Toast toast = Toast.makeText(gameActivity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG); //Forbidden Bar Rule #1 or #2
                            GameActivity.toastForbiddenBar = toast;
                            GameActivity.toastForbiddenBar.show();
                        }


                    } else {
                        //Does NOTHING. Only tells the user (if he's trying to move outwards, being in OUTER position) that's not possible.
                        if ((initialY - (int) event.getRawY()) >= movTolerance && (int) event.getRawY() < initialY) {
                            Toast toast = Toast.makeText(gameActivity.getApplicationContext(), R.string.game_bar_already_outer_position, Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }


                } else if (board.getvBarPosition()[numBar - 1] == CENTRAL) {
                    // CENTRAL position, means I can move the bar to the right or to the left

                    Log.i("FLOW_CONTROL", "Starting in CENTRAL position...");

                    if (((int) event.getRawY() - initialY) >= movTolerance && (int) event.getRawY() > initialY) {
                        //I'm trying to move the bar down (to inner position)
                        try {
                            board.move('v', numBar, 'i');
                            Log.i("FLOW_CONTROL", "Moving from CENTRAL to INNER");
                            drawableResIdFromAttr= Utils.getResIdFromAttribute(gameActivity, R.attr.vBarInner);
                            imageViewBar.setImageResource(drawableResIdFromAttr);
                            gameActivity.prepareForNextMove('v', numBar);
                        } catch (Exception e) {
                            GameActivity.toastForbiddenBar.cancel(); //This is done in order to avoid "chain of toasts" accumulating, when a player repeatedly tries to move a forbidden bar
                            Toast toast = Toast.makeText(gameActivity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG); //Forbidden Bar Rule #1 or #2
                            GameActivity.toastForbiddenBar = toast;
                            GameActivity.toastForbiddenBar.show();
                        }

                    } else if ((initialY - (int) event.getRawY()) >= movTolerance && (int) event.getRawY() < initialY) {
                        // I'm trying to move the bar up (outer position)
                        try {
                            board.move('v', numBar, 'o');
                            Log.i("FLOW_CONTROL", "Moving from CENTRAL to OUTER");
                            drawableResIdFromAttr= Utils.getResIdFromAttribute(gameActivity, R.attr.vBarOuter);
                            imageViewBar.setImageResource(drawableResIdFromAttr);
                            gameActivity.prepareForNextMove('v', numBar);
                        } catch (Exception e) {
                            GameActivity.toastForbiddenBar.cancel(); //This is done in order to avoid "chain of toasts" accumulating, when a player repeatedly tries to move a forbidden bar
                            Toast toast = Toast.makeText(gameActivity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG); //Forbidden Bar Rule #1 or #2
                            GameActivity.toastForbiddenBar = toast;
                            GameActivity.toastForbiddenBar.show();
                        }

                    } else {
                        Log.d("FLOW_CONTROL", "NOTHING DONE (user is undecided what movement he wants to do, or he only pushed the bar unintentionally.");
                    }

                } else if (board.getvBarPosition()[numBar - 1] == INNER) {

                    Log.i("FLOW_CONTROL", "Starting in INNER position...");

                    if ((initialY - (int) event.getRawY()) >= movTolerance && (int) event.getRawY() < initialY) {
                        // I'm trying to move the bar up (to central position)
                        try {
                            board.move('v', numBar, 'o');
                            Log.i("FLOW_CONTROL", "Moving from INNER to CENTRAL");
                            drawableResIdFromAttr= Utils.getResIdFromAttribute(gameActivity, R.attr.vBarCentral);
                            imageViewBar.setImageResource(drawableResIdFromAttr);
                            gameActivity.prepareForNextMove('v', numBar);

                        } catch (Exception e) {
                            GameActivity.toastForbiddenBar.cancel(); //This is done in order to avoid "chain of toasts" accumulating, when a player repeatedly tries to move a forbidden bar
                            Toast toast = Toast.makeText(gameActivity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG); //Forbidden Bar Rule #1 or #2
                            GameActivity.toastForbiddenBar = toast;
                            GameActivity.toastForbiddenBar.show();
                        }

                    } else {
                        //Does NOTHING. Only tells the user (if he's trying to move inwards, being in INNER position) that's not possible.
                        if (((int) event.getRawY() - initialY) >= movTolerance && (int) event.getRawY() > initialY) {
                            Toast toast = Toast.makeText(gameActivity.getApplicationContext(), R.string.game_bar_already_inner_position, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                }


                return true;
            }

            case MotionEvent.ACTION_DOWN: {

                initialY = (int) event.getRawY();
                return true;
            }
        }


        return false;


    }




}
