package me.hextech.remapped;

public class FadeUtils_DPfHthPqEJdfXfNYhDbG {
   public long length;
   private long start;

   public FadeUtils_DPfHthPqEJdfXfNYhDbG(long ms) {
      this.length = ms;
      this.reset();
   }

   public FadeUtils_DPfHthPqEJdfXfNYhDbG reset() {
      this.start = System.currentTimeMillis();
      return this;
   }

   public boolean isEnd() {
      return this.getTime() >= this.length;
   }

   protected long getTime() {
      return System.currentTimeMillis() - this.start;
   }

   public void setLength(long length) {
      this.length = length;
   }

   public double getFadeOne() {
      return this.isEnd() ? (double)BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.fadeInEnd.getValueFloat() : (double)this.getTime() / (double)this.length;
   }

   public double getFadeInDefault() {
      return Math.tanh((double)this.getTime() / (double)this.length * (double)BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.fadeInlength.getValueFloat());
   }

   public double getFadeOutDefault() {
      return 1.0
         - Math.tanh((double)this.getTime() / (double)this.length * (double)BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.fadeInlength.getValueFloat());
   }

   public double getEpsEzFadeIn() {
      return 1.0 - Math.sin((Math.PI / 2) * this.getFadeOne()) * Math.sin((Math.PI * 4.0 / 5.0) * this.getFadeOne());
   }

   public double getEpsEzFadeOut() {
      return Math.sin((Math.PI / 2) * this.getFadeOne()) * Math.sin((Math.PI * 4.0 / 5.0) * this.getFadeOne());
   }

   public double easeOutQuad() {
      return 1.0
         - ((double)BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.fadeInQuad.getValueFloat() - this.getFadeOne())
            * (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.fadeInQuad.getValue() - this.getFadeOne());
   }

   public double easeInQuad() {
      return this.getFadeOne() * this.getFadeOne();
   }

   public double getQuad(FadeUtils quad) {
      switch (quad) {
         case In:
            return this.easeInQuad();
         case In2:
            return this.getFadeInDefault();
         case Out:
            return this.easeOutQuad();
         default:
            return this.easeOutQuad();
      }
   }
}
