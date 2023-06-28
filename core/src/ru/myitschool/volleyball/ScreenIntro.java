package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class ScreenIntro implements Screen {
    private VolleyBall iv;
    private Texture imgBackGround;
    private TextButton btnPlay;
    private TextButton btnSettings;
    private TextButton btnRecords;
    private TextButton btnAbout;
    private TextButton btnExit;

    public ScreenIntro(VolleyBall volleyBall) {
        iv = volleyBall;

        imgBackGround = new Texture("screenbgintro.jpg");
        btnPlay = new TextButton(iv.fontLarge, iv.text.get("Play")[iv.lang], 550);
        btnSettings = new TextButton(iv.fontLarge, "SETTINGS", 450);
        btnRecords = new TextButton(iv.fontLarge, "BEST PLAYERS", 350);
        btnAbout = new TextButton(iv.fontLarge, "ABOUT", 250);
        btnExit = new TextButton(iv.fontLarge, "EXIT", 150);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // обработка касаний экрана
        if (Gdx.input.justTouched()) {
            iv.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            iv.camera.unproject(iv.touch);
            if (btnPlay.hit(iv.touch.x, iv.touch.y)) {
                iv.sleep();
                iv.setScreen(iv.getScreenGame());
            }
            if (btnSettings.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.getScreenSettings());
            }
            if (btnRecords.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.getScreenRecords());
            }
            if (btnAbout.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.getScreenAbout());
            }
            if (btnExit.hit(iv.touch.x, iv.touch.y)) {
                Gdx.app.exit();
            }
        }

        // отрисовка всей графики
        iv.camera.update();
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        iv.batch.end();
        iv.batch.setProjectionMatrix(iv.camera2.combined);
        iv.batch.begin();
        btnPlay.font.draw(iv.batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(iv.batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnRecords.font.draw(iv.batch, btnRecords.text, btnRecords.x, btnRecords.y);
        btnAbout.font.draw(iv.batch, btnAbout.text, btnAbout.x, btnAbout.y);
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
}

