package org.example.exceptions;


import java.io.*;
import java.net.Socket;

public class CashRegisterClient {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;

    private DataOutputStream writer;
    private DataInputStream reader;
    private boolean running = false;

    public CashRegisterClient() {

        try {
            socket = new Socket("localhost", 5555);
            writer = new DataOutputStream(socket.getOutputStream());
            reader = new DataInputStream(socket.getInputStream());
            System.out.println(reader.readUTF());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create input and output streams



    }


    public void close() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
