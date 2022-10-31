package org.bot;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        Transport transport = new Transport();

        String data = Commands.start.getData();
        transport.write(data);

        String userInput = null;
        while (!Objects.equals(userInput, "kill")) {
            userInput = transport.read();
            try {
                data = Commands.valueOf(userInput).getData();
                transport.write(data);
            } catch (IllegalArgumentException e) {
                transport.write(Commands.wrong.getData());
            }
        }

        data = Commands.finish.getData();
        transport.write(data);
    }
}