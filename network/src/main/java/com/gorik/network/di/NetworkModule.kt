package com.gorik.network.di

import android.content.Context
import androidx.core.content.contentValuesOf
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Singleton
import com.gorik.network.interceptor.CacheInterceptor


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        okhttpClient: OkHttpClient,
        baseUrl: String,
        moshi: Moshi,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl(baseUrl)
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
    fun provideOkHTTP(context:Context,
                      cacheInterceptor:CacheInterceptor,): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(Cache(File(context.cacheDir,"http-cache"),10L * 1024L * 1024L))
            .addInterceptor(cacheInterceptor)
            .build()
    }


}
