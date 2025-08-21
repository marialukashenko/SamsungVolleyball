package ru.myitschool.volleyball.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class InputKeyboard {
    private boolean endOfEdit;
    private String fontName = "minnie.otf";
    private String keysName = "keys.png";

    private final float x, y;
    private final float width, height;
    private final float keyWidth, keyHeight;
    private final float padding;
    private final int textLength;

    private BitmapFont font;

    private String text = "";
    private static final String LETTERS_EN_CAPS = "1234567890-~QWERTYUIOP+?^ASDFGHJKL;'`ZXCVBNM,. |";
    private static final String LETTERS_EN_LOW  = "1234567890_~qwertyuiop[]^asdfghjkl:'`zxcvbnm,. |";
    private static final String LETTERS_RU_CAPS = "1234567890-~ЙЦУКЕНГШЩЗХЪ^ФЫВАПРОЛДЖЭ`ЯЧСМИТЬБЮЁ|";
    private static final String LETTERS_RU_LOW  = "1234567890_~йцукенгшщзхъ^фывапролджэ`ячсмитьбюё|";
    private String letters = LETTERS_EN_CAPS;

    private final Texture imgAtlasKeys;
    private final TextureRegion imgEditText;
    private final TextureRegion imgKeyUP, imgKeyDown;
    private final TextureRegion imgKeyBS, imgKeyEnter, imgKeyCL, imgKeySW;

    private long timeStart, timeDuration = 150;
    private int keyPressed = -1;
    private final Array<Key> keys = new Array<>();

    public InputKeyboard(float scrWidth, float scrHeight, int textLength){
        generateFont();
        this.textLength = textLength;

        imgAtlasKeys = new Texture(keysName);
        imgKeyUP = new TextureRegion(imgAtlasKeys, 0, 0, 256, 256);
        imgKeyDown = new TextureRegion(imgAtlasKeys, 256, 0, 256, 256);
        imgEditText = new TextureRegion(imgAtlasKeys, 256*2, 0, 256, 256);
        imgKeyBS = new TextureRegion(imgAtlasKeys, 256*3, 0, 256, 256);
        imgKeyEnter = new TextureRegion(imgAtlasKeys, 256*4, 0, 256, 256);
        imgKeyCL = new TextureRegion(imgAtlasKeys, 256*5, 0, 256, 256);
        imgKeySW = new TextureRegion(imgAtlasKeys, 256*6, 0, 256, 256);

        scrWidth *= 100;
        scrHeight *= 100;
        width = scrWidth/21f*20;
        height = scrHeight/5f*3;
        x = (scrWidth-width)/2;
        y = height+scrHeight/30f;
        keyWidth = width/13;
        keyHeight = height/5;
        padding = 8;
        createKBD();
    }

    private void createKBD(){
        int j = 0;
        for (int i = 0; i < 12; i++, j++)
            keys.add(new Key(i*keyWidth+x+keyWidth/2, y-keyHeight*2, keyWidth-padding, keyHeight-padding, letters.charAt(j)));

        for (int i = 0; i < 13; i++, j++)
            keys.add(new Key(i*keyWidth+x, y-keyHeight*3, keyWidth-padding, keyHeight-padding, letters.charAt(j)));

        for (int i = 0; i < 12; i++, j++)
            keys.add(new Key(i*keyWidth+x+keyWidth/2, y-keyHeight*4, keyWidth-padding, keyHeight-padding, letters.charAt(j)));

        for (int i = 0; i < 11; i++, j++)
            keys.add(new Key(i*keyWidth+x+keyWidth, y-keyHeight*5, keyWidth-padding, keyHeight-padding, letters.charAt(j)));
    }

    private void setCharsKBD() {
        int j = 0;
        for (int i = 0; i < 12; i++, j++)
            keys.get(j).letter = letters.charAt(j);

        for (int i = 0; i < 13; i++, j++)
            keys.get(j).letter = letters.charAt(j);

        for (int i = 0; i < 12; i++, j++)
            keys.get(j).letter = letters.charAt(j);

        for (int i = 0; i < 11; i++, j++)
            keys.get(j).letter = letters.charAt(j);
    }

    public void draw(SpriteBatch batch){
        for (int i = 0; i < keys.size; i++) {
            drawImgKey(batch, i, keys.get(i).x, keys.get(i).y, keys.get(i).width, keys.get(i).height);
        }
        // рисуем вводимый текст
        batch.draw(imgEditText, 2*keyWidth+x+keyWidth/2, y-keyHeight, width-5*keyWidth-padding, keyHeight);
        font.draw(batch, text, 2*keyWidth+x+keyWidth/2, keys.get(0).letterY+keyHeight, width-5*keyWidth-padding, Align.center, false);
    }

    private void drawImgKey(SpriteBatch batch, int i, float x, float y, float width, float height){
        float dx, dy;
        if(keyPressed == i){
            batch.draw(imgKeyDown, x, y, width, height);
            dx = 2;
            dy = -2;
            if(TimeUtils.millis() - timeStart > timeDuration){
                keyPressed = -1;
            }
        } else {
            dx = 0;
            dy = 0;
            batch.draw(imgKeyUP, x, y, width, height);
        }

        switch (letters.charAt(i)) {
            case '~': batch.draw(imgKeyBS, x+dx, y+dy, width, height); break;
            case '^': batch.draw(imgKeyEnter, x+dx, y+dy, width, height); break;
            case '`': batch.draw(imgKeyCL, x+dx, y+dy, width, height); break;
            case '|': batch.draw(imgKeySW, x+dx, y+dy, width, height); break;
            default:
                font.draw(batch, ""+keys.get(i).letter, keys.get(i).letterX+dx, keys.get(i).letterY+dy);
        }
    }


    public boolean endOfEdit(float tx, float ty){
        for (int i = 0; i < keys.size; i++) {
            if(!keys.get(i).hit(tx, ty).equals("")){
                keyPressed = i;
                setText(i);
                timeStart = TimeUtils.millis();
            }
        }
        if(endOfEdit){
            endOfEdit = false;
            return true;
        }
        return false;
    }


    private void setText(int i){
        switch (letters.charAt(i)) {
            case '~':
                if(text.length()>0) text = text.substring(0, text.length() - 1);
                break;
            case '^':
                if(text.length()==0) break;
                endOfEdit = true;
                break;
            case '`': // caps lock
                if(letters.charAt(12) == 'Q') letters = LETTERS_EN_LOW;
                else if(letters.charAt(12) == 'q') letters = LETTERS_EN_CAPS;
                else if(letters.charAt(12) == 'Й') letters = LETTERS_RU_LOW;
                else if(letters.charAt(12) == 'й') letters = LETTERS_RU_CAPS;
                setCharsKBD();
                break;
            case '|':
                if(letters.charAt(12) == 'й') letters = LETTERS_EN_LOW;
                else if(letters.charAt(12) == 'Й') letters = LETTERS_EN_CAPS;
                else if(letters.charAt(12) == 'q') letters = LETTERS_RU_LOW;
                else if(letters.charAt(12) == 'Q') letters = LETTERS_RU_CAPS;
                setCharsKBD();
                break;
            default:
                if(text.length()< textLength) text += letters.charAt(i);
                if(text.length() == 1 && letters == LETTERS_EN_CAPS) letters = LETTERS_EN_LOW;
                if(text.length() == 1 && letters == LETTERS_RU_CAPS) letters = LETTERS_RU_LOW;
                setCharsKBD();
        }
    }

    public String getText() {
        String t = text;
        text = "";
        return t;
    }
    private class Key {
        float x, y;
        float width, height;
        char letter;
        float letterX, letterY;

        private Key (float x, float y, float width, float height, char letter) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.letter = letter;
            letterX = x + width/3;
            letterY = y + height - (height - font.getCapHeight())/2;
        }

        private String hit(float tx, float ty){
            if (x/100<tx && tx<(x+width)/100 && y/100<ty && ty<(y+height)/100) {
                return "" + letter;
            }
            return "";
        }
    }

    private void generateFont(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = new Color(1, 1, 1, 1);
        parameter.size = 50;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.borderStraight = true;
        String str = "";
        for (char i = 0x20; i < 0x7B; i++) str += i;
        for (char i = 0x401; i < 0x452; i++) str += i;
        parameter.characters = str;
        font = generator.generateFont(parameter);
        generator.dispose();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void dispose(){
        imgAtlasKeys.dispose();
        font.dispose();
    }
}
