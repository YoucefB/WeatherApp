package com.ybouidjeri.weatherapp.data

import com.ybouidjeri.weatherapp.Utils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private const val URL = "https://api.openweathermap.org/data/2.5/"
    private var language: String = "en"

    //Create logger to monitor request
    private val logger: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    //interceptor add ApiKey
    private val okHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(object: Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {
                val original: Request = chain.request()
                val originalHttpUrl: HttpUrl = original.url

                val url: HttpUrl = originalHttpUrl.newBuilder()
                    .addQueryParameter("appid", Utils.API_KEY)
                    .addQueryParameter("units", "metric")
                    .addQueryParameter("lang", language)
                    .build()

                // Request customization: add request headers
                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url)

                val request: Request = requestBuilder.build()
                return chain.proceed(request)
            }
        })
        .addInterceptor(logger)

    private val builder = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient.build())

    private val retrofit = builder.build()


    fun <S> buildService(serviceType: Class<S>, language: String): S {
        this.language = language
        return retrofit.create(serviceType)
    }

}