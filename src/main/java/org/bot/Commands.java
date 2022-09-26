package org.bot;

import java.text.SimpleDateFormat;
import java.util.Objects;
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
                //"\n\t\\question - to generate question" +
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

    /*public static void question() {
        questionGenerator("rightAnswer", "question");
    }

    public static void questionGenerator(String rightAnswer, String question) {
        while (true) {
            System.out.println(question);
            String userAnswer = LineReader.line();
            if (Objects.equals(userAnswer, rightAnswer)) {
                System.out.println("Congrats, u're right!");
                break;
            } else {
                System.out.println("Nope" +
                        "\ntype yes - to try again" +
                        "\ntype no - to forget question");
                String userRespond = LineReader.line();
                if (!Objects.equals(userRespond, "yes")) {
                    break;
                }
            }
        }
        botWait();
    }*/

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