package com.mfahmi.myfundamentalandroid.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mfahmi.myfundamentalandroid.ui.fragments.FollowersFragment
import com.mfahmi.myfundamentalandroid.ui.fragments.FollowingFragment

class DetailSectionsPagerAdapter(
    activity: AppCompatActivity,
    private val itemArray: Array<String>, private val userLogin: Bundle
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = itemArray.size

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 ->  FollowersFragment()
            1 ->  FollowingFragment()
            else -> FollowersFragment()
        }
        fragment.arguments = userLogin
        return fragment
    }
}