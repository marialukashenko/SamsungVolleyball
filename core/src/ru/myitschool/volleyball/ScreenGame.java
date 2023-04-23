package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_HEIGHT;
import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import org.graalvm.compiler.loop.MathUtil;

public class ScreenGame implements Screen {

    MyGdx gdx;
    Texture imgBackGround;
    StaticBodyBox[] block = new StaticBodyBox[3];
    StaticBodyBox net;
    DynamicBodyPlayer person1, person2;
    DynamicBodyBall ball;
    int countGoals_1, countGoals_2;
    long timeGoal, timeInterval=3000;
    boolean isGoal;
    TextButton btnBack;

    public ScreenGame(MyGdx myGdx) {
        gdx = myGdx;
        btnBack = new TextButton(gdx.fontLarge, "BACK", SCR_WIDTH*100-200, SCR_HEIGHT*100-30);
        //игровое поле и сетки
        block[0] = new StaticBodyBox(gdx.world, SCR_WIDTH / 2, 0, SCR_WIDTH, 0.3f);
        block[1] = new StaticBodyBox(gdx.world, 0, MyGdx.SCR_HEIGHT / 2, 0.3f, 1000);
        block[2] = new StaticBodyBox(gdx.world, SCR_WIDTH, MyGdx.SCR_HEIGHT / 2, 0.3f, 1000);
        net = new StaticBodyBox(gdx.world, SCR_WIDTH / 2, 1f, 0.2f, 7.23f);

        //задание тел
        ball = new DynamicBodyBall(gdx.world, SCR_WIDTH/2+ (MathUtils.randomBoolean()?0.7f:-0.7f), MyGdx.SCR_HEIGHT, 0.4f);
        person1 = new DynamicBodyPlayer(gdx.world, SCR_WIDTH/4, 0.65f, 0.5f);
        person2 = new DynamicBodyPlayer(gdx.world, SCR_WIDTH/4*3, 0.65f, 0.5f);
    }

    @Override
    public void show() {
        isGoal = false;
        countGoals_2 = 0;
        countGoals_1 = 0;
        ball.body.setLinearVelocity(0, 0);
        person1.body.setLinearVelocity(0, 0);
        person2.body.setLinearVelocity(0, 0);
        ball.body.setAngularVelocity(0);
        person1.body.setAngularVelocity(0);
        person2.body.setAngularVelocity(0);
        ball.body.setTransform(SCR_WIDTH/2+ (MathUtils.randomBoolean()?0.7f:-0.7f), MyGdx.SCR_HEIGHT, 0);
        person1.body.setTransform(SCR_WIDTH/4, 0.65f, 0);
        person2.body.setTransform(SCR_WIDTH/4*3, 0.65f, 0);
    }

    @Override
    public void render(float delta) {
        gdx.camera.update();
        gdx.world.step(1 / 60f, 6, 2);
        ScreenUtils.clear(0, 0, 0, 1);
        gdx.debugRenderer.render(gdx.world, gdx.camera.combined);

        if(btnBack.hit(gdx.touch.x, gdx.touch.y)) {
            gdx.setScreen(gdx.screenIntro);

        }

        if(Gdx.input.isTouched(0)) {
            gdx.touch.set(Gdx.input.getX(0), Gdx.input.getY(0), 0);
            gdx.camera.unproject(gdx.touch);
            touchScreen();
        }
        if(Gdx.input.isTouched(1)) {
            gdx.touch.set(Gdx.input.getX(1), Gdx.input.getY(1), 0);
            gdx.camera.unproject(gdx.touch);
            touchScreen();
        }

        // события
        person1.checkLevel();
        person2.checkLevel();
        if(isGoal) {
            if(TimeUtils.millis()>timeGoal+timeInterval){
                isGoal = false;
                if (ball.getX() < SCR_WIDTH / 2) {
                    gdx.world.destroyBody(ball.body);
                    ball = new DynamicBodyBall(gdx.world, SCR_WIDTH/4*3, MyGdx.SCR_HEIGHT, 0.4f);
                    //ball.body.setTransform((SCR_WIDTH/2+MathUtils.random(1, 5)), MyGdx.SCR_HEIGHT + 1, 0);
                }
                else{
                    gdx.world.destroyBody(ball.body);
                    ball = new DynamicBodyBall(gdx.world, SCR_WIDTH/4, MyGdx.SCR_HEIGHT, 0.4f);
                    //ball.body.setTransform(MathUtils.random(1, 5), MyGdx.SCR_HEIGHT + 1, 0);
                }
                //ball.body.setTransform(SCR_WIDTH/2+ (MathUtils.randomBoolean()?0.7f:-0.7f), MyGdx.SCR_HEIGHT, 0);
            }
        } else {
            if (ball.isGoal()) {
                isGoal = true;
                timeGoal = TimeUtils.millis();
                if (ball.getX() < SCR_WIDTH / 2) {
                    countGoals_2++;
                } else {
                    countGoals_1++;
                }
            }
        }

        // отрисовка
        gdx.batch.setProjectionMatrix(gdx.camera2.combined);
        gdx.batch.begin();
        btnBack.font.draw(gdx.batch, btnBack.text, btnBack.x, btnBack.y);
        gdx.font.draw(gdx.batch, ":", 0, SCR_HEIGHT*100-40, SCR_WIDTH*100, Align.center, true);
        gdx.font.draw(gdx.batch, countGoals_1+"", 0, SCR_HEIGHT*100-40, SCR_WIDTH*100/2-50, Align.right, true);
        gdx.font.draw(gdx.batch, countGoals_2+"", SCR_WIDTH*100/2+50, SCR_HEIGHT*100-40, SCR_WIDTH*100/2-50, Align.left, true);
        if(isGoal){
            gdx.fontLarge.draw(gdx.batch, "ГОЛ!", 0, SCR_HEIGHT*100/2, SCR_WIDTH*100, Align.center, true);
        }
        gdx.batch.end();
    }


    void touchScreen() {
        if(gdx.touch.x < SCR_WIDTH/2){
            if(!person1.isJumping) {
                person1.move(gdx.touch.x, gdx.touch.y);
            }
        } else {
            if(!person2.isJumping) {
                person2.move(gdx.touch.x, gdx.touch.y);
            }
        }
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
