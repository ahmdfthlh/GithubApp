package com.fattah.githubapp.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.fattah.githubapp.R
import com.fattah.githubapp.data.response.DetailUserGithubResponse
import com.fattah.githubapp.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailUserBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        if (username != null) {

            val sectionsPagerAdapter = SectionsPagerAdapter(this)
            sectionsPagerAdapter.username = username
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            supportActionBar?.elevation = 0f

            val detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

            detailViewModel.findDetailUser(username)

            detailViewModel.detailUser.observe(this) { detailUser ->
                setDetailUserData(detailUser)
            }

            detailViewModel.isLoading.observe(this) {
                showLoading(it)
            }
        }
    }

    @SuppressLint("SetTextI18n")
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
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}