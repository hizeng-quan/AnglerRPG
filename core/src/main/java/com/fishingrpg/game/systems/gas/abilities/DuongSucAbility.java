package com.fishingrpg.game.systems.gas.abilities;

import com.fishingrpg.game.systems.gas.AbilityContext;
import com.fishingrpg.game.systems.gas.GameplayAbility;
import com.fishingrpg.game.systems.gas.GameplayEffect;

public class DuongSucAbility extends GameplayAbility {

    public DuongSucAbility() {
        super("duong_suc", "Dưỡng Sức", "Vận sức điều hòa nhịp thở, hồi phục mạnh mẽ Thể lực.");
    }

    @Override
    public float getStaminaCost(AbilityContext ctx) {
        return 0f;
    }

    @Override
    public float getBaseCooldown(AbilityContext ctx) {
        return 9.0f;
    }

    @Override
    public boolean execute(AbilityContext ctx) {
        float channelTime = ctx.player.getSkillTree().getDuongSucChannelTime();
        float totalHeal = 50f;
        float regenPerSec = totalHeal / Math.max(0.1f, channelTime);

        // Áp dụng GameplayEffect hồi thể lực theo thời gian
        GameplayEffect effect = new GameplayEffect("effect_duong_suc", "Dưỡng Sức", channelTime)
                .setStaminaRegenPerSec(regenPerSec);
        ctx.fishingManager.getAbilitySystem().applyEffect(effect);
        ctx.fishingManager.duongSucChannelTimer = channelTime;

        ctx.fishingManager.combatMessage = "DƯỠNG SỨC! Đang hồi phục thể lực nhanh chóng!";
        ctx.audio.playSFX("sfx_success");
        ctx.vfx.triggerPlayerSkillVFX("duong_suc", 640f, 360f);
        if (ctx.screenShake != null) ctx.screenShake.shakeLight();

        return true;
    }
}
