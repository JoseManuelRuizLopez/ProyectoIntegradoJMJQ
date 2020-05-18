package com.example.proyectointegradojmjq;

public class Message {
    private String text;
    private boolean belongsToCurrentUser;
    private String fecha;

    public Message(String text, boolean belongsToCurrentUser, String fecha) {
        this.text = text;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.fecha = fecha;
    }

    public String getText() {
        return text;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public String getFecha() {
        return fecha;
    }
}
