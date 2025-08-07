package gecko10000.geckochat.placeholders

import gecko10000.geckolib.extensions.MM
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.entity.Player

object PlaceholderFixer {

    private val placeholderRegex = Regex("%([^%]+)%")

    private fun extractNameFromFullPlaceholder(fullPlaceholderWithoutPercents: String): String {
        val fixerType = FixerType.values.firstOrNull {
            fullPlaceholderWithoutPercents.startsWith("${it.name}:")
        } ?: FixerType.DEFAULT
        return fullPlaceholderWithoutPercents.substringAfter("${fixerType.name}:")
    }

    fun fixStringIntoComponent(string: String, player: Player): Component {
        val (fixedFormat, tagResolver) = fixStringAndGetPlaceholderValues(
            string,
            player
        )
        return MM.deserialize(
            fixedFormat,
            tagResolver,
        )
    }

    fun fixStringAndGetPlaceholderValues(string: String, player: Player): Pair<String, TagResolver> {
        val matches = placeholderRegex.findAll(string)
        val tagResolvers = matches
            .map { it.groupValues[1] }
            .map {
                Placeholder.component(extractNameFromFullPlaceholder(it), fixPlaceholder(player, it))
            }
            .toList()
        val topLevelResolver = TagResolver.resolver(tagResolvers)
        val fixedString = placeholderRegex.replace(string) { match ->
            val fullPlaceholderWithoutPercents = match.groupValues[1]
            val withoutPrefix = extractNameFromFullPlaceholder(fullPlaceholderWithoutPercents)
            return@replace "<$withoutPrefix>"
        }
        return fixedString to topLevelResolver
    }

    fun fixPlaceholder(player: Player, fullPlaceholderWithoutPercents: String): Component {
        for (fixer in FixerType.values) {
            if (!fullPlaceholderWithoutPercents.startsWith("${fixer.name}:")) continue
            val actualPlaceholder = fullPlaceholderWithoutPercents.substringAfter("${fixer.name}:")
            return fixPlaceholder(player, actualPlaceholder, fixer)
        }
        return fixPlaceholder<Component, Component>(player, fullPlaceholderWithoutPercents, null)
    }

    private fun <T : Component, U : Component> fixPlaceholder(
        player: Player, placeholderWithoutPercents: String, fixerType: FixerType<T, U>?
    ):
            Component {
        val placeholder = "%$placeholderWithoutPercents%"
        val replaced = PlaceholderAPI.setPlaceholders(player, placeholder)
        val actualFixer = fixerType ?: (if (replaced.contains('§')) FixerType.LEGACY_SEC else FixerType.MM_SAFE)
        return actualFixer.serializer.deserialize(replaced)
    }

}
