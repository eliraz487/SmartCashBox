package org.example;


import org.example.exceptions.CashRegisterClient;
import org.example.exceptions.NotEnoughMoneyInCashException;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        CashRegisterClient cashRegisterClient=new CashRegisterClient();
        cashRegisterClient.startOver();
        cashRegisterClient.pay(Coin.DIME,90);
        cashRegisterClient.pay(Coin.QUARTER,1);
        cashRegisterClient.buy();
        cashRegisterClient.close();
    }


}