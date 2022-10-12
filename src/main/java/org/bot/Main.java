package org.bot;

public class Main {
    private static final String START = "\\start", FINISH = "\\finish";
    private static final Dialog DIALOG = new Dialog();
    private static final Transport TRANSPORT = new Transport();

    public static void main(String[] args) {
        Response startResponse = commandProcessing(START);
        TRANSPORT.write(startResponse.getData());

        while (true) {
            String userInput = TRANSPORT.read();
            Response userResponse = commandProcessing(userInput);
            TRANSPORT.write(userResponse.getData());

            if (userResponse.getExit()) {
                break;
            }
        }

        Response endResponse = commandProcessing(FINISH);
        TRANSPORT.write(endResponse.getData());
    }

    private static Response commandProcessing(String userInput) {
        return DIALOG.dialog(userInput);
    }
}