package ru.myitschool.volleyball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class VolleyBall extends Game {
    public static final float SCR_WIDTH = 12.8f, SCR_HEIGHT = 7.2f;
    public static int number_background = 0;
    public static int number_players = 0;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public OrthographicCamera camera2;
    public World worldTwoPlayers;
    public World worldComputer;
    public Box2DDebugRenderer debugRenderer;
    public Vector3 touch;
    public BitmapFont font, fontLarge;

    ScreenIntro screenIntro;
    ScreenGame screenGame;
    ScreenSettings screenSettings;
    ScreenAbout screenAbout;
    ScreenBackgrounds screenBackgrounds;
    ScreenPlayers screenPlayers;

    public static final int STYLE_BEACH = 0, STYLE_KITCHEN = 1, STYLE_STEAM = 2, STYLE_ROOM = 3, STYLE_CASTLE = 4;
    public int numStyles = 5;
    public int gameStyle;

    public static final int MODE_VS_PLAYER = 0, MODE_VS_COMPUTER = 1;
    public int gameMode;

    public boolean soundOn = true;
    public boolean musicOn = true;

    @Override
    public void create() {
        // создание системных объектов
        batch = new SpriteBatch();
        camera = new OrthographicCamera();

        camera2 = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        camera2.setToOrtho(false, SCR_WIDTH * 100, SCR_HEIGHT * 100);

        worldTwoPlayers = new World(new Vector2(0, -10), false);
        worldComputer = new World(new Vector2(0, -10), false);
        touch = new Vector3();
        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();

        generateFont();
        screenIntro = new ScreenIntro(this);
        screenSettings = new ScreenSettings(this);
        screenBackgrounds = new ScreenBackgrounds(this);
        screenPlayers = new ScreenPlayers(this);
        screenAbout = new ScreenAbout(this);
        screenGame = new ScreenGame(this);
        setScreen(screenIntro);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        fontLarge.dispose();
    }

    private void generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("beautiful_letters.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = new Color(1, 1, 0.6f, 1);
        parameter.size = 45;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        parameter.borderStraight = true;
        parameter.shadowColor = new Color(0.1f, 0.1f, 0.1f, 0.8f);
        parameter.shadowOffsetX = parameter.shadowOffsetY = 3;
        String str = "";
        for (char i = 0x20; i < 0x7B; i++) str += i;
        for (char i = 0x401; i < 0x452; i++) str += i;
        parameter.characters = str;
        //parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
        font = generator.generateFont(parameter);
        parameter.size = 60;
        fontLarge = generator.generateFont(parameter);
        generator.dispose();
    }

    public void sleep() {
        try {
            Thread.sleep(300);
        } catch (Exception ignored) {
            //
        }
    }
}

