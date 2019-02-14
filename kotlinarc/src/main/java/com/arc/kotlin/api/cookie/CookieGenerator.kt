package com.arc.kotlin.api.cookie

import android.content.Context
import android.util.Log
import com.arc.kotlin.inject.scope.AppContext
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Inject

class CookieGenerator @Inject constructor(@param:AppContext private val context: Context) {

    val cookieHandler: CookieHandler
        get() {
            val persistentCookieStore = PersistentCookieStore(context)
            val cookieHandler = CookieManager(persistentCookieStore, CookiePolicy.ACCEPT_ALL)
            try {
                val phpSession = (persistentCookieStore.cookies[0].name
                        + "="
                        + persistentCookieStore.cookies[0].value)
                Log.e(TAG, phpSession)
                android.webkit.CookieManager.getInstance()
                    .setCookie(
                        persistentCookieStore.cookies[0].name,
                        persistentCookieStore.cookies[0].value
                    )
            } catch (e: Exception) {
                Log.d(TAG, e.message)
            }

            return cookieHandler
        }

    companion object {

        private val TAG = CookieGenerator::class.java.simpleName
    }
}
