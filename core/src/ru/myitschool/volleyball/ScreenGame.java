package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_HEIGHT;
import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;


public class ScreenGame implements Screen {

    MyGdx gdx;
    Texture imgBackGround;
    Texture imgBall;
    Texture imgPerson1, imgPerson2;
    Texture imgShadow;
    Texture imgNet;
    StaticBodyBox[] block = new StaticBodyBox[4];
    StaticBodyBox net;
    DynamicBodyPlayer person1, person2;
    DynamicBodyBall ball;
    int countGoals_1, countGoals_2;
    long timeGoal, timeInterval=3000;
    boolean isGoal, isWin;
    float ballHeight = 2.6f;
    float netHeight = 6.02f;
    TextButton btnBack, btnRerun;
    boolean startGame =true;
    float floor = 0.3f/2;


    public ScreenGame(MyGdx myGdx) {
        imgBackGround = new Texture("background_beach.jpg");
        imgBall = new Texture("ball1.png");
        imgShadow = new Texture("shadow.png");
        imgNet = new Texture("net.png");
        gdx = myGdx;
        btnBack = new TextButton(gdx.fontLarge, "BACK", SCR_WIDTH*100-200, SCR_HEIGHT*100-30);
        btnRerun = new TextButton(gdx.fontLarge, "REPLAY", 20, SCR_HEIGHT*100-30);
        //игровое поле и сетки
        block[0] = new StaticBodyBox(gdx.world, SCR_WIDTH / 2, 0, SCR_WIDTH, 0.3f); // пол
        block[1] = new StaticBodyBox(gdx.world, 0, MyGdx.SCR_HEIGHT / 2, 0.3f, 1000);
        block[2] = new StaticBodyBox(gdx.world, SCR_WIDTH, MyGdx.SCR_HEIGHT / 2, 0.3f, 1000);
        block[3] = new StaticBodyBox(gdx.world, SCR_WIDTH / 2, SCR_HEIGHT+0.4f, SCR_WIDTH, 0.3f);
        net = new StaticBodyBox(gdx.world, SCR_WIDTH / 2, 1f, 0.2f, netHeight);

        //задание тел
        ball = new DynamicBodyBall(gdx.world, SCR_WIDTH/4+ (MathUtils.randomBoolean()?0:SCR_WIDTH/2), ballHeight, 0.4f);
        person1 = new DynamicBodyPlayer(gdx.world, SCR_WIDTH/4, 0.65f, 0.5f);
        person2 = new DynamicBodyPlayer(gdx.world, SCR_WIDTH/4*3, 0.65f, 0.5f);
    }

    @Override
    public void show() {
        isGoal = false;
        countGoals_2 = 0;
        countGoals_1 = 0;
        create();
    }

    @Override
    public void render(float delta) {
        gdx.camera.update();
        gdx.world.step(1 / 60f, 6, 2);
        ScreenUtils.clear(0, 0, 0, 1);
        gdx.debugRenderer.render(gdx.world, gdx.camera.combined);

        // касания
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
        if(btnBack.hit(gdx.touch.x, gdx.touch.y)) {
            if(isWin){
                isWin = false;
            }
            gdx.setScreen(gdx.screenIntro);
            startGame = true;
        }

        if (isWin && btnRerun.hit(gdx.touch.x, gdx.touch.y)){
            startGame();
        }


        // события
        person1.move();
        person2.move();
        if(startGame) {
            if(person1.overlap(ball) || person2.overlap(ball)) {
                startGame = false;
            }
            else{
                ball.body.setLinearVelocity(0, 0);
                ball.body.setTransform(ball.getX(), ballHeight, 0);
            }
        }
        if(isGoal && !isWin) {
            if(TimeUtils.millis()>timeGoal+timeInterval){
                isGoal = false;
                if (ball.getX() < SCR_WIDTH / 2) {
                    gdx.world.destroyBody(ball.body);
                    ball = new DynamicBodyBall(gdx.world, SCR_WIDTH/4*3, ballHeight, 0.4f);
                    startGame = true;
                    //ball.body.setTransform((SCR_WIDTH/2+MathUtils.random(1, 5)), MyGdx.SCR_HEIGHT + 1, 0);
                }
                else{
                    gdx.world.destroyBody(ball.body);
                    ball = new DynamicBodyBall(gdx.world, SCR_WIDTH/4, ballHeight, 0.4f);
                    startGame = true;
                    //ball.body.setTransform(MathUtils.random(1, 5), MyGdx.SCR_HEIGHT + 1, 0);
                }
                gdx.world.destroyBody(person1.body);
                person1 = new DynamicBodyPlayer(gdx.world, SCR_WIDTH/4, 0.65f, 0.5f);
                gdx.world.destroyBody(person2.body);
                person2 = new DynamicBodyPlayer(gdx.world, SCR_WIDTH/4*3, 0.65f, 0.5f);
                //ball.body.setTransform(SCR_WIDTH/2+ (MathUtils.randomBoolean()?0.7f:-0.7f), MyGdx.SCR_HEIGHT, 0);
            }
        } else {
            if (ball.isGoal() && !isWin) {
                isGoal = true;
                timeGoal = TimeUtils.millis();
                if (ball.getX() < SCR_WIDTH / 2) {
                    countGoals_2++;
                    if (countGoals_2 == 3){
                        isWin=true;
                    }
                } else {
                    countGoals_1++;
                    if (countGoals_1 == 3){
                        isWin=true;
                    }
                }
            }
        }

        // отрисовка
        gdx.camera.update();
        gdx.batch.setProjectionMatrix(gdx.camera.combined);
        gdx.batch.begin();
        gdx.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        gdx.batch.draw(imgNet, SCR_WIDTH / 2 - 0.1f, -netHeight/3, 0.2f, netHeight);
        gdx.batch.draw(imgShadow, ball.scrX(), floor-ball.height()/8, ball.width(), ball.height()/4);
        gdx.batch.draw(imgShadow, person1.scrX(), floor-ball.height()/8, person1.width(), person1.height()/4);
        gdx.batch.draw(imgShadow, person2.scrX(), floor-ball.height()/8, person2.width(), person2.height()/4);


        gdx.batch.draw(imgBall, ball.scrX(), ball.scrY(), ball.r, ball.r, ball.width(), ball.height(), 1, 1, ball.getRotation(), 0, 0, 591, 591, false, false);
        gdx.batch.draw(imgBall, person1.scrX(), person1.scrY(), person1.width(), person1.height());
        gdx.batch.draw(imgBall, person2.scrX(), person2.scrY(), person2.width(), person2.height());
        gdx.batch.end();
        gdx.batch.setProjectionMatrix(gdx.camera2.combined);
        gdx.batch.begin();
        btnBack.font.draw(gdx.batch, btnBack.text, btnBack.x, btnBack.y);
        gdx.font.draw(gdx.batch, ":", 0, SCR_HEIGHT*100-40, SCR_WIDTH*100, Align.center, true);
        gdx.font.draw(gdx.batch, countGoals_1+"", 0, SCR_HEIGHT*100-40, SCR_WIDTH*100/2-50, Align.right, true);
        gdx.font.draw(gdx.batch, countGoals_2+"", SCR_WIDTH*100/2+50, SCR_HEIGHT*100-40, SCR_WIDTH*100/2-50, Align.left, true);
        if(isGoal && !isWin){
            gdx.fontLarge.draw(gdx.batch, "ГОЛ!", 0, SCR_HEIGHT*100/2, SCR_WIDTH*100, Align.center, true);
        }
        if (isWin){
            String winner = countGoals_1>countGoals_2?"Выиграл левый чувак!":"Выиграл правый чувак!";
            gdx.fontLarge.draw(gdx.batch, winner, 0, SCR_HEIGHT*100/2, SCR_WIDTH*100, Align.center, true);
            btnRerun.font.draw(gdx.batch, btnRerun.text, btnRerun.x, btnRerun.y);
        }
        gdx.batch.end();
    }


    void touchScreen() {
        if(gdx.touch.x < SCR_WIDTH/2){
            if(!person1.isJumping) {
                person1.touch(gdx.touch.x, gdx.touch.y);
            }
        } else {
            if(!person2.isJumping) {
                person2.touch(gdx.touch.x, gdx.touch.y);
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

    void startGame(){
        isWin = false;
        isGoal = false;
        startGame = true;
        countGoals_1 = 0;
        countGoals_2 = 0;
        create();
        gdx.sleep();
    }

    void create(){
        ball.body.setLinearVelocity(0, 0);
        ball.body.setAngularVelocity(0);
        ball.body.setTransform(SCR_WIDTH/4+ (MathUtils.randomBoolean()?0:SCR_WIDTH/2), ballHeight, 0);
        person1.body.setLinearVelocity(0, 0);
        person1.body.setAngularVelocity(0);
        person1.body.setTransform(SCR_WIDTH/4, 0.65f, 0);
        person2.body.setLinearVelocity(0, 0);
        person2.body.setAngularVelocity(0);
        person2.body.setTransform(SCR_WIDTH/4*3, 0.65f, 0);
    }

}
