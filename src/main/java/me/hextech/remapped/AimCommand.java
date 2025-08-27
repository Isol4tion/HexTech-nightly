package me.hextech.remapped;

import java.text.DecimalFormat;
import java.util.List;
import me.hextech.remapped.Command;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.EntityUtil;
import net.minecraft.util.math.Vec3d;

public class AimCommand
extends Command {
    public AimCommand() {
        super("aim", "Aim to pos", "[x] [y] [z]");
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void runCommand(String[] parameters) {
        double z;
        double y;
        double x;
        if (parameters.length != 3) {
            this.sendUsage();
            return;
        }
        if (this.isNumeric(parameters[0])) {
            x = Double.parseDouble(parameters[0]);
        } else {
            if (!parameters[0].startsWith("~")) {
                this.sendUsage();
                return;
            }
            if (this.isNumeric(parameters[0].replace("~", ""))) {
                x = AimCommand.mc.field_1724.method_23317() + Double.parseDouble(parameters[0].replace("~", ""));
            } else {
                if (!parameters[0].replace("~", "").equals("")) {
                    this.sendUsage();
                    return;
                }
                x = AimCommand.mc.field_1724.method_23317();
            }
        }
        if (this.isNumeric(parameters[1])) {
            y = Double.parseDouble(parameters[1]);
        } else {
            if (!parameters[1].startsWith("~")) {
                this.sendUsage();
                return;
            }
            if (this.isNumeric(parameters[1].replace("~", ""))) {
                y = AimCommand.mc.field_1724.method_23318() + Double.parseDouble(parameters[1].replace("~", ""));
            } else {
                if (!parameters[1].replace("~", "").equals("")) {
                    this.sendUsage();
                    return;
                }
                y = AimCommand.mc.field_1724.method_23318();
            }
        }
        if (this.isNumeric(parameters[2])) {
            z = Double.parseDouble(parameters[2]);
        } else {
            if (!parameters[2].startsWith("~")) {
                this.sendUsage();
                return;
            }
            if (this.isNumeric(parameters[2].replace("~", ""))) {
                z = AimCommand.mc.field_1724.method_23321() + Double.parseDouble(parameters[2].replace("~", ""));
            } else {
                if (!parameters[2].replace("~", "").equals("")) {
                    this.sendUsage();
                    return;
                }
                z = AimCommand.mc.field_1724.method_23321();
            }
        }
        float[] angle = EntityUtil.getLegitRotations(new Vec3d(x, y, z));
        AimCommand.mc.field_1724.method_36456(angle[0]);
        AimCommand.mc.field_1724.method_36457(angle[1]);
        DecimalFormat df = new DecimalFormat("0.0");
        CommandManager.sendChatMessage("\u00a7a[\u221a] \u00a7fAim to \u00a7eX:" + df.format(x) + " Y:" + df.format(y) + " Z:" + df.format(z));
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return new String[]{"~ "};
    }
}
