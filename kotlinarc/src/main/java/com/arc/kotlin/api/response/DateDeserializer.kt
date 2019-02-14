package com.arc.kotlin.api.response

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : JsonDeserializer<Date> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        jsonElement: JsonElement,
        typeOF: Type,
        context: JsonDeserializationContext
    ): Date? {
        for (format in DATE_FORMATS) {
            try {
                return SimpleDateFormat(format, Locale.US).parse(jsonElement.asString)
            } catch (ignored: ParseException) {
            }
        }
        return null
    }

    companion object {
        private val DATE_FORMATS = arrayOf("yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd")
    }
}
