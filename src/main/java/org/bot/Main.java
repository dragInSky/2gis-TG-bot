package org.bot;

public class Main {
    private static final String START = "\\start", FINISH = "\\finish";
    private static final Dialog DIALOG = new Dialog();

    private static final Transport TRANSPORT = new Transport();
    private static Response response;

    public static void main(String[] args) {
        String userInput;
        TRANSPORT.write(commandProcessing(START));
        do {
            userInput = TRANSPORT.read();

            TRANSPORT.write(commandProcessing(userInput));

        } while (!response.getExit());
        TRANSPORT.write(commandProcessing(FINISH));
    }

    private static String commandProcessing(String userInput) {
        response = DIALOG.dialog(userInput);
        return response.getData();
    }
}