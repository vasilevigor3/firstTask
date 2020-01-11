package telegramBot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class InlineKeyboardButtonBuilder {
    private final InlineKeyboardButton button;

    public InlineKeyboardButtonBuilder() {
        this.button = new InlineKeyboardButton();

    }

    public InlineKeyboardButtonBuilder setText(String text){
                button.setText(text);
        return this;
    }

    public InlineKeyboardButtonBuilder getText(){
        button.getText();
        return this;
    }

    public InlineKeyboardButtonBuilder setCallbackData(String callbackData){
        button.setCallbackData(callbackData);
        return this;
    }

    public InlineKeyboardButton build(){
        return button;
    }

}