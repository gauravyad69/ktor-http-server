
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.bson.types.ObjectId
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.json


@Serializable
data class Credentials(val username: String, val password: String)


fun main() {
    DatabaseFactory.init()
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json(Json {
            serializersModule = kotlinx.serialization.modules.SerializersModule {
                contextual(ObjectId::class, ObjectIdSerializer)
            }
        })
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
                val collection = DatabaseFactory.getCredentialsCollection()
                val user = collection.findOne(Credentials::username eq credentials.username)

                if (user != null && user.password == credentials.password) {
                    call.respond(HttpStatusCode.OK, "Credentials are valid")
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Credentials are wrong")
                }




          /*      val credentials = call.receive<Credentials>()
                if (credentials.username == "user" && credentials.password == "pass") {
                    call.respond(HttpStatusCode.OK, "Credentials are valid")
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Credentials are wrong")
                }*/
            }

            get("/text") {
                call.respond(HttpStatusCode.OK, "Here is the text from the server")
            }
        }
    }.start(wait = true)
}