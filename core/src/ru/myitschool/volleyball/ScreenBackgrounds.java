package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;
import static ru.myitschool.volleyball.VolleyBall.number_background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ScreenBackgrounds implements Screen {
    private final VolleyBall iv;
    private final Texture imgBackGround;
    private final ImageButton btnBack;
    private final Texture imgBack;
    private final int count_backgrounds = 5;
    private final ImageButton[] btnBackground = new ImageButton[count_backgrounds];
    private Texture[] imgBtnBackground = new Texture[count_backgrounds];
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public ScreenBackgrounds(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("background.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH-1, SCR_HEIGHT-0.9f, 0.7f, 0.7f);
        for (int i = 0; i < count_backgrounds; i++) {
            imgBtnBackground[i] = new Texture("background"+i+".jpg");
        }
        for (int i = 0; i < count_backgrounds; i++) {
            btnBackground[i] = new ImageButton(imgBtnBackground[i], 1+4*(i%3), SCR_HEIGHT-3*(i/3+1), 3.5f, 2);
        }
    }

    @Override
    public void show() {
        iv.sleep();
    }

    @Override
    public void render(float delta) {
        // обработка касаний экрана
        if(Gdx.input.justTouched()) {
            iv.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            iv.camera.unproject(iv.touch);

            if(btnBack.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.screenSettings);
            }

            for (int i = 0; i < count_backgrounds; i++) {
                if(btnBackground[i].hit(iv.touch.x, iv.touch.y)){
                    number_background = i;
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
        iv.batch.end();
        for (int i = 0; i < count_backgrounds; i++) {
            if (i == number_background){
                shapeRenderer.setProjectionMatrix(iv.camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.YELLOW);
                shapeRenderer.rect(btnBackground[number_background].x-0.1f, btnBackground[number_background].y - 0.1f, 3.7f, 2.2f);
                shapeRenderer.end();
            }
        }
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        for (int i = 0; i < count_backgrounds; i++) {
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
