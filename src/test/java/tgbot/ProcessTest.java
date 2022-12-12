package tgbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tgbot.Structs.Coordinates;
import tgbot.Structs.MessageContainer;
import tgbot.processors.Process;

class ProcessTest {
    private final Process process = new Process();

    @Test
    void helpCaseTest() {
        MessageContainer res = process.processing("1", "/help", null, "");
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
        MessageContainer res = process.processing("1", "/start", null, "");
        Assertions.assertEquals("""
                ��� ������������ 2gis ���, � ����:
                - �������� ����� ������� ��� ���� �����;
                - �������� �� ����� ����� �� ������;
                - �������� �� ������ ���������� �� ������������.
                
                /changecity - �������� ����� (������ ������������).
                """,
                res.getData());
    }

    @Test
    void defaultCaseTest() {
        MessageContainer res = process.processing("1", "/ss", null, "");
        Assertions.assertEquals("�������\\��������� �������!", res.getData());
    }

    @Test
    void changecityCaseTest() {
        MessageContainer res1 = process.processing("1", "/changecity", null, "");
        Assertions.assertEquals("������� �����, � ������� �� ����������", res1.getData());

        String text = "������������";
        MessageContainer res2 = process.processing("1", text, null, "");
        Assertions.assertEquals("�� �������� ����� �� " + text + "\n/help - ������ ���� ������", res2.getData());
    }

    @Test
    void mapCaseTest() {
        MessageContainer res1 = process.processing("1", "/map", null, "");
        Assertions.assertEquals("������� �����", res1.getData());

        MessageContainer res2 = process.processing("1", "8 ����� 51", null, "");
        Assertions.assertNotNull(res2.getData());
    }

    @Test
    void infoCaseTest() {
        MessageContainer res1 = process.processing("1", "/info", null, "");
        Assertions.assertEquals("������� �����", res1.getData());

        MessageContainer res2 = process.processing("1", "8 ����� 51", null, "");
        Assertions.assertTrue(res2.getData().startsWith("������ �����������:\n"));
    }

    @Test
    void wrongAddressInfoCaseTest() {
        MessageContainer res1 = process.processing("1", "/info", null, "");
        Assertions.assertEquals("������� �����", res1.getData());

        String text = "�";
        MessageContainer res2 = process.processing("1", text, null, "");
        Assertions.assertEquals("������ ������������ �����: ������������, " + text, res2.getData());
    }

    @Test
    void routeCaseTest() {
        MessageContainer res1 = process.processing("1", "/route", null, "");
        Assertions.assertEquals("������� ������ �����", res1.getData());

        MessageContainer res2 = process.processing("1", "8 ����� 51", null, "");
        Assertions.assertEquals("������� ������ �����", res2.getData());

        MessageContainer res3 = process.processing("1", "��������� 4", null, "");
        Assertions.assertNotNull(res3.getData());
    }

    @Test
    void wrongAddressRouteCaseTest() {
        MessageContainer res1 = process.processing("1", "/route", null, "");
        Assertions.assertEquals("������� ������ �����", res1.getData());

        MessageContainer res2 = process.processing("1", "8 ����� 51", null, "");
        Assertions.assertEquals("������� ������ �����", res2.getData());

        String text = "�";
        MessageContainer res3 = process.processing("1", text, null, "");
        Assertions.assertEquals("������ ������������ �����: ������������, " + text, res3.getData());
    }

    @Test
    void geolocationRouteCaseTest() {
        MessageContainer res1 = process.processing("1", "/route", null, "");
        Assertions.assertEquals("������� ������ �����", res1.getData());

        MessageContainer res2 = process.processing("1", "", new Coordinates(56.823422, 60.605626), "");
        Assertions.assertEquals("������� ������ �����", res2.getData());

        MessageContainer res3 = process.processing("1", "��������� 4", null, "");
        Assertions.assertNotNull(res3.getData());
    }
}