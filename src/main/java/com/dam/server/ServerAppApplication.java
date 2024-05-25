package com.dam.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerAppApplication {

    @Autowired
    public GameManager gameManager;

    public ServerAppApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerAppApplication.class, args);
        new ServerAppApplication().start();
    }

    public void start() {
        while (true) {
            try {
                wait(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            gameManager.line();
        }
    }

}
