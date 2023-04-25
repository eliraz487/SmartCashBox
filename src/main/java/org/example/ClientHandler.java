package org.example;

import org.example.exceptions.NotEnoughMoneyException;
import org.example.exceptions.NotEnoughMoneyInCashException;
import org.example.exceptions.YouAlreadyPayException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ClientHandler implements Runnable, Clientresopne {

    private Socket clientSocket;
    private double purchaseAmount=0;

    private DataOutputStream writer;
    private DataInputStream reader;


    private String name;

    private CashRegister cashRegister;

    public ClientHandler(Socket clientSocket, String name) {
        try {
            writer = new DataOutputStream(clientSocket.getOutputStream());
            reader = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.cashRegister = new CashRegister();
        this.name = name;
    }

    @Override
    public void run() {

        try {
            this.purchaseAmount = reader.readDouble();
            cashRegister.startPurchase(purchaseAmount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (!cashRegister.ispPurchaseFinish()) {
            String response = null;
            try {
                response = reader.readUTF();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (response.equals(CashFunction.Buy.name())) {
                bay();
                continue;
            }
            if (response.equals(CashFunction.Pay.name())) {
                pay();
            }
        }
    }

    private void pay() {
        try {
            String coinname;
            int coincount;
            coinname = reader.readUTF();
            coincount = Integer.parseInt(reader.readUTF());
            Coin coin = Coin.valueOf(coinname.toUpperCase());
            boolean youhaveenoughmoney = cashRegister.receivePayment(coin, coincount);
            if (youhaveenoughmoney) {
                writer.writeUTF("you have enough money to buy");
            } else {
                writer.writeUTF("you dont have enough money to buy");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (YouAlreadyPayException e) {
            try {
                writer.writeUTF("you already pay");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void bay() {
        Map<Coin, Integer> excess = null;
        while (!cashRegister.ispPurchaseFinish()) {
            try {
                excess = cashRegister.calculateExcess();
            } catch (NotEnoughMoneyInCashException e) {
                try {
                    writer.writeUTF("not enough money in Cash");
                    System.out.println("not enough money in Cash");
                    wait();
                } catch (InterruptedException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (NotEnoughMoneyException e) {
                try {
                    writer.writeUTF("not enough money you less :"+e.getLess());
                    System.out.println("not enough money you less :"+e.getLess());
                    writer.writeUTF("$");
                    return;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
        try {
            writer.writeUTF("your excess is :");
            System.out.println("your excess is :");
            for (Map.Entry<Coin, Integer> entry : excess.entrySet()) {
                writer.writeUTF(entry.getKey()+":"+entry.getValue());
                System.out.println(entry.getKey()+":"+entry.getValue());
            }
            writer.writeUTF("$");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void buyFinshed() {

    }
}