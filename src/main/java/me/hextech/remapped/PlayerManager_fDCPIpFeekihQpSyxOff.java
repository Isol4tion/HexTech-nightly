package me.hextech.remapped;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class PlayerManager_fDCPIpFeekihQpSyxOff implements Wrapper {
   public Map<PlayerEntity, PlayerManager> map = new ConcurrentHashMap();
   public CopyOnWriteArrayList<PlayerEntity> inWebPlayers = new CopyOnWriteArrayList();
   public boolean insideBlock = false;

   public void onUpdate() {
      if (!Module_eSdgMXWuzcxgQVaJFmKZ.nullCheck()) {
         this.inWebPlayers.clear();
         this.insideBlock = EntityUtil.isInsideBlock();

         for (PlayerEntity player : new ArrayList(mc.field_1687.method_18456())) {
            this.map.put(player, new PlayerManager(player.method_6096(), player.method_26825(EntityAttributes.field_23725)));
            this.webUpdate(player);
         }
      }
   }

   public boolean isInWeb(PlayerEntity player) {
      return this.inWebPlayers.contains(player);
   }

   private void webUpdate(PlayerEntity player) {
      for (float x : new float[]{0.0F, 0.3F, -0.3F}) {
         for (float z : new float[]{0.0F, 0.3F, -0.3F}) {
            for (int y : new int[]{-1, 0, 1, 2}) {
               BlockPos pos = new BlockPosX(player.method_23317() + (double)x, player.method_23318(), player.method_23321() + (double)z).method_10086(y);
               if (new Box(pos).method_994(player.method_5829()) && mc.field_1687.method_8320(pos).method_26204() == Blocks.field_10343) {
                  this.inWebPlayers.add(player);
                  return;
               }
            }
         }
      }
   }
}
