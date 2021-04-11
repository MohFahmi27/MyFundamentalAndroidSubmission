package com.mfahmi.myfundamentalandroid.ui.activities

import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns._ID
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.adapters.DetailSectionsPagerAdapter
import com.mfahmi.myfundamentalandroid.databinding.ActivityDetailBinding
import com.mfahmi.myfundamentalandroid.db.DatabaseContract.Companion.AVATAR_URL
import com.mfahmi.myfundamentalandroid.db.DatabaseContract.Companion.CONTENT_URI
import com.mfahmi.myfundamentalandroid.db.DatabaseContract.Companion.USERNAME
import com.mfahmi.myfundamentalandroid.db.DatabaseContract.Companion.USER_TYPE
import com.mfahmi.myfundamentalandroid.helper.MappingHelper
import com.mfahmi.myfundamentalandroid.model.User
import com.mfahmi.myfundamentalandroid.ui.viewmodels.DetailViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.*

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var uriWithId: Uri
    private var userState: Boolean = false
    private lateinit var userDetail: User

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        const val EXTRA_FRAGMENT = "extra_fragment"
        private const val TAG = "DetailActivity"
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

        loadingScreenVisibility(true)
        initData()
        addDataToView()
        uriWithId = Uri.parse("$CONTENT_URI/${userDetail.id}")
        checkUser(uriWithId)
        binding.fabAddFavorite.setOnClickListener { insertFavorite() }
    }

    private fun initData() {
        val tabTitle = resources.getStringArray(R.array.tab_title)
        userDetail = intent.getParcelableExtra<User>(EXTRA_DETAIL) as User

        userDetail.username.let {
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

    private fun checkUser(uri: Uri) {
        val userFind = contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )

        val getUser = MappingHelper.mapCursorToArrayList(userFind)
        if (getUser.size > 0) {
            userState = false
            setStateFab(true)
        } else {
            userState = true
            setStateFab(false)
        }
    }

    private fun addDataToView() {
        with(binding) {
            detailViewModel.getUserSearch().observe(this@DetailActivity) {
                it?.let {
                    loadingScreenVisibility(false)
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


    private fun insertFavorite() {
        Log.d(TAG, "insertFavorite: ${userDetail.id} & $CONTENT_URI")
        if (userState) {
            Log.d(TAG, "!userState: ${userDetail.id} & $CONTENT_URI")
            val contentValues = contentValuesOf(
                _ID to userDetail.id,
                USERNAME to userDetail.username,
                USER_TYPE to userDetail.userType,
                AVATAR_URL to userDetail.avatarUrl
            )
            contentResolver.insert(CONTENT_URI, contentValues)
            userState = false
            setStateFab(true)
            FancyToast.makeText(
                this,
                getString(R.string.add_to_favorite),
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                false
            ).show()
        } else {
            Log.d(TAG, "else: ${userDetail.id} & $CONTENT_URI")
            contentResolver.delete(uriWithId, null, null)
            userState = true
            setStateFab(false)
            FancyToast.makeText(
                this,
                getString(R.string.remove_to_favorite),
                FancyToast.LENGTH_SHORT,
                FancyToast.WARNING,
                false
            ).show()
        }
    }

    private fun loadingScreenVisibility(state: Boolean) {
        if (!state) {
            binding.lytScreenLoading.visibility = View.GONE
            binding.fabAddFavorite.visibility = View.VISIBLE
        } else {
            binding.lytScreenLoading.visibility = View.VISIBLE
            binding.fabAddFavorite.visibility = View.INVISIBLE
        }
    }

    private fun setStateFab(state: Boolean) =
        if (state) binding.fabAddFavorite.setImageResource(R.drawable.ic_heart_full)
        else binding.fabAddFavorite.setImageResource(R.drawable.ic_heart_border)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}