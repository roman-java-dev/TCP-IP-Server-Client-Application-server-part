package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final List<String> clients;
    private PrintWriter out;
    private BufferedReader in;
    private final ScheduledTasks scheduledTasks;
    private final ProcessDataService service;
    static ScheduledFuture<?> timerTask;

    public ClientHandler(Socket socket, int clientCount, List<String> clients) {
        this.clientSocket = socket;
        this.clients = clients;
        scheduledTasks = new ScheduledTasks();
        service = new ProcessDataService();
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            scheduledTasks.addClientWriters(out);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            try {
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);
                String output = service.processData(inputLine);
                out.println("Server answer: " + output);
                scheduledTasks.logMessage(inputLine, output, clients.size());
                if (timerTask == null){
                    timerTask = scheduledTasks.startTimer();
                }
            }
            } catch (SocketException e) {
                System.out.println("Client was disconnected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scheduledTasks.closeConnections(in, out, clientSocket);
        }
    }
}