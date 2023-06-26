package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

public class ScreenPlayers implements Screen {
    private VolleyBall iv;
    private Texture imgBackGround;
    private ImageButton btnBack;
    private TextButton btnName1, btnName2;
    private TextButton btnTypePlayer1, btnTypePlayer2;

    private Texture imgBack;
    private int count_players = 1;
    private ImageButton[] btnPlayer = new ImageButton[count_players];
    private Texture[] imgBtnPlayer = new Texture[count_players];


    public ScreenPlayers(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("background.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.6f, 0.6f);
        btnName1 = new TextButton(iv.font, "Noname", 100, 500);
        btnName2 = new TextButton(iv.font, "Noname", 0, 500);
        btnTypePlayer1 = new TextButton(iv.font, "Human", 100, 400);
        btnTypePlayer2 = new TextButton(iv.font, "Human", 0, 400);
        for (int i = 0; i < count_players; i++) {
            imgBtnPlayer[i] = new Texture("person" + i + ".png");
        }
        for (int i = 0; i < count_players; i++) {
            btnPlayer[i] = new ImageButton(imgBtnPlayer[i], 1 + 3.5f * (i % 3), SCR_HEIGHT - 3 * (i / 3 + 1), 2.5f, 2.5f);
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
        iv.font.draw(iv.batch, "Player1", 100, 600, SCR_WIDTH*100-100, Align.left, false);
        iv.font.draw(iv.batch, "Player2", 0, 600, SCR_WIDTH*100-100, Align.right, false);
        btnName1.font.draw(iv.batch, btnName1.text, btnName1.x, btnName1.y, SCR_WIDTH*100-100, Align.left, false);
        btnName2.font.draw(iv.batch, btnName2.text, btnName2.x, btnName2.y, SCR_WIDTH*100-100, Align.right, false);
        iv.font.draw(iv.batch, "vs", 0, btnTypePlayer1.y, SCR_WIDTH*100, Align.center, false);
        btnTypePlayer1.font.draw(iv.batch, btnTypePlayer1.text, btnTypePlayer1.x, btnTypePlayer1.y, SCR_WIDTH*100-100, Align.left, false);
        btnTypePlayer2.font.draw(iv.batch, btnTypePlayer2.text, btnTypePlayer2.x, btnTypePlayer2.y, SCR_WIDTH*100-100, Align.right, false);
        iv.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        //
    }

    @Override
    public void pause() {
        //
    }

    @Override
    public void resume() {
        //
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
        imgBack.dispose();
    }
}
