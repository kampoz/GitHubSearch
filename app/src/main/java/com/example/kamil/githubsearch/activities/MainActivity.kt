package com.example.kamil.githubsearch.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.kamil.githubsearch.R
import com.example.kamil.githubsearch.api.ApiManager
import com.example.kamil.githubsearch.api.GitHubApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    val log: String = "ApiLog  Http"
    var adapter = ResultsAdapter();
    var usersItems = mutableListOf<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rv = findViewById<RecyclerView>(R.id.rvResults)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        usersItems.add("PIerwszy")
        usersItems.add("Drugi")

        adapter.items.addAll(usersItems)

        rv.adapter = adapter

        btnSearch.setOnClickListener {
            searchUsers(rv)
//            search()
        }
    }



    fun searchUsers(rv : RecyclerView) {

        usersItems.clear()
        adapter.items.clear()
        
        var apiManager = ApiManager()
        apiManager.gitHubApi.getUsersByName("kampoz")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map { userResponse -> userResponse.items }
                .flatMapIterable { user -> user }
                .map { user -> user.login }
                .subscribe({
                    usersItems.add(it)
                }, {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    Log.e(log, "ApiError: " + it.toString());
                }, {

                    adapter.items.addAll(usersItems)
                    rv.adapter = adapter
                    adapter.notifyDataSetChanged()
//
                })
    }



    inner class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.CustomViewHolder>() {

        var items: MutableList<String?> = mutableListOf()

        override fun getItemCount(): Int {
            return items.size;
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
            return CustomViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item, parent, false))
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            holder.tv?.setText(items[position])
        }

        inner class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val tv = v.findViewById<TextView>(R.id.tvItemName)

        }
    }

}

