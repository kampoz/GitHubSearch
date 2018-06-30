package com.example.kamil.githubsearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    val log : String = "ApiLog  Http"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSearch.setOnClickListener{
            search()
        }






    }

    fun search(){

        val okHttpClientBuilder : OkHttpClient.Builder = OkHttpClient.Builder()

        val httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClientBuilder.build())
                .build()

        val gitHubApi = retrofit.create(GitHubApi::class.java)
        gitHubApi.getReposByName("tetris")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    var reposResponse : ReposResponse = it
                    var myItems : Array<Item>? = reposResponse.items
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                    Log.d(log, "ApiSuccess: "+ it.toString());
                    Log.d(log, "myItems - repos: "+ myItems.toString());
                }, {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    Log.d(log, "ApiError: "+ it.toString());
                })
    }
}
