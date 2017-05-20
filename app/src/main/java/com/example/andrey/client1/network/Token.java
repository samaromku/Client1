package com.example.andrey.client1.network;

import java.util.UUID;

public class Token {
    UUID token;

    public Token(){
        this.token = UUID.randomUUID();
    }
}
