package com.arc.kotlin.config

class ApiConfig private constructor() {

    private var cacheEnabled: Boolean = false
    private var apiLoggingEnabled: Boolean = false

    companion object {

        /** Creates new instance of [ApiConfig]*/
        fun create() = ApiConfig()
    }

    /** Returns cache enabled or not. Default value is false */
    fun isCacheEnabled() = cacheEnabled

    /** Enables or Disables cache. Default value is false
     * @param cacheEnabled true or false */
    fun setCacheEnabled(cacheEnabled: Boolean): ApiConfig {
        this.cacheEnabled = cacheEnabled
        return this
    }

    /** Returns Api Logging enabled or not. Default value is false */
    fun isApiLoggingEnabled() = apiLoggingEnabled

    /** Enables or Disables Logging interceptor. Default value is false.
     * @param apiLoggingEnabled true or false  */
    fun setApiLoggingEnable(apiLoggingEnabled: Boolean): ApiConfig {
        this.apiLoggingEnabled = apiLoggingEnabled
        return this
    }


}