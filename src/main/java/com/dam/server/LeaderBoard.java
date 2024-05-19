package com.dam.server;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LeaderBoard {
    private final ArrayList<Leader> leaderBoard = new ArrayList<>();

    public LeaderBoard() {}

    public void updateScore(String username, int score) {
        synchronized (leaderBoard) {
            for (int i = 0; i < leaderBoard.size(); i++) {
                if (leaderBoard.get(i).username.equals(username)) {
                    leaderBoard.remove(i);
                    break;
                }
            }
            if(score >= 2000){
                for(int i = 0; i < leaderBoard.size(); i++){
                    if(leaderBoard.get(i).score < score){
                        leaderBoard.add(i, new Leader(username, score));
                    }
                }
            }
        }
    }

    public String getLeaderBoard() {
        StringBuilder res = new StringBuilder();
        synchronized (leaderBoard) {
            for(int i = 0; i < 10; i++){
                if(leaderBoard.size() > i){
                    res.append(leaderBoard.get(i));
                }
                if(i != 9){
                    res.append(",");
                }
            }
        }
        return res.toString();
    }
}


class Leader {
    public String username;
    public int score;

    public Leader(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public Leader() {
        this.username = "";
        this.score = -1;
    }
}