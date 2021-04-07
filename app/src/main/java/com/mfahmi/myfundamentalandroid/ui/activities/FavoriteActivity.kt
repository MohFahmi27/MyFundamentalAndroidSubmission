package com.mfahmi.myfundamentalandroid.ui.activities

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.adapters.MainUserAdapter
import com.mfahmi.myfundamentalandroid.databinding.ActivityFavoriteBinding
import com.mfahmi.myfundamentalandroid.db.DatabaseContract.Companion.CONTENT_URI
import com.mfahmi.myfundamentalandroid.helper.MappingHelper
import com.mfahmi.myfundamentalandroid.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private var _binding: ActivityFavoriteBinding? = null
    private val binding = _binding!!
    private lateinit var adapter: MainUserAdapter

    companion object {
        private const val EXTRA_LIST_USER = "extra_list_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadData()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) loadData() else savedInstanceState.getParcelableArrayList<User>(
            EXTRA_LIST_USER
        )?.also { adapter.arrayListUser = it }
    }

    private fun initRecyclerView() {
        with(binding) {
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = MainUserAdapter(applicationContext)
            rvFavorite.adapter = adapter
            loadingBarVisibility(true)
        }
    }

    private fun loadData() {
        with(binding) {
            GlobalScope.launch(Dispatchers.Main) {
                val users = async(Dispatchers.IO) {
                    val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                val listUsers = users.await()
                if (listUsers.size == 0) {
                    placeholderVisibility(true)
                    placeholderText.text = getString(R.string.no_favorite_user)
                    adapter.arrayListUser = listUsers
                    loadingBarVisibility(false)
                } else {
                    placeholderVisibility(false)
                    adapter.arrayListUser = listUsers
                    loadingBarVisibility(false)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_LIST_USER, adapter.arrayListUser)
    }

    private fun loadingBarVisibility(progressBarState: Boolean) =
        if (progressBarState) binding.favoriteProgressBar.visibility = View.VISIBLE
        else binding.favoriteProgressBar.visibility = View.GONE

    private fun placeholderVisibility(layoutState: Boolean) {
        if (layoutState) {
            binding.placeholderImg.visibility = View.VISIBLE
            binding.placeholderText.visibility = View.VISIBLE
        } else {
            binding.placeholderImg.visibility = View.GONE
            binding.placeholderText.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}