package com.example.mohamed.tictactoe;

import android.os.Handler;
import android.util.Log;

import java.util.Random;

/**
 * Created by CompuCity on 1/31/2018.
 */

public class Agent {
    Game mGame;
     Agent(Game game){
         mGame=game;
     }
     int androidPlay(){
         if(mGame.difficulty== Game.GAMEDIFFICULTY.EASY){
            return easyplay();
         }else if(mGame.difficulty== Game.GAMEDIFFICULTY.MEDIUM){
            return mediumplay();
         }else if(mGame.difficulty== Game.GAMEDIFFICULTY.HARD){
             return hardplay();
         }else{
             return expertplay();
         }
     }

    private int expertplay() {
        return -1;
    }

    private int hardplay() {
         return -1;
    }

    private int mediumplay() {
         return -1;
    }

    private int easyplay(){
        Random rand = new Random();
        while(true){
            int  id = rand.nextInt(9);
            if(mGame.play(id,false)){
                Log.e("AGENT","found random at "+id/3+" "+id%3);
                return id;
            }
        }
     }

}
