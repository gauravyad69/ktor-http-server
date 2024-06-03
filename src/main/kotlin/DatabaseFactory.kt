// DatabaseFactory.kt
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

object DatabaseFactory {
    lateinit var client: MongoClient
    lateinit var database: com.mongodb.client.MongoDatabase

    fun init() {
        client = KMongo.createClient("mongodb+srv://gauravmongo:gaurav69@cluster0.t9kf18u.mongodb.net/")
        database = client.getDatabase("ktor_db")

        val collection = getCredentialsCollection()
        if (collection.find().toList().isEmpty()) {
            collection.insertOne(Credentials(username = "user", password = "pass"))
        }
    }



    fun getCredentialsCollection(): MongoCollection<Credentials> {
        return database.getCollection<Credentials>("ktor_db")
    }
}
