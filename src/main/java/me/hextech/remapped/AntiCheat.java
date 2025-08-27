package me.hextech.remapped;

import me.hextech.remapped.BooleanSetting;
import me.hextech.remapped.Burrow_eOaBGEoOSTDRbYIUAbXC;
import me.hextech.remapped.BypassSetting_RInKGmTQYgWFRhsUOiJP;
import me.hextech.remapped.CombatSetting_kxXrLvbWbduSuFoeBUsC;
import me.hextech.remapped.CommandManager;
import me.hextech.remapped.EnumSetting;
import me.hextech.remapped.FastWeb_dehcwwTxEbDSnkFtZvNl;
import me.hextech.remapped.Module_JlagirAibYQgkHtbRnhw;
import me.hextech.remapped.Module_eSdgMXWuzcxgQVaJFmKZ;
import me.hextech.remapped.Notify_EXlgYplaRzfgofOPOkyB;
import me.hextech.remapped.SliderSetting;
import me.hextech.remapped.Step_EShajbhvQeYkCdreEeNY;

public class AntiCheat
extends Module_eSdgMXWuzcxgQVaJFmKZ {
    public final EnumSetting<_ylqyOfVuMcYIoHfjTCiI> page = this.add(new EnumSetting<_ylqyOfVuMcYIoHfjTCiI>("Page", _ylqyOfVuMcYIoHfjTCiI.XiaoSong));
    public final SliderSetting xiaosongstepHeight = this.add(new SliderSetting("StepHeight[CN]", 3, 0, 4, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.XiaoSong)));
    public final SliderSetting xinstepHeight = this.add(new SliderSetting("StepHeight[Xin]", 2, 0, 4, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.Xin)));
    private final BooleanSetting CNconfig = this.add(new BooleanSetting("XiaoSong", false, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.XiaoSong)));
    private final BooleanSetting xiaosonginvSwap = this.add(new BooleanSetting("InvSwap[CN]", false, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.XiaoSong)));
    private final BooleanSetting xiaosongfastWeb = this.add(new BooleanSetting("FastWeb[CN]", true, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.XiaoSong)));
    private final BooleanSetting xiaosongfastWebGround = this.add(new BooleanSetting("FastWebGround[CN]", true, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.XiaoSong)));
    private final BooleanSetting xiaosongStepWebPause = this.add(new BooleanSetting("StepWebPauseOn[CN]", true, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.XiaoSong)));
    private final BooleanSetting xiaosongburrowWeb = this.add(new BooleanSetting("WebBurrowCheck[CN]", true, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.XiaoSong)));
    private final BooleanSetting xin = this.add(new BooleanSetting("Xin", false, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.Xin)));
    private final BooleanSetting xininswap = this.add(new BooleanSetting("InvSwap[Xin]", false, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.Xin)));
    private final BooleanSetting xinfastweb = this.add(new BooleanSetting("FastWeb[Xin]", true, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.Xin)));
    private final BooleanSetting xinfastWebGround = this.add(new BooleanSetting("FastWebGround[Xin]", false, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.Xin)));
    private final BooleanSetting xinStepinWebPause = this.add(new BooleanSetting("StepWebPauseOn[Xin]", false, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.Xin)));
    private final BooleanSetting xinburrowWeb = this.add(new BooleanSetting("WebBurrowCheck[Xin]", false, v -> this.page.is(_ylqyOfVuMcYIoHfjTCiI.Xin)));
    private boolean needUpdate = true;

    public AntiCheat() {
        super("AntiCheat", Module_JlagirAibYQgkHtbRnhw.Setting);
    }

    @Override
    public void onLogin() {
        this.needUpdate = true;
    }

    @Override
    public void onUpdate() {
        if (AntiCheat.nullCheck()) {
            return;
        }
        if (!this.needUpdate) {
            return;
        }
        if (mc.isInSingleplayer()) {
            return;
        }
        String server = AntiCheat.mc.player.networkHandler.getServerInfo().address.toLowerCase();
        if (this.xin.getValue()) {
            if (server.equals("2b2t.xin")) {
                CommandManager.sendChatMessage("\u00a76\u914d\u7f6e\u540c\u6b65\uff1a2B2T.Xin");
                Notify_EXlgYplaRzfgofOPOkyB.sendNotify("\u00a76\u914d\u7f6e\u540c\u6b65\uff1a2B2T.Xin");
                Step_EShajbhvQeYkCdreEeNY.INSTANCE.height.setValue(this.xinstepHeight.getValueFloat());
                Step_EShajbhvQeYkCdreEeNY.INSTANCE.inWebPause.setValue(this.xinStepinWebPause.getValue());
                if (this.xininswap.getValue()) {
                    CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.invSwapBypass.setValue(false);
                    BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.SwapSync.setValue(false);
                }
                if (this.xinfastweb.getValue()) {
                    FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.groundcheck.setValue(this.xinfastWebGround.getValue());
                }
                if (this.xinburrowWeb.getValue()) {
                    Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.webcheck.setValue(this.xinburrowWeb.getValue());
                }
            } else if (this.CNconfig.getValue() && (server.equals("2b2tpvp.cn") || server.equals("CrystalPVP.cn") || server.equals("markpvp.kozow.com") || server.equals("play.simpfun.cn:16266") || server.equals("play.simpfun.cn:32672"))) {
                CommandManager.sendChatMessage("\u00a76\u914d\u7f6e\u540c\u6b65\uff1aCrystalPVP.cn");
                Notify_EXlgYplaRzfgofOPOkyB.sendNotify("\u00a76\u914d\u7f6e\u540c\u6b65\uff1aCrystalPVP.cn");
                Step_EShajbhvQeYkCdreEeNY.INSTANCE.height.setValue(this.xiaosongstepHeight.getValueFloat());
                Step_EShajbhvQeYkCdreEeNY.INSTANCE.inWebPause.setValue(this.xiaosongStepWebPause.getValue());
                if (this.xiaosonginvSwap.getValue()) {
                    CombatSetting_kxXrLvbWbduSuFoeBUsC.INSTANCE.invSwapBypass.setValue(true);
                    BypassSetting_RInKGmTQYgWFRhsUOiJP.INSTANCE.SwapSync.setValue(true);
                }
                if (this.xiaosongfastWeb.getValue()) {
                    FastWeb_dehcwwTxEbDSnkFtZvNl.INSTANCE.groundcheck.setValue(this.xiaosongfastWebGround.getValue());
                }
                if (this.xiaosongfastWeb.getValue()) {
                    Burrow_eOaBGEoOSTDRbYIUAbXC.INSTANCE.webcheck.setValue(this.xiaosongburrowWeb.getValue());
                }
            }
        }
        this.needUpdate = false;
    }

    public static enum _ylqyOfVuMcYIoHfjTCiI {
        Xin,
        XiaoSong;

    }
}
