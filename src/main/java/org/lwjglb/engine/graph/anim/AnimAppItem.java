package org.lwjglb.engine.graph.anim;

import java.util.Map;
import java.util.Optional;

import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.items.AppItem;

public class AnimAppItem extends AppItem {

    private final Map<String, Animation> animations;

    private Animation currentAnimation;

    public AnimAppItem(Mesh[] meshes, Map<String, Animation> animations) {
        super(meshes);
        this.animations = animations;
        Optional<Map.Entry<String, Animation>> entry = animations.entrySet().stream().findFirst();
        currentAnimation = entry.isPresent() ? entry.get().getValue() : null;
    }

    public Animation getAnimation(String name) {
        return animations.get(name);
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }
}
