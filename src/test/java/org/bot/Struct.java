package org.bot;

public class Struct {
    public String m_consoleOutput;
    public Command m_command;

    Struct(String consoleOutput, Command command) {
        m_consoleOutput = consoleOutput;
        m_command = command;
    }
}
