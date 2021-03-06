package com.example.mohamed.tictactoe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import java.util.Arrays;

/**
 * Created by CompuCity on 1/30/2018.
 */

public class Game {
    enum PLAYMODE {
        ONEPLAYER, TWOPLAYER;
    }

    enum PLAYTURN {
        FIRST, SECOND;
    }

    enum PLAYITEM {      //to fill playmatrix
        X, O, NP;
    }

    enum GAMEENDSTATE {
        PLAYERONE, PLAYERTWO, DRAW, RUNNING;
    }

    enum GAMEDIFFICULTY {
        EASY, MEDIUM, HARD, EXPERT;
    }

    public PLAYMODE mplaymode;
    public PLAYTURN mplayerturn;
    public PLAYITEM[][] playmatrix;
    private Context mContext;
    public GAMEENDSTATE gameendstate;
    public GAMEDIFFICULTY difficulty = GAMEDIFFICULTY.EASY;
    final MediaPlayer mp1;
    final MediaPlayer mp2;
    final MediaPlayer mpend;
    final MediaPlayer mpchangemode;
    int player1win;
    int player2win;
    int singlemodedraw;
    int userwin;
    int Androidwin;
    int multimodedraw;

    public Game(Context context) {
        mplaymode = PLAYMODE.ONEPLAYER;
        mplayerturn = PLAYTURN.FIRST;
        playmatrix = new PLAYITEM[3][3];
        mContext = context;
        mp1 = MediaPlayer.create(context, R.raw.soundc1);
        mp2 = MediaPlayer.create(context, R.raw.soundc2);
        mpend = MediaPlayer.create(context, R.raw.soundend);
        mpchangemode = MediaPlayer.create(context, R.raw.soundcontrol);
        Initialize();
    }

    public void Initialize() {
        //make the playerturn is first
        mplayerturn = PLAYTURN.FIRST;
        //clear the gameplay matrix
        for (int i = 0; i < 3; i++)
            Arrays.fill(playmatrix[i], PLAYITEM.NP);
        //make the winner is RUNNING;
        gameendstate = GAMEENDSTATE.RUNNING;
    }

    public Drawable getPlayImage() {
        Drawable d = null;
        if (mplayerturn == PLAYTURN.FIRST) {
            d = mContext.getResources().getDrawable(R.drawable.ex);
        } else if (mplayerturn == PLAYTURN.SECOND) {
            d = mContext.getResources().getDrawable(R.drawable.oh);
        }
        return d;
    }

    public Drawable getModeImage() {
        Drawable d = null;
        if (mplaymode == PLAYMODE.ONEPLAYER) {
            d = mContext.getResources().getDrawable(R.drawable.oneplayer);
        } else if (mplaymode == PLAYMODE.TWOPLAYER) {
            d = mContext.getResources().getDrawable(R.drawable.twoplayers);
        }
        return d;
    }

    public int getTurnImage() {
        int d = 0;
        if (mplayerturn == PLAYTURN.FIRST) {
            d = R.drawable.xp;
        } else if (mplayerturn == PLAYTURN.SECOND) {
            d = R.drawable.op;
        }
        return d;
    }

    public void  play(int id) {
        int n = id / 3;
        int m = id % 3;
        if (mplayerturn == PLAYTURN.FIRST) {
            playmatrix[n][m] = PLAYITEM.X;
        } else {
            playmatrix[n][m] = PLAYITEM.O;
        }
    }

    //check if the game is finished
    public class ENDSTATE{
        int row = -1;
        int col = -1;
        int diagonal1 = -1;
        int diagonal2 = -1;
        GAMEENDSTATE mgameendstate=GAMEENDSTATE.RUNNING;
        ENDSTATE(){

        }
        int[] getResult(){
            return new int[]{row,col,diagonal1,diagonal2};
        }
    }
    public ENDSTATE checkEnd() {
        ENDSTATE endstate=new ENDSTATE();
        for (int i = 0; i < 3; i++) {
            PLAYITEM flagrow = playmatrix[i][0];
            PLAYITEM flagcol = playmatrix[0][i];
            if (flagrow != PLAYITEM.NP && playmatrix[i][1] == flagrow && playmatrix[i][2] == flagrow) {
                endstate.row = i;
                if (flagrow == PLAYITEM.X) {
                    endstate.mgameendstate = GAMEENDSTATE.PLAYERONE;
                } else {
                    endstate.mgameendstate = GAMEENDSTATE.PLAYERTWO;
                }
            }
            if (flagcol != PLAYITEM.NP && playmatrix[1][i] == flagcol && playmatrix[2][i] == flagcol) {
                endstate.col = i;
                if (flagcol == PLAYITEM.X) {
                    endstate.mgameendstate = GAMEENDSTATE.PLAYERONE;
                } else {
                    endstate.mgameendstate = GAMEENDSTATE.PLAYERTWO;
                }
            }
        }
        if (playmatrix[0][0] != PLAYITEM.NP && playmatrix[0][0] == playmatrix[1][1] && playmatrix[1][1] == playmatrix[2][2]) {
            endstate.diagonal1 = 1;
            if (playmatrix[0][0] == PLAYITEM.X) {
                endstate.mgameendstate = GAMEENDSTATE.PLAYERONE;
            } else {
                endstate.mgameendstate = GAMEENDSTATE.PLAYERTWO;
            }
        }
        if (playmatrix[2][0] != PLAYITEM.NP && playmatrix[2][0] == playmatrix[1][1] && playmatrix[1][1] == playmatrix[0][2]) {
            endstate.diagonal2 = 1;
            if (playmatrix[2][0] == PLAYITEM.X) {
                endstate.mgameendstate = GAMEENDSTATE.PLAYERONE;
            } else {
                endstate.mgameendstate = GAMEENDSTATE.PLAYERTWO;
            }
        }
        boolean full = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (playmatrix[i][j] == PLAYITEM.NP) {  //if game is still running
                    full = false;
                }
            }
        }
        if (endstate.mgameendstate == GAMEENDSTATE.RUNNING) {     // no one won
            if (full) {
                endstate.mgameendstate = GAMEENDSTATE.DRAW;
            }
        }
        return endstate;
    }

    boolean canPlay(int id, boolean user) {               //true if free block & 2 player| 1player on his turn
        int n = id / 3;
        int m = id % 3;
        return canPlay(n,m,user);
    }
    boolean canPlay(int n,int m,boolean user){
        if (gameendstate == GAMEENDSTATE.RUNNING && playmatrix[n][m] == PLAYITEM.NP) {
            if (mplaymode == PLAYMODE.TWOPLAYER) {
                return true;
            } else if (mplayerturn == PLAYTURN.FIRST) {
                return true;
            } else if (!user) {              //android can androidPlay
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
