package org.bot;

public class Main {
    private static final String START = "\\start", FINISH = "\\end";
    private static final Dialog DIALOG = new Dialog();
    private static final DataReader DATA_READER = new DataReader();
    private static Response response;
    public static void main(String[] args) {
        commandProcessing(START);
        do {
            commandProcessing(DATA_READER.read());
        } while (!response.getExit());
        commandProcessing(FINISH);
    }

    private static void commandProcessing(String userInput) {
        response = DIALOG.dialog(userInput);
        //передаю строку response.getData() Паше
    }
}