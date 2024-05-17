package com.dam.server;

public class RegisterBody {
    public String username;
    public String password;
    public String email;
    public int level;

    public RegisterBody(String username, String password, String email, int level) {
        this.username = username.toLowerCase();
        this.password = password;
        this.email = email;
        this.level = level;
    }

    public Player toPlayer(){
        return new Player(username, password, email, level==0?400:level==1?800:level==2?1200:1600);
    }
}
