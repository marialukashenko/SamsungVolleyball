package ru.myitschool.volleyball.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;



public class MyServer {
    public Server server;
    private MyRequest request;

    public MyServer(final MyResponse response) {
        request = new MyRequest();
        server = new Server();
        server.start();
        try {
            server.bind(54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Kryo kryo = server.getKryo();
        kryo.register(MyRequest.class);
        kryo.register(MyResponse.class);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof MyRequest) {
                    request = (MyRequest) object;
                    connection.sendTCP(response);
                }
            }
        });
    }

    public MyRequest getRequest() {
        return request;
    }
}
