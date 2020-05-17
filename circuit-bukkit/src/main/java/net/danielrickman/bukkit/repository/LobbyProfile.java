package net.danielrickman.bukkit.repository;

import lombok.Data;
import net.danielrickman.api.hologram.Hologram;
import net.danielrickman.api.repository.profile.IProfile;
import net.danielrickman.bukkit.sidebar.LobbySidebar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitTask;

@Data
public class LobbyProfile implements IProfile {

    private LobbySidebar lobbySidebar;
    private Hologram voteHologram;
    private BukkitTask voteEffectTask;


}
