package net.danielrickman.bukkit.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LobbySetting {

    VOTE(true);

    @Getter
    private boolean isAllowed;

    public void set(boolean isAllowed) {
        this.isAllowed = isAllowed;
    }
}
