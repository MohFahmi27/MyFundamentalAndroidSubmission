package com.mfahmi.myfundamentalandroid.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.mfahmi.myfundamentalandroid.db.DatabaseContract.Companion.AUTHORITY
import com.mfahmi.myfundamentalandroid.db.DatabaseContract.Companion.CONTENT_URI
import com.mfahmi.myfundamentalandroid.db.DatabaseContract.Companion.TABLE_NAME
import com.mfahmi.myfundamentalandroid.db.UserFavoriteHelper

class UserFavoriteProvider : ContentProvider() {

    private lateinit var userFavoriteHelper: UserFavoriteHelper

    companion object {
        private const val USERS = 1
        private const val USER_ID = 2
        private const val TAG = "UserFavoriteProvider"

        private val myUriMather = UriMatcher(UriMatcher.NO_MATCH)

        init {
            myUriMather.addURI(AUTHORITY, TABLE_NAME, USERS)
            myUriMather.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
        }
    }

    override fun onCreate(): Boolean {
        userFavoriteHelper = UserFavoriteHelper.getInstance(context as Context)
        userFavoriteHelper.open()
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USERS) {
            myUriMather.match(uri) -> userFavoriteHelper.insert(values!!)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when(USER_ID) {
            myUriMather.match(uri) -> userFavoriteHelper.delete(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? = null

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        Log.d(TAG, "query: $uri")
        return when(myUriMather.match(uri)) {
            USERS -> userFavoriteHelper.queryAll()
            USER_ID -> {
                Log.d(TAG, "query: ${uri.lastPathSegment.toString()}")
                userFavoriteHelper.queryById(uri.lastPathSegment.toString())
            }
            else  -> null
        }
    }

}