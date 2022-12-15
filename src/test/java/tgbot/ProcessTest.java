package tgbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tgbot.processors.HttpRequest;
import tgbot.processors.Parser;
import tgbot.structs.Coordinates;
import tgbot.structs.MessageContainer;
import tgbot.processors.Process;

import java.util.HashMap;

class ProcessTest {
    private final Process process = new Process(new Parser(), new HttpRequest());

    @Test
    void helpCaseTest() {
        MessageContainer res = process.processing("1", "/help",
                null, "", new HashMap<>());
        Assertions.assertEquals("""
                ������ ���� ������:
                /changecity - �������� �����
                /map - ������� �� ����� ����� �� ������
                /info - ������� �� ������ ���������� �� ������������
                /route - ����� ����� ������� ��� ���� �����
                """,
                res.getData());
    }

    @Test
    void startCaseTest() {
        MessageContainer res = process.processing("1", "/start",
                null, "", new HashMap<>());
        Assertions.assertTrue(res.getData().startsWith(
                """
                ��� ������������ 2gis ���, � ����:
                - �������� ����� ������� ��� ���� �����;
                - �������� �� ����� ����� �� ������;
                - �������� �� ������ ���������� �� ������������.
                """));
    }

    @Test
    void defaultCaseTest() {
        MessageContainer res = process.processing("1", "/ss",
                null, "", new HashMap<>());
        Assertions.assertEquals("�������\\��������� �������!", res.getData());
    }

    @Test
    void changecityCaseTest() {
        MessageContainer res1 = process.processing("1", "/changecity",
                null, "", new HashMap<>());
        Assertions.assertTrue(res1.getData().startsWith("������� �����, � ������� �� ���������� (������ "));

        String text = "������������";
        MessageContainer res2 = process.processing("1", text, null, "", new HashMap<>());
        Assertions.assertEquals("�� �������� ����� �� " + text + "\n/help - ������ ���� ������", res2.getData());
    }

    @Test
    void mapCaseTest() {
        MessageContainer res1 = process.processing("1", "/map",
                null, "", new HashMap<>());
        Assertions.assertEquals("������� �����", res1.getData());

        MessageContainer res2 = process.processing("1", "8 ����� 51",
                null, "", new HashMap<>());
        Assertions.assertNotNull(res2.getData());
    }

    @Test
    void infoCaseTest() {
        MessageContainer res1 = process.processing("1", "/info",
                null, "", new HashMap<>());
        Assertions.assertEquals("������� �����", res1.getData());

        MessageContainer res2 = process.processing("1", "8 ����� 51",
                null, "", new HashMap<>());
        Assertions.assertTrue(res2.getData().startsWith("������ �����������:\n"));
    }

    @Test
    void wrongAddressInfoCaseTest() {
        MessageContainer res1 = process.processing("1", "/info",
                null, "", new HashMap<>());
        Assertions.assertEquals("������� �����", res1.getData());

        String text = "�";
        MessageContainer res2 = process.processing("1", text,
                null, "", new HashMap<>());
        Assertions.assertEquals("������ ������������ �����: ������������, " + text, res2.getData());
    }

    @Test
    void routeCaseTest() {
        MessageContainer res1 = process.processing("1", "/route",
                null, "", new HashMap<>());
        Assertions.assertEquals("������� ������ �����", res1.getData());

        MessageContainer res2 = process.processing("1", "8 ����� 51",
                null, "", new HashMap<>());
        Assertions.assertEquals("������� ������ �����", res2.getData());

        MessageContainer res3 = process.processing("1", "��������� 4",
                null, "", new HashMap<>());
        Assertions.assertNotNull(res3.getData());
    }

    @Test
    void wrongAddressRouteCaseTest() {
        MessageContainer res1 = process.processing("1", "/route",
                null, "", new HashMap<>());
        Assertions.assertEquals("������� ������ �����", res1.getData());

        MessageContainer res2 = process.processing("1", "8 ����� 51",
                null, "", new HashMap<>());
        Assertions.assertEquals("������� ������ �����", res2.getData());

        String text = "�";
        MessageContainer res3 = process.processing("1", text, null, "", new HashMap<>());
        Assertions.assertEquals("������ ������������ �����: ������������, " + text, res3.getData());
    }

    @Test
    void geolocationRouteCaseTest() {
        MessageContainer res1 = process.processing("1", "/route",
                null, "", new HashMap<>());
        Assertions.assertEquals("������� ������ �����", res1.getData());

        MessageContainer res2 = process.processing("1", "",
                new Coordinates(56.823422, 60.605626), "", new HashMap<>());
        Assertions.assertEquals("������� ������ �����", res2.getData());

        MessageContainer res3 = process.processing("1", "��������� 4",
                null, "", new HashMap<>());
        Assertions.assertNotNull(res3.getData());
    }
}