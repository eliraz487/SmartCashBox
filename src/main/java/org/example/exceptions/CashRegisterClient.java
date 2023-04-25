package org.example.exceptions;


import org.example.CashFunction;
import org.example.Coin;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class CashRegisterClient {

    private Socket socket;

    private DataOutputStream writer;
    private DataInputStream reader;
    private boolean running = false;

    public CashRegisterClient() {

        try {
            socket = new Socket("localhost", 5555);
            writer = new DataOutputStream(socket.getOutputStream());
            reader = new DataInputStream(socket.getInputStream());
        }catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: localhost.");
            System.exit(1);
        }

        // Create input and output streams



    }

    public void buy(){
        try {
            writer.writeUTF(String.valueOf(CashFunction.Buy));
            writer.flush();
            String response="";
            String temp="";
            while (!(temp=reader.readUTF()).equals("$")){
                response=response+"\n"+temp;
            }
            System.out.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void pay(Coin coin,int count){
        try {
            writer.writeUTF(String.valueOf(CashFunction.Pay));
            writer.writeUTF(coin.getName());
            writer.writeUTF(String.valueOf(count));
            writer.flush();
            String messege=reader.readUTF();
            System.out.println(messege);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void startOver(){
        try {
            writer.writeDouble(10.0);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            socket.close();
            writer.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
