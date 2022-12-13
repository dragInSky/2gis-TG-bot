package tgbot.telegram;

import java.util.HashMap;
import java.util.Map;

public class BaseOfClients {
    //�������� �� ���������� ������ � Map(����) ��������
    private final Map<String, Client> base = new HashMap<>();

    private final HandlerJSON handlerJSON = new HandlerJSON();

    public void registateClient(String userID) {
        if (!base.containsKey(userID)) {
            base.put(userID,
                    new Client());//���� �������� - �� ���� �� ������, ��� ��� �� �������� ������ � �������
        }
    }

    public Client signIN(String userID) {
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
