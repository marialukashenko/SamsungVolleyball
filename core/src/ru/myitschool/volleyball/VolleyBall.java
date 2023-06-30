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
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Главный класс игры
 */
public class VolleyBall extends Game {
    public static final float SCR_WIDTH = 12.8f;
    public static final float SCR_HEIGHT = 7.2f;

    // системные объекты
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public OrthographicCamera cameraForText;
    public World world;
    public Box2DDebugRenderer debugRenderer;
    public Vector3 touch;

    // шрифты
    public BitmapFont fontNormal;
    public BitmapFont fontSmall;
    public BitmapFont fontLarge;
    public BitmapFont fontTitle;
    public BitmapFont fontMega;

    // экраны
    private ScreenIntro screenIntro;
    private ScreenGame screenGame;
    private ScreenSettings screenSettings;
    private ScreenAbout screenAbout;
    private ScreenStyle screenStyle;
    private ScreenPlayers screenPlayers;
    private ScreenRecords screenRecords;
    private ScreenNetwork screenNetwork;

    // стили
    public static final int STYLE_BEACH = 0;
    public static final int STYLE_CASTLE = 1;
    public static final int STYLE_STEAM = 2;
    public static final int STYLE_KITCHEN = 3;
    public static final int STYLE_GRAVE = 4;
    public static final int STYLE_WINTER = 5;
    public static final int NUM_STYLES = 6;
    public int gameStyle;

    // флаги
    public boolean isSoundOn = true;
    public boolean isMusicOn = true;
    public boolean isOnLanPlayer1 = false;
    public boolean isOnLanPlayer2 = false;

    // игроки
    public Player player1;
    public Player player2;
    public Player[] players = new Player[1000];

    // тексты и локализация
    public Map<String, String[]> text = new HashMap<>();
    public int lang;
    public static final int LANG_EN = 0;
    public static final int LANG_RU = 1;
    public static final int LANG_DE = 2;
    public static final int LANG_PO = 3;
    public static final int LANG_ES = 4;

    @Override
    public void create() {
        // создание системных объектов
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        cameraForText = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        cameraForText.setToOrtho(false, SCR_WIDTH * 100, SCR_HEIGHT * 100);
        touch = new Vector3();
        generateFont();

        // создание мира box2D
        world = new World(new Vector2(0, -10), false);
        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();

        // парсинг всех текстов с учётом локализации
        JsonValue json = new JsonReader().parse(Gdx.files.internal("text.json"));
        for (JsonValue j: json.iterator()) {
            text.put(j.name, json.get(j.name).asStringArray());
        }

        // создание игроков и таблицы рекордов
        player1 = new Player("Noname1");
        player2 = new Player("Noname2");
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("No`name");
        }
        Player.loadTableOfRecords(players);
        player1.getRecord(players);
        player2.getRecord(players);

        // создание экранов
        screenSettings = new ScreenSettings(this);
        screenIntro = new ScreenIntro(this);
        screenStyle = new ScreenStyle(this);
        screenPlayers = new ScreenPlayers(this);
        screenRecords = new ScreenRecords(this);
        screenAbout = new ScreenAbout(this);
        screenGame = new ScreenGame(this);
        screenNetwork = new ScreenNetwork(this);
        setScreen(screenIntro);
    }

    @Override
    public void dispose() {
        batch.dispose();
        fontTitle.dispose();
        fontSmall.dispose();
        fontNormal.dispose();
        fontLarge.dispose();
        world.dispose();
        debugRenderer.dispose();
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
        parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
        fontNormal = generator.generateFont(parameter);
        parameter.size = 40;
        fontLarge = generator.generateFont(parameter);
        parameter.size = 50;
        fontMega = generator.generateFont(parameter);
        parameter.color = new Color(1, 0.4f, 0.1f, 1);
        parameter.size = 30;
        fontTitle = generator.generateFont(parameter);
        generator.dispose();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("minnie.otf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = new Color(1, 1, 0.6f, 1);
        parameter.size = 40;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;
        parameter.borderStraight = true;
        parameter.shadowColor = new Color(0.1f, 0.1f, 0.1f, 0.8f);
        parameter.shadowOffsetX = parameter.shadowOffsetY = 3;
        parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
        fontSmall = generator.generateFont(parameter);
        generator.dispose();
    }

    // усыплялка
    public void sleep() {
        try {
            Thread.sleep(300);
        } catch (Exception ignored) {
        }
    }

    // раздача экранов
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

    public ScreenStyle getScreenStyle() {
        return screenStyle;
    }

    public ScreenPlayers getScreenPlayers() {
        return screenPlayers;
    }

    public ScreenRecords getScreenRecords() {
        return screenRecords;
    }

    public ScreenNetwork getScreenNetwork() {
        return screenNetwork;
    }
}

