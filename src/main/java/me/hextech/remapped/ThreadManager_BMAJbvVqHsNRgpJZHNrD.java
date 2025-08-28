package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.BlockUtil;
import me.hextech.remapped.EventHandler;
import me.hextech.remapped.PlaceRender;
import me.hextech.remapped.ThreadManager;
import me.hextech.remapped.TickEvent;
import net.minecraft.util.math.BlockPos;

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
}
