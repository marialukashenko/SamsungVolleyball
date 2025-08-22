package ru.myitschool.volleyball.components;

import com.badlogic.gdx.graphics.Texture;

import ru.myitschool.volleyball.interfaces.Button;

public class ImageButton implements Button {
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

    @Override
    public boolean hit(float tx, float ty) {
        return x < tx && tx < x + width && y + height > ty && ty > y;
    }
}
