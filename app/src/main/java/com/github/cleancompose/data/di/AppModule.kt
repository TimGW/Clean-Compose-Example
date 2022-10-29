@file:Suppress("unused")

package com.github.cleancompose.data.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.github.cleancompose.BuildConfig
import com.github.cleancompose.data.error.ErrorHandlerImpl
import com.github.cleancompose.data.local.AppDatabase
import com.github.cleancompose.data.local.DATABASE_NAME
import com.github.cleancompose.data.remote.HeaderInterceptor
import com.github.cleancompose.data.remote.jsonAdapter.RepoDetailsJsonAdapter
import com.github.cleancompose.data.remote.jsonAdapter.RepoJsonAdapter
import com.github.cleancompose.domain.model.state.ErrorHandler
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        @Provides
        @Singleton
        fun providesRoomDb(
            @ApplicationContext context: Context,
        ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .build()

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
            .add(RepoDetailsJsonAdapter())
            .build()
    }
}
