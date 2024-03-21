package com.fattah.githubapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fattah.githubapp.R
import com.fattah.githubapp.data.response.ItemsItem
import com.fattah.githubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragment : Fragment() {
    lateinit var rvUsers: RecyclerView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvUsers = view.findViewById(R.id.rvUsers)
        progressBar = view.findViewById(R.id.progressBar)

        val layoutManager = LinearLayoutManager(requireActivity())
        rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        rvUsers.addItemDecoration(itemDecoration)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME)

        if (index == 1) {
            if (username != null) {
                findListUser(username, index)
            }
        } else {
            if (username != null && index != null) {
                findListUser(username, index)
            }
        }
    }

    private fun findListUser(q: String, index: Int) {
        showLoading(true)

        val client = if (index == 1) {
            ApiConfig.getApiService().getFollowersOfUsername(q)
        } else {
            ApiConfig.getApiService().getFollowingOfUsername(q)
        }

        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody)
                    }
                } else {
                    Log.e(DetailUserActivity.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                showLoading(false)
                Log.e(DetailUserActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserData(userList : List<ItemsItem> ) {
        val adapter = UserAdapter()
        adapter.submitList(userList)
        rvUsers.adapter = adapter
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}