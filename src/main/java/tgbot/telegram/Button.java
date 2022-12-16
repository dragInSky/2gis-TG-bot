package tgbot.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;

public class Button {
    private final ReplyKeyboardMarkup replyKeyboardMarkup;
    private ReplyKeyboardRemove replyKeyboardRemove;
    Button() {
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

    }

    public void setUpGeolocation(SendMessage message) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton("Отправить геолокацию");
        keyboardButton.setRequestLocation(true);
        row.add(keyboardButton);
        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        message.setReplyMarkup(replyKeyboardMarkup);
    }

    public void route(SendMessage message) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton pedestrianButton = new KeyboardButton("Пешком");
        KeyboardButton jamButton = new KeyboardButton("На машине");
        KeyboardButton bicycleButton = new KeyboardButton("Велосипед");

        row.add(pedestrianButton);
        row.add(jamButton);
        row.add(bicycleButton);

        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        message.setReplyMarkup(replyKeyboardMarkup);
    }

    public void place(SendMessage message) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton cafeButton = new KeyboardButton("Кафе");
        KeyboardButton parkButton = new KeyboardButton("Парк");
        KeyboardButton barButton = new KeyboardButton("Бар");

        row.add(cafeButton);
        row.add(parkButton);
        row.add(barButton);

        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        message.setReplyMarkup(replyKeyboardMarkup);
    }

    public void removeKeyboard(SendMessage message) {
        message.setReplyMarkup(replyKeyboardRemove);
    }
}