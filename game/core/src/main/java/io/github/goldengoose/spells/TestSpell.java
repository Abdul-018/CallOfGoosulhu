package io.github.goldengoose.spells;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.player.Player;

public class TestSpell extends Spell{

    public TestSpell(Player player) {
        super(player);
        texture = new Texture("test_spell.png");
    }

    @Override
    protected void triggerEffect(Vector2 worldPos) {

    }
}
