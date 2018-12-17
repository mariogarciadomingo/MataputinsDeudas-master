package com.example.mario.mataputinsdeudas;

import co.intentservice.chatui.models.ChatMessage;

public class Mens {


        ChatMessage chatMessage;
        String id;

    public Mens(ChatMessage chatMessage, String id) {
        this.chatMessage = chatMessage;
        this.id = id;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
