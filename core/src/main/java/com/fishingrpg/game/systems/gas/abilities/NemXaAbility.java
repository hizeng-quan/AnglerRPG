package com.fishingrpg.game.systems.gas.abilities;

import com.fishingrpg.game.systems.gas.AbilityContext;
import com.fishingrpg.game.systems.gas.GameplayAbility;

public class NemXaAbility extends GameplayAbility {

    public NemXaAbility() {
        super("nem_xa", "Ném Xa", "Tập trung tinh thần, đảm bảo cú quăng cần tiếp theo chắc chắn Perfect.");
    }

    @Override
    public float getStaminaCost(AbilityContext ctx) {
        return 0f;
    }

    @Override
    public float getBaseCooldown(AbilityContext ctx) {
        return 1.0f;
    }

    @Override
    public boolean execute(AbilityContext ctx) {
        ctx.fishingManager.isNextCastPerfect = true;
        ctx.fishingManager.combatMessage = "NÉM XA: Cú quăng tiếp theo chắc chắn Perfect!";
        ctx.audio.playSFX("sfx_button");
        return true;
    }
}
