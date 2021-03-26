package com.mfahmi.myfundamentalandroid.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.adapter.DetailSectionsPagerAdapter
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
            val tabTitle = resources.getStringArray(R.array.tab_title)
            viewPagerDetail.adapter = DetailSectionsPagerAdapter(this@DetailActivity, tabTitle)
            TabLayoutMediator(tabLayoutDetail, viewPagerDetail) { tab, position ->
                tab.text = tabTitle[position]
            }.attach()

            tvToolbarName.text = userDetail?.username
            tvUsernameDetail.text = userDetail?.name
            tvNameDetail.text = userDetail?.username
            Glide.with(this@DetailActivity).load(userDetail?.avatarUrl).into(imgDetailUser)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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