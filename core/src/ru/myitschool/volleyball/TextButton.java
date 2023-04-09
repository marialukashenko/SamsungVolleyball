package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class TextButton {
    BitmapFont font;
    String text;
    float x, y;
    float width, height;

    public TextButton(BitmapFont font, String text, float x, float y) {
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        GlyphLayout gl = new GlyphLayout(font, text);
        width = gl.width;
        height = gl.height;
    }

    public TextButton(BitmapFont font, String text, float y) {
        this.font = font;
        this.text = text;
        this.y = y;
        GlyphLayout gl = new GlyphLayout(font, text);
        width = gl.width;
        height = gl.height;
        this.x = SCR_WIDTH*100/2f - width/2;
    }

    public void setText(String text) {
        this.text = text;
        GlyphLayout gl = new GlyphLayout(font, text);
        width = gl.width;
        this.x = SCR_WIDTH*100/2f - width/2;
    }

    boolean hit(float tx, float ty){
        return x/100f < tx && tx < (x+width)/100f && (y-height)/100f < ty && ty < y/100f;
    }
}
