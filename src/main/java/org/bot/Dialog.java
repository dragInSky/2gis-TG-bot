package org.bot;

public class Dialog {
    private static final Commands COMMANDS = new Commands();
    public Struct dialog(String userInput) {
        return switch (userInput) {
            case "\\help" -> new Struct(COMMANDS.help());
            case "\\anecdote" -> new Struct(COMMANDS.anecdote());
            case "\\data" -> new Struct(COMMANDS.data());
            case "\\random" -> new Struct(COMMANDS.random());
            case "\\start" -> new Struct(COMMANDS.start());
            case "\\end" -> new Struct(COMMANDS.end());
            case "\\kill" -> new Struct(COMMANDS.kill(), true);
            default -> new Struct(COMMANDS.wrong());
        };
    }
}