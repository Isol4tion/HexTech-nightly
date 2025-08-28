package me.hextech.remapped;

import me.hextech.remapped.api.events.eventbus.EventHandler;
import me.hextech.remapped.api.utils.render.AnimateUtil;

public class Zoom
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Zoom INSTANCE;
    public static boolean on;
    final SliderSetting animSpeed = this.add(new SliderSetting("AnimSpeed", 0.1, 0.0, 1.0, 0.01));
    final SliderSetting fov = this.add(new SliderSetting("Fov", 60.0, -130.0, 130.0, 1.0));
    public double currentFov;

    public Zoom() {
        super("Zoom", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
        //HexTech.EVENT_BUS.subscribe(new Zoom());
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (this.isOn()) {
            this.currentFov = AnimateUtil.animate(this.currentFov, this.fov.getValue(), this.animSpeed.getValue());
            Zoom.on = true;
        } else if (Zoom.on) {
            this.currentFov = AnimateUtil.animate(this.currentFov, 0.0, this.animSpeed.getValue());
            if ((int)this.currentFov == 0) {
                Zoom.on = false;
            }
        }
    }
    
    @Override
    public void onEnable() {
        if (Zoom.mc.options.getFov().getValue() == 70) {
            Zoom.mc.options.getFov().setValue(71);
        }
    }
}
