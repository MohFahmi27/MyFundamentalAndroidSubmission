package com.mfahmi.myfundamentalandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.databinding.ActivityDetailBinding
import com.mfahmi.myfundamentalandroid.model.User

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userDetail = intent.getParcelableExtra<User>(EXTRA_DETAIL)
        with(binding) {
            title = userDetail?.name
            tvUsernameDetail.text = getString(R.string.username_placeholder, userDetail?.username)
            tvNameDetail.text = userDetail?.name
            Glide.with(this@DetailActivity).load(userDetail?.profilePict).into(imgDetailUser)
            locationInclude.tvLocationDetail.text = userDetail?.location
            userInfoInclude.tvRepositoryDetail.text = userDetail?.repository.toString()
            userInfoInclude.tvFollowingDetail.text = userDetail?.following.toString()
            userInfoInclude.tvFollowersDetail.text = userDetail?.followers.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}