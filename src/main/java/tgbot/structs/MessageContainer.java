package tgbot.structs;

public class MessageContainer {
    private final String m_chatId, m_data;
    private final boolean m_bFlag;
    public MessageContainer(String chatId, String data) {
        m_chatId = chatId;
        m_data = data;
        m_bFlag = false;
    }
    public MessageContainer(String chatId, String data, boolean bFlag) {
        m_chatId = chatId;
        m_data = data;
        m_bFlag = bFlag;
    }
    public String getChatId() {
        return m_chatId;
    }
    public String getData() {
        return m_data;
    }
    public boolean isFlag() {
        return m_bFlag;
    }
}
