package com.mfahmi.myfundamentalandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

        with(binding) {
            rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
            rvMain.adapter = GithubUserAdapter(getArrayListUser())
        }
    }

    private fun getArrayListUser(): ArrayList<User> {
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
        return arrayListUser
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}