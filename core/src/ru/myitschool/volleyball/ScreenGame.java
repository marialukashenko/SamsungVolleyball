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

/**
 * главный экран игры
 */
public class ScreenGame implements Screen {

    private VolleyBall iv;

    // ресурсы
    private Sound sndBallHit;
    private Sound sndGoal;
    private Sound sndWin;
    private Texture imgBackGround;
    private Texture imgBall;
    private Texture imgShadow;
    private Texture imgNet;
    private Texture imgBack;
    private Texture imgPersonAtlas1;
    private Texture imgPersonAtlas2;
    private TextureRegion[] imgPerson1 = new TextureRegion[20];
    private TextureRegion[] imgPerson2 = new TextureRegion[20];

    // статические тела
    private StaticBody[] block = new StaticBody[4];
    private StaticBody net;
    private StaticBody net2;

    // динамические тела
    private DynamicBodyPlayer person1, person2;
    private DynamicBodyBall ball;

    // всё, что связано с голами
    private long timeGoal;
    private final long timeGoalsInterval = 3000;
    private int countGoals1;
    private int countGoals2;
    private boolean isGoal;
    private boolean isWin;

    // константы
    private static final float BALL_START_Y = 2.6f; // мяч стартует в этой точке
    private static final float NET_HEIGHT = 4f; // высота сетки
    private static final float FLOOR = 0.6f; // высота пола
    private static final float RADIUS_PERSON = 0.6f; // радиус колобка
    private static final float IMG_RESIZE = 1.6f; // коэффициент изменения размера картинки относительно размера шара
    private static final float SHADOW_MARG = 0.5f; // смещение тени

    // кнопки
    private ImageButton btnBack;
    private TextButton btnRerun;

    private long timeShowGame, timeStartGameInterval = 300;
    private long timeSoundPlay, timeSoundInterval = 100;
    private String winner="";
    private boolean isWinRecorded;
    private boolean startGame = true;

    private String GoalPlayer;
    private boolean NameChanged = false;

    public ScreenGame(VolleyBall volleyBall) {
        iv = volleyBall;

        imgShadow = new Texture("shadow.png");
        imgBack = new Texture("back.png");

        // загрузка музыки
        sndBallHit = Gdx.audio.newSound(Gdx.files.internal("ball_hit.mp3"));
        sndGoal = Gdx.audio.newSound(Gdx.files.internal("goal.mp3"));
        sndWin = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));

        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.6f, 0.6f);
        btnRerun = new TextButton(iv.fontLarge, iv.text.get("REPLAY")[iv.lang], 20, SCR_HEIGHT * 100 - 30);

        // стены на игровом поле
        block[0] = new StaticBody(iv.world, SCR_WIDTH / 2, 0, SCR_WIDTH, FLOOR *2, null); // пол
        block[1] = new StaticBody(iv.world, 0, VolleyBall.SCR_HEIGHT / 2, 0.3f, 1000, null); // стена слева
        block[2] = new StaticBody(iv.world, SCR_WIDTH, VolleyBall.SCR_HEIGHT / 2, 0.3f, 1000, null); // стена справа
        block[3] = new StaticBody(iv.world, SCR_WIDTH / 2, SCR_HEIGHT + 0.4f, SCR_WIDTH, 0.3f, null); // потолок
        // колобки
        person1 = new DynamicBodyPlayer(iv.world, 0, RADIUS_PERSON + FLOOR, RADIUS_PERSON, DynamicBodyPlayer.LEFT);
        person2 = new DynamicBodyPlayer(iv.world, 0, RADIUS_PERSON + FLOOR, RADIUS_PERSON, DynamicBodyPlayer.RIGHT);
    }

    @Override
    public void show() {
        timeShowGame = TimeUtils.millis();
        create();
        btnRerun.setText(iv.text.get("REPLAY")[iv.lang], false);
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
                ball.body.setTransform(ball.getX(), BALL_START_Y, 0);
            }
        }
        if (isGoal && !isWin) {
            if (TimeUtils.millis() > timeGoal + timeGoalsInterval) {
                isGoal = false;
                if (ball.getX() < SCR_WIDTH / 2) {
                    ball.body.setTransform(SCR_WIDTH / 4 * 3, BALL_START_Y, 0);
                    startGame = true;
                } else {
                    ball.body.setTransform(SCR_WIDTH / 4, BALL_START_Y, 0);
                    startGame = true;
                }
                setPersonsPositionToStart();
            }
        } else {
            if (isGoal() && !isWin) {
                isGoal = true;
                timeGoal = TimeUtils.millis();
                if (ball.getX() < SCR_WIDTH / 2) {
                    countGoals2++;
                    if(!NameChanged) {
                        GoalPlayer = iv.player2.name;
                        NameChanged = true;
                    }
                    if (countGoals2 == 5) {
                        isWin = true;
                        if (iv.isSoundOn) sndWin.play();
                    } else {
                        if (iv.isSoundOn) sndGoal.play();
                    }
                } else {
                    countGoals1++;
                    if(!NameChanged) {
                        GoalPlayer = iv.player1.name;
                        NameChanged = true;
                    }
                    if (countGoals1 == 5) {
                        isWin = true;
                        if (iv.isSoundOn) sndWin.play();
                    } else {
                        if (iv.isSoundOn) sndGoal.play();
                    }
                }
            } else {
                if ((person1.overlap(ball) || person2.overlap(ball)) && !isWin) {
                    if (timeSoundPlay + timeSoundInterval < TimeUtils.millis()) {
                        timeSoundPlay = TimeUtils.millis();
                        if (iv.isSoundOn) sndBallHit.play();
                    }
                }
            }
        }
        // если играем с компьютером
        if (iv.player1.isAi && !isWin && !isGoal) {
            person1.useAi(ball);
        }
        if (iv.player2.isAi && !isWin && !isGoal) {
            person2.useAi(ball);
        }
        // события сетевой игры
        if(iv.isOnLanPlayer1 || iv.isOnLanPlayer2) {
            if (iv.getScreenPlayers().isServer) {
                iv.getScreenPlayers().responseFromServer.text = "server";
                iv.getScreenPlayers().responseFromServer.x = iv.touch.x;
                iv.getScreenPlayers().responseFromServer.y = iv.touch.y;
                iv.getScreenPlayers().requestFromClient = iv.getScreenPlayers().server.getRequest();
                person2.touch(iv.getScreenPlayers().requestFromClient.x, iv.getScreenPlayers().requestFromClient.y);
            } else if (iv.getScreenPlayers().isClient) {
                iv.getScreenPlayers().requestFromClient.text = "client";
                iv.getScreenPlayers().requestFromClient.x = iv.touch.x;
                iv.getScreenPlayers().requestFromClient.y = iv.touch.y;
                iv.getScreenPlayers().client.send();
                iv.getScreenPlayers().responseFromServer = iv.getScreenPlayers().client.getResponse();
                person1.touch(iv.getScreenPlayers().responseFromServer.x, iv.getScreenPlayers().responseFromServer.y);
            }
        }

        if(isWin && !isWinRecorded) {
            if(countGoals1 > countGoals2) {
                winner = iv.player1.name + iv.text.get("WINS")[iv.lang];
                iv.player1.wins++;
                iv.player1.addWinToRecord(iv.players);
            } else {
                winner = iv.player2.name + iv.text.get("WINS")[iv.lang];
                iv.player2.wins++;
                iv.player2.addWinToRecord(iv.players);
            }
            isWinRecorded = true;
        }

        // отрисовка
        iv.camera.update();
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        if(iv.gameStyle == STYLE_WINTER) {
            iv.batch.draw(imgNet, SCR_WIDTH / 2 - 1.2f, 0.1f, 2.4f, NET_HEIGHT);
        } else {
            iv.batch.draw(imgNet, SCR_WIDTH / 2 - 0.6f, 0.1f, 1.2f, NET_HEIGHT);
        }
        iv.batch.draw(imgShadow, ball.scrX(), FLOOR /2, ball.width(), ball.height() / 4);
        iv.batch.draw(imgShadow, person1.scrX()+SHADOW_MARG/2, FLOOR /2, person1.width() * IMG_RESIZE - SHADOW_MARG, person1.height() / 4);
        iv.batch.draw(imgShadow, person2.scrX()+SHADOW_MARG/2, FLOOR /2, person2.width() * IMG_RESIZE - SHADOW_MARG, person2.height() / 4);

        iv.batch.draw(imgBall, ball.scrX(), ball.scrY(), ball.r, ball.r, ball.width(), ball.height(), 1, 1, ball.getRotation(), 0, 0, 200, 200, false, false);
        iv.batch.draw(imgPerson1[person1.faza], person1.scrX(), person1.scrY(), person1.width() * IMG_RESIZE/2, person1.height() * IMG_RESIZE/2,
                person1.width() * IMG_RESIZE, person1.height() * IMG_RESIZE, person1.isFlip ? -1 : 1, 1, 0);
        iv.batch.draw(imgPerson2[person2.faza], person2.scrX(), person2.scrY(), person2.width() * IMG_RESIZE/2, person2.height() * IMG_RESIZE/2,
                person2.width() * IMG_RESIZE, person2.height() * IMG_RESIZE, person2.isFlip ? -1 : 1, 1, 0);

        iv.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        iv.batch.end();
        iv.batch.setProjectionMatrix(iv.cameraForText.combined);
        iv.batch.begin();
        iv.fontNormal.draw(iv.batch, ":", 0, SCR_HEIGHT * 100 - 40, SCR_WIDTH * 100, Align.center, true);
        iv.fontNormal.draw(iv.batch, countGoals1 + "", 0, SCR_HEIGHT * 100 - 40, SCR_WIDTH * 100 / 2 - 50, Align.right, true);
        iv.fontNormal.draw(iv.batch, countGoals2 + "", SCR_WIDTH * 100 / 2 + 50, SCR_HEIGHT * 100 - 40, SCR_WIDTH * 100 / 2 - 50, Align.left, true);
        if (isGoal && !isWin) {
            iv.fontLarge.draw(iv.batch, iv.text.get("GOAL")[iv.lang], 0, SCR_HEIGHT * 100 / 2, SCR_WIDTH * 100, Align.center, true);
            NameChanged = false;
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
            if(!iv.player1.isAi) {
                person1.touch(touch.x, touch.y);
            }
        } else {
            if(!iv.player2.isAi) {
                person2.touch(touch.x, touch.y);
            }
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
        sndBallHit.dispose();
        sndGoal.dispose();
        sndWin.dispose();
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

        if(ball!=null) iv.world.destroyBody(ball.body);

        if(net!=null) {
            iv.world.destroyBody(net.body);
            net = null;
        }
        if(net2 != null) {
            iv.world.destroyBody(net2.body);
            net2 = null;
        }


        switch (iv.gameStyle) {
            case STYLE_BEACH:
                net = new StaticBody(iv.world, SCR_WIDTH / 2, NET_HEIGHT /2+0.1f, 0.14f, NET_HEIGHT, null);
                net2 = new StaticBody(iv.world, SCR_WIDTH / 2, NET_HEIGHT-0.3f, 0.5f, 0.7f, null);
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), BALL_START_Y, iv.gameStyle);
                break;
            case STYLE_CASTLE:
                net = new StaticBody(iv.world, SCR_WIDTH / 2, NET_HEIGHT /2+0.1f, 0.2f, NET_HEIGHT, null);
                net2 = new StaticBody(iv.world, SCR_WIDTH / 2, NET_HEIGHT-0.8f, 1, 0.12f, null);
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), BALL_START_Y, iv.gameStyle);
                break;
            case STYLE_STEAM:
                net = new StaticBody(iv.world, SCR_WIDTH / 2, NET_HEIGHT /2+0.1f, 0.54f, NET_HEIGHT, null);
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), BALL_START_Y, iv.gameStyle);
                break;
            case STYLE_KITCHEN:
                net = new StaticBody(iv.world, SCR_WIDTH / 2, NET_HEIGHT /2+0.1f, 0.1f, NET_HEIGHT, null);
                net2 = new StaticBody(iv.world, SCR_WIDTH / 2, FLOOR+0.3f, 0.2f, 1f, new float[]{0,0.5f, -0.4f,-0.5f, 0.4f,-0.5f});
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), BALL_START_Y, iv.gameStyle);
                break;
            case STYLE_GRAVE:
                net = new StaticBody(iv.world, SCR_WIDTH / 2, NET_HEIGHT /2+0.1f, 0.5f, NET_HEIGHT, null);
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), BALL_START_Y, iv.gameStyle);
                break;
            case STYLE_WINTER:
                net = new StaticBody(iv.world, SCR_WIDTH / 2, NET_HEIGHT /2+0.1f, 0.2f, NET_HEIGHT, null);
                net2 = new StaticBody(iv.world, SCR_WIDTH / 2, NET_HEIGHT /2+0.1f, 0.2f, NET_HEIGHT, new float[]{0,2, -1.2f,-2, 1.2f,-2});
                ball = new DynamicBodyBall(iv.world, SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), BALL_START_Y, iv.gameStyle);
                break;
            default: iv.setScreen(iv.getScreenIntro());
        }

        ball.body.setLinearVelocity(0, 0);
        ball.body.setAngularVelocity(0);
        ball.body.setTransform(SCR_WIDTH / 4 + (MathUtils.randomBoolean() ? 0 : SCR_WIDTH / 2), BALL_START_Y, 0);
        setPersonsPositionToStart();
    }

    private boolean isGoal() {
        return ball.getY() <= FLOOR + ball.r + 0.2f;
    }
}
