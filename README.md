# Kotlin-Arc

Base architecture and utility files to be used in the projects.

### Third-Party Library
The libraries used in this project are listed below.
 
|  Libraries |
| ------ |
| AppCompat |
| RecyclerView |
| Android KTX(core and fragment) |
| Dagger 2(Dependency injection) |
| Retrofit(Network) |
| OkHttp3 (Logging api request and Url Connection ) |

## Usage
Follow the steps given below to implement the code.

### Step 1 - Updating Gradle files
Add the below lines in the project level build.gradle file
```groovy
buildscript {
    repositories {
        maven {
            url  "https://dl.bintray.com/mraj0045/maven"
        }
    }
}
// Change version no below to use the latest version
ext {
    appCompatVersion = '1.1.0-alpha02'
    daggerVersion = '2.21'
    recyclerViewVersion = '1.0.0'
    retrofitVersion = '2.5.0'
    okHttpVersion = '3.13.1'
    coreKtxVersion = '1.1.0-alpha04'
    fragmentKtxVerion = '1.0.0'
}
```

Add the following code in the app level gradle file (app/build.gragle)
```g roovy
apply plugin: 'kotlin-kapt'

dependencies {
    implementation 'com.arc.kotlin:arc-kotlin:x.x.x'
    //Include Dagger compiler for annotation processing
    kapt "com.google.dagger:dagger-compiler:$rootProject.ext.daggerVersion"
}
```

### Step 2 - Creating Required files
Follow the steps to add the following files to the project. All the files are should be added under the project package.

#### REST API 
Create package **api.request** and create files as below.

**Api.kt**
```kotlin
interface Api {
    //Sample usage
    @GET("/posts")
    fun getPosts(): Call<ApiResponse<Post>>
}
```
**ApiHandler.kt**
```kotlin
class ApiHandler(private val context: Context, private val mApi: Api, private val retrofit: Retrofit) {

    //Sample usage
    @CheckResult
    fun getPosts(success: (List<Post>) -> Unit, authFailure: (() -> Unit)? = null, error: (ApiError) -> Unit):
            Call<ApiResponse<Post>>? {
        if (!context.isOnline()) {
            error(ApiError.noInternet())
            return null
        }

        val call = mApi.getPosts()
        call.enqueue(
            object : Callback<ApiResponse<Post>> {
                override fun onResponse(
                    call: Call<ApiResponse<Post>>,
                    response: Response<ApiResponse<Post>>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null) {
                            if (!apiResponse.isError) {
                                if (apiResponse.list.isEmpty())
                                    error(ApiError.noData())
                                else
                                    success(apiResponse.list)
                            } else {
                                error(apiResponse.error!!)
                            }
                        } else {
                            error(ApiError.noData())
                        }
                    } else {
                        if (response.code() == StatusCode.AUTH_ERROR) {
                            authFailure?.invoke()
                        } else {
                            var apiError: ApiError? = null
                            if (response.errorBody() != null) apiError = retrofit.parseError(response)
                            if (apiError == null)
                                error(ApiError.noData())
                        }
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Post>>, t: Throwable) {
                    when (t) {
                        is SSLException, is SocketException, is SocketTimeoutException, is UnknownHostException ->
                            error(ApiError.noInternet())
                        else -> {
                            error(ApiError.error())
                        }
                    }
                }
            })
        return call
    }
}
```
#### Dagger 2(Dependency injection)  
Create package **injection.component** and create files as below.

**AppComponent.kt**
```kotlin
@Singleton
@Component(modules = [AppModule::class, ApiModule::class]) //Can add more modules based on the requirement
interface AppComponent {

    fun activityComponent(
        activityModule: ActivityModule,
        activityPresenterModule: ActivityPresenterModule
    ): ActivityComponent

    fun fragmentComponent(
        fragmentModule: FragmentModule,
        fragmentPresenterModule: FragmentPresenterModule
    ): FragmentComponent

    fun inject(application: Application)
}
```
**ActivityComponent.kt**
```kotlin
@ActivityScope
@Subcomponent(modules = [ActivityModule::class, ActivityPresenterModule::class])
interface ActivityComponent {
    // Sample usage
    fun inject(postActivity: PostActivity) 
    
    //Add multiple activities for using injection in that activity
}
```
```kotlin
@FragmentScope
@Subcomponent(modules = [FragmentModule::class, FragmentPresenterModule::class])
interface FragmentComponent{
    // Sample usage
    fun inject(postFragment: postFragment) 
    
    //Add multiple fragments for using injection in that particular fragment
}
```
Create package **injection.module** and create files as below.

**ApiModule.kt**
```kotlin
@Module
class ApiModule {

    @Provides
    fun providesApiHandler(
        @AppContext context: Context, api: Api, retrofit: Retrofit
    ): ApiHandler {
        return ApiHandler(context, api, retrofit)
    }

    @Provides
    fun providesApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    fun providesRetrofit(httpClient: OkHttpClient, factory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl("<BASE_URL>")
            .client(httpClient)
            .addConverterFactory(factory)
            .build()
    }

    @Provides
    fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, cookieGenerator: CookieGenerator,trustManager: X509TrustManager?): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .cookieJar(JavaNetCookieJar(cookieGenerator.cookieHandler))
            .addInterceptor(loggingInterceptor)
            .sslSocketFactory(CustomSSLSocketFactory(), trustManager)
            .build()
    }
    
    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    
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

    @Provides
    fun providesGsonFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun providesGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            //For all the classes used for the response, register type adapter to make parsing works properly. Otherwise it will always goes to failure() method.
            .registerTypeAdapter(postType, CustomJsonDeserializer<Post>())
            .create()
    }

    private val postType: Type = toTypeToken<ApiResponse<Post>>()
}
```
**ActivityPresenterModule.kt**
```kotlin
@Module
class ActivityPresenterModule {
    //Sample usage
    @Provides
    @ActivityScope
    fun providesPostPresenter(post: PostPresenter): PostContract.Presenter {
        return post
    }

    //Sample usage
    @Provides
    @ActivityScope
    fun providesPostApi(handler: ApiHandler): PostContract.Repository {
        return PostRepository(handler)
    }
}
```
**FragmentPresenterModule.kt**
```kotlin
@Module
class FragmentPresenterModule {
    //Sample usage
    @Provides
    @FragmentScope
    fun providesPostPresenter(post: PostPresenter): PostContract.Presenter {
        return post
    }

    //Sample usage
    @Provides
    @FragmentScope
    fun providesPostApi(handler: ApiHandler): PostContract.Repository {
        return PostRepository(handler)
    }
}
```

### Step 3 - Creating MVP code for Activities/Fragments/DialogFragment
Follow the example steps below to create the files used for MVP Architecture.

#### 1. Create Contract interface
**PostContract.kt**
```kotlin
interface PostContract {
    interface View : BaseView 
    interface Presenter : BasePresenter<View>
    interface Repository : BaseRepository<Presenter>
}
```
#### 2. Create Presenter and Repository Implementation classes
**PostPresenter.kt**
```kotlin
class PostPresenter @Inject constructor(private val repository: PostContract.Repository) :
    PresenterImpl<PostContract.View>(),
    PostContract.Presenter {

    override fun attach(view: PostContract.View) {
        super.attach(view)
        view()?.run {
            // TODO 
        }
    }
    
    // Mandatory override. To be called before onDestroy()
    override fun detach() {
        repository.cancel()
        super.detach()
    }
}
```
**PostRepository.kt** (For all Network and Database operations)
```kotlin
class PostRepository(private val apiHandler: ApiHandler) : RepositoryImpl<PostContract.Presenter>(),
    PostContract.Repository {
    
    override fun cancel() {
       //TODO cancel any ongoing operation. Call this function in the detach() of presenter class.
    }
}
```

#### 3. Use the files created in the Activity or Fragment class
**PostActivity.kt**
```kotlin
class PostActivity : BaseActivity<PostContract.Presenter>(), PostContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        presenter().attach(this)
    }

    //
    override fun inject() {
        (application  as? App)
            ?.component()
            ?.activityComponent(ActivityModule(this), ActivityPresenterModule())
            ?.inject(this)
    }
}
```
Note: For Fragment and DialogFragment, extend the class with **BaseFragment.kt**. Also, use fragmentComponent() instead of activity component.

### Step 4 - Implement Application class
In the root package, create application class by extending **ArcApplication.kt**

**App.kt**
```kotlin
class App : ArcApplication<AppComponent>() {
    private var component: AppComponent? = null
    override fun component(): AppComponent? {
        if (component == null) {
            component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .apiModule(ApiModule())
                .build()
        }
        return component
    }
}
```
Finally add the **App.kt** to the manifest file
```xml
<application
        android:name=".App"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme"/>
```

## Latest version
* **0.0.3**
    * Added option to change the version no of Kotlin core-ktx and Fragment-ktx
* 0.0.2
* 0.0.1 - base version
