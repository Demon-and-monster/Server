package com.dam.server;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GameManager {
    final ArrayList<Game> games = new ArrayList<Game>();

    final ArrayList<Player> waitList = new ArrayList<>();

    public GameManager() {
    }

    public void line(){
        synchronized(waitList){
            while(waitList.size() >= 2){
                newGame(waitList.remove(0),waitList.remove(0),true);
            }
        }
    }

    public int lineUp(Player player) {
        synchronized (waitList) {
            if (waitList.contains(player) || player.getGameNumber() < 0) {
                return -1;
            }
            waitList.add(player);
            return 0;
        }
    }

    public boolean leftLine(Player player){
        synchronized (waitList) {
            if (waitList.contains(player)) {
                waitList.remove(player);
                return true;
            }else{
                return false;
            }
        }
    }

    public boolean inLine(Player player){
        synchronized (waitList) {
            return waitList.contains(player);
        }
    }

    public Game getGame(int gameNumber) {
        return games.get(gameNumber);
    }

    public void newGame(Player black, Player red, boolean scoreCount) {
        synchronized (games) {
            Game game = new Game(black, red, scoreCount);
            games.add(game);
            black.play(games.size() - 1);
            red.play(games.size() - 1);
        }
    }
}

class Game {
    public boolean playing;
    public Player black;
    public Player red;
    public boolean scoreCount;
    public long lastMove;
    public long blackTime;
    public long redTime;
    public boolean blackTurn;
    public String moveHistory;

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
            {"rc1", "rh1", "re1", "rk1", "r", "rk2", "re2", "rh2", "rc2"},
            {"x", "x", "x", "x", "x", "x", "x", "x", "x"},
            {"x", "ra1", "x", "x", "x", "x", "x", "ra2", "x"},
            {"rs1", "x", "rs2", "x", "rs3", "x", "rs4", "x", "rs5"},
            {"x", "x", "x", "x", "x", "x", "x", "x", "x"},
            {"x", "x", "x", "x", "x", "x", "x", "x", "x"},
            {"bs1", "x", "bs2", "x", "bs3", "x", "bs4", "x", "bs5"},
            {"x", "ba1", "x", "x", "x", "x", "x", "ba2", "x"},
            {"x", "x", "x", "x", "x", "x", "x", "x", "x"},
            {"bc1", "bh1", "be1", "bk1", "b", "bk2", "be2", "bh2", "bc2"},
    };

    public Game(Player black, Player red, boolean scoreCount) {
        playing = true;
        this.black = black;
        this.red = red;
        this.scoreCount = scoreCount;
        lastMove = System.currentTimeMillis();
        blackTime = 60 * 60 * 1000;
        redTime = 60 * 60 * 1000;
        blackTurn = false;
    }

    public void surrender(Player player) {
        if (scoreCount) {
            Player win = black.getUsername().equals(player.getUsername()) ? red : black;
            Player lose = black.getUsername().equals(player.getUsername()) ? black : red;
            int winner = win.getRating();
            int loser = lose.getRating();
            win.win(loser);
            lose.lose(winner);
        }
        playing = false;
    }

    public boolean isOnesTurn(Player player) {
        return (blackTurn ? black.getUsername() : red.getUsername()).equals(player.getUsername());
    }

    public int getTimeLeft() {
        if (blackTurn) {
            return (int) ((blackTime - System.currentTimeMillis()) / 1000);
        } else {
            return (int) ((redTime - System.currentTimeMillis()) / 1000);
        }
    }

    //return 0 for ok, -1 for illegal move, -2 for checked, -3 for checkmate
    public int move(String action) {
        long requestTime = System.currentTimeMillis();

        // legal?
        if (action.length() != 4) {
            return -1;
        }
        int[][] pos = toIntegerPosition(action);
        if (pos[0][0] < 0 || pos[0][0] > 8
                || pos[0][1] < 0 || pos[0][1] > 9
                || pos[1][0] < 0 || pos[1][0] > 8
                || pos[1][1] < 0 || pos[1][1] > 9) {
            return -1;
        }
        if (gameBoard[pos[0][1]][pos[0][0]].charAt(1) != (blackTurn ? 'b' : 'r')) {
            return -1;
        }
        if (!legalMove(gameBoard, action)) {
            return -1;
        }

        // checked?
        if (isChecked(action, blackTurn ? 'r' : 'b')) {
            return -2;
        }

        // checkmate?
        checkmate:
        {
            if (isChecked(action, blackTurn ? 'b' : 'r')) {
                String[][] movedGameBoard = new String[10][9];
                for (int i = 0; i < 9; i++) {
                    System.arraycopy(gameBoard[i], 0, movedGameBoard[i], 0, 10);
                }
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (gameBoard[i][j].charAt(0) == (blackTurn ? 'r' : 'b')) {
                            for (int a = 0; i < 10; i++) {
                                for (int b = 0; j < 9; j++) {
                                    if (legalMove(movedGameBoard, ((char) ('a' + j)) + String.valueOf(i) + ((char) ('a' + b)) + a)) {
                                        if (!isChecked(action, ((char) ('a' + j)) + String.valueOf(i) + ((char) ('a' + b)) + a, (blackTurn ? 'b' : 'r'))) {
                                            break checkmate;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // save the move
                moveHistory += action;
                gameBoard[pos[1][1]][pos[1][0]] = gameBoard[pos[0][1]][pos[0][0]];
                gameBoard[pos[0][1]][pos[0][0]] = "x";
                return -2;
            }
        }


        // save the move
        moveHistory += action;
        gameBoard[pos[1][1]][pos[1][0]] = gameBoard[pos[0][1]][pos[0][0]];
        gameBoard[pos[0][1]][pos[0][0]] = "x";

        // process time
        {
            if (blackTurn) {
                blackTime -= requestTime;
            } else {
                redTime -= requestTime;
            }
        }
        lastMove = System.currentTimeMillis();
        return 0;
    }

    public boolean legalMove(String[][] gameBoard, String action) {
        int[][] moves = toIntegerPosition(action);

        // move blank
        if (gameBoard[moves[0][0]][moves[0][1]].equals("x")) {
            return false;
        }

        // eat himself
        if (gameBoard[moves[0][1]][moves[0][0]].charAt(0) == gameBoard[moves[1][1]][moves[1][0]].charAt(0)) {
            return false;
        }

        // king moving
        if (gameBoard[moves[0][1]][moves[0][0]].equals("b") || gameBoard[moves[0][1]][moves[0][1]].equals("r")) {
            if (Math.abs(moves[0][0] - moves[1][0]) + Math.abs(moves[0][1] - moves[1][1]) != 1) {
                return false;
            }
            if (moves[1][0] < 3 || moves[1][0] > 5
                    || moves[1][1] < (blackTurn ? 7 : 0) || moves[1][1] > (blackTurn ? 9 : 2)) {
                return false;
            }
            return true;
        }

        // others moving
        switch (gameBoard[moves[0][1]][moves[0][0]].charAt(1)) {
            case 'c':
                if (moves[0][0] == moves[1][0]) {
                    if (moves[0][1] == moves[1][1]) {
                        return false;
                    } else {
                        for (int i = moves[0][1] + (moves[0][1] > moves[1][1] ? -1 : 1); (moves[0][1] > moves[1][1] ? i > moves[1][1] : i < moves[1][1]); i += (moves[0][1] > moves[1][1] ? -1 : 1)) {
                            if (!gameBoard[i][moves[0][0]].equals("x")) {
                                return false;
                            }
                        }
                    }
                } else {
                    if (moves[0][1] == moves[1][1]) {
                        for (int i = moves[0][0] + (moves[0][0] > moves[1][0] ? -1 : 1); (moves[0][0] > moves[1][0] ? i > moves[1][0] : i < moves[1][0]); i += (moves[0][0] > moves[1][0] ? -1 : 1)) {
                            if (!gameBoard[moves[0][1]][i].equals("x")) {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                }
                return true;
            case 'h':
                if ((Math.abs(moves[0][0] - moves[1][0]) == 1 && Math.abs(moves[0][1] - moves[1][1]) == 2)
                        || (Math.abs(moves[0][0] - moves[1][0]) == 2 && Math.abs(moves[0][1] - moves[1][1]) == 1)) {
                    if (Math.abs(moves[0][0] - moves[1][0]) == 2) {
                        return gameBoard[moves[0][1]][moves[0][0] + (moves[0][0] - moves[1][0] > 0 ? -1 : 1)].equals("x");
                    } else {
                        return gameBoard[moves[0][1] + (moves[0][1] - moves[1][1] > 0 ? -1 : 1)][moves[0][0]].equals("x");
                    }
                }
                return false;
            case 'e':
                if (Math.abs(moves[0][0] - moves[1][0]) == 2 && Math.abs(moves[0][1] - moves[1][1]) == 2) {
                    if (gameBoard[(moves[0][1] + moves[1][1]) / 2][(moves[0][0] + moves[1][0]) / 2].equals("x")) {
                        return blackTurn ? moves[1][1] >= 5 : moves[1][1] <= 4;
                    }
                }
                return false;
            case 'k':
                if (Math.abs(moves[0][0] - moves[1][0]) == 1 && Math.abs(moves[0][1] - moves[1][1]) == 1) {
                    if (moves[1][0] < 3 || moves[1][0] > 5) {
                        return false;
                    }
                    return blackTurn ? moves[1][1] >= 7 : moves[1][1] <= 2;
                }
                return false;
            case 's':
                if (moves[0][1] - moves[1][1] == (blackTurn ? 1 : -1)) {
                    return moves[0][0] == moves[1][0];
                } else if (Math.abs(moves[0][1] - moves[1][1]) == 0) {
                    if ((blackTurn && moves[0][1] <= 4) || ((!blackTurn) && moves[0][1] >= 5)) {
                        return Math.abs(moves[0][0] - moves[1][0]) == 1;
                    }
                }
                return false;
            case 'a':
                if (gameBoard[moves[1][1]][moves[1][0]].equals("x")) {
                    // move (as car)
                    if (moves[0][0] == moves[1][0]) {
                        if (moves[0][1] == moves[1][1]) {
                            return false;
                        } else {
                            for (int i = moves[0][1] + (moves[0][1] > moves[1][1] ? -1 : 1); (moves[0][1] > moves[1][1] ? i > moves[1][1] : i < moves[1][1]); i += (moves[0][1] > moves[1][1] ? -1 : 1)) {
                                if (!gameBoard[i][moves[0][0]].equals("x")) {
                                    return false;
                                }
                            }
                        }
                    } else {
                        if (moves[0][1] == moves[1][1]) {
                            for (int i = moves[0][0] + (moves[0][0] > moves[1][0] ? -1 : 1); (moves[0][0] > moves[1][0] ? i > moves[1][0] : i < moves[1][0]); i += (moves[0][0] > moves[1][0] ? -1 : 1)) {
                                if (!gameBoard[moves[0][1]][i].equals("x")) {
                                    return false;
                                }
                            }
                        } else {
                            return false;
                        }
                    }
                    return true;
                } else {
                    // eat (pass one thing)
                    boolean passed = false;
                    if (moves[0][0] == moves[1][0]) {
                        if (moves[0][1] == moves[1][1]) {
                            return false;
                        } else {
                            for (int i = moves[0][1] + (moves[0][1] > moves[1][1] ? -1 : 1); (moves[0][1] > moves[1][1] ? i > moves[1][1] : i < moves[1][1]); i += (moves[0][1] > moves[1][1] ? -1 : 1)) {
                                if (!gameBoard[i][moves[0][0]].equals("x")) {
                                    if (passed) {
                                        return false;
                                    } else {
                                        passed = true;
                                    }
                                }
                            }
                        }
                    } else {
                        if (moves[0][1] == moves[1][1]) {
                            for (int i = moves[0][0] + (moves[0][0] > moves[1][0] ? -1 : 1); (moves[0][0] > moves[1][0] ? i > moves[1][0] : i < moves[1][0]); i += (moves[0][0] > moves[1][0] ? -1 : 1)) {
                                if (!gameBoard[moves[0][1]][i].equals("x")) {
                                    if (passed) {
                                        return false;
                                    } else {
                                        passed = true;
                                    }
                                }
                            }
                        } else {
                            return false;
                        }
                    }
                    return passed;
                }
        }

        return true;
    }

    public int[][] toIntegerPosition(String action) {
        return new int[][]{{action.charAt(0) - 'a', Integer.parseInt(String.valueOf(action.charAt(1)))}, {action.charAt(2) - 'a', Integer.parseInt(String.valueOf(action.charAt(3)))}};
    }

    public boolean isChecked(String action, char checker) {
        int[][] pos = toIntegerPosition(action);
        String[][] movedGameBoard = new String[10][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(gameBoard[i], 0, movedGameBoard[i], 0, 10);
        }
        movedGameBoard[pos[1][1]][pos[1][0]] = movedGameBoard[pos[0][1]][pos[0][0]];
        movedGameBoard[pos[0][1]][pos[0][0]] = "x";
        StringBuilder dest = new StringBuilder();
        for (int i = 0; dest.toString().isBlank() && i < 10; i++) {
            for (int j = 0; dest.toString().isBlank() && j < 9; j++) {
                if (gameBoard[i][j].equals((checker == 'b' ? "r" : "b"))) {
                    dest.append((char) ('a' + j));
                    dest.append(i);
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 9; j++) {
                if (gameBoard[i][j].charAt(0) == checker) {
                    action = String.valueOf((char) ('a' + j)) + i + dest;
                    if (legalMove(movedGameBoard, action)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isChecked(String action, String action2, char checker) {
        int[][] pos = toIntegerPosition(action);
        int[][] pos2 = toIntegerPosition(action2);
        String[][] movedGameBoard = new String[10][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(gameBoard[i], 0, movedGameBoard[i], 0, 10);
        }
        movedGameBoard[pos[1][1]][pos[1][0]] = movedGameBoard[pos[0][1]][pos[0][0]];
        movedGameBoard[pos[0][1]][pos[0][0]] = "x";
        movedGameBoard[pos2[1][1]][pos2[1][0]] = movedGameBoard[pos2[0][1]][pos2[0][0]];
        movedGameBoard[pos2[0][1]][pos2[0][0]] = "x";
        StringBuilder dest = new StringBuilder();
        for (int i = 0; dest.toString().isBlank() && i < 10; i++) {
            for (int j = 0; dest.toString().isBlank() && j < 9; j++) {
                if (gameBoard[i][j].equals((checker == 'b' ? "r" : "b"))) {
                    dest.append((char) ('a' + j));
                    dest.append(i);
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 9; j++) {
                if (gameBoard[i][j].charAt(0) == checker) {
                    action = String.valueOf((char) ('a' + j)) + i + dest;
                    if (legalMove(movedGameBoard, action)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getMoveHistory() {
        return moveHistory;
    }
}