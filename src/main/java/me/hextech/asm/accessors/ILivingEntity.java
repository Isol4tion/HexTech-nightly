package me.hextech.asm.accessors;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={LivingEntity.class})
public interface ILivingEntity {
    @Accessor(value="lastAttackedTicks")
    int getLastAttackedTicks();

    @Accessor(value="jumpingCooldown")
    int getLastJumpCooldown();

    @Accessor(value="jumpingCooldown")
    void setLastJumpCooldown(int var1);
}
