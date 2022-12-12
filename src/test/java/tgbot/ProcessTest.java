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
                —писок моих команд:
                /changecity - помен€ть город
                /map - вывести на карте место по адресу
                /info - вывести по адресу информацию об организаци€х
                /route - найти место встречи дл€ двух людей
                """,
                res.getData());
    }

    @Test
    void startCaseTest() {
        MessageContainer res = process.processing("1", "/start", null, "");
        Assertions.assertEquals("""
                ¬ас приветствует 2gis бот, € могу:
                - находить место встречи дл€ двух людей;
                - выводить на карте место по адресу;
                - выводить по адресу информацию об организаци€х.
                
                /changecity - помен€ть город (сейчас ≈катеринбург).
                """,
                res.getData());
    }

    @Test
    void defaultCaseTest() {
        MessageContainer res = process.processing("1", "/ss", null, "");
        Assertions.assertEquals("¬ведите\\отправьте команду!", res.getData());
    }

    @Test
    void changecityCaseTest() {
        MessageContainer res1 = process.processing("1", "/changecity", null, "");
        Assertions.assertEquals("¬ведите город, в котором вы находитесь", res1.getData());

        String text = "≈катеринбург";
        MessageContainer res2 = process.processing("1", text, null, "");
        Assertions.assertEquals("¬ы изменили город на " + text + "\n/help - список моих команд", res2.getData());
    }

    @Test
    void mapCaseTest() {
        MessageContainer res1 = process.processing("1", "/map", null, "");
        Assertions.assertEquals("¬ведите адрес", res1.getData());

        MessageContainer res2 = process.processing("1", "8 марта 51", null, "");
        Assertions.assertNotNull(res2.getData());
    }

    @Test
    void infoCaseTest() {
        MessageContainer res1 = process.processing("1", "/info", null, "");
        Assertions.assertEquals("¬ведите адрес", res1.getData());

        MessageContainer res2 = process.processing("1", "8 марта 51", null, "");
        Assertions.assertTrue(res2.getData().startsWith("—писок организаций:\n"));
    }

    @Test
    void wrongAddressInfoCaseTest() {
        MessageContainer res1 = process.processing("1", "/info", null, "");
        Assertions.assertEquals("¬ведите адрес", res1.getData());

        String text = "ы";
        MessageContainer res2 = process.processing("1", text, null, "");
        Assertions.assertEquals("¬веден некорректный адрес: ≈катеринбург, " + text, res2.getData());
    }

    @Test
    void routeCaseTest() {
        MessageContainer res1 = process.processing("1", "/route", null, "");
        Assertions.assertEquals("¬ведите первый адрес", res1.getData());

        MessageContainer res2 = process.processing("1", "8 марта 51", null, "");
        Assertions.assertEquals("¬ведите второй адрес", res2.getData());

        MessageContainer res3 = process.processing("1", "“ургенева 4", null, "");
        Assertions.assertNotNull(res3.getData());
    }

    @Test
    void wrongAddressRouteCaseTest() {
        MessageContainer res1 = process.processing("1", "/route", null, "");
        Assertions.assertEquals("¬ведите первый адрес", res1.getData());

        MessageContainer res2 = process.processing("1", "8 марта 51", null, "");
        Assertions.assertEquals("¬ведите второй адрес", res2.getData());

        String text = "ы";
        MessageContainer res3 = process.processing("1", text, null, "");
        Assertions.assertEquals("¬веден некорректный адрес: ≈катеринбург, " + text, res3.getData());
    }

    @Test
    void geolocationRouteCaseTest() {
        MessageContainer res1 = process.processing("1", "/route", null, "");
        Assertions.assertEquals("¬ведите первый адрес", res1.getData());

        MessageContainer res2 = process.processing("1", "", new Coordinates(56.823422, 60.605626), "");
        Assertions.assertEquals("¬ведите второй адрес", res2.getData());

        MessageContainer res3 = process.processing("1", "“ургенева 4", null, "");
        Assertions.assertNotNull(res3.getData());
    }
}