package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    private VolleyBall iv;
    private Texture imgBackGround;
    private Texture imgBack;
    private TextButton btnMusic, btnSound, btnBackgrounds, btnPlayers;
    private ImageButton btnBack;

    public ScreenSettings(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("background.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH- 1, SCR_HEIGHT-0.9f, 0.7f, 0.7f);
        btnMusic = new TextButton(iv.fontLarge, "MUSIC", 500);
        btnSound = new TextButton(iv.fontLarge, "SOUND", 430);
        btnBackgrounds = new TextButton(iv.fontLarge, "BACKGROUNDS", 360);
        btnPlayers = new TextButton(iv.fontLarge, "PLAYERS", 290);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            iv.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            iv.camera.unproject(iv.touch);
            if (btnBack.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.screenIntro);
            }
            if (btnSound.hit(iv.touch.x, iv.touch.y)) {
                iv.soundOn = !iv.soundOn;
                btnSound.setText(iv.soundOn ? "SOUND ON" : "SOUND OFF");
            }
            if (btnMusic.hit(iv.touch.x, iv.touch.y)) {
                iv.musicOn = !iv.musicOn;
                btnMusic.setText(iv.musicOn ? "MUSIC ON" : "MUSIC OFF");
            }
            if (btnBackgrounds.hit(iv.touch.x, iv.touch.y)){
                iv.setScreen(iv.screenBackgrounds);
            }
            if (btnPlayers.hit(iv.touch.x, iv.touch.y)){
                iv.setScreen(iv.screenPlayers);
            }
        }

        //отрисовка
        iv.camera.update();
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        iv.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        iv.batch.end();
        iv.batch.setProjectionMatrix(iv.camera2.combined);
        iv.batch.begin();
        btnMusic.font.draw(iv.batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnSound.font.draw(iv.batch, btnSound.text, btnSound.x, btnSound.y);
        btnBackgrounds.font.draw(iv.batch, btnBackgrounds.text, btnBackgrounds.x, btnBackgrounds.y);
        btnPlayers.font.draw(iv.batch, btnPlayers.text, btnPlayers.x, btnPlayers.y);
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

