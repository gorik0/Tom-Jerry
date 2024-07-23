package com.gorik.network.di

import android.content.Context
import com.gorik.network.BuildConfig
import com.gorik.network.interceptor.CacheInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        okhttpClient: OkHttpClient,

        moshi: Moshi,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl(BuildConfig.NEWS_API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }


    @Provides
    @Singleton
    fun provideInterceptor(): CacheInterceptor {
        return CacheInterceptor()
    }


    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }


    @Provides
    @Singleton
    fun provideOkHTTP(
        @ApplicationContext context: Context,
        cacheInterceptor: CacheInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {

            cache(Cache(File(context.cacheDir, "http-cache"), 10L * 1024L * 1024L))
            addNetworkInterceptor(cacheInterceptor)
            addInterceptor(cacheInterceptor)
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }
            connectTimeout(3, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(25, TimeUnit.SECONDS)
        }
            .build()
    }


}
