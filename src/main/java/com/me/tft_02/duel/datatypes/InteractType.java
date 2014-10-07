package com.me.tft_02.duel.datatypes;

public enum InteractType {
    RIGHT_CLICK,
    LEFT_CLICK,
    OFF;

    public static InteractType getInteractType(String string) {
        try {
            return valueOf(string);
        }
        catch (IllegalArgumentException ex) {
            return OFF;
        }
    }
}
