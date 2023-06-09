package org.example;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CashRegisterServer {
    private static final int PORT = 5555;
    private static final int MAX_THREADS = 10;

    private final ExecutorService executorService;
    private boolean running;

    private Integer countthread;

    public CashRegisterServer() {
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
        this.running = false;
        this.countthread=0;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        this.running = true;
        while (running) {
            Socket socket = serverSocket.accept();
            System.out.println("client connected");
            countthread++;
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            ClientHandler clientHandler=new ClientHandler(socket,String.valueOf( countthread));
            executorService.submit(clientHandler);
        }

        serverSocket.close();
        executorService.shutdown();
    }

    public void stop() {
        this.running = false;
    }



    public static void main(String[] args) throws IOException {
        CashRegisterServer server = new CashRegisterServer();
        server.start();
    }
}
