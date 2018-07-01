package com.example.kamil.githubsearch.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.kamil.githubsearch.R
import com.example.kamil.githubsearch.api.ApiManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val log: String = "ApiLog  Http"
    var adapter = ResultsAdapter();
    //    var usersItems = mutableListOf<String?>()
//    var reposItems = mutableListOf<String?>()
    var allItems = mutableListOf<String?>()
    var apiManager = ApiManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rv = findViewById<RecyclerView>(R.id.rvResults)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv.adapter = adapter

        etInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                var timer = Timer()
                val DELAY: Long = 1000
                timer.cancel()
                timer = Timer()
                timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                this@MainActivity.runOnUiThread(java.lang.Runnable {
                                    loadReposAndNames(rv, etInput.text.toString())
                                })
                            }
                        },
                        DELAY
                )
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        btnSearch.setOnClickListener {
            //            searchUsers(rv)
//            searchRepos(rv
            loadReposAndNames(rv, etInput.text.toString())
        }
    }


    //todo przekazac text do funkcji z et
//    fun searchUsers(rv: RecyclerView) {
//        usersItems.clear()
//        adapter.items.clear()
//        apiManager.userLoginObservable("kampoz")
//                .subscribe({
//                    usersItems.add(it)
//                }, {
//                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
//                    Log.e(log, "ApiError: " + it.toString());
//                }, {
//                    adapter.items.addAll(usersItems)
//                    rv.adapter = adapter
//                    adapter.notifyDataSetChanged()
//                })
//    }
//
//    fun searchRepos(rv: RecyclerView) {
//        reposItems.clear()
//        adapter.items.clear()
//        apiManager.repoNameObservanle("tetris")
//                .subscribe({
//                    reposItems.add(it)
//                }, {
//                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
//                    Log.e(log, "ApiError: " + it.toString());
//                }, {
//                    adapter.items.addAll(reposItems)
//                    rv.adapter = adapter
//                    adapter.notifyDataSetChanged()
//                })
//    }

    fun loadReposAndNames(rv: RecyclerView, input: String) {
        allItems.clear()
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        apiManager.repoNameObservanle(input).mergeWith(apiManager.userLoginObservable(input))
                .subscribe({
                    allItems.add(it)
                }, {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                    Log.e(log, "ApiError: " + it.toString());
                }, {
                    adapter.items.addAll(allItems)
                    rv.adapter = adapter
                    adapter.notifyDataSetChanged()
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

