package me.hextech.remapped;

import me.hextech.HexTech;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Zoom;

public class Zoom_qxASoURSmqLSKrnTPdNq
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public static Zoom_qxASoURSmqLSKrnTPdNq INSTANCE;
    public static boolean on;
    final SliderSetting animSpeed = this.add(new SliderSetting("AnimSpeed", 0.1, 0.0, 1.0, 0.01));
    final SliderSetting fov = this.add(new SliderSetting("Fov", 60.0, -130.0, 130.0, 1.0));
    public double currentFov;

    public Zoom_qxASoURSmqLSKrnTPdNq() {
        super("Zoom", Module_JlagirAibYQgkHtbRnhw.Render);
        INSTANCE = this;
        HexTech.EVENT_BUS.subscribe(new Zoom(this));
    }

    @Override
    public void onEnable() {
        if ((Integer)Zoom_qxASoURSmqLSKrnTPdNq.mc.field_1690.method_41808().method_41753() == 70) {
            Zoom_qxASoURSmqLSKrnTPdNq.mc.field_1690.method_41808().method_41748((Object)71);
        }
    }
}
