package com.example.kamil.githubsearch.api

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Kamil on 01.07.2018.
 */
class ApiManager {

    val gitHubApi: GitHubApi
        get() = buildRetrofit().create<GitHubApi>(GitHubApi::class.java!!)


    private fun buildRetrofit(): Retrofit {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        val httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClientBuilder.build())
                .build()
    }

    fun userLoginObservable(name : String) : Observable<String>{
        return gitHubApi.getUsersByName(name)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map { userResponse -> userResponse.items }
                .flatMapIterable { user -> user }
                .map { user -> user.login }
    }

    companion object {
        val BASE_URL = "https://api.github.com/search/"
    }
}