package com.mfahmi.myfundamentalandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfahmi.myfundamentalandroid.adapters.DetailUserAdapter
import com.mfahmi.myfundamentalandroid.databinding.FragmentFollowingBinding
import com.mfahmi.myfundamentalandroid.ui.activities.DetailActivity
import com.mfahmi.myfundamentalandroid.ui.viewmodels.DetailViewModel

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var followingViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity!!.application)
        ).get(DetailViewModel::class.java)
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
            followingViewModel.setUsersLists(
                arguments!!.getString(DetailActivity.EXTRA_FRAGMENT).toString(),
                DetailViewModel.FOLLOWING
            )
        }

        recyclerViewAddData()
    }

    private fun placeholderVisibility(layoutState: Boolean) {
        if (layoutState) {
            binding.placeholderImg.visibility = View.VISIBLE
            binding.placeholderText.visibility = View.VISIBLE
        } else {
            binding.placeholderImg.visibility = View.GONE
            binding.placeholderText.visibility = View.GONE
        }
    }

    private fun recyclerViewAddData() {
        with(binding) {
            rvFollowingUser.layoutManager = LinearLayoutManager(context)
            val adapter = DetailUserAdapter()
            rvFollowingUser.adapter = adapter

            followingViewModel.getUsersGithub().observe(viewLifecycleOwner) {
                it?.let {
                    if (it.size == 0) {
                        placeholderVisibility(true)
                        loadingBarVisibility(false)
                    } else {
                        adapter.usersDetailLists = it
                        placeholderVisibility(false)
                        loadingBarVisibility(false)
                    }
                }
            }
        }
    }

    private fun loadingBarVisibility(progressBarState: Boolean) =
        if (progressBarState) binding.progressBarFollowers.visibility = View.VISIBLE
        else binding.progressBarFollowers.visibility = View.GONE
}