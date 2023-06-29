package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

/**
 * класс с описанием игры
 */
public class ScreenAbout implements Screen {
    private final VolleyBall iv;

    private final Texture imgBackGround;
    private final ImageButton btnBack;
    private final Texture imgBack;

    public ScreenAbout(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("screenbgabout.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.6f, 0.6f);
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

            if (btnBack.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.getScreenIntro());
            }
        }

        // события

        // отрисовка всей графики
        iv.camera.update();
        // рисуем изображения
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        iv.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        iv.batch.end();
        // рисуем тексты
        iv.batch.setProjectionMatrix(iv.cameraForText.combined);
        iv.batch.begin();
        iv.fontSmall.draw(iv.batch, iv.text.get("ABOUTTEXT")[iv.lang], 150, 600);
        iv.fontTitle.draw(iv.batch, iv.text.get("ABOUT")[iv.lang], 20, SCR_HEIGHT*100-20);
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

