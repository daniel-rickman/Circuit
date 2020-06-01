package net.danielrickman.bmt.state;

import net.danielrickman.api.listener.CircuitListener;
import net.danielrickman.api.listener.generic.GenericDisallowJoinListener;
import net.danielrickman.api.plugin.CircuitPlugin;
import net.danielrickman.api.state.TimedState;
import net.danielrickman.api.util.PlayerUtil;
import net.danielrickman.api.util.RandomUtil;
import net.danielrickman.bmt.BMTMapConfiguration;
import net.danielrickman.bmt.BuildMyThing;
import net.danielrickman.bmt.listener.BMTRoundListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BMTRoundState extends TimedState {

    private final Random random = new Random();
    private final BuildMyThing game;
    private final Player roundBuilder;
    private final BMTMapConfiguration mapConfiguration;
    private final String word;

    private String hint;

    public BMTRoundState(CircuitPlugin plugin, BuildMyThing game, Player roundBuilder) {
        super(plugin, BuildMyThing.ROUND_DURATION, BuildMyThing.DELAY_BETWEEN_ROUNDS_IN_TICKS);
        this.game = game;
        this.roundBuilder = roundBuilder;
        this.mapConfiguration = game.getMapConfiguration();
        this.word = pickWord();
        this.hint = word.replaceAll("[a-zA-Z]", "_");
    }

    @Override
    public void onTimerTick() {
        if (getTimer().getTimeLeft() % (20 - word.length()) == 0) {
            var isLetter = false;
            do {
                var index = random.nextInt(word.length() - 1);
                if (Character.isLetter(word.charAt(index))) {
                    var builder = new StringBuilder(hint);
                    builder.setCharAt(index, word.charAt(index));
                    hint = builder.toString();
                    isLetter = true;
                }
            } while (!isLetter);
        }
        PlayerUtil.forEach(player -> {
            game.getStats().getSidebar(player.getUniqueId()).updateTimer(getTimer());
            player.sendActionBar(ChatColor.GREEN.toString() + ChatColor.BOLD + hint);
        });
    }

    @Override
    public List<CircuitListener> getListeners() {
        return Arrays.asList(
                new GenericDisallowJoinListener(getPlugin()),
                new BMTRoundListener(getPlugin(), game, mapConfiguration, roundBuilder, word)
        );
    }

    private String pickWord() {
        String word;
        do {
            word = RandomUtil.randomFrom(mapConfiguration.getWords());
        } while (game.getBuiltWords().contains(word));
        return word;
    }
}