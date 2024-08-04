package dev.dancr.circuit.scoreboard

import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard

abstract class ScoreboardTemplate {

    public abstract fun updateObjective(scoreboard: Scoreboard, objective: Objective)

    public abstract fun updateTeams(scoreboard: Scoreboard)

    public abstract fun updateSidebar(scoreboard: Scoreboard, vararg params: Any)

}