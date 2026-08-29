package com.fishingrpg.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.fishingrpg.game.entities.Player;
import com.fishingrpg.game.screens.MainMenuScreen;

public class FishingRPG extends Game {

    public SpriteBatch batch;
    public Player player;
    public BitmapFont font;
    public BitmapFont titleFont;
    public BitmapFont bigFont;

    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player();
        com.fishingrpg.game.data.SaveManager.loadPlayer(player);
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/arial.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 18;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬÉÈẺẼẸÊẾỀỂỄỆÍÌỈĨỊÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÚÙỦŨỤƯỨỪỬỮỰÝỲỶỸỴĐ";
        font = generator.generateFont(parameter);
        
        parameter.size = 36;
        titleFont = generator.generateFont(parameter);
        
        parameter.size = 54;
        bigFont = generator.generateFont(parameter);
        
        generator.dispose();

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
        if (font != null) font.dispose();
        if (titleFont != null) titleFont.dispose();
        if (bigFont != null) bigFont.dispose();
    }
}
