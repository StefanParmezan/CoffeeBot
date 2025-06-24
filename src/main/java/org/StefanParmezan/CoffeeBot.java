package org.StefanParmezan;
import org.StefanParmezan.Models.Drinks;
import org.StefanParmezan.Services.FileReadService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CoffeeBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            switch(message) {
                case "/start" -> {
                    sendMessage(chatId, "Привет это бот для заказа кофе, кофейни CheeseCoffee \uD83E\uDDC0\n" +
                            "/menu - меню напитков\n" +
                            "/buy - купить напиток\n" +
                            "Желаем приятного аппетита!\n");

                }
                case "/menu" -> {
                    sendMessage(chatId, Drinks.getDrinksToString());
                }
                case "/buy" -> {
                    sendMessage(chatId, "Напиши txt файл в таком формате\nLatte=3\nMocha=1+Cheese\nИ отправляй его сюда, я напишу тебе итоговую цену");

                }



            }
        }
    }

    protected void sendMessage(Long chatId, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(text);

        try{
            execute(sendMessage);
        } catch (Exception e){
            System.out.println("Line 32 CoffeeBot.java" + e + " " + text + " " + chatId);
        }
    }


    @Override
    public String getBotUsername() {
        return "CoffeeBot";
    }

    @Override
    public String getBotToken() {
        return FileReadService.getInstance().readFile("C:\\Users\\StefanParmezan\\Desktop\\Home\\Programming\\CoffeeBot\\src\\main\\resources\\bot_token");
    }
}
