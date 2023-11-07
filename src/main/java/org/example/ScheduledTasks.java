package org.example;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledTasks {
    private static final String LOG_FILE = "LogFile";
    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    private static ScheduledExecutorService executor ;
    private static List<PrintWriter> clientWriters;
    private static int timerCounter = 0;

    public ScheduledTasks() {
        executor = Executors.newScheduledThreadPool(3);
        clientWriters = new ArrayList<>();
    }

    public void logMessage(String request, String response, int clientCount) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
            String timestamp = dateFormat.format(new Date());
            writer.println(timestamp + " - Client " + clientCount + ", received message: " + request
            + ", sent message: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClientWriters(PrintWriter clientWriters) {
        ScheduledTasks.clientWriters.add(clientWriters);
    }

    public ScheduledFuture<?> startTimer() {
         return executor.scheduleAtFixedRate(ScheduledTasks::logCurrentTime, 0, 10, TimeUnit.SECONDS);
    }

    private static void logCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        String timestamp = dateFormat.format(new Date());
        String message = "Counter " + timerCounter + ", Time " + timestamp;
        System.out.println(message);
        timerCounter++;

        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }

    public void closeConnections(BufferedReader in, PrintWriter out, Socket clientSocket) {
        try {
            in.close();
            out.close();
            clientSocket.close();
            if (ClientHandler.timerTask != null) {
                ClientHandler.timerTask.cancel(true);
            }
            System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}