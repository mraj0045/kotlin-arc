package com.arc.kotlin.config

object ArcSdk {

    private var apiConfig: ApiConfig = ApiConfig.create()


    /** Returns the API config */
    fun getApiConfig() = apiConfig

    /** Initializes the Api Config
     * @param apiConfig [ApiConfig] instance */
    fun setApiConfig(apiConfig: ApiConfig): ArcSdk {
        this.apiConfig = apiConfig
        return this
    }
}