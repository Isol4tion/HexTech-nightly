package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.client.particle.Particle;
import net.minecraft.particle.ParticleEffect;

public class ParticleEvent_KqEBTPXRWHlYeOSnrLnf
extends Event_auduwKaxKOWXRtyJkCPb {
    public ParticleEvent_KqEBTPXRWHlYeOSnrLnf() {
        super(Stage.Pre);
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public static class AddEmmiter
    extends ParticleEvent_KqEBTPXRWHlYeOSnrLnf {
        public final ParticleEffect emmiter;

        public AddEmmiter(ParticleEffect emmiter) {
            this.emmiter = emmiter;
        }
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public static class AddParticle
    extends ParticleEvent_KqEBTPXRWHlYeOSnrLnf {
        public final Particle particle;

        public AddParticle(Particle particle) {
            this.particle = particle;
        }
    }
}
