package com.example.kamil.githubsearch

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Kamil on 29.06.2018.
 */
public interface GitHubApi {



     @GET("repositories?q=tetris")
     fun getReposByName() : Observable<ReposResponse>

     @GET("repositories")
     fun getReposByName(@Query("q") repoName: String) : Observable<ReposResponse>
     //     fun getReposByName(@Path("repoName") repoName: String) : Observable<ReposResponse>
//
//     abstract fun getUserDetail(@Path("id") userId: String,
////                                @Header("authorization") authHeader: String): Observable<Response<User>>
}