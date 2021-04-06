package com.mfahmi.myfundamentalandroid.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mfahmi.myfundamentalandroid.api.ApiToken
import com.mfahmi.myfundamentalandroid.model.User
import com.shashank.sony.fancytoastlib.FancyToast
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val listUserGithubMain = MutableLiveData<ArrayList<User>>()

    internal fun setUserSearch(userLogin: String) {
        AsyncHttpClient().apply { addHeader("User-Agent", "request") }
            .apply { addHeader("Authorization", ApiToken.TOKEN_GITHUB_KEY) }
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
                        FancyToast.makeText(
                            getApplication(),
                            e.printStackTrace().toString(),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                        ).show()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error?.message}"
                    }
                    FancyToast.makeText(
                        getApplication(),
                        errorMessage,
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                    ).show()
                }
            })
    }

    internal fun getUserSearch(): LiveData<ArrayList<User>> = listUserGithubMain
}