package com.example.kamil.githubsearch.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.kamil.githubsearch.R
import com.example.kamil.githubsearch.api.ApiManager
import com.example.kamil.githubsearch.model.User


/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment() {

    var user: User? = null
    var id: String? = null
    var apiManager = ApiManager()
//    var tvUsersName : TextView? = null

    fun newInstance(userId: String): UserFragment {
        val fragment = UserFragment()
        fragment.id = userId
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_user, container, false)

        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        var tvUsersName = view.findViewById<TextView>(R.id.tvUserName)
        val ivStartNumber = view.findViewById<TextView>(R.id.tvStartNumber)
        val tvFollowers = view.findViewById<TextView>(R.id.tvFollowers)

        // Inflate the layout for this fragment
        apiManager.userById(id).subscribe({ user -> this.user = user


        }, {}, {
            val str : String? = user?.login
            tvUsersName.setText(str)
        })

        return view
    }


}// Required empty public constructor
