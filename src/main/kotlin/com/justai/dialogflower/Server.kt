package com.justai.dialogflower

import com.google.api.client.util.IOUtils
import com.justai.jaicf.channel.http.HttpBotRequest
import com.justai.jaicf.channel.yandexalice.AliceChannel
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun main() {
    val channel = AliceChannel(dialogflower, System.getenv("OAUTH_TOKEN"))

    embeddedServer(Netty, System.getenv("PORT")?.toInt() ?: 8080) {
        routing {

            post("/") {
                val stream = call.receiveStream().toByteArrayStream()
                val request = HttpBotRequest(stream)
                val element = requestThreadLocal.asContextElement(request.receiveText())
                launch(coroutineContext + element) {
                    stream.reset()
                    channel.process(request)?.let { response ->
                        call.respondOutputStream(ContentType.parse(response.contentType)) {
                            response.output.writeTo(this)
                        }
                    }
                }
            }

            get("/") {
                call.respondText(
                        "<center>Скопируйте адрес этой страницы и используйте его в качестве Webhook URL в вашем навыке для Алисы</center>",
                        ContentType.Text.Html
                )
            }
        }
    }.start(wait = true)
}

private fun InputStream.toByteArrayStream(): ByteArrayInputStream {
    val out = ByteArrayOutputStream()
    IOUtils.copy(this, out)
    return ByteArrayInputStream(out.toByteArray())
}