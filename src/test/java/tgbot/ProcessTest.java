package tgbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProcessTest {
    private final Process process = new Process();

    @Test
    void helpCaseTest() {
        Struct res = process.processing("1", "/help", null, "");
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
        Struct res = process.processing("1", "/start", null, "");
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
        Struct res = process.processing("1", "/ss", null, "");
        Assertions.assertEquals("�������\\��������� �������!", res.getData());
    }

    @Test
    void changecityCaseTest() {
        Struct res1 = process.processing("1", "/changecity", null, "");
        Assertions.assertEquals("������� �����, � ������� �� ����������", res1.getData());

        String text = "������������";
        Struct res2 = process.processing("1", text, null, "");
        Assertions.assertEquals("�� �������� ����� �� " + text + "\n/help - ������ ���� ������", res2.getData());
    }

    @Test
    void mapCaseTest() {
        Struct res1 = process.processing("1", "/map", null, "");
        Assertions.assertEquals("������� �����", res1.getData());

        Struct res2 = process.processing("1", "8 ����� 51", null, "");
        Assertions.assertNotNull(res2.getData());
    }

    @Test
    void infoCaseTest() {
        Struct res1 = process.processing("1", "/info", null, "");
        Assertions.assertEquals("������� �����", res1.getData());

        Struct res2 = process.processing("1", "8 ����� 51", null, "");
        Assertions.assertTrue(res2.getData().startsWith("������ �����������:\n"));
    }

    @Test
    void wrongAddressInfoCaseTest() {
        Struct res1 = process.processing("1", "/info", null, "");
        Assertions.assertEquals("������� �����", res1.getData());

        String text = "�";
        Struct res2 = process.processing("1", text, null, "");
        Assertions.assertEquals("������ ������������ �����: ������������, " + text, res2.getData());
    }

    @Test
    void routeCaseTest() {
        Struct res1 = process.processing("1", "/route", null, "");
        Assertions.assertEquals("������� ������ �����", res1.getData());

        Struct res2 = process.processing("1", "8 ����� 51", null, "");
        Assertions.assertEquals("������� ������ �����", res2.getData());

        Struct res3 = process.processing("1", "��������� 4", null, "");
        Assertions.assertNotNull(res3.getData());
    }

    @Test
    void wrongAddressRouteCaseTest() {
        Struct res1 = process.processing("1", "/route", null, "");
        Assertions.assertEquals("������� ������ �����", res1.getData());

        Struct res2 = process.processing("1", "8 ����� 51", null, "");
        Assertions.assertEquals("������� ������ �����", res2.getData());

        String text = "�";
        Struct res3 = process.processing("1", text, null, "");
        Assertions.assertEquals("������ ������������ �����: ������������, " + text, res3.getData());
    }

    @Test
    void geolocationRouteCaseTest() {
        Struct res1 = process.processing("1", "/route", null, "");
        Assertions.assertEquals("������� ������ �����", res1.getData());

        Struct res2 = process.processing("1", "", new Coordinates(56.823422, 60.605626), "");
        Assertions.assertEquals("������� ������ �����", res2.getData());

        Struct res3 = process.processing("1", "��������� 4", null, "");
        Assertions.assertNotNull(res3.getData());
    }
}