package com.fishingrpg.game.systems.vfx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

/**
 * Bộ Điều Khiển Rung Chấn Màn Hình Dựa Trên Trauma (Trauma-based Screen Shake Controller).
 * Chuẩn kỹ thuật công nghiệp game (GDC Squirrel Eiserloh):
 * - Rung chấn dựa trên chỉ số Trauma [0.0 -> 1.0], suy giảm mượt theo hàm mũ / thời gian.
 * - Độ rung tính theo hàm phi tuyến (Trauma^2 hoặc Trauma^3): Va chạm nhẹ chỉ rung rất tinh tế,
 *   va chạm mạnh (Boss skill, đứt cước) rung mãnh liệt và giảm dần tự nhiên.
 * - Kết hợp rung dịch chuyển (X, Y) và rung xoay góc (Rotation Angle) tạo cảm giác chấn động vật lý thật.
 * - Tuyệt đối không trôi tâm màn hình (Zero Camera Drift): Luôn hồi quy chính xác 100% về tâm gốc.
 */
public class ScreenShakeController {

    private float trauma = 0f;
    private float traumaDecayRate = 1.35f; // Tốc độ tiêu biến trauma mỗi giây
    private float maxTranslation = 22f;    // Độ dịch chuyển pixel tối đa khi trauma = 1.0
    private float maxAngle = 2.5f;          // Góc nghiêng tối đa (độ) khi trauma = 1.0
    private float frequency = 28f;          // Tần số dao động sóng rung

    private float shakeTimer = 0f;
    private float baseX = 640f;
    private float baseY = 360f;

    // Hit-stop (Micro-freeze / Time dilation) tạo độ nặng đòn
    private float hitStopTimer = 0f;

    public ScreenShakeController() {
        this(640f, 360f);
    }

    public ScreenShakeController(float baseX, float baseY) {
        this.baseX = baseX;
        this.baseY = baseY;
    }

    public void setBasePosition(float x, float y) {
        this.baseX = x;
        this.baseY = y;
    }

    /**
     * Thêm chỉ số chấn động Trauma [0.0, 1.0]. Tự cộng dồn và kẹp trần ở mức 1.0.
     */
    public void addTrauma(float amount) {
        this.trauma = MathUtils.clamp(this.trauma + amount, 0f, 1f);
    }

    /**
     * Rung nhẹ (Ví dụ: Tiếng máy câu, cá nhấp nháy mồi, gợn sóng).
     */
    public void shakeLight() {
        addTrauma(0.25f);
    }

    /**
     * Rung vừa (Ví dụ: Cá đớp mồi, kéo căng dây cước, hoàn thành nhiệm vụ).
     */
    public void shakeMedium() {
        addTrauma(0.50f);
    }

    /**
     * Rung mạnh (Ví dụ: Boss tung tuyệt chiêu, đứt cước, bắt được Boss cá).
     */
    public void shakeHeavy() {
        addTrauma(0.85f);
    }

    /**
     * Rung cực đại (Ví dụ: Nổ vàng Gacha 6★ Lạc Hồng, Thần biển Leviathan trỗi dậy).
     */
    public void shakeExtreme() {
        addTrauma(1.0f);
    }

    /**
     * Kích hoạt hiệu ứng khựng hình thời gian ngắn (Hit-Stop) để tạo cảm giác giật cần đanh thép.
     * @param duration thời gian khựng hình (vd: 0.04s - 0.08s)
     */
    public void triggerHitStop(float duration) {
        this.hitStopTimer = Math.max(this.hitStopTimer, duration);
    }

    public boolean isHitStopped() {
        return hitStopTimer > 0f;
    }

    /**
     * Cập nhật tọa độ rung chấn và áp dụng trực tiếp lên OrthographicCamera.
     */
    public void update(float delta, OrthographicCamera camera) {
        // Cập nhật Hit-stop
        if (hitStopTimer > 0f) {
            hitStopTimer -= delta;
            // Trong thời gian hit-stop, giữ nguyên chấn động đỉnh cao
            delta *= 0.2f;
        }

        if (trauma > 0f) {
            shakeTimer += delta;

            // Tính năng lượng rung phi tuyến: shake = trauma^2
            float shake = trauma * trauma;

            // Tính độ dịch chuyển mượt mà theo hàm điều hòa sóng sin/cos
            float offsetX = maxTranslation * shake * MathUtils.sin(shakeTimer * frequency * 1.0f);
            float offsetY = maxTranslation * shake * MathUtils.cos(shakeTimer * frequency * 1.3f);
            float angle = maxAngle * shake * MathUtils.sin(shakeTimer * frequency * 0.8f);

            // Cập nhật vị trí camera có rung chấn
            camera.position.set(baseX + offsetX, baseY + offsetY, 0f);
            camera.up.set(0, 1, 0);
            camera.rotate(angle);

            // Tiêu biến trauma dần theo thời gian
            trauma = Math.max(0f, trauma - traumaDecayRate * delta);
        } else {
            // Khi hết trauma, đảm bảo 100% camera được trả về vị trí gốc cân bằng hoàn hảo
            camera.position.set(baseX, baseY, 0f);
            camera.up.set(0, 1, 0);
            camera.direction.set(0, 0, -1);
            shakeTimer = 0f;
        }

        camera.update();
    }

    public float getTrauma() {
        return trauma;
    }

    public void stopShake() {
        this.trauma = 0f;
        this.hitStopTimer = 0f;
    }
}
