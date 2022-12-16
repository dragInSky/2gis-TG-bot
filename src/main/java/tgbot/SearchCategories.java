package tgbot;

public enum SearchCategories {
    CAFE("кафе"), RESTAURANT("ресторан"), BAR("бар"), CINEMA("кинотеатры"), CULTURE("культура"), PARK("парк отдыха");

    private final String m_search;

    SearchCategories(String search) {
        m_search = search;
    }

    public String getSearch() {
        return m_search;
    }
}
