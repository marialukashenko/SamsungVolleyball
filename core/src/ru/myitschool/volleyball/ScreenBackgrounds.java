package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_HEIGHT;
import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;
import static ru.myitschool.volleyball.MyGdx.number_background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ScreenBackgrounds implements Screen {
    MyGdx gdx;
    Texture imgBackGround;
    ImageButton btnBack;
    Texture imgBack;
    int count_backgrounds = 5;
    ImageButton[] btnBackground = new ImageButton[count_backgrounds];
    Texture[] imgBtnBackground = new Texture[count_backgrounds];
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public ScreenBackgrounds(MyGdx myGdx) {
        gdx = myGdx;
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
        gdx.sleep();
    }

    @Override
    public void render(float delta) {
        // обработка касаний экрана
        if(Gdx.input.justTouched()) {
            gdx.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gdx.camera.unproject(gdx.touch);

            if(btnBack.hit(gdx.touch.x, gdx.touch.y)) {
                gdx.setScreen(gdx.screenSettings);
            }

            for (int i = 0; i < count_backgrounds; i++) {
                if(btnBackground[i].hit(gdx.touch.x, gdx.touch.y)){
                    number_background = i;
                    break;
                }
            }
        }

        // отрисовка всей графики
        gdx.camera.update();
        gdx.batch.setProjectionMatrix(gdx.camera.combined);
        gdx.batch.begin();
        gdx.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        gdx.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        gdx.batch.end();
        for (int i = 0; i < count_backgrounds; i++) {
            if (i == number_background){
                shapeRenderer.setProjectionMatrix(gdx.camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.YELLOW);
                shapeRenderer.rect(btnBackground[number_background].x-0.1f, btnBackground[number_background].y - 0.1f, 3.7f, 2.2f);
                shapeRenderer.end();
            }
        }
        gdx.batch.setProjectionMatrix(gdx.camera.combined);
        gdx.batch.begin();
        for (int i = 0; i < count_backgrounds; i++) {
            gdx.batch.draw(btnBackground[i].img, btnBackground[i].x, btnBackground[i].y, btnBackground[i].width, btnBackground[i].height);
        }
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

    }
}
