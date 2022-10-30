package com.github.cleancompose.domain.model.repo

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object UrlSerializer : KSerializer<String> {
    private val encoding = StandardCharsets.UTF_8.toString()
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UrlSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(URLEncoder.encode(value, encoding))
    }

    override fun deserialize(decoder: Decoder): String {
        return URLDecoder.decode(decoder.decodeString(), encoding)
    }
}
