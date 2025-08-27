package me.hextech.remapped;

import me.hextech.HexTech;

/*
 * Exception performing whole class analysis ignored.
 */
public static class ThreadManager
extends Thread {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (HexTech.MODULE == null) continue;
                HexTech.MODULE.onThread();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
