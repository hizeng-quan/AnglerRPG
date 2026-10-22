package com.fishingrpg.game.systems.gas.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.fishingrpg.game.systems.gas.AbilityContext;
import com.fishingrpg.game.systems.gas.GameplayAbility;
import com.fishingrpg.game.systems.gas.GameplayEffect;

public class PhaGiapAbility extends GameplayAbility {

    public PhaGiapAbility() {
        super("pha_giap", "Phá Giáp", "Tấn công điểm yếu, khiến cá nhận thêm sát thương trong 10 giây.");
    }

    @Override
    public float getStaminaCost(AbilityContext ctx) {
        return 30f;
    }

    @Override
    public float getBaseCooldown(AbilityContext ctx) {
        return 20f;
    }

    @Override
    public boolean execute(AbilityContext ctx) {
        float cost = getStaminaCost(ctx);
        if (!ctx.player.consumeStamina(cost)) {
            if (ctx.ftm != null) ctx.ftm.addText("Không đủ Thể lực (-30)!", 640f, 380f, new Color(1f, 0.45f, 0.4f, 1f));
            return false;
        }

        float hpDmg = ctx.player.getSkillTree().getPhaGiapDamage();
        float fishDef = 1f;
        if (ctx.currentFish != null) {
            if (ctx.currentFish.getPassiveSkill("vo_cung") != null) fishDef -= 0.3f;
            if (ctx.currentFish.getPassiveSkill("vay_cung") != null) fishDef -= 0.5f;
            float finalDmg = hpDmg * Math.max(0.2f, fishDef);
            ctx.currentFish.takeDamage(finalDmg);

            if (ctx.ftm != null) {
                ctx.ftm.addText("PHÁ GIÁP! -" + (int) finalDmg, 640f + MathUtils.random(-50, 50),
                        360f + MathUtils.random(-50, 50), Color.RED);
            }
        }

        // Áp dụng GameplayEffect debuff lên cá trong 10 giây
        float debuffValue = ctx.player.getSkillTree().getPhaGiapDebuff() / 100f;
        GameplayEffect effect = new GameplayEffect("effect_pha_giap", "Phá Giáp", 10.0f)
                .setDamageMultiplier(1.0f + debuffValue);
        ctx.fishingManager.getAbilitySystem().applyEffect(effect);

        ctx.fishingManager.combatMessage = "PHÁ GIÁP! Cá nhận thêm sát thương trong 10s! (-30 Thể lực)";
        ctx.audio.playSFX("sfx_skill");
        ctx.vfx.triggerPlayerSkillVFX("pha_giap", 640f, 360f);
        if (ctx.screenShake != null) ctx.screenShake.shakeMedium();

        return true;
    }
}
