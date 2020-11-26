package com.justai.dialogflower

import com.justai.jaicf.activator.dialogflow.dialogflow
import com.justai.jaicf.channel.yandexalice.alice
import com.justai.jaicf.channel.yandexalice.api.model.Button
import com.justai.jaicf.channel.yandexalice.api.model.Image
import com.justai.jaicf.model.scenario.Scenario

object MainScenario: Scenario() {

    init {
        state("main") {
            activators {
                anyEvent()
                anyIntent()
            }

            action {

                reactions.alice?.let { reactions ->
                    activator.dialogflow?.let { activator ->
                        if (activator.queryResult.diagnosticInfo.fieldsMap["end_conversation"]?.boolValue == true) {
                            reactions.endSession()
                        }
                        activator.messages.forEach { message ->
                            if (message.hasSimpleResponses()) {
                                message.simpleResponses.simpleResponsesList.forEach {
                                    reactions.say(it.displayText, it.textToSpeech)
                                }
                            } else if (message.hasText()) {
                                message.text.textList.forEach { reactions.say(it) }
                            }

                            if (message.hasBasicCard()) {
                                reactions.image(
                                        url = message.basicCard.image.imageUri,
                                        title = message.basicCard.title,
                                        description = message.basicCard.formattedText,
                                        button = message.basicCard.buttonsList.firstOrNull()?.let {
                                            Button(it.title, url = it.openUriAction.uri)
                                        }
                                )
                            }

                            if (message.hasListSelect()) {
                                reactions.itemsList(
                                        header = message.listSelect.title
                                ).run {
                                    message.listSelect.itemsList.forEach {
                                        addImage(Image(
                                                imageId = reactions.api!!.getImageId(it.image.imageUri),
                                                title = it.title,
                                                description = it.description,
                                                button = Button("", url = it.info.key)
                                        ))
                                    }
                                }
                            }

                            if (message.hasSuggestions()) {
                                reactions.buttons(
                                        *message.suggestions.suggestionsList.map { it.title }.toTypedArray()
                                )
                            }

                            if (message.hasLinkOutSuggestion()) {
                                reactions.links(
                                        message.linkOutSuggestion.destinationName to message.linkOutSuggestion.uri
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}