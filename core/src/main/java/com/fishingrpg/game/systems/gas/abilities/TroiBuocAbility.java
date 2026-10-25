package com.fishingrpg.game.systems.gas.abilities;

import com.fishingrpg.game.systems.gas.AbilityContext;
import com.fishingrpg.game.systems.gas.GameplayAbility;
import com.fishingrpg.game.systems.gas.GameplayEffect;

public class TroiBuocAbility extends GameplayAbility {

    public TroiBuocAbility() {
        super("loi_nguyen_troi_buoc", "Trói Buộc", "Lời nguyền đại dương đóng băng cá, triệt tiêu hoàn toàn sức kéo.");
    }

    @Override
    public float getStaminaCost(AbilityContext ctx) {
        return 0f;
    }

    @Override
    public float getBaseCooldown(AbilityContext ctx) {
        return 16.0f;
    }

    @Override
    public boolean execute(AbilityContext ctx) {
        float duration = ctx.player.getSkillTree().getLoiNguyenTroiBuocDuration();

        // Áp dụng GameplayEffect đóng băng cá
        GameplayEffect effect = new GameplayEffect("effect_troi_buoc", "Trói Buộc", duration)
                .setFreezeFish(true)
                .setFishPullMultiplier(0f);
        ctx.fishingManager.getAbilitySystem().applyEffect(effect);
        ctx.fishingManager.troiBuocTimer = duration;

        ctx.fishingManager.combatMessage = "TRÓI BUỘC! Cá bị phong ấn không thể di chuyển!";
        ctx.audio.playSFX("sfx_skill");
        ctx.vfx.triggerPlayerSkillVFX("loi_nguyen_troi_buoc", 640f, 360f);
        if (ctx.screenShake != null) ctx.screenShake.shakeMedium();

        return true;
    }
}
