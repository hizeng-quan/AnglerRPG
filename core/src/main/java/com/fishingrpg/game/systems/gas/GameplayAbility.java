package com.fishingrpg.game.systems.gas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * Lớp Kỹ Năng Cơ Sở Trong Gameplay Ability System (Gameplay Ability).
 * Mọi kỹ năng của người chơi (Q, W, E, R) đều kế thừa từ lớp này,
 * tự quản lý chi phí thể lực, thời gian hồi chiêu, hoạt ảnh VFX và rung chấn màn hình.
 */
public abstract class GameplayAbility {

    protected final String id;
    protected final String name;
    protected final String description;

    public GameplayAbility(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    /**
     * Chi phí Thể lực (Stamina) tiêu hao khi kích hoạt.
     */
    public abstract float getStaminaCost(AbilityContext ctx);

    /**
     * Thời gian hồi chiêu gốc (Base Cooldown tính bằng giây).
     */
    public abstract float getBaseCooldown(AbilityContext ctx);

    /**
     * Kiểm tra điều kiện kích hoạt kỹ năng.
     * Hỗ trợ hiệu ứng Độc Dược Ma Quái (3 lần dùng skill miễn phí 0 thể lực).
     */
    public boolean canActivate(AbilityContext ctx) {
        if (ctx.player == null) return false;
        if (ctx.player.getFreeStaminaSkillCharges() > 0) return true;
        if (ctx.player.getCurrentStamina() < getStaminaCost(ctx)) return false;
        return true;
    }

    /**
     * Tiêu trừ Thể lực hoặc tiêu hao 1 charge Độc Dược Ma Quái (0 Stamina cost).
     */
    public boolean consumeStaminaOrFreeCharge(AbilityContext ctx) {
        if (ctx == null || ctx.player == null) return false;
        if (ctx.player.getFreeStaminaSkillCharges() > 0) {
            ctx.player.consumeFreeStaminaSkillCharge();
            if (ctx.ftm != null) {
                ctx.ftm.addText("ĐỘC DƯỢC: MIỄN PHÍ THỂ LỰC! (Còn " + ctx.player.getFreeStaminaSkillCharges() + ")", 640f, 420f, Color.PURPLE);
            }
            return true;
        }
        float cost = getStaminaCost(ctx);
        if (!ctx.player.consumeStamina(cost)) {
            if (ctx.ftm != null) ctx.ftm.addText("Không đủ Thể lực (-" + (int) cost + ")!", 640f, 380f, new Color(1f, 0.45f, 0.4f, 1f));
            return false;
        }
        return true;
    }

    /**
     * Tác động sát thương chuẩn cộng thêm nếu Bùa Huyết Nguyệt đang kích hoạt (+10% True Damage).
     */
    public void applyTrueDamageBonusIfActive(float damageDealt, AbilityContext ctx) {
        if (ctx == null || ctx.currentFish == null || damageDealt <= 0f) return;
        if (ctx.player != null && ctx.player.getBloodMoonTalismanTimer() > 0f) {
            float trueBonus = damageDealt * 0.10f;
            ctx.currentFish.takeTrueDamage(trueBonus);
            if (ctx.ftm != null) {
                ctx.ftm.addText("BÙA HUYẾT NGUYỆT: +" + (int)trueBonus + " CHUẨN!", 640f + MathUtils.random(-30, 30), 400f, Color.SCARLET);
            }
        }
    }

    /**
     * Thực thi tác động của kỹ năng lên người chơi và cá.
     * @return true nếu kích hoạt thành công, false nếu thất bại
     */
    public abstract boolean execute(AbilityContext ctx);

    public float calculateSkillDamage(float baseDmg, AbilityContext ctx) {
        if (ctx == null || ctx.currentFish == null) return baseDmg;
        if (ctx.fishingManager != null && ctx.fishingManager.fishPhantomInvulnTimer > 0f) {
            return 0f;
        }
        float mult = 1.0f;
        if (ctx.fishingManager != null) {
            mult *= ctx.fishingManager.getAbilitySystem().getDamageMultiplier();
            if (ctx.fishingManager.thanatosVulnerableTimer > 0f) {
                mult *= 2.0f; // Thanatos choang x2 sat thuong
            }
        }
        // Noi tai Ca Skeleton [Cot Cach Bat Diet]: Nhan +50% sat thuong tu ky nang
        if (ctx.currentFish.getPassiveSkill("cot_cach_bat_diet") != null) {
            mult *= 1.50f;
        }
        float fishDef = 1.0f;
        if (ctx.currentFish.getPassiveSkill("vo_cung") != null) fishDef -= 0.3f;
        if (ctx.currentFish.getPassiveSkill("vay_cung") != null) fishDef -= 0.5f;
        return baseDmg * mult * Math.max(0.2f, fishDef);
    }
}
