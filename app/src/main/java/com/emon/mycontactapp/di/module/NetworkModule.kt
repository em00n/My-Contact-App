package com.emon.mycontactapp.di.module

import android.content.Context
import com.emon.mycontactapp.data.remote.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.emon.mycontactapp.BuildConfig
import com.emon.mycontactapp.data.remote.MockResponseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val BASE_URL = BuildConfig.BASE_URL

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Provides
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    fun provideConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun provideLoggerInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message -> Timber.e(message) }
            .apply { level = HttpLoggingInterceptor.Level.BODY }
    }

/*    @Provides
    @Singleton
    fun provideOkHttpClient(loggerInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val timeOut = 30
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeOut.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeOut.toLong(), TimeUnit.SECONDS)
        httpClient.addInterceptor(loggerInterceptor)
        return httpClient.build()
    }*/

    @Provides
    @Singleton
    fun provideMockResponseInterceptor(@ApplicationContext context: Context): MockResponseInterceptor {
        return MockResponseInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideMockResponseOkHttpClient(
        mockResponseInterceptor: MockResponseInterceptor,
        loggerInterceptorFactory: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(mockResponseInterceptor)
            .addInterceptor(loggerInterceptorFactory)
            .build()
    }

    @Provides
    fun provideRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        factory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideCredentialApiService(
        retrofit: Retrofit
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}