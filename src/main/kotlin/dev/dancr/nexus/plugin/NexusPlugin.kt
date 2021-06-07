package dev.dancr.nexus.plugin

import dev.dancr.nexus.component.DefaultChat
import dev.dancr.nexus.component.LobbySpawning
import dev.dancr.nexus.component.NameTags
import dev.dancr.nexus.component.ScoreboardCreation
import dev.dancr.nexus.data.PlayerData
import dev.dancr.nexus.rank.Ranks
import java.sql.Connection
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

open class NexusPlugin : JavaPlugin() {

    companion object {
        private const val DATA_FILE_NAME = "data.db"

        public inline fun <reified T : JavaPlugin> getPlugin() = getPlugin(T::class.java)
    }

    final override fun onEnable() {
        Database.connect("jdbc:sqlite:/${dataFolder.absolutePath}/$DATA_FILE_NAME")
        transaction {
            TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        }

        registerTables()
        registerComponents()
        registerCommands()
        onPluginEnable()
    }

    protected open fun onPluginEnable() {}

    final override fun onDisable() {
        onPluginDisable()
    }

    protected open fun onPluginDisable() {}

    protected open fun registerTables() = transaction {
        SchemaUtils.create(PlayerData)
    }

    protected open fun registerComponents() {
        Ranks.enable()
        ScoreboardCreation.enable()
        NameTags.enable()
        DefaultChat.enable()
        LobbySpawning.enable()
    }

    protected open fun registerCommands() {}

}