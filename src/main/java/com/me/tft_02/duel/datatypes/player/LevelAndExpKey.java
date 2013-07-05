package com.me.tft_02.duel.datatypes.player;

public class LevelAndExpKey {
    private int level;
    private float exp;

    public LevelAndExpKey(int level, float exp) {
        this.setLevel(level);
        this.setExp(exp);
    }

    public int getLevel() {
        return level;
    }

    public float getExp() {
        return exp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExp(float exp) {
        this.exp = exp;
    }
}
