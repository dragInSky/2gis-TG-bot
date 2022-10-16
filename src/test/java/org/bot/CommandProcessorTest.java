package org.bot;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class CommandProcessorTest {
    interface Command {
        String getData();
    }
    static class StructTest {
        public String m_userInput;
        public Command m_command;
        StructTest(String userInput, Command command) {
            m_userInput = userInput;
            m_command = command;
        }
    }

    static final Commands COMMANDS = new Commands();
    static final CommandProcessor COMMAND_PROCESSOR = new CommandProcessor();
    static Stream<Arguments> structArrayProvider() {
        return Stream.of(
                Arguments.of(new StructTest("\\help", COMMANDS::help)),
                Arguments.of(new StructTest("\\anecdote", COMMANDS::anecdote)),
                Arguments.of(new StructTest("\\data", COMMANDS::data)),
                Arguments.of(new StructTest("\\start", COMMANDS::start)),
                Arguments.of(new StructTest("\\finish", COMMANDS::finish)),
                Arguments.of(new StructTest("em...What", COMMANDS::wrong))
        );
    }

    @ParameterizedTest
    @MethodSource("structArrayProvider")
    void commandProcessing(StructTest struct) {
        Response testWaiting = new Response(struct.m_command.getData());
        Response testResult = COMMAND_PROCESSOR.commandProcessing(struct.m_userInput);
        assertEquals(testWaiting.getData(), testResult.getData());
        assertEquals(testWaiting.getExit(), testResult.getExit());
    }

    @Test
    void testKill() {
        Response testWaiting = new Response(COMMANDS.kill(), true);
        Response testResult = COMMAND_PROCESSOR.commandProcessing("\\kill");
        assertEquals(testWaiting.getData(), testResult.getData());
        assertEquals(testWaiting.getExit(), testResult.getExit());
    }

    @RepeatedTest(10)
    public void testDialogSwitchRandom() {
        Response testWaiting = new Response(COMMANDS.random());
        int number = testWaiting.getData().charAt(0)-'0';
        Response testResult = COMMAND_PROCESSOR.commandProcessing("\\random");
        assertTrue(number >= 0);
        assertTrue(number <= 9);
        assertEquals(testWaiting.getExit(), testResult.getExit());
    }
}