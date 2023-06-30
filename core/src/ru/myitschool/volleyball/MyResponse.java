package ru.myitschool.volleyball;

/**
 * Класс-ответ от сервера-клиенту
 */
public class MyResponse {
    public String text = "";
    public float x;
    public float y;
    public int gameStyle;
    public String name;
    public boolean startGame;
    public float bx, by, ba, bvx, bvy, bva;
    public float p1x, p1y, p1a, p1vx, p1vy, p1va;
    public float p2x, p2y, p2a, p2vx, p2vy, p2va;
    public int p1faza, p2faza;
    public int goals1;
    public int goals2;
}
