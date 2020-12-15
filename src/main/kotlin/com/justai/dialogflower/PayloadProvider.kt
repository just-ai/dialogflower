package com.justai.dialogflower

import com.google.cloud.dialogflow.v2.QueryParameters
import com.google.protobuf.Struct
import com.google.protobuf.util.JsonFormat
import com.justai.jaicf.activator.dialogflow.QueryParametersProvider
import com.justai.jaicf.api.BotRequest
import com.justai.jaicf.context.BotContext

val requestThreadLocal = ThreadLocal<String?>()

object PayloadProvider: QueryParametersProvider {
    override fun provideParameters(botContext: BotContext, request: BotRequest): QueryParameters {
        return QueryParameters.newBuilder().apply {
            Struct.newBuilder().also {
                JsonFormat.parser().merge(requestThreadLocal.get(), it)
                setPayload(it)
            }
        }.build()
    }
}