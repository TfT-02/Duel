package com.me.tft_02.duel.runnables;

import com.me.tft_02.duel.Duel;
import com.me.tft_02.duel.util.UpdateChecker;

/**
 * Async task
 */
public class UpdateCheckerTask implements Runnable {
    @Override
    public void run() {
        try {
            Duel.p.updateCheckerCallback(UpdateChecker.updateAvailable());
        }
        catch (Exception e) {
            Duel.p.updateCheckerCallback(false);
        }
    }
}
