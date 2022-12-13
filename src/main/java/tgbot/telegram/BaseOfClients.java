package tgbot.telegram;

import java.util.HashMap;
import java.util.Map;

public class BaseOfClients {
    //отвечает за корректную запись в Map(базу) клиентов
    private final Map<String, Client> base = new HashMap<>();

    private final HandlerJSON handlerJSON = new HandlerJSON();

    public void registateClient(String userID) {
        if (!base.containsKey(userID)) {
            base.put(userID,
                    new Client());//ЕСЛИ СОДЕРЖИТ - ТО НИЧЕ НЕ ДЕЛАЕМ, ТАК КАК ОН СПОКОЙНО ВОЙДЕТ В СИСТЕМУ
        }
    }

    public Client signIN(String userID) {
        return base.get(userID); //так как он зарегистрирован на шаге выше
    }

    public void readToLocalBase() {
        String path = "D:\\JAVA\\UNIVERSITY\\Bot_consol\\ConsolniyBot\\text.json";
        handlerJSON.readBase(base, path);
    }

    public void updateToJSONBase() {
        String path = "D:\\JAVA\\UNIVERSITY\\Bot_consol\\ConsolniyBot\\text.json";
        handlerJSON.updateBase(base, path);
    }
}
}
