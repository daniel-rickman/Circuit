package net.danielrickman.api.repository;

import net.danielrickman.api.repository.profile.GlobalProfile;
import net.danielrickman.api.role.PlayerRoleChangeEvent;
import net.danielrickman.api.util.CircuitPrefix;
import net.danielrickman.api.role.PlayerRole;
import net.danielrickman.api.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class GlobalRepository extends Repository<GlobalProfile> {

    public void addCoins(UUID uuid, int amount) {
        var profile = getPlayerMap().get(uuid);
        Objects.requireNonNull(profile, "Couldn't add coins - GlobalProfile doesn't exist").setCoins(profile.getCoins() + amount);
        PlayerUtil.sendMessage(uuid, CircuitPrefix.COINS.getPrefix() + "You've been awarded " + ChatColor.GRAY + "%d " + ChatColor.WHITE + "coins. You now have " + ChatColor.GRAY + "%d " + ChatColor.WHITE + "coins.", amount, profile.getCoins());
    }

    public int getCoins(UUID uuid) {
        return (getPlayerMap().containsKey(uuid) ? getPlayerMap().get(uuid).getCoins() : 0);
    }

    public UUID getTopCoinsPlayer() {
        var entries = new ArrayList<>(getPlayerMap().entrySet());
        if (entries.size() < 2) {
            return null;
        }
        entries.sort(Comparator.comparingInt(e -> e.getValue().getCoins()));
        Collections.reverse(entries);
        if (entries.get(0).getValue().getCoins() == entries.get(1).getValue().getCoins()) {
            return null;
        } else {
            return entries.get(0).getKey();
        }
    }

    public void setRole(UUID uuid, PlayerRole role) {
        getPlayerMap().get(uuid).setRole(role);
        Bukkit.getPluginManager().callEvent(new PlayerRoleChangeEvent(Bukkit.getPlayer(uuid), role));
    }

    public PlayerRole getRole(UUID uuid) {
        return getPlayerMap().get(uuid).getRole();
    }

}
