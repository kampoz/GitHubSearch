package com.example.kamil.githubsearch.api

import com.example.kamil.githubsearch.model.ReposResponse
import com.example.kamil.githubsearch.model.User
import com.example.kamil.githubsearch.model.UsersResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Kamil on 29.06.2018.
 */
public interface GitHubApi {

    @GET("search/repositories")
    fun getReposByName(@Query("q") repoName: String) : Observable<ReposResponse>

    @GET("search/users")
    fun getUsersByName(@Query("q") userName: String) : Observable<UsersResponse>

    @GET("users/{user}")
    fun getSingleUser(@Path("user") user : String?) : Observable<User>

    @GET("users/{user}/starred")
    fun getStarredForUser(@Path("user") user : String) : Observable<ReposResponse>
}