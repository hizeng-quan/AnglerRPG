package com.fishingrpg.game.systems.gas.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.fishingrpg.game.systems.gas.AbilityContext;
import com.fishingrpg.game.systems.gas.GameplayAbility;

public class GiatKepAbility extends GameplayAbility {

    public GiatKepAbility() {
        super("giat_kep", "Giật Kép", "Tung hai cú giật cần liên hoàn uy lực cực đại gây sát thương bộc phá.");
    }

    @Override
    public float getStaminaCost(AbilityContext ctx) {
        float cost = 40f;
        if (ctx != null && ctx.currentFish != null && ctx.currentFish.getPassiveSkill("uy_ap_tu_than") != null) {
            cost *= 1.20f;
        }
        return cost;
    }

    @Override
    public float getBaseCooldown(AbilityContext ctx) {
        return 8.0f;
    }

    @Override
    public boolean execute(AbilityContext ctx) {
        float cost = getStaminaCost(ctx);
        if (!consumeStaminaOrFreeCharge(ctx)) {
            return false;
        }

        float rawDmg = ctx.player.getSkillTree().getKeoManhDamage() * ctx.player.getSkillTree().getGiatKepDamageMult();
        float finalDmg = calculateSkillDamage(rawDmg, ctx);
        if (ctx.currentFish != null) {
            if (ctx.fishingManager != null && ctx.fishingManager.fishPhantomInvulnTimer > 0f) {
                if (ctx.ftm != null) ctx.ftm.addText("HƯ VÔ! (MIỄN NHIỄM)", 640f, 360f, Color.PURPLE);
            } else {
                ctx.currentFish.takeDamage(finalDmg);
                applyTrueDamageBonusIfActive(finalDmg, ctx);
                if (ctx.ftm != null) {
                    ctx.ftm.addText("GIẬT KÉP! -" + (int) finalDmg, 640f + MathUtils.random(-50, 50),
                            360f + MathUtils.random(-50, 50), Color.RED);
                }
            }
        }

        ctx.player.increaseTension(30f);
        ctx.fishingManager.combatMessage = "GIẬT KÉP! Đòn tấn công liên hoàn vũ bão! (-" + (int) cost + " Thể lực)";
        ctx.audio.playSFX("sfx_skill");
        ctx.vfx.triggerPlayerSkillVFX("giat_kep", 640f, 360f);
        if (ctx.screenShake != null) ctx.screenShake.shakeHeavy();

        return true;
    }
}
