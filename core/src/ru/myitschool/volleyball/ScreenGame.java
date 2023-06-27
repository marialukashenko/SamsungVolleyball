package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private VolleyBall iv;
    private Sound soundBallHit, soundGoal, soundWin;
    private Texture imgBackGround;
    private Texture imgBall;
    private Texture imgShadow;
    private Texture imgNet;
    private Texture imgBack;
    public Texture imgPersonAtlas1, imgPersonAtlas2;
    public TextureRegion[] imgPerson1 = new TextureRegion[20];
    public TextureRegion[] imgPerson2 = new TextureRegion[20];

    private StaticBodyBox[] block = new StaticBodyBox[4];
    private StaticBodyBox net;
    private DynamicBodyPlayer person1, person2;
    private DynamicBodyBall ball;

    private int countGoals1, countGoals2;
    private long timeGoal, timeInterval = 3000;
    private boolean isGoal, isWin;
    private float ballStartY = 2.6f;
    private float netHeight = 4f;
    private ImageButton btnBack;
    private TextButton btnRerun;
    private boolean startGame = true;
    private float floor = 0.3f / 2;
    private long timeShowGame, timeStartGameInterval = 300;
    private long timeSoundPlay, timeSoundInterval = 100;
    private String winner="";
    private boolean isWinRecorded;

    public ScreenGame(VolleyBall volleyBall) {
        iv = volleyBall;

        imgShadow = new Texture("shadow.png");
        imgBack = new Texture("back.png");

        soundBallHit = Gdx.audio.newSound(Gdx.files.internal("ball_hit.mp3"));
        soundGoal = Gdx.audio.newSound(Gdx.files.internal("goal.mp3"));
        soundWin = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));

        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.6f, 0.6f);
        btnRerun = new TextButton(iv.fontLarge, "REPLAY", 20, SCR_HEIGHT * 100 - 30);

        // игровое поле и сетки
        block[0] = new StaticBodyBox(iv.world, SCR_WIDTH / 2, 0, SCR_WIDTH, 0.3f); // пол
        block[1] = new StaticBodyBox(iv.world, 0, VolleyBall.SCR_HEIGHT / 2, 0.3f, 1000);
        block[2] = new StaticBodyBox(iv.world, SCR_WIDTH, VolleyBall.SCR_HEIGHT / 2, 0.3f, 1000);
        block[3] = new StaticBodyBox(iv.world, SCR_WIDTH / 2, SCR_HEIGHT + 0.4f, SCR_WIDTH, 0.3f);
        // колобки
        person1 = new DynamicBodyPlayer(iv.world, 0, 0.65f, 0.5f, DynamicBodyPlayer.LEFT);
        person2 = new DynamicBodyPlayer(iv.world, 0, 0.65f, 0.5f, DynamicBodyPlayer.RIGHT);
    }

    @Override
    public void show() {
        timeShowGame = TimeUtils.millis();
        create();
    }

    @Override
    public void render(float delta) {
        if (timeShowGame + timeStartGameInterval > TimeUtils.millis()) return;

        iv.camera.update();
        iv.world.step(1 / 60f, 6, 2);
        ScreenUtils.clear(0, 0, 0, 1);
        iv.debugRenderer.render(iv.world, iv.camera.combined);


        // события
        person1.move();
        person2.move();
        if (startGame) {
            if (person1.overlap(ball) || person2.overlap(ball)) {
                startGame = false;
            } else {
                ball.body.setLinearVelocity(0, 0);
                ball.body.setTransform(ball.getX(), ballStartY, 0);
            }
        }
        if (isGoal && !isWin) {
            if (TimeUtils.millis() > timeGoal + timeInterval) {
                isGoal = false;
                if (ball.getX() < SCR_WIDTH / 2) {
                    //iv.world.destroyBody(ball.body);
                    //ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 * 3, ballStartY, iv.gameStyle);
                    ball.body.setTransform(SCR_WIDTH / 4 * 3, ballStartY, 0);
                    startGame = true;
                } else {
                    //iv.world.destroyBody(ball.body);
                    //ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4, ballStartY, iv.gameStyle);
                    ball.body.setTransform(SCR_WIDTH / 4, ballStartY, 0);
                    startGame = true;
                }
                setPersonsPositionToStart();
            }
        } else {
            if (ball.isGoal() && !isWin) {
                isGoal = true;
                timeGoal = TimeUtils.millis();
                if (ball.getX() < SCR_WIDTH / 2) {
                    countGoals2++;
                    if (countGoals2 == 5) {
                        isWin = true;
                        if (iv.soundOn) soundWin.play();
                    } else {
                        if (iv.soundOn) soundGoal.play();
                    }
                } else {
                    countGoals1++;
                    if (countGoals1 == 5) {
                        isWin = true;
                        if (iv.soundOn) soundWin.play();
                    } else {
                        if (iv.soundOn) soundGoal.play();
                    }
                }
            } else {
                if ((person1.overlap(ball) || person2.overlap(ball)) && !isWin) {
                    if (timeSoundPlay + timeSoundInterval < TimeUtils.millis()) {
                        timeSoundPlay = TimeUtils.millis();
                        if (iv.soundOn) soundBallHit.play();
                    }
                }
            }
        }
        // если играем с компьютером
        if (iv.gameMode == MODE_VS_COMPUTER && !isWin && !isGoal) {
            person2.useAi(ball);
        }

        if(isWin && !isWinRecorded) {
            if(countGoals1 > countGoals2) {
                winner = "Выиграл " + iv.player1.name;
                iv.player1.wins++;
            } else {
                winner = "Выиграл " + iv.player2.name;
                iv.player2.wins++;
            }
            isWinRecorded = true;
        }

        // отрисовка
        iv.camera.update();
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        iv.batch.draw(imgNet, SCR_WIDTH / 2 - 0.6f, 0.1f, 1.2f, netHeight);
        iv.batch.draw(imgShadow, ball.scrX(), floor - ball.height() / 8, ball.width(), ball.height() / 4);
        iv.batch.draw(imgShadow, person1.scrX(), floor - ball.height() / 8, person1.width(), person1.height() / 4);
        iv.batch.draw(imgShadow, person2.scrX(), floor - ball.height() / 8, person2.width(), person2.height() / 4);
        iv.batch.draw(imgBall, ball.scrX(), ball.scrY(), ball.r, ball.r, ball.width(), ball.height(), 1, 1, ball.getRotation(), 0, 0, 200, 200, false, false);
        iv.batch.draw(imgPerson1[person1.faza], person1.scrX() - 0.25f, person1.scrY(), person1.width() * 1.5f / 2, person1.height() * 1.5f / 2,
                person1.width() * 1.5f, person1.height() * 1.5f, person1.isFlip ? -1 : 1, 1, 0);
        iv.batch.draw(imgPerson2[person2.faza], person2.scrX() - 0.25f, person2.scrY(), person2.width() * 1.5f / 2, person2.height() * 1.5f / 2,
                person2.width() * 1.5f, person2.height() * 1.5f, person2.isFlip ? -1 : 1, 1, 0);
        iv.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        iv.batch.end();
        iv.batch.setProjectionMatrix(iv.camera2.combined);
        iv.batch.begin();
        iv.fontNormal.draw(iv.batch, ":", 0, SCR_HEIGHT * 100 - 40, SCR_WIDTH * 100, Align.center, true);
        iv.fontNormal.draw(iv.batch, countGoals1 + "", 0, SCR_HEIGHT * 100 - 40, SCR_WIDTH * 100 / 2 - 50, Align.right, true);
        iv.fontNormal.draw(iv.batch, countGoals2 + "", SCR_WIDTH * 100 / 2 + 50, SCR_HEIGHT * 100 - 40, SCR_WIDTH * 100 / 2 - 50, Align.left, true);
        if (isGoal && !isWin) {
            iv.fontLarge.draw(iv.batch, "ГОЛ!", 0, SCR_HEIGHT * 100 / 2, SCR_WIDTH * 100, Align.center, true);
        }
        if (isWin) {
            iv.fontLarge.draw(iv.batch, winner, 0, SCR_HEIGHT * 100 / 2, SCR_WIDTH * 100, Align.center, true);
            btnRerun.font.draw(iv.batch, btnRerun.text, btnRerun.x, btnRerun.y);
        }
        iv.batch.end();
    }

    void setPersonsPositionToStart() {
        person1.body.setLinearVelocity(0, 0);
        person1.body.setAngularVelocity(0);
        person1.body.setTransform(SCR_WIDTH/4-person1.r, 0.65f, 0);
        person1.faza = 0;
        person2.body.setLinearVelocity(0, 0);
        person2.body.setAngularVelocity(0);
        person2.body.setTransform(SCR_WIDTH/4*3+person1.r, 0.65f, 0);
        person2.faza = 0;
    }

    void touchScreen(Vector3 touch) {
        if (touch.x < SCR_WIDTH / 2) {
            person1.touch(touch.x, touch.y);
        } else {
            if(iv.gameMode==MODE_VS_PLAYER) person2.touch(touch.x, touch.y);
        }
    }

    @Override
    public void resize(int width, int height) {
        // unused
    }

    @Override
    public void pause() {
        // unused
    }

    @Override
    public void resume() {
        // unused
    }

    @Override
    public void hide() {
        saveRecords();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        imgBall.dispose();
        imgPersonAtlas1.dispose();
        imgPersonAtlas2.dispose();
        imgBackGround.dispose();
        imgNet.dispose();
        imgBack.dispose();
        imgShadow.dispose();
        soundBallHit.dispose();
        soundGoal.dispose();
        soundWin.dispose();
    }

    class MyInput implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            // левый игрок
            if(keycode == Input.Keys.A) {
                person1.touch(0.1f, person1.getY());
            }
            if(keycode == Input.Keys.D) {
                person1.touch(SCR_WIDTH/2-0.1f, person1.getY());
            }
            if(keycode == Input.Keys.W) {
                person1.touch(person1.getX(), person1.getY()+person1.height()*2);
            }
            if(keycode == Input.Keys.S) {
                person1.touch(person1.getX(), person1.getY());
            }
            // правый игрок
            if(keycode == Input.Keys.LEFT) {
                person2.touch(SCR_WIDTH/2+0.1f, person2.getY());
            }
            if(keycode == Input.Keys.RIGHT) {
                person2.touch(SCR_WIDTH-0.1f, person2.getY());
            }
            if(keycode == Input.Keys.UP) {
                person2.touch(person2.getX(), person2.getY()+person2.height()*2);
            }
            if(keycode == Input.Keys.DOWN) {
                person2.touch(person2.getX(), person2.getY());
            }
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            // левый игрок
            if(keycode == Input.Keys.A || keycode == Input.Keys.D) {
                person1.touch(person1.getX(), person1.getY());
            }
            // правый игрок
            if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.LEFT) {
                person2.touch(person2.getX(), person2.getY());
            }
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
            iv.touch.set(screenX, screenY, 0);
            iv.camera.unproject(iv.touch);

            if (btnBack.hit(iv.touch.x, iv.touch.y)) {
                if (isWin) {
                    isWin = false;
                }
                iv.setScreen(iv.getScreenIntro());
                startGame = true;
            }

            if (isWin && btnRerun.hit(iv.touch.x, iv.touch.y)) {
                create();
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

        private void touchColobs(int screenX, int screenY, int pointer) {
            if (pointer == 0) {
                iv.touch.set(screenX, screenY, 0);
                iv.camera.unproject(iv.touch);
                touchScreen(iv.touch);
            }
            if (pointer == 1) {
                iv.touch.set(screenX, screenY, 0);
                iv.camera.unproject(iv.touch);
                touchScreen(iv.touch);
            }
        }
    }

    private void loadPersons() {
        if(imgPersonAtlas1!=null) imgPersonAtlas1.dispose();
        if(imgPersonAtlas2!=null) imgPersonAtlas2.dispose();
        imgPersonAtlas1 = new Texture("colobatlas"+iv.gameStyle+"0.png");
        imgPersonAtlas2 = new Texture("colobatlas"+iv.gameStyle+"1.png");
        for (int i = 0; i < imgPerson1.length / 2; i++) {
            imgPerson1[i] = new TextureRegion(imgPersonAtlas1, i * 250, 0, 250, 250);
            imgPerson1[i + imgPerson1.length / 2] = new TextureRegion(imgPersonAtlas1, i * 250, 250, 250, 250);
        }
        for (int i = 0; i < imgPerson2.length / 2; i++) {
            imgPerson2[i] = new TextureRegion(imgPersonAtlas2, i * 250, 0, 250, 250);
            imgPerson2[i + imgPerson2.length / 2] = new TextureRegion(imgPersonAtlas2, i * 250, 250, 250, 250);
        }
    }

    void create() {
        isWin = false;
        isWinRecorded = false;
        startGame = true;
        isGoal = false;
        countGoals2 = 0;
        countGoals1 = 0;
        MyInput myInput = new MyInput();
        Gdx.input.setInputProcessor(myInput);
        if(imgBackGround!=null) imgBackGround.dispose();
        imgBackGround = new Texture("background" + iv.gameStyle + ".jpg");
        if(imgBall!=null) imgBall.dispose();
        imgBall = new Texture("ball" + iv.gameStyle + ".png");
        if(imgNet!=null) imgNet.dispose();
        imgNet = new Texture("net" + iv.gameStyle + ".png");
        loadPersons();
        net = new StaticBodyBox(iv.world, SCR_WIDTH / 2, netHeight/2+0.1f, 0.2f, netHeight);

        if(ball!=null) iv.world.destroyBody(ball.body);
        System.out.println("style"+iv.gameStyle);
        switch (iv.gameStyle) {
            case STYLE_BEACH:
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), ballStartY, iv.gameStyle);
                break;
            case STYLE_CASTLE:
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), ballStartY, iv.gameStyle);
                break;
            case STYLE_KITCHEN:
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), ballStartY, iv.STYLE_BEACH);
                break;
            case STYLE_ROOM:
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), ballStartY, iv.STYLE_BEACH);
                break;
            case STYLE_STEAM:
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), ballStartY, iv.gameStyle);
                break;
            case STYLE_FIELD:
                net = new StaticBodyBox(iv.world, SCR_WIDTH / 2, netHeight/2+0.1f, 0.2f, netHeight);
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), ballStartY, STYLE_BEACH);
                break;
            default: iv.setScreen(iv.getScreenIntro());
        }

        ball.body.setLinearVelocity(0, 0);
        ball.body.setAngularVelocity(0);
        ball.body.setTransform(SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), ballStartY, 0);
        setPersonsPositionToStart();
    }

    private void saveRecords() {
        iv.player1.insertRecord(iv.players);
        Player.sortTableOfRecords(iv.players);
        iv.player2.insertRecord(iv.players);
        Player.sortTableOfRecords(iv.players);
        Player.saveTableOfRecords(iv.players);
    }
}
