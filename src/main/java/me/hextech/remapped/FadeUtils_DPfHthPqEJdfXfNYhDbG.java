package me.hextech.remapped;

import me.hextech.remapped.BaseThreadSetting_TYdViPaJQVoRZLdgWIXF;
import me.hextech.remapped.FadeUtils;

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
        return 1.0 - Math.tanh((double)this.getTime() / (double)this.length * (double)BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.fadeInlength.getValueFloat());
    }

    public double getEpsEzFadeIn() {
        return 1.0 - Math.sin(1.5707963267948966 * this.getFadeOne()) * Math.sin(2.5132741228718345 * this.getFadeOne());
    }

    public double getEpsEzFadeOut() {
        return Math.sin(1.5707963267948966 * this.getFadeOne()) * Math.sin(2.5132741228718345 * this.getFadeOne());
    }

    public double easeOutQuad() {
        return 1.0 - ((double)BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.fadeInQuad.getValueFloat() - this.getFadeOne()) * (BaseThreadSetting_TYdViPaJQVoRZLdgWIXF.INSTANCE.fadeInQuad.getValue() - this.getFadeOne());
    }

    public double easeInQuad() {
        return this.getFadeOne() * this.getFadeOne();
    }

    public double getQuad(FadeUtils quad) {
        switch (quad.ordinal()) {
            case 0: {
                return this.easeInQuad();
            }
            case 1: {
                return this.getFadeInDefault();
            }
            case 2: {
                return this.easeOutQuad();
            }
        }
        return this.easeOutQuad();
    }
}
