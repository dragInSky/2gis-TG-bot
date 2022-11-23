package org.bot;

public class Processing {
    public static HttpRequest http = new HttpRequest();
    public String processMessage(String message, String addr) {
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
            case "/route" -> http.sendPostRoute(addr);
            default ->  "Bot can reply only on commands";
        };
    }

        public String processMessage(String message){
            return switch (message) {
                case "/start" -> """
                        Bot grats you!
                        /help - information about bot features
                        /route - display information about route
                        """;
                case "/help" -> """
                        /help - information about bot features
                        /route - display information about route
                        """;
                case "/route" -> http.sendPostRoute("");
                default -> "Bot can reply only on commands";
            };
        }


    public String coordinates(String addr) {
        HttpRequest request = new HttpRequest();
        return request.sendGetGeo(addr);
    }




}

