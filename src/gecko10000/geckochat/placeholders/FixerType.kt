package gecko10000.geckochat.placeholders

import gecko10000.geckolib.extensions.MM
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import net.kyori.adventure.text.serializer.ComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

class FixerType<T : Component, U : Component> private constructor(
    val serializer: ComponentSerializer<T, U, String>,
    val name: String,
) {
    companion object {
        val LEGACY_SEC = FixerType(LegacyComponentSerializer.legacySection(), "legacy")
        val MM_UNSAFE = FixerType(MM, "mm_unsafe")
        val MM_SAFE = FixerType(
            MiniMessage.builder()
                .tags(
                    TagResolver.resolver(
                        StandardTags.color(),
                        StandardTags.gradient(),
                        StandardTags.decorations(),
                        StandardTags.newline(),
                        StandardTags.reset(),
                        StandardTags.shadowColor(),
                    )
                ).build(), "mm"
        )
        val values = setOf(LEGACY_SEC, MM_UNSAFE, MM_SAFE)
        val DEFAULT = MM_SAFE
    }
}
