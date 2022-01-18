package com.jingtian.mtdemo.net

import com.jingtian.mtdemo.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetConstants {
    companion object {
        const val LOGIN_SUCCESS = 0
        const val LOGIN_BY_VC_FAIL = 602
        const val LOGIN_BY_PD_FAIL = 601
        private const val baseUrl = "http://49.232.223.216:8088/mt_server/"
        private val mInterface:NetInterface

        init {
            val interceptor = HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG)
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                else
                    setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build()
            val retrofit: Retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
            mInterface = retrofit.create(NetInterface::class.java)
        }
        fun getInterface() :NetInterface {
            return  mInterface
        }

    }
}