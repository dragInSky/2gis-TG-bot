package org.bot;

public class Main {
    static boolean bFlag = true;
    public static void main(String[] args) {
        Initialize.start();
        while (bFlag) {
            Dialog.dialog(LineReader.line());
        }
        System.out.println("the bot needs a little rest, plz");
    }
}