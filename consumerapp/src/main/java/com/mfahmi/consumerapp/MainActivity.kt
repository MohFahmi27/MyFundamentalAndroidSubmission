package com.mfahmi.consumerapp

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfahmi.consumerapp.adapter.MainUserAdapter
import com.mfahmi.consumerapp.databinding.ActivityMainBinding
import com.mfahmi.consumerapp.db.DatabaseContract.Companion.CONTENT_URI
import com.mfahmi.consumerapp.helper.MappingHelper
import com.mfahmi.consumerapp.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainUserAdapter

    companion object {
        private const val EXTRA_LIST_USER = "extra_list_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadData()
                placeholderVisibility(false)
                loadingBarVisibility(false)
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) loadData() else savedInstanceState.getParcelableArrayList<User>(
            EXTRA_LIST_USER
        )?.also {
            placeholderVisibility(false)
            loadingBarVisibility(false)
            adapter.arrayListUser = it
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MainUserAdapter(applicationContext)
            rvMain.adapter = adapter
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
        if (progressBarState) binding.mainProgressBar.visibility = View.VISIBLE
        else binding.mainProgressBar.visibility = View.GONE

    private fun placeholderVisibility(layoutState: Boolean) {
        if (layoutState) {
            binding.placeholderImg.visibility = View.VISIBLE
            binding.placeholderText.visibility = View.VISIBLE
        } else {
            binding.placeholderImg.visibility = View.GONE
            binding.placeholderText.visibility = View.GONE
        }
    }
}