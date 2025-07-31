package gecko10000.geckochat

import gecko10000.geckochat.commands.GeckoChatCommand
import gecko10000.geckochat.commands.IgnoreCommand
import gecko10000.geckochat.di.MyKoinContext
import gecko10000.geckolib.config.YamlFileManager
import org.bukkit.plugin.java.JavaPlugin

class GeckoChat : JavaPlugin() {

    private val configFile = YamlFileManager(
        configDirectory = dataFolder,
        initialValue = Config(),
        serializer = Config.serializer(),
    )

    val config: Config
        get() = configFile.value

    override fun onEnable() {
        MyKoinContext.init(this)
        ChatListener()
        GeckoChatCommand().register()
        IgnoreCommand().register()
    }

    fun reloadConfigs() {
        configFile.reload()
    }

}
