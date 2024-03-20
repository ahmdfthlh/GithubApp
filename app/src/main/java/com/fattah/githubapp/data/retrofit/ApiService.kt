package com.fattah.githubapp.data.retrofit

import com.fattah.githubapp.data.response.GithubResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getUserByQueryName(@Query("q") q: String): Call<GithubResponse>

}