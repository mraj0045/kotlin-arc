package com.arc.kotlin.config

import com.google.gson.JsonObject

object ArcSdk {

    private var apiConfig: ApiConfig = ApiConfig.create()

    internal var errorCondition: (JsonObject) -> Boolean = {
        (it.has("error") || it.has("errorcode")) && it.has("reason")
    }


    /** Returns the API config */
    fun getApiConfig() = apiConfig

    /** Initializes the Api Config
     * @param apiConfig [ApiConfig] instance */
    fun setApiConfig(apiConfig: ApiConfig): ArcSdk {
        this.apiConfig = apiConfig
        return this
    }

    /** Assigns the error conditions to check API returned error response.
     * @param predicate callback custom predicate.
     *
     *
     * By default predicate will check for keys "error", "errorcode" & "reason"*/
    fun errorCondition(predicate: (JsonObject) -> Boolean) {
        this.errorCondition = predicate
    }
}