package net.danielrickman.api.util;

import lombok.experimental.UtilityClass;
import net.danielrickman.api.repository.GlobalRepository;
import net.danielrickman.api.role.PlayerRole;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@UtilityClass
public class PlayerUtil {

    public void sendMessage(UUID uuid, String message, Object... params) {
        sendMessage(Objects.requireNonNull(Bukkit.getPlayer(uuid), "Player not found"), message, params);
    }

    public void sendMessage(Player player, String message, Object... params) {
        player.sendMessage(String.format(message, params));
    }

    public void sendToAll(String message, Object... params) {
        Bukkit.broadcastMessage(String.format(message, params));
    }

    public void reset(Player player) {
        player.setHealth(20.0);
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setLevel(0);
    }

    public void forEach(Consumer<? super Player> consumer) {
        Bukkit.getOnlinePlayers().forEach(consumer::accept);
    }

    public List<Player> getAlivePlayers(GlobalRepository global) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> global.getRole(player.getUniqueId()) == PlayerRole.PLAYER)
                .collect(Collectors.toList());
    }
}
