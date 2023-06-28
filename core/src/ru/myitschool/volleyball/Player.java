package ru.myitschool.volleyball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Player {
    public int wins;
    public String name;
    public boolean isAi;

    public Player(String name) {
        this.name = name;
    }

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

    public static String tableOfRecordsToString(Player[] players){
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
    }

    public static float getWidth(String text, BitmapFont font) {
        GlyphLayout gl = new GlyphLayout(font, text);
        return gl.width;
    }

    public static String tableOfRecordsToString(Player[] players, BitmapFont font){
        float maxWidth = getWidth("WWWWWWWW...9999", font);
        float pointWidth = getWidth(".", font);
        String s = "";
        for (int i = 0; i < 10; i++) {
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
        }
    }

    public static void loadTableOfRecords(Player[] players){
        try {
            Preferences pref = Gdx.app.getPreferences("Records iVolleyBall");
            for (int i = 0; i < players.length; i++) {
                if(pref.contains("name"+i))	players[i].name = pref.getString("name"+i, "Noname");
                if(pref.contains("wins"+i)) players[i].wins = pref.getInteger("wins"+i, 0);
            }
        } catch (Exception e) {
            // exception
        }
    }

    public void insertRecord(Player[] players) {
        boolean isFinded = false;
        for (Player value : players) {
            if (value.name.equals(name)) {
                value.wins += wins;
                isFinded = true;
                break;
            }
        }
        if(!isFinded) {
            players[players.length-1].wins = wins;
            players[players.length-1].name = name;
        }
    }

    public void getRecord(Player[] players) {
        for (Player value : players) {
            if (value.name.equals(name)) {
                wins = value.wins;
            }
        }
    }
}
