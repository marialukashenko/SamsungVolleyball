package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_HEIGHT;
import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    MyGdx gdx;
    Texture imgBackGround;
    Texture imgBack;
    TextButton btnMusic, btnSound, btnBackgrounds, btnPlayers;
    ImageButton btnBack;

    public ScreenSettings(MyGdx myGdx) {
        gdx = myGdx;
        imgBackGround = new Texture("background.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH- 1, SCR_HEIGHT-0.9f, 0.7f, 0.7f);
        btnMusic = new TextButton(gdx.fontLarge, "MUSIC", 500);
        btnSound = new TextButton(gdx.fontLarge, "SOUND", 430);
        btnBackgrounds = new TextButton(gdx.fontLarge, "BACKGROUNDS", 360);
        btnPlayers = new TextButton(gdx.fontLarge, "PLAYERS", 290);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            gdx.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gdx.camera.unproject(gdx.touch);
            if (btnBack.hit(gdx.touch.x, gdx.touch.y)) {
                gdx.setScreen(gdx.screenIntro);
            }
            if (btnSound.hit(gdx.touch.x, gdx.touch.y)) {
                gdx.soundOn = !gdx.soundOn;
                btnSound.setText(gdx.soundOn ? "SOUND ON" : "SOUND OFF");
            }
            if (btnMusic.hit(gdx.touch.x, gdx.touch.y)) {
                gdx.musicOn = !gdx.musicOn;
                btnMusic.setText(gdx.musicOn ? "MUSIC ON" : "MUSIC OFF");
            }
            if (btnBackgrounds.hit(gdx.touch.x, gdx.touch.y)){
                gdx.setScreen(gdx.screenBackgrounds);
            }
            if (btnPlayers.hit(gdx.touch.x, gdx.touch.y)){
                gdx.setScreen(gdx.screenPlayers);
            }
        }

        //отрисовка
        gdx.camera.update();
        gdx.batch.setProjectionMatrix(gdx.camera.combined);
        gdx.batch.begin();
        gdx.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        gdx.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        gdx.batch.end();
        gdx.batch.setProjectionMatrix(gdx.camera2.combined);
        gdx.batch.begin();
        btnMusic.font.draw(gdx.batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnSound.font.draw(gdx.batch, btnSound.text, btnSound.x, btnSound.y);
        btnBackgrounds.font.draw(gdx.batch, btnBackgrounds.text, btnBackgrounds.x, btnBackgrounds.y);
        btnPlayers.font.draw(gdx.batch, btnPlayers.text, btnPlayers.x, btnPlayers.y);
        gdx.batch.end();
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

    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}

