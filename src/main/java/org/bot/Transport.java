package org.bot;

import java.util.Scanner;

public class Transport
{
    private static final Scanner IN = new Scanner(System.in);

    public String read()
    {
        return IN.nextLine();
    }

    public void write(String output)
    {
        System.out.println(output);
    }
}
