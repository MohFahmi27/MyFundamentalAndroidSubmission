package com.mfahmi.myfundamentalandroid.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mfahmi.myfundamentalandroid.api.ApiToken
import com.mfahmi.myfundamentalandroid.model.UserDetail
import com.shashank.sony.fancytoastlib.FancyToast
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
                        FancyToast.makeText(
                            getApplication(),
                            e.printStackTrace().toString(),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, true
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
                        FancyToast.LENGTH_LONG, FancyToast.ERROR, true
                    ).show()
                }

            })
    }

    internal fun getUserSearch(): LiveData<UserDetail> = userGithubDetail
}