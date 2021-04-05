package com.mfahmi.myfundamentalandroid.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_favorite_user"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE ${DatabaseContract.TABLE_NAME} " +
                "($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${DatabaseContract.USERNAME} TEXT NOT NULL," +
                "${DatabaseContract.USER_TYPE} TEXT NOT NULL," +
                "${DatabaseContract.AVATAR_URL} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.TABLE_NAME}")
        onCreate(db)
    }
}