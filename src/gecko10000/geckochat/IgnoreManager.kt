package gecko10000.geckochat

import gecko10000.geckochat.di.MyKoinComponent
import gecko10000.geckochat.model.IgnoreList
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.koin.core.component.inject

class IgnoreManager : MyKoinComponent {

    private val plugin: GeckoChat by inject()
    private val json: Json by inject()

    private val pdcKey = NamespacedKey(plugin, "ignored")

    fun isIgnoring(source: Player, checked: Player): Boolean {
        val data = getData(source)
        if (data.allIgnored) return true
        return data.users.contains(checked.uniqueId)
    }

    fun getData(player: Player): IgnoreList {
        val string = player.persistentDataContainer.get(pdcKey, PersistentDataType.STRING) ?: return IgnoreList()
        val data = json.decodeFromString<IgnoreList>(string)
        return data
    }

    fun setData(player: Player, data: IgnoreList) {
        val string = json.encodeToString(data)
        player.persistentDataContainer.set(pdcKey, PersistentDataType.STRING, string)
    }

    fun modifyData(player: Player, block: (IgnoreList) -> IgnoreList) {
        val data = getData(player)
        val modifiedData = block(data)
        setData(player, modifiedData)
    }

}
