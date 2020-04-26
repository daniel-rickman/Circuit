package net.danielrickman.api.player;

import lombok.Getter;
import org.bukkit.entity.Player;

import javax.inject.Singleton;
import java.util.*;
import java.util.function.Consumer;

@Singleton
public class PlayerRepository {

    @Getter
    private final List<CorePlayer> players = new ArrayList<>();

    public void add(Player p) {
        players.add(new CorePlayer(p.getUniqueId()));
    }

    public void remove(Player p) {
        getPlayer(p).ifPresent(players::remove);
    }

    public Optional<CorePlayer> getPlayer(Player p) {
        return players
                .stream()
                .filter(cp -> cp.getUuid().equals(p.getUniqueId()))
                .findFirst();
    }

    public void forEach(Consumer<? super CorePlayer> consumer) {
        players.forEach(consumer::accept);
    }

    public int getCount() {
        return players.size();
    }
}
