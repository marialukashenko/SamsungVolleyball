package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;
import static ru.myitschool.volleyball.VolleyBall.number_players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ScreenPlayers implements Screen {
    private VolleyBall iv;
    private Texture imgBackGround;
    private ImageButton btnBack;
    private Texture imgBack;
    private int count_players = 1;
    private ImageButton[] btnPlayer = new ImageButton[count_players];
    private Texture[] imgBtnPlayer = new Texture[count_players];
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public ScreenPlayers(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("background.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH-1, SCR_HEIGHT-0.9f, 0.7f, 0.7f);
        for (int i = 0; i < count_players; i++) {
            imgBtnPlayer[i] = new Texture("person"+i+".png");
        }
        for (int i = 0; i < count_players; i++) {
            btnPlayer[i] = new ImageButton(imgBtnPlayer[i], 1+3.5f*(i%3), SCR_HEIGHT-3*(i/3+1), 2.5f, 2.5f);
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

            for (int i = 0; i < count_players; i++) {
                if(btnPlayer[i].hit(iv.touch.x, iv.touch.y)){
                    number_players = i;
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
        for (int i = 0; i < count_players; i++) {
            if (i == number_players){
                shapeRenderer.setProjectionMatrix(iv.camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.YELLOW);
                shapeRenderer.rect(btnPlayer[number_players].x-0.1f, btnPlayer[number_players].y - 0.1f, 2.7f, 2.7f);
                shapeRenderer.end();
            }
        }
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        for (int i = 0; i < count_players; i++) {
            iv.batch.draw(btnPlayer[i].img, btnPlayer[i].x, btnPlayer[i].y, btnPlayer[i].width, btnPlayer[i].height);
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
