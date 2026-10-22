package com.fishingrpg.game.systems.gas;

import com.fishingrpg.game.entities.Fish;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.systems.AudioManager;
import com.fishingrpg.game.systems.FishingManager;
import com.fishingrpg.game.systems.vfx.ScreenShakeController;
import com.fishingrpg.game.ui.CombatVFXManager;
import com.fishingrpg.game.ui.FloatingTextManager;

/**
 * Ngữ Cảnh Kích Hoạt Kỹ Năng (Ability Context).
 * Cung cấp đầy đủ thông tin về Người chơi, Cá mục tiêu, VFX, SFX và Rung chấn màn hình.
 */
public class AbilityContext {

    public final Player player;
    public final Fish currentFish;
    public final FishingManager fishingManager;
    public final FloatingTextManager ftm;
    public final CombatVFXManager vfx;
    public final ScreenShakeController screenShake;
    public final AudioManager audio;

    public AbilityContext(Player player, Fish currentFish, FishingManager fishingManager,
                          FloatingTextManager ftm, ScreenShakeController screenShake) {
        this.player = player;
        this.currentFish = currentFish;
        this.fishingManager = fishingManager;
        this.ftm = ftm;
        this.vfx = CombatVFXManager.getInstance();
        this.screenShake = screenShake;
        this.audio = AudioManager.getInstance();
    }
}
