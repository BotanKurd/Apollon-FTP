package org.apollon.network.message.processor;

import org.apollon.entity.Event;
import org.apollon.entity.enums.EventType;
import org.apollon.network.core.FTPClient;
import org.apollon.network.message.ClientMessageHandler;
import org.apollon.network.message.Message;
import org.apollon.network.message.impl.ConnectionResponse;
import org.apollon.network.message.impl.ErrorResponse;

import java.net.Socket;

public class ClientMessageProcessor {
    private final Socket socket;

    private final ClientMessageHandler[] handlers;


    public ClientMessageProcessor(Socket socket) {
        this.socket = socket;
        this.handlers = new ClientMessageHandler[Byte.MAX_VALUE];

        init();
    }

    private void init() {
        this.handlers[0] = new ConnectionResponse();
        this.handlers[Byte.MAX_VALUE - 1] = new ErrorResponse();
    }

    public void parse(Message message, FTPClient client) {
        try {
            this.handlers[message.getId()].parse(message.getData(), client, socket);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            client.handleEvent(new Event(EventType.BAD_PROTOCOL));
        }
    }


}
