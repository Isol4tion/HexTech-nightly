package me.hextech.remapped;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

public class BaseFinder extends Module_eSdgMXWuzcxgQVaJFmKZ {
   private static final File basedata;
   private static final File chestdata;
   public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
   private final SliderSetting delay = this.add(new SliderSetting("Delay", 15, 0, 30));
   private final SliderSetting count = this.add(new SliderSetting("Count", 50, 1, 2000));
   private final BooleanSetting log = this.add(new BooleanSetting("SaveChestLog", true));
   private final Timer timer = new Timer();

   public BaseFinder() {
      super("BaseFinder", Module_JlagirAibYQgkHtbRnhw.Misc);
   }

   private static void writePacketData(File file, String data) {
      new Thread(() -> {
         try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(data);
            writer.newLine();
            writer.close();
         } catch (IOException var3) {
         }
      }).start();
   }

   @Override
   public void onRender3D(MatrixStack matrixStack, float partialTicks) {
      for (BlockEntity blockEntity : BlockUtil.getTileEntities()) {
         if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof TrappedChestBlockEntity) {
            Box box = new Box(blockEntity.method_11016());
            Render3DUtil.draw3DBox(matrixStack, box, this.color.getValue());
         }
      }

      if (this.timer.passed((long)this.delay.getValueInt() * 20L)) {
         int chest = 0;

         for (BlockEntity blockEntityx : BlockUtil.getTileEntities()) {
            if (blockEntityx instanceof ChestBlockEntity || blockEntityx instanceof TrappedChestBlockEntity) {
               chest++;
               if (this.log.getValue()) {
                  writePacketData(chestdata, "FindChest:" + blockEntityx.method_11016());
               }
            }
         }

         if ((double)chest >= this.count.getValue()) {
            this.timer.reset();
            writePacketData(basedata, "Find:" + mc.field_1724.method_19538() + " Count:" + chest);
            CommandManager.sendChatMessage("Find:" + mc.field_1724.method_19538() + " Count:" + chest);
            int var9 = false;
         }
      }
   }
}
