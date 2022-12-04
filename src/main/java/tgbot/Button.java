package tgbot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;

public class Button {
    public void setUpGeolocation(SendMessage message, Message msg) {
        String chatId = msg.getChatId().toString();
        message.setChatId(String.valueOf(chatId));
        message.setText("¬ведите первый адрес");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var geoButton = new InlineKeyboardButton();

        geoButton.setText("ќтправить геолокацию");
        geoButton.setCallbackData("GEO_BUTTON");

        rowInLine.add(geoButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
    }
}
