package ru.myitschool.volleyball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * класс игрока
 */
public class Player {

    public int wins; // количество побед
    public String name; // имя
    public boolean isAi; // компьютер или человек

    public Player(String name) {
        this.name = name;
    }

    // сортировка таблицы рекордов
    public static void sortTableOfRecords(Player[] players){
        for (int j = 0; j < players.length; j++) {
            for (int i = 0; i < players.length-1; i++) {
                if(players[i].wins < players[i+1].wins){
                    Player c = players[i];
                    players[i] = players[i+1];
                    players[i+1] = c;
                }
            }
        }
    }

    /*public static String tableOfRecordsToString(Player[] players){
        String s = "";
        for (int i = 0; i < 10; i++) {
            s += players[i].name+points(players[i].name, 13)+players[i].wins +"\n";
        }
        return s;
    }

    public static String points(String name, int length){
        int n = length-name.length();
        String s = "";
        for (int i = 0; i < n; i++) s += ".";
        return s;
    }*/

    // определение ширины текста
    private static float getWidth(String text, BitmapFont font) {
        GlyphLayout gl = new GlyphLayout(font, text);
        return gl.width;
    }

    // преобразование верхней части таблицы рекордов в строку
    public static String tableOfRecordsToString(Player[] players, BitmapFont font){
        float maxWidth = getWidth("WWWWWWWW...9999", font);
        float pointWidth = getWidth(".", font);
        String s = "";
        for (int i = 0; i < 10; i++) { // берём только 10 верхних строк
            // определяем нужное количество точек и добиваем ими строку
            float rowWidth = getWidth(players[i].name+players[i].wins, font);
            int n =(int) ((maxWidth - rowWidth)/pointWidth);
            String points = "";
            for (int j = 0; j < n; j++) {
                points += ".";
            }
            s += players[i].name+" "+points+" "+players[i].wins +"\n";
        }
        return s;
    }

    // сохраннение таблицы рекордов
    public static void saveTableOfRecords(Player[] players){
        sortTableOfRecords(players);
        try {
            Preferences pref = Gdx.app.getPreferences("Records iVolleyBall");
            for (int i = 0; i < players.length; i++) {
                pref.putString("name"+i, players[i].name);
                pref.putInteger("wins"+i, players[i].wins);
            }
            pref.flush();
        } catch (Exception e){
            // exception
        }
    }

    // чтение таблицы рекордов
    public static void loadTableOfRecords(Player[] players){
        try {
            Preferences pref = Gdx.app.getPreferences("Records iVolleyBall");
            for (int i = 0; i < players.length; i++) {
                if(pref.contains("name"+i))	players[i].name = pref.getString("name"+i, "No`name");
                if(pref.contains("wins"+i)) players[i].wins = pref.getInteger("wins"+i, 0);
            }
        } catch (Exception e) {
            // exception
        }
    }

    // очистка таблицы рекордов
    public static void clearTableOfRecords(Player[] players){
        for (int i = 0; i < players.length; i++) {
            players[i].name = "No`name";
            players[i].wins = 0;
        }
        saveTableOfRecords(players);
    }

    // добавляем нашу победу в таблицу рекордов
    public void addWinToRecord(Player[] players) {
        // если игрока с таким именем нашли, то добавляем ему побезу
        boolean isFinded = false;
        for (Player value : players) {
            if (value.name.equals(name)) {
                value.wins += 1;
                isFinded = true;
                break;
            }
        }
        // если игрока с таким именем не нашли, то добавляем его с одной победой
        if(!isFinded) {
            players[players.length-1].wins = 1;
            players[players.length-1].name = name;
        }
        sortTableOfRecords(players);
        saveTableOfRecords(players);
    }

    // получить количество побед из таблицы
    public void getRecord(Player[] players) {
        for (Player value : players) {
            if (value.name.equals(name)) {
                wins = value.wins;
            }
        }
    }
}
