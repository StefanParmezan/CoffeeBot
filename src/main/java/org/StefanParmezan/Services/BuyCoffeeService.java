package org.StefanParmezan.Services;

public class BuyCoffeeService {
    public static BuyCoffeeService instance = new  BuyCoffeeService();

    private BuyCoffeeService() {}

    public static BuyCoffeeService getInstance() {
        return instance;
    }

    public int CalculatePrice(String filepath){
        return 0;
    }

}
