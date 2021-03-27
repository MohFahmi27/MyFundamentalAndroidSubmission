package com.mfahmi.myfundamentalandroid.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.adapters.GithubUserAdapter
import com.mfahmi.myfundamentalandroid.databinding.ActivityMainBinding
import com.mfahmi.myfundamentalandroid.ui.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainViewModel::class.java)

        getDataUser()
    }

    private fun getDataUser() {
        with(binding) {
            rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
            val adapter = GithubUserAdapter()
            rvMain.adapter = adapter

            mainViewModel.getUserSearch().observe(this@MainActivity) {
                it?.let {
                    adapter.setArrayListUser(it)
                    loadingBarVisibility(false)
                }
            }
        }
    }

    private fun loadingBarVisibility(progressBarState: Boolean) =
        if (progressBarState) binding.mainProgressBar.visibility = View.VISIBLE
        else binding.mainProgressBar.visibility = View.GONE

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search_bar_main)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    loadingBarVisibility(true)
                    mainViewModel.setUserSearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.localization_menu -> {startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}