package com.fishingrpg.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.screens.MainMenuScreen;

public class FishingRPG extends Game {

    public SpriteBatch batch;
    public Player player;
    public BitmapFont smallFont;
    public BitmapFont font;
    public BitmapFont titleFont;
    public BitmapFont bigFont;

    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player();
        com.fishingrpg.game.data.SaveManager.loadPlayer(player);

        // Nạp font hỗ trợ đầy đủ tiếng Việt và bộ ký tự/biểu tượng Unicode (★, ☆, ✓, ❖, ✦, ▲, ▼, ←, •,...)
        FreeTypeFontGenerator regularGen = new FreeTypeFontGenerator(Gdx.files.internal("data/dejavu.ttf"));
        FreeTypeFontGenerator boldGen = new FreeTypeFontGenerator(Gdx.files.internal("data/dejavu_bold.ttf"));

        String vietnameseChars = "áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđ" +
                                 "ÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬÉÈẺẼẸÊẾỀỂỄỆÍÌỈĨỊÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÚÙỦŨỤƯỨỪỬỮỰÝỲỶỸỴĐ";
        String symbolChars = "•★☆✓✔✕✖▲▼►◄◀▶←→↑↓❖✦✧–—…'‘’\"“”%‰$€₫°~+/*=()[]{}<>:;,!?._#@&|●◆■❝❞";
        String allChars = FreeTypeFontGenerator.DEFAULT_CHARS + vietnameseChars + symbolChars;

        // Cấu hình thông số tăng độ sắc nét tối đa, hạn chế mờ nhòe do khử răng cưa
        FreeTypeFontParameter regularParam = new FreeTypeFontParameter();
        regularParam.characters = allChars;
        regularParam.hinting = FreeTypeFontGenerator.Hinting.Full; // Căn chỉnh lưới pixel chuẩn xác
        regularParam.genMipMaps = true;                           // Sinh mipmap để khi scale nhỏ không bị nhòe
        regularParam.minFilter = Texture.TextureFilter.MipMapLinearNearest;
        regularParam.magFilter = Texture.TextureFilter.Linear;
        regularParam.gamma = 1.9f;
        regularParam.renderCount = 3;

        regularParam.size = 14;
        smallFont = regularGen.generateFont(regularParam);
        smallFont.setUseIntegerPositions(true);

        regularParam.size = 18;
        font = regularGen.generateFont(regularParam);
        font.setUseIntegerPositions(true);

        FreeTypeFontParameter boldParam = new FreeTypeFontParameter();
        boldParam.characters = allChars;
        boldParam.hinting = FreeTypeFontGenerator.Hinting.Full;
        boldParam.genMipMaps = true;
        boldParam.minFilter = Texture.TextureFilter.MipMapLinearNearest;
        boldParam.magFilter = Texture.TextureFilter.Linear;
        boldParam.gamma = 1.9f;
        boldParam.renderCount = 3;

        boldParam.size = 32;
        titleFont = boldGen.generateFont(boldParam);
        titleFont.setUseIntegerPositions(true);

        boldParam.size = 48;
        bigFont = boldGen.generateFont(boldParam);
        bigFont.setUseIntegerPositions(true);

        regularGen.dispose();
        boldGen.dispose();

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        com.fishingrpg.game.data.SaveManager.savePlayer(player);
        batch.dispose();
        if (smallFont != null) smallFont.dispose();
        if (font != null) font.dispose();
        if (titleFont != null) titleFont.dispose();
        if (bigFont != null) bigFont.dispose();
    }
}
