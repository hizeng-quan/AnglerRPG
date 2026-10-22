package com.fishingrpg.game.systems.gas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.fishingrpg.game.systems.gas.abilities.*;

import java.util.*;

/**
 * Thành Phần Hệ Thống Kỹ Năng (Ability System Component - ASC).
 * Trái tim của Gameplay Ability System (GAS):
 * - Quản lý đăng ký tất cả GameplayAbility.
 * - Tự động cập nhật thời gian hồi chiêu (Cooldowns) chính xác từng frame.
 * - Quản lý vòng đời của các hiệu ứng Buff/Debuff (GameplayEffect) và tính toán chỉ số động.
 * - Khắc phục triệt để các biến cooldown và timer rời rạc trước đây.
 */
public class AbilitySystemComponent {

    private final Map<String, GameplayAbility> registeredAbilities = new LinkedHashMap<>();
    private final Map<String, Float> cooldowns = new HashMap<>();
    private final List<GameplayEffect> activeEffects = new ArrayList<>();

    public AbilitySystemComponent() {
        registerDefaultAbilities();
    }

    private void registerDefaultAbilities() {
        registerAbility(new KeoManhAbility());
        registerAbility(new PhaGiapAbility());
        registerAbility(new LuotDayAbility());
        registerAbility(new TroiBuocAbility());
        registerAbility(new GiatKepAbility());
        registerAbility(new DuongSucAbility());
        registerAbility(new NemXaAbility());
    }

    public void registerAbility(GameplayAbility ability) {
        registeredAbilities.put(ability.getId(), ability);
    }

    public GameplayAbility getAbility(String id) {
        return registeredAbilities.get(id);
    }

    /**
     * Cập nhật thời gian hồi chiêu và các hiệu ứng trạng thái đang kích hoạt.
     */
    public void update(float delta, AbilityContext ctx) {
        // 1. Cập nhật Cooldowns
        for (Map.Entry<String, Float> entry : cooldowns.entrySet()) {
            float cd = entry.getValue();
            if (cd > 0f) {
                entry.setValue(Math.max(0f, cd - delta));
            }
        }

        // 2. Cập nhật Active GameplayEffects
        Iterator<GameplayEffect> it = activeEffects.iterator();
        while (it.hasNext()) {
            GameplayEffect effect = it.next();
            effect.update(delta);

            // Xử lý hiệu ứng theo chu kỳ (Periodic tick)
            if (effect.getStaminaRegenPerSec() > 0f && ctx != null && ctx.player != null) {
                ctx.player.consumeStamina(-effect.getStaminaRegenPerSec() * delta);
            }

            if (effect.isExpired()) {
                it.remove();
            }
        }
    }

    /**
     * Kích hoạt một kỹ năng theo ID qua luồng chuẩn hóa của GAS.
     */
    public boolean tryActivateAbility(String abilityId, AbilityContext ctx, float cdMult) {
        if (abilityId == null || ctx == null) return false;

        GameplayAbility ability = registeredAbilities.get(abilityId);
        if (ability == null) {
            return false;
        }

        // Kiểm tra xem kỹ năng đã được mở khóa trong cây kỹ năng chưa
        if (ctx.player.getSkillTree().getSkill(abilityId) == null ||
            !ctx.player.getSkillTree().getSkill(abilityId).isUnlocked()) {
            if (ctx.ftm != null) {
                ctx.ftm.addText("Kỹ năng chưa mở khóa!", 640f + MathUtils.random(-25, 25), 380f, Color.ORANGE);
            }
            ctx.audio.playSFX("sfx_button");
            return false;
        }

        // Kiểm tra Cooldown
        float currentCd = getCooldown(abilityId);
        if (currentCd > 0f) {
            if (ctx.ftm != null) {
                ctx.ftm.addText(ability.getName() + " đang hồi (" + String.format(Locale.US, "%.1fs", currentCd) + ")!",
                        640f, 380f, Color.GRAY);
            }
            return false;
        }

        // Kiểm tra điều kiện & thể lực
        if (!ability.canActivate(ctx)) {
            float cost = ability.getStaminaCost(ctx);
            if (ctx.ftm != null) {
                ctx.ftm.addText("Không đủ Thể lực (-" + (int) cost + ")!", 640f, 380f, new Color(1f, 0.45f, 0.4f, 1f));
            }
            return false;
        }

        // Thực thi kỹ năng
        boolean success = ability.execute(ctx);
        if (success) {
            // Thiết lập thời gian hồi chiêu
            float finalCd = ability.getBaseCooldown(ctx) * cdMult;
            cooldowns.put(abilityId, finalCd);

            // Cập nhật tiến độ nhiệm vụ dùng kỹ năng
            if (ctx.player.getQuestManager() != null) {
                ctx.player.getQuestManager().addProgress("skill", 1);
            }
            return true;
        }

        return false;
    }

    /**
     * Thêm một GameplayEffect (Buff/Debuff) vào hệ thống.
     * Tự động làm mới thời gian nếu hiệu ứng cùng ID đã tồn tại (Refresh on stack).
     */
    public void applyEffect(GameplayEffect newEffect) {
        if (newEffect == null) return;
        for (int i = 0; i < activeEffects.size(); i++) {
            GameplayEffect existing = activeEffects.get(i);
            if (existing.getId().equals(newEffect.getId())) {
                activeEffects.set(i, newEffect);
                return;
            }
        }
        activeEffects.add(newEffect);
    }

    public boolean hasEffect(String effectId) {
        if (effectId == null) return false;
        for (GameplayEffect e : activeEffects) {
            if (e.getId().equals(effectId)) return true;
        }
        return false;
    }

    public GameplayEffect getEffect(String effectId) {
        if (effectId == null) return null;
        for (GameplayEffect e : activeEffects) {
            if (e.getId().equals(effectId)) return e;
        }
        return null;
    }

    public float getCooldown(String abilityId) {
        if (abilityId == null) return 0f;
        Float cd = cooldowns.get(abilityId);
        return cd != null ? cd : 0f;
    }

    /**
     * Tính toán tổng hệ số sát thương từ tất cả Buff/Debuff đang hoạt động.
     */
    public float getDamageMultiplier() {
        float mult = 1.0f;
        for (GameplayEffect e : activeEffects) {
            mult *= e.getDamageMultiplier();
        }
        return mult;
    }

    /**
     * Tính toán hệ số lực kéo của cá từ tất cả Buff/Debuff đang hoạt động.
     */
    public float getFishPullMultiplier() {
        float mult = 1.0f;
        for (GameplayEffect e : activeEffects) {
            mult *= e.getFishPullMultiplier();
        }
        return mult;
    }

    /**
     * Kiểm tra xem cá có đang bị đóng băng (Trói buộc) hoàn toàn hay không.
     */
    public boolean isFishFrozen() {
        for (GameplayEffect e : activeEffects) {
            if (e.isFreezeFish()) return true;
        }
        return false;
    }

    /**
     * Xóa sạch toàn bộ Buff/Debuff khi kết thúc hoặc bắt đầu lượt câu mới.
     */
    public void clearEffects() {
        activeEffects.clear();
    }

    /**
     * Đặt lại toàn bộ thời gian hồi chiêu khi người chơi bắt đầu phiên câu mới.
     */
    public void resetCooldowns() {
        cooldowns.clear();
    }
}
