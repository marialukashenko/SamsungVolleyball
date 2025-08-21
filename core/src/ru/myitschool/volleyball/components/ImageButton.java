package ru.myitschool.volleyball.components;

import com.badlogic.gdx.graphics.Texture;

public class ImageButton {
    public Texture img;
    public float x, y;
    public float width, height;

    public ImageButton(Texture img, float x, float y, float width, float height) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public boolean hit(float tx, float ty) {
        return x < tx && tx < x + width && y + height > ty && ty > y;
    }
}
