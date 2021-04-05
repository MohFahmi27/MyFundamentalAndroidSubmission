package com.mfahmi.myfundamentalandroid.db

import android.provider.BaseColumns

class DatabaseContract: BaseColumns {
    companion object {
        const val TABLE_NAME = "tb_favorite_user"
        const val USERNAME = "username"
        const val USER_TYPE = "user_type"
        const val AVATAR_URL = "avatar_url"
    }
}