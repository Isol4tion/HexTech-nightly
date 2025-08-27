package me.hextech.remapped;

public class ThreadManager extends Thread {
   public void run() {
      while (!Thread.currentThread().isInterrupted()) {
         try {
            if (me.hextech.HexTech.MODULE != null) {
               me.hextech.HexTech.MODULE.onThread();
            }
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }
   }
}
