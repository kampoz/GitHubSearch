package com.example.kamil.githubsearch.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.kamil.githubsearch.R
import com.example.kamil.githubsearch.api.ApiManager
import com.example.kamil.githubsearch.fragment.UserFragment
import com.example.kamil.githubsearch.model.Item
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val log: String = "ApiLog  Http"
    var adapter = ResultsAdapter();
    var allItems = mutableSetOf<Item?>()
    var apiManager = ApiManager()
    val EMPTY = 0
    val SECOND = 1000L
    val TWO_SECONDS = 2000L
    var lastTime = 0L
    val TYPE_USER = 1
    val TYPE_REPO = 2
    val DELAY = 300

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
                pb.visibility = View.VISIBLE
                searchForResults(rv, tv, pb)
                hideProgressBar(pb)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    fun hideProgressBar(pb: ProgressBar) {
        if (etInput.text.toString().length == EMPTY) {
            pb.visibility = View.GONE
        }
    }

    fun searchForResults(rv: RecyclerView, tv: TextView, pb: ProgressBar) {
        if (lastInputWasSecondAgo() && etInput.text.toString().length > 2) {
            loadReposAndNames(rv, etInput.text.toString(), tv, pb)
        }
        lastTime = System.currentTimeMillis()

    }

    fun lastInputWasSecondAgo(): Boolean {
        return (System.currentTimeMillis() - lastTime > DELAY)
    }

    fun loadReposAndNames(rv: RecyclerView, input: String, textView: TextView, pb: ProgressBar) {
        allItems.clear()
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        apiManager.repoNameObservable(input).mergeWith(apiManager.userLoginObservable(input))
                .subscribe({
                    allItems.add(it)
                }, {

                }, {
                    adapter.items.addAll(allItems)
                    Collections.sort(adapter.items) { item1, item2 ->
                        Integer.valueOf(item1?.id) - Integer.valueOf(item2?.id)
                    }
                    rv.adapter = adapter
                    adapter.notifyDataSetChanged()
                    textView.setText(getString(R.string.results_amount, allItems.size.toString()))
                    pb.visibility = View.GONE
                })
    }

    inner class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.CustomViewHolder>() {

        var items: MutableList<Item?> = mutableListOf()

        override fun getItemCount(): Int {
            return items.size;
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
            when (viewType) {
                TYPE_USER -> {
                    val holder = CustomViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_user, parent, false))
                    holder.llContainer.setOnClickListener { v ->
                        run {
                            Toast.makeText(baseContext, "User clicked!", Toast.LENGTH_LONG).show()
                            addFragmentToContainer(UserFragment().newInstance(holder.tvName.text.toString()))
                        }
                    }

                    return holder
                }
                else -> {
                    return CustomViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_repo, parent, false))
                }
            }
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            holder.tvName?.setText(items[position]?.name)
            holder.tvId?.setText(items[position]?.id)
        }

        override fun getItemViewType(position: Int): Int {
            if (items[position]?.isUser ?: true) {
                return TYPE_USER
            } else
                return TYPE_REPO
        }

        inner class CustomViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val tvName = v.findViewById<TextView>(R.id.tvItemName)
            val tvId = v.findViewById<TextView>(R.id.tvId)
            val llContainer = v.findViewById<LinearLayout>(R.id.llContainer)

        }
    }

    private fun addFragmentToContainer(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .add(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commit()
    }
}

