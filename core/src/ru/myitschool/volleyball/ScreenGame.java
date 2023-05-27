package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_HEIGHT;
import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;
import static ru.myitschool.volleyball.MyGdx.number_background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;


public class ScreenGame implements Screen {

    MyGdx gdx;
    Sound ball_hit, goal, win;
    Texture imgBackGround;
    Texture imgBall;
    Texture imgShadow;
    Texture imgNet;
    Texture imgBack;
    Texture imgPersonAtlas1, imgPersonAtlas2;
    TextureRegion[] imgPerson1 = new TextureRegion[20];
    TextureRegion[] imgPerson2 = new TextureRegion[20];

    StaticBodyBox[] block = new StaticBodyBox[4];
    StaticBodyBox net;
    DynamicBodyPlayer person1, person2;
    DynamicBodyBall ball;
    int countGoals_1, countGoals_2;
    long timeGoal, timeInterval = 3000;
    boolean isGoal, isWin;
    float ballHeight = 2.6f;
    float netHeight = 6.02f;
    ImageButton btnBack;
    TextButton btnRerun;
    boolean startGame = true;
    float floor = 0.3f / 2;
    long timeShowGame, timeStartGameInterval = 300;
    long timeSoundPlay, timeSoundInterval = 100;

    public ScreenGame(MyGdx myGdx) {
        imgBackGround = new Texture("background"+number_background+".jpg");
        imgBall = new Texture("ball2.png");
        imgShadow = new Texture("shadow.png");
        imgNet = new Texture("net1.png");
        imgBack = new Texture("back.png");

        imgPersonAtlas1 = new Texture("colobatlasbeach.png");
        for (int i = 0; i < imgPerson1.length/2; i++) {
            imgPerson1[i] = new TextureRegion(imgPersonAtlas1, i*250, 0, 250, 250);
            imgPerson1[i+imgPerson1.length/2] = new TextureRegion(imgPersonAtlas1, i*250, 250, 250, 250);
        }
        imgPersonAtlas2 = new Texture("colobatlasbeach2.png");
        for (int i = 0; i < imgPerson2.length/2; i++) {
            imgPerson2[i] = new TextureRegion(imgPersonAtlas2, i*250, 0, 250, 250);
            imgPerson2[i+imgPerson2.length/2] = new TextureRegion(imgPersonAtlas2, i*250, 250, 250, 250);
        }

        ball_hit = Gdx.audio.newSound(Gdx.files.internal("ball_hit.mp3"));
        goal = Gdx.audio.newSound(Gdx.files.internal("goal.mp3"));
        win = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
        gdx = myGdx;

        btnBack = new ImageButton(imgBack, SCR_WIDTH- 1, SCR_HEIGHT-0.9f, 0.7f, 0.7f);
        btnRerun = new TextButton(gdx.fontLarge, "REPLAY", 20, SCR_HEIGHT * 100 - 30);
        //игровое поле и сетки
        block[0] = new StaticBodyBox(gdx.world, SCR_WIDTH / 2, 0, SCR_WIDTH, 0.3f); // пол
        block[1] = new StaticBodyBox(gdx.world, 0, MyGdx.SCR_HEIGHT / 2, 0.3f, 1000);
        block[2] = new StaticBodyBox(gdx.world, SCR_WIDTH, MyGdx.SCR_HEIGHT / 2, 0.3f, 1000);
        block[3] = new StaticBodyBox(gdx.world, SCR_WIDTH / 2, SCR_HEIGHT + 0.4f, SCR_WIDTH, 0.3f);
        net = new StaticBodyBox(gdx.world, SCR_WIDTH / 2, 1f, 0.2f, netHeight);

        //задание тел
        ball = new DynamicBodyBall(gdx.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), ballHeight, 0.4f);
        person1 = new DynamicBodyPlayer(gdx.world, SCR_WIDTH / 4, 0.65f, 0.5f, DynamicBodyPlayer.LEFT);
        person2 = new DynamicBodyPlayer(gdx.world, SCR_WIDTH / 4 * 3, 0.65f, 0.5f, DynamicBodyPlayer.RIGHT);
    }

    @Override
    public void show() {
        create();
        timeShowGame = TimeUtils.millis();
    }

    @Override
    public void render(float delta) {
        if(timeShowGame+timeStartGameInterval > TimeUtils.millis()) return;

        gdx.camera.update();
        gdx.world.step(1 / 60f, 6, 2);
        ScreenUtils.clear(0, 0, 0, 1);
        gdx.debugRenderer.render(gdx.world, gdx.camera.combined);

        // касания

        // события
        person1.move();
        person2.move();
        if (startGame) {
            if (person1.overlap(ball) || person2.overlap(ball)) {
                startGame = false;
            } else {
                ball.body.setLinearVelocity(0, 0);
                ball.body.setTransform(ball.getX(), ballHeight, 0);
            }
        }
        if (isGoal && !isWin) {
            if (TimeUtils.millis() > timeGoal + timeInterval) {
                isGoal = false;
                if (ball.getX() < SCR_WIDTH / 2) {
                    gdx.world.destroyBody(ball.body);
                    ball = new DynamicBodyBall(gdx.world, SCR_WIDTH / 4 * 3, ballHeight, 0.4f);
                    startGame = true;
                    //ball.body.setTransform((SCR_WIDTH/2+MathUtils.random(1, 5)), MyGdx.SCR_HEIGHT + 1, 0);
                } else {
                    gdx.world.destroyBody(ball.body);
                    ball = new DynamicBodyBall(gdx.world, SCR_WIDTH / 4, ballHeight, 0.4f);
                    startGame = true;
                    //ball.body.setTransform(MathUtils.random(1, 5), MyGdx.SCR_HEIGHT + 1, 0);
                }
                gdx.world.destroyBody(person1.body);
                person1 = new DynamicBodyPlayer(gdx.world, SCR_WIDTH / 4, 0.65f, 0.5f, DynamicBodyPlayer.LEFT);
                gdx.world.destroyBody(person2.body);
                person2 = new DynamicBodyPlayer(gdx.world, SCR_WIDTH / 4 * 3, 0.65f, 0.5f, DynamicBodyPlayer.RIGHT);
                //ball.body.setTransform(SCR_WIDTH/2+ (MathUtils.randomBoolean()?0.7f:-0.7f), MyGdx.SCR_HEIGHT, 0);
            }
        } else {
            if (ball.isGoal() && !isWin) {
                isGoal = true;
                timeGoal = TimeUtils.millis();
                if (ball.getX() < SCR_WIDTH / 2) {
                    countGoals_2++;
                    if (countGoals_2 == 5) {
                        isWin = true;
                        if(gdx.soundOn) win.play();
                    }
                    else {
                        if(gdx.soundOn) goal.play();
                    }
                } else {
                    countGoals_1++;
                    if (countGoals_1 == 5) {
                        isWin = true;
                        if(gdx.soundOn) win.play();
                    }
                    else {
                        if(gdx.soundOn) goal.play();
                    }
                }
            } else{
                if ((person1.overlap(ball) || person2.overlap(ball)) && !isWin) {
                    if(timeSoundPlay+timeSoundInterval < TimeUtils.millis()) {
                        timeSoundPlay = TimeUtils.millis();
                        if (gdx.soundOn) ball_hit.play();
                    }
                }
            }
        }

        // отрисовка
        gdx.camera.update();
        gdx.batch.setProjectionMatrix(gdx.camera.combined);
        gdx.batch.begin();
        gdx.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        gdx.batch.draw(imgNet, SCR_WIDTH / 2 - 0.1f, -netHeight / 3, 0.2f, netHeight);
        gdx.batch.draw(imgShadow, ball.scrX(), floor - ball.height() / 8, ball.width(), ball.height() / 4);
        gdx.batch.draw(imgShadow, person1.scrX(), floor - ball.height() / 8, person1.width(), person1.height() / 4);
        gdx.batch.draw(imgShadow, person2.scrX(), floor - ball.height() / 8, person2.width(), person2.height() / 4);


        gdx.batch.draw(imgBall, ball.scrX(), ball.scrY(), ball.r, ball.r, ball.width(), ball.height(), 1, 1, ball.getRotation(), 0, 0, 591, 591, false, false);

        gdx.batch.draw(imgPerson1[person1.faza], person1.scrX()-0.25f, person1.scrY(), person1.width()*1.5f/2, person1.height()*1.5f/2,
                person1.width()*1.5f, person1.height()*1.5f, person1.isFlip?-1:1, 1, 0);
        gdx.batch.draw(imgPerson2[person2.faza], person2.scrX()-0.25f, person2.scrY(), person2.width()*1.5f/2, person2.height()*1.5f/2,
                person2.width()*1.5f, person2.height()*1.5f, person2.isFlip?-1:1, 1, 0);

        gdx.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        gdx.batch.end();
        gdx.batch.setProjectionMatrix(gdx.camera2.combined);
        gdx.batch.begin();
        gdx.font.draw(gdx.batch, ":", 0, SCR_HEIGHT * 100 - 40, SCR_WIDTH * 100, Align.center, true);
        gdx.font.draw(gdx.batch, countGoals_1 + "", 0, SCR_HEIGHT * 100 - 40, SCR_WIDTH * 100 / 2 - 50, Align.right, true);
        gdx.font.draw(gdx.batch, countGoals_2 + "", SCR_WIDTH * 100 / 2 + 50, SCR_HEIGHT * 100 - 40, SCR_WIDTH * 100 / 2 - 50, Align.left, true);
        if (isGoal && !isWin) {
            gdx.fontLarge.draw(gdx.batch, "ГОЛ!", 0, SCR_HEIGHT * 100 / 2, SCR_WIDTH * 100, Align.center, true);
        }
        if (isWin) {
            String winner = countGoals_1 > countGoals_2 ? "Выиграл левый чувак!" : "Выиграл правый чувак!";
            gdx.fontLarge.draw(gdx.batch, winner, 0, SCR_HEIGHT * 100 / 2, SCR_WIDTH * 100, Align.center, true);
            btnRerun.font.draw(gdx.batch, btnRerun.text, btnRerun.x, btnRerun.y);
        }
        gdx.batch.end();
    }


    void touchScreen(Vector3 touch) {
        if (touch.x < SCR_WIDTH / 2) {
            person1.touch(touch.x, touch.y);
        } else {
            person2.touch(touch.x, touch.y);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

    }

    void startGame() {
        isWin = false;
        startGame = true;
        create();
    }

    void create() {
        isGoal = false;
        countGoals_2 = 0;
        countGoals_1 = 0;
        MyInput myInput = new MyInput();
        Gdx.input.setInputProcessor(myInput);
        imgBackGround = new Texture("background"+number_background+".jpg");
        loadPersons(number_background);
        ball.body.setLinearVelocity(0, 0);
        ball.body.setAngularVelocity(0);
        ball.body.setTransform(SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), ballHeight, 0);
        person1.body.setLinearVelocity(0, 0);
        person1.body.setAngularVelocity(0);
        person1.body.setTransform(SCR_WIDTH / 4, 0.65f, 0);
        person2.body.setLinearVelocity(0, 0);
        person2.body.setAngularVelocity(0);
        person2.body.setTransform(SCR_WIDTH / 4 * 3, 0.65f, 0);
    }

    void loadPersons(int type) {
        gdx.screenGame.imgPersonAtlas1.dispose();
        gdx.screenGame.imgPersonAtlas2.dispose();
        if(type == 3) {
            gdx.screenGame.imgPersonAtlas1 = new Texture("colobatlasknight.png");
            gdx.screenGame.imgPersonAtlas2 = new Texture("colobatlasknight2.png");
        } else {
            gdx.screenGame.imgPersonAtlas1 = new Texture("colobatlasbeach.png");
            gdx.screenGame.imgPersonAtlas2 = new Texture("colobatlasbeach2.png");
        }
        for (int i = 0; i < gdx.screenGame.imgPerson1.length/2; i++) {
            gdx.screenGame.imgPerson1[i] = new TextureRegion(gdx.screenGame.imgPersonAtlas1, i*250, 0, 250, 250);
            gdx.screenGame.imgPerson1[i+gdx.screenGame.imgPerson1.length/2] = new TextureRegion(gdx.screenGame.imgPersonAtlas1, i*250, 250, 250, 250);
        }
        for (int i = 0; i < gdx.screenGame.imgPerson2.length/2; i++) {
            gdx.screenGame.imgPerson2[i] = new TextureRegion(gdx.screenGame.imgPersonAtlas2, i*250, 0, 250, 250);
            gdx.screenGame.imgPerson2[i+gdx.screenGame.imgPerson2.length/2] = new TextureRegion(gdx.screenGame.imgPersonAtlas2, i*250, 250, 250, 250);
        }
    }

    class MyInput implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            touchColobs(screenX, screenY, pointer);
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            gdx.touch.set(screenX, screenY, 0);
            gdx.camera.unproject(gdx.touch);

            if (btnBack.hit(gdx.touch.x, gdx.touch.y)) {
                if (isWin) {
                    isWin = false;
                }
                gdx.setScreen(gdx.screenIntro);
                startGame = true;
            }

            if (isWin && btnRerun.hit(gdx.touch.x, gdx.touch.y)) {
                startGame();
            }

            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            touchColobs(screenX, screenY, pointer);
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }

    void touchColobs(int screenX, int screenY, int pointer) {
        if (pointer == 0) {
            gdx.touch.set(screenX, screenY, 0);
            gdx.camera.unproject(gdx.touch);
            touchScreen(gdx.touch);
        }
        if (pointer == 1) {
            gdx.touch.set(screenX, screenY, 0);
            gdx.camera.unproject(gdx.touch);
            touchScreen(gdx.touch);
        }
    }
}
