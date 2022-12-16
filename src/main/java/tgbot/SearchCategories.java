package tgbot;

public enum SearchCategories {
    CAFE("����"), RESTAURANT("��������"), BAR("���"), CINEMA("����������"), CULTURE("��������"), PARK("���� ������");

    private final String m_search;

    SearchCategories(String search) {
        m_search = search;
    }

    public String getSearch() {
        return m_search;
    }
}
