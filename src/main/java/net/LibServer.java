package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class LibServer extends Thread {

    public boolean running = true;
    private ServerSocket serverSocket;
    private Socket socket;
    private ArrayList<String> connectedClients = new ArrayList<>();

    public LibServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            runServer();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void runServer() {
        while (running) {
            try {
                socket = serverSocket.accept();
                connectedClients.add(socket.getRemoteSocketAddress().toString());
                new LibraryServerThread(socket).start();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }
}