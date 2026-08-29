package com.fishingrpg.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Luu ky luc kich thuoc (can nang kg) cho tung loai ca giua cac session.
 * Dung LibGDX Preferences (tu dong luu vao file tren dia).
 */
public class SizeRecord {

    private static final String PREFS_NAME  = "AnglerRPG_Records";
    private static final String KEY_WEIGHT  = "_maxWeight";
    private static final String KEY_LENGTH  = "_maxLength";
    private static final String KEY_CAUGHT  = "_caught";   // da tung cau duoc chua

    // -------------------------------------------------------------------------

    /** Can nang ky luc cua loai ca, tra 0 neu chua tung cau. */
    public static float getRecord(String fishName) {
        return prefs().getFloat(fishName + KEY_WEIGHT, 0f);
    }

    public static float getRecordLength(String fishName) {
        return prefs().getFloat(fishName + KEY_LENGTH, 0f);
    }

    /** Ca co tung duoc cau lan nao chua. */
    public static boolean isFirstCatch(String fishName) {
        return !prefs().getBoolean(fishName + KEY_CAUGHT, false);
    }

    /**
     * Kiem tra va cap nhat ky luc.
     * @return true neu pha ky luc (hoac la ky luc dau tien).
     */
    public static boolean checkAndUpdate(String fishName, float weightKg, float lengthCm) {
        Preferences p = prefs();
        boolean first  = !p.getBoolean(fishName + KEY_CAUGHT, false);
        
        float wRecord = p.getFloat(fishName + KEY_WEIGHT, 0f);
        float lRecord = p.getFloat(fishName + KEY_LENGTH, 0f);
        
        boolean newWRecord = weightKg > wRecord;
        boolean newLRecord = lengthCm > lRecord;

        if (first || newWRecord) {
            p.putFloat(fishName + KEY_WEIGHT, weightKg);
        }
        if (first || newLRecord) {
            p.putFloat(fishName + KEY_LENGTH, lengthCm);
        }
        p.putBoolean(fishName + KEY_CAUGHT, true);
        p.flush();
        return first || newWRecord || newLRecord;
    }

    // -------------------------------------------------------------------------

    private static Preferences prefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }
}
