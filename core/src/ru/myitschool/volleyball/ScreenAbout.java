package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_HEIGHT;
import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenAbout implements Screen {
    MyGdx gdx;
    Texture imgBackGround;
    TextButton btnBack;
    String textAbout =  "Эта супер-игра создана\n" +
            "в IT-школе Samsung\n" +
            "на java под Android.\n\n" +
            "Цель игры - победить\n" +
            "в волейбол.";

    public ScreenAbout(MyGdx myGdx) {
        gdx = myGdx;
        imgBackGround = new Texture("background.jpg");
        btnBack = new TextButton(gdx.fontLarge, "BACK", 100);
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

            if(btnBack.hit(gdx.touch.x, gdx.touch.y)) {
                gdx.setScreen(gdx.screenIntro);
            }
        }

        // события

        // отрисовка всей графики
        gdx.camera.update();
        gdx.batch.setProjectionMatrix(gdx.camera2.combined);
        gdx.batch.begin();
        gdx.batch.draw(imgBackGround, 0, 0, SCR_WIDTH*100, SCR_HEIGHT*100);
        gdx.font.draw(gdx.batch, textAbout, 400, 500);
        btnBack.font.draw(gdx.batch, btnBack.text, btnBack.x, btnBack.y);
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

    }
}

