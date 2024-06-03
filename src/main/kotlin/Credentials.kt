// Credentials.kt
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class CredentialsMongo(
    @Serializable(with = ObjectIdSerializer::class) val id: ObjectId = ObjectId(),
    val username: String,
    val password: String
)
