package com.justai.dialogflower

import com.justai.jaicf.BotEngine
import com.justai.jaicf.activator.dialogflow.DialogflowAgentConfig
import com.justai.jaicf.activator.dialogflow.DialogflowConnector
import com.justai.jaicf.activator.dialogflow.DialogflowIntentActivator
import java.io.ByteArrayInputStream
import java.io.File

private fun loadServiceAccount(): String {
    val account = System.getenv("DIALOGFLOW_SERVICE_ACCOUNT")
    return File(account).takeIf { it.exists() }?.readText() ?: account
}

private val dialogflow = DialogflowConnector(
        DialogflowAgentConfig(
                language = "ru",
                credentials = ByteArrayInputStream(
                        loadServiceAccount().toByteArray()
                )
        )
)

val dialogflower = BotEngine(
        model = MainScenario.model,
        activators = arrayOf(
                DialogflowIntentActivator.Factory(
                        connector = dialogflow,
                        queryParametersProvider = PayloadProvider
                )
        )
)