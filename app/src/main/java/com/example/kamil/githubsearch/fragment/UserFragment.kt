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
import com.example.kamil.githubsearch.model.Repo
import com.example.kamil.githubsearch.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment() {

    var user: User? = null
    var login: String? = null
    var apiManager = ApiManager()
    var arrayList = ArrayList<Repo>()
//    var ivAvatar: ImageView? = null
//    var tvUsersName : TextView? = null

    fun newInstance(userLogin: String): UserFragment {
        val fragment = UserFragment()
        fragment.login = userLogin
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_user, container, false)

        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        var tvUsersName = view.findViewById<TextView>(R.id.tvUserName)
        val ivStarsNumber = view.findViewById<TextView>(R.id.tvStartNumber)
        val tvFollowers = view.findViewById<TextView>(R.id.tvFollowers)

        apiManager.userByLogin(login)
                .subscribe({ user ->
                    this.user = user
                }, {}, {
                    tvUsersName.setText(user?.login)
                    tvFollowers.setText(getString(R.string.followers_number, user?.followers.toString()))
                    Picasso.get()
                            .load("https://avatars1.githubusercontent.com/u/25583667?v=4")
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(ivAvatar);
                })
        apiManager.getUsersStars(login).subscribe({
            tvStartNumber.setText(getString(R.string.starred_number, it.size.toString()))
        })

        return view


    }
}
