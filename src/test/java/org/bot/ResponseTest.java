package org.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    void getData() {
        String data = "abc";
        Response test = new Response(data);

        assertEquals(data, test.getData());
    }

    @Test
    void getExitDefault() {
        String data = "abc";
        Response test = new Response(data);

        assertFalse(test.getExit());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void getExit(boolean exit)
    {
        String data = "abc";
        Response test = new Response(data, exit);

        assertEquals(exit, test.getExit());
    }

}