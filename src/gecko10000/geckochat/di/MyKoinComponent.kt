package gecko10000.geckochat.di

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

internal interface MyKoinComponent : KoinComponent {

    override fun getKoin(): Koin {
        return MyKoinContext.koin
    }
}
