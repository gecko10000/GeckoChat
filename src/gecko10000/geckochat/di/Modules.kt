package gecko10000.geckochat.di

import gecko10000.geckochat.GeckoChat
import gecko10000.geckochat.IgnoreManager
import gecko10000.geckochat.MessageManager
import kotlinx.serialization.json.Json
import org.koin.dsl.module

fun pluginModules(plugin: GeckoChat) = module {
    single { plugin }
    single(createdAtStart = true) { IgnoreManager() }
    single(createdAtStart = true) { MessageManager() }
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
}
