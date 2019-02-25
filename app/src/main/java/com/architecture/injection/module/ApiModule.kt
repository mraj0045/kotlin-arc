package com.architecture.injection.module

import android.content.Context
import com.arc.kotlin.api.cookie.CookieGenerator
import com.arc.kotlin.api.response.ApiResponse
import com.arc.kotlin.api.response.CustomJsonDeserializer
import com.arc.kotlin.api.response.DateDeserializer
import com.arc.kotlin.api.security.CustomSSLSocketFactory
import com.arc.kotlin.inject.scope.AppContext
import com.arc.kotlin.util.toTypeToken
import com.architecture.BuildConfig
import com.architecture.api.request.Api
import com.architecture.api.request.ApiHandler
import com.architecture.model.Post
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Module
class ApiModule {

    @Singleton
    @Provides
    fun providesApiHandler(
        @AppContext context: Context, api: Api, retrofit: Retrofit
    ): ApiHandler {
        return ApiHandler(context, api, retrofit)
    }

    @Singleton
    @Provides
    fun providesApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Singleton
    @Provides
    fun providesRetrofit(httpClient: OkHttpClient, factory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .client(httpClient)
            .addConverterFactory(factory)
            .build()
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        loggingInterceptor: Lazy<HttpLoggingInterceptor>,
        cookieGenerator: CookieGenerator,
        trustManager: X509TrustManager?
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .cookieJar(JavaNetCookieJar(cookieGenerator.cookieHandler))
            .apply {
                if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor.get())
                if (trustManager != null) {
                    sslSocketFactory(CustomSSLSocketFactory(), trustManager)
                }
            }
            .build()
    }

    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    internal fun providesTrustManager(): X509TrustManager? {
        var trustManagerFactory: TrustManagerFactory? = null
        try {
            trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory!!.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            return if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                // throw new IllegalStateException(
                // &quot;Unexpected default trust managers:&quot; + Arrays.toString(trustManagers));
                null
            } else trustManagers[0] as X509TrustManager
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        } catch (e: KeyStoreException) {
            e.printStackTrace()
            return null
        }
    }

    @Singleton
    @Provides
    fun providesGsonFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .registerTypeAdapter(postType, CustomJsonDeserializer<Post>())
            .create()
    }

    private val postType: Type = toTypeToken<ApiResponse<Post>>()
}
