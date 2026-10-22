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

    // --- New mechanics fields ---
    public Catchable predictedCatch = null;
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
    private FloatingTextManager ftm;

    public FishingManager(Player player) {
        this.player = player;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public void setFloatingTextManager(FloatingTextManager ftm) {
        this.ftm = ftm;
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
        } else {
            predictedCatch = null;
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
        state = State.BITE;
        float pBonus = 0f;
        if (player.isSkillEquipped("nhanh_tay")) {
            pBonus = player.getSkillTree().getNhanhTayBonusTime();
        }
        biteWindow = 1.0f + pBonus + castBonus * 0.5f;
        stateTimer = biteWindow;
        combatMessage = "";
    }

    public void startEncounter() {
        if (predictedCatch != null) {
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
        
        accumDamage = 0f;
        accumTimer = 0f;
    }

    public void finishEncounter(boolean success) {
        finishEncounter(success, "Thất bại!");
    }

    public void finishEncounter(boolean success, String failReason) {
        state = State.PRE_CAST;
        if (!rewardDone) {
            rewardDone = true;
            lastEncounterSuccess = success;
            
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
            player.setRiskyBaitActive(false);
            player.setOverclockPotionActive(false);
            if (player.getWhetstoneCharges() > 0) {
                player.consumeWhetstoneCharge();
            }
            if (player.getTreasureMagnetCharges() > 0) {
                player.consumeTreasureMagnetCharge();
            }
            if (player.getSonarCharges() > 0) {
                player.consumeSonarCharge();
            }
            player.setCastRarityBoost(0f);
            
            if (!success) {
                isLineBroken = true;
                combatMessage = failReason;
                resultLine = "";
            } else {
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
                
                int lvUp = player.gainXp(xp);

                if (currentCatch.getType() == CatchableType.CONSUMABLE) {
                    player.addItem(currentCatch.getName(), multiplier);
                } else {
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
                    fishEscapeTimer = 3.2f;
                    fishEscapeMessage = "CHẬM TAY RỒI! CÁ ĐÃ ĂN HẾT MỒI VÀ CHẠY MẤT!";
                    finishEncounter(false, "CHẬM TAY RỒI! CÁ ĐÃ ĂN HẾT MỒI VÀ CHẠY MẤT!");
                    if (ftm != null) ftm.addText("CÁ ĐÃ CHẠY MẤT!", 640f, 360f, new Color(1f, 0.35f, 0.25f, 1f));
                    AudioManager.getInstance().playSFX("sfx_button");
                }
                break;
            case COMBAT:
                if (duongSucChannelTimer > 0) {
                    duongSucChannelTimer -= delta;
                    combatMessage = "ĐANG VẬN SỨC... (" + String.format("%.1f", duongSucChannelTimer) + "s)";
                    if (duongSucChannelTimer <= 0) {
                        float recovery = player.getSkillTree().getDuongSucHeal();
                        player.consumeStamina(-recovery);
                        combatMessage = "DƯỠNG SỨC! (+" + (int)recovery + " Thể lực)";
                        if (ftm != null) ftm.addText("+" + (int)recovery, 640f, 400f, Color.GREEN);
                        AudioManager.getInstance().playSFX("sfx_skill");
                    }
                    isSpaceDown = false; // Block pulling while channeling
                }
                updateCombat(delta, isSpaceDown);
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

    private void updateCombat(float delta, boolean isSpaceDown) {
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
                    com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX(s.id, 640f, 360f);
                    if (s.id.equals("but_toc")) {
                        fishButTocTimer = 3f;
                        combatMessage = "CÁ BỨT TỐC! Lực kéo x2!";
                    } else if (s.id.equals("quay_bun")) {
                        fishQuayBunTimer = 4f;
                        combatMessage = "CÁ QUẪY BÙN! Không thấy thanh Tension!";
                    } else if (s.id.equals("lan_sau")) {
                        fishLanSauTimer = 2.5f;
                        player.increaseTension(player.getMaxLineTension() * 0.3f);
                        combatMessage = "CÁ LẶN SÂU! Tension tăng sốc!";
                    } else if (s.id.equals("song_than")) {
                        fishSongThanTimer = 3f;
                        combatMessage = "SÓNG THẦN! Thể lực sụt giảm liên tục!";
                    } else if (s.id.equals("cuong_no")) {
                        fishCuongNoTimer = 3f;
                        combatMessage = "CUỒNG NỘ! Bất tử và giải debuff!";
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
                    }
                    if (ftm != null) ftm.addText(com.fishingrpg.game.entities.Fish.getSkillDisplayName(s.id).toUpperCase() + "!", 640f, 450f, Color.YELLOW);
                }
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
            if (isSpaceDown) {
                player.increaseTension(15f * delta);
            }
            if (fishNhayVotTimer <= 0) {
                player.increaseTension(player.getMaxLineTension() * 0.35f); // Tiếp nước: giật 35% tension (đã nerf từ 90%!)
                combatMessage = "CÁ TIẾP NƯỚC! Dây bị giật mạnh!";
                com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerFishSkillVFX("nhay_vot", 640f, 360f);
            }
        }
        if (fishSongAmTimer > 0) {
            fishSongAmTimer -= delta;
            player.increaseTension((com.badlogic.gdx.math.MathUtils.randomBoolean() ? 10f : -10f) * delta);
        }
        if (fishPhunMucMuTimer > 0) fishPhunMucMuTimer -= delta;
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
        float effectiveMaxTension = player.getMaxLineTension();
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

        // Fish Pulling
        if (currentFish.getState() == com.fishingrpg.game.entities.Fish.State.PULLING) {
            float pull = currentFish.getCurrentPull() * (isSpaceDown ? 1.5f : 0.4f);
            if (currentFish.getPassiveSkill("xuc_tu") != null) {
                pull *= (1f + (combatDuration / 10f) * 0.1f);
            }
            pull *= Math.max(0.1f, 1f - (player.getEquipmentTotalStat(com.fishingrpg.game.entities.equipment.Substat.Type.FISH_PULL_REDUCE) / 100f));
            float khangDut = player.getEquippedLine() != null ? player.getEquippedLine().mainStat2 : 0f;
            pull *= Math.max(0.1f, 1f - ((khangDut * 2.5f) / 100f));
            if (fishButTocTimer > 0) pull *= 2f;
            if (currentFish.getPassiveSkill("vung_vay") != null) {
                if (com.badlogic.gdx.math.MathUtils.random(100f) < 2f) {
                    pull *= 3f;
                }
            }
            
            if (luotDayTimer > 0) {
                pull /= player.getSkillTree().getLuotDayMultiplier(); // Reduce pull strength
            }
            if (troiBuocTimer > 0) pull = 0f; // Stopped
            
            // Nhip Nhang: if holding space, chance to negate pull
            if (isSpaceDown && player.isSkillEquipped("nhip_nhang")) {
                if (MathUtils.random(100f) < player.getSkillTree().getNhipNhangChance()) {
                    pull = 0f;
                }
            }
            if (player.isRiskyBaitActive()) {
                pull *= 2f;
            }
            tensionIncrease += pull * delta;
        }

        // Băng Giá set bonus: đóng băng Tension tăng từ cá kéo
        if (player.isSetBonusActive() && player.getEquippedRod() != null
                && player.getEquippedRod().setName.equals("Băng Giá")) {
            tensionIncrease = 0f;
        }

        // Player Action
        if (isSpaceDown) {
            if (troiBuocTimer <= 0) {
                if (currentFish.getState() == com.fishingrpg.game.entities.Fish.State.PULLING) {
                    tensionIncrease += (20f + player.getMaxLineTension() * 0.05f) * delta; // Reel in
                }
            }
            
            // Deal damage
            float dmgMult = 1f;
            if (phaGiapTimer > 0) dmgMult += phaGiapDebuff;
            if (player.getWhetstoneCharges() > 0) dmgMult += 0.25f; // Đá mài tăng +25% sát thương kéo
            float dmg = player.getBasePullDamage() * dmgMult * delta;
            if (fishCuongNoTimer > 0) {
                dmg = 0f;
            } else {
                if (currentFish.getPassiveSkill("tron_truot") != null) dmg *= 0.80f;
                if (currentFish.getPassiveSkill("ben_bi") != null)     dmg *= 0.85f;
                if (currentFish.getPassiveSkill("vo_cung") != null)    dmg *= 0.80f;
                if (currentFish.getPassiveSkill("an_minh") != null && currentFish.getState() == com.fishingrpg.game.entities.Fish.State.IDLE) dmg *= 0.75f;
                if (fishNhayVotTimer > 0) dmg *= 0.50f;
            }
            currentFish.takeDamage(dmg);
            accumDamage += dmg;
            
            slackTimer = 0f;
        } else {
            // Slack: không giữ dây → tension tự giảm
            float tensionDec = (20f + player.getMaxLineTension() * 0.1f) * delta;
            if (currentFish.getPassiveSkill("xuc_tu") != null) {
                tensionDec *= 0.7f; // Xúc tu quấn chặt làm dây chùng chậm hơn 30%
            }
            player.decreaseTension(tensionDec);
            
            if (player.getCurrentLineTension() <= 0f) {
                slackTimer += delta;
                if (slackTimer >= 2.0f) {
                    finishEncounter(false, "CÁ SẨY VÌ DÂY QUÁ CHÙNG!");
                    return;
                }
            } else {
                slackTimer = 0f;
            }
        }
        
        player.increaseTension(tensionIncrease);

        // Stamina drain
        float drainMult = 1f;
        if (player.isSkillEquipped("co_bap_thep")) drainMult -= (player.getSkillTree().getCoBapThepBonus() / 100f);
        if (phanXaActive) drainMult -= (player.getSkillTree().getPhanXaBonus() / 100f);
        if (drainMult < 0f) drainMult = 0f;
        
        player.consumeStamina(2f * drainMult * delta);
        
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
        for (int i = 0; i < 4; i++) {
            if (activeInputs[i]) {
                String skillId = player.getEquippedActives()[i];
                if (skillId == null) {
                    if (ftm != null) ftm.addText("Ô [" + hotkeyNames[i] + "] chưa gắn kỹ năng!", 640f + MathUtils.random(-25, 25), 380f, Color.YELLOW);
                    AudioManager.getInstance().playSFX("sfx_button");
                    continue;
                }
                if (player.getSkillTree().getSkill(skillId) == null || !player.getSkillTree().getSkill(skillId).isUnlocked()) {
                    if (ftm != null) ftm.addText("Kỹ năng chưa mở khóa!", 640f + MathUtils.random(-25, 25), 380f, Color.ORANGE);
                    AudioManager.getInstance().playSFX("sfx_button");
                    continue;
                }
                float cdMult = 1f;
                if (player.isSetBonusActive() && player.getEquippedRod() != null && player.getEquippedRod().setName.equals("Thời Không")) {
                    cdMult = 0.8f;
                }
                useActiveSkill(skillId, cdMult);
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
                keoManhCooldown = 3f * cdMult;
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
                giatKepCooldown = 5f * cdMult;
                skillUsed = true;
            } else {
                if (ftm != null) ftm.addText("Không đủ Thể lực (-40)!", 640f, 380f, new Color(1f, 0.45f, 0.4f, 1f));
            }
        } else if (skillId.equals("duong_suc")) {
            if (duongSucCooldown > 0 || duongSucChannelTimer > 0) {
                if (ftm != null) ftm.addText("Dưỡng Sức đang hồi (" + String.format(java.util.Locale.US, "%.1fs", duongSucCooldown) + ")!", 640f, 380f, Color.GRAY);
                return;
            }
            duongSucChannelTimer = player.getSkillTree().getDuongSucChannelTime();
            duongSucCooldown = 10f * cdMult;
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
                phaGiapCooldown = 20f * cdMult;
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
            luotDayCooldown = 15f * cdMult;
            com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerPlayerSkillVFX("luot_day", 640f, 360f);
            skillUsed = true;
        } else if (skillId.equals("loi_nguyen_troi_buoc")) {
            if (troiBuocCooldown > 0) {
                if (ftm != null) ftm.addText("Trói Buộc đang hồi (" + String.format(java.util.Locale.US, "%.1fs", troiBuocCooldown) + ")!", 640f, 380f, Color.GRAY);
                return;
            }
            troiBuocTimer = player.getSkillTree().getLoiNguyenTroiBuocDuration();
            combatMessage = "TRÓI BUỘC! Cá không thể di chuyển!";
            troiBuocCooldown = 25f * cdMult;
            com.fishingrpg.game.ui.CombatVFXManager.getInstance().triggerPlayerSkillVFX("loi_nguyen_troi_buoc", 640f, 360f);
            skillUsed = true;
        }
        
        if (skillUsed) {
            player.getQuestManager().addProgress("skill", 1);
        }
    }
}
