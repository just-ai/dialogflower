package com.justai.dialogflower

import com.justai.jaicf.channel.http.httpBotRouting
import com.justai.jaicf.channel.yandexalice.AliceChannel
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, System.getenv("PORT")?.toInt() ?: 8080) {
        routing {
            httpBotRouting(
                    "/" to AliceChannel(dialogflower, System.getenv("OAUTH_TOKEN"))
            )
            get("/") {
                call.respondText(
                        "<center>Скопируйте адрес этой страницы и используйте его в качестве Webhook URL в вашем навыке для Алисы</center>",
                        ContentType.Text.Html
                )
            }
        }
    }.start(wait = true)
}