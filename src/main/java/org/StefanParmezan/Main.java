package org.StefanParmezan;

import org.StefanParmezan.Services.BuyCoffeeService;
import org.StefanParmezan.Services.FileReadService;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws TelegramApiException, IOException {
        String token = FileReadService.getInstance().readFile("C:\\Users\\StefanParmezan\\Desktop\\Home\\Programming\\CoffeeBot\\src\\main\\resources\\bot_token");
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new CoffeeBot());
            System.out.println("Bot started!");
        } catch (TelegramApiException e) {
            System.out.println("Bot starting failed!");
            e.printStackTrace();
        }
        InputStream file = null;
        try {
            file = new FileInputStream("C:\\Users\\StefanParmezan\\Desktop\\Home\\Programming\\CoffeeBot\\src\\main\\resources\\orders\\Заказ.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(BuyCoffeeService.getInstance().CalculatePrice(file));

    }
}