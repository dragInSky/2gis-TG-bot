package org.bot;

public class Response {
    public final String m_data;
    public final boolean m_bFlag;

    Response(String data) {
        m_data = data;
        m_bFlag = false;
    }

    Response(String data, boolean bFlag) {
        m_data = data;
        m_bFlag = bFlag;
    }

    public String getData() {
        return m_data;
    }

    public boolean getFlag() {
        return m_bFlag;
    }
}