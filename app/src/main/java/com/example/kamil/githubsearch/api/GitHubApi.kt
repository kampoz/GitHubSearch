package com.example.kamil.githubsearch.api

import com.example.kamil.githubsearch.model.ReposResponse
import com.example.kamil.githubsearch.model.UsersResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Kamil on 29.06.2018.
 */
public interface GitHubApi {

    @GET("repositories?q=tetris")
    fun getReposByName(): Observable<ReposResponse>

    @GET("repositories")
    fun getReposByName(@Query("q") repoName: String): Observable<ReposResponse>

    @GET("users")
    fun getUsersByName(@Query("q") userName: String): Observable<UsersResponse>
}