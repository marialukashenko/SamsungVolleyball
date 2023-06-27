package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;

public class ScreenPlayers implements Screen {
    private final VolleyBall iv;
    private final Texture imgBackGround;
    private final ImageButton btnBack;
    private final TextButton btnName1;
    private final TextButton btnName2;
    private final TextButton btnTypePlayer1;
    private final TextButton btnTypePlayer2;

    private final Texture imgBack;
    private final InputKeyboard inputKeyboard;
    private boolean isEnterName1;
    private boolean isEnterName2;


    public ScreenPlayers(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("background.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.6f, 0.6f);
        btnName1 = new TextButton(iv.font, iv.playerName1, 100, 500);
        btnName2 = new TextButton(iv.font, iv.playerName2, 0, 500);
        btnTypePlayer1 = new TextButton(iv.font, "Human", 100, 400);
        btnTypePlayer2 = new TextButton(iv.font, "Human", 0, 400);
        inputKeyboard = new InputKeyboard(SCR_WIDTH, SCR_HEIGHT, 13);
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
            if (btnName1.hit(iv.touch.x, iv.touch.y)) {
                isEnterName1 = true;
            }
            if(isEnterName1 && inputKeyboard.endOfEdit(iv.touch.x, iv.touch.y)){
                btnName1.text = iv.playerName1 = inputKeyboard.getText();
                isEnterName1 = false;
            }
            if (btnName2.hit(iv.touch.x, iv.touch.y)) {
                isEnterName2 = true;
            }
            if(isEnterName2 && inputKeyboard.endOfEdit(iv.touch.x, iv.touch.y)){
                btnName2.text = iv.playerName2 = inputKeyboard.getText();
                isEnterName2 = false;
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
        if(isEnterName1 || isEnterName2){
            inputKeyboard.draw(iv.batch);
        }
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
        inputKeyboard.dispose();
    }
}
