package com.github.abnamro.data.remote

import com.github.abnamro.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val newRequestBuilder = request.newBuilder()

        // Adding `Accept` header is recommended by github
        newRequestBuilder.addHeader("Accept", "application/vnd.github+json")
        newRequestBuilder.addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")

        return chain.proceed(newRequestBuilder.build())
    }
}
