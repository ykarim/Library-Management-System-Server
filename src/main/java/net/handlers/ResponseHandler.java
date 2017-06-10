package net.handlers;

import model.item.Book;
import model.item.LibraryBook;
import net.packet.confirm.ConfirmationPacket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to communicate results of network calls back to client
 */
public class ResponseHandler {

    private ObjectOutputStream outputStream;

    public ResponseHandler(OutputStream outputStream) {
        try {
            this.outputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Replies to client packet by sending ConfirmationPacket to client via ObjectOutputStream
     * Use case example: send back confirmation if book exists
     *
     * @param confirmationPacket
     */
    private void sendData(ConfirmationPacket confirmationPacket) {
        try {
            outputStream.writeObject(confirmationPacket);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates ConfirmationPacket with confirmation to relay to method sendData(ConfirmationPacket)
     *
     * @param confirmation
     */
    public void sendData(boolean confirmation) {
        sendData(new ConfirmationPacket(confirmation, null));
    }

    /**
     * Creates ConfirmationPacket with Book objects to relay to method sendData(ConfirmationPacket)
     * Use case example: send back list of books
     *
     * @param booksToSend
     */
    public void sendData(List<LibraryBook> booksToSend) {
        if (booksToSend != null && booksToSend.size() > 0) {
            ArrayList<Object> dataToSend = new ArrayList<>();
            for (Book book : booksToSend) {
                Object objBook = book;
                dataToSend.add(objBook);
            }
            sendData(new ConfirmationPacket(true, dataToSend));
        }
    }

    /**
     * Creates ConfirmationPacket with confirmation and data to relay to method sendData(ConfirmationPacket)
     *
     * @param confirmation
     * @param dataToSend
     */
    public void sendData(boolean confirmation, List<Object> dataToSend) {
        if (dataToSend != null && dataToSend.size() > 0) {
            sendData(new ConfirmationPacket(confirmation, dataToSend));
        }
    }
}
