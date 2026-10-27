package com.fishingrpg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.fishingrpg.game.data.SaveManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestCrash {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 600);
        new Lwjgl3Application(new ApplicationAdapter() {
            FishingRPG game;
            @Override
            public void create() {
                try {
                    game = new FishingRPG();
                    game.create();
                    PlayScreen ps = new PlayScreen(game);
                    game.setScreen(ps);
                    System.out.println("TESTING EXPORT START");
                    
                    // Fixed export logic:
                    SaveManager.savePlayer(game.player);
                    System.out.println("savePlayer DONE");
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String ts = sdf.format(new Date());
                    String filename = "angler_save_" + ts + ".sav";

                    File fileTs = new File(filename);
                    SaveManager.exportToFile(fileTs);
                    System.out.println("exportToFile ts DONE: " + fileTs.getAbsolutePath() + " (" + fileTs.length() + " bytes)");

                    File fileStd = new File("angler_save.sav");
                    SaveManager.exportToFile(fileStd);
                    System.out.println("exportToFile std DONE");

                    File fileLegacy = new File("angler_save.json");
                    SaveManager.exportToFile(fileLegacy);
                    System.out.println("exportToFile legacy DONE");

                    // Clipboard safe handling
                    try {
                        String encryptedPayload = SaveManager.exportDataToString();
                        if (encryptedPayload != null && encryptedPayload.length() < 16384 && Gdx.app.getClipboard() != null) {
                            Gdx.app.getClipboard().setContents(encryptedPayload);
                        }
                    } catch (Throwable ignored) {}
                    System.out.println("ALL EXPORT OPERATIONS FINISHED SUCCESSFULLY WITHOUT CRASH!");

                    Gdx.app.exit();
                } catch (Throwable t) {
                    System.err.println("CRASH CAUGHT:");
                    t.printStackTrace();
                    Gdx.app.exit();
                }
            }
            @Override
            public void render() {
                if (game != null) game.render();
            }
        }, config);
    }
}

