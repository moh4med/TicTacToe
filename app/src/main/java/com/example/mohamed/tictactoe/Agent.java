package com.example.mohamed.tictactoe;

import android.util.Log;

import java.util.Random;

/**
 * Created by CompuCity on 1/31/2018.
 */

public class Agent {
    Game mGame;
    int GAMERUNNINGFLAG = -1000;
    int GAMEWINGFLAG = 10;
    int GAMELOSEFLAG = -10;
    int GAMEDRAWFLAG = 0;

    Agent(Game game) {
        mGame = game;
    }

    int androidPlay() {
        if (mGame.difficulty == Game.GAMEDIFFICULTY.EASY) {
            return easyplay();
        } else if (mGame.difficulty == Game.GAMEDIFFICULTY.MEDIUM) {
            return mediumplay();
        } else if (mGame.difficulty == Game.GAMEDIFFICULTY.HARD) {
            return hardplay();
        } else {
            return expertplay();
        }
    }

    private int expertplay() {
        return findBestMode();
    }

    private int findBestMode() {
        int best = -1000;
        int row = -1;
        int col = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (canPlayVirtual(i, j)) {
                    mGame.playmatrix[i][j] = Game.PLAYITEM.O;   //make move
                    int val = minmax(0, false);   //find value
                    mGame.playmatrix[i][j] = Game.PLAYITEM.NP;  //restore state

                    if (val > best) {          //update new value
                        best = val;
                        row = i;
                        col = j;
                    }
                }
            }
        }
        int id = row * 3 + col;
        Log.e("AGENT","row:"+row+" , col:"+col+" bestval:"+best);
        return id;
    }

    private int minmax(int depth, boolean MAXIMIZER) {
        int score = matrixEvaluate(depth);
        if (score != GAMERUNNINGFLAG) {     //game finished
            return score;
        }
        if (MAXIMIZER) {
            int best = -10000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (canPlayVirtual(i, j)) {
                        mGame.playmatrix[i][j] = Game.PLAYITEM.O;   //make move
                        int val = minmax(depth+1, false);   //find value
                        mGame.playmatrix[i][j] = Game.PLAYITEM.NP;  //restore state
                        if (val > best) {          //update new value
                            best = val;
                        }
                    }
                }
            }
            return best;
        } else {
            int best = 10000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (canPlayVirtual(i, j)) {
                        mGame.playmatrix[i][j] = Game.PLAYITEM.X;   //make user move
                        int val = minmax(depth+1, true);   //find value
                        mGame.playmatrix[i][j] = Game.PLAYITEM.NP;  //restore state
                        if (val < best) {          //update new value
                            best = val;
                        }
                    }
                }
            }
            return best;
        }
    }
    boolean canPlayVirtual(int n,int m){
        boolean canplay=false;
        if (mGame.gameendstate == Game.GAMEENDSTATE.RUNNING && mGame.playmatrix[n][m] == Game.PLAYITEM.NP) {
           canplay=true;
        }
        return canplay;
    }
    private int matrixEvaluate(int depth) {
        Game.ENDSTATE endstate = mGame.checkEnd();
        if (endstate.mgameendstate == Game.GAMEENDSTATE.PLAYERTWO) {   //Android win
            return GAMEWINGFLAG - depth;
        } else if (endstate.mgameendstate == Game.GAMEENDSTATE.PLAYERONE) { //user win
            return GAMELOSEFLAG + depth;
        } else if (endstate.mgameendstate == Game.GAMEENDSTATE.DRAW) { //Draw
            return GAMEDRAWFLAG;
        }
        return GAMERUNNINGFLAG;       //draw or not finished
    }

    private int hardplay() {
        return -1;
    }

    private int mediumplay() {
        return -1;
    }

    private int easyplay() {
        Random rand = new Random();
        while (true) {
            int id = rand.nextInt(9);
            if (mGame.canPlay(id, false)) {
                Log.e("AGENT", "found random at " + id / 3 + " " + id % 3);
                mGame.play(id);
                return id;
            }
        }
    }

}
