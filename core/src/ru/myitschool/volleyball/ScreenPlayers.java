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
public class ScreenPlayers implements Screen {
    private final VolleyBall iv;

    // изображения
    private final Texture imgBackGround;
    private final Texture imgBack;
    private final Texture imgSelector;

    // кнопки
    private final ImageButton btnBack;
    private final TextButton btnName1;
    private final TextButton btnName2;
    private final TextButton btnTypePlayer1;
    private final TextButton btnTypePlayer2;
    private final TextButton btnNetwork;
    private final TextButton btnPVP;

    // экранная клавиатура
    private final InputKeyboard inputKeyboard;
    private boolean isEnterName1;
    private boolean isEnterName2;

    public ScreenPlayers(VolleyBall volleyBall) {
        iv = volleyBall;

        imgBackGround = new Texture("screenbgplayers.jpg");
        imgBack = new Texture("back.png");
        imgSelector = new Texture("yellowselector.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 0.8f, SCR_HEIGHT - 0.8f, 0.6f, 0.6f);
        btnName1 = new TextButton(iv.fontNormal, iv.player1.name, 100, 400);
        btnName2 = new TextButton(iv.fontNormal, iv.player2.name, 0, 400);
        btnName2.setXY(SCR_WIDTH*100-100-btnName2.width, btnName2.y);

        // создаём кнопки и обновляем их содержимое
        btnTypePlayer1 = new TextButton(iv.fontNormal, iv.text.get("HUMAN")[iv.lang], 100, 300);
        btnTypePlayer2 = new TextButton(iv.fontNormal, iv.text.get("HUMAN")[iv.lang], 0, 300);
        btnPVP = new TextButton(iv.fontMega, iv.text.get("PVP")[iv.lang], 600);

        btnNetwork = new TextButton(iv.fontTitle, iv.text.get("NETWORK")[iv.lang], 100);

        updateButtons();

        // создаём экранную клавиатуру
        inputKeyboard = new InputKeyboard(SCR_WIDTH, SCR_HEIGHT, 8);
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
                    iv.setScreen(iv.getScreenIntro());
                }
                if (btnName1.hit(iv.touch.x, iv.touch.y)) {
                    isEnterName1 = true;
                }

                if (btnName2.hit(iv.touch.x, iv.touch.y)) {
                    isEnterName2 = true;
                }

                if (btnTypePlayer1.hit(iv.touch.x, iv.touch.y)) {
                    iv.player1.isAi = !iv.player1.isAi;
                    updateButtons();
                }
                if (btnTypePlayer2.hit(iv.touch.x, iv.touch.y)) {
                    iv.player2.isAi = !iv.player2.isAi;
                    updateButtons();
                }
                if (btnNetwork.hit(iv.touch.x, iv.touch.y)) {
                    iv.setScreen(iv.getScreenNetwork());
                }

                if (btnPVP.hit(iv.touch.x, iv.touch.y)) {
                    if (!iv.isOnLanPlayer2 && !iv.isOnLanPlayer1) {
                        iv.setScreen(iv.getScreenGame());
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
        iv.fontTitle.draw(iv.batch, iv.text.get("PLAYER 1")[iv.lang], 100, 500, SCR_WIDTH*100-100, Align.left, false);
        iv.fontTitle.draw(iv.batch, iv.text.get("PLAYER 2")[iv.lang], 0, 500, SCR_WIDTH*100-100, Align.right, false);
        btnName1.font.draw(iv.batch, btnName1.text, btnName1.x, btnName1.y);
        btnName2.font.draw(iv.batch, btnName2.text, btnName2.x, btnName2.y);
        iv.fontNormal.draw(iv.batch, iv.text.get("VS")[iv.lang], 0, btnTypePlayer1.y, SCR_WIDTH*100, Align.center, false);
        btnTypePlayer1.font.draw(iv.batch, btnTypePlayer1.text, btnTypePlayer1.x, btnTypePlayer1.y);
        btnTypePlayer2.font.draw(iv.batch, btnTypePlayer2.text, btnTypePlayer2.x, btnTypePlayer2.y);

        btnNetwork.font.draw(iv.batch, btnNetwork.text, btnNetwork.x, btnNetwork.y);

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
        if(iv.player1.isAi) {
            btnTypePlayer1.setText(iv.text.get("COMPUTER")[iv.lang], false);
        } else {
            btnTypePlayer1.setText(iv.text.get("HUMAN")[iv.lang], false);
        }
        if(iv.player2.isAi) {
            btnTypePlayer2.setText(iv.text.get("COMPUTER")[iv.lang], false);
        } else {
            btnTypePlayer2.setText(iv.text.get("HUMAN")[iv.lang], false);
        }
        // переставляем правую кнопку в зависимости от длины слова
        btnTypePlayer2.setXY(SCR_WIDTH*100-100-btnTypePlayer2.width, btnTypePlayer2.y);
        btnNetwork.setText(iv.text.get("NETWORK")[iv.lang], true);
        btnPVP.setText(iv.text.get("PVP")[iv.lang], true);
    }
}
