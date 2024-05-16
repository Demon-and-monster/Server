package com.dam.server;

public class Player {
    private String username;
    private String password;
    private String email;
    private String emailSha;
    private int rating;
    private int gameNumber;

    public Player(String username, String password, String email, int rating) {
        this.username = username;
        this.password = password;
        this.rating = rating;
        this.email = email;
        this.emailSha = HashUtil.sha256Hex(email);
        this.gameNumber = -1;
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

    public void lose(int opponentRating) {
        rating += (int) (-32 * (1 / (Math.pow(10, (rating - opponentRating) /400d))));
    }

    public void draw(int opponentRating) {
        rating += (int) (32 * (0.5-(1 / (Math.pow(10, (rating - opponentRating) /400d)))));
    }

    public void win(int opponentRating) {
        rating += (int) (32 * (1-(1 / (Math.pow(10, (rating - opponentRating) /400d)))));
    }
}
