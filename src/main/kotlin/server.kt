import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import com.fasterxml.jackson.databind.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.serialization.Serializable
import models.*
import DatabaseFactory.init as initDatabase
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


fun main() {



    initDatabase()

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson {
                // Jackson's configuration if needed
                configure(SerializationFeature.INDENT_OUTPUT, true)
            }
        }
        install(StatusPages) {
            exception<Throwable> { call, cause ->
                call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
            }
        }

        routing {
            get("/status") {
                call.respond(HttpStatusCode.OK, "Server is running")
            }

            post("/login") {
                val credentials = call.receive<Credentials>()
                val user = UserDAO.getUser(credentials.username, credentials.password)

                if (user != null) {
                    call.respond(HttpStatusCode.OK, "Credentials are valid")
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Credentials are wrong")
                }
            }

            post("/register") {
                val credentials = call.receive<Credentials>()
                val newUser = UserDAO.addUser(credentials.username, credentials.password)
                call.respond(HttpStatusCode.Created, newUser)
            }

            post("/text") {
                val credentials = call.receive<Credentials>()
                val userdata = UserDAO.getUserData(credentials.username, credentials.password)
                if (userdata != null) {
                    call.respond(HttpStatusCode.OK, "${userdata}")
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Credentials are wrong, gtfo bitch")
                }


            }



        }
    }.start(wait = true)
}


fun executeCustomQuery(username: String, password: String): User_Login? {
    return transaction {
        val result = Users.selectAll().where { (Users.username eq username) and (Users.password eq password) }
            .singleOrNull()

        result?.let {
            User_Login(it[Users.id], it[Users.username], it[Users.password])
        }
    }
}
