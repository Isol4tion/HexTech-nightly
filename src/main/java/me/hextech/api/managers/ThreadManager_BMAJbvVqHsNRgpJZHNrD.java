package me.hextech.api.managers;

import me.hextech.HexTech;
import me.hextech.api.events.eventbus.EventHandler;
import me.hextech.api.events.impl.TickEvent;
import me.hextech.api.utils.world.BlockUtil;
import me.hextech.mod.modules.impl.render.PlaceRender;

public class ThreadManager_BMAJbvVqHsNRgpJZHNrD {
    public static ThreadManager clientService;

    public ThreadManager_BMAJbvVqHsNRgpJZHNrD() {
        HexTech.EVENT_BUS.subscribe(this);
        clientService = new ThreadManager();
        clientService.setName("HexTech");
        clientService.setDaemon(true);
        clientService.start();
    }

    @EventHandler(priority=200)
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
            HexTech.SERVER.onUpdate();
            HexTech.MODULE.onUpdate();
            HexTech.GUI.update();
            HexTech.POP.update();
        }
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public static class ThreadManager
    extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (HexTech.MODULE == null) continue;
                    HexTech.MODULE.onThread();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
