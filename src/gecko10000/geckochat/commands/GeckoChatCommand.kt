package gecko10000.geckochat.commands

import gecko10000.geckochat.GeckoChat
import gecko10000.geckochat.di.MyKoinComponent
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.strokkur.commands.annotations.Aliases
import net.strokkur.commands.annotations.Command
import net.strokkur.commands.annotations.Executes
import net.strokkur.commands.annotations.Permission
import org.bukkit.command.CommandSender
import org.koin.core.component.inject

@Command("geckochat")
@Aliases("gc")
@Permission("geckochat.command")
class GeckoChatCommand : MyKoinComponent {

    private val plugin: GeckoChat by inject()

    fun register() {
        plugin.lifecycleManager
            .registerEventHandler(LifecycleEvents.COMMANDS.newHandler(LifecycleEventHandler { event ->
                GeckoChatCommandBrigadier.register(
                    event.registrar()
                )
            }))
    }

    @Executes("reload")
    @Permission("geckochat.command.reload")
    fun reload(sender: CommandSender) {
        plugin.reloadConfigs()
        sender.sendRichMessage("<green>Config reloaded.")
    }

}
