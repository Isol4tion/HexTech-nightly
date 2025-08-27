package me.hextech.asm.mixins;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.Random;
import me.hextech.HexTech;
import me.hextech.remapped.BaseThreadSetting_TYdViPaJQVoRZLdgWIXF;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.Event;
import me.hextech.remapped.ForceSync;
import me.hextech.remapped.MoveEvent;
import me.hextech.remapped.NoSlow_PaVUKKxFbWGbplzMaucl;
import me.hextech.remapped.PacketControl;
import me.hextech.remapped.PortalGui;
import me.hextech.remapped.RotateEvent;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.UpdateWalkingEvent;
import me.hextech.remapped.Velocity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.ClientPlayerTickable;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayerEntity.class})
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
   @Shadow
   public Input field_3913;
   @Final
   @Shadow
   public ClientPlayNetworkHandler field_3944;
   @Final
   @Shadow
   protected MinecraftClient field_3937;
   @Shadow
   @Final
   private List<ClientPlayerTickable> field_3933;
   @Shadow
   private boolean field_3927;
   @Shadow
   private double field_3926;
   @Shadow
   private double field_3940;
   @Shadow
   private double field_3924;
   @Shadow
   private float field_3941;
   @Shadow
   private float field_3925;
   @Shadow
   private boolean field_3920;
   @Shadow
   private boolean field_3936;
   @Shadow
   private int field_3923;

   public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
      super(world, profile);
   }

   @Inject(
      method = {"pushOutOfBlocks"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPushOutOfBlocksHook(double x, double d, CallbackInfo info) {
      if (Velocity.INSTANCE.isOn() && Velocity.INSTANCE.blockPush.getValue()) {
         info.cancel();
      }
   }

   @Redirect(
      method = {"tickMovement"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
      ),
      require = 0
   )
   private boolean tickMovementHook(ClientPlayerEntity player) {
      return NoSlow_PaVUKKxFbWGbplzMaucl.INSTANCE.isOn() ? false : player.method_6115();
   }

   @Redirect(
      method = {"updateNausea"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;"
      )
   )
   private Screen updateNauseaGetCurrentScreenProxy(MinecraftClient client) {
      return PortalGui.INSTANCE.isOn() ? null : client.field_1755;
   }

   @Inject(
      method = {"move"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"
      )},
      cancellable = true
   )
   public void onMoveHook(MovementType movementType, Vec3d movement, CallbackInfo ci) {
      if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.movehook.getValue()) {
         MoveEvent event = new MoveEvent(movement.field_1352, movement.field_1351, movement.field_1350);
         HexTech.EVENT_BUS.post(event);
         ci.cancel();
         if (!event.isCancelled()) {
            super.method_5784(movementType, new Vec3d(event.getX(), event.getY(), event.getZ()));
         }
      }
   }

   @Shadow
   private void method_46742() {
   }

   @Shadow
   private void method_3136() {
   }

   @Shadow
   protected boolean method_3134() {
      return false;
   }

   @Shadow
   public abstract float method_5695(float var1);

   @Inject(
      method = {"sendMovementPackets"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void sendMovementPacketsHook(CallbackInfo ci) {
      ci.cancel();
      if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.packethook.getValue()) {
         try {
            UpdateWalkingEvent updateEvent = new UpdateWalkingEvent(Event.Pre);
            RotateManager.lastEvent = updateEvent;
            HexTech.EVENT_BUS.post(updateEvent);
            this.method_46742();
            boolean bl = this.method_5715();
            if (bl != this.field_3936) {
               Mode mode = bl ? Mode.field_12979 : Mode.field_12984;
               this.field_3944.method_52787(new ClientCommandC2SPacket(this, mode));
               this.field_3936 = bl;
            }

            if (this.method_3134()) {
               double d = this.method_23317() - this.field_3926;
               double e = this.method_23318() - this.field_3940;
               double f = this.method_23321() - this.field_3924;
               float yaw = this.method_36454();
               float pitch = this.method_36455();
               RotateEvent rotateEvent = new RotateEvent(yaw, pitch);
               HexTech.EVENT_BUS.post(rotateEvent);
               if (rotateEvent.isModified()
                  && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.random.getValue()
                  && new Random().nextBoolean()
                  && new Random().nextBoolean()) {
                  rotateEvent.setPitch(Math.min(new Random().nextFloat() * 2.0F + rotateEvent.getPitch(), 90.0F));
               }

               yaw = rotateEvent.getYaw();
               pitch = rotateEvent.getPitch();
               HexTech.ROTATE.rotateYaw = yaw;
               HexTech.ROTATE.rotatePitch = pitch;
               double g = (double)(yaw - HexTech.ROTATE.lastYaw);
               double h = (double)(pitch - RotateManager.lastPitch);
               this.field_3923++;
               boolean bl2 = MathHelper.method_41190(d, e, f) > MathHelper.method_33723(2.0E-4)
                  || this.field_3923 >= 20
                  || ForceSync.INSTANCE.isOn() && ForceSync.INSTANCE.position.getValue();
               boolean bl3 = g != 0.0 || h != 0.0 || ForceSync.INSTANCE.isOn() && ForceSync.INSTANCE.rotate.getValue();
               if (PacketControl.INSTANCE.isOn()) {
                  bl3 = PacketControl.INSTANCE.full;
               }

               if (this.method_5765()) {
                  Vec3d vec3d = this.method_18798();
                  this.field_3944.method_52787(new Full(vec3d.field_1352, -999.0, vec3d.field_1350, yaw, pitch, this.method_24828()));
                  bl2 = false;
               } else if (bl2 && bl3) {
                  this.field_3944.method_52787(new Full(this.method_23317(), this.method_23318(), this.method_23321(), yaw, pitch, this.method_24828()));
               } else if (bl2) {
                  this.field_3944.method_52787(new PositionAndOnGround(this.method_23317(), this.method_23318(), this.method_23321(), this.method_24828()));
               } else if (bl3) {
                  this.field_3944.method_52787(new LookAndOnGround(yaw, pitch, this.method_24828()));
               } else if (this.field_3920 != this.method_24828()) {
                  this.field_3944.method_52787(new OnGroundOnly(this.method_24828()));
               }

               if (bl2) {
                  this.field_3926 = this.method_23317();
                  this.field_3940 = this.method_23318();
                  this.field_3924 = this.method_23321();
                  this.field_3923 = 0;
               }

               if (bl3) {
                  this.field_3941 = yaw;
                  this.field_3925 = pitch;
               }

               this.field_3920 = this.method_24828();
               this.field_3927 = (Boolean)this.field_3937.field_1690.method_42423().method_41753();
            }

            HexTech.EVENT_BUS.post(new UpdateWalkingEvent(Event.Post));
         } catch (Exception var21) {
            var21.printStackTrace();
         }
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void tickHook(CallbackInfo ci) {
      ci.cancel();
      if (this.method_37908().method_33598(this.method_31477(), this.method_31479())) {
         super.method_5773();
         if (this.method_5765()) {
            UpdateWalkingEvent pre = new UpdateWalkingEvent(Event.Pre);
            RotateManager.lastEvent = pre;
            HexTech.EVENT_BUS.post(pre);
            if (!pre.isCancelRotate()) {
               float yaw = this.method_36454();
               float pitch = this.method_36455();
               RotateEvent rot = new RotateEvent(yaw, pitch);
               HexTech.EVENT_BUS.post(rot);
               if (rot.isModified()
                  && CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.random.getValue()
                  && new Random().nextBoolean()
                  && new Random().nextBoolean()) {
                  rot.setPitch(Math.min(new Random().nextFloat() * 2.0F + rot.getPitch(), 90.0F));
               }

               yaw = rot.getYaw();
               pitch = rot.getPitch();
               HexTech.ROTATE.rotateYaw = yaw;
               HexTech.ROTATE.rotatePitch = pitch;
               this.field_3944.method_52787(new LookAndOnGround(yaw, pitch, this.method_24828()));
            }

            HexTech.EVENT_BUS.post(new UpdateWalkingEvent(Event.Post));
            this.field_3944.method_52787(new PlayerInputC2SPacket(this.field_6212, this.field_6250, this.field_3913.field_3904, this.field_3913.field_3903));
            Entity root = this.method_5668();
            if (root != this && root.method_5787()) {
               this.field_3944.method_52787(new VehicleMoveC2SPacket(root));
               this.method_46742();
            }
         } else {
            this.method_3136();
         }

         for (ClientPlayerTickable tickable : this.field_3933) {
            tickable.method_4756();
         }
      }
   }
}
