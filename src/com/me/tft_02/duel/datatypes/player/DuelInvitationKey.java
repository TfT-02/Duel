package com.me.tft_02.duel.datatypes.player;


public class DuelInvitationKey {
    private String playerName;
    private int timestamp;
    
    
    public DuelInvitationKey(String playerName, int timestamp) {
        this.setPlayerName(playerName);
        this.setTimestamp(timestamp);
    }


    public String getPlayerName() {
        return playerName;
    }


    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }


    public int getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
