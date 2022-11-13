package org.bot;

public class Proccessing {
    public String proccessMessage(String message) {
        message = message.charAt(0) != '/' ? message : message.substring(1);
        String data;
        try {
            data = Commands.valueOf(message).getData();
        } catch (IllegalArgumentException e) {
            data = Commands.wrong.getData();
        }
        return data;
    }
}
