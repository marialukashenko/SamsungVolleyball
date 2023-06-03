package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_HEIGHT;
import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenChoose implements Screen {
    MyGdx gdx;
    Texture imgBackGround;
    TextButton btnTwoPlayers, btnComputer;
    Texture imgBack;
    ImageButton btnBack;

    public ScreenChoose(MyGdx myGdx) {
        gdx = myGdx;
        imgBackGround = new Texture("background.jpg");
        btnTwoPlayers = new TextButton(gdx.fontLarge, "TWO PLAYERS", 450);
        btnComputer = new TextButton(gdx.fontLarge, "WITH COMPUTER", 350);
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH- 1, SCR_HEIGHT-0.9f, 0.7f, 0.7f);
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
            if(btnTwoPlayers.hit(gdx.touch.x, gdx.touch.y)) {
                gdx.sleep();
                gdx.setScreen(gdx.screenTwoPlayersGame);
            }
            if(btnComputer.hit(gdx.touch.x, gdx.touch.y)) {
                gdx.sleep();
                gdx.setScreen(gdx.screenComputerGame);
            }
            if(btnBack.hit(gdx.touch.x, gdx.touch.y)) {
                gdx.setScreen(gdx.screenIntro);
            }
        }

        // отрисовка всей графики
        gdx.camera.update();
        gdx.batch.setProjectionMatrix(gdx.camera.combined);
        gdx.batch.begin();
        gdx.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        gdx.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        gdx.batch.end();
        gdx.batch.setProjectionMatrix(gdx.camera2.combined);
        gdx.batch.begin();
        btnTwoPlayers.font.draw(gdx.batch, btnTwoPlayers.text, btnTwoPlayers.x, btnTwoPlayers.y);
        btnComputer.font.draw(gdx.batch, btnComputer.text, btnComputer.x, btnComputer.y);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}
