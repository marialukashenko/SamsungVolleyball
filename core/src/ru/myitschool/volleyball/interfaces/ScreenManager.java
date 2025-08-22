package ru.myitschool.volleyball.interfaces;

import ru.myitschool.volleyball.screens.ScreenAbout;
import ru.myitschool.volleyball.screens.ScreenGame;
import ru.myitschool.volleyball.screens.ScreenIntro;
import ru.myitschool.volleyball.screens.ScreenNetwork;
import ru.myitschool.volleyball.screens.ScreenPlayers;
import ru.myitschool.volleyball.screens.ScreenRecords;
import ru.myitschool.volleyball.screens.ScreenSettings;
import ru.myitschool.volleyball.screens.ScreenStyle;

public interface ScreenManager {
    public ScreenIntro getScreenIntro();

    public ScreenGame getScreenGame();

    public ScreenSettings getScreenSettings();

    public ScreenAbout getScreenAbout();

    public ScreenStyle getScreenStyle();

    public ScreenPlayers getScreenPlayers();

    public ScreenRecords getScreenRecords();

    public ScreenNetwork getScreenNetwork();
}
