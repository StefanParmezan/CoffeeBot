package org.StefanParmezan;
import org.StefanParmezan.Models.Drinks;
import org.StefanParmezan.Services.FileReadService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;

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
    protected void saveUserFIle(InputStream inputStream, Long chatId) {
        String basePath = "C:\\Users\\StefanParmezan\\Desktop\\Home\\Programming\\CoffeeBot\\src\\main\\resources\\orders";
        File directory = new File(basePath);

        if (!directory.exists()) {
            directory.mkdirs(); // создаём папку, если её нет
        }
        String fileName = "order_" + chatId + "_" + System.currentTimeMillis() + ".txt";
        File outputFile = new File(directory, fileName);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            System.out.println("Файл успешно сохранён: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Ошибка при сохранении файла: " + e.getMessage());
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
