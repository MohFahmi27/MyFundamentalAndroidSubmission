package com.mfahmi.myfundamentalandroid.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    val username: String,
    val name: String,
    val avatarUrl: String
) : Parcelable