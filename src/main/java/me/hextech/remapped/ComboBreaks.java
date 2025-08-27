package me.hextech.remapped;

import java.awt.Color;

public class ComboBreaks extends Module_eSdgMXWuzcxgQVaJFmKZ {
   public static ComboBreaks INSTANCE;
   public final EnumSetting<ComboBreaks_PNwHzpQsMnnDslSAEFgc> page = this.add(new EnumSetting("Page", ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack));
   public final ColorSetting Acolor = this.add(
      new ColorSetting("AttackColor ", new Color(255, 255, 255, 150), v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack)).injectBoolean(true)
   );
   public final ColorSetting Aonline = this.add(
         new ColorSetting("AttackOnline", new Color(255, 255, 255, 202), v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack))
      )
      .injectBoolean(true);
   public final SliderSetting attackSync = this.add(
      new SliderSetting("Attack-§4(SyncTime)", 10, 0, 10, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack))
   );
   public final SliderSetting AlastSync = this.add(
      new SliderSetting("Attack-§4(LastSync)", 10, 0, 10, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack))
   );
   public final SliderSetting AonlySync = this.add(
      new SliderSetting("Attack-§4(OnlySync)", 10, 0, 10, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack))
   );
   public final SliderSetting AspamTime = this.add(
      new SliderSetting("Attack-§4(SpamTime)", 0, 0, 1000, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack))
   );
   public final SliderSetting Acyrstal = this.add(new SliderSetting("Attack-§4(Place)", 70, 0, 150, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack)));
   public final SliderSetting Abreak = this.add(new SliderSetting("Attack-§4(Break)", 70, 0, 150, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack)));
   public final SliderSetting Aweb = this.add(new SliderSetting("A-AutoWeb-§4(Delay)", 50, 0, 150, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack)));
   public final SliderSetting Arender = this.add(
      new SliderSetting("Attack-§4(Slider)", 0.2, 0.01, 1.0, 0.01, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack))
   );
   public final SliderSetting Arotate = this.add(
      new SliderSetting("Attack-§4(LagTime)", 70.0, 0.0, 100.0, 0.1, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Attack))
   );
   public final ColorSetting Dcolor = this.add(
      new ColorSetting("DefendColor ", new Color(255, 255, 255, 150), v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Defend)).injectBoolean(true)
   );
   public final ColorSetting Donline = this.add(
         new ColorSetting("DefendOnline", new Color(255, 255, 255, 202), v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Defend))
      )
      .injectBoolean(true);
   public final SliderSetting DattackSync = this.add(
      new SliderSetting("Defend-§b(SyncTime)", 10, 0, 10, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Defend))
   );
   public final SliderSetting DlastSync = this.add(
      new SliderSetting("Defend-§b(LastSync)", 10, 0, 10, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Defend))
   );
   public final SliderSetting DonlySync = this.add(
      new SliderSetting("Defend-§b(OnlySync)", 10, 0, 10, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Defend))
   );
   public final SliderSetting DspamTime = this.add(
      new SliderSetting("Defend-§b(SpamTime)", 0, 0, 1000, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Defend))
   );
   public final SliderSetting Dcrystal = this.add(new SliderSetting("Defend-§b(Place)", 50, 0, 150, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Defend)));
   public final SliderSetting Dbreak = this.add(new SliderSetting("Defend-§b(Break)", 50, 0, 150, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Defend)));
   public final SliderSetting Dweb = this.add(new SliderSetting("D-AutoWeb-§b(Delay)", 50, 0, 150, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Defend)));
   public final SliderSetting Drender = this.add(
      new SliderSetting("D-Crystal-§b(Slider)", 0.2, 0.01, 1.0, 0.01, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Defend))
   );
   public final SliderSetting Drotate = this.add(
      new SliderSetting("D-Rotate-§b(LagTime)", 50, 0, 100, v -> this.page.is(ComboBreaks_PNwHzpQsMnnDslSAEFgc.Defend))
   );
   public final BooleanSetting staticmove = this.add(new BooleanSetting("StaticMove", false));

   public ComboBreaks() {
      super("ComboBreaks", Module_JlagirAibYQgkHtbRnhw.Setting);
      INSTANCE = this;
   }

   @Override
   public void onUpdate() {
      if (nullCheck()) {
      }
   }
}
