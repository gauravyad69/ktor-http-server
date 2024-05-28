
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


@Serializable
data class Credentials(val username: String, val password: String)


fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson {
                // Jackson configuration if needed
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
                if (credentials.username == "user" && credentials.password == "pass") {
                    call.respond(HttpStatusCode.OK, "Credentials are valid")
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Credentials are wrong")
                }
            }

            get("/text") {
                call.respond(HttpStatusCode.OK, "Here is the text from the server")
            }
        }
    }.start(wait = true)
}