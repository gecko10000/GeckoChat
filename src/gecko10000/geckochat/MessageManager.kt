package gecko10000.geckochat

import gecko10000.geckochat.di.MyKoinComponent
import gecko10000.geckochat.placeholders.PlayerPlaceholderResolver
import gecko10000.geckolib.extensions.MM
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.NamespacedKey
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.koin.core.component.inject
import java.util.*

class MessageManager : MyKoinComponent {

    private val plugin: GeckoChat by inject()
    private val ignoreManager: IgnoreManager by inject()
    private val replyToKey = NamespacedKey(plugin, "reply_to")

    private fun fixupMessage(format: String, src: Player, dst: Player, msg: String): Component {
        return MM.deserialize(
            format,
            PlayerPlaceholderResolver.getTagResolver("src", src),
            PlayerPlaceholderResolver.getTagResolver("dst", dst),
            Placeholder.component("message", GeckoChat.safeMiniMessage.deserialize(msg))
        )
    }

    private fun saveReplyTo(source: Player, destination: Player) {
        source.persistentDataContainer.set(replyToKey, PersistentDataType.STRING, destination.uniqueId.toString())
    }

    private fun getReplyTo(player: Player): OfflinePlayer? {
        val uuidString = player.persistentDataContainer.get(replyToKey, PersistentDataType.STRING)
        return uuidString
            ?.let { UUID.fromString(it) }
            ?.let { plugin.server.getOfflinePlayer(it) }
    }

    fun trySendMessage(source: Player, destination: Player, message: String) {
        if (ignoreManager.isIgnoring(destination, source)) {
            source.sendRichMessage(
                "<red><player> has you ignored.",
                Placeholder.component("player", destination.displayName())
            )
            return
        }
        if (ignoreManager.isIgnoring(source, destination)) {
            val command = "/ignore ${destination.name}"
            val hoverText = "'<red>Unignore ${destination.name}'"
            source.sendRichMessage(
                "<red>You're ignoring <player>. <u><click:run_command:$command><hover:show_text:$hoverText>Click to " +
                        "unignore.",
                Placeholder.component("player", destination.displayName())
            )
            return
        }
        source.sendMessage(fixupMessage(plugin.config.sourceMessageFormat, source, destination, message))
        destination.sendMessage(fixupMessage(plugin.config.destinationMessageFormat, source, destination, message))
        val sound = plugin.config.messageSound
        if (sound != null) {
            destination.playSound(destination, sound, plugin.config.messageVolume, 1f)
        }
        saveReplyTo(source, destination)
        if (plugin.config.replyToLatest) {
            saveReplyTo(destination, source)
        }
    }

    fun reply(source: Player, message: String) {
        val replyTo = getReplyTo(source)
        if (replyTo == null) {
            source.sendRichMessage("<red>You have nobody to reply to.")
            return
        }
        val player = replyTo.player
        if (player == null) {
            source.sendRichMessage(
                "<red><player> is not online.",
                Placeholder.unparsed("player", replyTo.name ?: replyTo.uniqueId.toString())
            )
            return
        }
        trySendMessage(source, player, message)
    }

}
