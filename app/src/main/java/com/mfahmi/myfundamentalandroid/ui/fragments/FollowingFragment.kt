package com.mfahmi.myfundamentalandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfahmi.myfundamentalandroid.adapters.FollowingUserAdapter
import com.mfahmi.myfundamentalandroid.databinding.FragmentFollowingBinding
import com.mfahmi.myfundamentalandroid.ui.activities.DetailActivity
import com.mfahmi.myfundamentalandroid.ui.viewmodels.FollowingViewModel

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followingViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(activity!!.application)).get(FollowingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            loadingBarVisibility(true)
            followingViewModel.setUserFollowing(arguments!!.getString(DetailActivity.EXTRA_FRAGMENT).toString())
        }

        recyclerViewAddData()
    }

    private fun recyclerViewAddData() {
        with(binding) {
            rvFollowersUser.layoutManager = LinearLayoutManager(context)
            val adapter = FollowingUserAdapter()
            rvFollowersUser.adapter = adapter

            followingViewModel.getUserFollowing().observe(viewLifecycleOwner) {
                it?.let {
                    adapter.setFollowingUsers(it)
                    loadingBarVisibility(false)
                }
            }
        }
    }

    private fun loadingBarVisibility(progressBarState: Boolean) =
        if (progressBarState) binding.progressBarFollowers.visibility = View.VISIBLE
        else binding.progressBarFollowers.visibility = View.GONE

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}