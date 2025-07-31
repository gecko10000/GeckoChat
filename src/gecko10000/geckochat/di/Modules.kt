package gecko10000.geckochat.di

import gecko10000.geckochat.GeckoChat
import gecko10000.geckochat.IgnoreManager
import kotlinx.serialization.json.Json
import org.koin.dsl.module

fun pluginModules(plugin: GeckoChat) = module {
    single { plugin }
    single(createdAtStart = true) { IgnoreManager() }
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
}
