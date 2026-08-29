package com.fishingrpg.game.entities;

import com.fishingrpg.game.skills.SkillTree;
import com.fishingrpg.game.entities.equipment.Substat;

/**
 * Calculates final stats based on Equipment, SkillTree, and Combat stats.
 */
public class PlayerStatCalculator {

    public static void applySkillBonuses(Player player, PlayerCombat combat, PlayerEquipment equipment, SkillTree skillTree) {
        float dayThepBonus = 0f;
        if (player.isSkillEquipped("day_thep")) dayThepBonus = skillTree.getDayThepBonus();
        
        float theLucBonus = 0f;
        if (player.isSkillEquipped("the_luc_doi_dao")) theLucBonus = skillTree.getTheLucDoiDaoBonus();
        
        float rodStaminaBonus = equipment.getRodStaminaBonus();
        float lineTensionBonus = equipment.getLineTensionBonus();
        
        combat.applyStatBonuses(100f, theLucBonus, rodStaminaBonus, dayThepBonus, lineTensionBonus);
    }

    public static float getBasePullDamage(PlayerEquipment equipment) {
        float dmg = 20f; // Base pull damage
        dmg += equipment.getRodDamageBonus();
        dmg *= (1f + equipment.getTotalSubstat(Substat.Type.DAMAGE_BONUS) / 100f);
        if ("Thợ Săn".equals(equipment.getActiveSetName())) dmg *= 1.2f;
        return dmg;
    }
}
