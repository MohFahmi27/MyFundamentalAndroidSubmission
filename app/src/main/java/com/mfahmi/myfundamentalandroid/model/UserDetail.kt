package com.mfahmi.myfundamentalandroid.model

data class UserDetail(
    val login: String,
    val userName: String,
    val userLocation: String,
    val avatarUrl: String,
    val publicRepos: Int,
    val followers: Int,
    val following: Int
)