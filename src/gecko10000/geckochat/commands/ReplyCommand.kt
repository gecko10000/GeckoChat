package gecko10000.geckochat.commands

import gecko10000.geckochat.GeckoChat
import gecko10000.geckochat.MessageManager
import gecko10000.geckochat.di.MyKoinComponent
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.strokkur.commands.StringArgType
import net.strokkur.commands.annotations.*
import net.strokkur.commands.annotations.arguments.StringArg
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.component.inject

@Command("reply")
@Aliases("r")
@Permission("geckochat.command.reply")
class ReplyCommand : MyKoinComponent {

    private val plugin: GeckoChat by inject()
    private val messageManager: MessageManager by inject()

    fun register() {
        plugin.lifecycleManager
            .registerEventHandler(LifecycleEvents.COMMANDS.newHandler(LifecycleEventHandler { event ->
                ReplyCommandBrigadier.register(
                    event.registrar()
                )
            }))
    }

    @Executes
    fun reply(sender: CommandSender, @Executor source: Player, @StringArg(StringArgType.GREEDY) message: String) {
        messageManager.reply(source, message)
    }

}
