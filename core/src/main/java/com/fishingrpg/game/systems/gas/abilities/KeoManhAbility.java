package com.fishingrpg.game.systems.gas.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.fishingrpg.game.systems.gas.AbilityContext;
import com.fishingrpg.game.systems.gas.GameplayAbility;

public class KeoManhAbility extends GameplayAbility {

    public KeoManhAbility() {
        super("keo_manh", "Kéo Mạnh", "Giật cần tạo xung lực lớn gây sát thương mạnh lên cá.");
    }

    @Override
    public float getStaminaCost(AbilityContext ctx) {
        if (ctx.player == null) return 20f;
        return ctx.player.getSkillTree().getKeoManhStaminaCost();
    }

    @Override
    public float getBaseCooldown(AbilityContext ctx) {
        return 3.0f;
    }

    @Override
    public boolean execute(AbilityContext ctx) {
        float cost = getStaminaCost(ctx);
        if (!ctx.player.consumeStamina(cost)) {
            if (ctx.ftm != null) ctx.ftm.addText("Không đủ Thể lực (-" + (int) cost + ")!", 640f, 380f, new Color(1f, 0.45f, 0.4f, 1f));
            return false;
        }

        float dmg = ctx.player.getSkillTree().getKeoManhDamage();
        boolean crit = false;
        if (ctx.player.isSkillEquipped("bao_kich")) {
            if (MathUtils.random(100f) < ctx.player.getSkillTree().getBaoKichChance()) {
                crit = true;
                dmg *= 2f;
            }
        }

        float dmgMult = ctx.fishingManager.getAbilitySystem().getDamageMultiplier();
        float fishDef = 1f;
        if (ctx.currentFish != null) {
            if (ctx.currentFish.getPassiveSkill("vo_cung") != null) fishDef -= 0.3f;
            if (ctx.currentFish.getPassiveSkill("vay_cung") != null) fishDef -= 0.5f;
            float finalDmg = dmg * dmgMult * Math.max(0.2f, fishDef);
            ctx.currentFish.takeDamage(finalDmg);

            if (ctx.ftm != null) {
                ctx.ftm.addText(crit ? "BẠO KÍCH! -" + (int) finalDmg : "KÉO MẠNH! -" + (int) finalDmg,
                        640f + MathUtils.random(-30, 30), 400f + MathUtils.random(-20, 20),
                        crit ? Color.GOLD : new Color(1f, 0.45f, 0.2f, 1f));
            }
        }

        ctx.player.increaseTension(15f);
        ctx.fishingManager.combatMessage = crit
                ? "BẠO KÍCH KÉO MẠNH! Xung lực bộc phá (-" + (int) cost + " Thể lực)"
                : "KÉO MẠNH! Giật cần xung lực cực đại! (-" + (int) cost + " Thể lực)";

        ctx.audio.playSFX("sfx_skill");
        ctx.vfx.triggerPlayerSkillVFX("keo_manh", 640f, 340f);
        if (ctx.screenShake != null) {
            if (crit) ctx.screenShake.shakeHeavy();
            else ctx.screenShake.shakeMedium();
        }

        return true;
    }
}
