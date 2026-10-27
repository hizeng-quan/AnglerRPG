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
        float cost = 30f;
        if (ctx != null && ctx.currentFish != null && ctx.currentFish.getPassiveSkill("uy_ap_tu_than") != null) {
            cost *= 1.20f;
        }
        return cost;
    }

    @Override
    public float getBaseCooldown(AbilityContext ctx) {
        return 14.0f;
    }

    @Override
    public boolean execute(AbilityContext ctx) {
        float cost = getStaminaCost(ctx);
        if (!consumeStaminaOrFreeCharge(ctx)) {
            return false;
        }

        float baseDmg = ctx.player.getSkillTree().getPhaGiapDamage();
        float finalDmg = calculateSkillDamage(baseDmg, ctx);

        if (ctx.currentFish != null) {
            if (ctx.fishingManager != null && ctx.fishingManager.fishPhantomInvulnTimer > 0f) {
                if (ctx.ftm != null) {
                    ctx.ftm.addText("HƯ VÔ! (MIỄN NHIỄM)", 640f, 380f, Color.PURPLE);
                }
            } else if (ctx.currentFish.hasShield()) {
                // Co che Pha Giap dac biet voi Giap:
                // Sat thuong goc: XUYEN THANG VAO MAU THAT
                ctx.currentFish.takeTrueDamage(baseDmg);
                // Sat thuong cong them: TRU VAO GIAP
                float bonusDmg = Math.max(0f, finalDmg - baseDmg);
                if (bonusDmg > 0f) {
                    ctx.currentFish.damageShield(bonusDmg);
                }

                if (ctx.ftm != null) {
                    ctx.ftm.addText("XUYÊN GIÁP! -" + (int) baseDmg, 640f + MathUtils.random(-30, 30),
                            380f + MathUtils.random(-20, 20), Color.RED);
                    if (bonusDmg > 0f) {
                        ctx.ftm.addText("VÀO GIÁP! -" + (int) bonusDmg, 640f + MathUtils.random(-30, 30),
                                340f + MathUtils.random(-20, 20), Color.CYAN);
                    }
                }
            } else {
                ctx.currentFish.takeDamage(finalDmg);
                if (ctx.ftm != null) {
                    ctx.ftm.addText("PHÁ GIÁP! -" + (int) finalDmg, 640f + MathUtils.random(-50, 50),
                            360f + MathUtils.random(-50, 50), Color.RED);
                }
            }
        }

        // Debuff 10s
        float debuffValue = ctx.player.getSkillTree().getPhaGiapDebuff() / 100f;
        GameplayEffect effect = new GameplayEffect("effect_pha_giap", "Phá Giáp", 10.0f)
                .setDamageMultiplier(1.0f + debuffValue);
        ctx.fishingManager.getAbilitySystem().applyEffect(effect);

        ctx.fishingManager.combatMessage = "PHÁ GIÁP! Cá nhận thêm sát thương trong 10s! (-" + (int) cost + " Thể lực)";
        ctx.audio.playSFX("sfx_skill");
        ctx.vfx.triggerPlayerSkillVFX("pha_giap", 640f, 360f);
        if (ctx.screenShake != null) ctx.screenShake.shakeMedium();

        return true;
    }
}
