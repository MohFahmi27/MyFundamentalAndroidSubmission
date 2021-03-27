package com.mfahmi.myfundamentalandroid.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    val username: String,
    val userType: String,
    val avatarUrl: String
) : Parcelable