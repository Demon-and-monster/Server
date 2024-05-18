package com.dam.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class Receiver {
    @Autowired
    SecurityConfig securityConfig;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private UserDetailsService inMemoryUserDetailsService;

    @GetMapping("/test")
    public String test() {
        UserDetails playerDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "Hello World, time stamp: " + new Date() + "\nYou are " + playerDetail.getUsername();
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterBody registerBody) {
        try{
            ((InMemoryUserDetailsManager)inMemoryUserDetailsService).getPlayerByUsername(registerBody.username);
        }catch (Exception e){
            ((InMemoryUserDetailsManager) inMemoryUserDetailsService).addPlayer(registerBody.toPlayer());
            return "User registered successfully";
        }
        return "Username already in use.";
    }

    @PostMapping("/lineup")
    public String lineup(){
        return "";
    }

    @GetMapping("/lineup")
    public String lineup2(){
        return "";
    }

    @PostMapping("/action")
    public String action(@RequestParam String action){
        return "";
    }

    @GetMapping("/getGameBoard")
    public String getGameBoard(@RequestParam int gameNumber){
        return "";
    }

    @GetMapping("/getGameStats")
    public String getGameStats(@RequestParam int gameNumber){
        return "";
    }

    @GetMapping("/getGamePlayer")
    public String getGamePlayer(@RequestParam int gameNumber){
        return "";
    }

    @PostMapping("/editEmail")
    public String editEmail(@RequestParam String email){
        return "";
    }

    @GetMapping("leaderBoard")
    public String leaderBoard(){
        return "";
    }

    @GetMapping("/userDetail")
    public String userDetail(@RequestParam String username){
        Player player = ((InMemoryUserDetailsManager)inMemoryUserDetailsService).getPlayerByUsername(username);
        return player.getUsername() + "," + player.getRating() + "," + player.getEmailSha() + "," + player.getWinCount() + "," + player.getLossCount() + "," + player.getMatchCount() + "," + player.getRegisterDate();
    }

    @GetMapping("/lastTenGames")
    public String lastTenGames(@RequestParam String username){
        return ((InMemoryUserDetailsManager)inMemoryUserDetailsService).getPlayerByUsername(username).getGamePlayed();
    }

}
