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
    
    public boolean bossSkillJustUsed = false;
    private FloatingTextManager ftm;

    public FishingManager(Player player) {
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
        combatMessage = "";
        rewardDone = false;
        
        if (bonus >= 2f) {
            castResultText = "PERFECT!";
            player.getQuestManager().addProgress("perfect", 1);
            if (ftm != null) ftm.addText("PERFECT!", 640f, 500f, Color.GOLD);
            AudioManager.getInstance().playSFX("sfx_success");
        } else if (bonus > 0f) {
            castResultText = "GOOD";
        } else {
            castResultText = "NORMAL";
        }
        castResultTimer = 1.5f;
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
        currentCatch = LootTable.rollCatch(player);
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
    }

    public void finishEncounter(boolean success) {
        finishEncounter(success, "Thất bại!");
    }

    public void finishEncounter(boolean success, String failReason) {
        state = State.RESULT;
        if (!rewardDone) {
            rewardDone = true;
            lastEncounterSuccess = success;
            
            if (success) {
                player.recordSuccessfulCatch();
            }
            
            // Clear one-time buffs
            player.setRiskyBaitActive(false);
            player.setOverclockPotionActive(false);
            
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
                
                float goldMod = 1f;
                if (player.getGoldBuffCharges() > 0) {
                    player.consumeGoldBuffCharge();
                    goldMod = 1.5f;
                }
                
                if (player.isSetBonusActive() && player.getEquippedRod() != null && player.getEquippedRod().setName.equals("Hoàng Gia")) {
                    xpMod += 0.3f;
                    goldMod += 0.3f;
                }
                
                int xp = (int)(currentCatch.getXpValue() * xpMod);
                int gold = (int)(currentCatch.getGoldValue() * goldMod);
                
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
                    player.gainGold(gold * multiplier); // Multiply gold if multi-catch? Let's assume net gives multiple instances of the catch.
                    
                    if (currentCatch instanceof com.fishingrpg.game.entities.Fish) {
                        com.fishingrpg.game.entities.Fish f = (com.fishingrpg.game.entities.Fish) currentCatch;
                        com.fishingrpg.game.entities.Fish.Rarity rarity = f.getRarity();
                        int diamonds = 0;
                        if (rarity == com.fishingrpg.game.entities.Fish.Rarity.RARE) {
                            diamonds = 1;
                        } else if (rarity == com.fishingrpg.game.entities.Fish.Rarity.EPIC) {
                            diamonds = MathUtils.random(2, 3);
                        } else if (rarity == com.fishingrpg.game.entities.Fish.Rarity.BOSS) {
                            diamonds = MathUtils.random(5, 10);
                        }
                        
                        if (diamonds > 0) {
                            diamonds *= multiplier;
                            player.addDiamonds(diamonds);
                            combatMessage += " [+" + diamonds + " KC]";
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
                    
                    player.getQuestManager().addProgress("catch", multiplier);
                    if (f.getRarity() == com.fishingrpg.game.entities.Fish.Rarity.RARE || f.getRarity() == com.fishingrpg.game.entities.Fish.Rarity.EPIC || f.getRarity() == com.fishingrpg.game.entities.Fish.Rarity.BOSS) {
                        player.getQuestManager().addProgress("rare", multiplier);
                    }
                }

                combatMessage = "CÂU ĐƯỢC: " + currentCatch.getName() + extra + (lvUp > 0 ? " [TĂNG CẤP]" : "");
                resultLine = "+" + xp + " XP   +" + (gold * multiplier) + " Vàng";
            }
            
            float benBiBonus = player.isSkillEquipped("ben_bi") ? player.getSkillTree().getBenBiBonus() : 0f;
            float recoveredPercent = 0.10f + (benBiBonus / 100f);
            player.consumeStamina(-(player.getMaxStamina() * recoveredPercent));
        }
    }

    public void update(float delta, boolean actClicked, boolean isSpaceJust, boolean isSpaceDown, boolean[] activeSkillInputs, boolean actClose) {
        if (castResultTimer > 0) castResultTimer -= delta;
        
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
                    finishEncounter(false, "Cá đã chạy mất!");
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
                    AudioManager.getInstance().playSFX("sfx_skill");
                    if (s.id.equals("but_toc")) {
                        fishButTocTimer = 3f;
                        combatMessage = "CÁ BỨT TỐC! Lực kéo x2!";
                    } else if (s.id.equals("quay_bun")) {
                        fishQuayBunTimer = 4f;
                        combatMessage = "CÁ QUẪY BÙN! Không thấy thanh Tension!";
                    } else if (s.id.equals("lan_sau")) {
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
                        fishNhayVotTimer = 1f;
                        combatMessage = "CÁ NHẢY VỌT! Cẩn thận đứt dây!";
                        player.decreaseTension(player.getCurrentLineTension());
                    } else if (s.id.equals("song_am")) {
                        fishSongAmTimer = 2f;
                        combatMessage = "SÓNG ÂM! Tension nhiễu loạn!";
                    } else if (s.id.equals("phun_muc_mu")) {
                        fishPhunMucMuTimer = 4f;
                        combatMessage = "PHUN MỰC MÙ! Tối tăm mù mịt!";
                    } else if (s.id.equals("hut_mau")) {
                        fishHutMauTimer = 3f;
                        combatMessage = "HÚT MÁU! Cá đang hút thể lực của bạn!";
                    } else if (s.id.equals("bao_to")) {
                        fishBaoToTimer = 3f;
                        player.increaseTension(player.getMaxLineTension() * 0.4f);
                        combatMessage = "BÃO TỐ! Sấm sét giáng xuống!";
                    }
                }
            }
        }
        
        if (fishButTocTimer > 0) fishButTocTimer -= delta;
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
            if (fishNhayVotTimer <= 0) {
                player.increaseTension(player.getMaxLineTension() * 0.9f); // Rơi xuống tăng 90% tension
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
            currentFish.takeDamage(-currentFish.getMaxHp() * 0.05f * delta); // Hồi 5% mỗi giây * 3 = 15%
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
            float pull = currentFish.getCurrentPull() * 1.5f;
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
                tensionIncrease += 35f * delta; // Reel in
            }
            
            // Deal damage
            float dmgMult = 1f;
            if (phaGiapTimer > 0) dmgMult += phaGiapDebuff;
            float dmg = player.getBasePullDamage() * dmgMult * delta;
            if (fishCuongNoTimer > 0) {
                dmg = 0f;
            } else if (currentFish.getPassiveSkill("tron_truot") != null) {
                dmg *= 0.8f;
            }
            currentFish.takeDamage(dmg);
            
            slackTimer = 0f;
        } else {
            // Slack: không giữ dây → tension tự giảm
            player.decreaseTension(17.5f * delta);
            
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
    }

    public float giatKepCooldown = 0f;
    public float duongSucCooldown = 0f;
    public float duongSucChannelTimer = 0f;

    private void handleSkills(boolean[] activeInputs, boolean isSpaceDown, float delta) {
        for (int i = 0; i < 4; i++) {
            if (activeInputs[i]) {
                String skillId = player.getEquippedActives()[i];
                if (skillId != null && player.getSkillTree().getSkill(skillId) != null && player.getSkillTree().getSkill(skillId).isUnlocked()) {
                    float cdMult = 1f;
                    if (player.isSetBonusActive() && player.getEquippedRod() != null && player.getEquippedRod().setName.equals("Thời Không")) {
                        cdMult = 0.8f;
                    }
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

        if (skillId.equals("keo_manh") && keoManhCooldown <= 0) {
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
                if (ftm != null) ftm.addText("-" + (int)finalDmg, 640f + MathUtils.random(-50, 50), 360f + MathUtils.random(-50, 50), Color.RED);

                player.increaseTension(15f);
                combatMessage = crit ? "BẠO KÍCH! (-" + (int)cost + " Thể lực)" : "KÉO MẠNH! (-" + (int)cost + " Thể lực)";
                AudioManager.getInstance().playSFX("sfx_skill");
                keoManhCooldown = 3f * cdMult;
            }
        } else if (skillId.equals("giat_kep") && giatKepCooldown <= 0) {
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
                giatKepCooldown = 5f * cdMult;
            }
        } else if (skillId.equals("duong_suc") && duongSucCooldown <= 0 && duongSucChannelTimer <= 0) {
            duongSucChannelTimer = player.getSkillTree().getDuongSucChannelTime();
            duongSucCooldown = 10f * cdMult;
        } else if (skillId.equals("pha_giap") && phaGiapCooldown <= 0) {
            float hpDmg = player.getSkillTree().getPhaGiapDamage();
            float stamCost = 30f; // Tốn 30 thể lực
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
                phaGiapCooldown = 20f * cdMult;
            }
        } else if (skillId.equals("luot_day") && luotDayCooldown <= 0) {
            luotDayTimer = player.getSkillTree().getLuotDayDuration();
            combatMessage = "LƯỚT DÂY! (Làm yếu lực kéo của cá)";
            luotDayCooldown = 15f * cdMult;
        } else if (skillId.equals("loi_nguyen_troi_buoc") && troiBuocCooldown <= 0) {
            troiBuocTimer = player.getSkillTree().getLoiNguyenTroiBuocDuration();
            combatMessage = "TRÓI BUỘC! Cá không thể di chuyển!";
            troiBuocCooldown = 25f * cdMult;
        }
        
        player.getQuestManager().addProgress("skill", 1);
    }
}
