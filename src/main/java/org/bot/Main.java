package org.bot;

public class Main {
    private static final String START = "\\start", FINISH = "\\finish";
    private static final Dialog DIALOG = new Dialog();

    private static final Transport Data_Reader = new Transport();

    private static final Transport Data_Writer = new Transport();
    private static Response response;

    private static String userInput = "";
    public static void main(String[] args) {
        Data_Writer.write(commandProcessing(START));
        do {
            userInput = Data_Reader.read();
            if (userInput.equals("\\kill")) {response.change_m_exit();}

            Data_Writer.write(commandProcessing(userInput));

        } while (!response.getExit());
        Data_Writer.write(commandProcessing(FINISH));
    }

    private static String commandProcessing(String userInput) {
        response = DIALOG.dialog(userInput);
        return response.getData();
    }
}