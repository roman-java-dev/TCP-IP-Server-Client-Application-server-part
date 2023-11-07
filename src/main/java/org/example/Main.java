package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int PORT = 8080;
    private static List<String> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server starting...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                clients.add(clientSocket.getInetAddress().getHostAddress());
                new ClientHandler(clientSocket, clients.size(), clients).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}