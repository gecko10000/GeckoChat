@file:UseSerializers(MMComponentSerializer::class)

package gecko10000.geckochat

import gecko10000.geckolib.config.serializers.MMComponentSerializer
import gecko10000.geckolib.extensions.MM
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import net.kyori.adventure.text.Component

@Serializable
data class Config(
    val chatFormat: String =
        "<gray>(<#00ffff>∞</#00ffff> | %luckperms_prefix%<gray>) " +
                "<white>%player_displayname%<gray>: <white><message>",
    val ignoreListHeader: Component = MM.deserialize("<gold><u>Ignored Players"),
    private val ignoreListEntryFormat: String = "<gray>{name}</gray> " +
            "<click:run_command:/ignore {name}><red>❌</red></click>"
) {
    fun ignoreListEntry(name: String): Component {
        val replaced = ignoreListEntryFormat.replace("{name}", name)
        return MM.deserialize(replaced)
    }
}
