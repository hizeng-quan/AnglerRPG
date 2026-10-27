package com.fishingrpg.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.fishingrpg.game.data.SizeRecord;
import com.fishingrpg.game.entities.Fish;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.loot.Catchable;
import com.fishingrpg.game.loot.CatchableType;
import com.fishingrpg.game.loot.LootTable;
import com.fishingrpg.game.ui.FloatingTextManager;
import com.badlogic.gdx.graphics.Color;
import com.fishingrpg.game.systems.AudioManager;

public class FishingManager {
    public boolean isBloodMoonActive() {
        if (!com.fishingrpg.game.systems.EventManager.isHalloweenActive()) return false;
        if (player != null && player.getManualBloodMoonTimer() > 0f) return true;
        java.time.LocalTime now = java.time.LocalTime.now();
        int hour = now.getHour();
        if (hour >= 18 || hour < 6) {
            int minute = now.getMinute();
            return ((minute / 15) % 2) == 0;
        }
        return false;
    }


    public enum State {
        IDLE, PRE_CAST, CASTING, WAITING, BITE, COMBAT, RESULT
    }

    private Player player;
    public State state = State.IDLE;
    public float stateTimer = 0f;
    public Catchable currentCatch;
    public Fish currentFish;
    
    public float keoManhCooldown = 0f;

    public float castCursorX = 0f;
    public int castDirection = 1;
    public float castSpeed = 400f;
    public float castBonus = 0f;

    public float slackTimer = 0f;
    private float biteWindow = 1.0f;

    public float fishButTocTimer = 0f;
    public float fishPhunMucTimer = 0f;
    public float fishGiatDienTimer = 0f;
    public float fishNhayVotTimer = 0f;
    public float fishLanSauTimer = 0f;
    public float fishBaoToTimer = 0f;
    public float fishPhunMucMuTimer = 0f;
    public float fishHutMauTimer = 0f;
    public float fishSongAmTimer = 0f;
    public float combatDuration = 0f;

    public float fishQuayBunTimer = 0f;
    public float fishSongThanTimer = 0f;
    public float fishCuongNoTimer = 0f;
    public String combatMessage = "";
    public String resultLine    = "";
    private boolean rewardDone = false;
    public boolean isLineBroken = false;
    public boolean lastEncounterSuccess = false;

    // --- Dao Xa & Marlin Boss Mechanics ---
    public float playerHoldSpaceDuration = 0f;
    public float marlinJumpCooldown = 8.0f;
    public boolean marlinEnrageActive = false;
    public float marlinSurgeTimer = 0f;
    public int marlinPullStacks = 0;
    public float marlinStackTimer = 0f;

    // --- Halloween & Thanatos Mechanics (HLW-03) ---
    public float fishSkillGCDTimer = 0f;
    public float fishPhantomInvulnTimer = 0f;
    public float frankHoldSpaceDuration = 0f;
    public float frankStunTimer = 0f;
    public boolean frankRevived = false;
    public float haiXaSietCuocTimer = 0f;
    public float haiXaDocTimer = 0f;
    public boolean thanatosSilenceNextSkill = false;
    public float thanatosVucXoayTimer = 0f;
    public float thanatosVulnerableTimer = 0f;
    public float playerWeaknessTimer = 0f;
    public float playerSkillLockedTimer = 0f;
    public float playerSealStaminaRegenTimer = 0f;
    public int thanatosReHookStep = 0;
    public int thanatosReHookSuccessCount = 0;
    public boolean thanatosReHookActive = false;
    public boolean dauLauReHookActive = false;

    public float jackOCooldown = 8.0f;
    public float dauLauCooldown = 9.0f;
    public float phantomSkillCooldown = 10.0f;
    public float phantomInvulnCooldown = 8.0f;
    public float haiXaSietCooldown = 12.0f;
    public float haiXaDocCooldown = 16.0f;
    public float thanatosTienghuCooldown = 12.0f;
    public float thanatosVucXoayCooldown = 14.0f;
    public float thanatosAmHonCooldown = 18.0f;
    public boolean thanatosPhase3Active = false;
    public boolean thanatosUltTriggered = false;

    public float fishCanDayTimer = 0f;
    public float fishCanDayCooldown = 0f;
    public float fishDoiHuongTimer = 0f;
    public float fishTroLucTextTimer = 0f;
    public float fishPhongDienFxTimer = 0f;
    public float fishXoayNuocTimer = 0f;
    public float fishVucDaTimer = 0f;
    public boolean vucDaDebuffActive = false;
    public float fishCanXeTimer = 0f;
    public boolean fishCanXe30Percent = false;
    public boolean fishCoBuomSecondJump = false;
    public boolean fishCoBuomActive = false;
    public boolean jumpLandingCueShown = false;
    public boolean jumpRehookHandled = false;
    public float totalJumpDuration = 0f;
    public float jumpHoldSpaceDuration = 0f;
    public boolean jumpFailedEarly = false;
    public int jumpSpacePressCount = 0;

    // Passive & Mechanics Float Text Notification Flags
    private boolean thinhMuiMauNotified = false;
    private boolean nuongHaiLuuNotified = false;
    private boolean thanHoCoThachNotified = false;
    private boolean gaiDocNotified = false;
    private boolean muiKiemHoldWarned = false;
    private com.fishingrpg.game.entities.Fish.State prevFishState = com.fishingrpg.game.entities.Fish.State.IDLE;

    // --- New mechanics fields ---
    public Catchable predictedCatch = null;
    public Catchable pendingBiteCatch = null;
    public Catchable lastCatch = null;
    public boolean showCatchSidebar = false;
    public String lastCatchExtra = "";
    public String lastCatchResultLine = "";
    public int lastCatchDiamonds = 0;
    public float phaGiapCooldown = 0f;
    public float luotDayCooldown = 0f;
    public float troiBuocCooldown = 0f;
    public float phaGiapTimer = 0f;
    public float phaGiapDebuff = 0f;
    public float luotDayTimer = 0f;
    public float troiBuocTimer = 0f;
    public boolean phanXaActive = false;
    public boolean isNextCastPerfect = false;
    public float perfectCastBuffTimer = 0f;

    public String castResultText = "";
    public float castResultTimer = 0f;
    
    // Casting Flight Animation & Active Buffs Report
    public float castFlightTimer = 0f;
    public static final float CAST_FLIGHT_DURATION = 0.70f;
    public float castBuffBannerTimer = 0f;
    public String castTimingText = "";
    public java.util.ArrayList<String> castBuffList = new java.util.ArrayList<>();
    public boolean isReeling = false;

    // Escape Notification
    public float fishEscapeTimer = 0f;
    public String fishEscapeMessage = "";

    // Reel-Up Leap Animation (Hoạt ảnh kéo cá/rác/kho báu vọt lên khỏi mặt nước)
    public float reelUpTimer = 0f;
    public static final float REEL_UP_DURATION = 0.9f;
    public Catchable reelUpCatch = null;
    public float reelUpStartX = 640f, reelUpStartY = 340f;

    public boolean bossSkillJustUsed = false;
    private float maxTensionRatioInFight = 0f;
    private FloatingTextManager ftm;
    private com.fishingrpg.game.systems.vfx.ScreenShakeController screenShake;
    private final com.fishingrpg.game.systems.gas.AbilitySystemComponent abilitySystem = new com.fishingrpg.game.systems.gas.AbilitySystemComponent();

    public com.fishingrpg.game.systems.gas.AbilitySystemComponent getAbilitySystem() {
        return abilitySystem;
    }

    public float getEffectiveMaxTension() {
        if (player == null) return 100f;
        float max = player.getMaxLineTension();
        if (fishCanDayTimer > 0f) max *= 0.90f;
        if (fishCanXeTimer > 0f) {
            max *= (fishCanXe30Percent ? 0.70f : 0.80f);
        }
        if (currentFish != null && currentFish.getPassiveSkill("ap_luc_sau") != null) {
            max *= 0.75f;
        }
        if (player.isOverclockPotionActive()) {
            max *= 0.70f;
        }
        return max;
    }

    public float getEffectiveMaxTensionRatio() {
        if (player == null || player.getMaxLineTension() <= 0f) return 1.0f;
        return Math.min(1.0f, getEffectiveMaxTension() / player.getMaxLineTension());
    }

    public FishingManager(Player player) {
        this.player = player;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public void setFloatingTextManager(FloatingTextManager ftm) {
        this.ftm = ftm;
    }

    public void setScreenShake(com.fishingrpg.game.systems.vfx.ScreenShakeController screenShake) {
        this.screenShake = screenShake;
    }

    public void startCasting() {
        AudioManager.getInstance().playSFX("sfx_cast");
        state = State.CASTING;
        castCursorX = 0f;
        castDirection = 1;
        castSpeed = 400f;
        combatMessage = "CĂN VÀO VÙNG XANH ĐỂ QUĂNG CẦN!";
        resultLine = "";
        currentCatch = null;
        currentFish = null;
        
        keoManhCooldown = 0f;
        giatKepCooldown = 0f;
        duongSucCooldown = 0f;
        phaGiapCooldown = 0f;
        luotDayCooldown = 0f;
        troiBuocCooldown = 0f;
        duongSucChannelTimer = 0f;
        abilitySystem.resetCooldowns();
        abilitySystem.clearEffects();
    }

    public void startWaiting(float bonus) {
        state = State.WAITING;
        castBonus = bonus;
        stateTimer = MathUtils.random(2f, 5f) - (bonus > 1f ? 1f : 0f); // Bonus giup ca nhanh can hon
        if (player.getWhetstoneCharges() > 0) {
            stateTimer *= 0.6f; // Lưỡi câu sắc bén thu hút cá cắn nhanh hơn 40%
        }
        if (player.getSonarCharges() > 0) {
            predictedCatch = LootTable.rollCatch(player);
            pendingBiteCatch = predictedCatch;
        } else {
            predictedCatch = null;
            pendingBiteCatch = null;
        }
        combatMessage = "";
        rewardDone = false;
        
        castFlightTimer = CAST_FLIGHT_DURATION;
        castBuffBannerTimer = 3.2f;

        if (bonus >= 2f) {
            castResultText = "PERFECT!";
            castTimingText = "PERFECT (+25% Hiếm, -50% Thời Gian Chờ)";
            player.getQuestManager().addProgress("perfect", 1);
            AudioManager.getInstance().playSFX("sfx_success");
        } else if (bonus > 0f) {
            castResultText = "GOOD";
            castTimingText = "GOOD (+10% Hiếm, -20% Thời Gian Chờ)";
        } else {
            castResultText = "NORMAL";
            castTimingText = "CHUẨN (Bình Thường)";
        }
        castResultTimer = 1.5f;

        // Collect all active fishing buffs from timing, equipment, items, and skills
        castBuffList.clear();
        if (bonus >= 2f) {
            player.setCastRarityBoost(25f);
            castBuffList.add("QUĂNG HOÀN HẢO (PERFECT): +25% Tỷ lệ Hiếm, Cắn nhanh 50%");
        } else if (bonus > 0f) {
            player.setCastRarityBoost(10f);
            castBuffList.add("QUĂNG CHUẨN XÁC (GOOD): +10% Tỷ lệ Hiếm, Cắn nhanh 20%");
        } else {
            player.setCastRarityBoost(0f);
        }
        if (player.isBaitBuffActive()) {
            castBuffList.add("MỒI THƠM: 100% Cá & Rương (0% Rác)");
        }
        if (player.getRarityBoost() > 0) {
            castBuffList.add("MÓC CÂU: +" + (int) player.getRarityBoost() + "% Tỷ lệ Cá Hiếm");
        }
        if (player.isSetBonusActive() && player.getEquippedRod() != null) {
            String sName = player.getEquippedRod().setName;
            if (sName.equals("Đại Dương")) castBuffList.add("SET ĐẠI DƯƠNG: +20% Tỷ lệ Hiếm");
            else if (sName.equals("Vực Thẳm")) castBuffList.add("SET VỰC THẲM: x3 Tỷ lệ Boss Vực Thẳm");
            else if (sName.equals("Hải Tặc")) castBuffList.add("SET HẢI TẶC: +30% Tỷ lệ Kho Báu");
            else if (sName.equals("Hoàng Gia")) castBuffList.add("SET HOÀNG GIA: +30% EXP & Vàng");
            else if (sName.equals("Thần Khí Lạc Hồng")) castBuffList.add("SET LẠC HỒNG: Miễn nhiễm kỹ năng Boss");
            else if (sName.equals("Dạ Hội Bí Ngô")) castBuffList.add("SET DẠ HỘI BÍ NGÔ: +35% EXP, +25% Vàng & 35% rơi Kẹo");
            else if (sName.equals("Huyết Nguyệt Thần Vương")) castBuffList.add("SET HUYẾT NGUYỆT: Perfect x2 Damage (2s), Hút 3% HP cá thành Thể Lực");
        }
        if (player.getTreasureMagnetCharges() > 0) {
            castBuffList.add("NAM CHÂM KHO BÁU: +45% Tỷ lệ Rương");
        }
        if (player.getWhetstoneCharges() > 0) {
            castBuffList.add("ĐÁ MÀI: -40% Chờ Cá Cắn, +25% Lực Kéo");
        }
        if (player.isSkillEquipped("may_man_co_ban")) {
            castBuffList.add("MAY MẮN CƠ BẢN: +" + (int) player.getSkillTree().getMayManCoBanBonus() + "% Cá Hiếm");
        }
        if (player.isSkillEquipped("ngu_dan_tinh_anh")) {
            castBuffList.add("NGƯ DÂN TINH ANH: +" + (int) player.getSkillTree().getNguDanTinhAnhBonus() + "% Cá Epic+");
        }
        if (player.isSkillEquipped("tho_san_kho_bau")) {
            castBuffList.add("THỢ SĂN KHO BÁU: +" + (int) player.getSkillTree().getThoSanKhoBauBonus() + "% Rương");
        }
    }

    public void triggerBite() {
        AudioManager.getInstance().playSFX("sfx_bite");
        if (screenShake != null) {
            screenShake.shakeMedium();
            screenShake.triggerHitStop(0.045f);
        }
        state = State.BITE;
        float pBonus = 0f;
        if (player.isSkillEquipped("nhanh_tay")) {
            pBonus = player.getSkillTree().getNhanhTayBonusTime();
        }
        biteWindow = 1.0f + pBonus + castBonus * 0.5f;
        stateTimer = biteWindow;
        combatMessage = "";

        if (pendingBiteCatch == null) {
            pendingBiteCatch = (predictedCatch != null) ? predictedCatch : LootTable.rollCatch(player);
        }
        if (player.getSonarCharges() > 0) {
            predictedCatch = pendingBiteCatch;
        } else {
            predictedCatch = null;
        }
        if (pendingBiteCatch instanceof Fish && ftm != null) {
            Fish f = (Fish) pendingBiteCatch;
            boolean isBoss = (f.getRarity() == Fish.Rarity.BOSS);
            boolean isNew = SizeRecord.isFirstCatch(f.getName());
            if (isBoss && isNew) {
                ftm.addText("BOSS!", 640f, 450f, new Color(1f, 0.22f, 0.22f, 1f));
                ftm.addText("NEW!", 640f, 410f, new Color(1f, 0.88f, 0.25f, 1f));
            } else if (isBoss) {
                ftm.addText("BOSS!", 640f, 430f, new Color(1f, 0.22f, 0.22f, 1f));
            } else if (isNew) {
                ftm.addText("NEW!", 640f, 430f, new Color(1f, 0.88f, 0.25f, 1f));
            }
        }
    }

    public void startEncounter() {
        if (pendingBiteCatch != null) {
            currentCatch = pendingBiteCatch;
            pendingBiteCatch = null;
            predictedCatch = null;
        } else if (predictedCatch != null) {
            currentCatch = predictedCatch;
            predictedCatch = null;
        } else {
            currentCatch = LootTable.rollCatch(player);
        }
        player.setBaitBuffActive(false);
        rewardDone = false;
        if (!currentCatch.requiresCombat()) {
            finishEncounter(true);
            return;
        }

        state = State.COMBAT;
        currentFish = (Fish) currentCatch;
        boolean isFirst = SizeRecord.isFirstCatch(currentFish.getName());
        currentFish.setFirstCatch(isFirst);
        player.resetForNewEncounter();
        maxTensionRatioInFight = 0f;
        abilitySystem.clearEffects();
        abilitySystem.resetCooldowns();
        keoManhCooldown = 0f;
        giatKepCooldown = 0f;
        duongSucCooldown = 0f;
        phaGiapCooldown = 0f;
        luotDayCooldown = 0f;
        troiBuocCooldown = 0f;
        duongSucChannelTimer = 0f;

        isLineBroken = false;
        rewardDone = false;
        phanXaActive = false;
        phaGiapTimer = 0f;
        luotDayTimer = 0f;
        troiBuocTimer = 0f;
        slackTimer = 0f;
        fishButTocTimer = 0f;
        fishPhunMucTimer = 0f;
        fishGiatDienTimer = 0f;
        fishNhayVotTimer = 0f;
        fishBaoToTimer = 0f;
        fishPhunMucMuTimer = 0f;
        fishHutMauTimer = 0f;
        fishSongAmTimer = 0f;
        combatDuration = 0f;

        fishQuayBunTimer = 0f;
        fishSongThanTimer = 0f;
        fishCuongNoTimer = 0f;
        combatMessage = "";
        
        playerHoldSpaceDuration = 0f;
        marlinJumpCooldown = 8.0f;
        marlinEnrageActive = false;
        marlinSurgeTimer = 0f;
        marlinPullStacks = 0;
        marlinStackTimer = 0f;
        fishCanDayTimer = 0f;
        fishCanDayCooldown = 0f;
        fishDoiHuongTimer = 0f;
        fishTroLucTextTimer = 0f;
        fishPhongDienFxTimer = 0f;
        fishXoayNuocTimer = 0f;
        fishVucDaTimer = 0f;
        vucDaDebuffActive = false;
        fishCanXeTimer = 0f;
        fishCanXe30Percent = false;
        fishCoBuomSecondJump = false;
        fishCoBuomActive = false;
        jumpLandingCueShown = false;
        jumpRehookHandled = false;
        totalJumpDuration = 0f;
        jumpHoldSpaceDuration = 0f;
        jumpFailedEarly = false;
        jumpSpacePressCount = 0;
        thinhMuiMauNotified = false;
        nuongHaiLuuNotified = false;
        thanHoCoThachNotified = false;
        gaiDocNotified = false;
        muiKiemHoldWarned = false;
        prevFishState = com.fishingrpg.game.entities.Fish.State.IDLE;
        player.setStaminaHealMultiplier(1.0f);
        fishSkillGCDTimer = 0f;
        fishPhantomInvulnTimer = 0f;
        frankHoldSpaceDuration = 0f;
        frankStunTimer = 0f;
        frankRevived = false;
        haiXaSietCuocTimer = 0f;
        haiXaDocTimer = 0f;
        thanatosSilenceNextSkill = false;
        thanatosVucXoayTimer = 0f;
        thanatosVulnerableTimer = 0f;
        playerWeaknessTimer = 0f;
        playerSkillLockedTimer = 0f;
        playerSealStaminaRegenTimer = 0f;
        thanatosReHookStep = 0;
        thanatosReHookSuccessCount = 0;
        thanatosReHookActive = false;
        dauLauReHookActive = false;
        jackOCooldown = 8.0f;
        dauLauCooldown = 9.0f;
        phantomSkillCooldown = 10.0f;
        phantomInvulnCooldown = 8.0f;
        haiXaSietCooldown = 12.0f;
        haiXaDocCooldown = 16.0f;
        thanatosTienghuCooldown = 12.0f;
        thanatosVucXoayCooldown = 14.0f;
        thanatosAmHonCooldown = 18.0f;
        thanatosPhase3Active = false;
        thanatosUltTriggered = false;

        accumDamage = 0f;
        accumTimer = 0f;

        if ("PERFECT!".equals(castResultText) && player.getEquipment() != null
                && player.getEquipment().isSetBonusActive()
                && "Huyết Nguyệt Thần Vương".equals(player.getEquipment().getActiveSetName())) {
            perfectCastBuffTimer = 2.0f;
            if (ftm != null) ftm.addText("HUYẾT NGUYỆT BỘC PHÁ: x2 SÁT THƯƠNG (2s)!", 640f, 400f, Color.SCARLET);
        } else {
            perfectCastBuffTimer = 0f;
        }
    }

    public void finishEncounter(boolean success) {
        finishEncounter(success, "Thất bại!");
    }

    public void finishEncounter(boolean success, String failReason) {
        state = State.PRE_CAST;
        if (!rewardDone) {
            rewardDone = true;
            lastEncounterSuccess = success;
            abilitySystem.clearEffects();
            abilitySystem.resetCooldowns();
            keoManhCooldown = 0f;
            giatKepCooldown = 0f;
            duongSucCooldown = 0f;
            phaGiapCooldown = 0f;
            luotDayCooldown = 0f;
            troiBuocCooldown = 0f;
            duongSucChannelTimer = 0f;
            perfectCastBuffTimer = 0f;
            
            playerHoldSpaceDuration = 0f;
            marlinJumpCooldown = 8.0f;
            marlinEnrageActive = false;
            marlinSurgeTimer = 0f;
            marlinPullStacks = 0;
            marlinStackTimer = 0f;
            fishCanDayTimer = 0f;
            fishCanDayCooldown = 0f;
            fishDoiHuongTimer = 0f;
            fishTroLucTextTimer = 0f;
            fishPhongDienFxTimer = 0f;
            fishXoayNuocTimer = 0f;
            fishVucDaTimer = 0f;
            vucDaDebuffActive = false;
            fishCanXeTimer = 0f;
            fishCanXe30Percent = false;
            fishCoBuomSecondJump = false;
            fishCoBuomActive = false;
            jumpLandingCueShown = false;
            jumpRehookHandled = false;
            totalJumpDuration = 0f;
            jumpHoldSpaceDuration = 0f;
            jumpFailedEarly = false;
            jumpSpacePressCount = 0;
            thinhMuiMauNotified = false;
            nuongHaiLuuNotified = false;
            thanHoCoThachNotified = false;
            gaiDocNotified = false;
            muiKiemHoldWarned = false;
            prevFishState = com.fishingrpg.game.entities.Fish.State.IDLE;
            player.setStaminaHealMultiplier(1.0f);
            
            if (success) {
                player.recordSuccessfulCatch();
                lastCatch = currentCatch;
                reelUpCatch = currentCatch;
                reelUpTimer = REEL_UP_DURATION;
                reelUpStartX = 640f;
                reelUpStartY = 320f;
                showCatchSidebar = false;
            }
            
            // Clear one-time buffs
            player.resetFreeStaminaSkillCharges();
            player.setRiskyBaitActive(false);
            player.setBaitBuffActive(false); // Mồi thơm mất khi cá cắn câu mà bỏ lỡ / hoàn thành câu
            player.setOverclockPotionActive(false);
            if (player.getWhetstoneCharges() > 0) {
                player.consumeWhetstoneCharge();
            }
            if (player.getTreasureMagnetCharges() > 0) {
                player.consumeTreasureMagnetCharge();
            }
            // Tinh chỉnh: Số lượt thấu thị sonar không bị giảm nếu không kéo cá mà để cá chạy đi (CHẬM TAY!)
            if (player.getSonarCharges() > 0 && !"CHẬM TAY!".equals(failReason)) {
                player.consumeSonarCharge();
            }
            player.setCastRarityBoost(0f);
            predictedCatch = null;
            pendingBiteCatch = null;
            
            if (!success) {
                isLineBroken = true;
                combatMessage = failReason;
                resultLine = "";
                fishEscapeTimer = 3.5f;
                if ("ĐỨT DÂY CÂU!".equals(failReason)) {
                    fishEscapeMessage = "DÂY CƯỚC BỊ ĐỨT, CÁ ĐÃ CHẠY MẤT!";
                    if (ftm != null) ftm.addText("ĐỨT DÂY CÂU!", 640f, 360f, new Color(1f, 0.25f, 0.25f, 1f));
                } else if ("CHẬM TAY!".equals(failReason)) {
                    fishEscapeMessage = "CHẬM TAY RỒI! CÁ ĐÃ ĂN HẾT MỒI VÀ CHẠY MẤT!";
                    if (ftm != null) ftm.addText("CHẬM TAY!", 640f, 360f, new Color(1f, 0.35f, 0.25f, 1f));
                } else if (failReason != null && !failReason.isEmpty()) {
                    fishEscapeMessage = failReason;
                    if (ftm != null) ftm.addText(failReason, 640f, 360f, new Color(1f, 0.35f, 0.25f, 1f));
                }
                if (screenShake != null) screenShake.shakeHeavy();
            } else {
                if (screenShake != null) screenShake.shakeLight();
                float xpMod = 1f;
                if (player.getExpBuffCharges() > 0) {
                    player.consumeExpBuffCharge();
                    xpMod = 1.5f;
                }
                
                int baseGold = currentCatch.getGoldValue();
                int goldBonus = 0;
                if (player.getGoldBuffCharges() > 0) {
                    player.consumeGoldBuffCharge();
                    // Bùa Hoàng Kim: +50% Vàng, đặt trần thưởng thêm tối đa 500 Vàng
                    goldBonus += Math.min((int)(baseGold * 0.5f), 500);
                }
                
                if (player.isSetBonusActive() && player.getEquippedRod() != null && player.getEquippedRod().setName.equals("Hoàng Gia")) {
                    goldBonus += (int)(baseGold * 0.3f);
                }
                
                int xp = (int)(currentCatch.getXpValue() * xpMod);
                int gold = baseGold + goldBonus;
                
                int multiplier = 1;
                if (player.isFishingNetActive()) {
                    player.setFishingNetActive(false);
                    if (MathUtils.randomBoolean(0.5f)) { // 50% chance
                        multiplier = MathUtils.random(2, 4);
                    }
                }
                
                // HLW-05: +20% Vang & +20% EXP trong Bau Troi Huyet Nguyet
                if (isBloodMoonActive()) {
                    gold = (int)(gold * 1.20f);
                    xp = (int)(xp * 1.20f);
                }
                int lvUp = player.gainXp(xp);

                if (currentCatch.getType() == CatchableType.CONSUMABLE) {
                    player.addItem(currentCatch.getName(), multiplier);
                } else {
                    if (currentCatch.getType() == CatchableType.TRASH || currentCatch.getType() == CatchableType.TREASURE) {
                        if (player.getQuestManager() != null) player.getQuestManager().addProgress("trash", 1);
                    }
                    // Lưới Đánh Cá: Nhân lượng cá bắt được, đặt trần thưởng Vàng thêm tối đa 400 Vàng
                    int netExtraGold = (multiplier > 1) ? Math.min(gold * (multiplier - 1), 400) : 0;
                    player.gainGold(gold + netExtraGold);
                    
                    if (currentCatch instanceof com.fishingrpg.game.entities.Fish) {
                        com.fishingrpg.game.entities.Fish f = (com.fishingrpg.game.entities.Fish) currentCatch;
                        com.fishingrpg.game.entities.Fish.Rarity rarity = f.getRarity();
                        int diamonds = 0;
                        if (rarity == com.fishingrpg.game.entities.Fish.Rarity.RARE) {
                            // 20% tỷ lệ rơi 1 KC (trước đây 100% rơi)
                            if (MathUtils.randomBoolean(0.20f)) diamonds = 1;
                        } else if (rarity == com.fishingrpg.game.entities.Fish.Rarity.EPIC) {
                            // 100% tỷ lệ rơi 1 KC, 50% cơ hội rơi thêm 1 KC (1-2 KC)
                            diamonds = MathUtils.randomBoolean(0.50f) ? 2 : 1;
                        } else if (rarity == com.fishingrpg.game.entities.Fish.Rarity.BOSS) {
                            // Boss luôn rơi 3 - 5 KC (trước đây 5-10 KC)
                            diamonds = MathUtils.random(3, 5);
                        }
                        
                        if (diamonds > 0) {
                            int extraDiamonds = (multiplier > 1) ? Math.min(diamonds * (multiplier - 1), 2) : 0;
                            diamonds += extraDiamonds;
                            player.addDiamonds(diamonds);
                            combatMessage += " [+" + diamonds + " KC]";
                            lastCatchDiamonds = diamonds;
                        } else {
                            lastCatchDiamonds = 0;
                        }
                    }
                }
                
                String extra = "";
                if (multiplier > 1) {
                    extra += " [x" + multiplier + "]";
                }
                // --- Set Bonus Dạ Hội Bí Ngô: 35% rơi thêm 1 Kẹo Trick-or-Treat ---
                if (player.getEquipment() != null && player.getEquipment().isSetBonusActive()
                        && "Dạ Hội Bí Ngô".equals(player.getEquipment().getActiveSetName())) {
                    if (MathUtils.randomBoolean(0.35f)) {
                        player.addItem("Kẹo Trick-or-Treat", 1);
                        combatMessage += " [+1 Kẹo Trick-or-Treat]";
                        if (ftm != null) ftm.addText("+1 KẸO TRICK-OR-TREAT!", 640f, 420f, Color.ORANGE);
                    }
                }

                // HLW-05: Ty le roi Ruong & Keo trong Dem Huyet Nguyet
                if (currentCatch instanceof Fish) {
                    String fn = currentCatch.getName();
                    boolean isHlwFish = fn.equals("Cá Skeleton") || fn.equals("Cá Đèn Lồng Jack-o'") ||
                                        fn.equals("Cá Đầu Lâu Huyết Nguyệt") || fn.equals("Cá Dạng Ma") ||
                                        fn.equals("Thủy Quái Frankenstein") || fn.equals("Hải Xà Dạ Xoa") ||
                                        fn.equals("Kình Ngư Tử Thần Thanatos");
                    if (isHlwFish) {
                        AudioManager.getInstance().playSFX("sfx_halloween_catch");
                    }
                    if (isBloodMoonActive()) {
                        if (!"Kình Ngư Tử Thần Thanatos".equals(fn) && isHlwFish) {
                            if (MathUtils.randomBoolean(0.20f)) {
                                player.addItem("Kẹo Trick-or-Treat", 1);
                                combatMessage += " [+1 Kẹo]";
                            }
                            if (MathUtils.randomBoolean(0.08f)) {
                                player.addItem("Rương Ma Quái", 1);
                                combatMessage += " [+1 Rương Ma Quái]";
                            }
                        } else if (!isHlwFish) {
                            if (MathUtils.randomBoolean(0.10f)) {
                                player.addItem("Kẹo Trick-or-Treat", 1);
                                combatMessage += " [+1 Kẹo]";
                            }
                        }
                    }
                }

                // --- 100% roi Ruong Ma Quai khi bat duoc Sieu Boss Thanatos ---
                if (currentCatch instanceof Fish && "Kình Ngư Tử Thần Thanatos".equals(currentCatch.getName())) {
                    player.addItem("Rương Ma Quái", 1);
                    combatMessage += " [+1 Rương Ma Quái]";
                    if (ftm != null) ftm.addText("★ CHIẾN TÍCH: +1 RƯƠNG MA QUÁI! ★", 640f, 440f, Color.GOLD);
                }

                // --- Kỹ năng: Nhặt Mót ---
                if (player.isSkillEquipped("nhat_mot")) {
                    float chance = player.getSkillTree().getNhatMotChance();
                    if (MathUtils.random(100f) < chance) {
                        // Rơi ngẫu nhiên Rác hoặc Tiêu hao
                        Catchable bonus = LootTable.rollTrashOrConsumable();
                        if (bonus.getType() == CatchableType.CONSUMABLE) {
                            player.addItem(bonus.getName(), 1);
                        } else {
                            player.gainGold(bonus.getGoldValue());
                        }
                        extra += " [+" + bonus.getName() + "]";
                    }
                }

                if (currentCatch instanceof Fish) {
                    for (int i = 0; i < multiplier; i++) player.incrementFishCaught();
                    Fish f = (Fish) currentCatch;
                    boolean nr = SizeRecord.checkAndUpdate(f.getName(), f.getWeightKg(), f.getLengthCm());
                    extra += (f.isFirstCatch() ? " [MỚI]" : "") + (nr ? " [KỶ LỤC]" : "");
                    if (nr) {
                        com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFireworks(640f, 440f);
                        com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFireworks(420f, 520f);
                        com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFireworks(860f, 520f);
                        if (ftm != null) ftm.addText("★ PHÁ KỶ LỤC MỚI! ★", 640f, 380f, Color.GOLD);
                        AudioManager.getInstance().playSFX("sfx_success");
                    }
                    
                    player.getQuestManager().addProgress("catch", multiplier);
                    if (f.getRarity() == com.fishingrpg.game.entities.Fish.Rarity.RARE || f.getRarity() == com.fishingrpg.game.entities.Fish.Rarity.EPIC || f.getRarity() == com.fishingrpg.game.entities.Fish.Rarity.BOSS) {
                        player.getQuestManager().addProgress("rare", multiplier);
                    }
                    if (f.getRarity() == com.fishingrpg.game.entities.Fish.Rarity.BOSS) {
                        player.getQuestManager().addProgress("boss", multiplier);
                    }
                    if (f.getWeightKg() >= 10f) {
                        player.getQuestManager().addProgress("heavy", 1);
                    }
                    if (maxTensionRatioInFight <= 0.85f) {
                        player.getQuestManager().addProgress("clean", 1);
                    }
                }

                combatMessage = "CÂU ĐƯỢC: " + currentCatch.getName() + extra + (lvUp > 0 ? " [TĂNG CẤP]" : "");
                resultLine = "+" + xp + " XP   +" + (gold * multiplier) + " Vàng";
                lastCatch = currentCatch;
                lastCatchExtra = extra;
                lastCatchResultLine = resultLine;
                showCatchSidebar = false;
            }
            
            float benBiBonus = player.isSkillEquipped("ben_bi") ? player.getSkillTree().getBenBiBonus() : 0f;
            float recoveredPercent = 0.10f + (benBiBonus / 100f);
            player.consumeStamina(-(player.getMaxStamina() * recoveredPercent));
        }
    }

    public void update(float delta, boolean actClicked, boolean isSpaceJust, boolean isSpaceDown, boolean[] activeSkillInputs, boolean actClose) {
        this.isReeling = isSpaceDown;
        if (actClose) {
            showCatchSidebar = false;
        }
        if (castResultTimer > 0) castResultTimer -= delta;
        if (castFlightTimer > 0) castFlightTimer -= delta;
        if (castBuffBannerTimer > 0) castBuffBannerTimer -= delta;
        if (fishEscapeTimer > 0) fishEscapeTimer -= delta;
        if (reelUpTimer > 0) {
            reelUpTimer -= delta;
            if (reelUpTimer <= 0) {
                reelUpTimer = 0f;
                showCatchSidebar = true;
            }
        }
        
        float cdDelta = delta;
        
        if (keoManhCooldown > 0) keoManhCooldown -= cdDelta;
        if (giatKepCooldown > 0) giatKepCooldown -= cdDelta;
        if (duongSucCooldown > 0) duongSucCooldown -= cdDelta;
        if (phaGiapCooldown > 0) phaGiapCooldown -= cdDelta;
        if (luotDayCooldown > 0) luotDayCooldown -= cdDelta;
        if (troiBuocCooldown > 0) troiBuocCooldown -= cdDelta;
        
        if (phaGiapTimer > 0) phaGiapTimer -= delta;
        if (luotDayTimer > 0) luotDayTimer -= delta;
        if (troiBuocTimer > 0) troiBuocTimer -= delta;

        switch (state) {
            case IDLE:
                // Check Ném Xa in IDLE
                for (int i = 0; i < 4; i++) {
                    if (activeSkillInputs[i]) {
                        String skillId = player.getEquippedActives()[i];
                        if ("nem_xa".equals(skillId) && player.getSkillTree().hasNemXa()) {
                            isNextCastPerfect = true;
                            combatMessage = "NÉM XA: Cú quăng tiếp theo chắc chắn Perfect!";
                        }
                    }
                }
                if (actClicked) state = State.PRE_CAST;
                break;
            case PRE_CAST:
                if (actClicked) startCasting();
                break;
            case CASTING:
                castCursorX += castDirection * castSpeed * delta;
                if (castCursorX > 200f) {
                    castCursorX = 200f;
                    castDirection = -1;
                } else if (castCursorX < 0f) {
                    castCursorX = 0f;
                    castDirection = 1;
                }
                
                if (actClicked || isSpaceJust) {
                    float centerDist = Math.abs(castCursorX - 100f);
                    // Cursor is 8px wide visually (4px radius). 4px / 400px * 200 units = 2 units radius.
                    float cursorRadius = 2f; 
                    float perfectSize = 10f;
                    if (player.isSkillEquipped("mat_dai_bang")) {
                        perfectSize *= player.getSkillTree().getMatDaiBangBonus();
                    }
                    if (isNextCastPerfect || centerDist <= (perfectSize + cursorRadius)) {
                        isNextCastPerfect = false;
                        startWaiting(2f); // Perfect
                    } else if (centerDist <= (40f + cursorRadius)) {
                        startWaiting(0.5f); // Good
                    } else {
                        startWaiting(0f); // Normal/Miss
                    }
                }
                break;
            case WAITING:
                stateTimer -= delta;
                if (stateTimer <= 0) triggerBite();
                break;
            case BITE:
                stateTimer -= delta;
                if (isSpaceJust || actClicked) {
                    if (player.isSkillEquipped("phan_xa")) {
                        float window = player.getSkillTree().getPhanXaTime();
                        if (biteWindow - stateTimer <= window) {
                            phanXaActive = true;
                        }
                    }
                    startEncounter();
                } else if (stateTimer <= 0) {
                    finishEncounter(false, "CHẬM TAY!");
                    AudioManager.getInstance().playSFX("sfx_button");
                }
                break;
            case COMBAT:
                com.fishingrpg.game.systems.gas.AbilityContext gasCtx = new com.fishingrpg.game.systems.gas.AbilityContext(player, currentFish, this, ftm, screenShake);
                abilitySystem.update(delta, gasCtx);
                keoManhCooldown = abilitySystem.getCooldown("keo_manh");
                giatKepCooldown = abilitySystem.getCooldown("giat_kep");
                duongSucCooldown = abilitySystem.getCooldown("duong_suc");
                phaGiapCooldown = abilitySystem.getCooldown("pha_giap");
                luotDayCooldown = abilitySystem.getCooldown("luot_day");
                troiBuocCooldown = abilitySystem.getCooldown("loi_nguyen_troi_buoc");

                if (duongSucChannelTimer > 0) {
                    duongSucChannelTimer -= delta;
                    combatMessage = "ĐANG VẬN SỨC... (" + String.format(java.util.Locale.US, "%.1f", duongSucChannelTimer) + "s)";
                    if (duongSucChannelTimer <= 0) {
                        combatMessage = "DƯỠNG SỨC HOÀN TẤT!";
                        if (ftm != null) ftm.addText("Dưỡng Sức hoàn tất!", 640f, 400f, Color.GREEN);
                        AudioManager.getInstance().playSFX("sfx_success");
                    }
                    isSpaceDown = false; // Block pulling while channeling
                }
                updateCombat(delta, isSpaceJust, isSpaceDown);
                handleSkills(activeSkillInputs, isSpaceDown, delta);
                break;
            case RESULT:
                if (actClicked) {
                    state = State.PRE_CAST;
                    startCasting();
                } else if (actClose) {
                    state = State.PRE_CAST;
                }
                break;
        }
    }

    private void updateCombat(float delta, boolean isSpaceJust, boolean isSpaceDown) {
        if (perfectCastBuffTimer > 0f) {
            perfectCastBuffTimer = Math.max(0f, perfectCastBuffTimer - delta);
        }
        if (currentFish == null || currentFish.isCaught()) {
            finishEncounter(true);
            return;
        }
        // --- Fish Active Skills ---
        if (currentFish.getSkills() != null) {
            float hpPct = currentFish.getCurrentHp() / currentFish.getMaxHp();
            for (com.fishingrpg.game.entities.FishSkill s : currentFish.getSkills()) {
                if (s.isActive && !s.triggered && hpPct <= s.triggerHpPercent) {
                    s.triggered = true;
                    bossSkillJustUsed = true;
                    
                    // Thần Khí Lạc Hồng Set Bonus: Vô hiệu hóa kỹ năng chủ động
                    if (player.isSetBonusActive() && player.getEquippedRod() != null 
                            && player.getEquippedRod().setName.equals("Thần Khí Lạc Hồng")) {
                        AudioManager.getInstance().playSFX("sfx_skill");
                        combatMessage = "Ý CHÍ BẤT DIỆT! Vô hiệu hóa " + com.fishingrpg.game.entities.Fish.getSkillDisplayName(s.id).toUpperCase() + "!";
                        player.addStaminaRegen(player.getMaxStamina() * 0.25f, player.getMaxStamina());
                        if (ftm != null) ftm.addText("MIỄN NHIỄM!", 640f, 450f, Color.CYAN);
                        continue;
                    }

                    AudioManager.getInstance().playSFX("sfx_skill");
                    if (screenShake != null) screenShake.shakeHeavy();
                    com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX(s.id, 640f, 360f);
                    if (s.id.equals("but_toc")) {
                        fishButTocTimer = 3f;
                        combatMessage = "CÁ BỨT TỐC! Lực kéo x2!";
                    } else if (s.id.equals("quay_bun")) {
                        fishQuayBunTimer = 4.5f;
                        if ("Thủy Quái Sông Trà".equals(currentFish.getName())) {
                            combatMessage = "THỦY QUÁI SÔNG TRÀ QUẪY BÙN! Phù sa mù mịt che thanh Tension!";
                        } else {
                            combatMessage = "CÁ QUẪY BÙN! Không thấy thanh Tension!";
                        }
                    } else if (s.id.equals("lan_sau")) {
                        fishLanSauTimer = 2.5f;
                        player.increaseTension(player.getMaxLineTension() * 0.3f);
                        if ("Thủy Quái Sông Trà".equals(currentFish.getName())) {
                            combatMessage = "THỦY QUÁI LẶN HANG ĐÁ! Dây cước bị kéo căng dữ dội!";
                        } else {
                            combatMessage = "CÁ LẶN SÂU! Tension tăng sốc!";
                        }
                    } else if (s.id.equals("song_than")) {
                        fishSongThanTimer = 3f;
                        combatMessage = "SÓNG THẦN! Thể lực sụt giảm liên tục!";
                    } else if (s.id.equals("cuong_no")) {
                        fishCuongNoTimer = 3.5f;
                        if ("Thủy Quái Sông Trà".equals(currentFish.getName())) {
                            combatMessage = "THỦY QUÁI BÁ CHỦ CUỒNG NỘ! Giải trừ khống chế & Bất tử!";
                        } else {
                            combatMessage = "CUỒNG NỘ! Bất tử và giải debuff!";
                        }
                        luotDayTimer = 0f;
                        troiBuocTimer = 0f;
                        phaGiapTimer = 0f;
                    } else if (s.id.equals("phun_muc")) {
                        fishPhunMucTimer = 4f;
                        combatMessage = "CÁ PHUN MỰC! Tầm nhìn bị che khuất!";
                    } else if (s.id.equals("giat_dien")) {
                        fishGiatDienTimer = 2f;
                        combatMessage = "CÁ GIẬT ĐIỆN! Tránh giữ dây!";
                    } else if (s.id.equals("nhay_vot")) {
                        fishNhayVotTimer = 1.8f;
                        combatMessage = "CÁ NHẢY VỌT! Thả dây kẻo tiếp nước đứt cước!";
                        player.decreaseTension(player.getCurrentLineTension() * 0.5f);
                    } else if (s.id.equals("song_am")) {
                        fishSongAmTimer = 2f;
                        combatMessage = "SÓNG ÂM! Tension nhiễu loạn!";
                    } else if (s.id.equals("phun_muc_mu")) {
                        fishPhunMucMuTimer = 4f;
                        combatMessage = "PHUN MỰC MÙ! Tối tăm mù mịt!";
                    } else if (s.id.equals("quet_xuc_tu")) {
                        player.increaseTension(player.getMaxLineTension() * 0.35f);
                        player.consumeStamina(25f);
                        combatMessage = "BẠCH TUỘC QUÉT XÚC TU! Sóng chấn động dữ dội!";
                    } else if (s.id.equals("hut_mau")) {
                        fishHutMauTimer = 3.5f;
                        combatMessage = "MỎ QUẠ CẮN XÉ! Hút thể lực để hồi sinh mệnh!";
                    } else if (s.id.equals("muc_mu_hut_mau")) {
                        fishPhunMucMuTimer = 4.5f;
                        fishHutMauTimer = 4.5f;
                        combatMessage = "MỰC MÙ HÚT MÁU! Màn đêm che khuất & Mỏ quạ hút thể lực hồi sinh mệnh!";
                        com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("muc_mu_hut_mau", 640f, 360f);
                    } else if (s.id.equals("bao_to")) {
                        fishBaoToTimer = 3f;
                        player.increaseTension(player.getMaxLineTension() * 0.4f);
                        combatMessage = "BÃO TỐ! Sấm sét giáng xuống!";
                    } else if (s.id.equals("xoay_nuoc")) {
                        fishXoayNuocTimer = 3.5f;
                        combatMessage = "CÁ NGỪ TẠO XOÁY NƯỚC! Giảm tốc độ xả dây & Tension dao động!";
                        AudioManager.getInstance().playSFX("sfx_skill");
                        com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("lan_sau", 640f, 360f);
                    } else if (s.id.equals("vu_dieu_co_buom")) {
                        fishNhayVotTimer = 0.8f;
                        totalJumpDuration = 0.8f;
                        jumpHoldSpaceDuration = 0f;
                        jumpFailedEarly = false;
                        jumpSpacePressCount = 0;
                        jumpLandingCueShown = false;
                        jumpRehookHandled = false;
                        fishCoBuomSecondJump = true;
                        fishCoBuomActive = true;
                        combatMessage = "VŨ ĐIỆU CỜ BUỒM! Phóng lướt 2 nhịp trên sóng!";
                        AudioManager.getInstance().playSFX("sfx_skill");
                        com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("nhay_vot", 640f, 360f);
                        if (ftm != null) ftm.addText("VŨ ĐIỆU CỜ BUỒM!", 640f, 450f, Color.GOLD);
                    } else if (s.id.equals("vuc_da_nuot_chung")) {
                        fishVucDaTimer = 3.5f;
                        vucDaDebuffActive = true;
                        player.setStaminaHealMultiplier(0.70f); // Giam 30% hoi the luc tu moi nguon
                        player.increaseTension(player.getMaxLineTension() * 0.20f); // Nerf con +20% Tension
                        combatMessage = "VỰC ĐÁ NUỐT CHỬNG! Giật +20% Tension & Giảm 30% Hồi Thể Lực!";
                        AudioManager.getInstance().playSFX("sfx_skill");
                        if (screenShake != null) screenShake.shakeMedium();
                        if (ftm != null) ftm.addText("VỰC ĐÁ NUỐT CHỬNG!", 640f, 450f, new Color(0.85f, 0.4f, 1f, 1f));
                    } else if (s.id.equals("can_xe_dien_cuong")) {
                        fishCanXeTimer = 5.0f;
                        boolean thinhMuiMauActive = (currentFish.getPassiveSkill("thinh_mui_mau") != null 
                                && (player.getCurrentStamina() / Math.max(1f, player.getMaxStamina())) < 0.50f);
                        if (thinhMuiMauActive) {
                            fishCanXe30Percent = true;
                            player.consumeStamina(30f);
                            combatMessage = "CẮN XÉ CUỒNG HUYẾT! -30% Max Tension & -30 Thể lực!";
                            if (ftm != null) ftm.addText("CẮN XÉ CUỒNG HUYẾT!", 640f, 440f, Color.RED);
                        } else {
                            fishCanXe30Percent = false;
                            combatMessage = "CÁ MẬP CẮN XÉ ĐIÊN CUỒNG! -20% Max Tension an toàn!";
                            if (ftm != null) ftm.addText("CẮN XÉ ĐIÊN CUỒNG!", 640f, 440f, Color.RED);
                        }
                        AudioManager.getInstance().playSFX("sfx_skill");
                        if (screenShake != null) screenShake.shakeHeavy();
                    } else if (s.id.equals("cuong_phong_ngu_kiem")) {
                        marlinEnrageActive = true;
                        marlinSurgeTimer = 0f;
                        marlinPullStacks = Math.max(1, marlinPullStacks);
                        marlinStackTimer = 0f;
                        combatMessage = "CÁ CỜ CUỒNG PHONG NGỰ KIẾM! Chạy đua sinh tử!";
                        AudioManager.getInstance().playSFX("sfx_skill");
                        if (screenShake != null) screenShake.shakeHeavy();
                        if (ftm != null) ftm.addText("CUỒNG PHONG NGỰ KIẾM!", 640f, 450f, Color.RED);
                    }
                    if (ftm != null) ftm.addText(com.fishingrpg.game.entities.Fish.getSkillDisplayName(s.id).toUpperCase() + "!", 640f, 450f, Color.YELLOW);
                }
            }
        }
        
        // --- Halloween Periodic Active Skills with GCD 5.5s ---
        if (currentFish != null && fishSkillGCDTimer <= 0f && fishNhayVotTimer <= 0f) {
            float hpPct = currentFish.getCurrentHp() / currentFish.getMaxHp();
            float thanatosCdMulti = thanatosPhase3Active ? 0.70f : 1.0f;

            // Thanatos Phase 3 Kich Hoat (< 30% HP)
            if (currentFish.getSkill("lien_hoan_tram_doat_menh") != null && hpPct <= 0.30f && !thanatosPhase3Active) {
                thanatosPhase3Active = true;
                combatMessage = "TỬ THẦN BỘC PHÁT! Thanatos giảm 30% hồi chiêu tất cả kỹ năng!";
                if (ftm != null) ftm.addText("TỬ THẦN BỘC PHÁT! (-30% HỒI CHIÊU)", 640f, 450f, Color.RED);
                AudioManager.getInstance().playSFX("sfx_skill");
                if (screenShake != null) screenShake.shakeHeavy();
            }

            // Thanatos Tuyet Ky: Lien Hoan Tram Doat Menh (Uu tien so 1)
            if (currentFish.getSkill("lien_hoan_tram_doat_menh") != null && hpPct <= 0.30f && !thanatosUltTriggered) {
                thanatosUltTriggered = true;
                fishSkillGCDTimer = 6.0f;
                thanatosReHookActive = true;
                thanatosReHookStep = 1;
                thanatosReHookSuccessCount = 0;
                fishNhayVotTimer = 0.5f;
                totalJumpDuration = 0.5f;
                jumpHoldSpaceDuration = 0f;
                jumpFailedEarly = false;
                jumpSpacePressCount = 0;
                jumpLandingCueShown = false;
                jumpRehookHandled = false;
                combatMessage = "LIÊN HOÀN TRẢM ĐOẠT MỆNH! Nhịp 1: Trảm Ngang Biển (0.5s)!";
                if (ftm != null) ftm.addText("LIÊN HOÀN TRẢM: NHỊP 1 (0.5s)!", 640f, 450f, Color.RED);
                AudioManager.getInstance().playSFX("sfx_skill");
                if (screenShake != null) screenShake.shakeHeavy();
            }
            // Thanatos Am Hon Quay Pha (Phase 2, <= 70% HP, 18s)
            else if (currentFish.getSkill("am_hon_quay_pha") != null && hpPct <= 0.70f && thanatosAmHonCooldown <= 0f) {
                thanatosAmHonCooldown = 18.0f * thanatosCdMulti;
                fishSkillGCDTimer = 5.5f;
                currentFish.setShield(500f);
                combatMessage = "ÂM HỒN QUẤY PHÁ! Thanatos tạo Soul Shield 500 HP!";
                if (ftm != null) ftm.addText("SOUL SHIELD: 500 GIÁP!", 640f, 450f, Color.CYAN);
                AudioManager.getInstance().playSFX("sfx_skill");
            }
            // Thanatos Vuc Xoay Am Ty (Phase 2, <= 70% HP, 14s)
            else if (currentFish.getSkill("vuc_xoay_am_ty") != null && hpPct <= 0.70f && thanatosVucXoayCooldown <= 0f) {
                thanatosVucXoayCooldown = 14.0f * thanatosCdMulti;
                fishSkillGCDTimer = 5.5f;
                thanatosVucXoayTimer = 3.0f;
                currentFish.heal(currentFish.getMaxHp() * 0.03f);
                combatMessage = "VỰC XOÁY ÂM TY! +15 Tension/s (3s) & Thanatos hồi 3% HP!";
                if (ftm != null) ftm.addText("VỰC XOÁY ÂM TY!", 640f, 450f, Color.DARK_GRAY);
                AudioManager.getInstance().playSFX("sfx_skill");
            }
            // Thanatos Tieng Hu Dia Nguc (Phase 1, 12s)
            else if (currentFish.getSkill("tieng_hu_dia_nguc") != null && thanatosTienghuCooldown <= 0f) {
                thanatosTienghuCooldown = 12.0f * thanatosCdMulti;
                fishSkillGCDTimer = 5.5f;
                thanatosSilenceNextSkill = true;
                combatMessage = "TIẾNG HÚ ĐỊA NGỤC! Vô hiệu hóa chiêu tiếp theo của bạn!";
                if (ftm != null) ftm.addText("TIẾNG HÚ ĐỊA NGỤC! (CÂM LẶNG)", 640f, 450f, Color.SCARLET);
                AudioManager.getInstance().playSFX("sfx_skill");
                if (screenShake != null) screenShake.shakeMedium();
            }
            // Hai Xa Doc To Am Ty (16s)
            else if (currentFish.getSkill("doc_to_am_ty") != null && haiXaDocCooldown <= 0f) {
                haiXaDocCooldown = 16.0f;
                fishSkillGCDTimer = 5.5f;
                haiXaDocTimer = 5.0f;
                combatMessage = "ĐỘC TỐ ÂM TY! Rút 5 Thể lực/s trong 5s!";
                if (ftm != null) ftm.addText("ĐỘC TỐ ÂM TY!", 640f, 450f, new Color(0.6f, 0.2f, 0.9f, 1f));
                AudioManager.getInstance().playSFX("sfx_skill");
            }
            // Hai Xa Mang Xa Siet Cuoc (12s)
            else if (currentFish.getSkill("mang_xa_siet_cuoc") != null && haiXaSietCooldown <= 0f) {
                haiXaSietCooldown = 12.0f;
                fishSkillGCDTimer = 5.5f;
                haiXaSietCuocTimer = 3.0f;
                combatMessage = "MÃNG XÀ SIẾT CƯỚC! Khóa cứng Tension trong 3s!";
                if (ftm != null) ftm.addText("MÃNG XÀ SIẾT CƯỚC (3s)!", 640f, 450f, Color.PURPLE);
                AudioManager.getInstance().playSFX("sfx_skill");
            }
            // Phantom Tieng Than U Am (10s)
            else if (currentFish.getSkill("tieng_than_u_am") != null && phantomSkillCooldown <= 0f) {
                phantomSkillCooldown = 10.0f;
                fishSkillGCDTimer = 5.5f;
                player.consumeStamina(15f);
                currentFish.heal(currentFish.getMaxHp() * 0.05f);
                playerSealStaminaRegenTimer = 5.0f;
                combatMessage = "TIẾNG THAN U ÁM! Hút 15 TL hồi 5% HP & Giảm hồi phục (5s)!";
                if (ftm != null) ftm.addText("TIẾNG THAN U ÁM!", 640f, 450f, Color.PURPLE);
                AudioManager.getInstance().playSFX("sfx_skill");
            }
            // Dau Lau Xung Kich Doat Hon (9s)
            else if (currentFish.getSkill("xung_kich_doat_hon") != null && dauLauCooldown <= 0f) {
                dauLauCooldown = 9.0f;
                fishSkillGCDTimer = 5.5f;
                dauLauReHookActive = true;
                fishNhayVotTimer = 0.6f;
                totalJumpDuration = 0.6f;
                jumpHoldSpaceDuration = 0f;
                jumpFailedEarly = false;
                jumpSpacePressCount = 0;
                jumpLandingCueShown = false;
                jumpRehookHandled = false;
                combatMessage = "CÁ ĐẦU LÂU XUNG KÍCH ĐOẠT HỒN! Re-Hook 0.6s!";
                if (ftm != null) ftm.addText("XUNG KÍCH ĐOẠT HỒN!", 640f, 450f, Color.SCARLET);
                AudioManager.getInstance().playSFX("sfx_skill");
            }
            // Jack-o Hoa Dang Jack-o (8s)
            else if (currentFish.getSkill("hoa_dang_jack_o") != null && jackOCooldown <= 0f) {
                jackOCooldown = 8.0f;
                fishSkillGCDTimer = 5.5f;
                player.increaseTension(player.getMaxLineTension() * 0.15f);
                player.consumeStamina(10f);
                combatMessage = "HỎA ĐĂNG JACK-O'! Sốc +15% Tension & -10 Thể lực!";
                if (ftm != null) ftm.addText("HỎA ĐĂNG JACK-O'!", 640f, 450f, Color.ORANGE);
                AudioManager.getInstance().playSFX("sfx_skill");
                if (screenShake != null) screenShake.shakeMedium();
            }
        }

        // Ticking cooldowns
        if (jackOCooldown > 0) jackOCooldown -= delta;
        if (dauLauCooldown > 0) dauLauCooldown -= delta;
        if (phantomSkillCooldown > 0) phantomSkillCooldown -= delta;
        if (haiXaSietCooldown > 0) haiXaSietCooldown -= delta;
        if (haiXaDocCooldown > 0) haiXaDocCooldown -= delta;
        if (thanatosTienghuCooldown > 0) thanatosTienghuCooldown -= delta;
        if (thanatosVucXoayCooldown > 0) thanatosVucXoayCooldown -= delta;
        if (thanatosAmHonCooldown > 0) thanatosAmHonCooldown -= delta;

        // Boss Cá Cờ Active 1: Vũ điệu lướt sóng định kỳ 8.0s
        if (currentFish.getSkill("vu_dieu_luot_song") != null) {
            marlinJumpCooldown -= delta;
            if (marlinJumpCooldown <= 0f && fishNhayVotTimer <= 0f) {
                marlinJumpCooldown = 8.0f;
                fishNhayVotTimer = 0.9f;
                totalJumpDuration = 0.9f;
                jumpHoldSpaceDuration = 0f;
                jumpFailedEarly = false;
                jumpSpacePressCount = 0;
                jumpLandingCueShown = false;
                jumpRehookHandled = false;
                fishCoBuomActive = false;
                
                // Cuong phong ngu kiem duoi 30% HP: moi lan kich hoat vu dieu luot song la +10% luc keo
                if (marlinEnrageActive) {
                    marlinPullStacks++;
                    combatMessage = "CÁ CỜ PHI THÂN! Lực kéo +" + (marlinPullStacks * 10) + "%!";
                    if (ftm != null) ftm.addText("CUỒNG PHONG LƯỚT SÓNG!", 640f, 430f, Color.RED);
                } else {
                    combatMessage = "CÁ CỜ PHI THÂN LƯỚT SÓNG! Nhả dây canh Re-Hook!";
                    if (ftm != null) ftm.addText("VŨ ĐIỆU LƯỚT SÓNG!", 640f, 450f, Color.YELLOW);
                }
                player.decreaseTension(player.getCurrentLineTension() * 0.5f);
                AudioManager.getInstance().playSFX("sfx_skill");
                com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("nhay_vot", 640f, 360f);
            }
        }

        if (fishButTocTimer > 0) fishButTocTimer -= delta;
        if (fishLanSauTimer > 0) fishLanSauTimer -= delta;
        if (fishQuayBunTimer > 0) fishQuayBunTimer -= delta;
        if (fishSongThanTimer > 0) {
            fishSongThanTimer -= delta;
            player.consumeStamina(20f * delta);
        }
        if (fishCuongNoTimer > 0) fishCuongNoTimer -= delta;
        if (fishPhunMucTimer > 0) fishPhunMucTimer -= delta;
        if (fishGiatDienTimer > 0) {
            fishGiatDienTimer -= delta;
            if (isSpaceDown) player.consumeStamina(15f * delta);
        }
        if (fishNhayVotTimer > 0) {
            fishNhayVotTimer -= delta;

            // Quy tac 1: Neu giu nut space > 50% thoi gian bay cua ca -> Nhan penalty ngay
            if (isSpaceDown) {
                jumpHoldSpaceDuration += delta;
                if (!jumpFailedEarly && totalJumpDuration > 0 && jumpHoldSpaceDuration > totalJumpDuration * 0.50f) {
                    jumpFailedEarly = true;
                    player.increaseTension(player.getMaxLineTension() * 0.20f);
                    combatMessage = "GHÌM DÂY QUÁ 50% KHI CÁ BAY! Phạt +20% Tension!";
                    if (ftm != null) ftm.addText("GHÌM DÂY QUÁ LÂU!", 640f, 450f, Color.RED);
                    AudioManager.getInstance().playSFX("sfx_fail");
                    if (screenShake != null) screenShake.shakeMedium();
                }
            }

            // Dau hieu sap cham nuoc (Landing cue) - Ho tro Den Long Bi Ngo +30% cua so phan ung
            float landingWindow = 0.35f;
            if (player.getPumpkinLanternCharges() > 0) {
                landingWindow = 0.35f * 1.30f;
            }
            if (fishNhayVotTimer <= landingWindow && !jumpLandingCueShown) {
                jumpLandingCueShown = true;
                if (!jumpFailedEarly) {
                    String cueMsg = (player.getPumpkinLanternCharges() > 0)
                            ? "▼ TIẾP NƯỚC (+30% PHẢN ỨNG)! NHẤP [SPACE] ▼"
                            : "▼ TIẾP NƯỚC! NHẤP [SPACE] ▼";
                    if (ftm != null) ftm.addText(cueMsg, 640f, 440f, Color.CYAN);
                    combatMessage = "CÁ SẮP TIẾP NƯỚC! Nhấp đúng 1 lần SPACE để Re-Hook!";
                }
            }

            // Quy tac 2: Ngan spam de lay perfect rehook (chi nhan 1 lan bam duy nhat trong cua so tiep nuoc)
            if (isSpaceJust) {
                jumpSpacePressCount++;
                if (fishNhayVotTimer > landingWindow) {
                    // Nhan qua som truoc khi toi cua so tiep nuoc
                    if (!jumpFailedEarly) {
                        jumpFailedEarly = true;
                        combatMessage = "NHẤN QUÁ SỚM! Hỏng Perfect Re-Hook!";
                        if (ftm != null) ftm.addText("BẤM QUÁ SỚM!", 640f, 450f, Color.RED);
                        AudioManager.getInstance().playSFX("sfx_fail");
                    }
                } else if (jumpSpacePressCount > 1) {
                    // Spam nhieu lan
                    if (!jumpFailedEarly) {
                        jumpFailedEarly = true;
                        combatMessage = "SPAM PHÍM SPACE! Mất quyền Perfect Re-Hook!";
                        if (ftm != null) ftm.addText("SPAM PHÍM!", 640f, 450f, Color.RED);
                        AudioManager.getInstance().playSFX("sfx_fail");
                    }
                } else if (!jumpFailedEarly && !jumpRehookHandled) {
                    // Chuan xac 1 lan bam duy nhat trong cua so tiep nuoc!
                    jumpRehookHandled = true;
                    combatMessage = "PERFECT RE-HOOK! Khống chế cú nhảy xuất sắc!";
                    if (ftm != null) ftm.addText("PERFECT RE-HOOK!", 640f, 460f, new Color(1f, 0.85f, 0.15f, 1f));
                    AudioManager.getInstance().playSFX("sfx_success");
                    if (screenShake != null) screenShake.shakeLight();
                    currentFish.takeDamage(player.getBasePullDamage() * 2.0f);
                    com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("nhay_vot", 640f, 360f);
                }
            }

            // Khi ca cham mat nuoc
            if (fishNhayVotTimer <= 0) {
                if (player.getPumpkinLanternCharges() > 0) {
                    player.consumePumpkinLanternCharge();
                }
                boolean isBoss = "Cá Cờ Xanh Khổng Lồ".equals(currentFish.getName());

                if (dauLauReHookActive) {
                    dauLauReHookActive = false;
                    if (jumpRehookHandled) {
                        combatMessage = "PERFECT RE-HOOK! Khống chế Xung Kích Đoạt Hồn thành công!";
                        if (ftm != null) ftm.addText("PERFECT RE-HOOK!", 640f, 460f, Color.GOLD);
                        AudioManager.getInstance().playSFX("sfx_success");
                    } else {
                        player.increaseTension(25f);
                        float lostHp = currentFish.getMaxHp() - currentFish.getCurrentHp();
                        if (lostHp > 0f) currentFish.heal(lostHp * 0.15f);
                        combatMessage = "TRƯỢT RE-HOOK! Phạt +25 Tension & Cá hồi 15% máu đã mất!";
                        if (ftm != null) ftm.addText("TRƯỢT RE-HOOK! (+25 TEN & HỒI MÁU)", 640f, 460f, Color.RED);
                        AudioManager.getInstance().playSFX("sfx_fail");
                        if (screenShake != null) screenShake.shakeMedium();
                    }
                    com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("nhay_vot", 640f, 360f);
                } else if (thanatosReHookActive) {
                    if (jumpRehookHandled) {
                        thanatosReHookSuccessCount++;
                    }
                    if (thanatosReHookStep == 1) {
                        // Nhip 1 xong -> Chuyen sang nhip 2
                        thanatosReHookStep = 2;
                        fishNhayVotTimer = 0.6f;
                        totalJumpDuration = 0.6f;
                        jumpHoldSpaceDuration = 0f;
                        jumpFailedEarly = false;
                        jumpSpacePressCount = 0;
                        jumpLandingCueShown = false;
                        jumpRehookHandled = false;
                        combatMessage = "LIÊN HOÀN TRẢM! Nhịp 2: Trảm Bổ Tử Thần (0.6s)!";
                        if (ftm != null) ftm.addText("LIÊN HOÀN TRẢM: NHỊP 2 (0.6s)!", 640f, 450f, Color.RED);
                        AudioManager.getInstance().playSFX("sfx_skill");
                        if (screenShake != null) screenShake.shakeHeavy();
                    } else {
                        // Nhip 2 xong -> Ket luan
                        thanatosReHookActive = false;
                        thanatosReHookStep = 0;
                        if (thanatosReHookSuccessCount == 2) {
                            thanatosVulnerableTimer = 3.5f;
                            combatMessage = "PHẢN ĐÒN TỐI THƯỢNG (2/2)! Thanatos suy yếu x2 Sát Thương (3.5s)!";
                            if (ftm != null) ftm.addText("PHẢN ĐÒN (2/2)! x2 SÁT THƯƠNG (3.5s)", 640f, 460f, Color.GOLD);
                            AudioManager.getInstance().playSFX("sfx_success");
                            if (screenShake != null) screenShake.shakeMedium();
                            currentFish.takeDamage(player.getBasePullDamage() * 3.0f);
                        } else if (thanatosReHookSuccessCount == 1) {
                            playerWeaknessTimer = 4.0f;
                            player.consumeStamina(15f);
                            combatMessage = "TRƯỢT 1 NHỊP (1/2)! Tê liệt cánh tay: -40% lực kéo (4s) & -15 Thể lực!";
                            if (ftm != null) ftm.addText("TÊ LIỆT CÁNH TAY (-40% LỰC KÉO, -15 TL)!", 640f, 460f, Color.ORANGE);
                            AudioManager.getInstance().playSFX("sfx_fail");
                            if (screenShake != null) screenShake.shakeMedium();
                        } else {
                            // Truot 2 nhip (0/2)
                            float effectiveRatio = player.getCurrentLineTension() / getEffectiveMaxTension();
                            if (effectiveRatio >= 0.90f) {
                                finishEncounter(false, "LƯỠI HÁI TỬ THẦN CHÉM ĐỨT PHĂNG DÂY CÂU!");
                                isLineBroken = true;
                                if (ftm != null) ftm.addText("LƯỠI HÁI CHÉM ĐỨT DÂY (TENSION > 90%)!", 640f, 460f, Color.RED);
                                AudioManager.getInstance().playSFX("sfx_fail");
                                if (screenShake != null) screenShake.shakeHeavy();
                                return;
                            }
                            player.increaseTension(30f);
                            playerSkillLockedTimer = 5.0f;
                            playerSealStaminaRegenTimer = 5.0f;
                            currentFish.heal(currentFish.getMaxHp() * 0.05f);
                            combatMessage = "TRƯỢT CẢ 2 NHỊP (0/2)! +30 Tension, Khóa Skill & Phong Ấn Hồi TL (5s)!";
                            if (ftm != null) ftm.addText("PHONG ẤN LINH HỒN (0/2)! +30 TEN, KHÓA SKILL 5s", 640f, 460f, Color.RED);
                            AudioManager.getInstance().playSFX("sfx_fail");
                            if (screenShake != null) screenShake.shakeHeavy();
                        }
                    }
                    com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("nhay_vot", 640f, 360f);
                } else if (fishCoBuomActive) {
                    // Vu dieu co buom: khong Perfect Re-hook phat +30% Tension moi nhip
                    if (jumpRehookHandled) {
                        combatMessage = "PERFECT RE-HOOK! Triệt tiêu hoàn toàn lực sốc!";
                    } else {
                        player.increaseTension(30f); // Phạt +30 đơn vị Tension tuyệt đối
                        combatMessage = "TRƯỢT RE-HOOK CỜ BUỒM! Phạt +30 Tension!";
                        if (ftm != null) ftm.addText("TRƯỢT RE-HOOK!", 640f, 460f, Color.RED);
                        AudioManager.getInstance().playSFX("sfx_fail");
                        if (screenShake != null) screenShake.shakeHeavy();
                    }
                    com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("nhay_vot", 640f, 360f);

                    if (fishCoBuomSecondJump) {
                        fishCoBuomSecondJump = false;
                        fishNhayVotTimer = 0.8f;
                        totalJumpDuration = 0.8f;
                        jumpHoldSpaceDuration = 0f;
                        jumpFailedEarly = false;
                        jumpSpacePressCount = 0;
                        jumpLandingCueShown = false;
                        jumpRehookHandled = false;
                        combatMessage = "CÁ CỜ BUỒM PHÓNG LƯỚT NHỊP 2!";
                        AudioManager.getInstance().playSFX("sfx_skill");
                        com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("nhay_vot", 640f, 360f);
                    } else {
                        fishCoBuomActive = false;
                    }
                } else if (isBoss) {
                    // Boss Ca Co Xanh: tiep nuoc soc +20% Tension, -15 The luc & hoi 8% mau boss tru khi Perfect Re-hook
                    if (jumpRehookHandled) {
                        combatMessage = "PERFECT RE-HOOK! Khống chế Vũ Điệu Lướt Sóng thành công!";
                    } else {
                        player.increaseTension(player.getMaxLineTension() * 0.20f);
                        player.consumeStamina(15f);
                        float bossHeal = currentFish.getMaxHp() * 0.08f;
                        currentFish.takeDamage(-bossHeal);
                        combatMessage = "SỐC TIẾP NƯỚC! +20% Tension, -15 TL & Boss hồi 8% máu!";
                        if (ftm != null) ftm.addText("SỐC TIẾP NƯỚC!", 640f, 460f, Color.RED);
                        AudioManager.getInstance().playSFX("sfx_fail");
                        if (screenShake != null) screenShake.shakeHeavy();
                    }
                    com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("nhay_vot", 640f, 360f);
                } else {
                    // Ca nhay thong thuong
                    if (jumpRehookHandled) {
                        combatMessage = "PERFECT RE-HOOK! Triệt tiêu hoàn toàn lực sốc!";
                    } else if (isSpaceDown) {
                        player.increaseTension(player.getMaxLineTension() * 0.25f);
                        combatMessage = "SỐC LỰC TIẾP NƯỚC! Dây bị giật căng!";
                        if (ftm != null) ftm.addText("SỐC LỰC TIẾP NƯỚC!", 640f, 460f, Color.RED);
                        AudioManager.getInstance().playSFX("sfx_fail");
                        if (screenShake != null) screenShake.shakeMedium();
                    } else {
                        player.increaseTension(player.getMaxLineTension() * 0.10f);
                        combatMessage = "CÁ TIẾP NƯỚC!";
                    }
                    com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("nhay_vot", 640f, 360f);
                }
            }
        }
        if (fishSongAmTimer > 0) {
            fishSongAmTimer -= delta;
            player.increaseTension((com.badlogic.gdx.math.MathUtils.randomBoolean() ? 10f : -10f) * delta);
        }
        if (fishPhunMucMuTimer > 0) fishPhunMucMuTimer -= delta;
        if (fishSkillGCDTimer > 0) fishSkillGCDTimer -= delta;
        if (fishPhantomInvulnTimer > 0) fishPhantomInvulnTimer -= delta;
        if (frankStunTimer > 0) frankStunTimer -= delta;
        if (haiXaSietCuocTimer > 0) haiXaSietCuocTimer -= delta;
        if (thanatosVucXoayTimer > 0) {
            thanatosVucXoayTimer -= delta;
            player.increaseTension(15.0f * delta);
        }
        if (thanatosVulnerableTimer > 0) thanatosVulnerableTimer -= delta;
        if (playerWeaknessTimer > 0) playerWeaknessTimer -= delta;
        if (playerSkillLockedTimer > 0) playerSkillLockedTimer -= delta;
        if (playerSealStaminaRegenTimer > 0) {
            playerSealStaminaRegenTimer -= delta;
            player.setStaminaHealMultiplier(0f);
        } else if (vucDaDebuffActive) {
            player.setStaminaHealMultiplier(0.70f);
        } else {
            player.setStaminaHealMultiplier(1.0f);
        }
        if (haiXaDocTimer > 0) {
            haiXaDocTimer -= delta;
            player.consumeStamina(5.0f * delta);
        }
        if (fishHutMauTimer > 0) {
            fishHutMauTimer -= delta;
            player.consumeStamina(20f * delta);
            currentFish.takeDamage(-currentFish.getMaxHp() * 0.04f * delta); // Hồi 4% mỗi giây
        }
        if (fishBaoToTimer > 0) {
            fishBaoToTimer -= delta;
            if (isSpaceDown) player.consumeStamina(25f * delta);
        }

        combatDuration += delta;

        // --- Halloween Passives ---
        if (currentFish != null) {
            // Frankenstein hoi sinh 1 lan
            if (currentFish.getCurrentHp() <= 0f && !frankRevived && currentFish.getSkill("sam_set_hoi_sinh") != null) {
                frankRevived = true;
                currentFish.heal(currentFish.getMaxHp() * 0.20f);
                player.increaseTension(player.getMaxLineTension() * 0.25f);
                combatMessage = "SẤM SÉT HỒI SINH! Frankenstein hồi sinh 20% HP & Giật +25% Tension!";
                if (ftm != null) ftm.addText("SẤM SÉT HỒI SINH! (+450 HP)", 640f, 450f, Color.YELLOW);
                AudioManager.getInstance().playSFX("sfx_skill");
                if (screenShake != null) screenShake.shakeHeavy();
            }

            // Hoa Nguc Lan Tinh: Thieu dot the luc neu tension > 70%
            if (currentFish.getPassiveSkill("hoa_nguc_lan_tinh") != null) {
                if (player.getCurrentLineTension() / player.getMaxLineTension() > 0.70f) {
                    player.consumeStamina(3.0f * delta);
                }
            }

            // Khat Mau Dem Ram: Hoi 1% HP/s khi player < 50% the luc
            if (currentFish.getPassiveSkill("khat_mau_dem_ram") != null) {
                if ((player.getCurrentStamina() / Math.max(1f, player.getMaxStamina())) < 0.50f) {
                    currentFish.heal(currentFish.getMaxHp() * 0.01f * delta);
                }
            }

            // The Xac Hu Vo: Moi 8s hoa khói 2.5s mien nhiem
            if (currentFish.getPassiveSkill("the_xac_hu_vo") != null) {
                phantomInvulnCooldown -= delta;
                if (phantomInvulnCooldown <= 0f) {
                    phantomInvulnCooldown = 10.5f;
                    fishPhantomInvulnTimer = 2.5f;
                    combatMessage = "THỂ XÁC HƯ VÔ! Miễn nhiễm toàn bộ sát thương 2.5s!";
                    if (ftm != null) ftm.addText("THỂ XÁC HƯ VÔ!", 640f, 440f, Color.PURPLE);
                    AudioManager.getInstance().playSFX("sfx_skill");
                }
            }

            // Uon Luon Ma Quai: Quat duoi +10 tension khi sang PULLING
            if (prevFishState == com.fishingrpg.game.entities.Fish.State.IDLE && currentFish.getState() == com.fishingrpg.game.entities.Fish.State.PULLING) {
                if (currentFish.getPassiveSkill("uon_luon_ma_quai") != null) {
                    player.increaseTension(10f);
                    if (ftm != null) ftm.addText("QUẤT ĐUÔI! +10 TENSION", 640f, 430f, Color.VIOLET);
                }
            }
        }

        // Thân đao xé gió: Giật +12% Tension ngay khi chuyển sang PULLING
        if (currentFish != null) {
            if (prevFishState == com.fishingrpg.game.entities.Fish.State.IDLE && currentFish.getState() == com.fishingrpg.game.entities.Fish.State.PULLING) {
                if (currentFish.getPassiveSkill("than_dao") != null) {
                    player.increaseTension(player.getMaxLineTension() * 0.12f);
                    if (ftm != null) ftm.addText("THÂN ĐAO XÉ GIÓ!", 640f, 430f, Color.SKY);
                    AudioManager.getInstance().playSFX("sfx_skill");
                    if (screenShake != null) screenShake.shakeLight();
                }
            }
            prevFishState = currentFish.getState();
        }

        // Thính mùi máu: Float text khi stamina người chơi < 50%
        if (currentFish != null && currentFish.getPassiveSkill("thinh_mui_mau") != null) {
            boolean isUnder50 = (player.getCurrentStamina() / Math.max(1f, player.getMaxStamina())) < 0.50f;
            if (isUnder50) {
                if (!thinhMuiMauNotified) {
                    thinhMuiMauNotified = true;
                    if (ftm != null) ftm.addText("THÍNH MÙI MÁU!", 640f, 440f, Color.RED);
                    AudioManager.getInstance().playSFX("sfx_skill");
                    if (screenShake != null) screenShake.shakeMedium();
                }
            } else {
                thinhMuiMauNotified = false;
            }
        }

        // Nương hải lưu: Float text khi tension > 55%
        if (currentFish != null && currentFish.getPassiveSkill("nuong_hai_luu") != null) {
            boolean over55 = (player.getCurrentLineTension() / Math.max(1f, player.getMaxLineTension())) > 0.55f;
            if (over55) {
                if (!nuongHaiLuuNotified) {
                    nuongHaiLuuNotified = true;
                    if (ftm != null) ftm.addText("NƯƠNG HẢI LƯU!", 640f, 420f, Color.ORANGE);
                }
            } else {
                nuongHaiLuuNotified = false;
            }
        }

        float effectiveMaxTension = getEffectiveMaxTension();

        if (fishXoayNuocTimer > 0f) {
            fishXoayNuocTimer -= delta;
            player.increaseTension(com.badlogic.gdx.math.MathUtils.sin(combatDuration * 7f) * 15f * delta);
        }
        if (fishVucDaTimer > 0f) {
            fishVucDaTimer -= delta;
            if (isSpaceDown) player.consumeStamina(8f * delta);
        }
        if (marlinEnrageActive) {
            marlinSurgeTimer += delta;
            marlinStackTimer += delta;
            if (marlinStackTimer >= 6.0f && marlinPullStacks < 5) {
                marlinStackTimer = 0f;
                marlinPullStacks++;
                combatMessage = "CÁ CỜ BỨT TỐC! Lực kéo +" + (marlinPullStacks * 10) + "%!";
                if (ftm != null) ftm.addText("CÁ CỜ BỨT TỐC!", 640f, 420f, Color.ORANGE);
                AudioManager.getInstance().playSFX("sfx_skill");
                if (screenShake != null) screenShake.shakeMedium();
            }
        }
        if (currentFish != null && currentFish.getPassiveSkill("can_day") != null) {
            if (currentFish.getState() == com.fishingrpg.game.entities.Fish.State.PULLING && fishCanDayCooldown <= 0f) {
                fishCanDayCooldown = 6.0f;
                if (com.badlogic.gdx.math.MathUtils.randomBoolean(0.25f)) {
                    fishCanDayTimer = 4.0f;
                    combatMessage = "CÁ NHỒNG CẮN DÂY! Giảm 10% Max Tension an toàn!";
                    if (ftm != null) ftm.addText("CẮN DÂY!", 640f, 430f, Color.ORANGE);
                }
            }
        }
        if (fishCanDayCooldown > 0f) fishCanDayCooldown -= delta;
        if (fishCanDayTimer > 0f) fishCanDayTimer -= delta;
        if (fishCanXeTimer > 0f) fishCanXeTimer -= delta;
        if (currentFish != null && currentFish.getPassiveSkill("doi_huong") != null) {
            fishDoiHuongTimer -= delta;
            if (fishDoiHuongTimer <= 0f) {
                fishDoiHuongTimer = com.badlogic.gdx.math.MathUtils.random(3.5f, 5.0f);
                if (com.badlogic.gdx.math.MathUtils.randomBoolean(0.20f)) {
                    float jitter = com.badlogic.gdx.math.MathUtils.random(-player.getMaxLineTension() * 0.10f, player.getMaxLineTension() * 0.10f);
                    player.increaseTension(jitter);
                    combatMessage = "CÁ ĐỔI HƯỚNG! Kim Tension chao đảo!";
                }
            }
        }
        if (fishTroLucTextTimer > 0f) fishTroLucTextTimer -= delta;
        if (fishPhongDienFxTimer > 0f) fishPhongDienFxTimer -= delta;
        if (currentFish != null && currentFish.getPassiveSkill("gai_doc_san_ho") != null
                && currentFish.getState() == com.fishingrpg.game.entities.Fish.State.PULLING
                && isSpaceDown) {
            player.consumeStamina(1.5f * delta);
        }
        if (currentFish != null && currentFish.getPassiveSkill("phong_dien") != null
                && currentFish.getState() == com.fishingrpg.game.entities.Fish.State.PULLING
                && isSpaceDown && playerHoldSpaceDuration > 2.5f) {
            player.consumeStamina(12f * delta);
            if (fishPhongDienFxTimer <= 0f) {
                fishPhongDienFxTimer = 1.5f;
                if (screenShake != null) screenShake.shakeLight();
                AudioManager.getInstance().playSFX("sfx_skill");
                if (ftm != null) ftm.addText("SỐC ĐIỆN!", 640f, 430f, Color.CYAN);
            }
        }

        if (currentFish != null && currentFish.getPassiveSkill("ap_luc_sau") != null) {
            effectiveMaxTension *= 0.75f;
        }
        if (player.isOverclockPotionActive()) {
            effectiveMaxTension *= 0.7f;
        }
        if (player.getCurrentLineTension() >= effectiveMaxTension) {
            // Trái Tim Đại Dương
            if (player.isSkillEquipped("trai_tim_dai_duong")) {
                float chance = player.getSkillTree().getTraiTimDaiDuongChance();
                if (MathUtils.random(100f) < chance) {
                    currentFish.takeDamage(99999f); // Instakill
                    combatMessage = "TRÁI TIM ĐẠI DƯƠNG! Cá đã kiệt sức ngay lập tức!";
                    return; // Wait for next tick to finishEncounter(true)
                }
            }
            if (player.isGhostCandleActive()) {
                player.setGhostCandleActive(false);
                player.setLineTension(player.getMaxLineTension() * 0.50f);
                combatMessage = "NẾN HỒN MA BẢO HỘ! Cứu đứt cước thành công, Tension giảm về 50%!";
                if (ftm != null) ftm.addText("NẾN HỒN MA CỨU ĐỨT DÂY!", 640f, 450f, Color.CYAN);
                AudioManager.getInstance().playSFX("sfx_ghost_candle");
                return;
            }
            finishEncounter(false, "ĐỨT DÂY CÂU!");
            return;
        }
        if (player.getCurrentStamina() <= 0) {
            finishEncounter(false, "HẾT THỂ LỰC! CÁ ĐÃ CHẠY MẤT!");
            return;
        }

        float extraIdleTime = 0f;
        if (player.isSkillEquipped("thuan_hoa")) {
            extraIdleTime = player.getSkillTree().getThuanHoaBonus();
        }
        currentFish.update(delta, extraIdleTime);

        float tensionIncrease = 0f;

        // Fish Pulling & Water Resistance (Cá giằng co PULLING & Lực cản khi cá mệt IDLE)
        float pullMult;
        if (currentFish.getState() == com.fishingrpg.game.entities.Fish.State.PULLING) {
            // Khi cá vùng vẫy: kéo thì 1.5x, buông chỉ chịu 40% lực giằng (0.4x thay vì 0.8x)
            pullMult = isSpaceDown ? 1.5f : 0.4f;
        } else {
            // Khi cá mệt (IDLE): Nerf mạnh lực cản khi kéo (0.15x -> 0.03x), buông dây cá thả trôi hoàn toàn (0.10x -> 0.0x)
            pullMult = isSpaceDown ? 0.03f : 0.0f;
        }
        float pull = currentFish.getPullStrength() * pullMult;
        if (currentFish.getPassiveSkill("xuc_tu") != null) {
            pull *= (1f + (combatDuration / 10f) * 0.1f);
        }
        float pullReduceFromSubstat = player.getEquipmentTotalStat(com.fishingrpg.game.entities.equipment.Substat.Type.FISH_PULL_REDUCE) / 100f;
        float khangDut = player.getEquippedLine() != null ? player.getEquippedLine().mainStat2 : 0f;
        float pullReduceFromLine = (khangDut * 1.5f) / 100f;
        float totalPullMult = Math.max(0.25f, 1f - (pullReduceFromSubstat + pullReduceFromLine));
        pull *= totalPullMult;
        if (fishButTocTimer > 0) pull *= 2f;
        if (marlinEnrageActive) pull *= (1.0f + marlinPullStacks * 0.10f);
        if (currentFish.getPassiveSkill("nuong_hai_luu") != null 
                && (player.getCurrentLineTension() / Math.max(1f, player.getMaxLineTension())) > 0.55f) {
            pull *= 1.18f;
        }
        if (currentFish.getPassiveSkill("than_dao") != null) {
            pull *= 1.40f; // +40% lực kéo trong pha PULLING ngắn
        }
        if (currentFish.getPassiveSkill("thinh_mui_mau") != null 
                && (player.getCurrentStamina() / Math.max(1f, player.getMaxStamina())) < 0.50f) {
            pull *= 1.20f;
        }
        if (currentFish.getPassiveSkill("vung_vay") != null) {
            if (com.badlogic.gdx.math.MathUtils.random(100f) < 2f) {
                pull *= 3f;
            }
        }
        
        if (luotDayTimer > 0 || abilitySystem.hasEffect("effect_luot_day")) {
            pull *= abilitySystem.getFishPullMultiplier();
        }

        if (currentFish.getPassiveSkill("mui_kiem_xe_bien") != null) {
            luotDayTimer = 0f;
            troiBuocTimer = 0f;
        }

        boolean isTroiBuoc = troiBuocTimer > 0 || abilitySystem.isFishFrozen() || abilitySystem.hasEffect("effect_troi_buoc");
        if (isTroiBuoc) {
            pull = 0f; // Stopped
        }
        
        // Nhip Nhang: if holding space, chance to negate pull
        if (isSpaceDown && player.isSkillEquipped("nhip_nhang")) {
            if (MathUtils.random(100f) < player.getSkillTree().getNhipNhangChance()) {
                pull = 0f;
            }
        }
        if (player.isRiskyBaitActive()) {
            pull *= 2f;
        }
        if (currentFish != null && currentFish.getPassiveSkill("hoa_nguc_lan_tinh") != null && currentFish.getState() == com.fishingrpg.game.entities.Fish.State.PULLING) {
            pull *= 1.25f; // +25% sinh tension
        }
        if (haiXaSietCuocTimer > 0f && isSpaceDown) {
            pull *= 2.0f; // Mang xa siet cuoc: keo luc nay sinh tension x2
        }
        tensionIncrease += pull * delta;

        // Băng Giá set bonus: đóng băng Tension tăng từ cá kéo
        if (player.isSetBonusActive() && player.getEquippedRod() != null
                && player.getEquippedRod().setName.equals("Băng Giá")) {
            tensionIncrease = 0f;
        }

        boolean isDuongSuc = duongSucChannelTimer > 0 || abilitySystem.hasEffect("effect_duong_suc");

        // Player Action
        if (isSpaceDown && !isDuongSuc && frankStunTimer <= 0f) {
            // Frankenstein: Qua tai dien nang neu keo > 1.5s
            if (currentFish != null && currentFish.getPassiveSkill("qua_tai_dien_nang") != null) {
                frankHoldSpaceDuration += delta;
                if (frankHoldSpaceDuration >= 1.5f) {
                    frankHoldSpaceDuration = 0f;
                    frankStunTimer = 0.6f;
                    player.consumeStamina(10f);
                    combatMessage = "QUÁ TẢI ĐIỆN NĂNG! Sốc điện -10 Thể lực & Ngắt kéo dây 0.6s!";
                    if (ftm != null) ftm.addText("QUÁ TẢI ĐIỆN NĂNG! (-10 TL)", 640f, 430f, Color.CYAN);
                    AudioManager.getInstance().playSFX("sfx_fail");
                    if (screenShake != null) screenShake.shakeMedium();
                }
            } else {
                frankHoldSpaceDuration = 0f;
            }
            playerHoldSpaceDuration += delta;
            if (currentFish.getPassiveSkill("mui_kiem_xe_bien") != null && playerHoldSpaceDuration > 1.2f) {
                float extraTension = player.getMaxLineTension() * 0.20f * (playerHoldSpaceDuration - 1.2f) * delta;
                tensionIncrease += extraTension;
                if (!muiKiemHoldWarned) {
                    muiKiemHoldWarned = true;
                    if (ftm != null) ftm.addText("MŨI KIẾM XÉ BIỂN!", 640f, 430f, Color.RED);
                }
            } else if (playerHoldSpaceDuration <= 1.2f) {
                muiKiemHoldWarned = false;
            }
            if (!isTroiBuoc) {
                // Reeling in always adds moderate baseline tension; reeling while fish pulls adds extra tension
                if (currentFish.getState() == com.fishingrpg.game.entities.Fish.State.PULLING) {
                    tensionIncrease += (26f + player.getMaxLineTension() * 0.055f) * delta;
                } else {
                    tensionIncrease += (10f + player.getMaxLineTension() * 0.020f) * delta;
                }
            }
            
            // Deal damage
            float dmgMult = abilitySystem.getDamageMultiplier();
            if (phaGiapTimer > 0) dmgMult += phaGiapDebuff;
            if (player.getWhetstoneCharges() > 0) dmgMult += 0.25f; // Đá mài tăng +25% sát thương kéo
            float dmg = player.getBasePullDamage() * dmgMult * delta;
            if (playerWeaknessTimer > 0f) dmg *= 0.60f; // -40% luc keo
            if (thanatosVulnerableTimer > 0f) dmg *= 2.0f; // x2 sat thuong khi choang
            if (currentFish != null && currentFish.getPassiveSkill("cot_cach_bat_diet") != null) {
                dmg *= 0.80f; // Giam 20% sat thuong keo thuong
            }
            // Set Huyết Nguyệt Thần Vương: Quăng Perfect x2 Sát thương trong 2s đầu
            if (perfectCastBuffTimer > 0f && player.getEquipment() != null
                    && player.getEquipment().isSetBonusActive()
                    && "Huyết Nguyệt Thần Vương".equals(player.getEquipment().getActiveSetName())) {
                dmg *= 2.0f;
            }
            if (fishCuongNoTimer > 0 || fishPhantomInvulnTimer > 0f) {
                dmg = 0f;
                if (fishPhantomInvulnTimer > 0f && ftm != null && MathUtils.randomBoolean(0.08f)) {
                    ftm.addText("HƯ VÔ!", 640f, 380f, Color.PURPLE);
                }
            } else {
                if (currentFish.getPassiveSkill("tron_truot") != null) dmg *= 0.80f;
                if (currentFish.getPassiveSkill("ben_bi") != null)     dmg *= 0.85f;
                if (currentFish.getPassiveSkill("vo_cung") != null)    dmg *= 0.80f;
                if (currentFish.getPassiveSkill("an_minh") != null && currentFish.getState() == com.fishingrpg.game.entities.Fish.State.IDLE) dmg *= 0.75f;
                // Cuồng Phong Ngự Kiếm: Boss bất tử khi ở trên không
                if (marlinEnrageActive && fishNhayVotTimer > 0) {
                    dmg = 0f;
                } else if (fishNhayVotTimer > 0) {
                    dmg *= 0.50f;
                }
                if (currentFish.getPassiveSkill("than_ho_co_thach") != null) {
                    dmg = Math.max(0f, dmg - 15f * delta);
                    if (!thanHoCoThachNotified) {
                        thanHoCoThachNotified = true;
                        if (ftm != null) ftm.addText("THẦN HỘ CỔ THẠCH!", 640f, 410f, Color.LIGHT_GRAY);
                    }
                }
                if (currentFish.getPassiveSkill("tro_luc") != null) {
                    if (com.badlogic.gdx.math.MathUtils.randomBoolean(0.25f)) {
                        dmg = 0f;
                        if (fishTroLucTextTimer <= 0f) {
                            if (ftm != null) ftm.addText("TRƠ LỰC!", 640f, 430f, Color.LIGHT_GRAY);
                            fishTroLucTextTimer = 0.8f;
                        }
                    }
                }
            }
            currentFish.takeDamage(dmg);
            if (player.getBloodMoonTalismanTimer() > 0f) {
                currentFish.takeTrueDamage(dmg * 0.10f);
            }
            accumDamage += dmg;

            // Set Huyết Nguyệt Thần Vương: Kéo dây hút 3% HP cá (tối đa 12.0 Thể lực/giây) chuyển thành Thể lực người chơi
            if (player.getEquipment() != null && player.getEquipment().isSetBonusActive()
                    && "Huyết Nguyệt Thần Vương".equals(player.getEquipment().getActiveSetName())) {
                float drainPerSec = Math.min(currentFish.getCurrentHp() * 0.03f, 12.0f);
                float staminaHeal = drainPerSec * delta;
                if (staminaHeal > 0f) {
                    player.consumeStamina(-staminaHeal);
                    currentFish.takeDamage(staminaHeal);
                    accumDamage += staminaHeal;
                }
            }
            
            slackTimer = 0f;
        } else {
            playerHoldSpaceDuration = 0f;
            // Slack: không giữ dây hoặc đang vận sức Dưỡng Sức → tension tự giảm an toàn (đã giảm mạnh tốc độ xả tự nhiên)
            float tensionDec = (6f + player.getMaxLineTension() * 0.04f) * delta;
            if (haiXaSietCuocTimer > 0f) {
                tensionDec = 0f; // Khoa cung tension khong the giam
            }
            if (currentFish != null && currentFish.getPassiveSkill("uon_luon_ma_quai") != null) {
                tensionDec *= 0.70f; // Giam 30% toc do xa chung
            }
            if (currentFish.getPassiveSkill("xuc_tu") != null) {
                tensionDec *= 0.7f; // Xúc tu quấn chặt làm dây chùng chậm hơn 30%
            }
            if (fishXoayNuocTimer > 0f) {
                tensionDec *= 0.60f; // Xoáy nước giảm 40% tốc độ xả dây
            }
            player.decreaseTension(tensionDec);
            
            if (player.getCurrentLineTension() <= 0f) {
                slackTimer += delta;
                if (slackTimer >= 2.0f && !isDuongSuc) {
                    finishEncounter(false, "CÁ SẨY VÌ DÂY QUÁ CHÙNG!");
                    return;
                }
            } else {
                slackTimer = 0f;
            }
        }
        
        if (isTroiBuoc) {
            tensionIncrease = 0f; // Trói Buộc: triệt tiêu hoàn toàn sự gia tăng lực căng dây
        }
        
        player.increaseTension(tensionIncrease);
        float curTensionRatio = player.getCurrentLineTension() / Math.max(1f, player.getMaxLineTension());
        if (curTensionRatio > maxTensionRatioInFight) maxTensionRatioInFight = curTensionRatio;

        // Stamina drain: tụt nhanh hơn một chút khi cá PULLING và biến thiên theo sát thương/lực kéo (pullStrength) của cá
        float drainMult = 1f;
        if (player.isSkillEquipped("co_bap_thep")) drainMult -= (player.getSkillTree().getCoBapThepBonus() / 100f);
        if (phanXaActive) drainMult -= (player.getSkillTree().getPhanXaBonus() / 100f);
        if (marlinEnrageActive) {
            drainMult += (marlinSurgeTimer * 0.01f); // Mỗi 1s trôi qua sau Cuồng Phong Ngự Kiếm tăng +1% tốc độ tụt thể lực
        }
        if (drainMult < 0f) drainMult = 0f;
        
        boolean isFishPulling = (currentFish != null && currentFish.getState() == com.fishingrpg.game.entities.Fish.State.PULLING);
        float baseDrain = isFishPulling ? 3.0f : 2.0f; // Nhanh hơn một chút khi cá PULLING
        float fishPullDmg = (currentFish != null) ? currentFish.getPullStrength() : 50f;
        float pullDmgScaling = fishPullDmg * 0.015f; // Biến thiên nhẹ theo damage của cá
        float totalDrainPerSec = (baseDrain + pullDmgScaling) * drainMult;
        
        player.consumeStamina(totalDrainPerSec * delta);
        
        // Hồi Phục Nhanh
        if (player.isSkillEquipped("hoi_phuc_nhanh")) {
            player.consumeStamina(-player.getSkillTree().getHoiPhucNhanhBonus() * delta);
        }
        
        accumTimer += delta;
        if (accumTimer >= 0.5f) {
            if (accumDamage > 0) {
                if (ftm != null) ftm.addText("-" + (int)accumDamage, 640f + com.badlogic.gdx.math.MathUtils.random(-30, 30), 380f + com.badlogic.gdx.math.MathUtils.random(-30, 30), Color.RED);
                accumDamage = 0f;
            }
            accumTimer = 0f;
        }
    }

    public float giatKepCooldown = 0f;
    public float duongSucCooldown = 0f;
    public float duongSucChannelTimer = 0f;
    
    private float accumDamage = 0f;
    private float accumTimer = 0f;

    private void handleSkills(boolean[] activeInputs, boolean isSpaceDown, float delta) {
        String[] hotkeyNames = {"Q", "W", "E", "R"};
        com.fishingrpg.game.systems.gas.AbilityContext gasCtx = new com.fishingrpg.game.systems.gas.AbilityContext(player, currentFish, this, ftm, screenShake);
        for (int i = 0; i < 4; i++) {
            if (activeInputs[i]) {
                String skillId = player.getEquippedActives()[i];
                if (skillId == null) {
                    if (ftm != null) ftm.addText("Ô [" + hotkeyNames[i] + "] chưa gắn kỹ năng!", 640f + MathUtils.random(-25, 25), 380f, Color.YELLOW);
                    AudioManager.getInstance().playSFX("sfx_button");
                    continue;
                }
                float cdMult = 1f;
                if (player.isSetBonusActive() && player.getEquippedRod() != null && player.getEquippedRod().setName.equals("Thời Không")) {
                    cdMult = 0.8f;
                }
                // Thử kích hoạt qua hệ thống GAS chuẩn hóa
                boolean activated = abilitySystem.tryActivateAbility(skillId, gasCtx, cdMult);
                if (!activated && !abilitySystem.hasEffect(skillId)) {
                    // Fallback to legacy implementation if not yet migrated
                    useActiveSkill(skillId, cdMult);
                }
            }
        }
        
        if (!isSpaceDown) {
            float giuDay = 0f;
            if (player.isSkillEquipped("giu_day")) {
                giuDay = player.getSkillTree().getGiuDayBonus();
            }
            if (giuDay > 0) player.decreaseTension(giuDay * delta);
        }
    }
    
    private void useActiveSkill(String skillId, float cdMult) {
        float dmgMult = 1f;
        if (phaGiapTimer > 0) dmgMult += phaGiapDebuff;
        boolean skillUsed = false;

        if (skillId.equals("keo_manh")) {
            if (keoManhCooldown > 0) {
                if (ftm != null) ftm.addText("Kéo Mạnh đang hồi (" + String.format(java.util.Locale.US, "%.1fs", keoManhCooldown) + ")!", 640f, 380f, Color.GRAY);
                return;
            }
            float cost = player.getSkillTree().getKeoManhStaminaCost();
            float dmg = player.getSkillTree().getKeoManhDamage();
            
            // Bạo Kích
            boolean crit = false;
            if (player.isSkillEquipped("bao_kich")) {
                if (MathUtils.random(100f) < player.getSkillTree().getBaoKichChance()) {
                    crit = true;
                    dmg *= 2f;
                }
            }
            
            if (player.consumeStamina(cost)) {
                float fishDef = 1f;
                if (currentFish.getPassiveSkill("vo_cung") != null) fishDef -= 0.3f;
                if (currentFish.getPassiveSkill("vay_cung") != null) fishDef -= 0.5f;
                float finalDmg = dmg * dmgMult * fishDef;
                currentFish.takeDamage(finalDmg);
                if (ftm != null) ftm.addText(crit ? "BẠO KÍCH! -" + (int)finalDmg : "KÉO MẠNH! -" + (int)finalDmg, 640f + MathUtils.random(-30, 30), 400f + MathUtils.random(-20, 20), crit ? Color.GOLD : new Color(1f, 0.45f, 0.2f, 1f));

                player.increaseTension(15f);
                combatMessage = crit ? "BẠO KÍCH KÉO MẠNH! Xung lực bộc phá (-" + (int)cost + " Thể lực)" : "KÉO MẠNH! Giật cần xung lực cực đại! (-" + (int)cost + " Thể lực)";
                AudioManager.getInstance().playSFX("sfx_skill");
                com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerPlayerSkillVFX("keo_manh", 640f, 340f);
                keoManhCooldown = 3.5f * cdMult;
                skillUsed = true;
            } else {
                if (ftm != null) ftm.addText("Không đủ Thể lực (-" + (int)cost + ")!", 640f, 380f, new Color(1f, 0.45f, 0.4f, 1f));
            }
        } else if (skillId.equals("giat_kep")) {
            if (giatKepCooldown > 0) {
                if (ftm != null) ftm.addText("Giật Kép đang hồi (" + String.format(java.util.Locale.US, "%.1fs", giatKepCooldown) + ")!", 640f, 380f, Color.GRAY);
                return;
            }
            float cost = 40f;
            float dmg = player.getSkillTree().getKeoManhDamage() * player.getSkillTree().getGiatKepDamageMult();
            if (player.consumeStamina(cost)) {
                float fishDef = 1f;
                if (currentFish.getPassiveSkill("vo_cung") != null) fishDef -= 0.3f;
                if (currentFish.getPassiveSkill("vay_cung") != null) fishDef -= 0.5f;
                float finalDmg = dmg * dmgMult * fishDef;
                currentFish.takeDamage(finalDmg);
                if (ftm != null) ftm.addText("-" + (int)finalDmg, 640f + MathUtils.random(-50, 50), 360f + MathUtils.random(-50, 50), Color.RED);

                player.increaseTension(30f);
                combatMessage = "GIẬT KÉP! (-40 Thể lực)";
                AudioManager.getInstance().playSFX("sfx_skill");
                com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerPlayerSkillVFX("giat_kep", 640f, 360f);
                giatKepCooldown = 8.0f * cdMult;
                skillUsed = true;
            } else {
                if (ftm != null) ftm.addText("Không đủ Thể lực (-40)!", 640f, 380f, new Color(1f, 0.45f, 0.4f, 1f));
            }
        } else if (skillId.equals("duong_suc")) {
            if (duongSucCooldown > 0 || duongSucChannelTimer > 0) {
                if (ftm != null) ftm.addText("Dưỡng Sức đang hồi (" + String.format(java.util.Locale.US, "%.1fs", duongSucCooldown) + ")!", 640f, 380f, Color.GRAY);
                return;
            }
            float channelTime = player.getSkillTree().getDuongSucChannelTime();
            float totalHeal = player.getSkillTree().getDuongSucHeal();
            float regenPerSec = totalHeal / Math.max(0.1f, channelTime);
            com.fishingrpg.game.systems.gas.GameplayEffect effect = new com.fishingrpg.game.systems.gas.GameplayEffect("effect_duong_suc", "Dưỡng Sức", channelTime)
                    .setStaminaRegenPerSec(regenPerSec);
            abilitySystem.applyEffect(effect);
            duongSucChannelTimer = channelTime;
            duongSucCooldown = 9.0f * cdMult;
            com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerPlayerSkillVFX("duong_suc", 640f, 360f);
            skillUsed = true;
        } else if (skillId.equals("pha_giap")) {
            if (phaGiapCooldown > 0) {
                if (ftm != null) ftm.addText("Phá Giáp đang hồi (" + String.format(java.util.Locale.US, "%.1fs", phaGiapCooldown) + ")!", 640f, 380f, Color.GRAY);
                return;
            }
            float hpDmg = player.getSkillTree().getPhaGiapDamage();
            float stamCost = 30f;
            if (player.consumeStamina(stamCost)) {
                float fishDef = 1f;
                if (currentFish.getPassiveSkill("vo_cung") != null) fishDef -= 0.3f;
                if (currentFish.getPassiveSkill("vay_cung") != null) fishDef -= 0.5f;
                float finalDmg = hpDmg * fishDef;
                currentFish.takeDamage(finalDmg);
                if (ftm != null) ftm.addText("-" + (int)finalDmg, 640f + MathUtils.random(-50, 50), 360f + MathUtils.random(-50, 50), Color.RED);

                phaGiapTimer = 10f;
                phaGiapDebuff = player.getSkillTree().getPhaGiapDebuff() / 100f;
                combatMessage = "PHÁ GIÁP! (-" + (int)stamCost + " Thể lực)";
                AudioManager.getInstance().playSFX("sfx_skill");
                com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerPlayerSkillVFX("pha_giap", 640f, 360f);
                phaGiapCooldown = 14.0f * cdMult;
                skillUsed = true;
            } else {
                if (ftm != null) ftm.addText("Không đủ Thể lực (-30)!", 640f, 380f, new Color(1f, 0.45f, 0.4f, 1f));
            }
        } else if (skillId.equals("luot_day")) {
            if (luotDayCooldown > 0) {
                if (ftm != null) ftm.addText("Lướt Dây đang hồi (" + String.format(java.util.Locale.US, "%.1fs", luotDayCooldown) + ")!", 640f, 380f, Color.GRAY);
                return;
            }
            luotDayTimer = player.getSkillTree().getLuotDayDuration();
            combatMessage = "LƯỚT DÂY! (Làm yếu lực kéo của cá)";
            luotDayCooldown = 12.0f * cdMult;
            com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerPlayerSkillVFX("luot_day", 640f, 360f);
            skillUsed = true;
        } else if (skillId.equals("loi_nguyen_troi_buoc")) {
            if (troiBuocCooldown > 0) {
                if (ftm != null) ftm.addText("Trói Buộc đang hồi (" + String.format(java.util.Locale.US, "%.1fs", troiBuocCooldown) + ")!", 640f, 380f, Color.GRAY);
                return;
            }
            troiBuocTimer = player.getSkillTree().getLoiNguyenTroiBuocDuration();
            combatMessage = "TRÓI BUỘC! Cá không thể di chuyển!";
            troiBuocCooldown = 16.0f * cdMult;
            com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerPlayerSkillVFX("loi_nguyen_troi_buoc", 640f, 360f);
            skillUsed = true;
        }
        
        if (skillUsed) {
            if (screenShake != null) screenShake.shakeMedium();
            player.getQuestManager().addProgress("skill", 1);
        }
    }
}
