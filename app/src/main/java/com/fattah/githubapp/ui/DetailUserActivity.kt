package com.fattah.githubapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fattah.githubapp.R
import com.fattah.githubapp.data.response.DetailUserGithubResponse
import com.fattah.githubapp.data.response.GithubResponse
import com.fattah.githubapp.data.response.ItemsItem
import com.fattah.githubapp.data.retrofit.ApiConfig
import com.fattah.githubapp.databinding.ActivityDetailUserBinding
import com.fattah.githubapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    companion object {
        private const val TAG = "DetailUserActivity"
        private var q = "USERNAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        if (username != null) {
            findDetailUser(username)
        }
    }

    private fun findDetailUser(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserGithubResponse> {
            override fun onResponse(
                call: Call<DetailUserGithubResponse>,
                response: Response<DetailUserGithubResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setDetailUserData(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUserGithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setDetailUserData(user : DetailUserGithubResponse ) {
        Glide.with(this)
            .load(user.avatarUrl)
            .into(binding.ivPicture)
        binding.tvName.text = "${user.name}"
        binding.tvUsername.text = "${user.login}"
        binding.tvFollowers.text = "${user.followers} Followers"
        binding.tvFollowing.text = "${user.following} Following"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}