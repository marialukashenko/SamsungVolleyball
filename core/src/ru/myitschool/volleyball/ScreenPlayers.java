package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;

public class ScreenPlayers implements Screen {
    private final VolleyBall iv;
    private final Texture imgBackGround;
    private final Texture imgBack;
    private final ImageButton btnBack;
    private final TextButton btnName1;
    private final TextButton btnName2;
    private final TextButton btnTypePlayer1;
    private final TextButton btnTypePlayer2;

    private final InputKeyboard inputKeyboard;
    private boolean isEnterName1;
    private boolean isEnterName2;

    public ScreenPlayers(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("screenbgplayers.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.6f, 0.6f);
        btnName1 = new TextButton(iv.fontNormal, iv.player1.name, 100, 500);
        btnName2 = new TextButton(iv.fontNormal, iv.player2.name, 0, 500);
        btnName2.setXY(SCR_WIDTH*100-100-btnName2.width, 500);
        if(iv.player1.isAi) {
            btnTypePlayer1 = new TextButton(iv.fontNormal, iv.text.get("COMPUTER")[iv.lang], 100, 400);
        } else {
            btnTypePlayer1 = new TextButton(iv.fontNormal, iv.text.get("HUMAN")[iv.lang], 100, 400);
        }
        if(iv.player2.isAi) {
            btnTypePlayer2 = new TextButton(iv.fontNormal, iv.text.get("COMPUTER")[iv.lang], 0, 400);
        } else {
            btnTypePlayer2 = new TextButton(iv.fontNormal, iv.text.get("HUMAN")[iv.lang], 0, 400);
        }
        btnTypePlayer2.setXY(SCR_WIDTH*100-100-btnTypePlayer2.width, 400);

        inputKeyboard = new InputKeyboard(SCR_WIDTH, SCR_HEIGHT, 8);
    }

    @Override
    public void show() {
        iv.sleep();
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
    }

    @Override
    public void render(float delta) {
        // обработка касаний экрана
        if (Gdx.input.justTouched()) {
            iv.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            iv.camera.unproject(iv.touch);

            if (btnBack.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.getScreenSettings());
            }
            if (btnName1.hit(iv.touch.x, iv.touch.y)) {
                isEnterName1 = true;
            }
            if(isEnterName1 && inputKeyboard.endOfEdit(iv.touch.x, iv.touch.y)){
                iv.player1.name = inputKeyboard.getText();
                btnName1.setText(iv.player1.name, false);
                isEnterName1 = false;
                iv.player1.insertRecord(iv.players);
                Player.sortTableOfRecords(iv.players);
            }
            if (btnName2.hit(iv.touch.x, iv.touch.y)) {
                isEnterName2 = true;
            }
            if(isEnterName2 && inputKeyboard.endOfEdit(iv.touch.x, iv.touch.y)){
                iv.player2.name = inputKeyboard.getText();
                btnName2.setText(iv.player2.name, false);
                btnName2.setXY(SCR_WIDTH*100-100-btnName2.width, 500);
                isEnterName2 = false;
                iv.player2.insertRecord(iv.players);
                Player.sortTableOfRecords(iv.players);
            }
            if(btnTypePlayer1.hit(iv.touch.x, iv.touch.y)) {
                iv.player1.isAi = !iv.player1.isAi;
                if(iv.player1.isAi) {
                    btnTypePlayer1.setText(iv.text.get("COMPUTER")[iv.lang], false);
                } else {
                    btnTypePlayer1.setText(iv.text.get("HUMAN")[iv.lang], false);
                }
            }
            if(btnTypePlayer2.hit(iv.touch.x, iv.touch.y)) {
                iv.player2.isAi = !iv.player2.isAi;
                if(iv.player2.isAi) {
                    btnTypePlayer2.setText(iv.text.get("COMPUTER")[iv.lang], false);
                } else {
                    btnTypePlayer2.setText(iv.text.get("HUMAN")[iv.lang], false);
                }
                btnTypePlayer2.setXY(SCR_WIDTH*100-100-btnTypePlayer2.width, 400);
            }
        }

        // отрисовка всей графики
        iv.camera.update();
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        iv.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        iv.batch.end();

        iv.batch.setProjectionMatrix(iv.camera2.combined);
        iv.batch.begin();
        iv.fontNormal.draw(iv.batch, iv.text.get("PLAYER 1")[iv.lang], 100, 600, SCR_WIDTH*100-100, Align.left, false);
        iv.fontNormal.draw(iv.batch, iv.text.get("PLAYER 2")[iv.lang], 0, 600, SCR_WIDTH*100-100, Align.right, false);
        btnName1.font.draw(iv.batch, btnName1.text, btnName1.x, btnName1.y);
        btnName2.font.draw(iv.batch, btnName2.text, btnName2.x, btnName2.y);
        iv.fontNormal.draw(iv.batch, iv.text.get("VS")[iv.lang], 0, btnTypePlayer1.y, SCR_WIDTH*100, Align.center, false);
        btnTypePlayer1.font.draw(iv.batch, btnTypePlayer1.text, btnTypePlayer1.x, btnTypePlayer1.y);
        btnTypePlayer2.font.draw(iv.batch, btnTypePlayer2.text, btnTypePlayer2.x, btnTypePlayer2.y);
        if(isEnterName1 || isEnterName2){
            inputKeyboard.draw(iv.batch);
        }
        iv.fontTitle.draw(iv.batch, iv.text.get("PLAYERS")[iv.lang], 20, SCR_HEIGHT*100-20);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
        imgBack.dispose();
        inputKeyboard.dispose();
    }
}
