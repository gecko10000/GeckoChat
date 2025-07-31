package gecko10000.geckochat

import gecko10000.geckochat.di.MyKoinComponent
import gecko10000.geckolib.extensions.MM
import io.papermc.paper.chat.ChatRenderer
import io.papermc.paper.event.player.AsyncChatEvent
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.koin.core.component.inject

class ChatListener : MyKoinComponent, Listener, ChatRenderer {

    private val plugin: GeckoChat by inject()
    private val ignoreManager: IgnoreManager by inject()

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    override fun render(source: Player, sourceDisplayName: Component, message: Component, viewer: Audience):
            Component {
        val withPlaceholders = PlaceholderAPI.setPlaceholders(source, plugin.config.chatFormat)
        return MM.deserialize(
            withPlaceholders,
            Placeholder.component("message", message),
        )
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private fun onChat(event: AsyncChatEvent) {
        event.renderer(this::render)
        event.viewers().removeAll { a -> a is Player && ignoreManager.isIgnoring(a, event.player) }
    }

}
