package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

/**
 * экран с таблицей рекордов
 */

public class ScreenRecords implements Screen {
    private final VolleyBall iv;

    private final Texture imgBackGround;
    private final ImageButton btnBack;
    private final TextButton btnClearTable;
    private Texture imgBack;

    public ScreenRecords(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("screenbgrecords.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.6f, 0.6f);
        btnClearTable = new TextButton(iv.fontNormal, iv.text.get("CLEAR RECORDS")[iv.lang], 500, 50);
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
            if(btnClearTable.hit(iv.touch.x, iv.touch.y)) {
                Player.clearTableOfRecords(iv.players);
            }
        }

        // события

        // отрисовка всей графики
        iv.camera.update();
        // рисуем картинки
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        iv.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        iv.batch.end();
        // рисуем буквы
        iv.batch.setProjectionMatrix(iv.cameraForText.combined);
        iv.batch.begin();
        iv.fontTitle.draw(iv.batch, iv.text.get("BEST PLAYERS")[iv.lang], 20, SCR_HEIGHT*100-20);
        iv.fontNormal.draw(iv.batch, iv.text.get("BEST PLAYERS")[iv.lang], 300, 600);
        iv.fontSmall.draw(iv.batch, Player.tableOfRecordsToString(iv.players, iv.fontSmall), 300, 550);
        btnClearTable.font.draw(iv.batch, btnClearTable.text, btnClearTable.x, btnClearTable.y);
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
        imgBack.dispose();
        imgBackGround.dispose();
    }
}

