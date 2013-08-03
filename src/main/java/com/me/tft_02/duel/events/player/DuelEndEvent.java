package com.me.tft_02.duel.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.me.tft_02.duel.datatypes.DuelResultType;

public class DuelEndEvent extends PlayerEvent {
    private DuelResultType duelResult;

    public DuelEndEvent(Player player, DuelResultType duelResult) {
        super(player);

        this.setDuelResult(duelResult);
    }

    public DuelResultType getDuelResult() {
        return duelResult;
    }

    public void setDuelResult(DuelResultType duelResult) {
        this.duelResult = duelResult;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
