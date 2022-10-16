package org.bot;

import java.util.Scanner;

public class Transport {
    private final Scanner IN = new Scanner(System.in);

    public String read() {
        System.out.print("-> ");
        return IN.nextLine();
    }

    public void write(String output) {
        System.out.println(output);
    }
}
