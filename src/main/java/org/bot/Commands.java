package org.bot;

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

public class Commands {
    private static String botWait() {
        return "bot is waiting for the command";
    }

    public String start() {
        return "Bot grats u" +
                "\n\t\\help - to read about bot" +
                "\n" + botWait();
    }

    public String finish() {
        return "See u";
    }

    public String wrong() {
        return "This command doesn't exist" +
                "\n\t\\help - to read about bot" +
                "\n" + botWait();
    }

    public String help() {
        return "-------------------------------------------------" +
                "\nHELP" +
                "\n(type one of these commands to interact with bot)" +
                "\n\t\\help - to read about bot" +
                "\n\t\\anecdote - to generate anecdote" +
                "\n\t\\data - to out current data and time" +
                "\n\t\\random - to generate random digit" +
                "\n\t\\kill - to kill the bot" +
                "\n-------------------------------------------------" +
                "\n" + botWait();
    }

    public String anecdote() {
        return "AHAHAH" +
                "\n" + botWait();
    }

    public String data() {
        return new SimpleDateFormat("dd.MM.yyyy\nHH:mm:ss").format(new java.util.Date()) +
                "\n" + botWait();
    }

    public String random() {
        return ThreadLocalRandom.current().nextInt(0, 10) +
                "\n" + botWait();
    }

    public String kill() {
        return "...";
    }
}