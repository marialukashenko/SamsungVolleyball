package ru.myitschool.volleyball;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenIntro implements Screen {
    MyGdx gdx;
    Texture imgBackGround;
    TextButton btnPlay, btnSettings, btnAbout, btnExit;

    public ScreenIntro(MyGdx myGdx) {
        gdx = myGdx;
        imgBackGround = new Texture("background.jpg");

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // отрисовка всей графики
        gdx.camera.update();
        gdx.batch.setProjectionMatrix(gdx.camera.combined);
        gdx.batch.begin();

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

