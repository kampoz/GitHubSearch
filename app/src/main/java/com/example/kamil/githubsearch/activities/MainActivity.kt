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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.kamil.githubsearch.R
import com.example.kamil.githubsearch.api.ApiManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val log: String = "ApiLog  Http"
    var adapter = ResultsAdapter();
    var allItems = mutableListOf<String?>()
    var apiManager = ApiManager()
    val EMPTY = 0
    val SECOND = 1000L
    val TWO_SECONDS = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rv = findViewById<RecyclerView>(R.id.rvResults)
        val tv = findViewById<TextView>(R.id.tvResultsAmount)
        tv.setText(getString(R.string.results_amount, EMPTY.toString()))
        val pb = findViewById<ProgressBar>(R.id.pb_progress_bar)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        rv.adapter = adapter

        etInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                searchForResults(rv, tv, pb)
                hideProgressBar(pb)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        btnSearch.setOnClickListener {
            searchForResults(rv, tv, pb)
        }
    }

    fun hideProgressBar(pb : ProgressBar){
        if (etInput.text.toString().length == EMPTY){
            pb.visibility = View.GONE
        }
    }

    fun searchForResults(rv: RecyclerView, tv : TextView, pb : ProgressBar) {
        if (etInput.text.toString().length > EMPTY){
            pb.visibility = View.VISIBLE
            var timer = Timer()
            val DELAY: Long = TWO_SECONDS
            timer.cancel()
            timer = Timer()
            timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            this@MainActivity.runOnUiThread(java.lang.Runnable {
                                loadReposAndNames(rv, etInput.text.toString(), tv, pb)

                            })
                        }
                    },
                    DELAY
            )
        }

    }

    fun loadReposAndNames(rv: RecyclerView, input: String, textView : TextView, pb : ProgressBar) {
        allItems.clear()
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        apiManager.repoNameObservanle(input).mergeWith(apiManager.userLoginObservable(input))
                .subscribe({
                    allItems.add(it)
                }, {
                    Toast.makeText(this, "Api Error\n" + it.toString() , Toast.LENGTH_LONG).show()
                    Log.e(log, "ApiError: " + it.toString());
                }, {
                    adapter.items.addAll(allItems)
                    rv.adapter = adapter
                    adapter.notifyDataSetChanged()
                    textView.setText(getString(R.string.results_amount, allItems.size.toString()))
                    pb.visibility = View.GONE
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

