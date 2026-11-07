package io.github.goldengoose.hidden_events;

import com.badlogic.gdx.math.Vector2;
import io.github.goldengoose.player.Player;
import io.github.goldengoose.world.Entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Set;


/**
 * This is a base class for every hidden event
 *
 * An event is an entity that owns a rectangular zone, calling GameWorld's interact(other) to check
 * if the player is standing inside the zone.
 *
 * HiddenEvent has 5 hooks - optional methods. So a concrete event should override the hooks it cares about:
 *  onEnter - something stepped into the zone
 *  onExit - something left the zone
 *  onStay - something is still in the zone (checked every frame)
 *  onUpdate - logic of its own e.g. timers, animation
 * All call trigger() the moment the event has counted as happened.
 *
 * The 5th hook is affects() which decided who is detected. The player by default. But should an event want to
 * react to geese for example (bread dispenser) then override it.
 *
 */

public abstract class HiddenEvent extends Entity {
    public interface Listener {
        void onTriggered(HiddenEvent event);
    }

    private final String name;
    private final String feedbackMessage; // shown on screen when triggered (QR_SOUND_ACCESS: never sound only)
    private final float width;
    private final float height;
    private final float cooldownSeconds;  // minimum time between triggers; 0 = no limit

    // Identity sets: two different entities are never "equal", whatever equals() says.
    private final Set<Entity> inside = Collections.newSetFromMap(new HashMap<>());
    private final Set<Entity> seenThisFrame = Collections.newSetFromMap(new HashMap<>());
    private final Vector2 otherCentre = new Vector2(); // scratch vector, avoids a new Vector2 per check

    private Listener listener;
    private float cooldownRemaining = 0f;
    private int timesTriggered = 0;
    private boolean revealed = false;


    protected HiddenEvent(String name, String feedbackMessage, float x, float y, float width, float height, float cooldownSeconds) {
        this.name = name;
        this.feedbackMessage = feedbackMessage;
        this.width = width;
        this.height = height;
        this.cooldownSeconds = cooldownSeconds;
        this.position.set(x, y);
    }


    // Hooks for subclasses
    // By default "entity" is the player - use affected() to change this.
    protected boolean affects(Entity other) {
        return other instanceof Player;
    }

    protected void onEnter(Entity entity) {
    }
    protected void onStay(Entity entity, float deltaTime) {
    }
    protected void onExit(Entity entity) {
    }
    protected void onUpdate(float deltaTime) {
    }

}
