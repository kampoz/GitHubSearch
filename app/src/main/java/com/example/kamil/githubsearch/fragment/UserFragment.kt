package com.example.kamil.githubsearch.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.kamil.githubsearch.R
import com.example.kamil.githubsearch.model.User


/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment() {

    var user : User? = null
    var id : String? = null

    fun newInstance(userId : String): UserFragment {
        val fragment = UserFragment()
        fragment.id = userId
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_user, container, false)
    }

}// Required empty public constructor
