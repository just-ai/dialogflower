package com.justai.dialogflower

import com.justai.jaicf.channel.http.httpBotRouting
import com.justai.jaicf.channel.yandexalice.AliceChannel
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, System.getenv("PORT")?.toInt() ?: 8080) {
        routing {
            httpBotRouting(
                    "/" to AliceChannel(dialogflower, System.getenv("OAUTH_TOKEN"))
            )
        }
    }.start(wait = true)
}