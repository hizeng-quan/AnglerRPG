package com.fishingrpg.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

public class AudioManager {
    private static AudioManager instance;

    private ObjectMap<String, Music> bgmMap;
    private ObjectMap<String, Sound> sfxMap;
    
    private String currentBGM;
    private boolean isMuted = false;
    private float bgmVolume = 0.5f;
    private float sfxVolume = 1.0f;

    private AudioManager() {
        bgmMap = new ObjectMap<>();
        sfxMap = new ObjectMap<>();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playBGM(String mapId) {
        if (isMuted) return;
        
        String bgmName = "bgm_" + mapId;
        if (currentBGM != null && currentBGM.equals(bgmName)) {
            return; // Already playing
        }

        // Stop current BGM
        if (currentBGM != null && bgmMap.containsKey(currentBGM)) {
            bgmMap.get(currentBGM).stop();
        }

        if (!bgmMap.containsKey(bgmName)) {
            try {
                Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/bgm/" + bgmName + ".wav"));
                music.setLooping(true);
                bgmMap.put(bgmName, music);
            } catch (Exception e) {
                Gdx.app.error("AudioManager", "Could not load BGM: " + bgmName, e);
                return;
            }
        }

        Music music = bgmMap.get(bgmName);
        if (music != null) {
            music.setVolume(bgmVolume);
            music.play();
            currentBGM = bgmName;
        }
    }

    public void playSFX(String sfxName) {
        if (isMuted) return;
        
        if (!sfxMap.containsKey(sfxName)) {
            try {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal("audio/sfx/" + sfxName + ".wav"));
                sfxMap.put(sfxName, sound);
            } catch (Exception e) {
                Gdx.app.error("AudioManager", "Could not load SFX: " + sfxName, e);
                return;
            }
        }

        Sound sound = sfxMap.get(sfxName);
        if (sound != null) {
            sound.play(sfxVolume);
        }
    }

    public void stopBGM() {
        if (currentBGM != null && bgmMap.containsKey(currentBGM)) {
            bgmMap.get(currentBGM).stop();
            currentBGM = null;
        }
    }

    public void setMuted(boolean muted) {
        this.isMuted = muted;
        if (muted) {
            stopBGM();
        }
    }
    
    public void dispose() {
        for (Music m : bgmMap.values()) {
            m.dispose();
        }
        for (Sound s : sfxMap.values()) {
            s.dispose();
        }
        bgmMap.clear();
        sfxMap.clear();
    }
}
