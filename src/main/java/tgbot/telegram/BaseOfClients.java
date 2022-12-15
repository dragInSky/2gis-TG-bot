/*package tgbot.telegram;

import tgbot.Structs.User;

import java.util.HashMap;
import java.util.Map;

public class BaseOfClients {
    //отвечает за корректную запись в Map(базу) клиентов
    private final Map<String, User> base = new HashMap<>();

    //private final HandlerJSON handlerJSON = new HandlerJSON();

    public void registateClient(String userID) {
        if (!base.containsKey(userID)) {
            base.put(userID,
                    new User());//ЕСЛИ СОДЕРЖИТ - ТО НИЧЕ НЕ ДЕЛАЕМ, ТАК КАК ОН СПОКОЙНО ВОЙДЕТ В СИСТЕМУ
        }
    }

    public User signIN(String userID) {
        return base.get(userID); //так как он зарегистрирован на шаге выше
    }

    public void readToLocalBase() {
        String path = "D:\\text.json";
       // handlerJSON.readBase(base, path);
    }

    public void updateToJSONBase() {
        String path = "D:\\text.json";
        //handlerJSON.updateBase(base, path);
    }
}
*/