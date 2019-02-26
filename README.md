# Kotlin-Arc

Base architecture and utility files to be used in the projects. Created based on the new AndroidX framework.

### Third-Party Library
The libraries used in this project are listed below.
 
|  Libraries |
| ------ |
| AppCompat |
| RecyclerView |
| ConstraintLayout |
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
```gradle
apply plugin: 'kotlin-kapt'

android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

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
                    success = { success.invoke(it.list) },
                    error = { error.invoke(this) },
                    pError = { retrofit.parseError(it) })
        return call
    }
}
```
#### Dagger 2(Dependency injection)  
Create package **injection.component** and create files as below.

**AppComponent.kt**
```kotlin
@Singleton
@Component(modules = [AppModule::class, RetrofitModule::class, ApiHandlerModule::class, ConfigModule::class, GsonModule::class]) //Can add more modules based on the requirement
interface AppComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

    fun fragmentComponent(fragmentModule: FragmentModule): FragmentComponent

    fun inject(application: Application)
}
```
**ActivityComponent.kt**
```kotlin
@ActivityScope
@Subcomponent(modules = [ActivityModule::class, ActivityMvpModule::class])
interface ActivityComponent {
    // Sample usage
    fun inject(postActivity: PostActivity) 
    
    //Add multiple activities for using injection in that activity
}
```
```kotlin
@FragmentScope
@Subcomponent(modules = [FragmentModule::class, FragmentMvpModule::class])
interface FragmentComponent{
    // Sample usage
    fun inject(postFragment: postFragment) 
    
    //Add multiple fragments for using injection in that particular fragment
}
```
Create package **injection.module** and create files as below.

**ApiHandlerModule.kt**
```kotlin
@Module
class ApiHandlerModule {

    @Singleton
    @Provides
    fun providesApiHandler(@AppContext context: Context, api: Api, retrofit: Retrofit): ApiHandler {
        return ApiHandler(context, api, retrofit)
    }

    @Singleton
    @Provides
    fun providesApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}
```
**ConfigModule.kt**
```kotlin
@Module
class ConfigModule {

    @BaseURL
    @Provides
    fun providesBaseUrl(): String {
        return "https://jsonplaceholder.typicode.com"
    }
}
```
**GsonModule.kt**
```kotlin
@Module
class GsonModule {

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
```
**ActivityMvpModule.kt**
```kotlin
@Module
abstract class ActivityMvpModule {
    //Sample usage
    @Binds
    @ActivityScope
    abstract fun providesPostPresenter(post: PostPresenter): PostContract.Presenter

    //Sample usage
    @Binds
    @ActivityScope
    abstract fun providesPostApi(postRepository: PostRepository): PostContract.Repository
}
```
**FragmentMvpModule.kt**
```kotlin
@Module
abstract class FragmentMvpModule {
    //Sample usage
    @Binds
    @FragmentScope
    abstract fun providesPostPresenter(post: PostPresenter): PostContract.Presenter

    //Sample usage
    @Binds
    @FragmentScope
    abstract fun providesPostApi(postRepository: PostRepository): PostContract.Repository
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
class PostRepository @Inject constructor(private val apiHandler: ApiHandler) : RepositoryImpl<PostContract.Presenter>(),
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
            ?.activityComponent(ActivityModule(this))
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
            component = DaggerAppComponent.builder()
               .appModule(AppModule(this))
               .retrofitModule(RetrofitModule())
               .apiHandlerModule(ApiHandlerModule())
               .gsonModule(GsonModule())
               .configModule(ConfigModule())
               .build()
        }
        return component
    }
    
    override fun onCreate() {
        super.onCreate()
        ArcSdk.setApiConfig(ApiConfig.create().setApiLoggingEnable(BuildConfig.DEBUG))
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
* **0.0.6**
    * ArcSdk class included to initialize config.
* 0.0.5
    * Included custom callback for Retrofit Call, call.enqueue{} and call.execute{}. 
    * Modified Dagger-retrofit implementation. 
    * Added options to enable or disable Logging interceptor and Cache.
* 0.0.4
    * Added constraint layout library
* 0.0.3
    * Added option to change the version no of Kotlin core-ktx and Fragment-ktx
* 0.0.2
* 0.0.1 - base version
