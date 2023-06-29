package ru.myitschool.volleyball;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Класс-клиент
 * отправляет запрос серверу и принимает от него ответ
 */

public class MyClient {
    Client client;

    boolean isCantConnected;
    private final MyRequest request;
    private MyResponse response;
    private InetAddress address;

    public MyClient(MyRequest request) {
        this.request = request;
        response = new MyResponse();

        client = new Client();
        client.start();
        address = client.discoverHost(54777, 5000);
        try {
            client.connect(5000, address, 54555, 54777);
        } catch (IOException e) {
            isCantConnected = true; // если не удалось подключиться
            e.printStackTrace();
        }

        Kryo kryoClient = client.getKryo();
        kryoClient.register(MyRequest.class);
        kryoClient.register(MyResponse.class);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof MyResponse) {
                    response = (MyResponse) object;
                }
            }
        });
    }

    public MyResponse getResponse() {
        return response;
    }

    void send() {
        client.sendTCP(request);
    }

    public InetAddress getIp() {
        return address;
    }
}
