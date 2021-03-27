package com.mfahmi.myfundamentalandroid.ui.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mfahmi.myfundamentalandroid.api.ApiToken
import com.mfahmi.myfundamentalandroid.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel(application: Application) : AndroidViewModel(application) {
    val listUserGithubFollowers = MutableLiveData<ArrayList<User>>()

    internal fun setUserFollowers(userLogin: String) {
        AsyncHttpClient().apply { addHeader("User-Agent", "request") }
            .apply { addHeader("Authorization", ApiToken.TOKEN_GITHUB_KEY) }
            .get(" https://api.github.com/users/$userLogin/followers", object :
                AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    try {
                        val listUser = ArrayList<User>()
                            JSONArray(String(responseBody!!)).run {
                            for (i in 0 until this.length()) {
                                this.getJSONObject(i).run {
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
                        listUserGithubFollowers.postValue(listUser)
                    } catch (e: Exception) {
                        Toast.makeText(
                            getApplication(),
                            e.printStackTrace().toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Toast.makeText(
                        getApplication(),
                        error?.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    internal fun getUserFollowers(): LiveData<ArrayList<User>> = listUserGithubFollowers
}