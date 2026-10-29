package io.github.goldengoose.spells;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.player.Player;

public class PentagramTrapSpell extends Spell{

    public PentagramTrapSpell(Player player) {
        super(player);
        texture = new Texture("pentagram_spell.png");
    }

    @Override
    protected void triggerEffect(Vector2 worldPos) {

    }
}
