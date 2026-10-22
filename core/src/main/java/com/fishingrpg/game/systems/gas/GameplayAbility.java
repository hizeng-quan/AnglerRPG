package com.fishingrpg.game.systems.gas;

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
     */
    public boolean canActivate(AbilityContext ctx) {
        if (ctx.player == null) return false;
        if (ctx.player.getCurrentStamina() < getStaminaCost(ctx)) return false;
        return true;
    }

    /**
     * Thực thi tác động của kỹ năng lên người chơi và cá.
     * @return true nếu kích hoạt thành công, false nếu thất bại
     */
    public abstract boolean execute(AbilityContext ctx);
}
