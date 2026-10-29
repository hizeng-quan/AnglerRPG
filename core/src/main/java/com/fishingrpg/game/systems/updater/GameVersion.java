package com.fishingrpg.game.systems.updater;

public class GameVersion {
    public static final String CURRENT_VERSION = "0.5.5.8";
    public static final String BUILD_DATE = "2026-10-29";
    public static final String GITHUB_OWNER = "hizeng-quan";
    public static final String GITHUB_REPO = "AnglerRPG";
    public static final String RELEASES_API_URL = "https://api.github.com/repos/" + GITHUB_OWNER + "/" + GITHUB_REPO
            + "/releases/latest";

    public static String getDisplayVersion() {
        return "v" + CURRENT_VERSION;

    }

}
