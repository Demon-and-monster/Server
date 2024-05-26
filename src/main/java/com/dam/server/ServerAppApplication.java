package com.dam.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerAppApplication extends Thread {

    public static void main(String[] args) {
        SpringApplication.run(ServerAppApplication.class, args);
    }

}
