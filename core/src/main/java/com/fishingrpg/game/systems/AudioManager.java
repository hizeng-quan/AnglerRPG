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
    private boolean isBgmMuted = false;
    private boolean isSfxMuted = false;
    private float bgmVolume = 0.5f;
    private float sfxVolume = 0.8f;

    private static final String PREF_AUDIO = "FishingRPG_Audio";
    private static final String KEY_BGM_VOL = "bgm_vol";
    private static final String KEY_SFX_VOL = "sfx_vol";
    private static final String KEY_BGM_MUTED = "bgm_muted";
    private static final String KEY_SFX_MUTED = "sfx_muted";

    private AudioManager() {
        bgmMap = new ObjectMap<>();
        sfxMap = new ObjectMap<>();
        loadPreferences();
    }

    private void loadPreferences() {
        try {
            com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences(PREF_AUDIO);
            bgmVolume = prefs.getFloat(KEY_BGM_VOL, 0.5f);
            sfxVolume = prefs.getFloat(KEY_SFX_VOL, 0.8f);
            isBgmMuted = prefs.getBoolean(KEY_BGM_MUTED, prefs.getBoolean("is_muted", false));
            isSfxMuted = prefs.getBoolean(KEY_SFX_MUTED, prefs.getBoolean("is_muted", false));
        } catch (Throwable ignored) {}
    }

    private void savePreferences() {
        try {
            com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences(PREF_AUDIO);
            prefs.putFloat(KEY_BGM_VOL, bgmVolume);
            prefs.putFloat(KEY_SFX_VOL, sfxVolume);
            prefs.putBoolean(KEY_BGM_MUTED, isBgmMuted);
            prefs.putBoolean(KEY_SFX_MUTED, isSfxMuted);
            prefs.flush();
        } catch (Throwable ignored) {}
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    private String targetBGM;

    public void playBGM(String bgmName) {
        targetBGM = bgmName;
        if (isBgmMuted) return;

        if (currentBGM == null) {
            startMusic(targetBGM);
        } else {
            Music currentMusic = bgmMap.get(currentBGM);
            if (currentMusic != null && !currentMusic.isPlaying()) {
                startMusic(targetBGM);
            }
            // If it is playing, we just do nothing. onCompletion will handle the transition.
        }
    }

    private void startMusic(String bgmName) {
        if (currentBGM != null && bgmMap.containsKey(currentBGM) && !currentBGM.equals(bgmName)) {
            bgmMap.get(currentBGM).stop();
        }

        if (!bgmMap.containsKey(bgmName)) {
            try {
                Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/bgm/" + bgmName + ".mp3"));
                music.setLooping(false); // Must be false to trigger onCompletion
                music.setOnCompletionListener(new Music.OnCompletionListener() {
                    @Override
                    public void onCompletion(Music m) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                if (targetBGM != null && !isBgmMuted) {
                                    startMusic(targetBGM);
                                }
                            }
                        });
                    }
                });
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
        if (isSfxMuted || sfxName == null) return;
        
        // Convenience aliases for legacy sound names
        if ("button_click".equals(sfxName)) sfxName = "sfx_button";
        if ("victory".equals(sfxName)) sfxName = "sfx_success";
        
        if (!sfxMap.containsKey(sfxName)) {
            try {
                com.badlogic.gdx.files.FileHandle fh = Gdx.files.internal("audio/sfx/" + sfxName + ".wav");
                if (fh.exists()) {
                    Sound sound = Gdx.audio.newSound(fh);
                    sfxMap.put(sfxName, sound);
                } else {
                    Gdx.app.log("AudioManager", "SFX not found on disk: " + sfxName);
                    sfxMap.put(sfxName, null);
                    return;
                }
            } catch (Exception e) {
                Gdx.app.error("AudioManager", "Could not load SFX: " + sfxName, e);
                sfxMap.put(sfxName, null);
                return;
            }
        }

        Sound sound = sfxMap.get(sfxName);
        if (sound != null) {
            sound.play(sfxVolume);
        }
    }

    public void playSound(String soundName) {
        playSFX(soundName);
    }

    public void stopBGM() {
        if (currentBGM != null && bgmMap.containsKey(currentBGM)) {
            bgmMap.get(currentBGM).stop();
            currentBGM = null;
        }
    }

    public void setBgmMuted(boolean muted) {
        this.isBgmMuted = muted;
        if (muted) {
            stopBGM();
        } else if (targetBGM != null) {
            startMusic(targetBGM);
        }
        savePreferences();
    }

    public boolean isBgmMuted() {
        return isBgmMuted;
    }

    public void setSfxMuted(boolean muted) {
        this.isSfxMuted = muted;
        savePreferences();
    }

    public boolean isSfxMuted() {
        return isSfxMuted;
    }

    public void setMuted(boolean muted) {
        setBgmMuted(muted);
        setSfxMuted(muted);
    }

    public boolean isMuted() {
        return isBgmMuted && isSfxMuted;
    }

    public float getBgmVolume() {
        return bgmVolume;
    }

    public void setBgmVolume(float vol) {
        this.bgmVolume = Math.max(0f, Math.min(1f, vol));
        if (currentBGM != null && bgmMap.containsKey(currentBGM)) {
            Music m = bgmMap.get(currentBGM);
            if (m != null) {
                m.setVolume(this.bgmVolume);
            }
        }
        savePreferences();
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(float vol) {
        this.sfxVolume = Math.max(0f, Math.min(1f, vol));
        savePreferences();
    }
    
    public void dispose() {
        for (Music m : bgmMap.values()) {
            if (m != null) m.dispose();
        }
        for (Sound s : sfxMap.values()) {
            if (s != null) s.dispose();
        }
        bgmMap.clear();
        sfxMap.clear();
    }
}
