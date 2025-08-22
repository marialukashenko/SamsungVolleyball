package ru.myitschool.volleyball.screens;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import ru.myitschool.volleyball.components.ImageButton;
import ru.myitschool.volleyball.components.InputKeyboard;
import ru.myitschool.volleyball.components.TextButton;
import ru.myitschool.volleyball.VolleyBall;
import ru.myitschool.volleyball.server.MyClient;
import ru.myitschool.volleyball.server.MyRequest;
import ru.myitschool.volleyball.server.MyResponse;
import ru.myitschool.volleyball.server.MyServer;


public class ScreenNetwork implements Screen {
    private final VolleyBall iv;


    private final Texture imgBackGround;
    private final Texture imgBack;
    private final Texture imgSelector;


    private final ImageButton btnBack;
    private final TextButton btnName1;
    private final TextButton btnName2;
    private final TextButton btnNetwork;
    private final TextButton btnPVP;
    private final TextButton btnCreateServer;
    private final TextButton btnCreateClient;
    private final TextButton btnStopServer;
    private final TextButton btnStopClient;


    private final InputKeyboard inputKeyboard;
    private boolean isEnterName1;
    private boolean isEnterName2;

    private InetAddress ipAddress;
    private String ipAddressOfServer;
    public MyServer server;
    public MyClient client;
    public boolean isServer;
    public boolean isClient;
    public MyRequest requestFromClient;
    public MyResponse responseFromServer;
    private String infoOfConnection;

    public ScreenNetwork(VolleyBall volleyBall) {
        iv = volleyBall;

        imgBackGround = new Texture("screenbgnetwork.jpg");
        imgBack = new Texture("back.png");
        imgSelector = new Texture("yellowselector.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 0.8f, SCR_HEIGHT - 0.8f, 0.6f, 0.6f);
        btnName1 = new TextButton(iv.fontNormal, iv.player1.name, 100, 500);
        btnName2 = new TextButton(iv.fontNormal, iv.player2.name, 0, 500);
        btnName2.setXY(SCR_WIDTH*100-100-btnName2.width, btnName2.y);

        btnPVP = new TextButton(iv.fontTitle, iv.myBundle.get("pvp"), 450);

        btnNetwork = new TextButton(iv.fontTitle, iv.myBundle.get("network"), 350);
        btnCreateServer = new TextButton(iv.fontSmall, iv.myBundle.get("start_server"), 100, 250);
        btnStopServer = new TextButton(iv.fontSmall, iv.myBundle.get("stop_server"), 100, 100);
        btnCreateClient = new TextButton(iv.fontSmall, iv.myBundle.get("start_client"), 100, 250);
        btnStopClient = new TextButton(iv.fontSmall, iv.myBundle.get("stop_client"), 100, 100);
        btnCreateClient.setXY(SCR_WIDTH*100-100-btnCreateClient.width, btnCreateClient.y);
        btnStopClient.setXY(SCR_WIDTH*100-100-btnStopClient.width, btnStopServer.y);


        updateButtons();

        inputKeyboard = new InputKeyboard(SCR_WIDTH, SCR_HEIGHT, 8);

        requestFromClient = new MyRequest();
        responseFromServer = new MyResponse();
        infoOfConnection = "waiting_for_connection";
        ipAddressOfServer = "server_not_started";
    }

    @Override
    public void show() {
        updateButtons();
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.justTouched()) {
            iv.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            iv.camera.unproject(iv.touch);
            if(!isEnterName1 && !isEnterName2) {
                if (btnBack.hit(iv.touch.x, iv.touch.y)) {
                    iv.setScreen(iv.getScreenPlayers());
                }
                if (btnName1.hit(iv.touch.x, iv.touch.y)) {
                    isEnterName1 = true;
                }

                if (btnName2.hit(iv.touch.x, iv.touch.y)) {
                    isEnterName2 = true;
                }

                if (btnCreateServer.hit(iv.touch.x, iv.touch.y) && !isServer && !isClient) {
                    server = new MyServer(responseFromServer);
                    ipAddressOfServer = detectIP();
                    isServer = true;
                    infoOfConnection = "waiting_for_client_connection";
                }
                if (btnCreateClient.hit(iv.touch.x, iv.touch.y) && !isServer && !isClient) {
                    isClient = true;
                    client = new MyClient(requestFromClient);
                    ipAddressOfServer = client.getIp().getHostAddress();
                    if (ipAddressOfServer.startsWith("192")) {
                        infoOfConnection = "connected_to_the_server";
                        iv.isOnLanPlayer2 = true;
                    } else {
                        infoOfConnection = "waiting_for_a_connection_to_the_server";
                    }
                    if (client.isCantConnected) {
                        isClient = false;
                        client = null;
                        ipAddressOfServer = "server_not_found";
                    }
                }

                if (btnPVP.hit(iv.touch.x, iv.touch.y)) {
                    if (!iv.isOnLanPlayer2) {
                        iv.setScreen(iv.getScreenGame());
                    }
                    if (iv.isOnLanPlayer1) {
                        responseFromServer.startGame = true;
                    }
                }
            }
            else if (isEnterName1 && inputKeyboard.endOfEdit(iv.touch.x, iv.touch.y)) {
                iv.player1.name = inputKeyboard.getText();
                btnName1.setText(iv.player1.name, false);
                isEnterName1 = false;
            } else if (isEnterName2 && inputKeyboard.endOfEdit(iv.touch.x, iv.touch.y)) {
                iv.player2.name = inputKeyboard.getText();
                btnName2.setText(iv.player2.name, false);
                btnName2.setXY(SCR_WIDTH * 100 - 100 - btnName2.width, btnName2.y);
                isEnterName2 = false;
            }
        }

        if(isServer){
            responseFromServer.text = "ru/myitschool/volleyball/server";
            responseFromServer.x = iv.touch.x;
            responseFromServer.y = iv.touch.y;
            responseFromServer.gameStyle = iv.gameStyle;
            responseFromServer.name = iv.player1.name;
            requestFromClient = server.getRequest();
            if(requestFromClient.text.equals("client")){
                infoOfConnection = "client_connected";
                iv.isOnLanPlayer1 = true;
                iv.player2.name = requestFromClient.name;
            }
        } else if(isClient){
            requestFromClient.text = "client";
            requestFromClient.x = iv.touch.x;
            requestFromClient.y = iv.touch.y;
            requestFromClient.name = iv.player2.name;
            client.send();
            responseFromServer = client.getResponse();
            if(responseFromServer.text.equals("ru/myitschool/volleyball/server")){
                infoOfConnection = "connected_to_the_server";
                iv.isOnLanPlayer2 = true;
                iv.player1.name = responseFromServer.name;
                iv.gameStyle = responseFromServer.gameStyle;
            }
            if(responseFromServer.startGame){
                iv.setScreen(iv.getScreenGame());
            }
        }

        iv.camera.update();
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        iv.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        iv.batch.end();
        iv.batch.setProjectionMatrix(iv.cameraForText.combined);
        iv.batch.begin();
        iv.fontTitle.draw(iv.batch, iv.myBundle.get("player_1"), 100, 550, SCR_WIDTH*100-100, Align.left, false);
        iv.fontTitle.draw(iv.batch, iv.myBundle.get("player_2"), 0, 550, SCR_WIDTH*100-100, Align.right, false);
        btnName1.font.draw(iv.batch, btnName1.text, btnName1.x, btnName1.y);
        btnName2.font.draw(iv.batch, btnName2.text, btnName2.x, btnName2.y);

        btnNetwork.font.draw(iv.batch, btnNetwork.text, btnNetwork.x, btnNetwork.y);

        btnCreateServer.font.draw(iv.batch, btnCreateServer.text, btnCreateServer.x, btnCreateServer.y);
        iv.fontSmall.draw(iv.batch, iv.myBundle.get("servers_ip") + " " + iv.myBundle.get(ipAddressOfServer), 0, btnCreateServer.y-80, SCR_WIDTH*100, Align.center, false);
        btnCreateClient.font.draw(iv.batch, btnCreateClient.text, btnCreateClient.x, btnCreateClient.y);
        iv.fontSmall.draw(iv.batch, iv.myBundle.get(infoOfConnection), 0, btnCreateServer.y-160, SCR_WIDTH*100, Align.center, false);

        iv.batch.draw(imgSelector, btnPVP.x-20, btnPVP.y-btnPVP.height*1.5f, btnPVP.width+40, btnPVP.height*2);
        btnPVP.font.draw(iv.batch, btnPVP.text, btnPVP.x, btnPVP.y);
        if(isEnterName1 || isEnterName2){
            inputKeyboard.draw(iv.batch);
        }
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
        iv.getScreenSettings().saveSettings();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
        imgBack.dispose();
        inputKeyboard.dispose();
    }

    private void updateButtons(){
        btnNetwork.setText(iv.myBundle.get("network"), true);
        btnPVP.setText(iv.myBundle.get("pvp"), true);
        btnCreateServer.setText(iv.myBundle.get("start_server"), false);
        btnStopServer.setText(iv.myBundle.get("stop_server"), false);
        btnCreateClient.setText(iv.myBundle.get("start_client"), false);
        btnStopClient.setText(iv.myBundle.get("stop_client"), false);
    }

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
