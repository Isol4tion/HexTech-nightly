package me.hextech.remapped;

public class ThreadManager_BMAJbvVqHsNRgpJZHNrD {
   public static ThreadManager clientService;

   public ThreadManager_BMAJbvVqHsNRgpJZHNrD() {
      me.hextech.HexTech.EVENT_BUS.subscribe(this);
      clientService = new ThreadManager();
      clientService.setName("HexTech");
      clientService.setDaemon(true);
      clientService.start();
   }

   @EventHandler(
      priority = 200
   )
   public void onEvent(TickEvent event) {
      if (event.isPre()) {
         if (!clientService.isAlive()) {
            clientService = new ThreadManager();
            clientService.setName("HexTech");
            clientService.setDaemon(true);
            clientService.start();
         }

         BlockUtil.placedPos.forEach(pos -> PlaceRender.renderMap.put(pos, PlaceRender.INSTANCE.create(pos)));
         BlockUtil.placedPos.clear();
         me.hextech.HexTech.SERVER.onUpdate();
         me.hextech.HexTech.MODULE.onUpdate();
         me.hextech.HexTech.GUI.update();
         me.hextech.HexTech.POP.update();
      }
   }
}
