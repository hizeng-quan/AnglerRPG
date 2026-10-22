package com.fishingrpg.game.systems.vfx;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

/**
 * Bộ Điều Khiển Nội Suy Chuyển Động & Cảm Giác Chơi (Juice & Easing Animator).
 * Cung cấp các đường cong nội suy nảy mượt (Bounce, Elastic, Swing, Back)
 * giúp giao diện UI, thẻ phần thưởng, popup và hiệu ứng trong game sống động, có "Game Juice".
 */
public class JuiceAnimator {

    /**
     * Hiệu ứng nảy mượt (Pop-in Bounce) cho Popup, Dialog hoặc Thẻ nhận cá mới.
     * Giá trị phóng to nhẹ (overshoot 1.15x) rồi dao động hồi phục về 1.0x.
     * @param elapsed Thời gian đã trôi qua kể từ khi mở (giây)
     * @param duration Tổng thời lượng của hiệu ứng xuất hiện (vd: 0.35s - 0.5s)
     * @return Tỷ lệ phóng to (Scale) từ 0.0f -> 1.15f -> 1.0f
     */
    public static float popupScale(float elapsed, float duration) {
        if (duration <= 0f) return 1.0f;
        float progress = MathUtils.clamp(elapsed / duration, 0f, 1f);
        return Interpolation.swingOut.apply(0f, 1.0f, progress);
    }

    /**
     * Hiệu ứng nảy bật đàn hồi (Elastic Pop-in) cho vật phẩm Huyền thoại / 6★ Gacha.
     * @param elapsed Thời gian đã trôi qua (giây)
     * @param duration Tổng thời lượng hiệu ứng (vd: 0.6s)
     * @return Tỷ lệ phóng to đàn hồi
     */
    public static float elasticScale(float elapsed, float duration) {
        if (duration <= 0f) return 1.0f;
        float progress = MathUtils.clamp(elapsed / duration, 0f, 1f);
        return Interpolation.elasticOut.apply(0f, 1.0f, progress);
    }

    /**
     * Hiệu ứng đập nảy tức thời (Punch Scale) khi nhấp chuột vào nút hoặc khi cá bị giật trúng đòn.
     * @param timer Thời gian từ lúc nhận tác động
     * @param duration Thời lượng tác động (vd: 0.15s)
     * @param punchAmount Độ phồng tối đa (vd: 0.25f nghĩa là phồng lên 1.25x)
     * @return Tỷ lệ scale hiện tại
     */
    public static float punchScale(float timer, float duration, float punchAmount) {
        if (timer >= duration || duration <= 0f) return 1.0f;
        float progress = timer / duration;
        float punch = (1.0f - progress) * MathUtils.sin(progress * MathUtils.PI);
        return 1.0f + punch * punchAmount;
    }

    /**
     * Nhịp thở hào quang (Rhythmic Pulse) cho nút bấm quan trọng hoặc trang bị phát sáng.
     * @param timer Bộ đếm thời gian liên tục (giây)
     * @param speed Tốc độ nhịp thở (chu kỳ/giây)
     * @param min Tỷ lệ tối thiểu
     * @param max Tỷ lệ tối đa
     */
    public static float pulse(float timer, float speed, float min, float max) {
        float wave = (MathUtils.sin(timer * speed) + 1.0f) * 0.5f;
        return min + wave * (max - min);
    }

    /**
     * Đường cong mượt mà Ease-Out-Quad cho thanh tiến độ (Progress bar lerp).
     */
    public static float easeOutQuad(float from, float to, float progress) {
        float p = MathUtils.clamp(progress, 0f, 1f);
        return Interpolation.pow2Out.apply(from, to, p);
    }

    /**
     * Đường cong mượt mà mờ dần (Alpha Fade In).
     */
    public static float fadeIn(float elapsed, float duration) {
        if (duration <= 0f) return 1.0f;
        float p = MathUtils.clamp(elapsed / duration, 0f, 1f);
        return Interpolation.fade.apply(0f, 1.0f, p);
    }
}
