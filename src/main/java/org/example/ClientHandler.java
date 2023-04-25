package org.example;

import org.example.exceptions.YouAlreadyPayException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private double purchaseAmount=0;

    private DataOutputStream writer;
    private DataInputStream reader;

    private boolean isinbuyprossing;

    private static ArrayList<Thread> multiprocessingbuy =new ArrayList<>();
    private String name;

    private CashRegister cashRegister;
    public ClientHandler(Socket clientSocket,String name,double purchase){
        try {
            writer = new DataOutputStream(clientSocket.getOutputStream());
            reader = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.purchaseAmount=purchaseAmount;
        CashRegister cashRegister=new CashRegister();
        cashRegister.startPurchase(purchaseAmount);
        this.name=name;
        this.isinbuyprossing=false;
    }

    @Override
    public void run() {

      while (cashRegister.ispPurchaseFinish()){
          if (isinbuyprossing){
              return;
          }
          String response = null;
          while (response == null) {
              // Wait for response from client
              try {
                  response = reader.readLine();
              } catch (IOException e) {
                  throw new RuntimeException(e);
              }
          }
          if (response==CashFunction.Buy.name()){
              isinbuyprossing=true;
              bay();
              continue;
          }
          if (response==CashFunction.Pay.name()){
              pay();
          }
      }
    }

    private void pay() {
        try {
            String coinname;
            int coincount;
            coinname=reader.readLine();
            coincount= Integer.parseInt(reader.readLine());

            boolean youhaveenoughmoney=cashRegister.receivePayment(Coin.valueOf(coinname),coincount);
            if (youhaveenoughmoney){
                writer.writeUTF("you have enough money to buy");
            }
            multiprocessingbuy.toArray().notifyAll();//without algorithm;

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
       Thread buyprocessing=new Thread(new BuyThread(cashRegister,writer,name));
       multiprocessingbuy.add(buyprocessing);
    }


}