package com.github.abnamro.data.di

import com.github.abnamro.BuildConfig
import com.github.abnamro.data.error.ErrorHandlerImpl
import com.github.abnamro.data.remote.HeaderInterceptor
import com.github.abnamro.data.remote.jsonAdapter.RepoJsonAdapter
import com.github.abnamro.domain.model.state.ErrorHandler
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindErrorHandler(errorHandlerImpl: ErrorHandlerImpl): ErrorHandler

    @Binds
    abstract fun provideHeaderInterceptor(headerInterceptor: HeaderInterceptor): Interceptor

    companion object {

        @Provides
        @Singleton
        fun provideRetrofit(
            okHttpClient: OkHttpClient,
            moshi: Moshi
        ): Retrofit {
            return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        @Provides
        fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
            val builder = OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)

            if (BuildConfig.DEBUG) builder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            return builder.build()
        }

        @Provides
        fun provideMoshi(): Moshi = Moshi.Builder()
            .add(RepoJsonAdapter())
            .build()
    }
}
