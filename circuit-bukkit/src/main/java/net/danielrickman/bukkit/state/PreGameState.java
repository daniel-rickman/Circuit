package net.danielrickman.bukkit.state;

import net.citizensnpcs.api.CitizensAPI;
import net.danielrickman.api.state.SimpleState;
import net.danielrickman.bukkit.Circuit;
import net.danielrickman.bukkit.listener.MainListener;
import net.danielrickman.bukkit.listener.OperatorListener;
import net.danielrickman.bukkit.listener.VoteListener;
import net.danielrickman.bukkit.task.NPCCreationTask;

public class PreGameState extends SimpleState {

    private final Circuit circuit;

    public PreGameState(Circuit circuit) {
        super(circuit);
        this.circuit = circuit;
    }

    @Override
    public void onStateStart() {
        enableListener(new MainListener(circuit));
        new NPCCreationTask(circuit).run();
        enableListener(new VoteListener(circuit));
        enableListener(new OperatorListener(circuit));
    }

    @Override
    public void onStateEnd() {
        CitizensAPI.getNPCRegistry().deregisterAll();
    }
}