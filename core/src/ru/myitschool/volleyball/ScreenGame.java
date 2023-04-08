package ru.myitschool.volleyball;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenGame implements Screen {
    MyGdx gdx;
    Texture imgBackGround;
    StaticBodyBox[] block = new StaticBodyBox[3];
    StaticBodyBox net;

    public ScreenGame(MyGdx myGdx) {
        gdx = myGdx;
    }

    @Override
    public void show() {
        block[0] = new StaticBodyBox(gdx.world, MyGdx.SCR_WIDTH / 2, 0, MyGdx.SCR_WIDTH, 0.3f);
        block[1] = new StaticBodyBox(gdx.world, 0, MyGdx.SCR_HEIGHT / 2, 0.3f, MyGdx.SCR_HEIGHT);
        block[2] = new StaticBodyBox(gdx.world, MyGdx.SCR_WIDTH, MyGdx.SCR_HEIGHT / 2, 0.3f, MyGdx.SCR_HEIGHT);
        net = new StaticBodyBox(gdx.world, MyGdx.SCR_WIDTH / 2, 1f, 0.15f, 10.5f);
    }

    @Override
    public void render(float delta) {
        gdx.camera.update();
        gdx.world.step(1 / 60f, 6, 2);
        ScreenUtils.clear(0, 0, 0, 1);
        gdx.debugRenderer.render(gdx.world, gdx.camera.combined);
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
