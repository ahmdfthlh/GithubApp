package com.fattah.githubapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fattah.githubapp.R
import com.fattah.githubapp.data.response.ItemsItem

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

        val followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowViewModel::class.java)

        if (index == 1) {
            if (username != null) {
                followViewModel.findListUser(username, index)
            }
        } else {
            if (username != null && index != null) {
                followViewModel.findListUser(username, index)
            }
        }

        followViewModel.listUsers.observe(requireActivity()) { listUser ->
            setUserData(listUser)
        }

        followViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }
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
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}