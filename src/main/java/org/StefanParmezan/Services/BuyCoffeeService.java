package org.StefanParmezan.Services;

import org.StefanParmezan.Models.Drinks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BuyCoffeeService {
    public static BuyCoffeeService instance = new  BuyCoffeeService();

    private BuyCoffeeService() {}

    public static BuyCoffeeService getInstance() {
        return instance;
    }

    public int CalculatePrice(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int totalPrice = 0;
        while((line = reader.readLine()) != null) {
            totalPrice += processLine(line);
        }
        return totalPrice;
    }

    private int processLine(String line) {
        if(!line.contains("=")) return 0;

        String[] parts = line.split("=");
        String drinkName = parts[0].trim().toUpperCase();
        String quantityExtra =  parts[1].trim();

        String[] subParts = quantityExtra.split("\\+");
        int quantity = Integer.parseInt(subParts[0]);

        boolean hasExtra = subParts.length > 1;

        int total = Drinks.getPrice(drinkName) * quantity;
        System.out.println("total " + total);
        if (hasExtra) {
            total += 40 * quantity; // цена за добавку
        }
        return total;
    }

}