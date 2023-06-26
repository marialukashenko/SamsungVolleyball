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
    public static final float SCR_WIDTH = 12.8f;
    public static final float SCR_HEIGHT = 7.2f;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public OrthographicCamera camera2;
    public World world;
    public Box2DDebugRenderer debugRenderer;
    public Vector3 touch;
    public BitmapFont font;
    public BitmapFont fontLarge;

    private ScreenIntro screenIntro;
    private ScreenGame screenGame;
    private ScreenSettings screenSettings;
    private ScreenAbout screenAbout;
    private ScreenPickStyle screenPickStyle;
    private ScreenPlayers screenPlayers;

    public static final int STYLE_BEACH = 0;
    public static final int STYLE_CASTLE = 1;
    public static final int STYLE_STEAM = 2;
    public static final int STYLE_ROOM = 3;
    public static final int STYLE_KITCHEN = 4;
    public static final int STYLE_FIELD = 5;
    public static final int NUM_STYLES = 6;
    public int gameStyle;

    public static final int MODE_VS_PLAYER = 0;
    public static final int MODE_VS_COMPUTER = 1;
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

        world = new World(new Vector2(0, -10), false);
        touch = new Vector3();
        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();

        generateFont();
        screenIntro = new ScreenIntro(this);
        screenSettings = new ScreenSettings(this);
        screenPickStyle = new ScreenPickStyle(this);
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
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("letters.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = new Color(1, 1, 0.6f, 1);
        parameter.size = 35;
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
        parameter.size = 40;
        fontLarge = generator.generateFont(parameter);
        generator.dispose();
    }

    public void sleep() {
        try {
            Thread.sleep(300);
        } catch (Exception ignored){
        }
    }

    public ScreenIntro getScreenIntro() {
        return screenIntro;
    }

    public ScreenGame getScreenGame() {
        return screenGame;
    }

    public ScreenSettings getScreenSettings() {
        return screenSettings;
    }

    public ScreenAbout getScreenAbout() {
        return screenAbout;
    }

    public ScreenPickStyle getScreenPickStyle() {
        return screenPickStyle;
    }

    public ScreenPlayers getScreenPlayers() {
        return screenPlayers;
    }
}

