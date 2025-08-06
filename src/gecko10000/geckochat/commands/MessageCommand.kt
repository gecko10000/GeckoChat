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

@Command("message")
@Aliases("msg", "m")
@Permission("geckochat.command.message")
class MessageCommand : MyKoinComponent {

    private val plugin: GeckoChat by inject()
    private val messageManager: MessageManager by inject()

    fun register() {
        plugin.lifecycleManager
            .registerEventHandler(LifecycleEvents.COMMANDS.newHandler(LifecycleEventHandler { event ->
                MessageCommandBrigadier.register(
                    event.registrar()
                )
            }))
    }

    @Executes
    fun message(
        sender: CommandSender,
        @Executor source: Player,
        destination: Player,
        @StringArg(StringArgType.GREEDY) message: String
    ) {
        messageManager.trySendMessage(source, destination, message)
    }

}
