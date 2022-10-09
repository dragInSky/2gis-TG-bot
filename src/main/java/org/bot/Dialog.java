package org.bot;

public class Dialog {
    private static final Commands COMMANDS = new Commands();
    public Response dialog(String userInput) {
        return switch (userInput) {
            case "\\help" -> new Response(COMMANDS.help());
            case "\\anecdote" -> new Response(COMMANDS.anecdote());
            case "\\data" -> new Response(COMMANDS.data());
            case "\\random" -> new Response(COMMANDS.random());
            case "\\start" -> new Response(COMMANDS.start());
            case "\\finish" -> new Response(COMMANDS.finish());
            case "\\kill" -> new Response(COMMANDS.kill(), true);
            default -> new Response(COMMANDS.wrong());
        };
    }
}