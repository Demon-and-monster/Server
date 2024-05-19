package com.dam.server;

import org.springframework.beans.factory.annotation.Autowired;
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
    UserDetailsService inMemoryUserDetailsService;

    @Autowired
    LeaderBoard leaderBoard;

    @GetMapping("/test")
    public String test() {
        UserDetails playerDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "Hello World, time stamp: " + new Date() + "\nYou are " + playerDetail.getUsername();
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterBody registerBody) {
        if (registerBody.username.contains(",") || registerBody.username.isBlank() || registerBody.password.isBlank()) {
            return "-1";
        }
        try {
            ((InMemoryUserDetailsManager) inMemoryUserDetailsService).getPlayerByUsername(registerBody.username);
        } catch (Exception e) {
            ((InMemoryUserDetailsManager) inMemoryUserDetailsService).addPlayer(registerBody.toPlayer());
            return "0";
        }
        return "-1";
    }

    @PostMapping("/lineup")
    public String lineup() {
        return "";
    }

    @PostMapping("/leftLine")
    public String leftLine() {
        return "";
    }

    @GetMapping("/lineup")
    public String lineup2() {
        return "";
    }

    @PostMapping("/action")
    public String action(@RequestParam String action) {
        return "";
    }

    @PostMapping("/surrender")
    public String surrender() {
        return "";
    }

    @GetMapping("/getGameBoard")
    public String getGameBoard(@RequestParam int gameNumber) {
        return "";
    }

    @GetMapping("/getGameStats")
    public String getGameStats(@RequestParam int gameNumber) {
        return "";
    }

    @GetMapping("/getGamePlayer")
    public String getGamePlayer(@RequestParam int gameNumber) {
        return "";
    }

    @PostMapping("/editEmail")
    public String editEmail(@RequestParam String email) {
        ((InMemoryUserDetailsManager) inMemoryUserDetailsService).getPlayerByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return "";
    }

    @GetMapping("leaderBoard")
    public String leaderBoard() {
        return leaderBoard.getLeaderBoard();
    }

    @GetMapping("/userDetail")
    public String userDetail(@RequestParam String username) {
        Player player = ((InMemoryUserDetailsManager) inMemoryUserDetailsService).getPlayerByUsername(username);
        return player.getUsername() + "," + player.getRating() + "," + player.getEmailSha() + "," + player.getWinCount() + "," + player.getLossCount() + "," + player.getMatchCount() + "," + player.getRegisterDate();
    }

    @GetMapping("/lastTenGames")
    public String lastTenGames(@RequestParam String username) {
        return ((InMemoryUserDetailsManager) inMemoryUserDetailsService).getPlayerByUsername(username).getGamePlayed();
    }

    @GetMapping("/friends")
    public String friends() {
        return ((InMemoryUserDetailsManager)inMemoryUserDetailsService).getPlayerByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).getFriendsString();
    }

    @PostMapping("/addFriend")
    public String addFriend(@RequestParam String username) {
        Player player;
        try{
            player = ((InMemoryUserDetailsManager) inMemoryUserDetailsService).getPlayerByUsername(username);
        }catch (Exception e){
            return "-1";
        }
        player.addFriend(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return "0";
    }

    @GetMapping("/getFriendRequests")
    public String getFriendRequests() {
        return ((InMemoryUserDetailsManager)inMemoryUserDetailsService).getPlayerByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).getRequestFriendsString();
    }

    @GetMapping("/confirmFriendRequest")
    public String confirmFriendRequest(@RequestParam String username) {
        return String.valueOf(((InMemoryUserDetailsManager)inMemoryUserDetailsService).getPlayerByUsername(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).acceptFriend(username));
    }

    @PostMapping("/playWithFriend")
    public String playWithFriend(@RequestParam String username) {
        return "";
    }

    @GetMapping("/playWithFriendWaitList")
    public String playWithFriendWaitList() {
        return "";
    }
}
