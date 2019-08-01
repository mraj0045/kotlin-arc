package com.arc.kotlin.inject.modules

import android.content.Context
import com.arc.kotlin.api.cookie.CookieGenerator
import com.arc.kotlin.api.security.CustomSSLSocketFactory
import com.arc.kotlin.config.ArcSdk
import com.arc.kotlin.inject.scope.AppContext
import com.arc.kotlin.inject.scope.BaseURL
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun providesRetrofit(
        @BaseURL baseURL: String, httpClient: OkHttpClient,
        factory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(httpClient)
            .addConverterFactory(factory)
            .build()
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        @AppContext context: Lazy<Context>,
        loggingInterceptor: Lazy<HttpLoggingInterceptor>,
        cookieGenerator: CookieGenerator,
        trustManager: X509TrustManager?
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(ArcSdk.getApiConfig().timeOut, TimeUnit.SECONDS)
            .connectTimeout(ArcSdk.getApiConfig().timeOut, TimeUnit.SECONDS)
            .cookieJar(JavaNetCookieJar(cookieGenerator.cookieHandler))
            .apply {
                if (ArcSdk.getApiConfig().isCacheEnabled()) cache(
                    okhttp3.Cache(
                        context.get().cacheDir,
                        10 * 1024 * 1024
                    )
                )
                if (ArcSdk.getApiConfig().isApiLoggingEnabled()) addInterceptor(loggingInterceptor.get())
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
        try {
            val trustManagerFactory: TrustManagerFactory? =
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
}
