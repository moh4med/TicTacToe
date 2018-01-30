package com.example.mohamed.tictactoe;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mohamed.tictactoe.Game.PLAYMODE;

public class MainActivity extends AppCompatActivity {
    private LinearLayout mygameplaybuttons;
    private LinearLayout mturntap;
    private RadioGroup mdifficultytap;
    private Game mGame;
    private ImageView ivTurn;
    private Button buttonchangeMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new Game(this);
        mdifficultytap = (RadioGroup) findViewById(R.id.difficutytap);
        mturntap = (LinearLayout) findViewById(R.id.turntap);
        mygameplaybuttons = (LinearLayout) findViewById(R.id.gameplaybuttons);
        ivTurn = (ImageView) findViewById(R.id.ivtype);
        buttonchangeMode = (Button) findViewById(R.id.buttonchangeplaymode);
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

            if(mGame.gameendstate!= Game.GAMEENDSTATE.DRAW) {          //draw line if any one WON the game
                for (int i = 0; i < 4; i++) {
                    if (result[0] != -1) {
                        //draw line
                    }
                }
            }
            if (mGame.gameendstate == Game.GAMEENDSTATE.DRAW) {
                Toast.makeText(this,"DRAW!",Toast.LENGTH_SHORT).show();
            } else if (mGame.gameendstate == Game.GAMEENDSTATE.PLAYERONE) {
                Toast.makeText(this,"PLAYER1 WON!",Toast.LENGTH_SHORT).show();
            } else if (mGame.gameendstate == Game.GAMEENDSTATE.PLAYERTWO) {
                Toast.makeText(this,"PLAYER 2 WON!",Toast.LENGTH_SHORT).show();
            }
            startGame();
        }
    }
}
