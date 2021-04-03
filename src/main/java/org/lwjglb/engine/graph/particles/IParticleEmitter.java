package org.lwjglb.engine.graph.particles;

import java.util.List;
import org.lwjglb.engine.items.AppItem;

public interface IParticleEmitter {

    void cleanup();
    
    Particle getBaseParticle();
    
    List<AppItem> getParticles();
}
