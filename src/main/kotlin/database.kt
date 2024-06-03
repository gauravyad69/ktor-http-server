import models.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/template1",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "gaurav"
        )

        transaction {
            SchemaUtils.create(Users)
        }
    }
}
