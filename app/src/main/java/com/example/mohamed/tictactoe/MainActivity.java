package com.example.mohamed.tictactoe;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.tictactoe.Game.PLAYMODE;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mygameplaybuttons;
    private LinearLayout mturntap;
    private RadioGroup mdifficultytap;
    private Game mGame;
    private ImageView ivTurn;
    private Button buttonchangeMode;
    private ImageView ivgameresultdiag2;
    private ImageView ivgameresultdiag1;
    private ImageView ivgameresultcol;
    private ImageView ivgameresultrow;
    ImageView toastimage;
    TextView toasttext;
    View toastlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new Game(this);
        mdifficultytap = (RadioGroup) findViewById(R.id.difficutytap);
        mturntap = (LinearLayout) findViewById(R.id.turntap);
        mygameplaybuttons = (LinearLayout) findViewById(R.id.gameplaybuttons);
        ivTurn = (ImageView) findViewById(R.id.ivtype);
        ivgameresultrow = (ImageView) findViewById(R.id.ivgameresultrow);
        ivgameresultcol = (ImageView) findViewById(R.id.ivgameresultcol);
        ivgameresultdiag1 = (ImageView) findViewById(R.id.ivgameresultdiag1);
        ivgameresultdiag2 = (ImageView) findViewById(R.id.ivgameresultdiag2);
        buttonchangeMode = (Button) findViewById(R.id.buttonchangeplaymode);
        LayoutInflater inflater = getLayoutInflater();
        toastlayout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        toastimage = (ImageView) toastlayout.findViewById(R.id.toastimage);
        toasttext = (TextView) toastlayout.findViewById(R.id.toasttext);
        startGame();
        showGameState();
    }

    public void changeplaymode(View view) {
        if (mGame.mplaymode == PLAYMODE.ONEPLAYER) { //change the layout for the new mode
            mGame.mplaymode = PLAYMODE.TWOPLAYER;
            mdifficultytap.setVisibility(View.INVISIBLE);
            mturntap.setVisibility(View.VISIBLE);
        } else {
            mGame.mplaymode = PLAYMODE.ONEPLAYER;
            mdifficultytap.setVisibility(View.VISIBLE);
            mturntap.setVisibility(View.INVISIBLE);
        }
        Drawable modeImage = mGame.getModeImage();
        buttonchangeMode.setBackground(modeImage);
        startGame();                        //start new game
        showGameState();                   //show the result of the new game
    }

    private void showGameState() {
    }

    private void startGame() {
        //set line visibility to invisible
        ivgameresultrow.setVisibility(View.INVISIBLE);
        ivgameresultcol.setVisibility(View.INVISIBLE);
        ivgameresultdiag1.setVisibility(View.INVISIBLE);
        ivgameresultdiag2.setVisibility(View.INVISIBLE);
        //make the gameplaybuttons backgound null
        int n = mygameplaybuttons.getChildCount();
        for (int i = 0; i < n; i++) {
            if (mygameplaybuttons.getChildAt(i) instanceof LinearLayout) {
                LinearLayout llchild = (LinearLayout) mygameplaybuttons.getChildAt(i);
                int m = llchild.getChildCount();
                for (int j = 0; j < n; j++) {
                    View viewchild = llchild.getChildAt(j);
                    if (viewchild instanceof Button) {
                        ((Button) viewchild).setBackground(null);
                    }
                }
            }
        }
        //change playerturn image
        mGame.Initialize();
    }

    public void newPlay(View view) {
        Button btn = (Button) view;
        int id = Integer.parseInt(btn.getTag().toString());
        boolean canPlay = mGame.play(id);
        if (canPlay) {
            Drawable playImage = mGame.getPlayImage();
            btn.setBackground(playImage);
            changeGameTurn();
            checkFinished();
        }
    }

    void changeGameTurn() {
        if (mGame.mplayerturn == Game.PLAYTURN.FIRST) {
            mGame.mplayerturn = Game.PLAYTURN.SECOND;
        } else {
            mGame.mplayerturn = Game.PLAYTURN.FIRST;
        }
        if (mGame.mplaymode == PLAYMODE.TWOPLAYER) {
            int d = mGame.getTurnImage();
            ivTurn.setImageResource(d);
        }
    }

    void checkFinished() {
        int[] result = mGame.checkEnd();
        if (mGame.gameendstate != Game.GAMEENDSTATE.RUNNING) {        //Game is finished

            if (mGame.gameendstate != Game.GAMEENDSTATE.DRAW) {          //draw line if any one WON the game
                for (int i = 0; i < 4; i++) {
                    if (result[i] != -1) {
                        drawwinline(i, result[i]);
                    }
                }
            }
            String text="";
            int imageid=0;
            if (mGame.gameendstate == Game.GAMEENDSTATE.DRAW) {
                Toast.makeText(this, "DRAW!", Toast.LENGTH_SHORT).show();
                if(mGame.mplaymode==PLAYMODE.ONEPLAYER){
                    text="This game is Draw!";
                    imageid=R.drawable.bg;
                }else{
                    text="This game is Draw!";
                    imageid=0;
                }
            } else if (mGame.gameendstate == Game.GAMEENDSTATE.PLAYERONE) {
                if(mGame.mplaymode==PLAYMODE.ONEPLAYER){
                    text="You have Won!";
                    imageid=R.drawable.bg;
                }else{
                    text="Player Won This round!";
                    imageid=R.drawable.ex;
                }
            } else if (mGame.gameendstate == Game.GAMEENDSTATE.PLAYERTWO) {
                if(mGame.mplaymode==PLAYMODE.ONEPLAYER){
                    text="You have Lose!";
                    imageid=R.drawable.bg;
                }else{
                    text="Player Won This round!";
                    imageid=R.drawable.oh;
                }
            }
            showToast(imageid,text);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startGame();
                }
            }, 1000);
        }
    }
    void showToast(int id,String s){
        Drawable d;
        if(id==0){
            d=null;
        }else{
            d= ContextCompat.getDrawable(this,id);
        }
        toastimage.setImageDrawable(d);
        toasttext.setText(s);
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastlayout);
        toast.show();
    }
    private void drawwinline(int i, int id) {
        final float scale = this.getResources().getDisplayMetrics().density;
        switch (i) {
            case 0: {                       //draw line on the row num id
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) ivgameresultrow.getLayoutParams();
                int top;
                if (id == 0) {
                    top = 50;
                } else if (id == 1) {
                    top = 150;
                } else {
                    top = 250;
                }
                layoutParams.setMargins(0, (int) (scale * top + 0.5f), 0, 0);
                ivgameresultrow.setVisibility(View.VISIBLE);
                break;
            }
            case 1: {                      //draw line on col num id
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) ivgameresultcol.getLayoutParams();
                int start;
                if (id == 0) {
                    start = 50;
                } else if (id == 1) {
                    start = 160;
                } else {
                    start = 270;
                }
             //   layoutParams.setMarginStart((int) (scale * start + 0.5f));
                layoutParams.setMargins((int) (scale * start + 0.5f),0, 0, 0);
                ivgameresultcol.setVisibility(View.VISIBLE);
                break;
            }
            case 2: {                   //draw diagonal line
                ivgameresultdiag1.setVisibility(View.VISIBLE);
                break;
            }
            case 3: {                    //draw diagonal line
                ivgameresultdiag2.setVisibility(View.VISIBLE);
                break;
            }
        }
    }
}
