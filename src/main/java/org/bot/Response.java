package org.bot;

public class Response {
    private final String m_data;
    private boolean m_exit;

    Response(String data) {
        m_data = data;
        m_exit = false;
    }

    Response(String data, boolean exit) {
        m_data = data;
        m_exit = exit;
    }

    public String getData() {
        return m_data;
    }

    public boolean getExit() {
        return m_exit;
    }

}
