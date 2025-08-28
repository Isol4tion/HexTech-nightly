package me.hextech.remapped;

import me.hextech.remapped.FadeUtils_DPfHthPqEJdfXfNYhDbG;

/*
 * Exception performing whole class analysis ignored.
 */
public class Notify {
    public final FadeUtils_DPfHthPqEJdfXfNYhDbG firstFade = new FadeUtils_DPfHthPqEJdfXfNYhDbG(250L);
    public final FadeUtils_DPfHthPqEJdfXfNYhDbG endFade;
    public final FadeUtils_DPfHthPqEJdfXfNYhDbG yFade = new FadeUtils_DPfHthPqEJdfXfNYhDbG(500L);
    public final String first;
    public int delayed = 55;
    public boolean end;

    public Notify(String string) {
        this.endFade = new FadeUtils_DPfHthPqEJdfXfNYhDbG(350L);
        this.first = string;
        this.firstFade.reset();
        this.yFade.reset();
        this.endFade.reset();
        this.end = false;
    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum _eNVZNRNonauDhUZRxBAg {
        Line,
        Fill

    }

    /*
     * Exception performing whole class analysis ignored.
     */
    public enum _hvcAdwcUFPZabyUezEMv {
        Notify,
        Chat,
        Both

    }
}
