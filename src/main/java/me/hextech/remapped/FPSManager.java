package me.hextech.remapped;

import java.util.ArrayList;
import java.util.List;

public class FPSManager {
   private final List<Long> records = new ArrayList();

   public void record() {
      this.records.add(System.currentTimeMillis());
   }

   public int getFps() {
      this.records.removeIf(aLong -> aLong + 1000L < System.currentTimeMillis());
      return this.records.size();
   }
}
