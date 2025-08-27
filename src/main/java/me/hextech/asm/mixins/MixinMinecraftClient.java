package me.hextech.asm.mixins;

import me.hextech.HexTech;
import me.hextech.remapped.ClickGui_ABoiivByuLsVqarYqfYv;
import me.hextech.remapped.FontRenderers;
import me.hextech.remapped.MineTweak;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftClient.class})
public abstract class MixinMinecraftClient extends ReentrantThreadExecutor<Runnable> {
   @Shadow
   public int field_1771;
   @Shadow
   public ClientPlayerEntity field_1724;
   @Shadow
   public HitResult field_1765;
   @Shadow
   public ClientPlayerInteractionManager field_1761;
   @Final
   @Shadow
   public ParticleManager field_1713;
   @Shadow
   public ClientWorld field_1687;

   public MixinMinecraftClient(String string) {
      super(string);
   }

   @Inject(
      method = {"<init>"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/client/MinecraftClient;instance:Lnet/minecraft/client/MinecraftClient;"
      )}
   )
   private void onInstanceConstruct(RunArgs args, CallbackInfo ci) {
      System.setProperty("java.awt.headless", "false");
   }

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   void postWindowInit(RunArgs args, CallbackInfo ci) throws Throwable {
      HexTech.load();

      try {
         FontRenderers.Arial = FontRenderers.createArial(ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.fontsize.getValueFloat());
         FontRenderers.Calibri = FontRenderers.create("calibri", 1, ClickGui_ABoiivByuLsVqarYqfYv.INSTANCE.fontsize.getValueFloat());
         FontRenderers.ui = FontRenderers.create("tahoma", 0, 16.0F);
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   @Shadow
   public ClientPlayNetworkHandler method_1562() {
      return null;
   }

   @Shadow
   public ServerInfo method_1558() {
      return null;
   }

   @Inject(
      method = {"handleBlockBreaking"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void handleBlockBreaking(boolean breaking, CallbackInfo ci) {
      if (this.field_1771 <= 0 && this.field_1724.method_6115() && MineTweak.INSTANCE.multiTask()) {
         if (breaking && this.field_1765 != null && this.field_1765.method_17783() == Type.field_1332) {
            BlockHitResult blockHitResult = (BlockHitResult)this.field_1765;
            BlockPos blockPos = blockHitResult.method_17777();
            if (!this.field_1687.method_8320(blockPos).method_26215()) {
               Direction direction = blockHitResult.method_17780();
               if (this.field_1761.method_2902(blockPos, direction)) {
                  this.field_1713.method_3054(blockPos, direction);
                  this.field_1724.method_6104(Hand.field_5808);
               }
            }
         } else {
            this.field_1761.method_2925();
         }

         ci.cancel();
      }
   }

   @Inject(
      at = {@At("TAIL")},
      method = {"tick()V"}
   )
   public void tickTail(CallbackInfo info) {
      HexTech.SERVER.run();
      if (this.field_1687 != null) {
         HexTech.update();
      }

      HexTech.ROTATE.run();
   }

   @Overwrite
   private String method_24287() {
      return !HexTech.isLoaded ? "Ez Cracked By Isol4ion.(github.com/Isol4ion) 幻影盾拯救不了狂妄自大的你" : "Ez Cracked By Isol4ion.(github.com/Isol4ion)";
   }
}
