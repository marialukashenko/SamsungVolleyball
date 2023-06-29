package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ScreenNetwork implements Screen {
    private VolleyBall iv;
    private Texture imgBackGround;

    TextButton btnCreateServer;
    TextButton btnCreateClient;
    TextButton btnStop;
    TextButton btnExit;

    // всё, что требуется для работы сетевого соединения
    private InetAddress ipAddress;
    private String ipAddressOfServer = "?";
    MyServer server;
    MyClient client;
    boolean isServer;
    boolean isClient;
    MyRequest requestFromClient;
    MyResponse responseFromServer;

    public ScreenNetwork(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("screenbgnetwork.jpg");
        btnCreateServer = new TextButton(iv.fontNormal, "Create Server", 100, 700);
        btnCreateClient = new TextButton(iv.fontNormal, "Create Client", 100, 500);
        btnStop = new TextButton(iv.fontNormal, "Stop Client/Server", 100, 400);
        btnExit = new TextButton(iv.fontNormal, "Exit", 100, 100);

        requestFromClient = new MyRequest();
        responseFromServer = new MyResponse();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // обработка касаний экрана
        if(Gdx.input.isTouched()){
            iv.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            iv.camera.unproject(iv.touch);

            if(btnCreateServer.hit(iv.touch.x, iv.touch.y) && !isServer && !isClient) {
                server = new MyServer(responseFromServer);
                ipAddressOfServer = detectIP();
                isServer = true;
            }
            if(btnCreateClient.hit(iv.touch.x, iv.touch.y) && !isServer && !isClient){
                isClient = true;
                client = new MyClient(requestFromClient);
                ipAddressOfServer = client.getIp().getHostAddress();
                if(client.isCantConnected){
                    isClient = false;
                    client = null;
                    ipAddressOfServer = "Server not found";
                }
            }
            /*if(btnStop.hit(iv.touch.x, iv.touch.y)){
                if(isServer) {
                    isServer = false;
                    server.server.stop();
                    try {
                        server.server.dispose();
                    } catch (IOException e) {
                        //throw new RuntimeException(e);
                    }
                }
                if(isClient) {
                    isClient = false;
                    client.client.stop();
                    try {
                        client.client.dispose();
                    } catch (IOException e) {
                        //throw new RuntimeException(e);
                    }
                }
                ipAddressOfServer = "?";
            }*/
            if(btnExit.hit(iv.touch.x, iv.touch.y)){
                iv.setScreen(iv.getScreenPlayers());
            }
        }

        // события игры
        if(isServer){
            responseFromServer.text = "blue: ";
            responseFromServer.x = iv.touch.x;
            responseFromServer.y = iv.touch.y;
            requestFromClient = server.getRequest();
        } else if(isClient){
            requestFromClient.text = "red: ";
            requestFromClient.x = iv.touch.x;
            requestFromClient.y = iv.touch.y;
            client.send();
            responseFromServer = client.getResponse();
        }

        // отрисовка всей графики
        iv.camera.update();
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        iv.batch.end();
        iv.batch.setProjectionMatrix(iv.cameraForText.combined);
        iv.batch.begin();
        iv.fontSmall.draw(iv.batch, "Server "+ responseFromServer.text+ (int) responseFromServer.x+" "+ (int) responseFromServer.y, 100, 300);
        iv.fontSmall.draw(iv.batch, "Client "+ requestFromClient.text+ (int) requestFromClient.x+" "+ (int) requestFromClient.y, 100, 200);

        btnCreateServer.font.draw(iv.batch, btnCreateServer.text, btnCreateServer.x, btnCreateServer.y);
        iv.fontSmall.draw(iv.batch, "Server's IP: "+ ipAddressOfServer, btnCreateServer.x, btnCreateServer.y-100);
        btnCreateClient.font.draw(iv.batch, btnCreateClient.text, btnCreateClient.x, btnCreateClient.y);
        btnStop.font.draw(iv.batch, btnStop.text, btnStop.x, btnStop.y);
        btnExit.font.draw(iv.batch, btnExit.text, btnExit.x, btnExit.y);
        iv.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }

    /**
     * Определяем IP адрес собственного компа
     * @return IP
     */
    public String detectIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLinkLocalAddress() && !address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        ipAddress = address;
                        //System.out.println("IP-адрес устройства: " + ipAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if(ipAddress != null){
            return ipAddress.getHostAddress();
        }
        return "";
    }
}

