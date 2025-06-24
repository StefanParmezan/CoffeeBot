package org.StefanParmezan;
import org.StefanParmezan.Services.FileReader;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CoffeeBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "CoffeeBot";
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();

        }
    }

    @Override
    public String getBotToken() {
        return FileReader.getInstance().readFile("src/main/java/resources/bot_token.txt");
    }
}
