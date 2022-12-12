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
    private final ReplyKeyboardRemove replyKeyboardRemove;
    Button() {
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton("Отправить геолокацию");
        keyboardButton.setRequestLocation(true);
        row.add(keyboardButton);
        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
    }
    public void setUpGeolocation(SendMessage message) {
        message.setReplyMarkup(replyKeyboardMarkup);
    }

    public void removeKeyboard(SendMessage message) {
        message.setReplyMarkup(replyKeyboardRemove);
    }
}