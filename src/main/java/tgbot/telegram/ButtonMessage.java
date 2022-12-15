package tgbot.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


public class ButtonMessage {

    public static final String CAFE_BUTTON = "CAFE_BUTTON";

    public static final String PARK_BUTTON = "PARK_BUTTON";

    public static final String BAR_BUTTON = "BAR_BUTTON";

    public static final String PEDESTRIAN_BUTTON = "PEDESTRIAN_BUTTON";//Пешеходный маршрут

    public static final String JAM_BUTTON = "JAM_BUTTON";//На машине

    public static final String BICYCLE_BUTTON = "BICYCLE_BUTTON";
    private final InlineKeyboardMarkup markupInLine;
    private final List<List<InlineKeyboardButton>> rowsInLine;
    private final List<InlineKeyboardButton> rowInLine;

    ButtonMessage(){

        markupInLine = new InlineKeyboardMarkup();
        rowsInLine = new ArrayList<>();
        rowInLine = new ArrayList<>();

    }

    public void Place(SendMessage message) {
        var cafeButton = new InlineKeyboardButton();

        cafeButton.setText("Кафе");
        cafeButton.setCallbackData(CAFE_BUTTON);

        var parkButton = new InlineKeyboardButton();

        parkButton.setText("Парк");
        parkButton.setCallbackData(PARK_BUTTON);

        var barButton = new InlineKeyboardButton();

        barButton.setText("Бар");
        barButton.setCallbackData(BAR_BUTTON);


        rowInLine.add(cafeButton);
        rowInLine.add(parkButton);
        rowInLine.add(barButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setText("Выберете место");
        message.setReplyMarkup(markupInLine);
    }

    public void routeType(SendMessage message) {
        var pedestrianButton = new InlineKeyboardButton();

        pedestrianButton.setText("Пешком");
        pedestrianButton.setCallbackData(PEDESTRIAN_BUTTON);

        var jamButton = new InlineKeyboardButton();

        jamButton.setText("На машине");
        jamButton.setCallbackData(JAM_BUTTON);

        var bicycleButton = new InlineKeyboardButton();

        bicycleButton.setText("На велике");
        bicycleButton.setCallbackData(BICYCLE_BUTTON);

        rowInLine.add(pedestrianButton);
        rowInLine.add(jamButton);
        rowInLine.add(bicycleButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setText("Выберите тип маршрута");
        message.setReplyMarkup(markupInLine);
    }

    public void resetRows(){
        rowInLine.clear();
        rowsInLine.clear();
    }

}
