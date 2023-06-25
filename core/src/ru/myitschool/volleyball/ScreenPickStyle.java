package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ScreenPickStyle implements Screen {
    private VolleyBall iv;
    private Texture imgBackGround;
    private Texture imgBack;
    private Texture imgSelector;
    private Texture[] imgBtnBackground = new Texture[NUM_STYLES];

    private ImageButton btnBack;
    private final ImageButton[] btnBackground = new ImageButton[NUM_STYLES];

    public ScreenPickStyle(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("background.jpg");
        imgBack = new Texture("back.png");
        imgSelector = new Texture("yellowselector.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.6f, 0.6f);
        for (int i = 0; i < NUM_STYLES; i++) {
            imgBtnBackground[i] = new Texture("background" + i + ".jpg");
        }
        for (int i = 0; i < NUM_STYLES; i++) {
            btnBackground[i] = new ImageButton(imgBtnBackground[i], 1 + 4 * (i % 3), SCR_HEIGHT - 3 * (i / 3 + 1), 3.5f, 2f);
        }
    }

    @Override
    public void show() {
        iv.sleep();
    }

    @Override
    public void render(float delta) {
        // обработка касаний экрана
        if (Gdx.input.justTouched()) {
            iv.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            iv.camera.unproject(iv.touch);

            if (btnBack.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.getScreenSettings());
            }

            for (int i = 0; i < NUM_STYLES; i++) {
                if (btnBackground[i].hit(iv.touch.x, iv.touch.y)) {
                    iv.gameStyle = i;
                    break;
                }
            }
        }

        // отрисовка всей графики
        iv.camera.update();
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        iv.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        iv.batch.draw(imgSelector, btnBackground[iv.gameStyle].x - 0.1f, btnBackground[iv.gameStyle].y - 0.1f, 3.7f, 2.2f);
        for (int i = 0; i < NUM_STYLES; i++) {
            iv.batch.draw(btnBackground[i].img, btnBackground[i].x, btnBackground[i].y, btnBackground[i].width, btnBackground[i].height);
        }
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

    }
}
