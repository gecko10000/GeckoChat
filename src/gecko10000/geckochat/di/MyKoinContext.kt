package gecko10000.geckochat.di

import gecko10000.geckochat.GeckoChat
import org.koin.core.Koin
import org.koin.dsl.koinApplication

object MyKoinContext {
    internal lateinit var koin: Koin
    fun init(plugin: GeckoChat) {
        koin = koinApplication(createEagerInstances = false) {
            modules(pluginModules(plugin))
        }.koin
        koin.createEagerInstances()
    }
}
