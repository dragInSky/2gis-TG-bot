/*
package org.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DialogTest {
    static Stream<Arguments> structArrayProvider() {
        return Stream.of(
                Arguments.of(new StructTest("\\help", Commands::help)),
                Arguments.of(new StructTest("\\anecdote", Commands::anecdote)),
                Arguments.of(new StructTest("\\data", Commands::data)),
                Arguments.of(new StructTest("\\kill", Commands::kill)),
                Arguments.of(new StructTest("em...What", Commands::wrong))
        );
    }

    @ParameterizedTest
    @MethodSource("structArrayProvider")
    public void testDialogSwitchCases(StructTest struct) {
        String directConsoleOutput = null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(100);
            PrintStream capture = new PrintStream(outputStream);
            System.setOut(capture);

            Dialog.dialog(struct.m_consoleOutput);
            capture.flush();
            struct.m_consoleOutput = outputStream.toString();

            outputStream.reset();
            struct.m_command.write();
            capture.flush();
            directConsoleOutput = outputStream.toString();

            System.setOut(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(struct.m_consoleOutput, directConsoleOutput);
    }

    @Test
    public void testDialogSwitchRandom() {
        int randomDigit = 0;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(100);
            PrintStream capture = new PrintStream(outputStream);
            System.setOut(capture);

            Dialog.dialog("\\random");
            capture.flush();
            randomDigit = Integer.parseInt(outputStream.toString().substring(0, 1));

            System.setOut(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(randomDigit >= 0);
        assertTrue(randomDigit <= 9);
    }
}*/
