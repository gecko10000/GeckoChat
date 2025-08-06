@file:UseSerializers(MMComponentSerializer::class, SoundSerializer::class)

package gecko10000.geckochat

import com.charleskorn.kaml.YamlComment
import gecko10000.geckolib.config.serializers.MMComponentSerializer
import gecko10000.geckolib.config.serializers.SoundSerializer
import gecko10000.geckolib.extensions.MM
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import net.kyori.adventure.text.Component
import org.bukkit.Sound

@Serializable
data class Config(
    val chatFormat: String =
        "<gray>(<#00ffff>∞</#00ffff> | %luckperms_prefix%<gray>) " +
                "<white>%player_displayname%<gray>: <white><message>",
    val ignoreListHeader: Component = MM.deserialize("<gold><u>Ignored Players"),
    private val ignoreListEntryFormat: String = "<gray>{name}</gray> " +
            "<click:run_command:/ignore {name}><red>❌</red></click>",
    val sourceMessageFormat: String = "<gray>[<dark_gray>you</dark_gray> -> " +
            "<dark_gray><dst:%player_name%></dark_gray>] <white><message>",
    val destinationMessageFormat: String = "<gray>[<dark_gray><src:%player_name%></dark_gray> -> " +
            "<dark_gray>you</dark_gray>] <white><message>",
    @YamlComment(
        "Whether /r should reply to the latest messenger",
        "(can cause unexpected replies, but more straightforward)"
    )
    val replyToLatest: Boolean = true,
    val messageSound: Sound? = Sound.ENTITY_ENDERMITE_DEATH,
    val messageVolume: Float = 0.5f,
) {
    fun ignoreListEntry(name: String): Component {
        val replaced = ignoreListEntryFormat.replace("{name}", name)
        return MM.deserialize(replaced)
    }
}
