package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenRecords implements Screen {
    private final VolleyBall iv;
    private final Texture imgBackGround;
    private final ImageButton btnBack;
    private Texture imgBack;
    private String textAbout = "";

    public ScreenRecords(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("screenbgrecords.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.6f, 0.6f);
    }

    @Override
    public void show() {
        Player.loadTableOfRecords(iv.players);
    }

    @Override
    public void render(float delta) {
        // обработка касаний экрана
        if (Gdx.input.justTouched()) {
            iv.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            iv.camera.unproject(iv.touch);

            if (btnBack.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.getScreenIntro());
            }
        }

        // события

        // отрисовка всей графики
        iv.camera.update();
        iv.batch.setProjectionMatrix(iv.camera2.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH * 100, SCR_HEIGHT * 100);
        iv.fontNormal.draw(iv.batch, "Best Players", 200, 600);
        iv.fontSmall.draw(iv.batch, Player.tableOfRecordsToString(iv.players, iv.fontSmall), 200, 500);
        iv.batch.end();
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
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

    }

    @Override
    public void dispose() {

    }
}
