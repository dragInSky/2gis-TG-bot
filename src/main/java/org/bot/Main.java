package org.bot;

public class Main {
    private static final String START = "\\start", FINISH = "\\finish";
    private static final CommandProcessor COMMAND_PROCESSOR = new CommandProcessor();
    private static final Transport TRANSPORT = new Transport();

    public static void main(String[] args) {
        Response startResponse = COMMAND_PROCESSOR.commandProcessing(START);
        TRANSPORT.write(startResponse.getData());

        while (true) {
            String userInput = TRANSPORT.read();
            Response userResponse = COMMAND_PROCESSOR.commandProcessing(userInput);
            TRANSPORT.write(userResponse.getData());

            if (userResponse.getExit()) {
                break;
            }
        }

        Response endResponse = COMMAND_PROCESSOR.commandProcessing(FINISH);
        TRANSPORT.write(endResponse.getData());
    }
}