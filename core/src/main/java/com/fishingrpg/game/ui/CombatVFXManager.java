package com.fishingrpg.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Dedicated visual effects manager handling rich visual animations for:
 * 1. Player active skills (Kéo Mạnh, Giật Kép, Phá Giáp, Dưỡng Sức, Lướt Dây, Trói Buộc)
 * 2. Consumable item usages (Bánh Mì, Nước Tăng Lực, Mồi Thơm, Dây Cước, Bùa Hoàng Kim...)
 * 3. Fish active skills (Bứt Tốc, Quấy Bùn, Lặn Sâu, Sóng Thần, Cuồng Nộ, Giật Điện, Nhảy Vọt, Sóng Âm, Hút Máu, Bão Tố)
 * 4. Ambient water splashes, tension electricity, and celebration confetti.
 */
public class CombatVFXManager {

    private static CombatVFXManager instance;
    public static CombatVFXManager getInstance() {
        if (instance == null) instance = new CombatVFXManager();
        return instance;
    }

    public static class Particle {
        public float x, y, vx, vy, size, life, maxLife;
        public Color color;
        public int shapeType; // 0: circle, 1: line, 2: shard/triangle, 3: ring
        public float angle, rotSpeed;
        public float gravity = 0f;

        public Particle(float x, float y, float vx, float vy, float size, float life, Color color, int shapeType) {
            this.x = x; this.y = y; this.vx = vx; this.vy = vy;
            this.size = size; this.life = life; this.maxLife = life;
            this.color = new Color(color);
            this.shapeType = shapeType;
            this.angle = MathUtils.random(0f, 360f);
            this.rotSpeed = MathUtils.random(-180f, 180f);
            this.gravity = 0f;
        }

        public Particle(float x, float y, float vx, float vy, float size, float life, Color color, int shapeType, float gravity) {
            this(x, y, vx, vy, size, life, color, shapeType);
            this.gravity = gravity;
        }
    }

    public static class ActiveEffect {
        public String id;
        public float timer;
        public float maxDuration;
        public float cx, cy;
        public Color color;
        public int type; // 1: Player Skill, 2: Item Use, 3: Fish Skill

        public ActiveEffect(String id, int type, float cx, float cy, float duration, Color color) {
            this.id = id;
            this.type = type;
            this.cx = cx;
            this.cy = cy;
            this.timer = duration;
            this.maxDuration = duration;
            this.color = color;
        }
    }

    private final List<Particle> particles = new ArrayList<>();
    private final List<ActiveEffect> effects = new ArrayList<>();

    public void update(float delta) {
        // Update particles
        Iterator<Particle> pIt = particles.iterator();
        while (pIt.hasNext()) {
            Particle p = pIt.next();
            p.life -= delta;
            if (p.life <= 0) {
                pIt.remove();
            } else {
                p.x += p.vx * delta;
                p.y += p.vy * delta;
                p.vy -= p.gravity * delta;
                p.angle += p.rotSpeed * delta;
                p.color.a = Math.max(0f, p.life / p.maxLife);
            }
        }

        // Update active effects
        Iterator<ActiveEffect> eIt = effects.iterator();
        while (eIt.hasNext()) {
            ActiveEffect e = eIt.next();
            e.timer -= delta;
            if (e.timer <= 0) {
                eIt.remove();
            }
        }
    }

    // =========================================================================
    // 1. PLAYER SKILL ANIMATIONS
    // =========================================================================
    public void triggerPlayerSkillVFX(String skillId, float cx, float cy) {
        effects.add(new ActiveEffect(skillId, 1, cx, cy, 1.2f, Color.GOLD));

        if (skillId.equals("keo_manh")) {
            // High-velocity upward kinetic pull chevrons and volcanic gold embers
            for (int i = 0; i < 35; i++) {
                float ang = MathUtils.random(55f, 125f) * MathUtils.degreesToRadians;
                float spd = MathUtils.random(260f, 650f);
                particles.add(new Particle(cx + MathUtils.random(-35f, 35f), cy - 20f,
                        MathUtils.cos(ang) * spd, MathUtils.sin(ang) * spd,
                        MathUtils.random(5f, 10f), MathUtils.random(0.5f, 0.9f),
                        (i % 3 == 0) ? new Color(1f, 1f, 0.6f, 1f) : new Color(1f, 0.65f, 0.15f, 1f), 0));
            }
            // Bursting water droplet spray horizontally
            for (int i = 0; i < 22; i++) {
                float spd = MathUtils.random(140f, 340f);
                float ang = (i % 2 == 0 ? 0f : 180f) * MathUtils.degreesToRadians + MathUtils.random(-0.5f, 0.5f);
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * spd, MathUtils.sin(ang) * spd,
                        MathUtils.random(3.5f, 7.5f), MathUtils.random(0.4f, 0.8f), new Color(0.7f, 0.9f, 1f, 0.9f), 0));
            }
        } else if (skillId.equals("giat_kep")) {
            // Twin fierce cross-slashes
            for (int i = 0; i < 35; i++) {
                float ang = (i % 2 == 0 ? 45f : 135f) * MathUtils.degreesToRadians + MathUtils.random(-0.3f, 0.3f);
                float spd = MathUtils.random(150f, 380f);
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * spd, MathUtils.sin(ang) * spd,
                        MathUtils.random(3f, 7f), MathUtils.random(0.4f, 0.7f), new Color(1f, 0.45f, 0.15f, 1f), 1));
            }
        } else if (skillId.equals("pha_giap")) {
            // Shattering metallic armor shards flying outward
            for (int i = 0; i < 24; i++) {
                float ang = i * (MathUtils.PI2 / 12f);
                float spd = MathUtils.random(120f, 260f);
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * spd, MathUtils.sin(ang) * spd,
                        MathUtils.random(6f, 12f), MathUtils.random(0.6f, 1.0f), new Color(0.85f, 0.90f, 1f, 1f), 2));
            }
        } else if (skillId.equals("duong_suc")) {
            // Green healing motes rising gracefully
            for (int i = 0; i < 30; i++) {
                particles.add(new Particle(cx + MathUtils.random(-60f, 60f), cy - 40f,
                        MathUtils.random(-25f, 25f), MathUtils.random(60f, 140f),
                        MathUtils.random(3f, 7f), MathUtils.random(0.8f, 1.4f), new Color(0.3f, 1f, 0.5f, 1f), 0));
            }
        } else if (skillId.equals("luot_day")) {
            // Cyan water stream ribbons
            for (int i = 0; i < 20; i++) {
                particles.add(new Particle(cx + MathUtils.random(-80f, 80f), cy + MathUtils.random(-40f, 40f),
                        MathUtils.random(-100f, 100f), MathUtils.random(-30f, 30f),
                        MathUtils.random(4f, 8f), MathUtils.random(0.5f, 0.9f), new Color(0.35f, 0.85f, 1f, 1f), 0));
            }
        } else if (skillId.equals("loi_nguyen_troi_buoc")) {
            // Purple eldritch runes and sparks
            for (int i = 0; i < 25; i++) {
                float ang = MathUtils.random(0f, MathUtils.PI2);
                float spd = MathUtils.random(50f, 180f);
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * spd, MathUtils.sin(ang) * spd,
                        MathUtils.random(4f, 8f), MathUtils.random(0.6f, 1.1f), new Color(0.75f, 0.25f, 0.95f, 1f), 2));
            }
        }
    }

    // =========================================================================
    // 2. ITEM USE ANIMATIONS
    // =========================================================================
    public void triggerItemUseVFX(String itemName, float cx, float cy) {
        effects.add(new ActiveEffect(itemName, 2, cx, cy, 1.0f, Color.CYAN));

        if (itemName.equals("Bánh Mì") || itemName.equals("Nước Tăng Lực") || itemName.equals("Thuốc Hồi Stamina")) {
            // Emerald Vitality Burst (+HP text & floating green crosses)
            for (int i = 0; i < 24; i++) {
                particles.add(new Particle(cx + MathUtils.random(-40f, 40f), cy + MathUtils.random(-30f, 30f),
                        MathUtils.random(-35f, 35f), MathUtils.random(50f, 120f),
                        MathUtils.random(4f, 8f), MathUtils.random(0.6f, 1.0f), new Color(0.2f, 1f, 0.45f, 1f), 0));
            }
        } else if (itemName.equals("Dây Câu Tốt") || itemName.equals("Nước Bức Phá")) {
            // Golden Reinforcement Tension Shield
            for (int i = 0; i < 20; i++) {
                float ang = MathUtils.random(0f, MathUtils.PI2);
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * 90f, MathUtils.sin(ang) * 90f,
                        MathUtils.random(3f, 7f), MathUtils.random(0.5f, 0.9f), new Color(1f, 0.90f, 0.35f, 1f), 0));
            }
        } else if (itemName.equals("Mồi Thơm") || itemName.equals("Mồi Rủi Ro")) {
            // Floating fragrant aroma stars and perfume smoke
            for (int i = 0; i < 22; i++) {
                particles.add(new Particle(cx + MathUtils.random(-50f, 50f), cy - 20f,
                        MathUtils.random(-40f, 40f), MathUtils.random(40f, 90f),
                        MathUtils.random(4f, 8f), MathUtils.random(0.7f, 1.3f), new Color(1f, 0.55f, 0.85f, 1f), 2));
            }
        } else if (itemName.equals("Bùa Hoàng Kim") || itemName.equals("Sách Thông Thái")) {
            // Golden coin shower
            for (int i = 0; i < 20; i++) {
                particles.add(new Particle(cx + MathUtils.random(-60f, 60f), cy + 80f,
                        MathUtils.random(-20f, 20f), -MathUtils.random(60f, 140f),
                        MathUtils.random(5f, 9f), MathUtils.random(0.8f, 1.2f), new Color(1f, 0.85f, 0.2f, 1f), 0));
            }
        } else if (itemName.equals("Lưới Đánh Cá")) {
            // Expanding cyan net mesh
            for (int i = 0; i < 24; i++) {
                float ang = i * (MathUtils.PI2 / 12f);
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * 120f, MathUtils.sin(ang) * 120f,
                        5f, 0.8f, new Color(0.4f, 0.95f, 1f, 1f), 1));
            }
        }
    }

    // =========================================================================
    // 3. FISH SKILL ANIMATIONS
    // =========================================================================
    public void triggerFishSkillVFX(String skillId, float cx, float cy) {
        effects.add(new ActiveEffect(skillId, 3, cx, cy, 1.5f, Color.RED));

        if (skillId.equals("but_toc")) {
            // Red fury speed trail and rushing bubbles
            for (int i = 0; i < 26; i++) {
                particles.add(new Particle(cx - MathUtils.random(20f, 90f), cy + MathUtils.random(-25f, 25f),
                        -MathUtils.random(150f, 320f), MathUtils.random(-30f, 30f),
                        MathUtils.random(4f, 9f), MathUtils.random(0.4f, 0.8f), new Color(1f, 0.25f, 0.2f, 1f), 1));
            }
        } else if (skillId.equals("quay_bun")) {
            // Mud swirl and dark murky water splashes
            for (int i = 0; i < 30; i++) {
                float ang = MathUtils.random(0f, MathUtils.PI2);
                float spd = MathUtils.random(40f, 140f);
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * spd, MathUtils.sin(ang) * spd,
                        MathUtils.random(8f, 16f), MathUtils.random(0.8f, 1.4f), new Color(0.38f, 0.26f, 0.14f, 0.85f), 0));
            }
        } else if (skillId.equals("lan_sau")) {
            // Column of deep water bubbles surging upward
            for (int i = 0; i < 32; i++) {
                particles.add(new Particle(cx + MathUtils.random(-40f, 40f), cy - 60f,
                        MathUtils.random(-20f, 20f), MathUtils.random(90f, 220f),
                        MathUtils.random(3f, 8f), MathUtils.random(0.6f, 1.2f), new Color(0.6f, 0.9f, 1f, 0.85f), 0));
            }
        } else if (skillId.equals("song_than")) {
            // Massive tidal wave foam cresting from both sides
            for (int i = 0; i < 40; i++) {
                particles.add(new Particle(cx + (i % 2 == 0 ? -180f : 180f), cy - 40f,
                        (i % 2 == 0 ? 200f : -200f), MathUtils.random(40f, 160f),
                        MathUtils.random(6f, 14f), MathUtils.random(0.6f, 1.2f), new Color(0.85f, 0.95f, 1f, 0.95f), 0));
            }
        } else if (skillId.equals("cuong_no")) {
            // Exploding red spikes and fire aura
            for (int i = 0; i < 36; i++) {
                float ang = MathUtils.random(0f, MathUtils.PI2);
                float spd = MathUtils.random(100f, 280f);
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * spd, MathUtils.sin(ang) * spd,
                        MathUtils.random(5f, 11f), MathUtils.random(0.5f, 1.0f), new Color(1f, 0.15f, 0.15f, 1f), 2));
            }
        } else if (skillId.equals("giat_dien")) {
            // Neon yellow electric sparks jittering along water
            for (int i = 0; i < 28; i++) {
                particles.add(new Particle(cx + MathUtils.random(-60f, 60f), cy + MathUtils.random(-40f, 40f),
                        MathUtils.random(-150f, 150f), MathUtils.random(-150f, 150f),
                        MathUtils.random(2f, 6f), MathUtils.random(0.2f, 0.5f), new Color(1f, 1f, 0.3f, 1f), 1));
            }
        } else if (skillId.equals("nhay_vot")) {
            // Huge splash arc erupting from water
            for (int i = 0; i < 35; i++) {
                float spd = MathUtils.random(120f, 260f);
                float ang = MathUtils.random(40f, 140f) * MathUtils.degreesToRadians;
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * spd, MathUtils.sin(ang) * spd,
                        MathUtils.random(4f, 10f), MathUtils.random(0.6f, 1.1f), new Color(0.75f, 0.92f, 1f, 0.95f), 0));
            }
        } else if (skillId.equals("song_am")) {
            // Expanding sonar rings
            for (int i = 0; i < 20; i++) {
                float ang = i * (MathUtils.PI2 / 10f);
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * 140f, MathUtils.sin(ang) * 140f,
                        4f, 0.9f, new Color(0.2f, 0.9f, 0.8f, 0.9f), 0));
            }
        } else if (skillId.equals("hut_mau") || skillId.equals("muc_mu_hut_mau")) {
            // Crimson life drain drops streaming into fish
            for (int i = 0; i < 28; i++) {
                particles.add(new Particle(cx - MathUtils.random(120f, 220f), cy + MathUtils.random(-50f, 50f),
                        MathUtils.random(120f, 210f), MathUtils.random(-25f, 25f),
                        MathUtils.random(4f, 9f), MathUtils.random(0.7f, 1.2f), new Color(0.95f, 0.12f, 0.35f, 0.95f), 0));
            }
            // Abyssal dark ink spores swirling outward
            for (int i = 0; i < 25; i++) {
                float ang = MathUtils.random(0f, MathUtils.PI2);
                float spd = MathUtils.random(50f, 180f);
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * spd, MathUtils.sin(ang) * spd,
                        MathUtils.random(8f, 18f), MathUtils.random(0.8f, 1.5f), new Color(0.12f, 0.05f, 0.18f, 0.85f), 0));
            }
        } else if (skillId.equals("bao_to")) {
            // Violet thunderbolts striking down
            for (int i = 0; i < 30; i++) {
                particles.add(new Particle(cx + MathUtils.random(-80f, 80f), cy + 180f,
                        MathUtils.random(-40f, 40f), -MathUtils.random(250f, 450f),
                        MathUtils.random(3f, 7f), MathUtils.random(0.3f, 0.7f), new Color(0.75f, 0.45f, 1f, 1f), 1));
            }
        } else if (skillId.equals("quet_xuc_tu")) {
            // Massive cyan & violet shockwave ripples radiating outward from Kraken slam
            for (int i = 0; i < 35; i++) {
                float ang = MathUtils.random(0f, MathUtils.PI2);
                float spd = MathUtils.random(100f, 280f);
                particles.add(new Particle(cx, cy, MathUtils.cos(ang) * spd, MathUtils.sin(ang) * spd,
                        MathUtils.random(5f, 11f), MathUtils.random(0.5f, 0.9f),
                        (i % 2 == 0) ? new Color(0.2f, 0.95f, 0.9f, 0.95f) : new Color(0.7f, 0.2f, 0.95f, 0.95f), 0));
            }
        }
    }

    public void triggerFireworks(float cx, float cy) {
        Color[] colors = {
            new Color(1f, 0.85f, 0.2f, 1f),  // Radiant Gold
            new Color(1f, 0.25f, 0.35f, 1f), // Ruby Crimson
            new Color(0.2f, 0.85f, 1f, 1f),  // Bright Cyan
            new Color(0.35f, 1f, 0.45f, 1f), // Emerald Green
            new Color(0.85f, 0.4f, 1f, 1f),  // Mystic Violet
            new Color(1f, 0.6f, 0.2f, 1f),   // Flaming Amber
            new Color(1f, 1f, 1f, 1f)        // Silver Diamond
        };

        // Exploding starburst particles with realistic gravity
        for (int i = 0; i < 48; i++) {
            float angle = MathUtils.random(0f, MathUtils.PI2);
            float speed = MathUtils.random(80f, 320f);
            float vx = MathUtils.cos(angle) * speed;
            float vy = MathUtils.sin(angle) * speed;
            float life = MathUtils.random(0.8f, 1.6f);
            Color col = colors[MathUtils.random(colors.length - 1)];
            int shape = MathUtils.random(0, 2);
            float size = MathUtils.random(3.5f, 6.5f);
            float grav = MathUtils.random(90f, 150f);
            particles.add(new Particle(cx, cy, vx, vy, size, life, col, shape, grav));
        }

        // Add expanding shockwave flash
        effects.add(new ActiveEffect("fireworks_flash", 1, cx, cy, 0.65f, new Color(1f, 0.95f, 0.5f, 0.9f)));
    }

    // =========================================================================
    // 4. DRAWING ACTIVE VFX SHAPES
    // =========================================================================
    public void drawShapes(ShapeRenderer sr) {
        // Draw complex animated active effects overlays
        for (ActiveEffect e : effects) {
            float progress = 1f - (e.timer / e.maxDuration); // 0.0 -> 1.0
            float alpha = Math.max(0f, e.timer / e.maxDuration);

            if (e.id.equals("keo_manh")) {
                // 1. Dual Kinetic Pull Chevrons (▲ ▲) rushing violently upward!
                float chevronY1 = e.cy - 10f + progress * 160f;
                float chevronY2 = e.cy + 35f + progress * 180f;
                float chevW = 50f * (1f - progress * 0.4f);
                float chevH = 32f * (1f - progress * 0.4f);

                // Lower Chevron ▲
                sr.setColor(1f, 0.55f, 0.1f, 0.85f * alpha);
                sr.triangle(e.cx - chevW, chevronY1 - chevH, e.cx + chevW, chevronY1 - chevH, e.cx, chevronY1);
                sr.setColor(1f, 0.95f, 0.4f, 0.95f * alpha);
                sr.triangle(e.cx - chevW * 0.7f, chevronY1 - chevH * 0.85f, e.cx + chevW * 0.7f, chevronY1 - chevH * 0.85f, e.cx, chevronY1 - 4f);

                // Upper Chevron ▲
                sr.setColor(1f, 0.8f, 0.2f, 0.95f * alpha);
                sr.triangle(e.cx - chevW * 0.85f, chevronY2 - chevH * 0.85f, e.cx + chevW * 0.85f, chevronY2 - chevH * 0.85f, e.cx, chevronY2);
                sr.setColor(1f, 1f, 0.8f, 1f * alpha);
                sr.triangle(e.cx - chevW * 0.55f, chevronY2 - chevH * 0.7f, e.cx + chevW * 0.55f, chevronY2 - chevH * 0.7f, e.cx, chevronY2 - 3f);

                // 2. Blazing vertical line tension surge
                float beamH = 260f * progress;
                sr.setColor(1f, 0.6f, 0.15f, 0.7f * alpha);
                sr.rectLine(e.cx, e.cy - 40f, e.cx, e.cy + beamH, 12f * (1f - progress));
                sr.setColor(1f, 1f, 0.85f, 0.95f * alpha);
                sr.rectLine(e.cx, e.cy - 40f, e.cx, e.cy + beamH, 4f * (1f - progress));

                // 3. Expanding water shockwave ellipses on surface
                float rw = 35f + progress * 160f;
                float rh = 12f + progress * 48f;
                sr.setColor(1f, 0.75f, 0.2f, 0.7f * alpha);
                sr.ellipse(e.cx - rw, e.cy - rh, rw * 2f, rh * 2f);
                sr.setColor(0.6f, 0.9f, 1f, 0.85f * alpha);
                sr.ellipse(e.cx - rw * 0.8f, e.cy - rh * 0.8f, rw * 1.6f, rh * 1.6f);
            } else if (e.id.equals("giat_kep")) {
                // Twin X-cross slashes
                sr.setColor(1f, 0.4f, 0.15f, 0.9f * alpha);
                float span = 45f + progress * 60f;
                sr.rectLine(e.cx - span, e.cy - span, e.cx + span, e.cy + span, 7f * (1f - progress));
                sr.rectLine(e.cx - span, e.cy + span, e.cx + span, e.cy - span, 7f * (1f - progress));
            } else if (e.id.equals("pha_giap")) {
                // Shattered titan armor shockwave & volcanic fracture ring
                float r = 30f + progress * 85f;
                sr.setColor(1f, 0.30f, 0.15f, 0.75f * alpha);
                sr.circle(e.cx, e.cy, r, 28);
                sr.setColor(1f, 0.85f, 0.25f, 0.85f * alpha);
                sr.circle(e.cx, e.cy, r * 0.72f, 24);
                // 4 diagonal fracture cracks
                float fLen = 22f + progress * 45f;
                sr.setColor(1f, 0.95f, 0.80f, alpha);
                sr.rectLine(e.cx - fLen, e.cy - fLen, e.cx + fLen, e.cy + fLen, 3f * (1f - progress));
                sr.rectLine(e.cx - fLen, e.cy + fLen, e.cx + fLen, e.cy - fLen, 3f * (1f - progress));
            } else if (e.id.equals("duong_suc")) {
                // Emerald life spiral circles
                sr.setColor(0.25f, 1f, 0.55f, 0.6f * alpha);
                float rot = progress * 360f * MathUtils.degreesToRadians;
                for (int k = 0; k < 3; k++) {
                    float a = rot + k * (MathUtils.PI2 / 3f);
                    sr.circle(e.cx + MathUtils.cos(a) * 45f, e.cy + MathUtils.sin(a) * 45f, 9f * alpha);
                }
            } else if (e.id.equals("luot_day")) {
                // Hydrofoil wave skim & surging cyan rooster-tail spray
                float waveSpan = progress * 160f;
                sr.setColor(0.20f, 0.85f, 1f, 0.70f * alpha);
                sr.rectLine(e.cx - waveSpan, e.cy - 12f, e.cx + waveSpan, e.cy + 12f, 6f * (1f - progress));
                sr.setColor(1f, 1f, 1f, 0.85f * alpha);
                sr.circle(e.cx + waveSpan * 0.7f, e.cy + 18f * (1f - progress), 8f * (1f - progress));
                sr.circle(e.cx - waveSpan * 0.7f, e.cy - 18f * (1f - progress), 8f * (1f - progress));
                sr.setColor(1f, 0.85f, 0.35f, 0.9f * alpha);
                sr.triangle(e.cx + waveSpan * 0.8f, e.cy + 10f, e.cx + waveSpan * 0.85f + 14f, e.cy, e.cx + waveSpan * 0.6f, e.cy - 8f);
            } else if (e.id.equals("loi_nguyen_troi_buoc")) {
                // Eldritch purple binding chains
                sr.setColor(0.65f, 0.20f, 0.85f, 0.85f * alpha);
                sr.rectLine(e.cx - 50f, e.cy - 30f, e.cx + 50f, e.cy + 30f, 6f);
                sr.rectLine(e.cx - 50f, e.cy + 30f, e.cx + 50f, e.cy - 30f, 6f);
                sr.setColor(1f, 0.6f, 1f, 0.95f * alpha);
                sr.circle(e.cx, e.cy, 18f, 20);
            } else if (e.id.equals("cuong_no")) {
                // Red rage spikes
                sr.setColor(1f, 0.15f, 0.15f, 0.65f * alpha);
                float pulse = 35f + 15f * MathUtils.sin(progress * 25f);
                sr.circle(e.cx, e.cy, pulse, 28);
            } else if (e.id.equals("song_than")) {
                // Tidal wave barriers
                sr.setColor(0.3f, 0.75f, 1f, 0.65f * alpha);
                float waveW = progress * 240f;
                sr.rect(e.cx - waveW, e.cy - 50f, waveW * 2f, 100f);
            } else if (e.id.equals("giat_dien")) {
                // Electric lightning bolts
                sr.setColor(1f, 1f, 0.4f, 0.9f * alpha);
                float lx = e.cx - 60f;
                for (int k = 0; k < 4; k++) {
                    float nx = lx + 30f;
                    float ny = e.cy + MathUtils.random(-25f, 25f);
                    sr.rectLine(lx, e.cy, nx, ny, 3.5f);
                    lx = nx;
                }
            } else if (e.id.equals("muc_mu_hut_mau")) {
                // Expanding abyssal dark ink sphere & crimson life-drain vortex
                float r = 35f + progress * 130f;
                sr.setColor(0.08f, 0.04f, 0.12f, 0.88f * alpha);
                sr.circle(e.cx, e.cy, r, 32);
                sr.setColor(0.75f, 0.10f, 0.35f, 0.75f * alpha);
                sr.circle(e.cx, e.cy, r * 0.75f, 28);
                sr.setColor(0.95f, 0.25f, 0.85f, 0.85f * alpha);
                sr.circle(e.cx, e.cy, r * 0.45f, 24);
            } else if (e.id.equals("fireworks_flash")) {
                float ringR = progress * 140f;
                sr.setColor(e.color.r, e.color.g, e.color.b, 0.45f * alpha);
                sr.circle(e.cx, e.cy, ringR, 32);
                sr.setColor(1f, 1f, 1f, 0.85f * alpha);
                sr.circle(e.cx, e.cy, Math.max(1f, ringR * 0.4f), 24);
            }
        }

        // Draw individual particle elements
        for (Particle p : particles) {
            sr.setColor(p.color);
            if (p.shapeType == 0) {
                // Glowing circle
                sr.circle(p.x, p.y, p.size);
            } else if (p.shapeType == 1) {
                // Streak line
                sr.rectLine(p.x, p.y, p.x + p.vx * 0.08f, p.y + p.vy * 0.08f, p.size);
            } else if (p.shapeType == 2) {
                // Rotating shard
                float rad = p.angle * MathUtils.degreesToRadians;
                float cos = MathUtils.cos(rad) * p.size;
                float sin = MathUtils.sin(rad) * p.size;
                sr.triangle(p.x, p.y + p.size, p.x - cos, p.y - sin, p.x + cos, p.y - sin);
            }
        }
    }
}
