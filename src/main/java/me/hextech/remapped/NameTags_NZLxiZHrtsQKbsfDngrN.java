package me.hextech.remapped;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4d;

public class NameTags_NZLxiZHrtsQKbsfDngrN extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static NameTags_NZLxiZHrtsQKbsfDngrN INSTANCE;
   public final EnumSetting<NameTags_VRQxrjlOGbJxMNTCEBWa> font = this.add(new EnumSetting("FontMode", NameTags_VRQxrjlOGbJxMNTCEBWa.Fast));
   private final SliderSetting scale = this.add(new SliderSetting("Scale", 0.68F, 0.1F, 2.0, 0.01));
   private final SliderSetting minScale = this.add(new SliderSetting("MinScale", 0.2F, 0.1F, 1.0, 0.01));
   private final SliderSetting scaled = this.add(new SliderSetting("Scaled", 1.0, 0.0, 2.0, 0.01));
   private final SliderSetting offset = this.add(new SliderSetting("Offset", 0.315F, 0.001F, 1.0, 0.001));
   private final SliderSetting height = this.add(new SliderSetting("Height", 0.0, -3.0, 3.0, 0.01));
   private final BooleanSetting gamemode = this.add(new BooleanSetting("Gamemode", false));
   private final BooleanSetting ping = this.add(new BooleanSetting("Ping", false));
   private final BooleanSetting health = this.add(new BooleanSetting("Health", true));
   private final BooleanSetting distance = this.add(new BooleanSetting("Distance", true));
   private final BooleanSetting pops = this.add(new BooleanSetting("TotemPops", true));
   private final BooleanSetting enchants = this.add(new BooleanSetting("Enchants", true));
   private final ColorSetting outline = this.add(new ColorSetting("Outline", new Color(-1711276033, true)).injectBoolean(true));
   private final ColorSetting rect = this.add(new ColorSetting("Rect", new Color(-1728053247, true)).injectBoolean(true));
   private final ColorSetting friendColor = this.add(new ColorSetting("FriendColor", new Color(-14811363, true)));
   private final ColorSetting color = this.add(new ColorSetting("Color", new Color(-1, true)));
   private final SliderSetting armorHeight = this.add(new SliderSetting("ArmorHeight", 0.3F, -10.0, 10.0));
   private final SliderSetting armorScale = this.add(new SliderSetting("ArmorScale", 0.9F, 0.1F, 2.0, 0.01F));
   private final EnumSetting<NameTags_AuEMiXPlywKMVYDJMcAR> armorMode = this.add(new EnumSetting("ArmorMode", NameTags_AuEMiXPlywKMVYDJMcAR.Full));

   public NameTags_NZLxiZHrtsQKbsfDngrN() {
      super("NameTags", Module_JlagirAibYQgkHtbRnhw.Render);
      INSTANCE = this;
   }

   public static String getEntityPing(PlayerEntity entity) {
      if (mc.method_1562() == null) {
         return "-1";
      } else {
         PlayerListEntry playerListEntry = mc.method_1562().method_2871(entity.method_5667());
         if (playerListEntry == null) {
            return "-1";
         } else {
            int ping = playerListEntry.method_2959();
            Formatting color = Formatting.field_1060;
            if (ping >= 100) {
               color = Formatting.field_1054;
            }

            if (ping >= 250) {
               color = Formatting.field_1061;
            }

            return color.toString() + ping;
         }
      }
   }

   public static GameMode getEntityGamemode(PlayerEntity entity) {
      if (entity == null) {
         return null;
      } else {
         PlayerListEntry playerListEntry = mc.method_1562().method_2871(entity.method_5667());
         return playerListEntry == null ? null : playerListEntry.method_2958();
      }
   }

   public static float round2(double value) {
      BigDecimal bd = new BigDecimal(value);
      bd = bd.setScale(1, RoundingMode.HALF_UP);
      return bd.floatValue();
   }

   @Override
   public void onRender2D(DrawContext context, float tickDelta) {
      for (PlayerEntity ent : mc.field_1687.method_18456()) {
         if (ent != mc.field_1724 || !mc.field_1690.method_31044().method_31034() || !FreeCam.INSTANCE.isOff()) {
            double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)mc.method_1488();
            double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)mc.method_1488();
            double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)mc.method_1488();
            Vec3d vector = new Vec3d(x, y + this.height.getValue() + ent.method_5829().method_17940() + 0.3, z);
            vector = TextUtil.worldSpaceToScreenSpace(new Vec3d(vector.field_1352, vector.field_1351, vector.field_1350));
            if (vector.field_1350 > 0.0 && vector.field_1350 < 1.0) {
               Vector4d position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0);
               position.x = Math.min(vector.field_1352, position.x);
               position.y = Math.min(vector.field_1351, position.y);
               position.z = Math.max(vector.field_1352, position.z);
               String final_string = "";
               if (this.ping.getValue()) {
                  final_string = final_string + getEntityPing(ent) + "ms ";
               }

               if (this.gamemode.getValue()) {
                  final_string = final_string + this.translateGamemode(getEntityGamemode(ent)) + " ";
               }

               final_string = final_string + Formatting.field_1070 + ent.method_5477().getString();
               if (this.health.getValue()) {
                  final_string = final_string + " " + this.getHealthColor(ent) + round2((double)(ent.method_6067() + ent.method_6032()));
               }

               if (this.distance.getValue()) {
                  final_string = final_string + " " + Formatting.field_1070 + String.format("%.1f", mc.field_1724.method_5739(ent)) + "m";
               }

               if (this.pops.getValue() && me.hextech.HexTech.POP.getPop(ent.method_5477().getString()) != 0) {
                  final_string = final_string + " §bPop " + Formatting.field_1076 + me.hextech.HexTech.POP.getPop(ent.method_5477().getString());
               }

               double posX = position.x;
               double posY = position.y;
               double endPosX = position.z;
               float diff = (float)(endPosX - posX) / 2.0F;
               float textWidth;
               if (this.font.getValue() == NameTags_VRQxrjlOGbJxMNTCEBWa.Fancy) {
                  textWidth = FontRenderers.Arial.getWidth(final_string) * 1.0F;
               } else {
                  textWidth = (float)mc.field_1772.method_1727(final_string);
               }

               float tagX = (float)((posX + (double)diff - (double)(textWidth / 2.0F)) * 1.0);
               ArrayList<ItemStack> stacks = new ArrayList();
               stacks.add(ent.method_6047());
               stacks.add((ItemStack)ent.method_31548().field_7548.get(3));
               stacks.add((ItemStack)ent.method_31548().field_7548.get(2));
               stacks.add((ItemStack)ent.method_31548().field_7548.get(1));
               stacks.add((ItemStack)ent.method_31548().field_7548.get(0));
               stacks.add(ent.method_6079());
               context.method_51448().method_22903();
               context.method_51448().method_46416(tagX - 2.0F + (textWidth + 4.0F) / 2.0F, (float)(posY - 13.0) + 6.5F, 0.0F);
               float size = (float)Math.max(
                  1.0 - (double)MathHelper.method_15355((float)mc.field_1719.method_5707(vector)) * 0.01 * this.scaled.getValue(), 0.0
               );
               context.method_51448()
                  .method_22905(
                     Math.max(this.scale.getValueFloat() * size, this.minScale.getValueFloat()),
                     Math.max(this.scale.getValueFloat() * size, this.minScale.getValueFloat()),
                     1.0F
                  );
               context.method_51448()
                  .method_46416(0.0F, this.offset.getValueFloat() * MathHelper.method_15355((float)EntityUtil.getEyesPos().method_1025(vector)), 0.0F);
               context.method_51448().method_46416(-(tagX - 2.0F + (textWidth + 4.0F) / 2.0F), -((float)(posY - 13.0 + 6.5)), 0.0F);
               float item_offset = 0.0F;
               if (this.armorMode.getValue() != NameTags_AuEMiXPlywKMVYDJMcAR.None) {
                  int count = 0;

                  for (ItemStack armorComponent : stacks) {
                     count++;
                     if (!armorComponent.method_7960()) {
                        context.method_51448().method_22903();
                        context.method_51448().method_46416(tagX - 2.0F + (textWidth + 4.0F) / 2.0F, (float)(posY - 13.0) + 6.5F, 0.0F);
                        context.method_51448().method_22905(this.armorScale.getValueFloat(), this.armorScale.getValueFloat(), 1.0F);
                        context.method_51448().method_46416(-(tagX - 2.0F + (textWidth + 4.0F) / 2.0F), -((float)(posY - 13.0 + 6.5)), 0.0F);
                        context.method_51448()
                           .method_22904(posX - 52.5 + (double)item_offset, (double)((float)(posY - 29.0) + this.armorHeight.getValueFloat()), 0.0);
                        float durability = (float)(armorComponent.method_7936() - armorComponent.method_7919());
                        int percent = (int)(durability / (float)armorComponent.method_7936() * 100.0F);
                        Color color;
                        if (percent <= 33) {
                           color = Color.RED;
                        } else if (percent <= 66) {
                           color = Color.ORANGE;
                        } else {
                           color = Color.GREEN;
                        }

                        switch ((NameTags_AuEMiXPlywKMVYDJMcAR)this.armorMode.getValue()) {
                           case Full:
                              DiffuseLighting.method_24210();
                              context.method_51427(armorComponent, 0, 0);
                              context.method_51431(mc.field_1772, armorComponent, 0, 0);
                              if (armorComponent.method_7936() > 0) {
                                 if (this.font.getValue() == NameTags_VRQxrjlOGbJxMNTCEBWa.Fancy) {
                                    FontRenderers.Arial
                                       .drawString(
                                          context.method_51448(),
                                          String.valueOf(percent),
                                          9.0F - FontRenderers.Arial.getWidth(String.valueOf(percent)) / 2.0F,
                                          -FontRenderers.Arial.getFontHeight() + 3.0F,
                                          color.getRGB()
                                       );
                                 } else {
                                    context.method_51433(
                                       mc.field_1772,
                                       String.valueOf(percent),
                                       9 - mc.field_1772.method_1727(String.valueOf(percent)) / 2,
                                       -9 + 1,
                                       color.getRGB(),
                                       true
                                    );
                                 }
                              }
                              break;
                           case Durability:
                              context.method_51431(mc.field_1772, armorComponent, 0, 0);
                              if (armorComponent.method_7936() > 0) {
                                 if (!armorComponent.method_31578()) {
                                    int i = armorComponent.method_31579();
                                    int j = armorComponent.method_31580();
                                    int k = 2;
                                    int l = 13;
                                    context.method_51739(RenderLayer.method_51785(), k, l, k + 13, l + 2, -16777216);
                                    context.method_51739(RenderLayer.method_51785(), k, l, k + i, l + 1, j | 0xFF000000);
                                 }

                                 if (this.font.getValue() == NameTags_VRQxrjlOGbJxMNTCEBWa.Fancy) {
                                    FontRenderers.Arial
                                       .drawString(
                                          context.method_51448(),
                                          String.valueOf(percent),
                                          9.0F - FontRenderers.Arial.getWidth(String.valueOf(percent)) / 2.0F,
                                          7.0F,
                                          color.getRGB()
                                       );
                                 } else {
                                    context.method_51433(
                                       mc.field_1772,
                                       String.valueOf(percent),
                                       9 - mc.field_1772.method_1727(String.valueOf(percent)) / 2,
                                       5,
                                       color.getRGB(),
                                       true
                                    );
                                 }
                              }
                              break;
                           case Item:
                              DiffuseLighting.method_24210();
                              context.method_51427(armorComponent, 0, 0);
                              context.method_51431(mc.field_1772, armorComponent, 0, 0);
                              break;
                           case OnlyArmor:
                              if (count > 1 && count < 6) {
                                 DiffuseLighting.method_24210();
                                 context.method_51427(armorComponent, 0, 0);
                                 context.method_51431(mc.field_1772, armorComponent, 0, 0);
                              }
                        }

                        context.method_51448().method_22909();
                        if (this.enchants.getValue()) {
                           float enchantmentY = 0.0F;
                           NbtList enchants = armorComponent.method_7921();

                           for (int index = 0; index < enchants.size(); index++) {
                              String id = enchants.method_10602(index).method_10558("id");
                              short level = enchants.method_10602(index).method_10568("lvl");
                              String encName;
                              switch (id) {
                                 case "minecraft:blast_protection":
                                    encName = "B" + level;
                                    break;
                                 case "minecraft:protection":
                                    encName = "P" + level;
                                    break;
                                 case "minecraft:thorns":
                                    encName = "T" + level;
                                    break;
                                 case "minecraft:sharpness":
                                    encName = "S" + level;
                                    break;
                                 case "minecraft:efficiency":
                                    encName = "E" + level;
                                    break;
                                 case "minecraft:unbreaking":
                                    encName = "U" + level;
                                    break;
                                 case "minecraft:power":
                                    encName = "PO" + level;
                                    break;
                                 default:
                                    continue;
                              }

                              if (this.font.getValue() == NameTags_VRQxrjlOGbJxMNTCEBWa.Fancy) {
                                 FontRenderers.Arial
                                    .drawString(
                                       context.method_51448(), encName, posX - 50.0 + (double)item_offset, (double)((float)posY - 45.0F + enchantmentY), -1
                                    );
                              } else {
                                 context.method_51448().method_22903();
                                 context.method_51448().method_22904(posX - 50.0 + (double)item_offset, posY - 45.0 + (double)enchantmentY, 0.0);
                                 context.method_51433(mc.field_1772, encName, 0, 0, -1, true);
                                 context.method_51448().method_22909();
                              }

                              enchantmentY -= 8.0F;
                           }
                        }
                     }

                     item_offset += 18.0F;
                  }
               }

               if (this.rect.booleanValue) {
                  Render2DUtil.drawRect(context.method_51448(), tagX - 2.0F, (float)(posY - 13.0), textWidth + 4.0F, 11.0F, this.rect.getValue());
               }

               if (this.outline.booleanValue) {
                  Render2DUtil.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0), textWidth + 6.0F, 1.0F, this.outline.getValue());
                  Render2DUtil.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 2.0), textWidth + 6.0F, 1.0F, this.outline.getValue());
                  Render2DUtil.drawRect(context.method_51448(), tagX - 3.0F, (float)(posY - 14.0), 1.0F, 12.0F, this.outline.getValue());
                  Render2DUtil.drawRect(context.method_51448(), tagX + textWidth + 2.0F, (float)(posY - 14.0), 1.0F, 12.0F, this.outline.getValue());
               }

               if (this.font.getValue() == NameTags_VRQxrjlOGbJxMNTCEBWa.Fancy) {
                  FontRenderers.Arial
                     .drawString(
                        context.method_51448(),
                        final_string,
                        tagX,
                        (float)posY - 10.0F,
                        me.hextech.HexTech.FRIEND.isFriend(ent) ? this.friendColor.getValue().getRGB() : this.color.getValue().getRGB()
                     );
               } else {
                  context.method_51448().method_22903();
                  context.method_51448().method_46416(tagX, (float)posY - 11.0F, 0.0F);
                  context.method_51433(
                     mc.field_1772,
                     final_string,
                     0,
                     0,
                     me.hextech.HexTech.FRIEND.isFriend(ent) ? this.friendColor.getValue().getRGB() : this.color.getValue().getRGB(),
                     true
                  );
                  context.method_51448().method_22909();
               }

               context.method_51448().method_22909();
            }
         }
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private String translateGamemode(GameMode gamemode) {
      if (gamemode == null) {
         return "§7[BOT]";
      } else {
         return switch (<unrepresentable>.$SwitchMap$net$minecraft$world$GameMode[gamemode.ordinal()]) {
            case 1 -> "§b[S]";
            case 2 -> "§c[C]";
            case 3 -> "§7[SP]";
            case 4 -> "§e[A]";
            default -> throw new IncompatibleClassChangeError();
         };
      }
   }

   private Formatting getHealthColor(@NotNull PlayerEntity entity) {
      int health = (int)((float)((int)entity.method_6032()) + entity.method_6067());
      if (health >= 30) {
         return Formatting.field_1077;
      } else if (health >= 24) {
         return Formatting.field_1060;
      } else if (health >= 18) {
         return Formatting.field_1054;
      } else if (health >= 12) {
         return Formatting.field_1065;
      } else {
         return health >= 6 ? Formatting.field_1061 : Formatting.field_1079;
      }
   }
}
