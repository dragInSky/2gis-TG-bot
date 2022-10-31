package org.bot;

import java.util.Scanner;

public class Transport {
    private final Scanner in = new Scanner(System.in);

    public String read() {
        return in.nextLine();
    }

    public void write(String output) {
        System.out.println(output);
    }
}
