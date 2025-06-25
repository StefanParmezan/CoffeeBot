package org.StefanParmezan.Models;

import java.util.Map;
import java.util.TreeMap;

public enum Drinks {
    LATTE(240),
    ESPRESSO(130),
    AMERICANO(150),
    CAPPUCCINO(180),
    MACCHIATO(200),
    MOCHA(250),
    FLAT_WHITE(220),
    RAF(260),
    COCOA_LATTE(280),
    GLACE(270),
    CHEESE(40);

    int price;
    Drinks(int price) {
        this.price = price;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public static int getPrice(String drinkName) {
        return Drinks.valueOf(drinkName).getPrice();
    }


    public Drinks addCheese(Drinks drink) {
        drink.setPrice(drink.getPrice() + 40);
        return drink;
    }

    public static String getDrinksToString(){
        StringBuilder result = new StringBuilder();
        result.append("CoffeeMenu ☕\n\n");
        for(Drinks drink : Drinks.values()){
            result.append(drink.toString().toLowerCase()).append(": ").append(drink.getPrice() + " руб.");
            result.append("\n");
        }
        return result.toString();
    }


}
