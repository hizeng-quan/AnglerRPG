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
        float cost = (ctx == null || ctx.player == null) ? 20f : ctx.player.getSkillTree().getKeoManhStaminaCost();
        if (ctx != null && ctx.currentFish != null && ctx.currentFish.getPassiveSkill("uy_ap_tu_than") != null) {
            cost *= 1.20f;
        }
        return cost;
    }

    @Override
    public float getBaseCooldown(AbilityContext ctx) {
        return 3.5f;
    }

    @Override
    public boolean execute(AbilityContext ctx) {
        float cost = getStaminaCost(ctx);
        if (!consumeStaminaOrFreeCharge(ctx)) {
            return false;
        }

        float rawDmg = ctx.player.getSkillTree().getKeoManhDamage();
        boolean crit = false;
        if (ctx.player.isSkillEquipped("bao_kich")) {
            if (MathUtils.random(100f) < ctx.player.getSkillTree().getBaoKichChance()) {
                crit = true;
                rawDmg *= 2f;
            }
        }

        float finalDmg = calculateSkillDamage(rawDmg, ctx);
        if (ctx.currentFish != null) {
            if (ctx.fishingManager != null && ctx.fishingManager.fishPhantomInvulnTimer > 0f) {
                if (ctx.ftm != null) ctx.ftm.addText("HƯ VÔ! (MIỄN NHIỄM)", 640f, 400f, Color.PURPLE);
            } else {
                ctx.currentFish.takeDamage(finalDmg);
                applyTrueDamageBonusIfActive(finalDmg, ctx);
                if (ctx.ftm != null) {
                    ctx.ftm.addText(crit ? "BẠO KÍCH! -" + (int) finalDmg : "KÉO MẠNH! -" + (int) finalDmg,
                            640f + MathUtils.random(-30, 30), 400f + MathUtils.random(-20, 20),
                            crit ? Color.GOLD : new Color(1f, 0.45f, 0.2f, 1f));
                }
            }
        }

        ctx.player.increaseTension(15f);
        ctx.fishingManager.combatMessage = crit
                ? "BẠO KÍCH KÉO MẠNH! Xung lực bộc phá (-" + (int) cost + " Thể lực)"
                : "KÉO MẠNH! Giật cần xung lực cực đại! (-" + (int) cost + " Thể lực)";
        ctx.audio.playSFX("sfx_skill");
        ctx.vfx.triggerPlayerSkillVFX("keo_manh", 640f, 360f);
        if (ctx.screenShake != null) ctx.screenShake.shakeMedium();

        return true;
    }
}
