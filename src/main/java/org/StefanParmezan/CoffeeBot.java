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
                    sendMessage(chatId, """
                            Привет это бот для заказа кофе, кофейни CheeseCoffee \uD83E\uDDC0
                            /menu - меню напитков
                            /buy - купить напиток
                            Желаем приятного аппетита!
                            """);

                }
                case "/menu" -> {
                    sendMessage(chatId, Drinks.getDrinksToString());
                }
                /*case "/buy" -> {
                    sendMessage(chatId, "Вы хотите взять кофе здесь или с собой?");
                    if(userInput.equalsIgnoreCase("с собой")){
                        sendMessage(chatId, """
                                Напиши txt файл в таком формате
                                Latte=3
                                Mocha=1+Cheese
                                И отправляй его сюда, я напишу тебе итоговую цену в виде сообщения""");
                    }
                    else if(message.contains("с собой")){
                        sendMessage(chatId, """
                                Напиши txt файл в таком формате
                                Latte=3
                                Mocha=1+Cheese
                                И отправляй его сюда, я напишу тебе итоговую цену в виде .txt файла""");
                    }
                    else{
                        sendMessage(chatId, """
                                Не понял тебя \uD83D\uDE05
                                еще раз напиши ты будешь здесь или с собой?""");
                    }
                }*/



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
            e.printStackTrace();
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
