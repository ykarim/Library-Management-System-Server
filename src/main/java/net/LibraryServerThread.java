package net;

import log.Logger;
import net.handlers.BookResponseHandler;
import net.handlers.LibraryBookHandler;
import net.handlers.LibraryUserHandler;
import net.handlers.UserResponseHandler;
import net.packet.Packet;
import net.packet.auth.LoginPacket;
import net.packet.model.book.LibraryBookPacket;
import net.packet.model.user.LibAdminPacket;
import net.packet.model.user.LibUserPacket;
import net.packet.requests.RequestLibraryBookPacket;
import net.packet.requests.RequestLibraryBooksPacket;
import net.packet.requests.RequestNetUserPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created for each client connected and handles all received packets through respective packet handlers
 */
public class LibraryServerThread extends Thread {

    private volatile boolean running;
    private Socket socket;
    private ObjectInputStream inputStream;
    private LibraryBookHandler libraryBookHandler;
    private LibraryUserHandler libraryUserHandler;
    private BookResponseHandler bookResponseHandler;
    private UserResponseHandler userResponseHandler;

    /**
     * Starts Thread as well as inputStream to receive packets and creates handlers to process received packets
     * @param socket
     */
    public LibraryServerThread(Socket socket) {
        setRunning(true);
        this.socket = socket;

        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            bookResponseHandler = new BookResponseHandler(socket.getOutputStream());
            userResponseHandler = new UserResponseHandler(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        libraryBookHandler = new LibraryBookHandler();
        libraryUserHandler = new LibraryUserHandler();
    }

    /**
     * Sends all respective packets (non-null) to their handlers
     */
    public void run() {
        while (running) {
            try {
                Packet packet = (Packet) inputStream.readObject();
                if (packet != null) {
                    if (packet instanceof LibraryBookPacket) {
                        bookResponseHandler.sendData(libraryBookHandler.parseLibraryBookPacket((LibraryBookPacket) packet));
                    } else if (packet instanceof LibUserPacket) {
                        userResponseHandler.sendData(libraryUserHandler.parseLibUserPacket((LibUserPacket) packet));
                    } else if (packet instanceof LibAdminPacket) {
                        userResponseHandler.sendData(libraryUserHandler.parseLibAdminPacket((LibAdminPacket) packet));
                    } else if (packet instanceof RequestLibraryBookPacket) {
                        bookResponseHandler.sendData(libraryBookHandler.parseBookPropertiesPacket((RequestLibraryBookPacket) packet));
                    } else if (packet instanceof RequestLibraryBooksPacket) {
                        bookResponseHandler.sendBooks(libraryBookHandler.parseLibraryBooksPacket());
                    } else if (packet instanceof RequestNetUserPacket) {
                        userResponseHandler.sendData(libraryUserHandler.parseNetUserPacket((RequestNetUserPacket) packet));
                    } else if (packet instanceof LoginPacket) {
                        userResponseHandler.sendData(libraryUserHandler.parseLoginPacket((LoginPacket) packet));
                    }
                }
            } catch (Exception e) {
                Logger.writeException(null, e);
            }
        }
    }

    /**
     * Closes socket and sets running to false
     */
    public void stopSocket() {
        try {
            socket.close();
            running = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        if (this.running && !running) { //Going from true to false (on to off)
            stopSocket();
        } else { //Going from false to true (off to on)
            this.running = running;
        }
    }
}
