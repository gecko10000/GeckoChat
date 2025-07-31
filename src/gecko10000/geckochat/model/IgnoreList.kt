@file:UseSerializers(UUIDSerializer::class)

package gecko10000.geckochat.model

import gecko10000.geckolib.config.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

@Serializable
data class IgnoreList(
    val users: Set<UUID> = setOf(),
    val allIgnored: Boolean = false,
)
