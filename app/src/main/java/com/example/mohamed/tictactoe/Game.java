package com.example.mohamed.tictactoe;

import android.content.Context;
import android.graphics.drawable.Drawable;

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
    enum GAMEENDSTATE{
        PLAYERONE,PLAYERTWO,DRAW, RUNNING;
    }
    public PLAYMODE mplaymode;
    public PLAYTURN mplayerturn;
    public PLAYITEM[][] playmatrix;
    private Context mContext;
    public GAMEENDSTATE gameendstate;
    public Game(Context context) {
        mplaymode = PLAYMODE.ONEPLAYER;
        mplayerturn = PLAYTURN.FIRST;
        playmatrix = new PLAYITEM[3][3];
        mContext = context;
        Initialize();
    }

    public void Initialize() {
        //make the playerturn is first
        mplayerturn=PLAYTURN.FIRST;
        //clear the gameplay matrix
        for (int i = 0; i < 3; i++)
            Arrays.fill(playmatrix[i], PLAYITEM.NP);
        //make the winner is RUNNING;
        gameendstate=GAMEENDSTATE.RUNNING;

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
        } else if (mplaymode ==PLAYMODE.TWOPLAYER) {
            d = mContext.getResources().getDrawable(R.drawable.twoplayers);
        }
        return d;
    }
    public int getTurnImage() {
        int d = 0;
        if (mplayerturn == PLAYTURN.FIRST) {
            d=R.drawable.xp;
        } else if (mplayerturn == PLAYTURN.SECOND) {
            d =R.drawable.op;
        }
        return d;
    }

    public boolean play(int id) {
        int n = id / 3;
        int m = id % 3;
        if (canPlay(n,m)) {
            if (mplayerturn == PLAYTURN.FIRST) {
                playmatrix[n][m] = PLAYITEM.X;
            } else {
                playmatrix[n][m] = PLAYITEM.O;
            }
            return true;
        }else{
            return false;
        }
    }
    //check if the game is finished
    public int[] checkEnd() {
        int row=-1;
        int col=-1;
        int diagonal1=-1;
        int diagonal2=-1;
        int gamefinish=1;
        int playerwon=-1;
        for (int i=0;i<3;i++){
            PLAYITEM flagrow=playmatrix[i][0];
            PLAYITEM flagcol=playmatrix[0][i];
            if(flagrow!=PLAYITEM.NP&&playmatrix[i][1]==flagrow&&playmatrix[i][2]==flagrow){
                row=i;
                if(flagrow==PLAYITEM.X){
                    gameendstate=GAMEENDSTATE.PLAYERONE;
                }else{
                    gameendstate=GAMEENDSTATE.PLAYERTWO;
                }
            }
            if(flagcol!=PLAYITEM.NP&&playmatrix[1][i]==flagcol&&playmatrix[2][i]==flagcol){
                col=i;
                if(flagcol==PLAYITEM.X){
                    gameendstate=GAMEENDSTATE.PLAYERONE;
                }else{
                    gameendstate=GAMEENDSTATE.PLAYERTWO;
                }
            }
        }
        if(playmatrix[0][0]!=PLAYITEM.NP&&playmatrix[0][0]==playmatrix[1][1]&&playmatrix[1][1]==playmatrix[2][2]){
            diagonal1=1;
            if(playmatrix[0][0]==PLAYITEM.X){
                gameendstate=GAMEENDSTATE.PLAYERONE;
            }else{
                gameendstate=GAMEENDSTATE.PLAYERTWO;
            }
        }
        if(playmatrix[2][0]!=PLAYITEM.NP&&playmatrix[2][0]==playmatrix[1][1]&&playmatrix[1][1]==playmatrix[0][2]){
            diagonal2=1;
            if(playmatrix[2][0]==PLAYITEM.X){
                gameendstate=GAMEENDSTATE.PLAYERONE;
            }else{
                gameendstate=GAMEENDSTATE.PLAYERTWO;
            }
        }
        int []result={row,col,diagonal1,diagonal2};
        boolean full=true;
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                if(playmatrix[i][j]==PLAYITEM.NP){  //if game is still running
                    full=false;
                }
            }
        }
        if(gameendstate==GAMEENDSTATE.RUNNING){     // no one won
            if(full){
                gameendstate=GAMEENDSTATE.DRAW;
            }
            return null;
        }else{
            return result;
        }
    }
    boolean canPlay(int n,int m){               //true if free block & 2 player| 1player on his turn
        if(playmatrix[n][m] == PLAYITEM.NP&&
                (mplaymode==PLAYMODE.TWOPLAYER ||
                        (mplaymode==PLAYMODE.ONEPLAYER&&mplayerturn==PLAYTURN.FIRST))){
            return true;
        }
        return false;
    }
    static class TowPlayerGame {
        static int player1win;
        static int player2win;
        static int draw;
    }

    static class OnePlayerGame {
        static int playerwin;
        static int computerwin;
        static int draw;
    }

}
