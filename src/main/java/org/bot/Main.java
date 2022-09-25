package org.bot;

public class Main {
    public static void main(String[] args) {
        Commands.help();
        while (true) {
            Dialog.dialog(LineReader.line());
        }
    }
}