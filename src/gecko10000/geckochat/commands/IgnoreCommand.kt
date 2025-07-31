package gecko10000.geckochat.commands

import gecko10000.geckochat.GeckoChat
import gecko10000.geckochat.IgnoreManager
import gecko10000.geckochat.di.MyKoinComponent
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.strokkur.commands.annotations.Command
import net.strokkur.commands.annotations.Executes
import net.strokkur.commands.annotations.Executor
import net.strokkur.commands.annotations.Permission
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.component.inject

@Command("ignore")
@Permission("geckochat.command.ignore")
class IgnoreCommand : MyKoinComponent {

    private val plugin: GeckoChat by inject()
    private val ignoreManager: IgnoreManager by inject()

    fun register() {
        plugin.lifecycleManager
            .registerEventHandler(LifecycleEvents.COMMANDS.newHandler(LifecycleEventHandler { event ->
                IgnoreCommandBrigadier.register(
                    event.registrar()
                )
            }))
    }

    @Executes
    fun ignoreList(sender: CommandSender, @Executor player: Player) {
        val data = ignoreManager.getData(player)
        sender.sendMessage(plugin.config.ignoreListHeader)
        data.users.map(plugin.server::getOfflinePlayer)
            .mapNotNull { it.name }
            .forEach { sender.sendMessage(plugin.config.ignoreListEntry(it)) }
    }

    @Executes
    fun ignorePlayer(sender: CommandSender, @Executor player: Player, toIgnore: Player) {
        val uuid = toIgnore.uniqueId
        if (player.uniqueId == uuid) {
            player.sendRichMessage("<red>You can't escape the voices.")
            return
        }
        val data = ignoreManager.getData(player)
        val wasIgnored = data.users.contains(uuid)
        ignoreManager.setData(player, data.copy(if (wasIgnored) data.users.minus(uuid) else data.users.plus(uuid)))
        player.sendRichMessage(
            "<yellow><state> <player>.",
            Placeholder.unparsed("state", if (wasIgnored) "Unignored" else "Ignored"),
            Placeholder.component("player", toIgnore.name()),
        )
    }

}
