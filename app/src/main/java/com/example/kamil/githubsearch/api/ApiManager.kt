package com.example.kamil.githubsearch.api

import com.example.kamil.githubsearch.model.Item
import com.example.kamil.githubsearch.model.Repo
import com.example.kamil.githubsearch.model.RepoUser
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

    fun userLoginObservable(login : String) : Observable<Item>{
        return gitHubApi.getUsersByName(login)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map { userResponse -> userResponse.items }
                .flatMapIterable { user -> user }
                .map { user -> userToItem(user) }
    }

    fun repoNameObservable(name : String) : Observable<Item> {
        return gitHubApi.getReposByName(name)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map{repoResponse -> repoResponse.items}
                .flatMapIterable { repo -> repo }
                .map { repo -> repoToItem(repo)}

    }

    companion object {
        val BASE_URL = "https://api.github.com/search/"
    }

    fun repoToItem(repo : Repo) : Item {
        val item = Item()
        item.id = repo.id
        item.name = repo.name
        return item

    }

    fun userToItem(user : RepoUser) : Item {
        val item = Item()
        item.id = user.id
        item.name = user.login
        return item

    }
}
