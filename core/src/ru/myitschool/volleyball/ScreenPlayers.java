package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_HEIGHT;
import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;
import static ru.myitschool.volleyball.MyGdx.number_players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenPlayers implements Screen {
    MyGdx gdx;
    Texture imgBackGround;
    ImageButton btnBack;
    Texture imgBack;
    int count_players = 1;
    ImageButton[] btnPlayer = new ImageButton[count_players];
    Texture[] imgBtnPlayer = new Texture[count_players];

    public ScreenPlayers(MyGdx myGdx) {
        gdx = myGdx;
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

            for (int i = 0; i < count_players; i++) {
                if(btnPlayer[i].hit(gdx.touch.x, gdx.touch.y)){
                    number_players = i;
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
        for (int i = 0; i < count_players; i++) {
            gdx.batch.draw(btnPlayer[i].img, btnPlayer[i].x, btnPlayer[i].y, btnPlayer[i].width, btnPlayer[i].height);
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
