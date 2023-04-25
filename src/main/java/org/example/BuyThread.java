package org.example;

import org.example.exceptions.NotEnoughMoneyInCashException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class BuyThread implements Runnable {

    private final CashRegister cashRegister;
    private final DataOutputStream writer;

    private final String name;

    public BuyThread(CashRegister cashRegister, DataOutputStream dataOutputStream, String name) {
        this.cashRegister = cashRegister;
        this.writer = dataOutputStream;
        this.name = name;
    }

    @Override
    public void run() {
        Map<Coin, Integer> excess = null;
        while (excess == null) {
            try {
                excess = cashRegister.calculateExcess();
            } catch (NotEnoughMoneyInCashException e) {
                try {
                    writer.writeUTF("not enough money in Cash");
                    wait();
                } catch (InterruptedException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        try {
            writer.writeUTF("your excess is :");
            for (Map.Entry<Coin, Integer> entry : excess.entrySet()) {
                writer.writeUTF(entry.getKey()+":"+entry.getValue());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
