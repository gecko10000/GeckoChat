package gecko10000.geckochat.placeholders

import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.entity.Player

object PlayerPlaceholderResolver {
    fun getTagResolver(prefix: String, player: Player): TagResolver {
        return TagResolver.resolver(prefix) { args, context ->
            val placeholder = args.popOr("Missing a PAPI placeholder.").value()
            val fixedPlaceholderValue = PlaceholderFixer.fixPlaceholder(player, placeholder.trim { it == '%' })
            return@resolver Tag.inserting(fixedPlaceholderValue)
        }
    }
}
