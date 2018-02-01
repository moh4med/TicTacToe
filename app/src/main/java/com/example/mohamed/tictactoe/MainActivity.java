package com.example.mohamed.tictactoe;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
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
import android.widget.RadioButton;
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
    private ImageView iv_xwin;
    private ImageView iv_owin;
    private ImageView iv_draw;
    ImageView toastimage;
    TextView toasttext;
    View toastlayout;
    Agent androidAgent;
    public static final String PREFS_NAME = "MyPrefsFile";
    private String PLAYER1WINTAG="PLAYER1WINTAG";
    private String PLAYER2WINTAG="PLAYER2WINTAG";
    private String SINGLEMODEDRAWTAG="SINGLEMODEDRAWTAG";
    private String ANDROIDWINTAG="ANDROIDWINTAG";
    private String USERWINTAG="USERWINTAG";
    private String MULTIMODEDRAWWINTAG="MULTIMODEDRAWWINTAG";
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
        iv_xwin = (ImageView) findViewById(R.id.iv_xwin);
        iv_owin = (ImageView) findViewById(R.id.iv_owin);
        iv_draw = (ImageView) findViewById(R.id.iv_draw);
        buttonchangeMode = (Button) findViewById(R.id.buttonchangeplaymode);
        LayoutInflater inflater = getLayoutInflater();
        toastlayout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        toastimage = (ImageView) toastlayout.findViewById(R.id.toastimage);
        toasttext = (TextView) toastlayout.findViewById(R.id.toasttext);
        androidAgent = new Agent(mGame);
        setupSharedData();
        setupGameDifficulty();
        startGame();
    }
    void setupSharedData(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        mGame.player1win=settings.getInt(PLAYER1WINTAG,0);
        mGame.player2win=settings.getInt(PLAYER2WINTAG,0);
        mGame.singlemodedraw=settings.getInt(SINGLEMODEDRAWTAG,0);
        mGame.userwin=settings.getInt(USERWINTAG,0);
        mGame.Androidwin=settings.getInt(ANDROIDWINTAG,0);
        mGame.multimodedraw=settings.getInt(MULTIMODEDRAWWINTAG,0);
    }
    private void setupGameDifficulty() {
        mdifficultytap.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbtneasy) {
                    Toast.makeText(getApplicationContext(), "EASY", Toast.LENGTH_SHORT).show();
                    mGame.difficulty = Game.GAMEDIFFICULTY.EASY;
                } else if (checkedId == R.id.rbtnmed) {
                    Toast.makeText(getApplicationContext(), "MEDIUM", Toast.LENGTH_SHORT).show();
                    mGame.difficulty = Game.GAMEDIFFICULTY.MEDIUM;
                } else if (checkedId == R.id.rbtnhard) {
                    Toast.makeText(getApplicationContext(), "HARD", Toast.LENGTH_SHORT).show();
                    mGame.difficulty = Game.GAMEDIFFICULTY.HARD;
                } else if (checkedId == R.id.rbtnexpert) {
                    Toast.makeText(getApplicationContext(), "EXPERT!", Toast.LENGTH_SHORT).show();
                    mGame.difficulty = Game.GAMEDIFFICULTY.EXPERT;
                }
                mGame.mpchangemode.start();
                startGame();
            }
        });
    }

    public void changeplaymode(View view) {
        mGame.mpchangemode.start();
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
    }

    private void SetGameState() {
        int x, o, d;
        if (mGame.mplaymode == PLAYMODE.ONEPLAYER) {
            x = mGame.userwin;
            o = mGame.Androidwin;
            d = mGame.singlemodedraw;
        } else {
            x = mGame.player1win;
            o = mGame.player2win;
            d = mGame.multimodedraw;
        }

        setnumberonImageView(x, iv_xwin);
        setnumberonImageView(o, iv_owin);
        setnumberonImageView(d, iv_draw);
    }

    private void setnumberonImageView(int x, ImageView view) {
        int dig = 0;
        LinearLayout layout = (LinearLayout) view.getParent();
        int totiv = layout.getChildCount();
        int f;
        String name;
        int resourceId;
        Drawable d;
        while (x > 0||(x==0&&dig==0)) {
            dig++;
            //make new variable if no on exist
            if (dig > totiv) {
                view = new ImageView(this);
                view.setLayoutParams(new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                layout.addView(view, 0);
            } else { //use previous one if exist
                view = (ImageView) layout.getChildAt(totiv - dig);
            }
            f = x % 10;                 //set digit on the view
            x /= 10;
            name = "co" + f;
            resourceId = this.getResources().getIdentifier(name, "drawable",
                    this.getPackageName());
            d = ContextCompat.getDrawable(this, resourceId);
            view.setImageDrawable(d);
            view.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < totiv - dig; i++) {
            view = (ImageView) layout.getChildAt(i);
            view.setVisibility(View.INVISIBLE);
        }

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
        //show the result of the new game
        SetGameState();
    }

    public void newPlay(View view) {
        Button btn = (Button) view;
        int id = Integer.parseInt(btn.getTag().toString());
        boolean canPlay = mGame.canPlay(id, true);
        if (canPlay) {
            mGame.play(id);
            makePlay(btn);
        }
    }

    void makePlay(Button btn) {
        Drawable playImage = mGame.getPlayImage();
        btn.setBackground(playImage);
        playSound();
        changeGameTurn();
        boolean end = checkFinished();
        if (end) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mGame.gameendstate != Game.GAMEENDSTATE.RUNNING) {
                        startGame();
                    }
                }
            }, 1500);
        } else {
            if (mGame.mplaymode == PLAYMODE.ONEPLAYER && mGame.mplayerturn == Game.PLAYTURN.SECOND) {        //check if in one player mode make the computer androidPlay
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newAgentPlay();
                    }
                }, 200);

            }
        }
    }

    private void newAgentPlay() {
        int id = androidAgent.androidPlay();
        mGame.play(id);
        id++;                   //for calling the right button name
        String name = "button" + id;
        int resid = this.getResources().getIdentifier(name, "id", getPackageName());
        Button btn = (Button) findViewById(resid);
        makePlay(btn);
    }

    private void playSound() {
        if (mGame.mplayerturn == Game.PLAYTURN.FIRST) {
            mGame.mp1.start();
        } else {
            mGame.mp2.start();
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

    boolean checkFinished() {
        Game.ENDSTATE endstate = mGame.checkEnd();
        int[] result = endstate.getResult();
        mGame.gameendstate = endstate.mgameendstate;
        if (mGame.gameendstate != Game.GAMEENDSTATE.RUNNING) {        //Game is finished
            mGame.mpend.start();
            if (mGame.gameendstate != Game.GAMEENDSTATE.DRAW) {          //draw line if any one WON the game
                for (int i = 0; i < 4; i++) {
                    if (result[i] != -1) {
                        drawwinline(i, result[i]);
                    }
                }
            }
            String text = "";
            int imageid = 0;
            if (mGame.gameendstate == Game.GAMEENDSTATE.DRAW) {
                Toast.makeText(this, "DRAW!", Toast.LENGTH_SHORT).show();
                if (mGame.mplaymode == PLAYMODE.ONEPLAYER) {
                    mGame.singlemodedraw++;
                    text = "This game is Draw!";
                    imageid = R.drawable.drawemoji;
                } else {
                    mGame.multimodedraw++;
                    text = "This game is Draw!";
                    imageid = R.drawable.drawemoji;
                }
            } else if (mGame.gameendstate == Game.GAMEENDSTATE.PLAYERONE) {
                if (mGame.mplaymode == PLAYMODE.ONEPLAYER) {
                    mGame.userwin++;
                    text = "You have Won!";
                    imageid = R.drawable.winemoji;
                } else {
                    mGame.player1win++;
                    text = "Player Won This round!";
                    imageid = R.drawable.ex;
                }
            } else if (mGame.gameendstate == Game.GAMEENDSTATE.PLAYERTWO) {
                if (mGame.mplaymode == PLAYMODE.ONEPLAYER) {
                    mGame.Androidwin++;
                    text = "You have Lose!";
                    imageid = R.drawable.loseemoji;
                } else {
                    mGame.player2win++;
                    text = "Player Won This round!";
                    imageid = R.drawable.oh;
                }
            }
            showToast(imageid, text);
            return true;
        }
        return false;
    }

    void showToast(int id, String s) {
        Drawable d;
        if (id == 0) {
            d = null;
        } else {
            d = ContextCompat.getDrawable(this, id);
        }
        toastimage.setImageDrawable(d);
        toasttext.setText(s);
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastlayout);
        toast.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(PLAYER1WINTAG, mGame.player1win);
        editor.putInt(PLAYER2WINTAG, mGame.player2win);
        editor.putInt(SINGLEMODEDRAWTAG, mGame.singlemodedraw);
        editor.putInt(USERWINTAG, mGame.userwin);
        editor.putInt(ANDROIDWINTAG, mGame.Androidwin);
        editor.putInt(MULTIMODEDRAWWINTAG, mGame.multimodedraw);
        // Commit the edits!
        editor.commit();
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
                layoutParams.setMargins((int) (scale * start + 0.5f), 0, 0, 0);
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

    public void changedifficultychoice(View view) {
        String name = view.getTag().toString();
        int resid = getResources().getIdentifier(name, "id", getPackageName());
        RadioButton rbtn = (RadioButton) findViewById(resid);
        rbtn.setChecked(true);
    }
}
