package gecko10000.geckochat

import gecko10000.geckochat.commands.GeckoChatCommand
import gecko10000.geckochat.commands.IgnoreCommand
import gecko10000.geckochat.commands.MessageCommand
import gecko10000.geckochat.commands.ReplyCommand
import gecko10000.geckochat.di.MyKoinContext
import gecko10000.geckolib.config.YamlFileManager
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import org.bukkit.plugin.java.JavaPlugin

class GeckoChat : JavaPlugin() {

    private val configFile = YamlFileManager(
        configDirectory = dataFolder,
        initialValue = Config(),
        serializer = Config.serializer(),
    )

    val config: Config
        get() = configFile.value

    companion object {
        val safeMiniMessage: MiniMessage = MiniMessage.builder()
            .tags(
                TagResolver.resolver(
                    StandardTags.color(),
                    StandardTags.gradient(),
                    StandardTags.decorations(),
                    StandardTags.newline(),
                    StandardTags.reset(),
                    StandardTags.shadowColor(),
                )
            ).build()
    }

    override fun onEnable() {
        MyKoinContext.init(this)
        ChatListener()
        GeckoChatCommand().register()
        IgnoreCommand().register()
        MessageCommand().register()
        ReplyCommand().register()
    }

    fun reloadConfigs() {
        configFile.reload()
    }

}
