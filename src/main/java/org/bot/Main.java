package org.bot;

public class Main {
    private static final String START = "\\start", END = "\\end";
    private static final Dialog DIALOG = new Dialog();
    private static final DataWriter DATA_WRITER = new DataWriter();
    private static Struct data;
    public static void main(String[] args) {
        commandProcessing(START);
        do {
            commandProcessing(DATA_WRITER.write());
        } while (!data.getExit());
        commandProcessing(END);
    }

    private static void commandProcessing(String userInput) {
        data = DIALOG.dialog(userInput);
        //передаю строку m_struct.getData() Паше
    }
}