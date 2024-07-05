package com.kiran.githubsearch.di

import com.kiran.githubsearch.BuildConfig
import com.kiran.githubsearch.api.GithubApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val AUTHORIZATION_TOKEN = BuildConfig.API_READ_ACCESS_TOKEN
    private const val AUTHORIZATION = "Authorization"
    private const val BEARER = "Bearer"
    private const val TIMEOUT = 120L

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder().build()
            chain.proceed(request)
        }
        //I have not used header due to key limit
        //If required add header here and add token in build.gradle(module)
        val client = OkHttpClient.Builder().readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS).addInterceptor(logging)
            .addInterceptor(authInterceptor).build()

        return Retrofit.Builder().baseUrl(GithubApi.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideGithubAPi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)
}