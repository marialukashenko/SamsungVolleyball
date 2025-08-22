package ru.myitschool.volleyball.components;

import static ru.myitschool.volleyball.VolleyBall.SCR_WIDTH;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import ru.myitschool.volleyball.interfaces.Button;

public class TextButton implements Button {
    public BitmapFont font;
    public String text;
    public float x, y;
    public float width, height;

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
        this.x = SCR_WIDTH * 100 / 2f - width / 2;
    }

    public void setText(String text, boolean center) {
        this.text = text;
        GlyphLayout gl = new GlyphLayout(font, text);
        width = gl.width;
        if(center) {
            this.x = SCR_WIDTH * 100 / 2f - width / 2;
        }
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean hit(float tx, float ty) {
        return x / 100f < tx && tx < (x + width) / 100f && (y - height) / 100f < ty && ty < y / 100f;
    }
}
