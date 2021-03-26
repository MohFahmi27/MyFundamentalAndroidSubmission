package com.mfahmi.myfundamentalandroid.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mfahmi.myfundamentalandroid.BuildConfig
import com.mfahmi.myfundamentalandroid.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {
    companion object {
        private const val TOKEN_GITHUB_KEY = BuildConfig.TOKEN_GITHUB_KEY
    }

    val listUserGithubMain = MutableLiveData<ArrayList<User>>()

    internal fun setUserSearch(userLogin: String) {
        AsyncHttpClient().apply { addHeader("User-Agent", "request") }
            .apply { addHeader("Authorization", TOKEN_GITHUB_KEY) }
            .get(" https://api.github.com/search/users?q=$userLogin", object :
                AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    try {
                        val listUser = ArrayList<User>()
                        JSONObject(String(responseBody!!)).run {
                            val githubUser = this.getJSONArray("items")
                            for (i in 0 until githubUser.length()) {
                                githubUser.getJSONObject(i).run {
                                    listUser.add(
                                        User(
                                            this.getString("login"),
                                            this.getString("type"),
                                            this.getString("avatar_url")
                                        )
                                    )
                                }
                            }
                        }
                        listUserGithubMain.postValue(listUser)
                    } catch (e: Exception) {
                        Log.d("onSuccess: ", e.printStackTrace().toString())
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Log.d("onFailure: ", error?.message.toString())
                }

            })
    }

    internal fun getUserSearch(): LiveData<ArrayList<User>> = listUserGithubMain
}