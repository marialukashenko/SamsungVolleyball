package ru.myitschool.volleyball;

import static ru.myitschool.volleyball.MyGdx.SCR_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class ImageButton {
    Texture img;
    float x, y;
    float width, height;

    public ImageButton(Texture img, float x, float y, float width, float height) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    boolean hit(float tx, float ty){
        return x < tx && tx < x+width && y+height > ty && ty > y;
    }
}
