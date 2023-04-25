package org.example;

import org.example.exceptions.NotEnoughMoneyInCashException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class BuyThread implements Runnable {

    private final CashRegister cashRegister;
    private final DataOutputStream writer;

    private final String name;

    private Clientresopne clientresopne;
    public BuyThread(CashRegister cashRegister, DataOutputStream dataOutputStream, String name,Clientresopne clientresopne) {
        this.cashRegister = cashRegister;
        this.writer = dataOutputStream;
        this.name = name;
        this.clientresopne=clientresopne;
    }

    @Override
    public void run() {


    }
}
