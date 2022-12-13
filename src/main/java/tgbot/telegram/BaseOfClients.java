package tgbot.telegram;

import tgbot.Structs.User;

import java.util.HashMap;
import java.util.Map;

public class BaseOfClients {
    //�������� �� ���������� ������ � Map(����) ��������
    private final Map<String, User> base = new HashMap<>();

    private final HandlerJSON handlerJSON = new HandlerJSON();

    public void registateClient(String userID) {
        if (!base.containsKey(userID)) {
            base.put(userID,
                    new User());//���� �������� - �� ���� �� ������, ��� ��� �� �������� ������ � �������
        }
    }

    public User signIN(String userID) {
        return base.get(userID); //��� ��� �� ��������������� �� ���� ����
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
