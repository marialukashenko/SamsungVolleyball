package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    private final VolleyBall iv;
    private final Texture imgBackGround;
    private final Texture imgBack;
    private final TextButton btnMusic;
    private final TextButton btnSound;
    private final TextButton btnBackgrounds;
    private final TextButton btnPlayers;
    private final TextButton btnLanguage;
    private final ImageButton btnBack;

    public ScreenSettings(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("screenbgsettings.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.6f, 0.6f);
        btnMusic = new TextButton(iv.fontLarge, iv.text.get("MUSIC ON")[iv.lang], 600);
        btnSound = new TextButton(iv.fontLarge, iv.text.get("SOUND ON")[iv.lang], 500);
        btnBackgrounds = new TextButton(iv.fontLarge, iv.text.get("STYLE")[iv.lang], 400);
        btnPlayers = new TextButton(iv.fontLarge, iv.text.get("PLAYERS")[iv.lang], 300);
        btnLanguage = new TextButton(iv.fontLarge, iv.text.get("LANGUAGE")[iv.lang], 200);
        loadSettings();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            iv.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            iv.camera.unproject(iv.touch);
            if (btnBack.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.getScreenIntro());
            }
            if (btnSound.hit(iv.touch.x, iv.touch.y)) {
                iv.soundOn = !iv.soundOn;
                updateButtons();
            }
            if (btnMusic.hit(iv.touch.x, iv.touch.y)) {
                iv.musicOn = !iv.musicOn;
                updateButtons();
            }
            if (btnBackgrounds.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.getScreenPickStyle());
            }
            if (btnPlayers.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.getScreenPlayers());
            }
            if (btnLanguage.hit(iv.touch.x, iv.touch.y)) {
                ++iv.lang;
                if(iv.lang > LANG_ES){
                    iv.lang = LANG_EN; // меняем лангуагу
                }
                updateButtons();
            }
        }

        //отрисовка
        iv.camera.update();
        iv.batch.setProjectionMatrix(iv.camera.combined);
        iv.batch.begin();
        iv.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        iv.batch.draw(btnBack.img, btnBack.x, btnBack.y, btnBack.width, btnBack.height);
        iv.batch.end();
        iv.batch.setProjectionMatrix(iv.camera2.combined);
        iv.batch.begin();
        btnMusic.font.draw(iv.batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnSound.font.draw(iv.batch, btnSound.text, btnSound.x, btnSound.y);
        btnBackgrounds.font.draw(iv.batch, btnBackgrounds.text, btnBackgrounds.x, btnBackgrounds.y);
        btnPlayers.font.draw(iv.batch, btnPlayers.text, btnPlayers.x, btnPlayers.y);
        btnLanguage.font.draw(iv.batch, btnLanguage.text, btnLanguage.x, btnLanguage.y);
        iv.fontTitle.draw(iv.batch, iv.text.get("SETTINGS")[iv.lang], 20, SCR_HEIGHT*100-20);
        iv.batch.end();
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
        saveSettings();
        Gdx.input.setInputProcessor(null);
    }

    void saveSettings() {
        Preferences pref = Gdx.app.getPreferences("Settings iVolleyBall");
        pref.putBoolean("sound", iv.soundOn);
        pref.putBoolean("music", iv.musicOn);
        pref.putInteger("style", iv.gameStyle);
        pref.putInteger("language", iv.lang);
        pref.putString("name1", iv.player1.name);
        pref.putString("name2", iv.player2.name);
        pref.putBoolean("isai1", iv.player1.isAi);
        pref.putBoolean("isai2", iv.player2.isAi);
        pref.flush();
    }

    void loadSettings() {
        Preferences pref = Gdx.app.getPreferences("Settings iVolleyBall");
        if(pref.contains("sound")) iv.soundOn = pref.getBoolean("sound", false);
        if(pref.contains("music")) iv.musicOn = pref.getBoolean("music", false);
        if(pref.contains("style")) iv.gameStyle = pref.getInteger("style", 0);
        if(pref.contains("language")) iv.lang = pref.getInteger("language", 0);
        if(pref.contains("name1")) iv.player1.name = pref.getString("name1", "Noname");
        if(pref.contains("name2")) iv.player2.name = pref.getString("name2", "Noname");
        if(pref.contains("isai1")) iv.player1.isAi = pref.getBoolean("isai1", false);
        if(pref.contains("isai2")) iv.player2.isAi = pref.getBoolean("isai2", false);
        updateButtons();
    }

    void updateButtons(){
        btnSound.setText(iv.soundOn ? iv.text.get("SOUND ON")[iv.lang] : iv.text.get("SOUND OFF")[iv.lang], true);
        btnMusic.setText(iv.musicOn ? iv.text.get("MUSIC ON")[iv.lang] : iv.text.get("MUSIC OFF")[iv.lang], true);
        btnBackgrounds.setText(iv.text.get("STYLE")[iv.lang], true);
        btnPlayers.setText(iv.text.get("PLAYERS")[iv.lang], true);
        btnLanguage.setText(iv.text.get("LANGUAGE")[iv.lang], true);
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}

