package net.danielrickman.bmt.listener;

import net.citizensnpcs.api.CitizensAPI;
import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.npc.NPCBuilder;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.rank.Rank;
import net.danielrickman.api.state.event.StateEndEvent;
import net.danielrickman.api.state.event.StateStartEvent;
import net.danielrickman.api.util.ItemBuilder;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.RandomUtil;
import net.danielrickman.bmt.BMTMapConfiguration;
import net.danielrickman.bmt.BuildMyThing;
import net.danielrickman.bmt.map.BMTWordMapRenderer;
import net.danielrickman.bmt.repository.BMTRepository;
import net.danielrickman.bmt.sidebar.BMTSidebar;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.map.MapView;

public class BMTRoundListener extends CircuitListener {

    private final BuildMyThing game;
    private final BMTMapConfiguration mapConfiguration;
    private final Player roundBuilder;
    private final String word;
    private final BMTRepository stats;

    private int numberGuessed = 0;

    public BMTRoundListener(CircuitPlugin plugin, BuildMyThing game, BMTMapConfiguration mapConfiguration, Player roundBuilder, String word) {
        super(plugin);
        this.game = game;
        this.mapConfiguration = mapConfiguration;
        this.roundBuilder = roundBuilder;
        this.word = word;
        this.stats = game.getStats();
    }

    @EventHandler
    private void on(AsyncPlayerChatEvent e) {
        if (stats.hasGuessedCorrectly(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            PlayerUtil.forEach(player -> {
                if (stats.hasGuessedCorrectly(player.getUniqueId())) {
                    PlayerUtil.sendMessage(player, game.getChatPrefix() + "%s" + ChatColor.WHITE + ": " + ChatColor.GRAY.toString() + ChatColor.ITALIC + "%s", Rank.getFormattedName(player), e.getMessage());
                }
            });
        } else {
            if (e.getMessage().equalsIgnoreCase(word) && e.getPlayer() != roundBuilder) {
                var guesser = e.getPlayer();
                var points = BuildMyThing.POINTS_FOR_FIRST_GUESS - (numberGuessed * BuildMyThing.MINIMUM_POINTS_FOR_GUESS);

                e.setCancelled(true);

                stats.setGuessedCorrectly(guesser.getUniqueId(), true);
                stats.addPoints(guesser.getUniqueId(), ((points > 2) ? points : BuildMyThing.MINIMUM_POINTS_FOR_GUESS));
                guesser.sendTitle("", ChatColor.GREEN + String.format("+%d points!", points), 0, 20, 20);
                PlayerUtil.sendMessage(guesser, game.getChatPrefix() + ChatColor.GRAY.toString() + ChatColor.ITALIC + "Players who haven't guessed the word can no longer hear you!");

                stats.addPoints(roundBuilder.getUniqueId(), BuildMyThing.POINTS_FOR_BUILDER_PER_GUESS);
                numberGuessed++;

                PlayerUtil.forEach(player -> {
                    PlayerUtil.sendMessage(player, game.getChatPrefix() + "%s " + ChatColor.GREEN.toString() + ChatColor.BOLD + "has guessed the word!", Rank.get(player).getNameColor().toString() + ChatColor.BOLD + guesser.getName());
                    player.playSound(player.getEyeLocation(), Sound.BLOCK_ANVIL_LAND, 0.3f, 1.0f);
                    stats.updateRankings(player.getUniqueId());
                });

                if (numberGuessed == PlayerUtil.getAlivePlayers(getPlugin().getGlobalRepository()).size() - 1) {
                    PlayerUtil.forEach(player -> player.playSound(player.getEyeLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 0.5f, 1.0f));
                    PlayerUtil.sendToAll(game.getChatPrefix() +ChatColor.RED.toString() + ChatColor.BOLD + "Everyone guessed the word! The word was: " + ChatColor.YELLOW + ChatColor.BOLD + "%s", word);
                    Bukkit.getScheduler().runTask(getPlugin(), () -> getPlugin().nextState());
                }
            }
        }
    }

    @EventHandler
    private void on(BlockBreakEvent e) {
        if (e.getPlayer() == roundBuilder && !mapConfiguration.getBuildRegion().containsLocation(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void on(BlockFromToEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    private void on(BlockPlaceEvent e) {
        if (e.getPlayer() == roundBuilder && !mapConfiguration.getBuildRegion().containsLocation(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void on(CreatureSpawnEvent e) {
        e.setCancelled(true);
        if (e.getEntityType() != EntityType.ARMOR_STAND && mapConfiguration.getBuildRegion().containsLocation(e.getLocation())) {
            NPCBuilder
                    .of(e.getEntityType())
                    .withName(e.getEntity().getName(), false)
                    .withNoAI()
                    .spawnEntity(e.getLocation());
        }
    }

    @EventHandler
    private void on(MapInitializeEvent e) {
        var map = e.getMap();
        map.getRenderers().clear();
        map.setLocked(true);
        map.setScale(MapView.Scale.CLOSEST);
        map.addRenderer(new BMTWordMapRenderer(word));
    }

    @EventHandler
    private void on(InventoryOpenEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    private void on(PlayerMoveEvent e) {
        if (e.getPlayer() == roundBuilder && !mapConfiguration.getBuildRegion().containsLocation(e.getTo())) {
            var player = e.getPlayer();
            e.setCancelled(true);
            player.teleport(mapConfiguration.getBuilderSpawnLocation().toWorldLocation(game.getWorld()));
            player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.5f, 1.0f);
            PlayerUtil.sendMessage(player, game.getChatPrefix() + ChatColor.RED + "You can't exit this area!");
        }
    }

    @EventHandler
    private void on(PlayerQuitEvent e) {
        if (e.getPlayer() == roundBuilder) {
            getPlugin().nextState();
            PlayerUtil.forEach(player -> player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.5f, 1.0f));
            PlayerUtil.sendToAll("%s" + ChatColor.RED + " left the game! Skipping their turn.", Rank.getFormattedName(e.getPlayer()));
        }
    }

    @EventHandler
    private void on(StateEndEvent e) {
        roundBuilder.teleport(RandomUtil.randomFrom(mapConfiguration.getSpawnLocations()).toWorldLocation(game.getWorld()));
        PlayerUtil.forEach(player -> {
            if (numberGuessed == 0) {
                player.playSound(player.getEyeLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
                PlayerUtil.sendMessage(player, game.getChatPrefix() + ChatColor.RED.toString() + ChatColor.BOLD + "Time's up! The word was: " + ChatColor.YELLOW + ChatColor.BOLD + "%s", word);
            }
            stats.getSidebar(player.getUniqueId()).destroy();
        });
        CitizensAPI.getNPCRegistry().deregisterAll();
    }

    @EventHandler
    private void on(StateStartEvent e) {
        if (roundBuilder == null || !roundBuilder.isOnline()) {
            getPlugin().nextState(); //Skip the round
        } else {
            mapConfiguration.getBuildRegion().getAllLocations(game.getWorld()).forEach(loc -> loc.getBlock().setType(Material.AIR));

            PlayerUtil.forEach(player -> {
                var uuid = player.getUniqueId();
                PlayerUtil.reset(player);
                stats.setGuessedCorrectly(uuid, false);
                stats.setSidebar(uuid, new BMTSidebar(game, player).initialise());
                stats.getSidebar(uuid).setBuilder(roundBuilder);
                stats.updateRankings(player.getUniqueId());
                if (player != roundBuilder) {
                    player.sendTitle(ChatColor.AQUA + "New Round", Rank.getFormattedName(roundBuilder) + ChatColor.WHITE + " is building", 20, 20, 20);
                    PlayerUtil.sendMessage(player, game.getChatPrefix() + ChatColor.YELLOW + roundBuilder.getName() + ChatColor.WHITE + " is building.");
                    PlayerUtil.sendMessage(player, game.getChatPrefix() + "You have " + ChatColor.YELLOW + "%d " + ChatColor.WHITE + " seconds to guess the build!", BuildMyThing.ROUND_DURATION);
                }
            });

            roundBuilder.teleport(mapConfiguration.getBuilderSpawnLocation().toWorldLocation(game.getWorld()));
            roundBuilder.setGameMode(GameMode.CREATIVE);
            PlayerUtil.sendMessage(roundBuilder, "");
            PlayerUtil.sendMessage(roundBuilder, game.getChatPrefix() + ChatColor.GREEN.toString() + ChatColor.BOLD + "You are the builder!");
            PlayerUtil.sendMessage(roundBuilder,  game.getChatPrefix() + "The object that you have to build is written on the " + ChatColor.YELLOW + "map" + ChatColor.WHITE + ".");
            PlayerUtil.sendMessage(roundBuilder, "");

            roundBuilder.getInventory().setItem(EquipmentSlot.OFF_HAND, ItemBuilder.ofType(Material.FILLED_MAP).displayName(ChatColor.YELLOW + "Your word").build());
        }
    }
}