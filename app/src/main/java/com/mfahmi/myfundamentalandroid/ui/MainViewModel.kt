package com.mfahmi.myfundamentalandroid.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.mfahmi.myfundamentalandroid.model.User

class MainViewModel : ViewModel() {
    val listUserGithubMain = MutableLiveData<Sequence<User>>()

    internal fun setUserSearch() {
//        AsyncHttpClient().apply { addHeader("Authorization", ) }
    }
}