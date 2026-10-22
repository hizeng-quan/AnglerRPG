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
        return 40f;
    }

    @Override
    public float getBaseCooldown(AbilityContext ctx) {
        return 5f;
    }

    @Override
    public boolean execute(AbilityContext ctx) {
        float cost = getStaminaCost(ctx);
        if (!ctx.player.consumeStamina(cost)) {
            if (ctx.ftm != null) ctx.ftm.addText("Không đủ Thể lực (-40)!", 640f, 380f, new Color(1f, 0.45f, 0.4f, 1f));
            return false;
        }

        float dmg = ctx.player.getSkillTree().getKeoManhDamage() * ctx.player.getSkillTree().getGiatKepDamageMult();
        float dmgMult = ctx.fishingManager.getAbilitySystem().getDamageMultiplier();
        float fishDef = 1f;
        if (ctx.currentFish != null) {
            if (ctx.currentFish.getPassiveSkill("vo_cung") != null) fishDef -= 0.3f;
            if (ctx.currentFish.getPassiveSkill("vay_cung") != null) fishDef -= 0.5f;
            float finalDmg = dmg * dmgMult * Math.max(0.2f, fishDef);
            ctx.currentFish.takeDamage(finalDmg);

            if (ctx.ftm != null) {
                ctx.ftm.addText("GIẬT KÉP! -" + (int) finalDmg, 640f + MathUtils.random(-50, 50),
                        360f + MathUtils.random(-50, 50), Color.RED);
            }
        }

        ctx.player.increaseTension(30f);
        ctx.fishingManager.combatMessage = "GIẬT KÉP! Đòn tấn công liên hoàn vũ bão! (-40 Thể lực)";
        ctx.audio.playSFX("sfx_skill");
        ctx.vfx.triggerPlayerSkillVFX("giat_kep", 640f, 360f);
        if (ctx.screenShake != null) ctx.screenShake.shakeHeavy();

        return true;
    }
}
