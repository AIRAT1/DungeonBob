package de.android.ayrathairullin.dungeonbob.managers;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.android.ayrathairullin.dungeonbob.GameData;

public class TextManager {
    static BitmapFont font ; // we draw the text to the screen using this variable
    // viewport width and height
    static float width,height;

    public static void initialize(float width,float height,BitmapFont font){
        TextManager.font = font;
        //font = new BitmapFont();
        TextManager.width = width;
        TextManager.height= height;
        //set the font color to red
        font.setColor(Color.RED);
        //scale the font size according to screen width
        font.getData().scale(width / 1000f);

    }

    public static void displayMessage(SpriteBatch batch){
        float fontWidth =  new GlyphLayout(font, "Score: " + GameData.score).width; // get the width of the text being displayed
        //top the score display at top right corner
        font.draw(batch, "Score: " + GameData.score, width - fontWidth - width/15f,height*0.98f);
        // show the number of lives at top left corner
        font.draw(batch, "Lives: " + GameData.lives, width*0.01f,height*0.98f);

    }
}
