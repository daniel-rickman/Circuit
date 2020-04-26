package net.danielrickman.api.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum WorldSetting {

    PLACE(false),
    BREAK(false),
    PVP(false),
    PVE(false),
    CHAT(true),
    HUNGER(false),
    WEATHER(false),
    DROP(false);

    @Getter
    @Setter
    private boolean isAllowed;

}
