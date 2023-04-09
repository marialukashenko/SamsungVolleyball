package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_HEIGHT;
import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenIntro implements Screen {
    MyGdx gdx;
    Texture imgBackGround;
    TextButton btnPlay, btnSettings, btnAbout, btnExit;

    public ScreenIntro(MyGdx myGdx) {
        gdx = myGdx;
        imgBackGround = new Texture("background.jpg");
        btnPlay = new TextButton(gdx.fontLarge, "PLAY", 550);
        btnSettings = new TextButton(gdx.fontLarge, "SETTINGS", 450);
        btnAbout = new TextButton(gdx.fontLarge, "ABOUT", 350);
        btnExit = new TextButton(gdx.fontLarge, "EXIT", 250);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // обработка касаний экрана
        if(Gdx.input.justTouched()) {
            gdx.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gdx.camera.unproject(gdx.touch);
            if(btnPlay.hit(gdx.touch.x, gdx.touch.y)) {
                gdx.setScreen(gdx.screenGame);
            }
            if(btnSettings.hit(gdx.touch.x, gdx.touch.y)) {
                gdx.setScreen(gdx.screenSettings);
            }
            if(btnAbout.hit(gdx.touch.x, gdx.touch.y)) {
                gdx.setScreen(gdx.screenAbout);
            }
            if(btnExit.hit(gdx.touch.x, gdx.touch.y)) {
                Gdx.app.exit();
            }
        }

        // отрисовка всей графики
        gdx.camera.update();
        gdx.batch.setProjectionMatrix(gdx.camera.combined);
        gdx.batch.begin();
        gdx.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        gdx.batch.end();
        gdx.batch.setProjectionMatrix(gdx.camera2.combined);
        gdx.batch.begin();
        btnPlay.font.draw(gdx.batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(gdx.batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnAbout.font.draw(gdx.batch, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(gdx.batch, btnExit.text, btnExit.x, btnExit.y);
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

