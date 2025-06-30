package org.StefanParmezan;

import org.StefanParmezan.Models.Drinks;
import org.StefanParmezan.Models.UserState;
import org.StefanParmezan.Services.BuyCoffeeService;
import org.StefanParmezan.Services.FileReadService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CoffeeBot extends TelegramLongPollingBot {

    private final Map<Long, UserState> userStates = new HashMap<>();
    private final BuyCoffeeService buyCoffeeService = BuyCoffeeService.getInstance();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            // 1. Проверяем, ожидаем ли мы ответ от пользователя
            if (userStates.containsKey(chatId)) {
                UserState state = userStates.get(chatId);

                if (state == UserState.AWAITING_LOCATION) {
                    if (messageText.equalsIgnoreCase("здесь")) {
                        sendMessage(chatId, """
                                Отправьте .txt файл с заказом.
                                Формат:
                                Latte=2
                                Mocha=1+Cheese""");
                        userStates.put(chatId, UserState.AWAITING_ORDER_FILE_HERE);

                    } else if (messageText.equalsIgnoreCase("с собой")) {
                        sendMessage(chatId, """
                                Отправьте .txt файл с заказом.
                                Формат:
                                Latte=2
                                Mocha=1+Cheese""");
                        userStates.put(chatId, UserState.AWAITING_ORDER_FILE_TO_GO);
                    } else {
                        sendMessage(chatId, "Напишите 'здесь' или 'с собой'");
                    }

                    userStates.remove(chatId); // Очищаем состояние
                    return;
                }
            }

            // 2. Обработка команд
            switch (messageText) {
                case "/start":
                    sendMessage(chatId, """
                            Привет! Это бот для заказа кофе 🧀
                            /menu — посмотреть меню
                            /buy — сделать заказ""");
                    break;

                case "/menu":
                    sendMessage(chatId, Drinks.getDrinksToString());
                    break;

                case "/buy":
                    sendMessage(chatId, "Вы хотите здесь или с собой?");
                    userStates.put(chatId, UserState.AWAITING_LOCATION);
                    break;

                default:
                    sendMessage(chatId, "Неизвестная команда: " + messageText);
                    break;
            }

        } else if (update.hasMessage() && update.getMessage().hasDocument()) {
            Long chatId = update.getMessage().getChatId();
            UserState state = userStates.getOrDefault(chatId, UserState.IDLE);

            if (state == UserState.AWAITING_ORDER_FILE_HERE || state == UserState.AWAITING_ORDER_FILE_TO_GO) {
                try {
                    String fileId = update.getMessage().getDocument().getFileId();
                    InputStream fileStream = super.downloadFileAsStream(fileId);

                    // Сохраняем файл локально
                    saveUserFIle(fileStream, chatId);

                    // Перечитываем файл, чтобы CalculatePrice(...) работал корректно
                    fileStream = super.downloadFileAsStream(fileId);

                    int totalCost = buyCoffeeService.CalculatePrice(fileStream);

                    if (state == UserState.AWAITING_ORDER_FILE_HERE) {
                        sendMessage(chatId, "Итого: " + totalCost + " ₽\nПриятного кофе!");

                    } else if (state == UserState.AWAITING_ORDER_FILE_TO_GO) {
                        File receiptFile = createReceiptFile(chatId, totalCost);
                        sendDocument(chatId, receiptFile);
                    }

                    userStates.remove(chatId); // Сбрасываем состояние

                } catch (Exception e) {
                    sendMessage(chatId, "Ошибка при обработке файла.");
                    e.printStackTrace();
                }
            }
        }
    }

    protected void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void sendDocument(Long chatId, File file) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId.toString());

        try {
            sendDocument.setDocument(new InputFile(new FileInputStream(file), file.getName()));
            execute(sendDocument);
        } catch (TelegramApiException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private File createReceiptFile(Long chatId, int totalCost) {
        try {
            File tempFile = java.io.File.createTempFile("receipt_" + chatId, ".txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write("☕ Чек CoffeeBot");
                writer.newLine();
                writer.write("--------------------");
                writer.newLine();
                writer.write("Итого: " + totalCost + " ₽");
                writer.newLine();
                writer.write("Спасибо за заказ!");
            }

            return tempFile;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveUserFIle(InputStream inputStream, Long chatId) {
        String basePath = "C:\\Users\\StefanParmezan\\Desktop\\Home\\Programming\\CoffeeBot\\src\\main\\resources\\orders";
        File directory = new File(basePath);

        if (!directory.exists()) {
            directory.mkdirs(); // создаём папку, если её нет [[3]]
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

            System.out.println("Файл сохранён: " + outputFile.getAbsolutePath());

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
        return FileReadService.getInstance().readFile("C:\\Users\\StefanParmezan\\Desktop\\Home\\Programming\\CoffeeBot\\src\\main\\resources\\bot_token"); // заменить на чтение из файла
    }
}