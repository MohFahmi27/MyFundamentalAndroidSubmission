package com.mfahmi.myfundamentalandroid.ui.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mfahmi.myfundamentalandroid.api.ApiToken
import com.mfahmi.myfundamentalandroid.model.UserDetail
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    val userGithubDetail = MutableLiveData<UserDetail>()

    internal fun setUserDetailData(userLogin: String) {
        AsyncHttpClient().apply { addHeader("User-Agent", "request") }
            .apply { addHeader("Authorization", ApiToken.TOKEN_GITHUB_KEY) }
            .get(" https://api.github.com/users/$userLogin", object :
                AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    try {
                        JSONObject(String(responseBody!!)).run {
                            userGithubDetail.postValue(
                                UserDetail(
                                    this.getString("name"),
                                    this.getString("login"),
                                    this.getString("location"),
                                    this.getString("avatar_url"),
                                    this.getInt("public_repos"),
                                    this.getInt("followers"),
                                    this.getInt("following")
                                )
                            )
                        }
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

    internal fun getUserSearch(): LiveData<UserDetail> = userGithubDetail
}