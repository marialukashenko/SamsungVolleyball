package ru.myitschool.volleyball;

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

/**
 * настройка и выбор игроков
 */
public class ScreenNetwork implements Screen {
    private final VolleyBall iv;

    // изображения
    private final Texture imgBackGround;
    private final Texture imgBack;
    private final Texture imgSelector;

    // кнопки
    private final ImageButton btnBack;
    private final TextButton btnName1;
    private final TextButton btnName2;
    private final TextButton btnNetwork;
    private final TextButton btnPVP;
    private final TextButton btnCreateServer;
    private final TextButton btnCreateClient;
    private final TextButton btnStopServer;
    private final TextButton btnStopClient;

    // экранная клавиатура
    private final InputKeyboard inputKeyboard;
    private boolean isEnterName1;
    private boolean isEnterName2;

    // всё, что требуется для работы сетевого соединения
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

        // создаём кнопки и обновляем их содержимое
        btnPVP = new TextButton(iv.fontTitle, iv.text.get("PVP")[iv.lang], 450);

        btnNetwork = new TextButton(iv.fontTitle, iv.text.get("NETWORK")[iv.lang], 350);
        btnCreateServer = new TextButton(iv.fontSmall, iv.text.get("Start Server")[iv.lang], 100, 250);
        btnStopServer = new TextButton(iv.fontSmall, iv.text.get("Stop Server")[iv.lang], 100, 100);
        btnCreateClient = new TextButton(iv.fontSmall, iv.text.get("Start Client")[iv.lang], 100, 250);
        btnStopClient = new TextButton(iv.fontSmall, iv.text.get("Stop Client")[iv.lang], 100, 100);
        btnCreateClient.setXY(SCR_WIDTH*100-100-btnCreateClient.width, btnCreateClient.y);
        btnStopClient.setXY(SCR_WIDTH*100-100-btnStopClient.width, btnStopServer.y);


        updateButtons();

        // создаём экранную клавиатуру
        inputKeyboard = new InputKeyboard(SCR_WIDTH, SCR_HEIGHT, 8);

        // создаём объекты сетевого запроса и ответа
        requestFromClient = new MyRequest();
        responseFromServer = new MyResponse();
        infoOfConnection = iv.text.get("waiting for connection")[iv.lang];
        ipAddressOfServer = iv.text.get("server not started")[iv.lang];
    }

    @Override
    public void show() {
        updateButtons();
    }

    @Override
    public void render(float delta) {
        // обработка касаний экрана
        if (Gdx.input.justTouched()) {
            iv.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            iv.camera.unproject(iv.touch);
            // если НЕ включена экранная клавиатура, то все остальные кнопки работают
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

                if (btnNetwork.hit(iv.touch.x, iv.touch.y)) {
                    //iv.setScreen(iv.getScreenNetwork());
                }
                if (btnCreateServer.hit(iv.touch.x, iv.touch.y) && !isServer && !isClient) {
                    server = new MyServer(responseFromServer);
                    ipAddressOfServer = detectIP();
                    isServer = true;
                    infoOfConnection = iv.text.get("waiting for client connection")[iv.lang];
                }
                if (btnCreateClient.hit(iv.touch.x, iv.touch.y) && !isServer && !isClient) {
                    isClient = true;
                    client = new MyClient(requestFromClient);
                    ipAddressOfServer = client.getIp().getHostAddress();
                    if (ipAddressOfServer.startsWith("192")) {
                        infoOfConnection = iv.text.get("connected to the server")[iv.lang];
                        iv.isOnLanPlayer2 = true;
                    } else {
                        infoOfConnection = iv.text.get("waiting for a connection to the server")[iv.lang];
                    }
                    if (client.isCantConnected) {
                        isClient = false;
                        client = null;
                        ipAddressOfServer = iv.text.get("Server not found")[iv.lang];
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
            // если включена экранная клавиатура, то все остальные кнопки не работают
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

        // сетевые события игры
        if(isServer){
            responseFromServer.text = "server";
            responseFromServer.x = iv.touch.x;
            responseFromServer.y = iv.touch.y;
            responseFromServer.gameStyle = iv.gameStyle;
            responseFromServer.name = iv.player1.name;
            requestFromClient = server.getRequest();
            if(requestFromClient.text.equals("client")){
                infoOfConnection = iv.text.get("client connected")[iv.lang];
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
            if(responseFromServer.text.equals("server")){
                infoOfConnection = iv.text.get("connected to the server")[iv.lang];
                iv.isOnLanPlayer2 = true;
                iv.player1.name = responseFromServer.name;
                iv.gameStyle = responseFromServer.gameStyle;
            }
            if(responseFromServer.startGame){
                iv.setScreen(iv.getScreenGame());
            }
        }

        // отрисовка всей графики
        iv.camera.update();
        // рисуем картинки
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        iv.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        iv.batch.end();
        // рисуем буквы
        iv.batch.setProjectionMatrix(iv.cameraForText.combined);
        iv.batch.begin();
        iv.fontTitle.draw(iv.batch, iv.text.get("PLAYER 1")[iv.lang], 100, 550, SCR_WIDTH*100-100, Align.left, false);
        iv.fontTitle.draw(iv.batch, iv.text.get("PLAYER 2")[iv.lang], 0, 550, SCR_WIDTH*100-100, Align.right, false);
        btnName1.font.draw(iv.batch, btnName1.text, btnName1.x, btnName1.y);
        btnName2.font.draw(iv.batch, btnName2.text, btnName2.x, btnName2.y);

        btnNetwork.font.draw(iv.batch, btnNetwork.text, btnNetwork.x, btnNetwork.y);

        btnCreateServer.font.draw(iv.batch, btnCreateServer.text, btnCreateServer.x, btnCreateServer.y);
        iv.fontSmall.draw(iv.batch, iv.text.get("Server's IP: ")[iv.lang] + ipAddressOfServer, 0, btnCreateServer.y-80, SCR_WIDTH*100, Align.center, false);
        btnCreateClient.font.draw(iv.batch, btnCreateClient.text, btnCreateClient.x, btnCreateClient.y);
        iv.fontSmall.draw(iv.batch, infoOfConnection, 0, btnCreateServer.y-160, SCR_WIDTH*100, Align.center, false);
        //btnStopServer.font.draw(iv.batch, btnStopServer.text, btnStopServer.x, btnStopServer.y);
        //btnStopClient.font.draw(iv.batch, btnStopClient.text, btnStopClient.x, btnStopClient.y);

        iv.batch.draw(imgSelector, btnPVP.x-20, btnPVP.y-btnPVP.height*1.5f, btnPVP.width+40, btnPVP.height*2);
        btnPVP.font.draw(iv.batch, btnPVP.text, btnPVP.x, btnPVP.y);
        //iv.fontTitle.draw(iv.batch, iv.text.get("PLAYERS")[iv.lang], 20, SCR_HEIGHT*100-20);
        if(isEnterName1 || isEnterName2){
            inputKeyboard.draw(iv.batch);
        }
        iv.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        //
    }

    @Override
    public void pause() {
        //
    }

    @Override
    public void resume() {
        //
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

    // обновляем надписи на кнопках
    private void updateButtons(){
        btnNetwork.setText(iv.text.get("NETWORK")[iv.lang], false);
        btnPVP.setText(iv.text.get("PVP")[iv.lang], false);
    }

    // Определяем IP адрес собственного компа
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
