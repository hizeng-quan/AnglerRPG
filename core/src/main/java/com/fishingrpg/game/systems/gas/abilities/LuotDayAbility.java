package com.fishingrpg.game.systems.gas.abilities;

import com.fishingrpg.game.systems.gas.AbilityContext;
import com.fishingrpg.game.systems.gas.GameplayAbility;
import com.fishingrpg.game.systems.gas.GameplayEffect;

public class LuotDayAbility extends GameplayAbility {

    public LuotDayAbility() {
        super("luot_day", "Lướt Dây", "Thao tác nới dây điêu luyện, làm suy giảm mạnh sức kéo của cá.");
    }

    @Override
    public float getStaminaCost(AbilityContext ctx) {
        return 0f;
    }

    @Override
    public float getBaseCooldown(AbilityContext ctx) {
        return 15f;
    }

    @Override
    public boolean execute(AbilityContext ctx) {
        float duration = ctx.player.getSkillTree().getLuotDayDuration();
        float mult = ctx.player.getSkillTree().getLuotDayMultiplier();

        // Áp dụng GameplayEffect giảm lực kéo cá
        GameplayEffect effect = new GameplayEffect("effect_luot_day", "Lướt Dây", duration)
                .setFishPullMultiplier(1.0f / Math.max(1f, mult));
        ctx.fishingManager.getAbilitySystem().applyEffect(effect);

        ctx.fishingManager.combatMessage = "LƯỚT DÂY! (Làm suy yếu sức cản của cá)";
        ctx.audio.playSFX("sfx_skill");
        ctx.vfx.triggerPlayerSkillVFX("luot_day", 640f, 360f);
        if (ctx.screenShake != null) ctx.screenShake.shakeLight();

        return true;
    }
}
