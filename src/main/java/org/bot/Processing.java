package org.bot;

public class Processing {
    public String processMessage(String message) {
        return switch (message) {
            case "/start" ->  """
                    Bot grats you!
                    /help - information about bot features
                    /route - display information about route
                    """;
            case "/help" ->  """
                    /help - information about bot features
                    /route - display information about route
                    """;
            case "/route" -> new HttpRequest().sendPostRoute();
            default ->  "Bot can reply only on commands";
        };
    }

    public String coordinates(String addr) {
        HttpRequest request = new HttpRequest();
        return request.sendGetGeo(addr);
    }
}