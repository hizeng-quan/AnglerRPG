package com.fishingrpg.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.entities.Quest;
import com.fishingrpg.game.entities.equipment.Equipment;
import com.fishingrpg.game.entities.equipment.EquipmentGenerator;
import com.fishingrpg.game.entities.equipment.EquipmentType;
import com.fishingrpg.game.entities.equipment.Substat;
import com.fishingrpg.game.skills.Skill;
import com.fishingrpg.game.systems.QuestManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SaveManager {

    private static final String PREF_NAME    = "AnglerRPG_Save";
    private static final String TAG          = "SaveManager";
    /** Increment when adding new fields that are backwards incompatible. */
    private static final int    SAVE_VERSION = 3;

    // =========================================================================
    // Save
    // =========================================================================

    public static void savePlayer(Player player) {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        prefs.putInteger("saveVersion",  SAVE_VERSION);
        prefs.putInteger("level",        player.getLevel());
        prefs.putInteger("currentXp",    player.getCurrentXp());
        prefs.putInteger("gold",         player.getGold());
        prefs.putInteger("diamonds",     player.getDiamonds());
        prefs.putInteger("gachaTickets", player.getGachaTickets());
        prefs.putInteger("premiumTickets", player.getPremiumTickets());
        prefs.putInteger("eventPoints",  player.getEventPoints());
        prefs.putInteger("totalLoginDaysThisMonth", player.getTotalLoginDaysThisMonth());
        prefs.putLong("lastLoginTimestamp", player.getLastLoginTimestamp());
        prefs.putLong("lastEventMissionResetTimestamp", player.getLastEventMissionResetTimestamp());
        prefs.putInteger("fishCaught",   player.getFishCaught());
        prefs.putInteger("skillPoints",  player.getSkillPoints());

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        if (player.getEventManager() != null) {
            prefs.putString("eventQuests", json.toJson(player.getEventManager().eventQuests));
            prefs.putBoolean("eventMilestoneClaimed", player.getEventManager().milestoneClaimedToday);
        }

        prefs.putString("inventory",         json.toJson(player.getInventory()));
        prefs.putString("claimedCollections",json.toJson(player.getClaimedCollections()));
        prefs.putString("claimedFishCatalog",json.toJson(player.getClaimedFishCatalog()));
        prefs.putString("equippedActives",   json.toJson(player.getEquippedActives()));
        prefs.putString("equippedPassives",  json.toJson(player.getEquippedPassives()));
        prefs.putString("currentMap",        player.getCurrentMap());

        // Skills
        Map<String, Integer> skillLevels = new HashMap<>();
        for (Skill s : player.getSkillTree().getAllSkills()) {
            skillLevels.put(s.id, s.getCurrentLevel());
        }
        prefs.putString("skills", json.toJson(skillLevels));

        // Quests
        if (player.getQuestManager() != null) {
            prefs.putString("questManager", json.toJson(player.getQuestManager()));
        }

        // Equipment
        prefs.putString("equipmentInventory", json.toJson(player.getEquipmentInventory()));
        prefs.putString("equippedRod",  json.toJson(player.getEquippedRod(),  Equipment.class));
        prefs.putString("equippedLine", json.toJson(player.getEquippedLine(), Equipment.class));
        prefs.putString("equippedHook", json.toJson(player.getEquippedHook(), Equipment.class));
        prefs.putInteger("pityRare",      player.pityRare);
        prefs.putInteger("pityEpic",      player.pityEpic);
        prefs.putInteger("pityLegendary", player.pityLegendary);
        prefs.putInteger("pityLimited",   player.pityLimited);
        
        // Buffs
        prefs.putString("playerBuffs", json.toJson(player.getBuffs()));

        prefs.flush();
    }

    // =========================================================================
    // Clear / Reset Save
    // =========================================================================

    public static boolean hasSaveData() {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        return prefs.contains("level");
    }

    public static void clearSave() {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        prefs.clear();
        prefs.flush();
        Preferences recs = Gdx.app.getPreferences("AnglerRPG_Records");
        recs.clear();
        recs.flush();
        SizeRecord.clearCache();
        Gdx.app.log(TAG, "All player save data and records have been cleared.");
    }

    // =========================================================================
    // Encryption & Anti-Tamper Security
    // =========================================================================

    public static final String ENCRYPTION_HEADER = "ANGLER_SECURE_SAVE_v1:";
    private static final String SECRET_SALT = "AnglerRPG_MasterSecretKey_2026#TamperProof!@X9";

    /**
     * Mã hóa chuỗi JSON dữ liệu bằng AES/CBC/PKCS5Padding kết hợp mã kiểm tra băm SHA-256
     * để chống gian lận và ngăn chặn người chơi can thiệp chỉnh sửa file save thủ công.
     */
    public static String encryptData(String plainText) {
        if (plainText == null || plainText.isEmpty()) return plainText;
        try {
            byte[] plainBytes = plainText.getBytes(java.nio.charset.StandardCharsets.UTF_8);

            // 1. Tính toán SHA-256 Checksum của dữ liệu gốc để kiểm tra tính toàn vẹn
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] checksum = md.digest(plainBytes);

            // 2. Tạo khóa AES-256 bí mật từ SECRET_SALT
            byte[] keyBytes = md.digest(SECRET_SALT.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");

            // 3. Khởi tạo vector ngẫu nhiên IV 16-byte cho chế độ mã hóa CBC
            byte[] iv = new byte[16];
            new java.security.SecureRandom().nextBytes(iv);
            javax.crypto.spec.IvParameterSpec ivSpec = new javax.crypto.spec.IvParameterSpec(iv);

            // 4. Mã hóa AES
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] cipherBytes = cipher.doFinal(plainBytes);

            // 5. Đóng gói payload: [IV (16 bytes)] + [Checksum (32 bytes)] + [Ciphertext]
            byte[] combined = new byte[16 + 32 + cipherBytes.length];
            System.arraycopy(iv, 0, combined, 0, 16);
            System.arraycopy(checksum, 0, combined, 16, 32);
            System.arraycopy(cipherBytes, 0, combined, 48, cipherBytes.length);

            // 6. Mã hóa Base64 và đính kèm Header nhận dạng
            String b64 = java.util.Base64.getEncoder().encodeToString(combined);
            return ENCRYPTION_HEADER + b64;
        } catch (Exception e) {
            logError("Lỗi mã hóa dữ liệu save", e);
            return plainText;
        }
    }

    private static void logError(String msg, Throwable t) {
        try {
            if (Gdx.app != null) {
                if (t != null) Gdx.app.error(TAG, msg, t);
                else Gdx.app.error(TAG, msg);
                return;
            }
        } catch (Throwable ignored) {}
        System.err.println("[" + TAG + "] " + msg);
        if (t != null) t.printStackTrace();
    }

    /**
     * Giải mã dữ liệu save đã được mã hóa. Nếu dữ liệu bị chỉnh sửa (sai checksum hoặc lỗi giải mã),
     * phương thức sẽ trả về null và ghi cảnh báo phát hiện can thiệp (Tamper Detection).
     */
    public static String decryptData(String input) {
        if (input == null) return null;
        String trimmed = input.trim();
        if (!trimmed.startsWith(ENCRYPTION_HEADER)) {
            // Định dạng JSON hoặc XML cũ (hỗ trợ tương thích ngược)
            return trimmed;
        }

        try {
            String b64 = trimmed.substring(ENCRYPTION_HEADER.length()).trim();
            byte[] combined = java.util.Base64.getDecoder().decode(b64);
            if (combined.length < 48 + 16) {
                logError("Payload lưu trữ không hợp lệ: Kích thước quá ngắn!", null);
                return null;
            }

            byte[] iv = new byte[16];
            byte[] expectedChecksum = new byte[32];
            byte[] cipherBytes = new byte[combined.length - 48];

            System.arraycopy(combined, 0, iv, 0, 16);
            System.arraycopy(combined, 16, expectedChecksum, 0, 32);
            System.arraycopy(combined, 48, cipherBytes, 0, cipherBytes.length);

            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = md.digest(SECRET_SALT.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
            javax.crypto.spec.IvParameterSpec ivSpec = new javax.crypto.spec.IvParameterSpec(iv);

            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(cipherBytes);

            // Kiểm tra Checksum SHA-256 phát hiện can thiệp chỉnh sửa
            byte[] actualChecksum = md.digest(decryptedBytes);
            if (!java.security.MessageDigest.isEqual(expectedChecksum, actualChecksum)) {
                logError("CẢNH BÁO: Phát hiện can thiệp! Checksum file save không trùng khớp.", null);
                return null;
            }

            return new String(decryptedBytes, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            logError("Không thể giải mã dữ liệu save (File bị hỏng hoặc đã bị chỉnh sửa trái phép)", e);
            return null;
        }
    }

    // =========================================================================
    // Export / Import Save Data
    // =========================================================================

    public static String exportPlainDataToString() {
        Preferences savePrefs = Gdx.app.getPreferences(PREF_NAME);
        Preferences recsPrefs = Gdx.app.getPreferences("AnglerRPG_Records");

        java.io.StringWriter sw = new java.io.StringWriter();
        com.badlogic.gdx.utils.JsonWriter jw = new com.badlogic.gdx.utils.JsonWriter(sw);
        jw.setOutputType(com.badlogic.gdx.utils.JsonWriter.OutputType.json);
        try {
            jw.object();
            jw.set("game", "AnglerRPG");
            jw.set("saveVersion", SAVE_VERSION);
            jw.set("exportTime", java.time.LocalDateTime.now().toString());

            jw.object("playerData");
            for (Map.Entry<String, ?> e : savePrefs.get().entrySet()) {
                if (e.getValue() != null) {
                    jw.set(e.getKey(), e.getValue().toString());
                }
            }
            jw.pop();

            jw.object("recordData");
            for (Map.Entry<String, ?> e : recsPrefs.get().entrySet()) {
                if (e.getValue() != null) {
                    jw.set(e.getKey(), e.getValue().toString());
                }
            }
            jw.pop();

            jw.pop();
            jw.close();
            return sw.toString();
        } catch (java.io.IOException e) {
            Gdx.app.error(TAG, "Error generating export JSON", e);
            return "{}";
        }
    }

    public static String exportDataToString() {
        String plainJson = exportPlainDataToString();
        return encryptData(plainJson);
    }

    public static java.io.File exportToFile(java.io.File targetFile) throws java.io.IOException {
        if (targetFile == null) {
            targetFile = new java.io.File("angler_save.sav");
        }
        String encryptedData = exportDataToString();
        java.nio.file.Files.writeString(targetFile.toPath(), encryptedData, java.nio.charset.StandardCharsets.UTF_8);
        return targetFile;
    }

    public static boolean importDataFromString(String inputStr, Player player) {
        if (inputStr == null || inputStr.trim().isEmpty()) return false;
        String jsonStr = decryptData(inputStr);
        if (jsonStr == null) {
            Gdx.app.error(TAG, "Dữ liệu lưu trữ không thể nạp: File đã bị can thiệp hoặc sai định dạng!");
            return false;
        }
        try {
            com.badlogic.gdx.utils.JsonReader reader = new com.badlogic.gdx.utils.JsonReader();
            com.badlogic.gdx.utils.JsonValue root = reader.parse(jsonStr);
            if (root == null || !root.has("playerData")) {
                Gdx.app.error(TAG, "JSON missing playerData root");
                return false;
            }

            com.badlogic.gdx.utils.JsonValue playerData = root.get("playerData");
            Preferences savePrefs = Gdx.app.getPreferences(PREF_NAME);
            savePrefs.clear();
            for (com.badlogic.gdx.utils.JsonValue entry = playerData.child; entry != null; entry = entry.next) {
                String valStr = (entry.isObject() && entry.has("value")) ? entry.getString("value") : entry.asString();
                savePrefs.putString(entry.name, valStr);
            }
            savePrefs.flush();

            if (root.has("recordData")) {
                com.badlogic.gdx.utils.JsonValue recordData = root.get("recordData");
                Preferences recPrefs = Gdx.app.getPreferences("AnglerRPG_Records");
                recPrefs.clear();
                for (com.badlogic.gdx.utils.JsonValue entry = recordData.child; entry != null; entry = entry.next) {
                    String valStr = (entry.isObject() && entry.has("value")) ? entry.getString("value") : entry.asString();
                    recPrefs.putString(entry.name, valStr);
                }
                recPrefs.flush();
            }
            SizeRecord.clearCache();

            if (player != null) {
                loadPlayer(player);
            }
            Gdx.app.log(TAG, "Save data imported successfully!");
            return true;
        } catch (Exception e) {
            Gdx.app.error(TAG, "Failed to import save data", e);
            return false;
        }
    }

    public static boolean importFromFile(java.io.File file, Player player) {
        if (file == null || !file.exists()) return false;
        try {
            String content = java.nio.file.Files.readString(file.toPath(), java.nio.charset.StandardCharsets.UTF_8);
            if (content.trim().startsWith("<?xml") || file.getName().endsWith(".bak")) {
                return importFromXmlFile(file, player);
            }
            return importDataFromString(content, player);
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error reading import file: " + file.getPath(), e);
            return false;
        }
    }

    public static boolean importFromXmlFile(java.io.File file, Player player) {
        try {
            java.util.Properties props = new java.util.Properties();
            try (java.io.FileInputStream in = new java.io.FileInputStream(file)) {
                props.loadFromXML(in);
            }
            Preferences savePrefs = Gdx.app.getPreferences(PREF_NAME);
            savePrefs.clear();
            for (String key : props.stringPropertyNames()) {
                savePrefs.putString(key, props.getProperty(key));
            }
            savePrefs.flush();

            // Tự động tìm nạp kèm file kỷ lục AnglerRPG_Records.bak trong cùng thư mục
            java.io.File recFile = new java.io.File(file.getParentFile(), "AnglerRPG_Records.bak");
            if (!recFile.exists()) {
                recFile = new java.io.File(file.getParentFile(), "AnglerRPG_Records");
            }
            if (recFile.exists()) {
                java.util.Properties recProps = new java.util.Properties();
                try (java.io.FileInputStream in = new java.io.FileInputStream(recFile)) {
                    recProps.loadFromXML(in);
                }
                Preferences recPrefs = Gdx.app.getPreferences("AnglerRPG_Records");
                recPrefs.clear();
                for (String key : recProps.stringPropertyNames()) {
                    recPrefs.putString(key, recProps.getProperty(key));
                }
                recPrefs.flush();
            }
            SizeRecord.clearCache();

            if (player != null) {
                loadPlayer(player);
            }
            Gdx.app.log(TAG, "Loaded .bak XML save file successfully!");
            return true;
        } catch (Exception e) {
            Gdx.app.error(TAG, "Failed to import from XML .bak", e);
            return false;
        }
    }

    // =========================================================================
    // Load
    // =========================================================================

    @SuppressWarnings("unchecked")
    public static void loadPlayer(Player player) {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        if (!prefs.contains("level")) return; // No save data

        // Check save version
        int savedVersion = prefs.getInteger("saveVersion", 1);
        if (savedVersion < SAVE_VERSION) {
            Gdx.app.log(TAG, "Old save version (" + savedVersion + " < " + SAVE_VERSION
                    + "). Some new fields will use default values.");
        }

        // Primitive fields — safe, no try-catch needed
        player.setLevel(prefs.getInteger("level"));
        player.setCurrentXp(prefs.getInteger("currentXp"));
        player.setGold(prefs.getInteger("gold"));
        player.setDiamonds(prefs.getInteger("diamonds", 0));
        player.setGachaTickets(prefs.getInteger("gachaTickets", 0));
        player.setPremiumTickets(prefs.getInteger("premiumTickets", 0));
        player.setEventPoints(prefs.getInteger("eventPoints", 0));
        player.setTotalLoginDaysThisMonth(prefs.getInteger("totalLoginDaysThisMonth", 0));
        player.setLastLoginTimestamp(prefs.getLong("lastLoginTimestamp", 0L));
        player.setLastEventMissionResetTimestamp(prefs.getLong("lastEventMissionResetTimestamp", 0L));

        player.setFishCaught(prefs.getInteger("fishCaught"));
        player.setCurrentMap(prefs.getString("currentMap", "ao_lang"));
        player.setSkillPoints(prefs.getInteger("skillPoints", 0));

        Json json = new Json();

        // --- Inventory ---
        try {
            Map<String, Integer> inv = json.fromJson(HashMap.class, prefs.getString("inventory", "{}"));
            if (inv != null) {
                player.getInventory().clear();
                for (Map.Entry<String, Integer> e : inv.entrySet()) {
                    if (e.getValue() > 0) player.addItem(e.getKey(), e.getValue());
                }
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing inventory — keeping defaults.", e);
        }

        // --- Skills ---
        try {
            Map<String, Integer> skillLevels = json.fromJson(HashMap.class, prefs.getString("skills", "{}"));
            if (skillLevels != null) {
                for (Map.Entry<String, Integer> e : skillLevels.entrySet()) {
                    Skill s = player.getSkillTree().getSkill(e.getKey());
                    if (s != null) s.setCurrentLevel(e.getValue());
                }
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing skills — keeping defaults.", e);
        }

        // --- Equipped skill slots ---
        try {
            String[] ea = json.fromJson(String[].class, prefs.getString("equippedActives", "[]"));
            if (ea != null) {
                for (int i = 0; i < Math.min(ea.length, player.getEquippedActives().length); i++) {
                    player.getEquippedActives()[i] = ea[i];
                }
            }
            String[] ep = json.fromJson(String[].class, prefs.getString("equippedPassives", "[]"));
            if (ep != null) {
                for (int i = 0; i < Math.min(ep.length, player.getEquippedPassives().length); i++) {
                    player.getEquippedPassives()[i] = ep[i];
                }
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing equipped slots — keeping defaults.", e);
        }
        player.ensureDefaultSkills();

        // --- Claimed collections ---
        try {
            HashSet claimed = json.fromJson(HashSet.class, prefs.getString("claimedCollections", "[]"));
            if (claimed != null) player.setClaimedCollections(claimed);
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing claimedCollections.", e);
        }

        // --- Claimed fish catalog entries ---
        try {
            HashSet claimedFish = json.fromJson(HashSet.class, prefs.getString("claimedFishCatalog", "[]"));
            if (claimedFish != null) player.setClaimedFishCatalog(claimedFish);
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing claimedFishCatalog.", e);
        }

        // --- Quest Manager ---
        try {
            String questJson = prefs.getString("questManager", null);
            if (questJson != null && !questJson.isEmpty()) {
                QuestManager qm = json.fromJson(QuestManager.class, questJson);
                if (qm != null) {
                    qm.initIfEmpty();
                    player.setQuestManager(qm);
                }
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing questManager — resetting to defaults.", e);
        }
        if (player.getQuestManager() == null) {
            player.setQuestManager(new QuestManager());
            player.getQuestManager().initIfEmpty();
        }
        player.getQuestManager().setPlayer(player);
        player.getQuestManager().checkResets();

        // --- Event Manager Quests ---
        if (player.getEventManager() != null) {
            try {
                String eqJson = prefs.getString("eventQuests", null);
                if (eqJson != null && !eqJson.isEmpty()) {
                    ArrayList<Quest> loaded = json.fromJson(ArrayList.class, Quest.class, eqJson);
                    if (loaded != null && !loaded.isEmpty()) {
                        player.getEventManager().eventQuests.clear();
                        player.getEventManager().eventQuests.addAll(loaded);
                    }
                }
            } catch (Exception e) {
                Gdx.app.error(TAG, "Error parsing eventQuests.", e);
            }
            player.getEventManager().milestoneClaimedToday = prefs.getBoolean("eventMilestoneClaimed", false);
            player.getEventManager().checkAndResetDailyMissions();
        }

        // --- Equipment ---
        try {
            String eqInvJson = prefs.getString("equipmentInventory", "[]");
            if (!eqInvJson.equals("[]")) {
                ArrayList<Equipment> eqInv = json.fromJson(ArrayList.class, Equipment.class, eqInvJson);
                if (eqInv != null) {
                    player.getEquipmentInventory().clear();
                    for (Equipment eq : eqInv) {
                        if (eq != null && ("Thần Khí Lạc Hồng".equals(eq.setName) || "Lạc Hồng".equals(eq.setName))) {
                            eq.rarity = com.fishingrpg.game.entities.equipment.Rarity.LIMITED_PREMIUM;
                        }
                        player.getEquipmentInventory().add(eq);
                    }
                }
            }

            // Re-apply new formula only for legacy save versions (< 3)
            if (savedVersion < 3) {
                for (Equipment eq : player.getEquipmentInventory()) {
                    int mult = EquipmentGenerator.getSetPoints(eq.setName)
                             + EquipmentGenerator.getRarityPoints(eq.rarity);
                    if (eq.type == EquipmentType.ROD) {
                        eq.mainStat1 = 10f + mult * 2f;
                        eq.mainStat2 = 50f + mult * 15f;
                    } else if (eq.type == EquipmentType.LINE) {
                        eq.mainStat1 = 100f + mult * 25f;
                        eq.mainStat2 = 2f  + mult * 0.4f;
                    } else if (eq.type == EquipmentType.HOOK) {
                        eq.mainStat1 = 5f  + mult * 1.0f;
                        eq.mainStat2 = 10f + mult * 2.0f;
                    }
                    for (Substat sub : eq.substats) {
                        sub.value = Math.max(1f, Math.round(2f + mult / 2f));
                    }
                }
            }

            // Re-equip exact instance from inventory
            Equipment rod  = json.fromJson(Equipment.class, prefs.getString("equippedRod",  "null"));
            Equipment line = json.fromJson(Equipment.class, prefs.getString("equippedLine", "null"));
            Equipment hook = json.fromJson(Equipment.class, prefs.getString("equippedHook", "null"));
            for (Equipment eq : player.getEquipmentInventory()) {
                if (rod  != null && eq.instanceId.equals(rod.instanceId))  player.equip(eq);
                if (line != null && eq.instanceId.equals(line.instanceId)) player.equip(eq);
                if (hook != null && eq.instanceId.equals(hook.instanceId)) player.equip(eq);
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing equipment — keeping empty.", e);
        }

        player.pityRare      = prefs.getInteger("pityRare", 0);
        player.pityEpic      = prefs.getInteger("pityEpic", 0);
        player.pityLegendary = prefs.getInteger("pityLegendary", 0);
        player.pityLimited   = prefs.getInteger("pityLimited", 0);

        // --- Buffs ---
        try {
            String buffsJson = prefs.getString("playerBuffs", null);
            if (buffsJson != null && !buffsJson.isEmpty()) {
                com.fishingrpg.game.entities.PlayerBuffs savedBuffs = json.fromJson(com.fishingrpg.game.entities.PlayerBuffs.class, buffsJson);
                if (savedBuffs != null) {
                    com.fishingrpg.game.entities.PlayerBuffs buffs = player.getBuffs();
                    buffs.setBaitBuffActive(savedBuffs.isBaitBuffActive());
                    buffs.setGoldBuffCharges(savedBuffs.getGoldBuffCharges());
                    buffs.setExpBuffCharges(savedBuffs.getExpBuffCharges());
                    buffs.setFishingNetActive(savedBuffs.isFishingNetActive());
                    buffs.setRiskyBaitActive(savedBuffs.isRiskyBaitActive());
                    buffs.setOverclockPotionActive(savedBuffs.isOverclockPotionActive());
                }
            }
        } catch (Exception e) {
            Gdx.app.error(TAG, "Error parsing playerBuffs — keeping defaults.", e);
        }

        player.applySkillBonuses();
    }
}
