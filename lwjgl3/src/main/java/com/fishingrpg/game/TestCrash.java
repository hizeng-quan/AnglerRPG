package com.fishingrpg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class TestCrash {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 600);
        new Lwjgl3Application(new ApplicationAdapter() {
            FishingRPG game;
            @Override
            public void create() {
                game = new FishingRPG();
                game.create();
                try {
                    game.setScreen(new PlayScreen(game));
                    System.out.println("SUCCESSFULLY CREATED PLAYSCREEN");
                    Gdx.app.exit();
                } catch (Exception e) {
                    e.printStackTrace();
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