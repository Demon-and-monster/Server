package com.dam.server;

import org.springframework.stereotype.Service;

@Service
public class GameManager {

}

class Game {
    public Player black;
    public Player red;
    public boolean scoreCount;
    public long lastMove;
    public int blackTime;
    public int redTime;
    public boolean blackTurn;

    /**
     * piece: <color><type><number>
     * x for blank
     * color: b for black and r for red
     * types:
     * car => c
     * horse => h
     * elephant => e
     * knight => k
     * king => only color
     * soldier => s
     * cannon => a
     */


    public String[][] gameBoard = { // y, x and 0 <= y <= 9, 0 <= x <= 8
            {"bc1", "bh1", "be1", "bk1", "b", "bk2", "be2", "bh2", "bc2"},
            {"x", "x", "x", "x", "x", "x", "x", "x", "x"},
            {"x", "ba1", "x", "x", "x", "x", "x", "ba2", "x"},
            {"bs1", "x", "bs2", "x", "bs3", "x", "bs4", "x", "bs5"},
            {"x", "x", "x", "x", "x", "x", "x", "x", "x"},
            {"x", "x", "x", "x", "x", "x", "x", "x", "x"},
            {"rs1", "x", "rs2", "x", "rs3", "x", "rs4", "x", "rs5"},
            {"x", "ra1", "x", "x", "x", "x", "x", "ra2", "x"},
            {"x", "x", "x", "x", "x", "x", "x", "x", "x"},
            {"rc1", "rh1", "re1", "rk1", "r", "rk2", "re2", "rh2", "rc2"}
    };

    public Game(Player black, Player red, boolean scoreCount) {
        this.black = black;
        this.red = red;
        this.scoreCount = scoreCount;
        lastMove = System.currentTimeMillis();
        blackTime = 60 * 60 * 1000;
        redTime = 60 * 60 * 1000;
        blackTurn = false;
    }

    //return 0 for ok, -1 for illegal move, -2 for checked
    public int move(String action){
        String[][] movedGameBoard = new String[10][9];
        for(int i = 0; i < 10; i++){
            System.arraycopy(gameBoard[i], 0, movedGameBoard[i], 0, 9);
        }
        if(checked(movedGameBoard, blackTurn?'r':'b')){
            
        }

        return 0;
    }

    public boolean checked(String[][] gameBoard, char player){
        return false;
    }
}