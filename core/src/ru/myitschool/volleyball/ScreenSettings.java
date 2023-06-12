package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.VolleyBall.MODE_VS_COMPUTER;
import static ru.myitschool.volleyball.VolleyBall.MODE_VS_PLAYER;
import static ru.myitschool.volleyball.VolleyBall.SCR_HEIGHT;
import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    private VolleyBall iv;
    private Texture imgBackGround;
    private Texture imgBack;
    private TextButton btnMusic, btnSound, btnBackgrounds, btnPlayers, btnModeGame;
    private ImageButton btnBack;

    public ScreenSettings(VolleyBall volleyBall) {
        iv = volleyBall;
        imgBackGround = new Texture("background.jpg");
        imgBack = new Texture("back.png");
        btnBack = new ImageButton(imgBack, SCR_WIDTH - 1, SCR_HEIGHT - 0.9f, 0.7f, 0.7f);
        btnMusic = new TextButton(iv.fontLarge, "MUSIC ON", 500);
        btnSound = new TextButton(iv.fontLarge, "SOUND ON", 430);
        btnBackgrounds = new TextButton(iv.fontLarge, "BACKGROUNDS", 360);
        btnPlayers = new TextButton(iv.fontLarge, "PLAYERS", 290);
        btnModeGame = new TextButton(iv.fontLarge, "PLAYER vs PLAYER", 220);
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
                iv.setScreen(iv.screenIntro);
            }
            if (btnSound.hit(iv.touch.x, iv.touch.y)) {
                iv.soundOn = !iv.soundOn;
                btnSound.setText(iv.soundOn ? "SOUND ON" : "SOUND OFF");
            }
            if (btnMusic.hit(iv.touch.x, iv.touch.y)) {
                iv.musicOn = !iv.musicOn;
                btnMusic.setText(iv.musicOn ? "MUSIC ON" : "MUSIC OFF");
            }
            if (btnBackgrounds.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.screenPickStyle);
            }
            if (btnPlayers.hit(iv.touch.x, iv.touch.y)) {
                iv.setScreen(iv.screenPlayers);
            }
            if (btnModeGame.hit(iv.touch.x, iv.touch.y)) {
                iv.gameMode =  iv.gameMode==MODE_VS_PLAYER ? MODE_VS_COMPUTER : MODE_VS_PLAYER;
                btnModeGame.setText(iv.gameMode==MODE_VS_PLAYER ? "PLAYER vs PLAYER" : "PLAYER vs COMPUTER");
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
        btnModeGame.font.draw(iv.batch, btnModeGame.text, btnModeGame.x, btnModeGame.y);
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
        Preferences pref = Gdx.app.getPreferences("settings iVolleyBall");
        pref.putBoolean("sound", iv.soundOn);
        pref.putBoolean("music", iv.musicOn);
        pref.putInteger("style", iv.gameStyle);
        pref.putInteger("mode", iv.gameMode);
        pref.flush();
    }

    void loadSettings() {
        Preferences pref = Gdx.app.getPreferences("settings iVolleyBall");
        if(pref.contains("sound")) iv.soundOn = pref.getBoolean("sound", false);
        if(pref.contains("music")) iv.musicOn = pref.getBoolean("music", false);
        if(pref.contains("style")) iv.gameStyle = pref.getInteger("style", 0);
        if(pref.contains("mode")) iv.gameMode = pref.getInteger("mode", 0);
        updateButtons();
    }

    void updateButtons(){
        btnSound.setText(iv.soundOn ? "SOUND ON" : "SOUND OFF");
        btnMusic.setText(iv.musicOn ? "MUSIC ON" : "MUSIC OFF");
        btnModeGame.setText(iv.gameMode==MODE_VS_PLAYER ? "PLAYER vs PLAYER" : "PLAYER vs COMPUTER");
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}

