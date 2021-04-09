package com.mfahmi.myfundamentalandroid.ui.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.mfahmi.myfundamentalandroid.databinding.ActivityPreferenceBinding
import com.mfahmi.myfundamentalandroid.ui.fragments.AppPreferenceFragment

class PreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBackToolbar.setOnClickListener { finish() }

        supportFragmentManager.commit {
            add(binding.settingsHolder.id, AppPreferenceFragment())
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}