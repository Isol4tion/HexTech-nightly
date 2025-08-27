package me.hextech.remapped;

import java.util.Arrays;
import java.util.List;
import me.hextech.remapped.Command;
import me.hextech.remapped.HUD_ssNtBhEveKlCmIccBvAN;

public class WatermarkCommand
extends Command {
    public WatermarkCommand() {
        super("watermark", "change watermark", "[text]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length == 0) {
            this.sendUsage();
            return;
        }
        StringBuilder text = new StringBuilder();
        for (String s : Arrays.stream(parameters).toList()) {
            text.append(" ").append(s);
        }
        HUD_ssNtBhEveKlCmIccBvAN.INSTANCE.watermarkString.setValue(text.toString());
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}
