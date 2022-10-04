package org.bot;

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

public class Commands {
    private static void botWait() {
        System.out.println("bot is waiting for the command");
    }

    public static void wrong() {
        System.out.println("This command doesn't exist" +
                "\n\t\\help - to read about bot");
        botWait();
    }

    public static void help() {
        System.out.println("-------------------------------------------------" +
                "\nHELP" +
                "\n(type one of these commands to interact with bot)" +
                "\n\t\\help - to read about bot" +
                "\n\t\\anecdote - to generate anecdote" +
                "\n\t\\data - to out current data and time" +
                "\n\t\\random - to generate random digit" +
                "\n\t\\kill - to kill the bot" +
                "\n-------------------------------------------------");
        botWait();
    }

    public static void anecdote() {
        System.out.println("AHAHAH");
        botWait();
    }

    public static void data() {
        System.out.println(new SimpleDateFormat("dd.MM.yyyy\nHH:mm:ss").format(new java.util.Date()));
        botWait();
    }

    public static void random() {
        System.out.println(ThreadLocalRandom.current().nextInt(0, 10));
        botWait();
    }

    public static void kill() {
        System.out.println("...");
        Main.bFlag = false;
    }
}