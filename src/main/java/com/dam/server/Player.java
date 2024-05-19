package com.dam.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class Player {
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    LeaderBoard leaderBoard;

    private String username;
    private String password;
    private String email;
    private String emailSha;
    private int rating;

    private int gameNumber = -1; // -1: not in game, -2: in list, -3: wait for friend
    private int matchCount = 0;
    private int winCount = 0;
    private int lossCount = 0;
    private String registerDate;
    private int[] gamePlayed = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<String> friendsRequests = new ArrayList<>();

    public Player(){}

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

    public String getFriendsString() {
        return getString(friends);
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void requestFriend(String username) {
        if (!friendsRequests.contains(username)) {
            friendsRequests.add(username);
        }
    }

    public int acceptFriend(String username) {
        if (friendsRequests.contains(username)) {
            friendsRequests.remove(username);
            friends.add(username);
            ((InMemoryUserDetailsManager) userDetailsService).getPlayerByUsername(username).addFriend(this.username);
            return 0;
        } else {
            return -1;
        }
    }

    public void addFriend(String username) {
        friends.add(username);
    }

    public String getRequestFriendsString() {
        return getString(friendsRequests);
    }

    public ArrayList<String> getFriendsRequests() {
        return friendsRequests;
    }

    private String getString(ArrayList<String> arrayList) {
        if (arrayList.isEmpty()) {
            return "-1";
        } else {
            StringBuilder res = new StringBuilder();

            for (int i = 0; i < arrayList.size(); i++) {
                res.append(arrayList.get(i));
                if (i != arrayList.size() - 1) {
                    res.append(",");
                }
            }

            return res.toString();
        }
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
        matchCount++;
        lossCount++;
        rating += (int) (-32 * (1 / (Math.pow(10, (rating - opponentRating) / 400d))));
        leaderBoard.updateScore(this.username, this.rating);
    }

    public void draw(int opponentRating) {
        matchCount++;
        rating += (int) (32 * (0.5 - (1 / (Math.pow(10, (rating - opponentRating) / 400d)))));
        leaderBoard.updateScore(this.username, this.rating);
    }

    public void win(int opponentRating) {
        matchCount++;
        winCount++;
        rating += (int) (32 * (1 - (1 / (Math.pow(10, (rating - opponentRating) / 400d)))));
        leaderBoard.updateScore(this.username, this.rating);
    }
}
