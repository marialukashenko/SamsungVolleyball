package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;
import static ru.myitschool.volleyball.MyGdx.TYPE_BALL;
import static ru.myitschool.volleyball.MyGdx.TYPE_PERS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import org.graalvm.compiler.loop.MathUtil;

public class ScreenGame implements Screen {

    MyGdx gdx;
    Texture imgBackGround;
    StaticBodyBox[] block = new StaticBodyBox[3];
    StaticBodyBox net;
    DynamicBodyBall person1, person2;
    DynamicBodyBall ball;

    public ScreenGame(MyGdx myGdx) {
        gdx = myGdx;
    }

    @Override
    public void show() {
        block[0] = new StaticBodyBox(gdx.world, SCR_WIDTH / 2, 0, SCR_WIDTH, 0.3f);
        block[1] = new StaticBodyBox(gdx.world, 0, MyGdx.SCR_HEIGHT / 2, 0.3f, MyGdx.SCR_HEIGHT*3);
        block[2] = new StaticBodyBox(gdx.world, SCR_WIDTH, MyGdx.SCR_HEIGHT / 2, 0.3f, MyGdx.SCR_HEIGHT*3);
        net = new StaticBodyBox(gdx.world, SCR_WIDTH / 2, 1f, 0.2f, 8.5f);
        ball = new DynamicBodyBall(gdx.world, SCR_WIDTH/2+ (MathUtils.randomBoolean()?0.7f:-0.7f), MyGdx.SCR_HEIGHT, 0.4f, TYPE_BALL);
        //person1 = new KinematicBodyPerson(gdx.world, SCR_WIDTH/4, 0.65f, 0.5f, LEFT);
        //person2 = new KinematicBodyPerson(gdx.world, SCR_WIDTH/4*3, 0.65f, 0.5f, RIGHT);
        person1 = new DynamicBodyBall(gdx.world, SCR_WIDTH/4, 0.65f, 0.5f, TYPE_PERS);
        person2 = new DynamicBodyBall(gdx.world, SCR_WIDTH/4*3, 0.65f, 0.5f, TYPE_PERS);
    }

    @Override
    public void render(float delta) {
        gdx.camera.update();
        gdx.world.step(1 / 60f, 6, 2);
        ScreenUtils.clear(0, 0, 0, 1);
        gdx.debugRenderer.render(gdx.world, gdx.camera.combined);
        if(Gdx.input.isTouched(0)) {
            gdx.touch.set(Gdx.input.getX(0), Gdx.input.getY(0), 0);
            gdx.camera.unproject(gdx.touch);
            if(gdx.touch.x < SCR_WIDTH/2){
                person1.move(gdx.touch.x, gdx.touch.y);
                person1.hit(gdx.touch.x, gdx.touch.y);
            }
            else{
                person2.move(gdx.touch.x, gdx.touch.y);
                person2.hit(gdx.touch.x, gdx.touch.y);
            }
        }
        if(Gdx.input.isTouched(1)) {
            gdx.touch.set(Gdx.input.getX(1), Gdx.input.getY(1), 0);
            gdx.camera.unproject(gdx.touch);
            if(gdx.touch.x < SCR_WIDTH/2){
                person1.move(gdx.touch.x, gdx.touch.y);
                person1.hit(gdx.touch.x, gdx.touch.y);
            }
            else{
                person2.move(gdx.touch.x, gdx.touch.y);
                person2.hit(gdx.touch.x, gdx.touch.y);
            }
        }

        // события
        //person1.move();
        //person2.move();
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
