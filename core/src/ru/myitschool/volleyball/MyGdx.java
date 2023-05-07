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
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MyGdx extends Game {
	public static final float SCR_WIDTH = 12.8f, SCR_HEIGHT = 7.2f;
	SpriteBatch batch;
	OrthographicCamera camera;
	OrthographicCamera camera2;
	World world;
	Box2DDebugRenderer debugRenderer;
	Vector3 touch;
	BitmapFont font, fontLarge;

	ScreenIntro screenIntro;
	ScreenGame screenGame;
	ScreenSettings screenSettings;
	ScreenAbout screenAbout;

	@Override
	public void create() {
		// создание системных объектов
		batch = new SpriteBatch();
		camera = new OrthographicCamera();

		camera2 = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		camera2.setToOrtho(false, SCR_WIDTH*100, SCR_HEIGHT*100);

		world = new World(new Vector2(0, -10), false);
		touch = new Vector3();
		Box2D.init();
		debugRenderer = new Box2DDebugRenderer();

		generateFont();
		screenIntro = new ScreenIntro(this);
		screenGame = new ScreenGame(this);
		screenSettings = new ScreenSettings(this);
		screenAbout = new ScreenAbout(this);
		setScreen(screenIntro);
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		fontLarge.dispose();
	}

	void generateFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("classicfont.otf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = new Color(1, 0.8f, 0.4f, 1);
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

	public void sleep(){
		try {
			Thread.sleep(300);
		} catch (Exception ignored){

		}
	}


}

