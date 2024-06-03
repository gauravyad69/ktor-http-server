package postgres

import io.ktor.http.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun ContentType.Application.module() {
//    configure()
//    configureSerialization()
//    configureRouting()
}