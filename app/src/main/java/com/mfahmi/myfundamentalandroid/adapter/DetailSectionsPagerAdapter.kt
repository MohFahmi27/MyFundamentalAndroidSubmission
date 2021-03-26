package com.mfahmi.myfundamentalandroid.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mfahmi.myfundamentalandroid.ui.fragment.FollowersFragment
import com.mfahmi.myfundamentalandroid.ui.fragment.FollowingFragment

class DetailSectionsPagerAdapter(
    activity: AppCompatActivity,
    private val itemArray: Array<String>
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = itemArray.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowersFragment()
            1 -> FollowingFragment()
            else -> FollowersFragment()
        }
    }
}