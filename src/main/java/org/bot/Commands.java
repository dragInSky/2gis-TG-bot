package org.bot;

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

public enum Commands {
    start("Bot grats u" +
            "\n\thelp - to read about bot"),
    help("(type one of these commands to interact with bot)" +
            "\n\thelp - to read about bot" +
            "\n\tanecdote - to generate anecdote" +
            "\n\tdata - to out current data and time" +
            "\n\trandom - to generate random digit" +
            "\n\tkill - to kill the bot"),
    wrong("This command doesn't exist" +
            "\n\thelp - to read about bot"),
    anecdote("AHAHAH"),
    data(new SimpleDateFormat("dd.MM.yyyy\nHH:mm:ss").format(new java.util.Date())),
    random(ThreadLocalRandom.current().nextInt(0, 10) + "");

    private final String m_data;
    Commands(String data) {
        m_data = data;
    }
    public String getData() {
        return m_data;
    }
}
