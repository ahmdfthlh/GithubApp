package com.fattah.githubapp.data.retrofit

import com.fattah.githubapp.data.response.DetailUserGithubResponse
import com.fattah.githubapp.data.response.GithubResponse
import com.fattah.githubapp.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUserByQueryName(@Query("q") q: String): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserGithubResponse>

    @GET("users/{username}/followers")
    fun getFollowersOfUsername(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowingOfUsername(@Path("username") username: String): Call<List<ItemsItem>>
}