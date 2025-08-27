package me.hextech.asm.mixins;

import me.hextech.remapped.Sprint;
import me.hextech.remapped.ViewModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LivingEntity.class})
public abstract class MixinLivingEntity
extends Entity {
    @Final
    @Shadow
    private static EntityAttributeModifier field_6231;

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    @Nullable
    public EntityAttributeInstance getAttributeInstance(EntityAttribute attribute) {
        return this.method_6127().method_26842(attribute);
    }

    @Shadow
    public AttributeContainer method_6127() {
        return null;
    }

    @Inject(method={"getHandSwingDuration"}, at={@At(value="HEAD")}, cancellable=true)
    private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
        if (ViewModel.INSTANCE.isOn() && ViewModel.INSTANCE.slowAnimation.getValue()) {
            info.setReturnValue((Object)ViewModel.INSTANCE.slowAnimationVal.getValueInt());
        }
    }

    @Inject(method={"setSprinting"}, at={@At(value="HEAD")}, cancellable=true)
    public void setSprintingHook(boolean sprinting, CallbackInfo ci) {
        if (this == MinecraftClient.getInstance().player && Sprint.INSTANCE.isOn() && Sprint.INSTANCE.mode.getValue() == Sprint._kIBjeDSbfTeuMDPgEQgD.Rage) {
            ci.cancel();
            sprinting = Sprint.shouldSprint;
            super.setSprinting(sprinting);
            EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.field_23719);
            entityAttributeInstance.method_6200(field_6231.method_6189());
            if (sprinting) {
                entityAttributeInstance.addTemporaryModifier(field_6231);
            }
        }
    }
}
