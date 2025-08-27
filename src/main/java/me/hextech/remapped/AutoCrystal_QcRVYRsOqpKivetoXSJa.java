package me.hextech.remapped;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import me.hextech.asm.accessors.IInteractEntityC2SPacket;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AutoCrystal_QcRVYRsOqpKivetoXSJa extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static AutoCrystal_QcRVYRsOqpKivetoXSJa INSTANCE;
   public static BlockPos tempPos;
   public static BlockPos breakPos;
   public static BlockPos syncPos;
   public static float lastYaw;
   public static float lastPitch;
   public static BlockPos crystalPos;
   static Vec3d placeVec3d;
   static Vec3d curVec3d;
   public final Timer lastBreakTimer = new Timer();
   public final EnumSetting<Enum_sBhvBqKgHyCqkGvharVr> page = this.add(new EnumSetting("Page", Enum_sBhvBqKgHyCqkGvharVr.General));
   public final BooleanSetting eatingPause = this.add(new BooleanSetting("EatingPause", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
   public final BooleanSetting preferAnchor = this.add(new BooleanSetting("PreferAnchor", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
   public final BooleanSetting preferpiston = this.add(new BooleanSetting("PreferPiston", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
   public final EnumSetting<SwingSide> swingMode = this.add(
      new EnumSetting("Swing", SwingSide.Server, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General)
   );
   public final BooleanSetting breakOnlyHasCrystal = this.add(
      new BooleanSetting("OnlyHoldItem", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General)
   );
   public final SliderSetting switchCooldown = this.add(
      new SliderSetting("SwitchPause", 100, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General)
   );
   public final SliderSetting targetRange = this.add(
      new SliderSetting("TargetRange", 12.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General)
   );
   public final SliderSetting updateDelay = this.add(
      new SliderSetting("UpdateDelay", 50, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General)
   );
   public final SliderSetting wallRange = this.add(
      new SliderSetting("WallRange", 6.0, 0.0, 6.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General)
   );
   public final SliderSetting lagTime = this.add(new SliderSetting("LagTime", 500, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
   private final BooleanSetting render3DUpdate = this.add(
      new BooleanSetting("Render3DUpdate", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General)
   );
   private final BooleanSetting render3DUpdate2 = this.add(
      new BooleanSetting("Render3DUpdate2", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General)
   );
   private final BooleanSetting walkingUpdate = this.add(
      new BooleanSetting("WalkingUpdate", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General)
   );
   private final BooleanSetting render2DUpdate = this.add(
      new BooleanSetting("Render2DUpdate", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General)
   );
   public final BooleanSetting waitburrow = this.add(new BooleanSetting("WaitBurrow", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Cancel));
   public final BooleanSetting cancelBurrow = this.add(new BooleanSetting("CancelBurrow", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Cancel));
   public final BooleanSetting waitCleaner = this.add(new BooleanSetting("WaitCleaner", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Cancel));
   public final BooleanSetting cblink = this.add(new BooleanSetting("CancelBlink", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Cancel));
   public final BooleanSetting expcancel = this.add(new BooleanSetting("ExpCancel", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Cancel));
   public final BooleanSetting place = this.add(new BooleanSetting("Place", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
   public final EnumSetting<Enum_rNhWITNdkrqkhKfDZgGo> autoSwap = this.add(
      new EnumSetting("AutoSwap", Enum_rNhWITNdkrqkhKfDZgGo.Off, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place)
   );
   public final SliderSetting minDamage = this.add(new SliderSetting("MinDamage", 6.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
   public final SliderSetting maxSelf = this.add(new SliderSetting("MaxSelfDmg", 12.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
   public final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 0.0, 6.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
   public final SliderSetting noSuicide = this.add(new SliderSetting("NoSuicide", 3.0, 0.0, 10.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
   public final BooleanSetting replace = this.add(new BooleanSetting("Replace", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
   public final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 300, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
   public final SliderSetting ingionPiston = this.add(
      new SliderSetting("IngionPiston", 8.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place)
   );
   public final SliderSetting yOffset = this.add(new SliderSetting("YOffset", 0, -1, 1, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
   public final SliderSetting expand = this.add(new SliderSetting("Expand", 0.5, 0.0, 1.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
   public final BooleanSetting ObbyVector = this.add(new BooleanSetting("ObbyVector", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
   public final BooleanSetting Break = this.add(new BooleanSetting("Break", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Break));
   public final SliderSetting breakMinDmg = this.add(
      new SliderSetting("BreakMin", 2.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Break)
   );
   public final SliderSetting breakDelay = this.add(new SliderSetting("BreakDelay", 300, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Break));
   public final BooleanSetting breakRemove = this.add(new BooleanSetting("SetDead", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Break));
   public final BooleanSetting instant = this.add(new BooleanSetting("Instant", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Break));
   public final BooleanSetting instantcalc = this.add(new BooleanSetting("InstantCalcs", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Break));
   public final BooleanSetting alwayscalc = this.add(new BooleanSetting("AlwaysCalcs", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Break));
   public final SliderSetting minAge = this.add(new SliderSetting("MinAge", 0, 0, 20, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Break));
   public final BooleanSetting rotate = this.add(new BooleanSetting("Rotation", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Rotation));
   public final BooleanSetting onBreak = this.add(new BooleanSetting("OnBreak", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Rotation));
   public final BooleanSetting offTackStep = this.add(new BooleanSetting("OffTack", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Rotation));
   public final BooleanSetting checkLook = this.add(new BooleanSetting("CheckLook", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Rotation));
   public final SliderSetting fov = this.add(new SliderSetting("Fov", 30.0, 0.0, 90.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Rotation));
   public final BooleanSetting faceVector = this.add(new BooleanSetting("FaceVector", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Rotation));
   public final EnumSetting<Enum> rotateMode = this.add(
      new EnumSetting("RotateMode", Enum.OffTrack, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Rotation)
   );
   public final BooleanSetting obsidian = this.add(new BooleanSetting("Obsidian", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Obsidian));
   public final SliderSetting placeObsDelay = this.add(
      new SliderSetting("PlaceObsDelay", 500, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Obsidian)
   );
   public final BooleanSetting smart = this.add(new BooleanSetting("Smart", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations));
   public final BooleanSetting useThread = this.add(new BooleanSetting("UseThread", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations));
   public final BooleanSetting doCrystal = this.add(
      new BooleanSetting("CalcCrystal", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations)
   );
   public final BooleanSetting lite = this.add(new BooleanSetting("LiteCheck", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations));
   public final EnumSetting<Enum_IKgLeKHCELPvcpdGlLhV> calcMode = this.add(
      new EnumSetting("CalcMode", Enum_IKgLeKHCELPvcpdGlLhV.OyVey, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations)
   );
   public final SliderSetting calcdelay = this.add(
      new SliderSetting("CalcDelay", 500, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations)
   );
   public final BooleanSetting slowPlace = this.add(new BooleanSetting("FacePlace", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace));
   public final SliderSetting slowDelay = this.add(
      new SliderSetting("FacePlaceDelay", 600, 0, 2000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace)
   );
   public final SliderSetting slowMinDamage = this.add(
      new SliderSetting("FaceMin", 1.5, 0.0, 36.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace)
   );
   public final BooleanSetting forcePlace = this.add(new BooleanSetting("HealthPlace", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace));
   public final SliderSetting forceMaxHealth = this.add(
      new SliderSetting("MaxHealth", 7, 0, 36, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace)
   );
   public final SliderSetting forceMin = this.add(
      new SliderSetting("HealthMin", 1.5, 0.0, 36.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace)
   );
   public final BooleanSetting armorBreaker = this.add(
      new BooleanSetting("ArmorBreaker", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace)
   );
   public final SliderSetting maxDurable = this.add(
      new SliderSetting("MaxDurable", 8, 0, 100, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace)
   );
   public final SliderSetting armorBreakerDamage = this.add(
      new SliderSetting("MinDurable", 3.0, 0.0, 36.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace)
   );
   public final BooleanSetting antiSurround = this.add(
      new BooleanSetting("AntiSurround", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.AntiSurround)
   );
   public final SliderSetting antiSurroundMax = this.add(
      new SliderSetting("WhenLower", 5.0, 0.0, 36.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.AntiSurround)
   );
   public final SliderSetting antiSurroundProgress = this.add(
      new SliderSetting("Progress", 0.8, 0.0, 1.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.AntiSurround)
   );
   public final SliderSetting HurtTime = this.add(
      new SliderSetting("SyncTime", 10.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize)
   );
   public final SliderSetting SyncTime = this.add(
      new SliderSetting("LastSync", 8.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize)
   );
   public final BooleanSetting WebSync = this.add(new BooleanSetting("WebDamage", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
   public final SliderSetting WebMinDamage = this.add(
      new SliderSetting("WebMinDmg", 5.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize)
   );
   public final BooleanSetting SyncTerrainIgnore = this.add(
      new BooleanSetting("TerrainIgnore", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize)
   );
   public final BooleanSetting WebdeSync = this.add(new BooleanSetting("DeSyncWeb", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
   public final BooleanSetting breaktime = this.add(
      new BooleanSetting("BreakTimeSync", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize)
   );
   public final BooleanSetting posSync = this.add(new BooleanSetting("PosSync", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
   public final BooleanSetting forceWeb = this.add(new BooleanSetting("SyncTick", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
   public final BooleanSetting obbysync = this.add(new BooleanSetting("ObbySync", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
   public final BooleanSetting OnlySync = this.add(new BooleanSetting("OnlySync", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
   public final SliderSetting OnlySyncTime = this.add(
      new SliderSetting("OnlySyncTime", 10.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize)
   );
   public final SliderSetting SpamSyncTime = this.add(
      new SliderSetting("SpamSyncTime", 100.0, 0.0, 1000.0, 1.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize)
   );
   public final BooleanSetting syncdebug = this.add(new BooleanSetting("SyncDebug", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
   public final EnumSetting<Aura_nurTqHTNjexQmuWdDgIn> mode = this.add(
      new EnumSetting("TargetESP", Aura_nurTqHTNjexQmuWdDgIn.Jello, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render)
   );
   public final ColorSetting color = this.add(
      new ColorSetting("TargetColor", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render)
   );
   public final ColorSetting text = this.add(
      new ColorSetting("Text", new Color(-1), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false)
   );
   public final ColorSetting misscatext = this.add(
      new ColorSetting("MissText", new Color(-1), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(true)
   );
   public final ColorSetting nullpostext = this.add(
      new ColorSetting("NullposText", new Color(-1), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(true)
   );
   public final ColorSetting spamtext = this.add(
      new ColorSetting("SpamText", new Color(-1), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(true)
   );
   public final BooleanSetting render = this.add(new BooleanSetting("Render", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render));
   public final BooleanSetting shrink = this.add(
      new BooleanSetting("Shrink", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue())
   );
   public final ColorSetting box = this.add(
      new ColorSetting("Box", new Color(255, 255, 255, 255), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue())
         .injectBoolean(true)
   );
   public final SliderSetting lineWidth = this.add(
      new SliderSetting("LineWidth", 1.5, 0.01, 3.0, 0.01, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue())
   );
   public final ColorSetting fill = this.add(
      new ColorSetting("Fill", new Color(255, 255, 255, 100), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue())
         .injectBoolean(true)
   );
   public final SliderSetting sliderSpeed = this.add(
      new SliderSetting("SliderSpeed", 0.2, 0.01, 1.0, 0.01, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue())
   );
   public final SliderSetting startFadeTime = this.add(
      new SliderSetting("StartFade", 0.3, 0.0, 5.0, 0.01, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue())
         .setSuffix("s")
   );
   public final SliderSetting fadeSpeed = this.add(
      new SliderSetting("FadeSpeed", 0.2, 0.01, 1.0, 0.01, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue())
   );
   public final ColorSetting online = new ColorSetting("Online", new Color(255, 255, 255, 255), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render)
      .injectBoolean(true);
   public final BooleanSetting bold = this.add(new BooleanSetting("Bold", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render));
   public final EnumSetting<AutoCrystal_ohSMJidwaoXtIVckTOpo> colortype = this.add(
      new EnumSetting("ColorType", AutoCrystal_ohSMJidwaoXtIVckTOpo.Custom, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render)
   );
   public final ColorSetting showCB_A = this.add(
      new ColorSetting("ShowCB_A", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false)
   );
   public final ColorSetting showCB_D = this.add(
      new ColorSetting("ShowCB_D", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false)
   );
   public final ColorSetting showIf = this.add(
      new ColorSetting("ShowIf", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false)
   );
   public final ColorSetting showif2 = this.add(
      new ColorSetting("ShowIf2", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false)
   );
   public final ColorSetting showCleaner = this.add(
      new ColorSetting("ShowCleaner", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false)
   );
   private final Timer delayTimer = new Timer();
   private final Timer placeTimer = new Timer();
   private final Timer noPosTimer = new Timer();
   private final Timer switchTimer = new Timer();
   private final Timer lagTimer = new Timer();
   private final Timer onlysynctime = new Timer();
   private final List<AutoCrystal_DyfHylndhLrmDUsYPHRl> idPredictQueue = new ArrayList();
   public float breakDamage;
   public float tempDamage;
   public float lastDamage;
   public PlayerEntity displayTarget;
   public Vec3d directionVec = null;
   double fade = 0.0;
   private int lastConfirmedId = -1;

   public AutoCrystal_QcRVYRsOqpKivetoXSJa() {
      super("AutoCrystal", Module_JlagirAibYQgkHtbRnhw.Combat);
      INSTANCE = this;
      me.hextech.HexTech.EVENT_BUS.subscribe(new AutoCrystal(this));
   }

   @Override
   public String getInfo() {
      return this.displayTarget != null && this.lastDamage > 0.0F
         ? this.displayTarget.method_5477().getString() + ", " + new DecimalFormat("0.0").format((double)this.lastDamage)
         : null;
   }

   private boolean shouldReturn() {
      if (mc.field_1724 != null && this.eatingPause.getValue() && mc.field_1724.method_6115()) {
         this.lastBreakTimer.reset();
         return true;
      } else if (this.preferAnchor.getValue() && AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos != null) {
         this.lastBreakTimer.reset();
         return true;
      } else {
         return false;
      }
   }

   private void doInteract() {
      if (!this.shouldReturn()) {
         if (breakPos != null) {
            this.doBreak(breakPos);
            breakPos = null;
         }

         if (crystalPos != null) {
            this.doCrystal(crystalPos);
         }
      }
   }

   @Override
   public void onEnable() {
      lastYaw = me.hextech.HexTech.ROTATE.lastYaw;
      lastPitch = RotateManager.lastPitch;
      crystalPos = null;
      tempPos = null;
      breakPos = null;
      this.displayTarget = null;
      this.onlysynctime.reset();
      this.lastBreakTimer.reset();
   }

   @Override
   public void onDisable() {
      lastYaw = me.hextech.HexTech.ROTATE.lastYaw;
      lastPitch = RotateManager.lastPitch;
      crystalPos = null;
      tempPos = null;
      breakPos = null;
      this.displayTarget = null;
      this.idPredictQueue.clear();
      this.onlysynctime.reset();
      this.lastBreakTimer.reset();
   }

   @Override
   public void onThread() {
      if (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.crystalThread.getValue()) {
         this.updateCrystalPos();
         List<AutoCrystal_DyfHylndhLrmDUsYPHRl> remove = new ArrayList();

         for (AutoCrystal_DyfHylndhLrmDUsYPHRl t : this.idPredictQueue) {
            if (System.currentTimeMillis() >= t.executeAt) {
               this.sendAttackPacket(t.crystalId, t.crystalPos);
               remove.add(t);
            }
         }

         this.idPredictQueue.removeAll(remove);
      }
   }

   @EventHandler
   public void onUpdateWalking(UpdateWalkingEvent event) {
      if (!this.useThread.getValue()) {
         this.updateCrystalPos();
      }

      if (this.walkingUpdate.getValue()) {
         this.doInteract();
      }

      List<AutoCrystal_DyfHylndhLrmDUsYPHRl> remove = new ArrayList();

      for (AutoCrystal_DyfHylndhLrmDUsYPHRl t : this.idPredictQueue) {
         if (System.currentTimeMillis() >= t.executeAt) {
            this.sendAttackPacket(t.crystalId, t.crystalPos);
            remove.add(t);
         }
      }

      this.idPredictQueue.removeAll(remove);
   }

   private void sendAttackPacket(int id, Vec3d pos) {
      if (mc.field_1687 != null) {
         PlayerInteractEntityC2SPacket packet = null;
         if (mc.field_1724 != null) {
            packet = PlayerInteractEntityC2SPacket.method_34206(mc.field_1724, mc.field_1724.method_5715());
         }

         if (packet != null) {
            ((IInteractEntityC2SPacket)packet).setId(id);
         }

         mc.method_1562().method_52787(packet);
         if (this.breakRemove.getValue()) {
            Entity ent = mc.field_1687.method_8469(id);
            if (ent instanceof EndCrystalEntity) {
               mc.field_1687.method_2945(id, RemovalReason.field_26998);
            }
         }
      }
   }

   @EventHandler
   public void onUpdateWalking() {
      if (!this.useThread.getValue()) {
         this.updateCrystalPos();
      }

      if (crystalPos != null) {
         this.doCrystal(crystalPos);
      }
   }

   @Override
   public void onRender3D(MatrixStack matrixStack) {
      if (!this.useThread.getValue()) {
         this.updateCrystalPos();
      }

      if (this.render3DUpdate.getValue()) {
         this.doInteract();
      }

      if (this.displayTarget != null && !this.noPosTimer.passedMs(500L)) {
         this.doRender(matrixStack, mc.method_1488(), this.displayTarget, (Aura_nurTqHTNjexQmuWdDgIn)this.mode.getValue());
      }
   }

   @Override
   public void onRender2D(DrawContext drawContext, float tickDelta) {
      if (!this.useThread.getValue()) {
         this.updateCrystalPos();
      }

      if (this.render2DUpdate.getValue()) {
         this.doInteract();
      }
   }

   public void doRender(MatrixStack matrixStack, float partialTicks, Entity entity, Aura_nurTqHTNjexQmuWdDgIn mode) {
      if (Objects.requireNonNull(mode) == Aura_nurTqHTNjexQmuWdDgIn.Jello) {
         JelloUtil.drawJello(matrixStack, entity, this.color.getValue());
      }
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      if (!this.useThread.getValue()) {
         this.updateCrystalPos();
      }

      if (this.render3DUpdate2.getValue()) {
         this.doInteract();
      }

      if (this.displayTarget != null && !this.noPosTimer.passedMs(500L)) {
         Aura.doRender(matrixStack, partialTicks, this.displayTarget, this.color.getValue(), (Aura_nurTqHTNjexQmuWdDgIn)this.mode.getValue());
      }
   }

   @EventHandler
   public void onRotate(RotateEvent event) {
      if (!this.rotate.getValue() || this.directionVec == null || this.noPosTimer.passed(CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.maxrotateTime.getValue())) {
         lastYaw = me.hextech.HexTech.ROTATE.lastYaw;
         lastPitch = RotateManager.lastPitch;
         event.setYaw(lastYaw);
         event.setPitch(lastPitch);
      } else if (INSTANCE.rotateMode.getValue() == Enum.Inject) {
         float[] newAngle = InjectRotate.injectStep(
            EntityUtil.getLegitRotations(this.directionVec), CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injectstep.getValueFloat()
         );
         lastYaw = newAngle[0];
         lastPitch = newAngle[1];
         if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.random.getValue() && new Random().nextBoolean()) {
            lastPitch = Math.min(new Random().nextFloat() * 2.0F + lastPitch, 90.0F);
         }

         event.setYaw(lastYaw);
         event.setPitch(lastPitch);
      } else if (INSTANCE.rotateMode.getValue() == Enum.OffTrack) {
         if (this.offTackStep.getValue()) {
            OffTrackEvent offTrackEvent = new OffTrackEvent();
            offTrackEvent.setTarget(this.directionVec, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.offstep.getValueFloat(), 1.0F);
            me.hextech.HexTech.EVENT_BUS.post(offTrackEvent);
            if (offTrackEvent.getTarget() != null || offTrackEvent.getRotation()) {
               float[] newAngle = me.hextech.HexTech.ROTATE
                  .offtrackStep(this.directionVec, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.offstep.getValueFloat());
               lastYaw = newAngle[0];
               lastPitch = newAngle[1];
               event.setYaw(lastYaw);
               event.setPitch(lastPitch);
               return;
            }
         }

         float[] newAngle = me.hextech.HexTech.ROTATE.offtrackStep(this.directionVec, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.offstep.getValueFloat());
         lastYaw = newAngle[0];
         lastPitch = newAngle[1];
         event.setYaw(lastYaw);
         event.setPitch(lastPitch);
      }
   }

   @EventHandler(
      priority = -199
   )
   public void onPacketSend(PacketEvent event) {
      if (!event.isCancelled()) {
         if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket) {
            this.switchTimer.reset();
         }
      }
   }

   private void updateCrystalPos() {
      this.update();
      this.lastDamage = this.tempDamage;
      crystalPos = tempPos;
   }

   private void update() {
      if (!nullCheck()) {
         if (this.obbysync.getValue() && BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.tempPos != null) {
            AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos = null;
            tempPos = null;
         } else if (this.lagTimer.passedMs((long)this.lagTime.getValueInt())) {
            if (this.eatingPause.getValue() && EntityUtil.isUsing()) {
               tempPos = null;
            } else {
               if (this.waitburrow.getValue() && Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.placePos == null) {
                  AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos = null;
                  tempPos = null;
               }

               if (this.cancelBurrow.getValue() && Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
                  tempPos = null;
               } else {
                  if (this.waitCleaner.getValue() && Cleaner_iFwqnooxsJEmHoVteFeQ.INSTANCE.syncPos != null) {
                     AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos = null;
                     tempPos = null;
                  }

                  if (this.cblink.getValue() && Blink.INSTANCE.isOn()) {
                     this.lastBreakTimer.reset();
                     tempPos = null;
                  } else if (this.expcancel.getValue() && AutoEXP.INSTANCE.isOn()) {
                     this.lastBreakTimer.reset();
                     tempPos = null;
                  } else if (this.delayTimer.passedMs((long)this.updateDelay.getValue())) {
                     if (this.preferpiston.getValue() && PistonCrystal.INSTANCE.isOn()) {
                        this.lastBreakTimer.reset();
                        tempPos = null;
                     } else {
                        if (this.WebdeSync.getValue()) {
                           AutoCrystal_QcRVYRsOqpKivetoXSJa.crystalPos = null;
                           tempPos = null;
                        }

                        if (this.breaktime.getValue()) {
                           this.lastBreakTimer.reset();
                        }

                        if (this.posSync.getValue()) {
                           tempPos = null;
                        }

                        if (mc.field_1724 != null
                           && this.breakOnlyHasCrystal.getValue()
                           && !mc.field_1724.method_6047().method_31574(Items.field_8301)
                           && !mc.field_1724.method_6079().method_31574(Items.field_8301)
                           && !this.findCrystal()) {
                           this.lastBreakTimer.reset();
                           tempPos = null;
                        } else if (!this.switchTimer.passedMs((long)this.switchCooldown.getValue())) {
                           tempPos = null;
                        } else if (!this.shouldReturn()) {
                           this.delayTimer.reset();
                           tempPos = null;
                           this.tempDamage = 0.0F;
                           breakPos = null;
                           this.breakDamage = 0.0F;
                           ArrayList<PredictionSetting_XBpBEveLWEKUGQPHCCIS> targets = new ArrayList();

                           for (PlayerEntity target : CombatUtil.getEnemies((double)this.targetRange.getValueFloat())) {
                              if (target.field_6235 <= this.HurtTime.getValueInt()) {
                                 targets.add(new PredictionSetting_XBpBEveLWEKUGQPHCCIS(target));
                              }
                           }

                           if (targets.isEmpty()) {
                              this.lastBreakTimer.reset();
                           } else {
                              PredictionSetting_XBpBEveLWEKUGQPHCCIS self = new PredictionSetting_XBpBEveLWEKUGQPHCCIS(mc.field_1724);

                              for (BlockPos pos : BlockUtil.getSphere((float)this.range.getValue() + 1.0F)) {
                                 if (!WallCheck.behindWall(pos)
                                    && !(mc.field_1724.method_33571().method_1022(pos.method_46558().method_1031(0.0, -0.5, 0.0)) > this.range.getValue())
                                    && this.canTouch(pos.method_10074())
                                    && CanPlaceCrystal.canPlaceCrystal(pos, true, false)) {
                                    for (PredictionSetting_XBpBEveLWEKUGQPHCCIS targetx : targets) {
                                       if (!this.lite.getValue()
                                          || !ListenerHelperUtil.liteCheck(pos.method_46558().method_1031(0.0, -0.5, 0.0), targetx.predict.method_19538())) {
                                          int placeTicks = (int)PredictionSetting.INSTANCE.placeExtrap.getValue();
                                          PlayerEntity placePredict = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createPredict(
                                             targetx.player, placeTicks, (int)PredictionSetting.INSTANCE.extrapTicks.getValue()
                                          );
                                          float damage = CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateDamage(pos, placePredict, placePredict);
                                          if (tempPos == null || damage > this.tempDamage) {
                                             float selfDamage = CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateDamage(
                                                pos,
                                                self.player,
                                                ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createSelfPredict(
                                                   self.player, (int)PredictionSetting.INSTANCE.selfExtrap.getValue()
                                                )
                                             );
                                             if (!((double)selfDamage > this.maxSelf.getValue())
                                                && (
                                                   !(this.noSuicide.getValue() > 0.0)
                                                      || !(
                                                         (double)selfDamage
                                                            > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067()) - this.noSuicide.getValue()
                                                      )
                                                )
                                                && !((double)damage < this.getDamage(targetx.player))
                                                && (!this.smart.getValue() || !(damage < selfDamage))) {
                                                this.displayTarget = targetx.player;
                                                tempPos = pos;
                                                this.tempDamage = damage;
                                             }
                                          }
                                       }
                                    }
                                 }
                              }

                              if (mc.field_1687 != null) {
                                 for (Entity entity : mc.field_1687.method_18112()) {
                                    if (entity instanceof EndCrystalEntity) {
                                       EndCrystalEntity crystal = (EndCrystalEntity)entity;
                                       if ((
                                             mc.field_1724.method_6057(crystal)
                                                || !(mc.field_1724.method_33571().method_1022(crystal.method_19538()) > this.wallRange.getValue())
                                          )
                                          && !(mc.field_1724.method_33571().method_1022(crystal.method_19538()) > this.range.getValue())) {
                                          for (PredictionSetting_XBpBEveLWEKUGQPHCCIS targetxx : targets) {
                                             int breakTicks = (int)PredictionSetting.INSTANCE.breakExtrap.getValue();
                                             PlayerEntity breakPredict = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createPredict(
                                                targetxx.player, breakTicks, (int)PredictionSetting.INSTANCE.extrapTicks.getValue()
                                             );
                                             float damage = CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateDamage(
                                                BlockPos.method_49638(crystal.method_19538()), breakPredict, breakPredict
                                             );
                                             if (breakPos == null || damage > this.breakDamage) {
                                                float selfDamage = CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateDamage(
                                                   BlockPos.method_49638(crystal.method_19538()),
                                                   self.player,
                                                   ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createSelfPredict(
                                                      self.player, (int)PredictionSetting.INSTANCE.selfExtrap.getValue()
                                                   )
                                                );
                                                if (!((double)selfDamage > this.maxSelf.getValue())
                                                   && (
                                                      !(this.noSuicide.getValue() > 0.0)
                                                         || !(
                                                            (double)selfDamage
                                                               > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067())
                                                                  - this.noSuicide.getValue()
                                                         )
                                                   )
                                                   && !((double)damage < this.breakMinDmg.getValue())) {
                                                   breakPos = crystal.method_24515();
                                                   if (damage > this.tempDamage) {
                                                      this.displayTarget = targetxx.player;
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }

                              if (this.antiSurround.getValue()
                                 && SpeedMine.breakPos != null
                                 && SpeedMine.progress >= this.antiSurroundProgress.getValue()
                                 && !BlockUtil.hasEntity(SpeedMine.breakPos, false)
                                 && this.tempDamage <= this.antiSurroundMax.getValueFloat()) {
                                 for (PredictionSetting_XBpBEveLWEKUGQPHCCIS targetxxx : targets) {
                                    for (Direction dir : Direction.values()) {
                                       if (dir != Direction.field_11033 && dir != Direction.field_11036) {
                                          BlockPos offsetPos = targetxxx.player.method_24515().method_10093(dir);
                                          if (offsetPos.equals(SpeedMine.breakPos)) {
                                             BlockPos crystalPos = offsetPos.method_10093(dir);
                                             if (CanPlaceCrystal.canPlaceCrystal(crystalPos, false, false)) {
                                                float selfDamage = CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateDamage(
                                                   crystalPos,
                                                   self.player,
                                                   ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createSelfPredict(
                                                      self.player, (int)PredictionSetting.INSTANCE.selfExtrap.getValue()
                                                   )
                                                );
                                                if ((double)selfDamage < this.maxSelf.getValue()
                                                   && (
                                                      !(this.noSuicide.getValue() > 0.0)
                                                         || !(
                                                            (double)selfDamage
                                                               > (double)(mc.field_1724.method_6032() + mc.field_1724.method_6067())
                                                                  - this.noSuicide.getValue()
                                                         )
                                                   )) {
                                                   tempPos = crystalPos;
                                                   if (this.doCrystal.getValue()) {
                                                      this.doCrystal(tempPos);
                                                   }

                                                   return;
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean canTouch(BlockPos pos) {
      Direction side = BlockUtil.getClickSideStrict(pos);
      return mc.field_1724 == null
         ? false
         : side != null
            && pos.method_46558()
                  .method_1019(
                     new Vec3d(
                        (double)side.method_10163().method_10263() * 0.5,
                        (double)side.method_10163().method_10264() * 0.5,
                        (double)side.method_10163().method_10260() * 0.5
                     )
                  )
                  .method_1022(mc.field_1724.method_33571())
               <= this.range.getValue();
   }

   public void doCrystal(BlockPos pos) {
      if (!INSTANCE.eatingPause.getValue() || !EntityUtil.isUsing()) {
         if (CanPlaceCrystal.canPlaceCrystal(pos, false, true)) {
            if (mc.field_1724 != null
               && (
                  mc.field_1724.method_6047().method_7909().equals(Items.field_8301)
                     || mc.field_1724.method_6079().method_7909().equals(Items.field_8301)
                     || this.findCrystal()
               )) {
               this.doPlace(pos);
            }
         } else {
            this.doBreak(pos);
         }
      }
   }

   private double getDamage(PlayerEntity target) {
      if (SpeedMine.INSTANCE.obsidian.isPressed()
         || !this.slowPlace.getValue()
         || !this.lastBreakTimer.passedMs((long)this.slowDelay.getValue())
         || BedAura_BzCWaQEhnpenizjBqrRp.INSTANCE.isOn() && BedAura_BzCWaQEhnpenizjBqrRp.INSTANCE.getBed() != -1) {
         if (this.forcePlace.getValue() && (double)EntityUtil.getHealth(target) <= this.forceMaxHealth.getValue() && !SpeedMine.INSTANCE.obsidian.isPressed()) {
            return this.forceMin.getValue();
         } else {
            if (this.armorBreaker.getValue()) {
               for (ItemStack armor : target.method_31548().field_7548) {
                  if (!armor.method_7960() && !((double)EntityUtil.getDamagePercent(armor) > this.maxDurable.getValue())) {
                     return this.armorBreakerDamage.getValue();
                  }
               }
            }

            return PistonCrystal.INSTANCE.isOn() ? (double)this.ingionPiston.getValueFloat() : this.minDamage.getValue();
         }
      } else {
         return this.slowMinDamage.getValue();
      }
   }

   public boolean findCrystal() {
      return this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Off ? false : this.getCrystal() != -1;
   }

   private void doBreak(BlockPos pos) {
      this.noPosTimer.reset();
      if (this.Break.getValue()) {
         if (!INSTANCE.eatingPause.getValue() || !EntityUtil.isUsing()) {
            if (!this.OnlySync.getValue()
               || this.displayTarget == null
               || this.displayTarget.field_6235 <= this.OnlySyncTime.getValueInt()
               || this.onlysynctime.passed(this.SpamSyncTime.getValue())) {
               this.lastBreakTimer.reset();
               if (this.switchTimer.passedMs((long)this.switchCooldown.getValue())) {
                  this.onlysynctime.reset();

                  for (EndCrystalEntity entity : BlockUtil.getEndCrystals(
                     new Box(
                        (double)pos.method_10263(),
                        (double)pos.method_10264(),
                        (double)pos.method_10260(),
                        (double)(pos.method_10263() + 1),
                        (double)(pos.method_10264() + 2),
                        (double)(pos.method_10260() + 1)
                     )
                  )) {
                     if (entity.field_6012 >= this.minAge.getValueInt()) {
                        if (this.rotate.getValue()
                           && this.onBreak.getValue()
                           && !this.faceVector(entity.method_19538().method_1031(0.0, this.yOffset.getValue(), 0.0))) {
                           return;
                        }

                        if (!CombatUtil.breakTimer.passedMs((long)this.breakDelay.getValue())) {
                           return;
                        }

                        CombatUtil.breakTimer.reset();
                        syncPos = pos;
                        if (mc.field_1724 != null) {
                           ((ClientPlayNetworkHandler)Objects.requireNonNull(mc.method_1562()))
                              .method_52787(PlayerInteractEntityC2SPacket.method_34206(entity, mc.field_1724.method_5715()));
                        }

                        if (mc.field_1724 != null) {
                           mc.field_1724.method_7350();
                        }

                        EntityUtil.swingHand(Hand.field_5808, (SwingSide)this.swingMode.getValue());
                        if (this.breakRemove.getValue() && mc.field_1687 != null) {
                           mc.field_1687.method_2945(entity.method_5628(), RemovalReason.field_26998);
                        }

                        if (crystalPos != null
                           && this.displayTarget != null
                           && (double)this.lastDamage >= this.getDamage(this.displayTarget)
                           && this.instant.getValue()
                           && (!this.rotate.getValue() || this.rotateMode.getValue() == Enum.OffTrack)) {
                           this.doPlace(crystalPos);
                        }

                        if (this.instantcalc.getValue()) {
                           this.updateCrystalPos();
                        }

                        if (this.alwayscalc.getValue()) {
                           this.updateCrystalPos();
                        }

                        if (this.WebSync.getValue() && (double)this.lastDamage > this.WebMinDamage.getValue()) {
                           WebAuraTick_gaIdrzDzsbegzNTtPQoV.force = false;
                        }

                        if (this.forceWeb.getValue() && WebAuraTick_gaIdrzDzsbegzNTtPQoV.INSTANCE.isOn()) {
                           WebAuraTick_gaIdrzDzsbegzNTtPQoV.force = true;
                        }

                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private void doPlace(BlockPos pos) {
      this.noPosTimer.reset();
      if (!INSTANCE.eatingPause.getValue() || !EntityUtil.isUsing()) {
         if (this.place.getValue()) {
            if (mc.field_1724 == null
               || mc.field_1724.method_6047().method_7909().equals(Items.field_8301)
               || mc.field_1724.method_6079().method_7909().equals(Items.field_8301)
               || this.findCrystal()) {
               if (this.canTouch(pos.method_10074())) {
                  BlockPos obsPos = pos.method_10074();
                  Direction facing = BlockUtil.getClickSide(obsPos);
                  Vec3d vec = obsPos.method_46558()
                     .method_1031(
                        (double)facing.method_10163().method_10263() * 0.5,
                        (double)facing.method_10163().method_10264() * 0.5,
                        (double)facing.method_10163().method_10260() * 0.5
                     );
                  if (facing != Direction.field_11036 && facing != Direction.field_11033) {
                     vec = vec.method_1031(0.0, 0.45, 0.0);
                  }

                  if (!this.rotate.getValue() || this.faceVector(vec)) {
                     if (this.placeTimer.passedMs((long)this.placeDelay.getValue())) {
                        if (!mc.field_1724.method_6047().method_7909().equals(Items.field_8301)
                           && !mc.field_1724.method_6079().method_7909().equals(Items.field_8301)) {
                           this.placeTimer.reset();
                           syncPos = pos;
                           int old = mc.field_1724.method_31548().field_7545;
                           int crystal = this.getCrystal();
                           if (crystal == -1) {
                              return;
                           }

                           this.doSwap(crystal);
                           this.placeCrystal(pos);
                           if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Silent) {
                              this.doSwap(old);
                           } else if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
                              this.doSwap(crystal);
                              EntityUtil.syncInventory();
                           }
                        } else {
                           this.placeTimer.reset();
                           syncPos = pos;
                           this.placeCrystal(pos);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void doSwap(int slot) {
      if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Silent || this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Normal) {
         InventoryUtil.switchToSlot(slot);
      } else if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory && mc.field_1724 != null) {
         InventoryUtil.inventorySwap(slot, mc.field_1724.method_31548().field_7545);
      }
   }

   private int getCrystal() {
      if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Silent || this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Normal) {
         return InventoryUtil.findItem(Items.field_8301);
      } else {
         return this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory ? InventoryUtil.findItemInventorySlot(Items.field_8301) : -1;
      }
   }

   public void placeCrystal(BlockPos pos) {
      boolean offhand = false;
      if (mc.field_1724 != null) {
         offhand = mc.field_1724.method_6079().method_7909() == Items.field_8301;
      }

      BlockPos obsPos = pos.method_10074();
      Direction facing = BlockUtil.getClickSide(obsPos);
      BlockUtil.clickBlock(obsPos, facing, false, offhand ? Hand.field_5810 : Hand.field_5808, (SwingSide)this.swingMode.getValue());
      if (PredictionSetting.INSTANCE.idPredict.getValue()) {
         int highest = this.getHighestEntityId();
         int startId = highest + PredictionSetting.INSTANCE.idStartOffset.getValueInt();

         for (int i = 0; i < PredictionSetting.INSTANCE.idPackets.getValueInt(); i++) {
            int cid = startId + i * PredictionSetting.INSTANCE.idPacketOffset.getValueInt();
            long delay = (long)(PredictionSetting.INSTANCE.idStartDelay.getValue() + (double)i * PredictionSetting.INSTANCE.idPacketDelay.getValue());
            this.idPredictQueue
               .add(new AutoCrystal_DyfHylndhLrmDUsYPHRl(cid, pos.method_46558().method_1031(0.0, 1.0, 0.0), System.currentTimeMillis() + delay));
         }
      }
   }

   private int getHighestEntityId() {
      int max = this.lastConfirmedId;
      if (mc.field_1687 != null) {
         for (Entity e : mc.field_1687.method_18112()) {
            if (e.method_5628() > max) {
               max = e.method_5628();
            }
         }
      }

      this.lastConfirmedId = max;
      return max;
   }

   private boolean faceVector(Vec3d directionVec) {
      if (!this.faceVector.getValue()) {
         RotateManager.TrueVec3d(directionVec);
         return true;
      } else {
         this.directionVec = directionVec;
         return me.hextech.HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat()) ? true : !this.checkLook.getValue();
      }
   }
}
