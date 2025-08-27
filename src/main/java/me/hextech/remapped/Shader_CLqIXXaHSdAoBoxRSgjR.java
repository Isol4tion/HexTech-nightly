package me.hextech.remapped;

import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class Shader_CLqIXXaHSdAoBoxRSgjR extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static Shader_CLqIXXaHSdAoBoxRSgjR INSTANCE;
   private final EnumSetting<Shader> page = this.add(new EnumSetting("Page", Shader.Shader));
   public final EnumSetting<ShaderManager_nSIALuQmpuiGKWaEurQW> mode = this.add(
      new EnumSetting("Mode", ShaderManager_nSIALuQmpuiGKWaEurQW.Solid, v -> this.page.getValue() == Shader.Shader)
   );
   public final EnumSetting<ShaderManager_nSIALuQmpuiGKWaEurQW> skyMode = this.add(
      new EnumSetting("SkyMode", ShaderManager_nSIALuQmpuiGKWaEurQW.Solid, v -> this.page.getValue() == Shader.Shader)
   );
   public final SliderSetting speed = this.add(new SliderSetting("Speed", 4.0, 0.0, 20.0, 0.1, v -> this.page.getValue() == Shader.Shader));
   public final ColorSetting fill = this.add(new ColorSetting("Color", new Color(255, 255, 255), v -> this.page.getValue() == Shader.Shader));
   public final SliderSetting maxSample = this.add(new SliderSetting("MaxSample", 10.0, 0.0, 20.0, v -> this.page.getValue() == Shader.Shader));
   public final SliderSetting divider = this.add(new SliderSetting("Divider", 150.0, 0.0, 300.0, v -> this.page.getValue() == Shader.Shader));
   public final SliderSetting radius = this.add(new SliderSetting("Radius", 2.0, 0.0, 6.0, v -> this.page.getValue() == Shader.Shader));
   public final SliderSetting smoothness = this.add(new SliderSetting("Smoothness", 1.0, 0.0, 1.0, 0.01, v -> this.page.getValue() == Shader.Shader));
   public final SliderSetting alpha = this.add(new SliderSetting("GlowAlpha", 255, 0, 255, v -> this.page.getValue() == Shader.Shader));
   public final BooleanSetting sky = this.add(new BooleanSetting("Sky", false, v -> this.page.getValue() == Shader.Target));
   public final SliderSetting maxRange = this.add(new SliderSetting("MaxRange", 64, 16, 512, v -> this.page.getValue() == Shader.Target));
   public final SliderSetting factor = this.add(new SliderSetting("GradientFactor", 2.0, 0.0, 20.0, v -> this.page.getValue() == Shader.Legacy));
   public final SliderSetting gradient = this.add(new SliderSetting("Gradient", 2.0, 0.0, 20.0, v -> this.page.getValue() == Shader.Legacy));
   public final SliderSetting octaves = this.add(new SliderSetting("Octaves", 10, 5, 30, v -> this.page.getValue() == Shader.Legacy));
   public final ColorSetting smoke1 = this.add(new ColorSetting("Smoke1", new Color(255, 255, 255), v -> this.page.getValue() == Shader.Legacy));
   public final ColorSetting smoke2 = this.add(new ColorSetting("Smoke2", new Color(255, 255, 255), v -> this.page.getValue() == Shader.Legacy));
   public final ColorSetting smoke3 = this.add(new ColorSetting("Smoke3", new Color(255, 255, 255), v -> this.page.getValue() == Shader.Legacy));
   private final BooleanSetting hands = this.add(new BooleanSetting("Hands", true, v -> this.page.getValue() == Shader.Target));
   private final BooleanSetting self = this.add(new BooleanSetting("Self", true, v -> this.page.getValue() == Shader.Target));
   private final BooleanSetting players = this.add(new BooleanSetting("Players", true, v -> this.page.getValue() == Shader.Target));
   private final BooleanSetting friends = this.add(new BooleanSetting("Friends", true, v -> this.page.getValue() == Shader.Target));
   private final BooleanSetting crystals = this.add(new BooleanSetting("Crystals", true, v -> this.page.getValue() == Shader.Target));
   private final BooleanSetting creatures = this.add(new BooleanSetting("Creatures", false, v -> this.page.getValue() == Shader.Target));
   private final BooleanSetting monsters = this.add(new BooleanSetting("Monsters", false, v -> this.page.getValue() == Shader.Target));
   private final BooleanSetting ambients = this.add(new BooleanSetting("Ambients", false, v -> this.page.getValue() == Shader.Target));
   private final BooleanSetting items = this.add(new BooleanSetting("Items", true, v -> this.page.getValue() == Shader.Target));
   private final BooleanSetting others = this.add(new BooleanSetting("Others", false, v -> this.page.getValue() == Shader.Target));

   public Shader_CLqIXXaHSdAoBoxRSgjR() {
      super("Shader", Module_JlagirAibYQgkHtbRnhw.Render);
      INSTANCE = this;
   }

   @Override
   public String getInfo() {
      return ((ShaderManager_nSIALuQmpuiGKWaEurQW)this.mode.getValue()).name();
   }

   public boolean shouldRender(Entity entity) {
      if (entity == null) {
         return false;
      } else if (mc.field_1724 == null) {
         return false;
      } else if ((double)MathHelper.method_15355((float)mc.field_1724.method_5707(entity.method_19538())) > this.maxRange.getValue()) {
         return false;
      } else if (entity instanceof PlayerEntity) {
         if (entity == mc.field_1724) {
            return this.self.getValue();
         } else {
            return me.hextech.HexTech.FRIEND.isFriend((PlayerEntity)entity) ? this.friends.getValue() : this.players.getValue();
         }
      } else if (entity instanceof EndCrystalEntity) {
         return this.crystals.getValue();
      } else if (entity instanceof ItemEntity) {
         return this.items.getValue();
      } else {
         return switch (entity.method_5864().method_5891()) {
            case field_6294, field_6300 -> this.creatures.getValue();
            case field_6302 -> this.monsters.getValue();
            case field_6303, field_24460 -> this.ambients.getValue();
            default -> this.others.getValue();
         };
      }
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      if (this.hands.getValue()) {
         me.hextech.HexTech.SHADER
            .renderShader(
               () -> mc.field_1773.method_3172(matrixStack, mc.field_1773.method_19418(), mc.method_1488()),
               (ShaderManager_nSIALuQmpuiGKWaEurQW)this.mode.getValue()
            );
      }
   }

   @Override
   public void onToggle() {
      me.hextech.HexTech.SHADER.reloadShaders();
   }

   @Override
   public void onLogin() {
      me.hextech.HexTech.SHADER.reloadShaders();
   }
}
