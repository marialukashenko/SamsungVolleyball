package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenChoose implements Screen {
    private VolleyBall iv;
    private Texture imgBackGround;
    private TextButton btnTwoPlayers, btnComputer;
    private Texture imgBack;
    private ImageButton btnBack;

    public ScreenChoose(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("background.jpg");
        btnTwoPlayers = new TextButton(iv.fontLarge, "TWO PLAYERS", 450);
        btnComputer = new TextButton(iv.fontLarge, "WITH COMPUTER", 350);
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.7f, 0.7f);
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
            if (btnTwoPlayers.hit(iv.touch.x, iv.touch.y)) {
                iv.sleep();
                iv.setScreen(iv.screenTwoPlayersGame);
            }
            if (btnComputer.hit(iv.touch.x, iv.touch.y)) {
                iv.sleep();
                iv.setScreen(iv.screenComputerGame);
            }
            if (btnBack.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.screenIntro);
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
        btnTwoPlayers.font.draw(iv.batch, btnTwoPlayers.text, btnTwoPlayers.x, btnTwoPlayers.y);
        btnComputer.font.draw(iv.batch, btnComputer.text, btnComputer.x, btnComputer.y);
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
