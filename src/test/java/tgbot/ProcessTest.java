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
        MessageContainer res = process.processing("1", "/start",
                null, "", new HashMap<>());
        Assertions.assertTrue(res.getData().startsWith(
                """
                ¬ас приветствует 2gis бот, € могу:
                - находить место встречи дл€ двух людей;
                - выводить на карте место по адресу;
                - выводить по адресу информацию об организаци€х.
                """));
    }

    @Test
    void defaultCaseTest() {
        MessageContainer res = process.processing("1", "/ss",
                null, "", new HashMap<>());
        Assertions.assertEquals("¬ведите\\отправьте команду!", res.getData());
    }

    @Test
    void changecityCaseTest() {
        MessageContainer res1 = process.processing("1", "/changecity",
                null, "", new HashMap<>());
        Assertions.assertTrue(res1.getData().startsWith("¬ведите город, в котором вы находитесь (сейчас "));

        String text = "≈катеринбург";
        MessageContainer res2 = process.processing("1", text, null, "", new HashMap<>());
        Assertions.assertEquals("¬ы изменили город на " + text + "\n/help - список моих команд", res2.getData());
    }

    @Test
    void mapCaseTest() {
        MessageContainer res1 = process.processing("1", "/map",
                null, "", new HashMap<>());
        Assertions.assertEquals("¬ведите адрес", res1.getData());

        MessageContainer res2 = process.processing("1", "8 марта 51",
                null, "", new HashMap<>());
        Assertions.assertNotNull(res2.getData());
    }

    @Test
    void infoCaseTest() {
        MessageContainer res1 = process.processing("1", "/info",
                null, "", new HashMap<>());
        Assertions.assertEquals("¬ведите адрес", res1.getData());

        MessageContainer res2 = process.processing("1", "8 марта 51",
                null, "", new HashMap<>());
        Assertions.assertTrue(res2.getData().startsWith("—писок организаций:\n"));
    }

    @Test
    void wrongAddressInfoCaseTest() {
        MessageContainer res1 = process.processing("1", "/info",
                null, "", new HashMap<>());
        Assertions.assertEquals("¬ведите адрес", res1.getData());

        String text = "ы";
        MessageContainer res2 = process.processing("1", text,
                null, "", new HashMap<>());
        Assertions.assertEquals("¬веден некорректный адрес: ≈катеринбург, " + text, res2.getData());
    }

    @Test
    void routeCaseTest() {
        MessageContainer res1 = process.processing("1", "/route",
                null, "", new HashMap<>());
        Assertions.assertEquals("¬ведите первый адрес", res1.getData());

        MessageContainer res2 = process.processing("1", "8 марта 51",
                null, "", new HashMap<>());
        Assertions.assertEquals("¬ведите второй адрес", res2.getData());

        MessageContainer res3 = process.processing("1", "“ургенева 4",
                null, "", new HashMap<>());
        Assertions.assertNotNull(res3.getData());
    }

    @Test
    void wrongAddressRouteCaseTest() {
        MessageContainer res1 = process.processing("1", "/route",
                null, "", new HashMap<>());
        Assertions.assertEquals("¬ведите первый адрес", res1.getData());

        MessageContainer res2 = process.processing("1", "8 марта 51",
                null, "", new HashMap<>());
        Assertions.assertEquals("¬ведите второй адрес", res2.getData());

        String text = "ы";
        MessageContainer res3 = process.processing("1", text, null, "", new HashMap<>());
        Assertions.assertEquals("¬веден некорректный адрес: ≈катеринбург, " + text, res3.getData());
    }

    @Test
    void geolocationRouteCaseTest() {
        MessageContainer res1 = process.processing("1", "/route",
                null, "", new HashMap<>());
        Assertions.assertEquals("¬ведите первый адрес", res1.getData());

        MessageContainer res2 = process.processing("1", "",
                new Coordinates(56.823422, 60.605626), "", new HashMap<>());
        Assertions.assertEquals("¬ведите второй адрес", res2.getData());

        MessageContainer res3 = process.processing("1", "“ургенева 4",
                null, "", new HashMap<>());
        Assertions.assertNotNull(res3.getData());
    }
}