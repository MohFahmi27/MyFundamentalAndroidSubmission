package com.mfahmi.myfundamentalandroid.ui.activities

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.adapters.MainUserAdapter
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
            val adapter = MainUserAdapter()
            rvMain.adapter = adapter

            mainViewModel.getUserSearch().observe(this@MainActivity) {
                it?.let {
                    if (it.size == 0) {
                        placeholderVisibility(true)
                        placeholderText.text = getString(R.string.no_result_text)
                        loadingBarVisibility(false)
                    } else {
                        adapter.arrayListUser = it
                        loadingBarVisibility(false)
                    }
                }
            }
        }
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
                    placeholderVisibility(false)
                }
                val inputMethodManager =
                    getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(window.currentFocus?.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.favorite_menu -> true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}