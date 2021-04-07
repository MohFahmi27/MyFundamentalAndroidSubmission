package com.mfahmi.myfundamentalandroid.db

import android.net.Uri
import android.provider.BaseColumns

class DatabaseContract: BaseColumns {
    companion object {
        const val AUTHORITY = "com.mfahmi.myfundamentalandroid"
        const val SCHEME = "content"

        const val TABLE_NAME = "tb_favorite_user"
        const val USERNAME = "username"
        const val USER_TYPE = "user_type"
        const val AVATAR_URL = "avatar_url"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
    }
}