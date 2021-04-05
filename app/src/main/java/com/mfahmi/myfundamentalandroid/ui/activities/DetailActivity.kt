package com.mfahmi.myfundamentalandroid.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.adapters.DetailSectionsPagerAdapter
import com.mfahmi.myfundamentalandroid.databinding.ActivityDetailBinding
import com.mfahmi.myfundamentalandroid.db.DatabaseContract
import com.mfahmi.myfundamentalandroid.db.UserFavoriteHelper
import com.mfahmi.myfundamentalandroid.model.User
import com.mfahmi.myfundamentalandroid.ui.viewmodels.DetailViewModel

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var userFavoriteHelper: UserFavoriteHelper

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        const val EXTRA_FRAGMENT = "extra_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBackToolbar.setOnClickListener { finish() }

        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(DetailViewModel::class.java)

        setUserData()
        addDataToView()
    }

    private fun setUserData() {
        val tabTitle = resources.getStringArray(R.array.tab_title)
        val userDetail = intent.getParcelableExtra<User>(EXTRA_DETAIL)

        userDetail?.username?.let {
            val usernameLogin = Bundle()
            usernameLogin.putString(EXTRA_FRAGMENT, userDetail.username)
            detailViewModel.setUserDetailData(it)
            binding.viewPagerDetail.adapter =
                DetailSectionsPagerAdapter(this@DetailActivity, tabTitle, usernameLogin)
        }
        TabLayoutMediator(binding.tabLayoutDetail, binding.viewPagerDetail) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

    }

    private fun addDataToView() {
        with(binding) {
            detailViewModel.getUserSearch().observe(this@DetailActivity) {
                it?.let {
                    tvToolbarName.text = it.userName
                    tvNameDetail.text = it.userName
                    tvUsernameDetail.text = it.login
                    locationInclude.tvLocationDetail.text =
                        if (it.userLocation == "null") getString(R.string.location_not_found) else it.userLocation
                    userInfoInclude.run {
                        tvRepositoryDetail.text = it.publicRepos.toString()
                        tvFollowersDetail.text = it.followers.toString()
                        tvFollowingDetail.text = it.following.toString()
                    }

                    Glide.with(this@DetailActivity).load(it.avatarUrl).into(imgDetailUser)
                }
            }
        }
    }

}