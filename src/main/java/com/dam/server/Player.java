package com.dam.server;

import java.time.LocalDate;

public class Player {
    private String username;
    private String password;
    private String email;
    private String emailSha;
    private int rating;

    private int gameNumber = -1;
    private int matchCount = 0;
    private int winCount = 0;
    private int lossCount = 0;
    private String registerDate;
    private int[] gamePlayed = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    public Player(String username, String password, String email, int rating) {
        this.username = username;
        this.password = password;
        this.rating = rating;
        this.email = email;
        this.emailSha = HashUtil.sha256Hex(email);
        this.registerDate = LocalDate.now().toString();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailSha() {
        return emailSha;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailSha(String emailSha) {
        this.emailSha = emailSha;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public int getRating() {
        return rating;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public int getWinCount() {
        return winCount;
    }

    public int getLossCount() {
        return lossCount;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public String getGamePlayed() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < gamePlayed.length; i++) {
            res.append(gamePlayed[i]);
            if (i != gamePlayed.length - 1) {
                res.append(",");
            }
        }
        return res.toString();
    }

    public void play(int gameNumber) {
        for (int i = gamePlayed.length - 1; i > 0; i--) {
            gamePlayed[i] = gamePlayed[i - 1];
        }
        gamePlayed[0] = gameNumber;
        this.gameNumber = gameNumber;
    }

    public void lose(int opponentRating) {
        rating += (int) (-32 * (1 / (Math.pow(10, (rating - opponentRating) / 400d))));
    }

    public void draw(int opponentRating) {
        rating += (int) (32 * (0.5 - (1 / (Math.pow(10, (rating - opponentRating) / 400d)))));
    }

    public void win(int opponentRating) {
        rating += (int) (32 * (1 - (1 / (Math.pow(10, (rating - opponentRating) / 400d)))));
    }
}
