package com.fishingrpg.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

public class KeybindManager {
    private static KeybindManager instance;

    // Actions
    public static final int ACTION_PULL = 0;
    public static final int ACTION_SKILL_1 = 1;
    public static final int ACTION_SKILL_2 = 2;
    public static final int ACTION_SKILL_3 = 3;
    public static final int ACTION_SKILL_4 = 4;
    public static final int ACTION_QUICK_1 = 5;
    public static final int ACTION_QUICK_2 = 6;
    public static final int ACTION_QUICK_3 = 7;
    public static final int ACTION_QUICK_4 = 8;
    public static final int ACTION_QUICK_5 = 9;
    public static final int ACTION_QUICK_6 = 10;
    public static final int ACTION_QUICK_7 = 11;
    public static final int ACTION_QUICK_8 = 12;
    public static final int ACTION_QUICK_9 = 13;
    public static final int ACTION_COUNT = 14;

    public static final String[] ACTION_NAMES = {
        "Kéo Dây / Giằng Co",
        "Kỹ Năng 1",
        "Kỹ Năng 2",
        "Kỹ Năng 3",
        "Kỹ Năng 4",
        "Phím Tắt 1",
        "Phím Tắt 2",
        "Phím Tắt 3",
        "Phím Tắt 4",
        "Phím Tắt 5",
        "Phím Tắt 6",
        "Phím Tắt 7",
        "Phím Tắt 8",
        "Phím Tắt 9"
    };

    private static final int[] DEFAULT_KEYS = {
        Input.Keys.SPACE,
        Input.Keys.Q,
        Input.Keys.W,
        Input.Keys.E,
        Input.Keys.R,
        Input.Keys.NUM_1,
        Input.Keys.NUM_2,
        Input.Keys.NUM_3,
        Input.Keys.NUM_4,
        Input.Keys.NUM_5,
        Input.Keys.NUM_6,
        Input.Keys.NUM_7,
        Input.Keys.NUM_8,
        Input.Keys.NUM_9
    };

    private static final String PREF_NAME = "FishingRPG_Keybinds";
    private static final String[] PREF_KEYS = {
        "key_pull",
        "key_skill_1",
        "key_skill_2",
        "key_skill_3",
        "key_skill_4",
        "key_quick_1",
        "key_quick_2",
        "key_quick_3",
        "key_quick_4",
        "key_quick_5",
        "key_quick_6",
        "key_quick_7",
        "key_quick_8",
        "key_quick_9"
    };

    private final int[] keyCodes = new int[ACTION_COUNT];

    private KeybindManager() {
        loadPreferences();
    }

    public static KeybindManager getInstance() {
        if (instance == null) {
            instance = new KeybindManager();
        }
        return instance;
    }

    public void loadPreferences() {
        try {
            Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
            for (int i = 0; i < ACTION_COUNT; i++) {
                int code = prefs.getInteger(PREF_KEYS[i], DEFAULT_KEYS[i]);
                if (code <= 0) code = DEFAULT_KEYS[i];
                keyCodes[i] = code;
            }
        } catch (Throwable t) {
            resetDefaults();
        }
    }

    public void savePreferences() {
        try {
            Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
            for (int i = 0; i < ACTION_COUNT; i++) {
                prefs.putInteger(PREF_KEYS[i], keyCodes[i]);
            }
            prefs.flush();
        } catch (Throwable ignored) {}
    }

    public void resetDefaults() {
        for (int i = 0; i < ACTION_COUNT; i++) {
            keyCodes[i] = DEFAULT_KEYS[i];
        }
        savePreferences();
    }

    public int getKeyCode(int action) {
        if (action >= 0 && action < ACTION_COUNT) {
            int code = keyCodes[action];
            if (code <= 0) return DEFAULT_KEYS[action];
            return code;
        }
        return Input.Keys.UNKNOWN;
    }

    /**
     * Checks if newKey is already bound to any action other than the specified action.
     * @return the conflicting action index, or -1 if no conflict.
     */
    public int findConflictAction(int action, int newKey) {
        if (newKey == Input.Keys.UNKNOWN || newKey == Input.Keys.ESCAPE) return -1;
        for (int i = 0; i < ACTION_COUNT; i++) {
            if (i != action && keyCodes[i] == newKey) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Sets keycode for action and automatically swaps with conflicting action if key is already bound.
     * @return the conflicting action index that was swapped, or -1 if clean assignment without conflict.
     */
    public int setKeyCodeWithSwap(int action, int newKey) {
        if (action < 0 || action >= ACTION_COUNT) return -1;
        if (newKey <= 0 || newKey == Input.Keys.ESCAPE) return -1;

        int conflict = findConflictAction(action, newKey);
        int oldKey = keyCodes[action];
        if (conflict != -1) {
            keyCodes[conflict] = (oldKey > 0) ? oldKey : DEFAULT_KEYS[conflict];
        }
        keyCodes[action] = newKey;
        savePreferences();
        return conflict;
    }

    /**
     * Sets the keycode for an action, automatically swapping on conflict.
     * @return true if successfully set.
     */
    public boolean setKeyCode(int action, int newKey) {
        if (action < 0 || action >= ACTION_COUNT) return false;
        if (newKey <= 0 || newKey == Input.Keys.ESCAPE) return false;

        setKeyCodeWithSwap(action, newKey);
        return true;
    }

    public int getPullKey() {
        int code = keyCodes[ACTION_PULL];
        if (code <= 0) return DEFAULT_KEYS[ACTION_PULL];
        return code;
    }

    public int getSkillKey(int slot) {
        if (slot >= 0 && slot < 4) {
            int code = keyCodes[ACTION_SKILL_1 + slot];
            if (code <= 0) return DEFAULT_KEYS[ACTION_SKILL_1 + slot];
            return code;
        }
        return Input.Keys.UNKNOWN;
    }

    public int getQuickslotKey(int slot) {
        if (slot >= 0 && slot < 9) {
            int code = keyCodes[ACTION_QUICK_1 + slot];
            if (code <= 0) return DEFAULT_KEYS[ACTION_QUICK_1 + slot];
            return code;
        }
        return Input.Keys.UNKNOWN;
    }

    public String getKeyDisplayName(int action) {
        int code = getKeyCode(action);
        return formatKeyName(code);
    }

    public String getSkillKeyDisplayName(int slot) {
        int code = getSkillKey(slot);
        return formatKeyName(code);
    }

    public String getQuickslotKeyDisplayName(int slot) {
        int code = getQuickslotKey(slot);
        return formatKeyName(code);
    }

    public static String formatKeyName(int code) {
        if (code == Input.Keys.SPACE) return "SPACE";
        if (code == Input.Keys.ENTER) return "ENTER";
        if (code == Input.Keys.TAB) return "TAB";
        if (code == Input.Keys.SHIFT_LEFT || code == Input.Keys.SHIFT_RIGHT) return "SHIFT";
        if (code == Input.Keys.CONTROL_LEFT || code == Input.Keys.CONTROL_RIGHT) return "CTRL";
        if (code == Input.Keys.ALT_LEFT || code == Input.Keys.ALT_RIGHT) return "ALT";
        if (code == Input.Keys.BACKSPACE) return "BACK";
        if (code >= Input.Keys.NUM_0 && code <= Input.Keys.NUM_9) {
            return String.valueOf(code - Input.Keys.NUM_0);
        }
        if (code >= Input.Keys.NUMPAD_0 && code <= Input.Keys.NUMPAD_9) {
            return "NUM " + (code - Input.Keys.NUMPAD_0);
        }
        String str = Input.Keys.toString(code);
        return (str != null && !str.isEmpty()) ? str.toUpperCase() : "PHÍM " + code;
    }
}
