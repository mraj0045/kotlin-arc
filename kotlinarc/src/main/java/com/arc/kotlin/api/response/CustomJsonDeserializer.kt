package com.arc.kotlin.api.response

import com.google.gson.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

@Suppress("SpellCheckingInspection")
class CustomJsonDeserializer<T : Any> : JsonDeserializer<ApiResponse<T>> {

    private val gson = getGson()

    @Throws(JsonParseException::class)
    override fun deserialize(
        element: JsonElement, typeOfT: Type, context: JsonDeserializationContext
    ): ApiResponse<T> {
        var parseType: Type? = null
        if (typeOfT is ParameterizedType) {
            parseType = typeOfT.actualTypeArguments[0]
        }
        when {
            element.isJsonArray -> {
                val list = ArrayList<T>()
                for (ele in element.asJsonArray) {
                    list.add(gson.fromJson(ele, parseType))
                }
                return ApiResponse(list)
            }
            element.isJsonObject -> {
                val json = element.asJsonObject
                return if (json.has("error")) {
                    val error = gson.fromJson(element, ApiError::class.java)
                    ApiResponse(error)
                } else {
                    ApiResponse(gson.fromJson<T>(element, parseType))
                }
            }
            else -> return if ((element as JsonPrimitive).isString) {
                ApiResponse(gson.fromJson<T>(element, parseType))
            } else {
                throw JsonParseException("Unsupported type of monument element")
            }
        }
    }

    private fun getGson(): Gson {
        return GsonBuilder().registerTypeAdapter(Date::class.java, DateDeserializer()).create()
    }
}
