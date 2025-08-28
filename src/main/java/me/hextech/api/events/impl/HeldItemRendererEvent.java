package me.hextech.api.events.impl;

import me.hextech.api.events.Event_auduwKaxKOWXRtyJkCPb;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class HeldItemRendererEvent
extends Event_auduwKaxKOWXRtyJkCPb {
    private final Hand hand;
    private final ItemStack item;
    private final float ep;
    private final MatrixStack stack;

    public HeldItemRendererEvent(Hand hand, ItemStack item, float equipProgress, MatrixStack stack) {
        super(Stage.Pre);
        this.hand = hand;
        this.item = item;
        this.ep = equipProgress;
        this.stack = stack;
    }

    public Hand getHand() {
        return this.hand;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public float getEp() {
        return this.ep;
    }

    public MatrixStack getStack() {
        return this.stack;
    }
}
