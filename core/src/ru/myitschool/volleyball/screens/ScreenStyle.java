package ru.myitschool.volleyball.screens;

import static ru.myitschool.volleyball.VolleyBall.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

import ru.myitschool.volleyball.components.ImageButton;
import ru.myitschool.volleyball.VolleyBall;


public class ScreenStyle implements Screen {
    private final VolleyBall iv;

    private final Texture imgBackGround;
    private final Texture imgBack;
    private final Texture imgSelector;
    private final Texture[] imgBtnBackground = new Texture[NUM_STYLES];

    private final ImageButton btnBack;
    private final ImageButton[] btnBackground = new ImageButton[NUM_STYLES];

    public ScreenStyle(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("screenbgbackgrounds.jpg");
        imgBack = new Texture("back.png");
        imgSelector = new Texture("yellowselector.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 0.8f, SCR_HEIGHT - 0.8f, 0.6f, 0.6f);
        for (int i = 0; i < NUM_STYLES; i++) {
            imgBtnBackground[i] = new Texture("background" + i + ".jpg");
        }
        for (int i = 0; i < NUM_STYLES; i++) {
            btnBackground[i] = new ImageButton(imgBtnBackground[i], 0.7f + 4 * (i % 3), SCR_HEIGHT - 3 * (i / 3 + 1), 3.5f, 2f);
        }
    }

    @Override
    public void show() {
        iv.sleep();
    }

    @Override
    public void render(float delta) {
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
        iv.batch.setProjectionMatrix(iv.cameraForText.combined);
        iv.batch.begin();
        iv.fontTitle.draw(iv.batch, iv.myBundle.get("style"), 20, SCR_HEIGHT*100-20);
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
        imgBack.dispose();
        imgBackGround.dispose();
        imgSelector.dispose();
        for (int i = 0; i < imgBtnBackground.length; i++) {
            imgBtnBackground[i].dispose();
        }
    }
}
