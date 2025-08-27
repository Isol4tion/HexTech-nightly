package me.hextech.remapped;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import me.hextech.HexTech;
import me.hextech.asm.accessors.IInteractEntityC2SPacket;
import me.hextech.remapped.Aura;
import me.hextech.remapped.Aura_nurTqHTNjexQmuWdDgIn;
import me.hextech.remapped.AutoAnchor_MDcwoWYRcPYheLZJWRZK;
import me.hextech.remapped.AutoCrystal;
import me.hextech.remapped.AutoCrystal_DyfHylndhLrmDUsYPHRl;
import me.hextech.remapped.AutoCrystal_ohSMJidwaoXtIVckTOpo;
import me.hextech.remapped.AutoEXP;
import me.hextech.remapped.BaseThreadSetting_TYdViPaJQVoRZLdgWIXF;
import me.hextech.remapped.BedAura_BzCWaQEhnpenizjBqrRp;
import me.hextech.remapped.Blink;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.remapped.CanPlaceCrystal;
import me.hextech.remapped.Cleaner_iFwqnooxsJEmHoVteFeQ;
import me.hextech.remapped.ColorSetting;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.CombatUtil;
import me.hextech.remapped.CrystalDamage_eJITUTNYpCPnjaYYZUHH;
import me.hextech.remapped.EntityUtil;
import me.hextech.remapped.Enum;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Enum_IKgLeKHCELPvcpdGlLhV;
import me.hextech.remapped.Enum_rNhWITNdkrqkhKfDZgGo;
import me.hextech.remapped.Enum_sBhvBqKgHyCqkGvharVr;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn;
import me.hextech.remapped.InjectRotate;
import me.hextech.remapped.InventoryUtil;
import me.hextech.remapped.JelloUtil;
import me.hextech.remapped.ListenerHelperUtil;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.OffTrackEvent;
import me.hextech.remapped.PacketEvent;
import me.hextech.remapped.PistonCrystal;
import me.hextech.remapped.PredictionSetting;
import me.hextech.remapped.RotateEvent;
import me.hextech.remapped.RotateManager;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.SpeedMine;
import me.hextech.remapped.SwingSide;
import me.hextech.remapped.Timer;
import me.hextech.remapped.UpdateWalkingEvent;
import me.hextech.remapped.WallCheck;
import me.hextech.remapped.WebAuraTick_gaIdrzDzsbegzNTtPQoV;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;

public class AutoCrystal_QcRVYRsOqpKivetoXSJa
extends Module_eSdgMXWuzcxgQVaJFmKZ {
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
    public final EnumSetting<Enum_sBhvBqKgHyCqkGvharVr> page = this.add(new EnumSetting<Enum_sBhvBqKgHyCqkGvharVr>("Page", Enum_sBhvBqKgHyCqkGvharVr.General));
    public final BooleanSetting eatingPause = this.add(new BooleanSetting("EatingPause", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    public final BooleanSetting preferAnchor = this.add(new BooleanSetting("PreferAnchor", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    public final BooleanSetting preferpiston = this.add(new BooleanSetting("PreferPiston", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    public final EnumSetting<SwingSide> swingMode = this.add(new EnumSetting<SwingSide>("Swing", SwingSide.Server, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    public final BooleanSetting breakOnlyHasCrystal = this.add(new BooleanSetting("OnlyHoldItem", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    public final SliderSetting switchCooldown = this.add(new SliderSetting("SwitchPause", 100, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 12.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    public final SliderSetting updateDelay = this.add(new SliderSetting("UpdateDelay", 50, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    public final SliderSetting wallRange = this.add(new SliderSetting("WallRange", 6.0, 0.0, 6.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    public final SliderSetting lagTime = this.add(new SliderSetting("LagTime", 500, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    private final BooleanSetting render3DUpdate = this.add(new BooleanSetting("Render3DUpdate", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    private final BooleanSetting render3DUpdate2 = this.add(new BooleanSetting("Render3DUpdate2", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    private final BooleanSetting walkingUpdate = this.add(new BooleanSetting("WalkingUpdate", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    private final BooleanSetting render2DUpdate = this.add(new BooleanSetting("Render2DUpdate", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.General));
    public final BooleanSetting waitburrow = this.add(new BooleanSetting("WaitBurrow", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Cancel));
    public final BooleanSetting cancelBurrow = this.add(new BooleanSetting("CancelBurrow", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Cancel));
    public final BooleanSetting waitCleaner = this.add(new BooleanSetting("WaitCleaner", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Cancel));
    public final BooleanSetting cblink = this.add(new BooleanSetting("CancelBlink", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Cancel));
    public final BooleanSetting expcancel = this.add(new BooleanSetting("ExpCancel", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Cancel));
    public final BooleanSetting place = this.add(new BooleanSetting("Place", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final EnumSetting<Enum_rNhWITNdkrqkhKfDZgGo> autoSwap = this.add(new EnumSetting<Enum_rNhWITNdkrqkhKfDZgGo>("AutoSwap", Enum_rNhWITNdkrqkhKfDZgGo.Off, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final SliderSetting minDamage = this.add(new SliderSetting("MinDamage", 6.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final SliderSetting maxSelf = this.add(new SliderSetting("MaxSelfDmg", 12.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 0.0, 6.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final SliderSetting noSuicide = this.add(new SliderSetting("NoSuicide", 3.0, 0.0, 10.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final BooleanSetting replace = this.add(new BooleanSetting("Replace", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 300, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final SliderSetting ingionPiston = this.add(new SliderSetting("IngionPiston", 8.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final SliderSetting yOffset = this.add(new SliderSetting("YOffset", 0, -1, 1, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final SliderSetting expand = this.add(new SliderSetting("Expand", 0.5, 0.0, 1.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final BooleanSetting ObbyVector = this.add(new BooleanSetting("ObbyVector", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Place));
    public final BooleanSetting Break = this.add(new BooleanSetting("Break", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Break));
    public final SliderSetting breakMinDmg = this.add(new SliderSetting("BreakMin", 2.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Break));
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
    public final EnumSetting<Enum> rotateMode = this.add(new EnumSetting<Enum>("RotateMode", Enum.OffTrack, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Rotation));
    public final BooleanSetting obsidian = this.add(new BooleanSetting("Obsidian", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Obsidian));
    public final SliderSetting placeObsDelay = this.add(new SliderSetting("PlaceObsDelay", 500, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Obsidian));
    public final BooleanSetting smart = this.add(new BooleanSetting("Smart", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations));
    public final BooleanSetting useThread = this.add(new BooleanSetting("UseThread", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations));
    public final BooleanSetting doCrystal = this.add(new BooleanSetting("CalcCrystal", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations));
    public final BooleanSetting lite = this.add(new BooleanSetting("LiteCheck", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations));
    public final EnumSetting<Enum_IKgLeKHCELPvcpdGlLhV> calcMode = this.add(new EnumSetting<Enum_IKgLeKHCELPvcpdGlLhV>("CalcMode", Enum_IKgLeKHCELPvcpdGlLhV.OyVey, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations));
    public final SliderSetting calcdelay = this.add(new SliderSetting("CalcDelay", 500, 0, 1000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Calculations));
    public final BooleanSetting slowPlace = this.add(new BooleanSetting("FacePlace", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace));
    public final SliderSetting slowDelay = this.add(new SliderSetting("FacePlaceDelay", 600, 0, 2000, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace));
    public final SliderSetting slowMinDamage = this.add(new SliderSetting("FaceMin", 1.5, 0.0, 36.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace));
    public final BooleanSetting forcePlace = this.add(new BooleanSetting("HealthPlace", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace));
    public final SliderSetting forceMaxHealth = this.add(new SliderSetting("MaxHealth", 7, 0, 36, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace));
    public final SliderSetting forceMin = this.add(new SliderSetting("HealthMin", 1.5, 0.0, 36.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace));
    public final BooleanSetting armorBreaker = this.add(new BooleanSetting("ArmorBreaker", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace));
    public final SliderSetting maxDurable = this.add(new SliderSetting("MaxDurable", 8, 0, 100, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace));
    public final SliderSetting armorBreakerDamage = this.add(new SliderSetting("MinDurable", 3.0, 0.0, 36.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.ForcePlace));
    public final BooleanSetting antiSurround = this.add(new BooleanSetting("AntiSurround", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.AntiSurround));
    public final SliderSetting antiSurroundMax = this.add(new SliderSetting("WhenLower", 5.0, 0.0, 36.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.AntiSurround));
    public final SliderSetting antiSurroundProgress = this.add(new SliderSetting("Progress", 0.8, 0.0, 1.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.AntiSurround));
    public final SliderSetting HurtTime = this.add(new SliderSetting("SyncTime", 10.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final SliderSetting SyncTime = this.add(new SliderSetting("LastSync", 8.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final BooleanSetting WebSync = this.add(new BooleanSetting("WebDamage", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final SliderSetting WebMinDamage = this.add(new SliderSetting("WebMinDmg", 5.0, 0.0, 20.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final BooleanSetting SyncTerrainIgnore = this.add(new BooleanSetting("TerrainIgnore", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final BooleanSetting WebdeSync = this.add(new BooleanSetting("DeSyncWeb", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final BooleanSetting breaktime = this.add(new BooleanSetting("BreakTimeSync", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final BooleanSetting posSync = this.add(new BooleanSetting("PosSync", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final BooleanSetting forceWeb = this.add(new BooleanSetting("SyncTick", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final BooleanSetting obbysync = this.add(new BooleanSetting("ObbySync", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final BooleanSetting OnlySync = this.add(new BooleanSetting("OnlySync", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final SliderSetting OnlySyncTime = this.add(new SliderSetting("OnlySyncTime", 10.0, 0.0, 10.0, 0.1, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final SliderSetting SpamSyncTime = this.add(new SliderSetting("SpamSyncTime", 100.0, 0.0, 1000.0, 1.0, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final BooleanSetting syncdebug = this.add(new BooleanSetting("SyncDebug", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.SyncHronize));
    public final EnumSetting<Aura_nurTqHTNjexQmuWdDgIn> mode = this.add(new EnumSetting<Aura_nurTqHTNjexQmuWdDgIn>("TargetESP", Aura_nurTqHTNjexQmuWdDgIn.Jello, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render));
    public final ColorSetting color = this.add(new ColorSetting("TargetColor", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render));
    public final ColorSetting text = this.add(new ColorSetting("Text", new Color(-1), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false));
    public final ColorSetting misscatext = this.add(new ColorSetting("MissText", new Color(-1), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(true));
    public final ColorSetting nullpostext = this.add(new ColorSetting("NullposText", new Color(-1), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(true));
    public final ColorSetting spamtext = this.add(new ColorSetting("SpamText", new Color(-1), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(true));
    public final BooleanSetting render = this.add(new BooleanSetting("Render", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render));
    public final BooleanSetting shrink = this.add(new BooleanSetting("Shrink", true, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue()));
    public final ColorSetting box = this.add(new ColorSetting("Box", new Color(255, 255, 255, 255), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue()).injectBoolean(true));
    public final SliderSetting lineWidth = this.add(new SliderSetting("LineWidth", 1.5, 0.01, 3.0, 0.01, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue()));
    public final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(255, 255, 255, 100), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue()).injectBoolean(true));
    public final SliderSetting sliderSpeed = this.add(new SliderSetting("SliderSpeed", 0.2, 0.01, 1.0, 0.01, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue()));
    public final SliderSetting startFadeTime = this.add(new SliderSetting("StartFade", 0.3, 0.0, 5.0, 0.01, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue()).setSuffix("s"));
    public final SliderSetting fadeSpeed = this.add(new SliderSetting("FadeSpeed", 0.2, 0.01, 1.0, 0.01, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render && this.render.getValue()));
    public final ColorSetting online = new ColorSetting("Online", new Color(255, 255, 255, 255), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(true);
    public final BooleanSetting bold = this.add(new BooleanSetting("Bold", false, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render));
    public final EnumSetting<AutoCrystal_ohSMJidwaoXtIVckTOpo> colortype = this.add(new EnumSetting<AutoCrystal_ohSMJidwaoXtIVckTOpo>("ColorType", AutoCrystal_ohSMJidwaoXtIVckTOpo.Custom, v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render));
    public final ColorSetting showCB_A = this.add(new ColorSetting("ShowCB_A", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false));
    public final ColorSetting showCB_D = this.add(new ColorSetting("ShowCB_D", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false));
    public final ColorSetting showIf = this.add(new ColorSetting("ShowIf", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false));
    public final ColorSetting showif2 = this.add(new ColorSetting("ShowIf2", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false));
    public final ColorSetting showCleaner = this.add(new ColorSetting("ShowCleaner", new Color(255, 255, 255, 250), v -> this.page.getValue() == Enum_sBhvBqKgHyCqkGvharVr.Render).injectBoolean(false));
    private final Timer delayTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer noPosTimer = new Timer();
    private final Timer switchTimer = new Timer();
    private final Timer lagTimer = new Timer();
    private final Timer onlysynctime = new Timer();
    private final List<AutoCrystal_DyfHylndhLrmDUsYPHRl> idPredictQueue = new ArrayList<AutoCrystal_DyfHylndhLrmDUsYPHRl>();
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
        HexTech.EVENT_BUS.subscribe(new AutoCrystal(this));
    }

    @Override
    public String getInfo() {
        if (this.displayTarget != null && this.lastDamage > 0.0f) {
            return this.displayTarget.method_5477().getString() + ", " + new DecimalFormat("0.0").format(this.lastDamage);
        }
        return null;
    }

    private boolean shouldReturn() {
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player != null && this.eatingPause.getValue() && AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6115()) {
            this.lastBreakTimer.reset();
            return true;
        }
        if (this.preferAnchor.getValue() && AutoAnchor_MDcwoWYRcPYheLZJWRZK.currentPos != null) {
            this.lastBreakTimer.reset();
            return true;
        }
        return false;
    }

    private void doInteract() {
        if (this.shouldReturn()) {
            return;
        }
        if (breakPos != null) {
            this.doBreak(breakPos);
            breakPos = null;
        }
        if (crystalPos != null) {
            this.doCrystal(crystalPos);
        }
    }

    @Override
    public void onEnable() {
        lastYaw = HexTech.ROTATE.lastYaw;
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
        lastYaw = HexTech.ROTATE.lastYaw;
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
            ArrayList<AutoCrystal_DyfHylndhLrmDUsYPHRl> remove = new ArrayList<AutoCrystal_DyfHylndhLrmDUsYPHRl>();
            for (AutoCrystal_DyfHylndhLrmDUsYPHRl t : this.idPredictQueue) {
                if (System.currentTimeMillis() < t.executeAt) continue;
                this.sendAttackPacket(t.crystalId, t.crystalPos);
                remove.add(t);
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
        ArrayList<AutoCrystal_DyfHylndhLrmDUsYPHRl> remove = new ArrayList<AutoCrystal_DyfHylndhLrmDUsYPHRl>();
        for (AutoCrystal_DyfHylndhLrmDUsYPHRl t : this.idPredictQueue) {
            if (System.currentTimeMillis() < t.executeAt) continue;
            this.sendAttackPacket(t.crystalId, t.crystalPos);
            remove.add(t);
        }
        this.idPredictQueue.removeAll(remove);
    }

    private void sendAttackPacket(int id, Vec3d pos) {
        Entity ent;
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.world == null) {
            return;
        }
        PlayerInteractEntityC2SPacket packet = null;
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player != null) {
            packet = PlayerInteractEntityC2SPacket.attack((Entity)AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player, (boolean)AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_5715());
        }
        if (packet != null) {
            ((IInteractEntityC2SPacket)packet).setId(id);
        }
        mc.getNetworkHandler().method_52787(packet);
        if (this.breakRemove.getValue() && (ent = AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.world.method_8469(id)) instanceof EndCrystalEntity) {
            AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.world.removeEntity(id, Entity.RemovalReason.KILLED);
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
            this.doRender(matrixStack, mc.method_1488(), (Entity)this.displayTarget, this.mode.getValue());
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
            Aura.doRender(matrixStack, partialTicks, (Entity)this.displayTarget, this.color.getValue(), this.mode.getValue());
        }
    }

    @EventHandler
    public void onRotate(RotateEvent event) {
        if (this.rotate.getValue() && this.directionVec != null && !this.noPosTimer.passed(CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.maxrotateTime.getValue())) {
            if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.rotateMode.getValue() == Enum.Inject) {
                float[] newAngle = InjectRotate.injectStep(EntityUtil.getLegitRotations(this.directionVec), CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.injectstep.getValueFloat());
                lastYaw = newAngle[0];
                lastPitch = newAngle[1];
                if (CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.random.getValue() && new Random().nextBoolean()) {
                    lastPitch = Math.min(new Random().nextFloat() * 2.0f + lastPitch, 90.0f);
                }
                event.setYaw(lastYaw);
                event.setPitch(lastPitch);
            } else if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.rotateMode.getValue() == Enum.OffTrack) {
                if (this.offTackStep.getValue()) {
                    OffTrackEvent offTrackEvent = new OffTrackEvent();
                    offTrackEvent.setTarget(this.directionVec, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.offstep.getValueFloat(), 1.0f);
                    HexTech.EVENT_BUS.post(offTrackEvent);
                    if (offTrackEvent.getTarget() != null || offTrackEvent.getRotation()) {
                        float[] newAngle = HexTech.ROTATE.offtrackStep(this.directionVec, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.offstep.getValueFloat());
                        lastYaw = newAngle[0];
                        lastPitch = newAngle[1];
                        event.setYaw(lastYaw);
                        event.setPitch(lastPitch);
                        return;
                    }
                }
                float[] newAngle = HexTech.ROTATE.offtrackStep(this.directionVec, CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.offstep.getValueFloat());
                lastYaw = newAngle[0];
                lastPitch = newAngle[1];
                event.setYaw(lastYaw);
                event.setPitch(lastPitch);
            }
        } else {
            lastYaw = HexTech.ROTATE.lastYaw;
            lastPitch = RotateManager.lastPitch;
            event.setYaw(lastYaw);
            event.setPitch(lastPitch);
        }
    }

    @EventHandler(priority=-199)
    public void onPacketSend(PacketEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket) {
            this.switchTimer.reset();
        }
    }

    private void updateCrystalPos() {
        this.update();
        this.lastDamage = this.tempDamage;
        crystalPos = tempPos;
    }

    private void update() {
        float selfDamage;
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.nullCheck()) {
            return;
        }
        if (this.obbysync.getValue() && BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.tempPos != null) {
            crystalPos = null;
            tempPos = null;
            return;
        }
        if (!this.lagTimer.passedMs(this.lagTime.getValueInt())) {
            return;
        }
        if (this.eatingPause.getValue() && EntityUtil.isUsing()) {
            tempPos = null;
            return;
        }
        if (this.waitburrow.getValue() && Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.placePos == null) {
            crystalPos = null;
            tempPos = null;
        }
        if (this.cancelBurrow.getValue() && Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.isOn()) {
            tempPos = null;
            return;
        }
        if (this.waitCleaner.getValue() && Cleaner_iFwqnooxsJEmHoVteFeQ.INSTANCE.syncPos != null) {
            crystalPos = null;
            tempPos = null;
        }
        if (this.cblink.getValue() && Blink.INSTANCE.isOn()) {
            this.lastBreakTimer.reset();
            tempPos = null;
            return;
        }
        if (this.expcancel.getValue() && AutoEXP.INSTANCE.isOn()) {
            this.lastBreakTimer.reset();
            tempPos = null;
            return;
        }
        if (!this.delayTimer.passedMs((long)this.updateDelay.getValue())) {
            return;
        }
        if (this.preferpiston.getValue() && PistonCrystal.INSTANCE.isOn()) {
            this.lastBreakTimer.reset();
            tempPos = null;
            return;
        }
        if (this.WebdeSync.getValue()) {
            crystalPos = null;
            tempPos = null;
        }
        if (this.breaktime.getValue()) {
            this.lastBreakTimer.reset();
        }
        if (this.posSync.getValue()) {
            tempPos = null;
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player != null && this.breakOnlyHasCrystal.getValue() && !AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6047().isOf(Items.END_CRYSTAL) && !AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6079().isOf(Items.END_CRYSTAL) && !this.findCrystal()) {
            this.lastBreakTimer.reset();
            tempPos = null;
            return;
        }
        if (!this.switchTimer.passedMs((long)this.switchCooldown.getValue())) {
            tempPos = null;
            return;
        }
        if (this.shouldReturn()) {
            return;
        }
        this.delayTimer.reset();
        tempPos = null;
        this.tempDamage = 0.0f;
        breakPos = null;
        this.breakDamage = 0.0f;
        ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS> targets = new ArrayList<PredictionSetting._XBpBEveLWEKUGQPHCCIS>();
        for (PlayerEntity target : CombatUtil.getEnemies(this.targetRange.getValueFloat())) {
            if (target.field_6235 > this.HurtTime.getValueInt()) continue;
            targets.add(new PredictionSetting._XBpBEveLWEKUGQPHCCIS(target));
        }
        if (targets.isEmpty()) {
            this.lastBreakTimer.reset();
            return;
        }
        PredictionSetting._XBpBEveLWEKUGQPHCCIS self = new PredictionSetting._XBpBEveLWEKUGQPHCCIS((PlayerEntity)AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player);
        for (BlockPos pos : BlockUtil.getSphere((float)this.range.getValue() + 1.0f)) {
            if (WallCheck.behindWall(pos) || AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_33571().distanceTo(pos.toCenterPos().add(0.0, -0.5, 0.0)) > this.range.getValue() || !this.canTouch(pos.down()) || !CanPlaceCrystal.canPlaceCrystal(pos, true, false)) continue;
            for (PredictionSetting._XBpBEveLWEKUGQPHCCIS target : targets) {
                float selfDamage2;
                if (this.lite.getValue() && ListenerHelperUtil.liteCheck(pos.toCenterPos().add(0.0, -0.5, 0.0), target.predict.method_19538())) continue;
                int placeTicks = (int)PredictionSetting.INSTANCE.placeExtrap.getValue();
                PlayerEntity placePredict = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createPredict(target.player, placeTicks, (int)PredictionSetting.INSTANCE.extrapTicks.getValue());
                float damage = CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateDamage(pos, placePredict, placePredict);
                if (tempPos != null && !(damage > this.tempDamage) || (double)(selfDamage2 = CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateDamage(pos, self.player, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createSelfPredict(self.player, (int)PredictionSetting.INSTANCE.selfExtrap.getValue()))) > this.maxSelf.getValue() || this.noSuicide.getValue() > 0.0 && (double)selfDamage2 > (double)(AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6032() + AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6067()) - this.noSuicide.getValue() || (double)damage < this.getDamage(target.player) || this.smart.getValue() && damage < selfDamage2) continue;
                this.displayTarget = target.player;
                tempPos = pos;
                this.tempDamage = damage;
            }
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.world != null) {
            for (Entity entity : AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.world.getEntities()) {
                EndCrystalEntity crystal;
                if (!(entity instanceof EndCrystalEntity) || !AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6057((Entity)(crystal = (EndCrystalEntity)entity)) && AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_33571().distanceTo(crystal.method_19538()) > this.wallRange.getValue() || AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_33571().distanceTo(crystal.method_19538()) > this.range.getValue()) continue;
                for (PredictionSetting._XBpBEveLWEKUGQPHCCIS target : targets) {
                    int breakTicks = (int)PredictionSetting.INSTANCE.breakExtrap.getValue();
                    PlayerEntity breakPredict = ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createPredict(target.player, breakTicks, (int)PredictionSetting.INSTANCE.extrapTicks.getValue());
                    float damage = CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateDamage(BlockPos.ofFloored((Position)crystal.method_19538()), breakPredict, breakPredict);
                    if (breakPos != null && !(damage > this.breakDamage) || (double)(selfDamage = CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateDamage(BlockPos.ofFloored((Position)crystal.method_19538()), self.player, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createSelfPredict(self.player, (int)PredictionSetting.INSTANCE.selfExtrap.getValue()))) > this.maxSelf.getValue() || this.noSuicide.getValue() > 0.0 && (double)selfDamage > (double)(AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6032() + AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6067()) - this.noSuicide.getValue() || (double)damage < this.breakMinDmg.getValue()) continue;
                    breakPos = crystal.method_24515();
                    if (!(damage > this.tempDamage)) continue;
                    this.displayTarget = target.player;
                }
            }
        }
        if (this.antiSurround.getValue() && SpeedMine.breakPos != null && SpeedMine.progress >= this.antiSurroundProgress.getValue() && !BlockUtil.hasEntity(SpeedMine.breakPos, false) && this.tempDamage <= this.antiSurroundMax.getValueFloat()) {
            for (PredictionSetting._XBpBEveLWEKUGQPHCCIS target : targets) {
                for (Direction dir : Direction.values()) {
                    BlockPos crystalPos;
                    BlockPos offsetPos;
                    if (dir == Direction.DOWN || dir == Direction.UP || !(offsetPos = target.player.method_24515().offset(dir)).equals((Object)SpeedMine.breakPos) || !CanPlaceCrystal.canPlaceCrystal(crystalPos = offsetPos.offset(dir), false, false) || !((double)(selfDamage = CrystalDamage_eJITUTNYpCPnjaYYZUHH.calculateDamage(crystalPos, self.player, ExtrapolationUtil_PeyhWPRKVrDcYEjSgxgn.createSelfPredict(self.player, (int)PredictionSetting.INSTANCE.selfExtrap.getValue()))) < this.maxSelf.getValue()) || this.noSuicide.getValue() > 0.0 && (double)selfDamage > (double)(AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6032() + AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6067()) - this.noSuicide.getValue()) continue;
                    tempPos = crystalPos;
                    if (this.doCrystal.getValue()) {
                        this.doCrystal(tempPos);
                    }
                    return;
                }
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean canTouch(BlockPos pos) {
        Direction side = BlockUtil.getClickSideStrict(pos);
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player == null) return false;
        if (side == null) return false;
        Vec3d vec3d = new Vec3d((double)side.method_10163().getX() * 0.5, (double)side.method_10163().getY() * 0.5, (double)side.method_10163().getZ() * 0.5);
        if (!(pos.toCenterPos().add(vec3d).distanceTo(AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_33571()) <= this.range.getValue())) return false;
        return true;
    }

    public void doCrystal(BlockPos pos) {
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.eatingPause.getValue() && EntityUtil.isUsing()) {
            return;
        }
        if (CanPlaceCrystal.canPlaceCrystal(pos, false, true)) {
            if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player != null && (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6047().getItem().equals(Items.END_CRYSTAL) || AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6079().getItem().equals(Items.END_CRYSTAL) || this.findCrystal())) {
                this.doPlace(pos);
            }
        } else {
            this.doBreak(pos);
        }
    }

    private double getDamage(PlayerEntity target) {
        if (!SpeedMine.INSTANCE.obsidian.isPressed() && this.slowPlace.getValue() && this.lastBreakTimer.passedMs((long)this.slowDelay.getValue()) && (!BedAura_BzCWaQEhnpenizjBqrRp.INSTANCE.isOn() || BedAura_BzCWaQEhnpenizjBqrRp.INSTANCE.getBed() == -1)) {
            return this.slowMinDamage.getValue();
        }
        if (this.forcePlace.getValue() && (double)EntityUtil.getHealth((Entity)target) <= this.forceMaxHealth.getValue() && !SpeedMine.INSTANCE.obsidian.isPressed()) {
            return this.forceMin.getValue();
        }
        if (this.armorBreaker.getValue()) {
            DefaultedList armors = target.getInventory().armor;
            for (ItemStack armor : armors) {
                if (armor.isEmpty() || (double)EntityUtil.getDamagePercent(armor) > this.maxDurable.getValue()) continue;
                return this.armorBreakerDamage.getValue();
            }
        }
        if (PistonCrystal.INSTANCE.isOn()) {
            return this.ingionPiston.getValueFloat();
        }
        return this.minDamage.getValue();
    }

    public boolean findCrystal() {
        if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Off) {
            return false;
        }
        return this.getCrystal() != -1;
    }

    private void doBreak(BlockPos pos) {
        this.noPosTimer.reset();
        if (!this.Break.getValue()) {
            return;
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.eatingPause.getValue() && EntityUtil.isUsing()) {
            return;
        }
        if (this.OnlySync.getValue() && this.displayTarget != null && this.displayTarget.field_6235 > this.OnlySyncTime.getValueInt() && !this.onlysynctime.passed(this.SpamSyncTime.getValue())) {
            return;
        }
        this.lastBreakTimer.reset();
        if (!this.switchTimer.passedMs((long)this.switchCooldown.getValue())) {
            return;
        }
        this.onlysynctime.reset();
        for (EndCrystalEntity entity : BlockUtil.getEndCrystals(new Box((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)(pos.method_10264() + 2), (double)(pos.method_10260() + 1)))) {
            if (entity.field_6012 < this.minAge.getValueInt()) continue;
            if (this.rotate.getValue() && this.onBreak.getValue() && !this.faceVector(entity.method_19538().add(0.0, this.yOffset.getValue(), 0.0))) {
                return;
            }
            if (!CombatUtil.breakTimer.passedMs((long)this.breakDelay.getValue())) {
                return;
            }
            CombatUtil.breakTimer.reset();
            syncPos = pos;
            if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player != null) {
                Objects.requireNonNull(mc.getNetworkHandler()).method_52787((Packet)PlayerInteractEntityC2SPacket.attack((Entity)entity, (boolean)AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_5715()));
            }
            if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player != null) {
                AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_7350();
            }
            EntityUtil.swingHand(Hand.MAIN_HAND, this.swingMode.getValue());
            if (this.breakRemove.getValue() && AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.world != null) {
                AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.world.removeEntity(entity.method_5628(), Entity.RemovalReason.KILLED);
            }
            if (crystalPos != null && this.displayTarget != null && (double)this.lastDamage >= this.getDamage(this.displayTarget) && this.instant.getValue() && (!this.rotate.getValue() || this.rotateMode.getValue() == Enum.OffTrack)) {
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

    private void doPlace(BlockPos pos) {
        this.noPosTimer.reset();
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.INSTANCE.eatingPause.getValue() && EntityUtil.isUsing()) {
            return;
        }
        if (!this.place.getValue()) {
            return;
        }
        if (!(AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player == null || AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6047().getItem().equals(Items.END_CRYSTAL) || AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6079().getItem().equals(Items.END_CRYSTAL) || this.findCrystal())) {
            return;
        }
        if (!this.canTouch(pos.down())) {
            return;
        }
        BlockPos obsPos = pos.down();
        Direction facing = BlockUtil.getClickSide(obsPos);
        Vec3d vec = obsPos.toCenterPos().add((double)facing.method_10163().getX() * 0.5, (double)facing.method_10163().getY() * 0.5, (double)facing.method_10163().getZ() * 0.5);
        if (facing != Direction.UP && facing != Direction.DOWN) {
            vec = vec.add(0.0, 0.45, 0.0);
        }
        if (this.rotate.getValue() && !this.faceVector(vec)) {
            return;
        }
        if (!this.placeTimer.passedMs((long)this.placeDelay.getValue())) {
            return;
        }
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6047().getItem().equals(Items.END_CRYSTAL) || AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6079().getItem().equals(Items.END_CRYSTAL)) {
            this.placeTimer.reset();
            syncPos = pos;
            this.placeCrystal(pos);
        } else {
            this.placeTimer.reset();
            syncPos = pos;
            int old = AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_31548().selectedSlot;
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
        }
    }

    private void doSwap(int slot) {
        if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Silent || this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Normal) {
            InventoryUtil.switchToSlot(slot);
        } else if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory && AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player != null) {
            InventoryUtil.inventorySwap(slot, AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_31548().selectedSlot);
        }
    }

    private int getCrystal() {
        if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Silent || this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Normal) {
            return InventoryUtil.findItem(Items.END_CRYSTAL);
        }
        if (this.autoSwap.getValue() == Enum_rNhWITNdkrqkhKfDZgGo.Inventory) {
            return InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL);
        }
        return -1;
    }

    public void placeCrystal(BlockPos pos) {
        boolean offhand = false;
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player != null) {
            offhand = AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.player.method_6079().getItem() == Items.END_CRYSTAL;
        }
        BlockPos obsPos = pos.down();
        Direction facing = BlockUtil.getClickSide(obsPos);
        BlockUtil.clickBlock(obsPos, facing, false, offhand ? Hand.OFF_HAND : Hand.MAIN_HAND, this.swingMode.getValue());
        if (PredictionSetting.INSTANCE.idPredict.getValue()) {
            int highest = this.getHighestEntityId();
            int startId = highest + PredictionSetting.INSTANCE.idStartOffset.getValueInt();
            for (int i = 0; i < PredictionSetting.INSTANCE.idPackets.getValueInt(); ++i) {
                int cid = startId + i * PredictionSetting.INSTANCE.idPacketOffset.getValueInt();
                long delay = (long)(PredictionSetting.INSTANCE.idStartDelay.getValue() + (double)i * PredictionSetting.INSTANCE.idPacketDelay.getValue());
                this.idPredictQueue.add(new AutoCrystal_DyfHylndhLrmDUsYPHRl(cid, pos.toCenterPos().add(0.0, 1.0, 0.0), System.currentTimeMillis() + delay));
            }
        }
    }

    private int getHighestEntityId() {
        int max = this.lastConfirmedId;
        if (AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.world != null) {
            for (Entity e : AutoCrystal_QcRVYRsOqpKivetoXSJa.mc.world.getEntities()) {
                if (e.method_5628() <= max) continue;
                max = e.method_5628();
            }
        }
        this.lastConfirmedId = max;
        return max;
    }

    private boolean faceVector(Vec3d directionVec) {
        if (!this.faceVector.getValue()) {
            RotateManager.TrueVec3d(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        if (HexTech.ROTATE.inFov(directionVec, this.fov.getValueFloat())) {
            return true;
        }
        return !this.checkLook.getValue();
    }
}
