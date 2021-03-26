package com.mfahmi.myfundamentalandroid.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.adapter.GithubUserAdapter
import com.mfahmi.myfundamentalandroid.databinding.ActivityMainBinding
import com.mfahmi.myfundamentalandroid.model.User

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        loadingBarVisibility(true)
        with(binding) {
            rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
            rvMain.adapter = GithubUserAdapter(getArrayListUser())
            loadingBarVisibility(false)
        }
    }

    private fun loadingBarVisibility(state: Boolean) =
            if (state) binding.mainProgressBar.visibility = View.VISIBLE
            else binding.mainProgressBar.visibility = View.GONE

    private fun getArrayListUser(): Sequence<User> {
        val getUsernameUser = resources.getStringArray(R.array.username)
        val getNameUser = resources.getStringArray(R.array.name)
        val getLocationUser = resources.getStringArray(R.array.location)
        val getRepositoryUser = resources.getIntArray(R.array.repository)
        val getCompanyUser = resources.getStringArray(R.array.company)
        val getFollowers = resources.getIntArray(R.array.followers)
        val getFollowing = resources.getIntArray(R.array.following)
        val getProfilePict = resources.obtainTypedArray(R.array.profile_pict)

        val arrayListUser: ArrayList<User> = arrayListOf()
        for (position in getUsernameUser.indices) {
            arrayListUser.add(
                    User(
                            getUsernameUser[position],
                            getNameUser[position],
                            getLocationUser[position],
                            getRepositoryUser[position],
                            getCompanyUser[position],
                            getFollowers[position],
                            getFollowing[position],
                            getProfilePict.getResourceId(position, -1)
                    )
            )
        }
        getProfilePict.recycle()
        return arrayListUser.asSequence()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}