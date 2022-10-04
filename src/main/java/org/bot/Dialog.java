package org.bot;

public class Dialog {
    public static void dialog(String str) {
        switch (str) {
            case "\\help" -> Commands.help();
            case "\\anecdote" -> Commands.anecdote();
            case "\\data" -> Commands.data();
            case "\\random" -> Commands.random();
            case "\\kill" -> Commands.kill();
            default -> Commands.wrong();
        }
    }
}